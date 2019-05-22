package joe.gallerydemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import joe.gallerydemo.model.VideoInfo

class VideoAdapter : RecyclerView.Adapter<VideoAdapter.VideoHolder>() {

//    private var videoInfos :ArrayList<VideoInfo> = ArrayList()
//
//    constructor(videoInfos: ArrayList<VideoInfo>){
//
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video_thumb,parent,false)
        return VideoHolder(view)
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 0
    }



    class VideoHolder constructor(itemView : View) : RecyclerView.ViewHolder(itemView) {



    }
}