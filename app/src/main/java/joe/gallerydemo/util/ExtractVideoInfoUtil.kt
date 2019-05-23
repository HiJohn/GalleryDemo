package joe.gallerydemo.util

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.text.TextUtils
import com.blankj.utilcode.util.LogUtils
import joe.gallerydemo.model.VideoInfo


import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.ArrayList
import java.util.HashSet


object ExtractVideoInfoUtil {


    /**
     * 获取视频某一帧,不一定是关键帧，不耗时
     *
     * @param timeMs 毫秒
     */
    fun extractFrame(file: String, timeMs: Long): Bitmap? {
        val mMetadataRetriever = MediaMetadataRetriever()
        mMetadataRetriever.setDataSource(file)
        val duration = mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val fileLength = java.lang.Long.valueOf(duration)
        //第一个参数是传入时间，只能是us(微秒)
        //OPTION_CLOSEST ,在给定的时间，检索最近一个帧,这个帧不一定是关键帧。
        //OPTION_CLOSEST_SYNC   在给定的时间，检索最近一个同步与数据源相关联的的帧（关键帧）
        //OPTION_NEXT_SYNC 在给定时间之后检索一个同步与数据源相关联的关键帧。
        //OPTION_PREVIOUS_SYNC  顾名思义，同上
        //        Bitmap bitmap = mMetadataRetriever.getFrameAtTime(timeMs * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
        var bitmap: Bitmap? = null
        var i = timeMs
        while (i < fileLength) {
            bitmap = mMetadataRetriever.getFrameAtTime(i * 1000, MediaMetadataRetriever.OPTION_CLOSEST)
            if (bitmap != null) {
                break
            }
            i += 1000
        }
        return bitmap
    }


    fun getVideoInfoFromPath(filePath: String): VideoInfo {


        val mMetadataRetriever = MediaMetadataRetriever()
        mMetadataRetriever.setDataSource(filePath)

        val rotation = mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)
        val width = mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
        val height = mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
        val duration = mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val mimeType = mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
        val bitRate = mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)
        val videoInfo = VideoInfo(filePath)
        videoInfo.path = filePath
        try {

            videoInfo.width = width.toInt()
            videoInfo.height = height.toInt()
            videoInfo.duration = duration.toLong()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return videoInfo
    }


    fun getVideoInfoList(context: Context): ArrayList<VideoInfo>? {
        val videoItemHashSet = HashSet<VideoInfo>()
        var videoInfo: VideoInfo
        val cursor = context.contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null)
        try {
            if (cursor == null) {
                return null
            }
            cursor.moveToFirst()
            do {
                videoInfo = VideoInfo()
                videoInfo.path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                videoInfo.width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.WIDTH))
                videoInfo.height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.HEIGHT))
                videoInfo.duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)).toLong()
                videoItemHashSet.add(videoInfo)
            } while (cursor.moveToNext())

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return ArrayList(videoItemHashSet)
    }

    fun saveImage(bmp: Bitmap?, dirPath: String): String {
        if (bmp == null) {
            return ""
        }
        val appDir = File(dirPath)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fileName = "video_thumb_" + System.currentTimeMillis() + "_upload.jpg"
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath
    }

    fun saveImageToSD(bmp: Bitmap?, dirPath: String): String {
        if (bmp == null) {
            return ""
        }
        val appDir = File(dirPath)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath
    }


    fun saveImageToSDForEdit(bmp: Bitmap?, dirPath: String, fileName: String): String {
        if (bmp == null) {
            return ""
        }
        val appDir = File(dirPath)
        if (!appDir.exists()) {
            appDir.mkdir()
        }
        val file = File(appDir, fileName)
        try {
            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, fos)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return file.absolutePath
    }

}
