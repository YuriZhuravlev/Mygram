package com.example.mygram.ui.message_recycler_view.view_holder

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.mygram.database.CURRENT_UID
import com.example.mygram.database.getFileFromStorage
import com.example.mygram.ui.message_recycler_view.view.MessageView
import com.example.mygram.utilits.WRITE_FILES
import com.example.mygram.utilits.asTime
import com.example.mygram.utilits.checkPermission
import com.example.mygram.utilits.showToast
import kotlinx.android.synthetic.main.message_item_file.view.*
import java.io.File

class HolderFileMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {


    private val blockUserFileMessage: ConstraintLayout = view.bloc_user_file_message
    private val chatUserFileMessageTime: TextView = view.chat_user_file_time
    private val chatUserFileName: TextView = view.chat_user_filename
    private val chatUserDownload: ImageView = view.chat_user_btn_download
    private val chatUserProgressBar: ProgressBar = view.chat_user_progress_bar

    private val blockReceivedFileMessage: ConstraintLayout = view.bloc_received_file_message
    private val chatReceivedFileMessageTime: TextView = view.chat_received_file_time
    private val chatReceivedFileName: TextView = view.chat_received_filename
    private val chatReceivedDownload: ImageView = view.chat_received_btn_download
    private val chatReceivedProgressBar: ProgressBar = view.chat_received_progress_bar

    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockReceivedFileMessage.visibility = View.GONE
            blockUserFileMessage.visibility = View.VISIBLE
            chatUserFileMessageTime.text =
                view.timestamp.asTime()
            chatUserFileName.text = view.text
        } else {
            blockUserFileMessage.visibility = View.GONE
            blockReceivedFileMessage.visibility = View.VISIBLE
            chatReceivedFileMessageTime.text =
                view.timestamp.asTime()
            chatReceivedFileName.text = view.text
        }
    }

    override fun onAttach(view: MessageView) {
        if (view.from == CURRENT_UID) {
            chatUserDownload.setOnClickListener { clickToBtnFile(view) }
        }
        else {
            chatReceivedDownload.setOnClickListener { clickToBtnFile(view) }
        }
    }

    private fun clickToBtnFile(view: MessageView) {
        if (view.from == CURRENT_UID) {
            chatUserDownload.visibility = View.INVISIBLE
            chatUserProgressBar.visibility = View.VISIBLE
        } else {
            chatReceivedDownload.visibility = View.INVISIBLE
            chatReceivedProgressBar.visibility = View.VISIBLE
        }
        val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        view.text)
        try {
            if (checkPermission(WRITE_FILES)) {
                file.createNewFile()
                getFileFromStorage(file, view.fileUrl) {
                    if (view.from == CURRENT_UID) {
                        chatUserDownload.visibility = View.VISIBLE
                        chatUserProgressBar.visibility = View.INVISIBLE
                    } else {
                        chatReceivedDownload.visibility = View.VISIBLE
                        chatReceivedProgressBar.visibility = View.INVISIBLE
                    }
                }
            }
        }catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    override fun onDetach() {
        chatUserDownload.setOnClickListener(null)
        chatReceivedDownload.setOnClickListener(null)
    }
}