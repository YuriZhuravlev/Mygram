package com.example.mygram.ui.screens.main_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mygram.R
import com.example.mygram.models.CommonModel
import com.example.mygram.ui.screens.groups.GroupChatFragment
import com.example.mygram.ui.screens.single_chat.SingleChatFragment
import com.example.mygram.utilits.TYPE_CHAT
import com.example.mygram.utilits.TYPE_GROUP
import com.example.mygram.utilits.downloadAndSetImage
import com.example.mygram.utilits.replaceFragment
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.main_list_item.view.*

class MainListAdapter : RecyclerView.Adapter<MainListAdapter.MainListHolder>() {

    private val listItems = mutableListOf<CommonModel>()

    class MainListHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.main_list_item_name
        val itemLastMessage: TextView = view.main_list_item_last_message
        val itemPhoto: CircleImageView = view.main_list_item_photo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        val holder = MainListHolder(view)
        holder.itemView.setOnClickListener {
            when(listItems[holder.adapterPosition].type) {
                TYPE_CHAT -> replaceFragment(SingleChatFragment(listItems[holder.adapterPosition]))
                TYPE_GROUP -> replaceFragment(GroupChatFragment(listItems[holder.adapterPosition]))
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: MainListHolder, position: Int) {
        holder.itemName.text = listItems[position].fullname
        holder.itemLastMessage.text = listItems[position].lastMessage
        holder.itemPhoto.downloadAndSetImage(listItems[position].photoURL)
    }

    override fun getItemCount(): Int = listItems.size

    fun updateListItems(item: CommonModel) {
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }
}