package com.example.mygram.ui.fragments.single_chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mygram.R
import com.example.mygram.models.CommonModel
import com.example.mygram.database.CURRENT_UID
import com.example.mygram.utilits.DiffUtilCallback
import com.example.mygram.utilits.asTime
import kotlinx.android.synthetic.main.message_item.view.*


class SingleChatAdapter : RecyclerView.Adapter<SingleChatAdapter.SingleChatHolder>() {

    private var mListMessagesCache = mutableListOf<CommonModel>()
    private lateinit var mDiffResult: DiffUtil.DiffResult

    class SingleChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        val blockUserMessage: ConstraintLayout = view.bloc_user_message
        val chatUserMessage: TextView = view.chat_user_message
        val chatUserMessageTime: TextView = view.user_message_time

        val blockReceivedMessage: ConstraintLayout = view.bloc_received_message
        val chatReceivedMessage: TextView = view.chat_received_message
        val chatReceivedMessageTime: TextView = view.user_received_time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleChatHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        return SingleChatHolder(view)
    }

    override fun getItemCount(): Int = mListMessagesCache.size

    override fun onBindViewHolder(holder: SingleChatHolder, position: Int) {
        if (mListMessagesCache[position].from == CURRENT_UID) {
            holder.blockUserMessage.visibility = View.VISIBLE
            holder.blockReceivedMessage.visibility = View.GONE
            holder.chatUserMessage.text = mListMessagesCache[position].text
            holder.chatUserMessageTime.text =
                mListMessagesCache[position].timestamp.toString().asTime()
        } else {
            holder.blockUserMessage.visibility = View.GONE
            holder.blockReceivedMessage.visibility = View.VISIBLE
            holder.chatReceivedMessage.text = mListMessagesCache[position].text
            holder.chatReceivedMessageTime.text =
                mListMessagesCache[position].timestamp.toString().asTime()
        }
    }

    fun setList(list: List<CommonModel>) {

        //notifyDataSetChanged()
    }

    fun addItem(
        item: CommonModel,
        toBottom: Boolean,
        onSuccess: () -> Unit
    ) {
        if (toBottom) {
            if (!mListMessagesCache.contains(item)) {
                mListMessagesCache.add(item)
                notifyItemInserted(mListMessagesCache.size)
            }
        } else {
            if (!mListMessagesCache.contains(item)) {
                mListMessagesCache.add(item)
                mListMessagesCache.sortBy { it.timestamp.toString() }
                notifyItemInserted(0)
            }
        }
        onSuccess()
    }
}
