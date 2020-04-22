package com.example.mygram

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mygram.activities.RegisterActivity
import com.example.mygram.databinding.ActivityMainBinding
import com.example.mygram.models.User
import com.example.mygram.ui.fragments.ChatsFragment
import com.example.mygram.ui.objects.AppDrawer
import com.example.mygram.utilits.*
import com.theartofdev.edmodo.cropper.CropImage

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDrawer: AppDrawer
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFields()
        initFunc()
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this,mToolbar)
        initFirebase()
        initUser()
    }

    private fun initFunc() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(mToolbar)
            mAppDrawer.create()
            replaceFragment(ChatsFragment(), false)
        } else {
            replaceActivity(RegisterActivity())
        }


    }

    private fun initUser() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
            .addListenerForSingleValueEvent(AppValueEventListener{
                USER = it.getValue(User::class.java) ?:  User()
            })
    }
}
