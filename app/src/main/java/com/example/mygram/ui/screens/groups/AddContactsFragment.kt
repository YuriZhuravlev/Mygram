package com.example.mygram.ui.screens.groups

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.mygram.R
import com.example.mygram.database.*
import com.example.mygram.models.CommonModel
import com.example.mygram.ui.screens.base.BaseFragment
import com.example.mygram.utilits.*
import kotlinx.android.synthetic.main.fragment_add_contacts.*

class AddContactsFragment : BaseFragment(R.layout.fragment_add_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private val mRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private var mListItems = listOf<CommonModel>()

    override fun onResume() {
        listContacts.clear()
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.add_contacts_title)
        hideKeyBoard()
        initRecyclerView()
        add_contacts_btn_next.setOnClickListener {
            if (listContacts.isEmpty()) {
                showToast(getString(R.string.add_contact_toast_empty_list))
            } else {
                replaceFragment(CreateGroupFragment(listContacts))
            }

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
