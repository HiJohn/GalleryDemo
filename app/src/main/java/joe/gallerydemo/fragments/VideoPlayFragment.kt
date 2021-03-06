package joe.gallerydemo.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.exoplayer2.PlaybackPreparer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.util.Util
import joe.gallerydemo.GalleryApp
import joe.gallerydemo.R
import joe.gallerydemo.activities.VideoPagerListActivity

import joe.gallerydemo.model.VideoInfo
import kotlinx.android.synthetic.main.item_exoplayer.*
import leakcanary.LeakSentry

const val TAG:String = "VideoPlayFragment"
//FIXME can`t stop player when vertical scrolled out
class VideoPlayFragment : Fragment() ,PlaybackPreparer{
    override fun preparePlayback() {
        player.retry()
    }

//    private val TAG:String = "VideoPlayFragment"

    private lateinit var videoInfo :VideoInfo
    private var position: Int = 0
    private lateinit var mediaSource: ProgressiveMediaSource
    private lateinit var player: SimpleExoPlayer
    private lateinit var activity: VideoPagerListActivity
    lateinit var app: GalleryApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = this.context?.applicationContext as GalleryApp

        arguments?.apply {
            videoInfo =this.getParcelable<VideoInfo>(ARG_VIDEO_INFO)?:VideoInfo()
            position = this.getInt(ARG_VIDEO_POSITION,0)
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is VideoPagerListActivity){
            activity = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.item_exoplayer, container, false)
    }

    private fun initPlayer(){

        player = activity.player
        player.playWhenReady = true
        val uri = Uri.parse(videoInfo.path)
        mediaSource = ProgressiveMediaSource.Factory(app.cacheDataSourceFactory).createMediaSource(uri)
        exoplayer_view.player = player

        exoplayer_view.requestFocus()

        player.prepare(mediaSource)
    }



    //===================================================================================

    override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initPlayer()
            exoplayer_view.onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        if (Util.SDK_INT <=23) {
            initPlayer()
            exoplayer_view.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
//        LogUtils.i(TAG," on Pause ")
        if (Util.SDK_INT <= 23) {
            exoplayer_view.onPause()
            releasePlayer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            exoplayer_view.onPause()
            releasePlayer()
        }
    }


    private fun releasePlayer() {
        player.playWhenReady = false
        exoplayer_view.player = null
    }


    override fun onDestroy() {
        super.onDestroy()
        LeakSentry.refWatcher.watch(this)
    }

//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//        LogUtils.i(TAG,"onHiddenChanged: $hidden, position:$position")
//    }

    //===================================================================================

    companion object {

        const val ARG_VIDEO_INFO  = "video_info"
        const val ARG_VIDEO_POSITION  = "video_pos"

        @JvmStatic
        fun newInstance(videoInfo: VideoInfo) =
                VideoPlayFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_VIDEO_INFO, videoInfo)
                    }
                }

        @JvmStatic
        fun newInstance(videoInfo: VideoInfo,position:Int) =
                VideoPlayFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_VIDEO_INFO, videoInfo)
                        putInt(ARG_VIDEO_POSITION,position)
                    }
                }
    }
}
