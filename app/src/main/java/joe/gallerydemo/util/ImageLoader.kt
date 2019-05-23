package joe.gallerydemo.util

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import joe.gallerydemo.fragments.ImageFragment

fun ImageView.loadImage(uri: Uri){
    Glide.with(this)
            .load(uri).transition(withCrossFade(300))
            .apply(RequestOptions().skipMemoryCache(false).transform(MultiTransformation
            (CenterCrop(), RoundedCorners(4))))
            .into(this)
}

fun ImageView.loadImage(mUri: Uri?, context: ImageFragment) {
    Glide.with(context).load(mUri).into(this)
}
