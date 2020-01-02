package joe.gallerydemo.util

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ThreadUtils
import joe.gallerydemo.GalleryApp
import joe.gallerydemo.model.VideoInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * Created by takashi on 2017/9/8.
 */

object VideoStoreUtil  {


    private val TAG = "VideoStoreUtil"

    suspend fun getImagesUri(): ArrayList<Uri> = withContext(Dispatchers.IO){
        LogUtils.i("curry"," thread :"+ThreadUtils.isMainThread());
        var context = GalleryApp.instance
        val imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = context.contentResolver.query(imgUri, null, null, null, null)
        val uris = ArrayList<Uri>()
        var mUri: Uri?
        if (cursor != null) {
            while (cursor.moveToNext()) {

                val id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID))
                mUri = Uri.withAppendedPath(imgUri, "" + id)

                uris.add(mUri)
            }
            cursor.close()
        }

         uris

    }


    fun extractMediaRotation(filePath: String): String {
        val metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource(filePath)

        return metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
    }


    suspend fun getImageUris(context: Context): ArrayList<Uri> {

        val imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = context.contentResolver.query(imgUri, null, null, null, null)
        val uris = ArrayList<Uri>()

        var mUri: Uri? = null
        if (cursor != null) {
            while (cursor.moveToNext()) {

                val data = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA))

                val id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID))
                mUri = Uri.withAppendedPath(imgUri, "" + id)

                uris.add(mUri)

            }
            cursor.close()
        }
        return uris
    }



    fun getAllMedia(context: Context): ArrayList<String>? {
        val videoItemHashSet = HashSet<String>()
        val projection = arrayOf(MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME)
        val cursor = context.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null)
        try {
            if (cursor == null) {
                return null
            }

            cursor.moveToFirst()
            do {
                videoItemHashSet.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)))
            } while (cursor.moveToNext())

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return ArrayList(videoItemHashSet)
    }

    fun getVideoInfoList(context: Context): ArrayList<VideoInfo> {
//        val videoItemHashSet = HashSet<VideoInfo>()
        var videoInfo: VideoInfo
        var videoInfoList = ArrayList<VideoInfo>()
        val cursor = context.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
        try {
            if (cursor == null) {
                return videoInfoList
            }
            cursor.moveToFirst()
            do {
                var path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media
                        .DATA))
                videoInfo = VideoInfo(path)
                videoInfo.displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video
                        .Media.DISPLAY_NAME))
                videoInfo.width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video
                        .Media.WIDTH))
                videoInfo.height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT))
                videoInfo.duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video
                        .Media.DURATION))

                videoInfoList.add(videoInfo)
//                val cat = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.CATEGORY))
            } while (cursor.moveToNext())

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        return videoInfoList
    }


}
