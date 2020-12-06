package com.issen.workerfinder.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.issen.workerfinder.R

@BindingAdapter("photo")
fun setPhoto(imageView: ImageView, photo: String) {
    Glide.with(imageView.context).load(photo).placeholder(R.drawable.meme)
        .into(imageView)
}





