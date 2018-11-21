package joe.gallerydemo

import android.hardware.Camera
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.TextureView
import android.view.View
import android.widget.Button
import joe.gallerydemo.util.CameraHelper
import java.io.File
import java.io.IOException

class RecorderActivity : AppCompatActivity() {


    private var mCamera: Camera? = null
    private var mPreview: TextureView? = null
    private var mMediaRecorder: MediaRecorder? = null
    private var mOutputFile: File? = null

    private var isRecording = false
    private val TAG = "Recorder"
    private var captureButton: Button? = null

    companion object {

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recorder)
        mPreview = findViewById<TextureView>(R.id.surface_view)
        captureButton = findViewById<Button>(R.id.button_capture)
    }

    fun onCaptureClick(view: View) {
        if (isRecording) {
            // BEGIN_INCLUDE(stop_release_media_recorder)

            // stop recording and release camera
            try {
                mMediaRecorder?.stop()  // stop the recording
            } catch (e: RuntimeException) {
                // RuntimeException is thrown when stop() is called immediately after start().
                // In this case the output file is not properly constructed ans should be deleted.
                Log.d(TAG, "RuntimeException: stop() is called immediately after start()")

                mOutputFile?.delete()
            }

            releaseMediaRecorder() // release the MediaRecorder object
            mCamera?.lock()         // take camera access back from MediaRecorder

            // inform the user that recording has stopped
            setCaptureButtonText("Capture")
            isRecording = false
            releaseCamera()
            // END_INCLUDE(stop_release_media_recorder)

        } else {

            // BEGIN_INCLUDE(prepare_start_media_recorder)

            MediaPrepareTask().execute(null, null, null)

            // END_INCLUDE(prepare_start_media_recorder)

        }
    }

    private fun setCaptureButtonText(title: String) {
        captureButton?.text = title
    }

    override fun onPause() {
        super.onPause()
        // if we are using MediaRecorder, release it first
        releaseMediaRecorder()
        // release the camera immediately on pause event
        releaseCamera()
    }

    private fun releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            // clear recorder configuration
            mMediaRecorder?.reset()
            // release the recorder object
            mMediaRecorder?.release()
            mMediaRecorder = null
            // Lock camera for later use i.e taking it back from MediaRecorder.
            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
            mCamera?.lock()
        }
    }

    private fun releaseCamera() {
        if (mCamera != null) {
            // release the camera for other applications
            mCamera?.release()
            mCamera = null
        }
    }

    private fun prepareVideoRecorder(): Boolean {

        // BEGIN_INCLUDE (configure_preview)
        mCamera = CameraHelper.defaultCameraInstance

        // We need to make sure that our preview and recording video size are supported by the
        // camera. Query camera to find all the sizes and choose the optimal size given the
        // dimensions of our preview surface.
        val parameters = mCamera?.parameters
        val mSupportedPreviewSizes = parameters?.supportedPreviewSizes
        val mSupportedVideoSizes = parameters?.supportedVideoSizes
        val optimalSize = CameraHelper.getOptimalVideoSize(mSupportedVideoSizes,
                mSupportedPreviewSizes!!, mPreview!!.width, mPreview!!.height)

        // Use the same size for recording profile.
        val profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH)
        profile.videoFrameWidth = optimalSize!!.width
        profile.videoFrameHeight = optimalSize.height

        // likewise for the camera object itself.
        parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight)
        mCamera?.parameters = parameters
        try {
            // Requires API level 11+, For backward compatibility use {@link setPreviewDisplay}
            // with {@link SurfaceView}
            mCamera?.setPreviewTexture(mPreview?.getSurfaceTexture())
        } catch (e: IOException) {
            Log.e(TAG, "Surface texture is unavailable or unsuitable" + e.message)
            return false
        }

        // END_INCLUDE (configure_preview)


        // BEGIN_INCLUDE (configure_media_recorder)
        mMediaRecorder = MediaRecorder()

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera?.unlock()
        mMediaRecorder?.setCamera(mCamera)

        // Step 2: Set sources
        mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
        mMediaRecorder?.setVideoSource(MediaRecorder.VideoSource.CAMERA)

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        mMediaRecorder?.setProfile(profile)

        // Step 4: Set output file
        mOutputFile = CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_VIDEO)
        if (mOutputFile == null) {
            return false
        }
        mMediaRecorder?.setOutputFile(mOutputFile?.getPath())
        // END_INCLUDE (configure_media_recorder)

        // Step 5: Prepare configured MediaRecorder
        try {
            mMediaRecorder?.prepare()
        } catch (e: IllegalStateException) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.message)
            releaseMediaRecorder()
            return false
        } catch (e: IOException) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.message)
            releaseMediaRecorder()
            return false
        }

        return true
    }

    /**
     * Asynchronous task for preparing the [android.media.MediaRecorder] since it's a long blocking
     * operation.
     */
    internal inner class MediaPrepareTask : AsyncTask<Void, Void, Boolean>() {

        override fun doInBackground(vararg voids: Void): Boolean? {
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder?.start()

                isRecording = true
            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder()
                return false
            }
            return true
        }

        override fun onPostExecute(result: Boolean?) {
            if (!(result)!!) {
                this@RecorderActivity.finish()
            }
            // inform the user that recording has started
            setCaptureButtonText("Stop")

        }
    }

}
