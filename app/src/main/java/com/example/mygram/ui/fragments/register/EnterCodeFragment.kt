package com.example.mygram.ui.fragments.register

import androidx.fragment.app.Fragment

import com.example.mygram.R
import com.example.mygram.database.*
import com.example.mygram.utilits.*
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*


class EnterCodeFragment(val phoneNumber: String,val id: String) : Fragment(R.layout.fragment_enter_code) {

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.title = phoneNumber
        register_input_code.addTextChangedListener(AppTextWatcher {
            val string = register_input_code.text.toString()
            if (string.length == 6) {
                verificationCode()
            }
        })
    }

    private fun verificationCode() {
        val code = register_input_code.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val uid = AUTH.currentUser?.uid.toString()
                REF_DATABASE_ROOT.child(
                    NODE_USERS
                )
                    .addListenerForSingleValueEvent(AppValueEventListener {users ->
                        if (users.hasChild(uid)) {
                            showToast(getString(R.string.register_welcome))
                            restartActivity()
                        } else {
                            registerUser(uid)
                        }
                    })
            } else {
                showToast(it.exception?.message.toString())
            }
        }
    }

    private fun registerUser(uid: String) {
        var dateMap = mutableMapOf<String, Any>()
        dateMap[CHILD_ID] = uid
        dateMap[CHILD_PHONE] = phoneNumber
        dateMap[CHILD_USERNAME] = phoneNumber
        REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid)
            .addOnFailureListener { showToast(it.message.toString()) }
            .addOnSuccessListener {
                REF_DATABASE_ROOT.child(
                    NODE_USERS
                ).child(uid).updateChildren(dateMap)
                    .addOnFailureListener { it.message.toString() }
                    .addOnCompleteListener {
                        showToast(getString(R.string.register_welcome))
                        restartActivity()
                    }
            }
    }
}
