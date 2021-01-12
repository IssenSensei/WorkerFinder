package com.issen.workerfinder.utils

import android.content.res.ColorStateList
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.issen.workerfinder.R
import com.issen.workerfinder.WorkerFinderApplication
import com.issen.workerfinder.WorkerFinderApplication.Companion.currentLoggedInUserFull
import com.issen.workerfinder.database.models.*
import com.issen.workerfinder.enums.CompletionTypes
import com.issen.workerfinder.enums.DashboardNotificationTypes
import com.issen.workerfinder.enums.PriorityTypes

private var auth = FirebaseAuth.getInstance()

@BindingAdapter("photo")
fun setPhoto(imageView: ImageView, photo: String?) {
    Glide.with(imageView.context).load(photo).placeholder(R.drawable.meme)
        .into(imageView)
}

@BindingAdapter("conversationPhoto")
fun setConversationPhoto(imageView: ImageView, conversationsFull: ConversationsFull) {
    Glide.with(imageView.context).load(
        if (conversationsFull.firstUser.userId != currentLoggedInUserFull!!.userData.userId) {
            conversationsFull.firstUser.photo
        } else {
            conversationsFull.secondUser.photo
        }
    )
        .placeholder(R.drawable.meme)
        .into(imageView)
}

@BindingAdapter("conversationName")
fun setConversationName(textView: TextView, conversationsFull: ConversationsFull) {
    textView.text = if (conversationsFull.firstUser.userId != currentLoggedInUserFull!!.userData.userId) {
        conversationsFull.firstUser.userName
    } else {
        conversationsFull.secondUser.userName
    }
}

@BindingAdapter("messageSpacingLeft")
fun setMessageSpacingLeft(view: View, userId: String) {
    (view.layoutParams as ConstraintLayout.LayoutParams).horizontalWeight = if (userId != currentLoggedInUserFull!!.userData.userId) {
        0f
    } else {
        2f
    }
}

@BindingAdapter("messageSpacingRight")
fun setMessageSpacingRight(view: View, userId: String) {
    (view.layoutParams as ConstraintLayout.LayoutParams).horizontalWeight = if (userId != currentLoggedInUserFull!!.userData.userId) {
        2f
    } else {
        0f
    }
}

@BindingAdapter("messageGravity")
fun setMessageGravity(view: CardView, userId: String) {
    view.foregroundGravity = if (userId != currentLoggedInUserFull!!.userData.userId) {
        Gravity.START
    } else {
        Gravity.END
    }
}

@BindingAdapter("setNullableText")
fun setNullableText(view: TextView, string: String?) {
    view.text = if (string != "" && string != null) string else view.context.getString(R.string.no_data)
}

@BindingAdapter("taskWorkerText")
fun setTaskWorkerText(view: TextView, taskModelFull: TaskModelFull) {
    view.text =
        if (taskModelFull.task.workerFirebaseKey
            == auth.currentUser!!.uid
        )
            taskModelFull.taskUser!!.userName
        else
            taskModelFull.taskWorker!!.userName
}

@BindingAdapter("taskWorkerTextVisibility")
fun setTaskWorkerTextVisibility(view: TextView, taskModelFull: TaskModelFull) {
    view.visibility = if (taskModelFull.taskWorker!!.userId == taskModelFull.taskUser!!.userId) View.GONE else View.VISIBLE
}

@BindingAdapter("contactInviteVisibility", "userData")
fun setContactInviteVisibility(view: Button, isUserContact: Boolean, user: UserData) {
    view.visibility = if (isUserContact || user.userId == currentLoggedInUserFull!!.userData.userId) View.GONE else View.VISIBLE
}

@BindingAdapter("contactDeleteVisibility", "userData")
fun setContactDeleteVisibility(view: Button, isUserContact: Boolean, user: UserData) {
    view.visibility = if (isUserContact && user.userId != currentLoggedInUserFull!!.userData.userId) View.VISIBLE else View.GONE
}

@BindingAdapter("offerWorkVisibility")
fun setOfferWorkVisibility(view: Button, user: UserData) {
    view.visibility = if (user.isOpenForWork && user.userId != currentLoggedInUserFull!!.userData.userId) View.VISIBLE else View.GONE
}

@BindingAdapter("publicVisibility")
fun setPublicVisibility(view: Button, user: UserData) {
    view.visibility = if (!user.isAccountPublic && user.userId == currentLoggedInUserFull!!.userData.userId) View.VISIBLE else View.GONE
}

@BindingAdapter("privateVisibility")
fun setPrivateVisibility(view: Button, user: UserData) {
    view.visibility = if (user.isAccountPublic && user.userId == currentLoggedInUserFull!!.userData.userId) View.VISIBLE else View.GONE
}

@BindingAdapter("visibleIfCurrentUser")
fun setVisibleIfCurrentUser(view: Button, user: UserData) {
    view.visibility = if (user.userId != currentLoggedInUserFull!!.userData.userId) View.GONE else View.VISIBLE
}

