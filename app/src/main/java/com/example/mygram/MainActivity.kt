package com.example.mygram

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.mygram.database.AUTH
import com.example.mygram.database.initFirebase
import com.example.mygram.database.initUser
import com.example.mygram.databinding.ActivityMainBinding
import com.example.mygram.ui.screens.MainFragment
import com.example.mygram.ui.screens.register.EnterPhoneNumberFragment
import com.example.mygram.ui.objects.AppDrawer
import com.example.mygram.utilits.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityMainBinding
    lateinit var mAppDrawer: AppDrawer
    lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        initFirebase()
        initUser() {
            CoroutineScope(Dispatchers.IO).launch { initContacts() }

            initFields()
            initFunc()
        }
    }

    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer()
    }

    private fun initFunc() {
        setSupportActionBar(mToolbar)
        if (AUTH.currentUser != null) {
            mAppDrawer.create()
            replaceFragment(MainFragment(), false)
        } else {
            replaceFragment(EnterPhoneNumberFragment(), false)
        }
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateStates(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateStates(AppStates.OFFLINE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(APP_ACTIVITY,READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
            initContacts()
    }
}
