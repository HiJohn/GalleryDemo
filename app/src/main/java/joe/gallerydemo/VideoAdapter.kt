package joe.gallerydemo

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import joe.gallerydemo.model.VideoInfo
import joe.gallerydemo.util.loadImage
import java.io.File

class VideoAdapter : RecyclerView.Adapter<VideoAdapter.VideoHolder>() {

    var videoInfos :ArrayList<VideoInfo> = ArrayList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video_thumb,parent,false)
        return VideoHolder(view)
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        val videoInfo:VideoInfo = videoInfos[position]
        var file = File(videoInfo.path)
        holder.name.text = videoInfo.displayName
        holder.thumb.loadImage(Uri.fromFile(file))
        holder.itemView.setOnClickListener {
            ToastUtils.showLong(videoInfo.path)
        }
    }

    override fun getItemCount(): Int {
        return videoInfos.size
    }


    class VideoHolder constructor(itemView : View) : RecyclerView.ViewHolder(itemView) {

        val thumb:ImageView = itemView.findViewById(R.id.video_thumb)
        val name:TextView = itemView.findViewById(R.id.video_name)

    }
}