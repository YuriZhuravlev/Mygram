package com.example.mygram.ui.fragments

import androidx.fragment.app.Fragment
import com.example.mygram.R
import com.example.mygram.utilits.APP_ACTIVITY
import com.example.mygram.utilits.hideKeyBoard

class MainFragment : Fragment(R.layout.fragment_chats) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = "Mygram"
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyBoard()
    }
}
