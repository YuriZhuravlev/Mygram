package com.example.mygram.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.mygram.R
import com.example.mygram.utilits.APP_ACTIVITY

/**
 * A simple [Fragment] subclass.
 */
class ContactsFragment : BaseFragment(R.layout.fragment_contacts) {

    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.title_contacts)
    }
}
