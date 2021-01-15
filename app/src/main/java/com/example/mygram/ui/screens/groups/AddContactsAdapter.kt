package com.example.mygram.ui.screens.groups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mygram.R
import com.example.mygram.models.CommonModel
import com.example.mygram.utilits.downloadAndSetImage
import com.example.mygram.utilits.showToast
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.add_contact_item.view.*

class AddContactsAdapter : RecyclerView.Adapter<AddContactsAdapter.AddContactsHolder>() {

    private val listItems = mutableListOf<CommonModel>()

    class AddContactsHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.add_contact_item_name
        val itemStatus: TextView = view.add_contact_item_status
        val itemPhoto: CircleImageView = view.add_contact_item_photo
        val itemChoice: CircleImageView = view.add_contact_item_choice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddContactsHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.add_contact_item, parent, false)
        val holder = AddContactsHolder(view)
        holder.itemView.setOnClickListener {
            if (listItems[holder.adapterPosition].isChoice) {
                holder.itemChoice.visibility = View.INVISIBLE
                listItems[holder.adapterPosition].isChoice = false
                AddContactsFragment.listContacts.remove(listItems[holder.adapterPosition])
            } else {
                holder.itemChoice.visibility = View.VISIBLE
                listItems[holder.adapterPosition].isChoice = true
                AddContactsFragment.listContacts.add(listItems[holder.adapterPosition])
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: AddContactsHolder, position: Int) {
        holder.itemName.text = listItems[position].fullname
        holder.itemStatus.text = listItems[position].state
        holder.itemPhoto.downloadAndSetImage(listItems[position].photoURL)
    }

    override fun getItemCount(): Int = listItems.size

    fun updateListItems(item: CommonModel) {
        listItems.add(item)
        notifyItemInserted(listItems.size)
    }
}