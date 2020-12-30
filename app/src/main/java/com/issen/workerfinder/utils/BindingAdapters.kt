package com.issen.workerfinder.utils

import android.content.res.ColorStateList
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.database.models.DashboardNotificationFull
import com.issen.workerfinder.database.models.TaskModelFull
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.enums.DashboardNotificationTypes
import com.issen.workerfinder.enums.PriorityTypes

@BindingAdapter("photo")
fun setPhoto(imageView: ImageView, photo: String?) {
    Glide.with(imageView.context).load(photo).placeholder(R.drawable.meme)
        .into(imageView)
}

@BindingAdapter("notificationWorkText")
fun setNotificationWorkText(textView: TextView, notificationFull: DashboardNotificationFull) {
    textView.text = "Użytkownik ${notificationFull.userData?.userName}" + when (notificationFull.notification.dashboardNotificationType) {
        DashboardNotificationTypes.WORKACCEPTED.toString() -> {
            " przyjął Twoją ofertę pracy."
        }
        DashboardNotificationTypes.WORKOFFERED.toString() -> {
            " wysyła Ci ofertę pracy."
        }
        DashboardNotificationTypes.WORKREFUSED.toString() -> {
            " odrzucił Twoją ofertę pracy."
        }
        DashboardNotificationTypes.WORKCANCELED.toString() -> {
            " anulował wysłaną Ci ofertę pracy."
        }
        else -> {
            "Error, no data found!"
        }
    }
}

@BindingAdapter("notificationRatingText")
fun setNotificationRatingText(textView: TextView, notificationFull: DashboardNotificationFull) {
    textView.text = "Użytkownik " + notificationFull.userData?.userName + when (notificationFull.notification.dashboardNotificationType) {
        DashboardNotificationTypes.RATEDBYUSER.toString() -> {
            " ocenił Cię jako pracownika."
        }
        DashboardNotificationTypes.RATEDBYWORKER.toString() -> {
            " ocenił Cię jako pracodawcę."
        }
        else -> {
            "Error, no data found!"
        }
    }
}

@BindingAdapter("notificationTaskText")
fun setNotificationTaskText(textView: TextView, notificationFull: DashboardNotificationFull) {
    textView.text = "Użytkownik " + notificationFull.userData?.userName + when (notificationFull.notification.dashboardNotificationType) {
        DashboardNotificationTypes.TASKABANDONED.toString() -> {
            " porzucił wykonanie przyjętego od Ciebie zadania."
        }
        DashboardNotificationTypes.TASKCOMPLETED.toString() -> {
            " wykonał przyjęte od Ciebie zadanie."
        }
        DashboardNotificationTypes.TASKACCEPTED.toString() -> {
            " zatwierdził poprawne wykonanie zadania."
        }
        DashboardNotificationTypes.TASKREJECTED.toString() -> {
            " odrzucił Twoją decyzję o wykonaniu zadania."
        }
        else -> {
            "Error, no data found!"
        }
    }
}

@BindingAdapter("notificationContactText")
fun setNotificationContactText(textView: TextView, notificationFull: DashboardNotificationFull) {
    textView.text = "Użytkownik " + notificationFull.userData?.userName + when (notificationFull.notification.dashboardNotificationType) {
        DashboardNotificationTypes.CONTACTACCEPTED.toString() -> {
            " przyjął Twoje zaproszenie do kontaktów."
        }
        DashboardNotificationTypes.CONTACTCANCELED.toString() -> {
            " anulował zaproszenie do kontaktów."
        }
        DashboardNotificationTypes.CONTACTINVITED.toString() -> {
            " chce nawiązać z Tobą kontakt."
        }
        DashboardNotificationTypes.CONTACTREFUSED.toString() -> {
            " odrzucił Twoje zaproszenie do kontaktów."
        }
        DashboardNotificationTypes.CONTACTREMOVED.toString() -> {
            " usunął Cię ze swojej listy kontaktów."
        }
        DashboardNotificationTypes.CONTACTPENDING.toString() -> {
            " otrzymał Twoje zaproszenie do kontaktów."
        }
        else -> {
            "Error, no data found!"
        }
    }
}

@BindingAdapter("checkButtonTint")
fun setCheckBoxTint(checkBox: CheckBox, priority: String) {
    val states = arrayOf(
        intArrayOf(-android.R.attr.state_checked)
    )
    when (priority) {
        PriorityTypes.URGENT.toString() -> {
            checkBox.buttonTintList =
                ColorStateList(states, intArrayOf(ContextCompat.getColor(WorkerFinderApplication.appContext, R.color.colorUrgent)))
        }
        PriorityTypes.HIGH.toString() -> {
            checkBox.buttonTintList =
                ColorStateList(states, intArrayOf(ContextCompat.getColor(WorkerFinderApplication.appContext, R.color.colorHigh)))
        }
        PriorityTypes.NORMAL.toString() -> {
            checkBox.buttonTintList =
                ColorStateList(states, intArrayOf(ContextCompat.getColor(WorkerFinderApplication.appContext, R.color.colorNormal)))
        }
        PriorityTypes.LOW.toString() -> {
            checkBox.buttonTintList =
                ColorStateList(states, intArrayOf(ContextCompat.getColor(WorkerFinderApplication.appContext, R.color.colorLow)))
        }
        PriorityTypes.OTHER.toString() -> {
            checkBox.buttonTintList =
                ColorStateList(states, intArrayOf(ContextCompat.getColor(WorkerFinderApplication.appContext, R.color.colorOther)))
        }
        else -> {
            0
        }
    }
}

@BindingAdapter("checkButtonVisibility")
fun setCheckBoxVisibility(checkBox: CheckBox, completion: String) {
    if (completion == CompletionTypes.ACTIVE.toString()) {
        checkBox.visibility = View.VISIBLE
    } else {
        checkBox.visibility = View.INVISIBLE
    }
}

@BindingAdapter("cardStrokeColor")
fun setCheckBoxTint(cardView: CardView, taskFull: TaskModelFull) {
    cardView.foreground = when (taskFull.task.completed) {
        CompletionTypes.ABANDONED.toString() -> {
            ContextCompat.getDrawable(WorkerFinderApplication.appContext, R.drawable.rectangle_red_stroke)
        }
        CompletionTypes.COMPLETED.toString() -> {
            ContextCompat.getDrawable(WorkerFinderApplication.appContext, R.drawable.rectangle_green_stroke)
        }
        CompletionTypes.ACTIVE.toString() -> {
            ContextCompat.getDrawable(WorkerFinderApplication.appContext, R.drawable.rectangle_blue_stroke)
        }
        CompletionTypes.PENDING.toString() -> {
            ContextCompat.getDrawable(WorkerFinderApplication.appContext, R.drawable.rectangle_yellow_stroke)
        }
        else -> {
            ContextCompat.getDrawable(WorkerFinderApplication.appContext, R.drawable.rectangle_no_stroke)
        }
    }
}
