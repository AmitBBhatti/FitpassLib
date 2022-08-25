package com.fitpass.libfitpass.base.CustomAdapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.fitpass.libfitpass.R

@BindingAdapter("loadImage")
fun setImageView(imgView: ImageView?, imageUrl:String?)
{
    if(imageUrl!=null) {
        Glide.with(imgView!!.context)
            .load(imageUrl)
            .fitCenter()
            .placeholder(R.drawable.placeholder)
            .into(imgView!!)
    }

}

@BindingAdapter("loadImagenoplaceholder")
fun setImageViewNoPlaceholder(imgView: ImageView?, imageUrl:String?)
{
    if(imageUrl!=null) {
        Glide.with(imgView!!.context)
            .load(imageUrl)
            .fitCenter()
            .into(imgView!!)
    }

}
