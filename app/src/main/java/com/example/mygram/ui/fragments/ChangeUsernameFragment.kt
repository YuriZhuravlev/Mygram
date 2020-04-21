package com.example.mygram.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.mygram.MainActivity

import com.example.mygram.R
import com.example.mygram.utilits.*
import kotlinx.android.synthetic.main.fragment_change_username.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {

    lateinit var mUserName: String

    override fun onResume() {
        super.onResume()
        settings_input_username.setText(USER.username)
    }

    override fun change() {
        mUserName = settings_input_username.text.toString().toLowerCase(Locale.getDefault())
        if (mUserName.isEmpty())
            showToast("Поле пустое!")
        else {
            REF_DATABASE_ROOT.child(NODE_USERNAMES)
                .addListenerForSingleValueEvent(AppValueEventListener {
                    if (it.hasChild(mUserName)) {
                        showToast(getString(R.string.settings_toast_username_exists))
                    } else {
                        changeUsername()
                    }
                })
        }
    }

    private fun changeUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mUserName).setValue(UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername()
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    private fun updateCurrentUsername() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_USERNAME)
            .setValue(mUserName)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    deleteOldUsername()

                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUsername() {
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(USER.username).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    showToast(getString(R.string.toast_data_update))
                    USER.username = mUserName
                    fragmentManager?.popBackStack()
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }
}
