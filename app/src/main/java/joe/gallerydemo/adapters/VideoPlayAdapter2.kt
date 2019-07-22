package joe.gallerydemo.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import joe.gallerydemo.GalleryApp
import joe.gallerydemo.R
import joe.gallerydemo.model.VideoInfo


class VideoPlayAdapter2 :RecyclerView.Adapter<VideoHolder>() {


    var  videos = ArrayList<VideoInfo>()

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exoplayer,parent,false)

        return VideoHolder(view)

    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        holder.bind(videos[holder.adapterPosition])

    }


}

class VideoHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
    private val playerView: PlayerView = itemView.findViewById(R.id.exoplayer_view)
    private lateinit var videoInfo:VideoInfo
    lateinit var mediaSource: ProgressiveMediaSource
    lateinit var player: SimpleExoPlayer
    fun bind(vi:VideoInfo){
        videoInfo = vi
        buildMedia()
    }
    private fun buildMedia(){
        val uri = Uri.parse(videoInfo.path)
        mediaSource = ProgressiveMediaSource.Factory(GalleryApp.instance.cacheDataSourceFactory)
                .createMediaSource(uri)
    }
    fun preparePlay(){
        player = GalleryApp.instance.player
        playerView.player = player
        player.prepare(mediaSource)
        player.playWhenReady = true
    }

    fun detachPlayer(){
        player.playWhenReady = false
        playerView.player = null
    }

    fun pauseOrPlayVideo(playWhenReady:Boolean){
        player.playWhenReady = playWhenReady
    }
}