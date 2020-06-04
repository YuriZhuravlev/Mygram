package com.example.mygram.ui.fragments

import androidx.fragment.app.Fragment

import com.example.mygram.R
import com.example.mygram.database.*
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
            REF_DATABASE_ROOT.child(
                NODE_USERNAMES
            )
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
        REF_DATABASE_ROOT.child(NODE_USERNAMES).child(mUserName).setValue(
            CURRENT_UID
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUsername(mUserName)
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }
}
