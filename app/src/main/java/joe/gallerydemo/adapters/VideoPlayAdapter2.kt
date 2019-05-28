package joe.gallerydemo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.ui.PlayerView
import joe.gallerydemo.R
import joe.gallerydemo.model.VideoInfo


class VideoPlayAdapter2 :RecyclerView.Adapter<VideoPlayAdapter2.VideoHolder>() {


    var  videoDatas = ArrayList<VideoInfo>()

    override fun getItemCount(): Int {
        return videoDatas.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exoplayer,parent,false)

        return VideoHolder(view)

    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onViewRecycled(holder: VideoHolder) {
        super.onViewRecycled(holder)
        
    }




    class VideoHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val playerView: PlayerView = itemView.findViewById(R.id.exoplayer_view)
    }
}