@BindingAdapter("visibleIfNotCurrentUser")
fun setVisibleIfNotCurrentUser(view: View, user: UserData) {
    view.visibility = if (user.userId != currentLoggedInUserFull!!.userData.userId) View.VISIBLE else View.GONE
}

@BindingAdapter("taskBoardWorkerPickerVisibility")
fun setTaskBoardWorkerPickerVisibility(view: Button, taskModelFull: TaskModelFull) {
    view.visibility = if (taskModelFull.task.completed == "BOARD" && taskModelFull.task.userFirebaseKey == currentLoggedInUserFull!!.userData.userId) View.VISIBLE else View.GONE
}

@BindingAdapter("notificationWorkText")
fun setNotificationWorkText(textView: TextView, notificationFull: DashboardNotificationFull) {
    textView.text = when (notificationFull.notification.dashboardNotificationType) {
        DashboardNotificationTypes.WORKACCEPTED.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                if (notificationFull.notification.resolved) {
                    textView.context.getString(R.string.notification_work_accepted_current_user, notificationFull.task!!.taskTitle, notificationFull.userData.userName)
                } else {
                    textView.context.getString(R.string.notification_work_accepted, notificationFull.userData.userName, notificationFull.task!!.taskTitle)
                }
            } else {
                textView.context.getString(R.string.notification_work_accepted_current_user, notificationFull.task!!.taskTitle, currentLoggedInUserFull!!.userData.userName)
            }
        }
        DashboardNotificationTypes.WORKOFFERED.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                if (notificationFull.notification.resolved) {
                    textView.context.getString(R.string.notification_work_offered_current_user, notificationFull.userData.userName, notificationFull.task!!.taskTitle)
                } else {
                    textView.context.getString(R.string.notification_work_offered, notificationFull.userData.userName, notificationFull.task!!.taskTitle)
                }
            } else {
                textView.context.getString(R.string.notification_work_offered_current_user, currentLoggedInUserFull!!.userData.userName, notificationFull.task!!.taskTitle)
            }
        }
        DashboardNotificationTypes.WORKREFUSED.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                if (notificationFull.notification.resolved) {
                    textView.context.getString(R.string.notification_work_refused_current_user, notificationFull.task!!.taskTitle, notificationFull.userData.userName)
                } else {
                    textView.context.getString(R.string.notification_work_refused, notificationFull.userData.userName, notificationFull.task!!.taskTitle)
                }
            } else {
                textView.context.getString(R.string.notification_work_refused_current_user, notificationFull.task!!.taskTitle, currentLoggedInUserFull!!.userData.userName)
            }
        }
        DashboardNotificationTypes.WORKCANCELED.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                if (notificationFull.notification.resolved) {
                    textView.context.getString(R.string.notification_work_canceled_current_user, notificationFull.task!!.taskTitle, notificationFull.userData.userName)
                } else {
                    textView.context.getString(R.string.notification_work_canceled, notificationFull.userData.userName, notificationFull.task!!.taskTitle)
                }
            } else {
                textView.context.getString(R.string.notification_work_canceled_current_user, notificationFull.task!!.taskTitle, currentLoggedInUserFull!!.userData.userName)
            }
        }
        else -> {
            textView.context.getString(R.string.no_data_found_error)
        }
    }
}

@BindingAdapter("notificationRatingText")
fun setNotificationRatingText(textView: TextView, notificationFull: DashboardNotificationFull) {
    textView.text = when (notificationFull.notification.dashboardNotificationType) {
        DashboardNotificationTypes.RATEDBYUSER.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                textView.context.getString(R.string.notification_rating_user, notificationFull.userData.userName)
            } else {
                textView.context.getString(R.string.notification_rating_user_current_user, currentLoggedInUserFull!!.userData.userName)
            }
        }
        DashboardNotificationTypes.RATEDBYWORKER.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                textView.context.getString(R.string.notification_rating_worker, notificationFull.userData.userName)
            } else {
                textView.context.getString(R.string.notification_rating_worker_current_user, currentLoggedInUserFull!!.userData.userName)
            }
        }
        else -> {
            textView.context.getString(R.string.no_data_found_error)
        }
    }
}

