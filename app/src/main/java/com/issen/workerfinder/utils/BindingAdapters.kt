package com.issen.workerfinder.utils

import android.content.res.ColorStateList
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.issen.workerfinder.R
import com.issen.workerfinder.TaskApplication
import com.issen.workerfinder.database.models.FullTaskModel
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.enums.PriorityTypes

@BindingAdapter("photo")
fun setPhoto(imageView: ImageView, photo: String) {
    Glide.with(imageView.context).load(photo).placeholder(R.drawable.meme)
        .into(imageView)
}

@BindingAdapter("checkButtonTint")
fun setCheckBoxTint(checkBox: CheckBox, priority: String) {
    val states = arrayOf(
        intArrayOf(-android.R.attr.state_checked)
    )
    when (priority) {
        PriorityTypes.URGENT.toString() -> {
            checkBox.buttonTintList =
                ColorStateList(states, intArrayOf(ContextCompat.getColor(TaskApplication.appContext, R.color.colorUrgent)))
        }
        PriorityTypes.HIGH.toString() -> {
            checkBox.buttonTintList =
                ColorStateList(states, intArrayOf(ContextCompat.getColor(TaskApplication.appContext, R.color.colorHigh)))
        }
        PriorityTypes.NORMAL.toString() -> {
            checkBox.buttonTintList =
                ColorStateList(states, intArrayOf(ContextCompat.getColor(TaskApplication.appContext, R.color.colorNormal)))
        }
        PriorityTypes.LOW.toString() -> {
            checkBox.buttonTintList =
                ColorStateList(states, intArrayOf(ContextCompat.getColor(TaskApplication.appContext, R.color.colorLow)))
        }
        PriorityTypes.OTHER.toString() -> {
            checkBox.buttonTintList =
                ColorStateList(states, intArrayOf(ContextCompat.getColor(TaskApplication.appContext, R.color.colorOther)))
        }
        else -> {
            0
        }
    }
}

@BindingAdapter("checkButtonVisibility")
fun setCheckBoxVisibility(checkBox: CheckBox, completion: String) {
    if(completion == CompletionTypes.ACTIVE.toString()){
        checkBox.visibility = View.VISIBLE
    } else {
        checkBox.visibility = View.INVISIBLE
    }
}

@BindingAdapter("cardStrokeColor")
fun setCheckBoxTint(cardView: CardView, task: FullTaskModel) {
    cardView.foreground = when(task.task.completed){
        CompletionTypes.ABANDONED.toString() -> {
            ContextCompat.getDrawable(TaskApplication.appContext, R.drawable.rectangle_red_stroke)
        }
        CompletionTypes.COMPLETED.toString() -> {
            ContextCompat.getDrawable(TaskApplication.appContext, R.drawable.rectangle_green_stroke)
        }
        CompletionTypes.ACTIVE.toString() -> {
            ContextCompat.getDrawable(TaskApplication.appContext, R.drawable.rectangle_blue_stroke)
        }
        CompletionTypes.PENDING.toString() -> {
            ContextCompat.getDrawable(TaskApplication.appContext, R.drawable.rectangle_yellow_stroke)
        }
        else -> {
            ContextCompat.getDrawable(TaskApplication.appContext, R.drawable.rectangle_no_stroke)
        }
    }
}
