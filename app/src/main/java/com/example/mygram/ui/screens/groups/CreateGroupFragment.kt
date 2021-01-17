package com.example.mygram.ui.screens.groups

import androidx.recyclerview.widget.RecyclerView
import com.example.mygram.R
import com.example.mygram.models.CommonModel
import com.example.mygram.ui.screens.base.BaseFragment
import com.example.mygram.utilits.*
import kotlinx.android.synthetic.main.fragment_create_group.*

class CreateGroupFragment(private var listContacts: List<CommonModel>) :
    BaseFragment(R.layout.fragment_create_group) {
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.create_group_title)
        initRecyclerView()
        create_group_btn_complete.setOnClickListener {
            showToast("Click")
        }
        create_group_input_name.requestFocus()
        create_group_counts.text = getPlurals(listContacts.size)
    }

    private fun initRecyclerView() {
        mRecyclerView = create_group_recycler_view
        mAdapter = AddContactsAdapter()
        listContacts.forEach {
            mAdapter.updateListItems(it)
        }
        mRecyclerView.adapter = mAdapter
    }
}