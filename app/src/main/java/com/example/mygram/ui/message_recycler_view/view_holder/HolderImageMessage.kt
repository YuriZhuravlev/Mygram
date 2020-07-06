package com.example.mygram.ui.message_recycler_view.view_holder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mygram.database.CURRENT_UID
import com.example.mygram.ui.message_recycler_view.view.MessageView
import com.example.mygram.utilits.asTime
import com.example.mygram.utilits.downloadAndSetImage
import kotlinx.android.synthetic.main.message_item_image.view.*

class HolderImageMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {
    private val blockUserImageMessage: ConstraintLayout = view.bloc_user_image_message
    private val chatUserImageMessage: ImageView = view.chat_user_image
    private val chatUserImageMessageTime: TextView = view.chat_user_image_time

    private val blockReceivedImageMessage: ConstraintLayout = view.bloc_received_image_message
    private val chatReceivedImageMessage: ImageView = view.chat_received_image
    private val chatReceivedImageMessageTime: TextView = view.chat_received_image_time

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockReceivedImageMessage.visibility = View.GONE
            blockUserImageMessage.visibility = View.VISIBLE
            chatUserImageMessage.downloadAndSetImage(view.fileUrl)
            chatUserImageMessageTime.text =
                view.timestamp.asTime()
        } else {
            blockUserImageMessage.visibility = View.GONE
            blockReceivedImageMessage.visibility = View.VISIBLE
            chatReceivedImageMessage.downloadAndSetImage(view.fileUrl)
            chatReceivedImageMessageTime.text =
                view.timestamp.asTime()
        }
    }

    override fun onAttach(view: MessageView) {

    }

    override fun onDetach() {

    }
}