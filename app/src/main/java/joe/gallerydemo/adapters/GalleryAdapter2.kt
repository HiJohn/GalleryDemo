package joe.gallerydemo.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import joe.gallerydemo.R
import joe.gallerydemo.util.loadImage

class GalleryAdapter2 :RecyclerView.Adapter<GalleryHolder>() {


    var mUris:ArrayList<Uri> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
        return GalleryHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image,
                parent,false))
    }

    override fun onBindViewHolder(holder: GalleryHolder, position: Int) {

        holder.image.loadImage(mUris[holder.adapterPosition])
    }

    override fun getItemCount(): Int {
        return mUris.size
    }

}

class GalleryHolder constructor(itemView:View):RecyclerView.ViewHolder (itemView){

    var image:ImageView = itemView.findViewById(R.id.image_show)
}