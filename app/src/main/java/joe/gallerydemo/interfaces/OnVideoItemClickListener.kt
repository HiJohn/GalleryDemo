package joe.gallerydemo.interfaces

import android.net.Uri
import joe.gallerydemo.model.VideoInfo

interface OnVideoItemClickListener {

    fun onVideoItemClick(videoInfo: VideoInfo,uri: Uri)

}