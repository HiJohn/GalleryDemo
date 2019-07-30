package joe.gallerydemo.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import joe.gallerydemo.R
import joe.gallerydemo.model.VideoInfo

class VideoPlayAdapter3 :RecyclerView.Adapter<VideoHolder3>(){

    var  videos = ArrayList<VideoInfo>()

    override fun getItemCount(): Int {
        return videos.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder3 {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video_view,parent,false)
        return VideoHolder3(view)
    }


    override fun onBindViewHolder(holder: VideoHolder3, position: Int) {
        holder.bind(videos[holder.adapterPosition])
    }


    override fun onViewRecycled(holder: VideoHolder3) {
        holder.stopPlayback()
        super.onViewRecycled(holder)
    }

}

class VideoHolder3(itemView: View) : RecyclerView.ViewHolder(itemView){
    private lateinit var videoInfo: VideoInfo
    private val videoView: VideoView = itemView.findViewById(R.id.video_view)
    private lateinit var uri: Uri
    fun bind(vi:VideoInfo){
        videoInfo = vi
        buildMedia()
    }


    private fun buildMedia(){
        uri = Uri.parse(videoInfo.path)
        videoView.setVideoURI(uri)
    }

    fun resume(){
        videoView.resume()
    }

    fun isPlaying():Boolean{
        return videoView.isPlaying
    }


    fun startPlay(){
        if (videoView.isPlaying) {
            videoView.resume()
        }else{
            videoView.start()
        }
    }
    fun pause(){
        videoView.pause()
    }

    fun stopPlayback(){
        videoView.stopPlayback()
    }

}