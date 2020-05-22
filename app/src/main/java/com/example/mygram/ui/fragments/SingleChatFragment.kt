package com.example.mygram.ui.fragments

import android.view.View
import androidx.fragment.app.Fragment

import com.example.mygram.R
import com.example.mygram.models.CommonModel
import com.example.mygram.utilits.APP_ACTIVITY
import kotlinx.android.synthetic.main.activity_main.view.*

/**
 * A simple [Fragment] subclass.
 */
class SingleChatFragment(contact: CommonModel) : BaseFragment(R.layout.fragment_single_chat) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.mToolbar.toolbar_info.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.mToolbar.toolbar_info.visibility = View.INVISIBLE
    }
}