@BindingAdapter("notificationTaskText")
fun setNotificationTaskText(textView: TextView, notificationFull: DashboardNotificationFull) {
    textView.text = when (notificationFull.notification.dashboardNotificationType) {
        DashboardNotificationTypes.TASKABANDONED.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                if (notificationFull.notification.resolved) {
                    textView.context.getString(R.string.notification_task_abandoned_current_user, notificationFull.task!!.taskTitle, notificationFull.userData.userName)
                } else {
                    textView.context.getString(R.string.notification_task_abandoned, notificationFull.userData.userName, notificationFull.task!!.taskTitle)
                }
            } else {
                textView.context.getString(R.string.notification_task_abandoned_current_user, notificationFull.task!!.taskTitle, currentLoggedInUserFull!!.userData.userName)
            }
        }
        DashboardNotificationTypes.TASKCOMPLETED.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                if (notificationFull.notification.resolved) {
                    textView.context.getString(R.string.notification_task_completed_current_user, notificationFull.task!!.taskTitle, notificationFull.userData.userName)
                } else {
                    textView.context.getString(R.string.notification_task_completed, notificationFull.userData.userName, notificationFull.task!!.taskTitle)
                }
            } else {
                textView.context.getString(R.string.notification_task_completed_current_user, notificationFull.task!!.taskTitle, currentLoggedInUserFull!!.userData.userName)
            }
        }
        DashboardNotificationTypes.TASKACCEPTED.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                if (notificationFull.notification.resolved) {
                    textView.context.getString(R.string.notification_task_accepted_current_user, notificationFull.task!!.taskTitle, notificationFull.userData.userName)
                } else {
                    textView.context.getString(R.string.notification_task_accepted, notificationFull.userData.userName, notificationFull.task!!.taskTitle)
                }
            } else {
                textView.context.getString(R.string.notification_task_accepted_current_user, notificationFull.task!!.taskTitle, currentLoggedInUserFull!!.userData.userName)
            }
        }
        DashboardNotificationTypes.TASKREJECTED.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                if (notificationFull.notification.resolved) {
                    textView.context.getString(R.string.notification_task_rejected_current_user, notificationFull.task!!.taskTitle, notificationFull.userData.userName)
                } else {
                    textView.context.getString(R.string.notification_task_rejected, notificationFull.userData.userName, notificationFull.task!!.taskTitle)
                }
            } else {
                textView.context.getString(R.string.notification_task_rejected_current_user, notificationFull.task!!.taskTitle, currentLoggedInUserFull!!.userData.userName)
            }
        }
        else -> {
            textView.context.getString(R.string.no_data_found_error)
        }
    }
}

@BindingAdapter("notificationContactText")
fun setNotificationContactText(textView: TextView, notificationFull: DashboardNotificationFull) {
    textView.text = when (notificationFull.notification.dashboardNotificationType) {
        DashboardNotificationTypes.CONTACTACCEPTED.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                if (notificationFull.notification.resolved) {
                    textView.context.getString(R.string.notification_contact_accepted_current_user, notificationFull.userData.userName)
                } else {
                    textView.context.getString(R.string.notification_contact_accepted, notificationFull.userData.userName)
                }
            } else {
                textView.context.getString(R.string.notification_contact_accepted_current_user, currentLoggedInUserFull!!.userData.userName)
            }
        }
        DashboardNotificationTypes.CONTACTCANCELED.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                if (notificationFull.notification.resolved) {
                    textView.context.getString(R.string.notification_contact_canceled_current_user, notificationFull.userData.userName)
                } else {
                    textView.context.getString(R.string.notification_contact_canceled, notificationFull.userData.userName)
                }
            } else {
                textView.context.getString(R.string.notification_contact_canceled_current_user, currentLoggedInUserFull!!.userData.userName)
            }
        }
        DashboardNotificationTypes.CONTACTINVITED.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                textView.context.getString(R.string.notification_contact_invited, notificationFull.userData.userName)
            } else {
                textView.context.getString(R.string.notification_contact_invited_current_user, currentLoggedInUserFull!!.userData.userName)
            }
        }
        DashboardNotificationTypes.CONTACTREFUSED.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                if (notificationFull.notification.resolved) {
                    textView.context.getString(R.string.notification_contact_refused_current_user, notificationFull.userData.userName)
                } else {
                    textView.context.getString(R.string.notification_contact_refused, notificationFull.userData.userName)
                }
            } else {
                textView.context.getString(R.string.notification_contact_refused_current_user, currentLoggedInUserFull!!.userData.userName)
            }
        }
        DashboardNotificationTypes.CONTACTREMOVED.toString() -> {
            if (notificationFull.userData.userId != currentLoggedInUserFull!!.userData.userId) {
                textView.context.getString(R.string.notification_contact_removed, notificationFull.userData.userName)
            } else {
                textView.context.getString(R.string.notification_contact_removed_current_user, currentLoggedInUserFull!!.userData.userName)
            }
        }
        else -> {
            textView.context.getString(R.string.no_data_found_error)
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
fun setCheckBoxVisibility(checkBox: CheckBox, task: TaskModel) {
    if (task.completed == CompletionTypes.ACTIVE.toString() && task.workerFirebaseKey == auth.currentUser!!.uid) {
        checkBox.visibility = View.VISIBLE
    } else {
        checkBox.visibility = View.INVISIBLE
    }
}

@BindingAdapter("taskStateIndicatorColor")
fun setTaskStateIndicatorColor(view: View, completionType: String) {
    when (completionType) {
        CompletionTypes.ABANDONED.toString() -> {
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorAbandon))
        }
        CompletionTypes.COMPLETED.toString() -> {
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorCompleted))
        }
        CompletionTypes.ACTIVE.toString() -> {
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorActive))
        }
        CompletionTypes.PENDING.toString() -> {
            view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorPending))
        }
    }
}


@BindingAdapter("cardStrokeColor")
fun setCheckBoxTint(cardView: CardView, completionType: String) {
    cardView.foreground = when (completionType) {
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