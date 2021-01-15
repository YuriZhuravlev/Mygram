package com.example.mygram.ui.screens.groups

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mygram.R
import com.example.mygram.database.*
import com.example.mygram.models.CommonModel
import com.example.mygram.utilits.APP_ACTIVITY
import com.example.mygram.utilits.AppValueEventListener
import com.example.mygram.utilits.hideKeyBoard
import com.example.mygram.utilits.showToast
import kotlinx.android.synthetic.main.fragment_add_contacts.*

class AddContactsFragment : Fragment(R.layout.fragment_add_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private val mRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
//    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Добавить участников"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyBoard()
        initRecyclerView()
        add_contacts_btn_next.setOnClickListener {
            showToast(listContacts.size.toString())
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = add_contact_recycler_view
        mAdapter = AddContactsAdapter()

        // 1 запрос
        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener {
            mListItems = it.children.map { it.getCommonModel() }
            mListItems.forEach { model ->

                // 2 запрос
                mRefUsers.child(model.id).addListenerForSingleValueEvent(AppValueEventListener {
                    val newModel = it.getCommonModel()

                    if (newModel.fullname.isEmpty()) {
                        newModel.fullname = newModel.phone
                    }
                    mAdapter.updateListItems(newModel)
                })
            }
        })

        mRecyclerView.adapter = mAdapter
    }

    companion object {
        val listContacts = mutableListOf<CommonModel>()
    }
}
