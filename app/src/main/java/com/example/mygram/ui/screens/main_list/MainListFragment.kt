package com.example.mygram.ui.screens.main_list

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mygram.R
import com.example.mygram.database.*
import com.example.mygram.models.CommonModel
import com.example.mygram.utilits.*
import kotlinx.android.synthetic.main.fragment_main_list.*

class MainListFragment : Fragment(R.layout.fragment_main_list) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MainListAdapter
    private val mRefMainList = REF_DATABASE_ROOT.child(NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefGroups = REF_DATABASE_ROOT.child(NODE_GROUPS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)
    private var mListItems = listOf<CommonModel>()

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Mygram"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyBoard()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        mRecyclerView = main_list_recycler_view
        mAdapter = MainListAdapter()

        // 1 запрос
        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener {
            mListItems = it.children.map { it.getCommonModel() }
            mListItems.forEach { model ->

                when (model.type) {
                    TYPE_CHAT -> {
                        showChat(model)
                    }
                    TYPE_GROUP -> {
                        showGroup(model)
                    }
                }
            }
        })

        mRecyclerView.adapter = mAdapter
    }

    private fun showGroup(model: CommonModel) {
        mRefGroups.child(model.id).addListenerForSingleValueEvent(AppValueEventListener {
            val newModel = it.getCommonModel()

            mRefGroups.child(model.id).child(NODE_MESSAGES)
                .limitToLast(1)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    val tmpList = it.children.map { it.getCommonModel() }
                    if (tmpList.isNotEmpty()) {
                        newModel.lastMessage = tmpList[0].text
                    }
                    newModel.fullname = newModel.username
                    newModel.type = TYPE_GROUP
                    mAdapter.updateListItems(newModel)
                })
        })
    }

    private fun showChat(model: CommonModel) {
        mRefUsers.child(model.id).addListenerForSingleValueEvent(AppValueEventListener {
            val newModel = it.getCommonModel()

            mRefMessages.child(model.id).limitToLast(1)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    val tmpList = it.children.map { it.getCommonModel() }
                    if (tmpList.isNotEmpty()) {
                        newModel.lastMessage = tmpList[0].text
                    }
                    if (newModel.fullname.isEmpty()) {
                        newModel.fullname = newModel.phone
                    }
                    newModel.type = TYPE_CHAT
                    mAdapter.updateListItems(newModel)
                })
        })
    }
}
