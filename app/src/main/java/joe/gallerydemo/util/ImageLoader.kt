package joe.gallerydemo.util

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import joe.gallerydemo.R
import joe.gallerydemo.fragments.ImageFragment
import java.io.File

fun ImageView.loadImage(uri: Uri){
    Glide.with(this)
            .load(uri).transition(withCrossFade(300))
            .placeholder(R.drawable.transparent)
            .fallback(R.drawable.transparent)
            .transition(withCrossFade(300))
            .apply(RequestOptions().skipMemoryCache(false).transform(MultiTransformation
            (CenterCrop(), RoundedCorners(4))))
            .into(this)
}

fun ImageView.loadImage(url:String){
    Glide.with(this)
            .load(url).transition(withCrossFade(300))
            .placeholder(R.drawable.transparent)
            .fallback(R.drawable.transparent)
            .transition(withCrossFade(300))
            .apply(RequestOptions().skipMemoryCache(false).transform(MultiTransformation
            (CenterCrop(), RoundedCorners(4))))
            .into(this)
}

fun ImageView.loadImage(file:File){
    Glide.with(this)
            .load(file).transition(withCrossFade(300))
            .placeholder(R.drawable.transparent)
            .fallback(R.drawable.transparent)
            .transition(withCrossFade(300))
            .apply(RequestOptions().skipMemoryCache(false).transform(MultiTransformation
            (CenterCrop(), RoundedCorners(4))))
            .into(this)
}

fun ImageView.loadImage(resId:Int){
    Glide.with(this)
            .load(resId).transition(withCrossFade(300))
            .placeholder(R.drawable.transparent)
            .fallback(R.drawable.transparent)
            .transition(withCrossFade(300))
            .apply(RequestOptions().skipMemoryCache(false).transform(MultiTransformation
            (CenterCrop(), RoundedCorners(4))))
            .into(this)
}

fun ImageView.loadImageCenterCrop(uri: Uri){
    Glide.with(this)
            .load(uri).transition(withCrossFade(300))
            .placeholder(R.drawable.transparent)
            .fallback(R.drawable.image_bg_default)
            .transition(withCrossFade(300))
            .centerCrop()
//            .fitCenter()
            .into(this)
}

fun ImageView.loadImage(mUri: Uri?, context: ImageFragment) {
    Glide.with(context).load(mUri).into(this)
}
