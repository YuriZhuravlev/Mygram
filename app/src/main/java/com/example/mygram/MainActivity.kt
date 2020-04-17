package com.example.mygram

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.mygram.activities.RegisterActivity
import com.example.mygram.databinding.ActivityMainBinding
import com.example.mygram.ui.fragments.ChatsFragment
import com.example.mygram.ui.objects.AppDrawer
import com.example.mygram.utilits.AUTH
import com.example.mygram.utilits.initFirebase
import com.example.mygram.utilits.replaceActivity
import com.example.mygram.utilits.replaceFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mAppDrawer: AppDrawer
    private lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        initFields()
        initFunc()
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer(this,mToolbar)
        initFirebase()
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

}
