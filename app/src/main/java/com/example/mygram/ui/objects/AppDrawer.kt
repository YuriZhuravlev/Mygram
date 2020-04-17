package com.example.mygram.ui.objects

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.mygram.R
import com.example.mygram.ui.fragments.HelpFragment
import com.example.mygram.ui.fragments.SettingsFragment
import com.example.mygram.utilits.replaceFragment
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem

class AppDrawer(val mainActivity: AppCompatActivity,val toolbar: Toolbar) {
    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mDrawerLayout: DrawerLayout

    fun create() {
        createHeader()
        createDrawer()
        mDrawerLayout = mDrawer.drawerLayout
    }

    fun disableDrawer() {
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toolbar.setNavigationOnClickListener {
            mainActivity.supportFragmentManager.popBackStack()
        }
    }

    fun enableDrawer() {
        mainActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.setNavigationOnClickListener {
            mDrawer.openDrawer()
        }
    }

    private fun createHeader() {
        mHeader = AccountHeaderBuilder()
            .withActivity(mainActivity)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                ProfileDrawerItem().withName("UserName")
                    .withEmail("username@username.com")
            ).build()
    }

    private fun createDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(mainActivity)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .withAccountHeader(mHeader)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(101)
                    .withIconTintingEnabled(true)
                    .withName(R.string.menu_item1)
                    .withIcon(R.drawable.ic_menu_create_groups)
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(102)
                    .withIconTintingEnabled(true)
                    .withName(R.string.menu_item2)
                    .withIcon(R.drawable.ic_menu_secret_chat)
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(103)
                    .withIconTintingEnabled(true)
                    .withName(R.string.menu_item3)
                    .withIcon(R.drawable.ic_menu_create_channel)
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(104)
                    .withIconTintingEnabled(true)
                    .withName(R.string.menu_item4)
                    .withIcon(R.drawable.ic_menu_contacts)
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(105)
                    .withIconTintingEnabled(true)
                    .withName(R.string.menu_item5)
                    .withIcon(R.drawable.ic_menu_phone)
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(106)
                    .withIconTintingEnabled(true)
                    .withName(R.string.menu_item6)
                    .withIcon(R.drawable.ic_menu_favorites)
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(107)
                    .withIconTintingEnabled(true)
                    .withName(R.string.menu_item7)
                    .withIcon(R.drawable.ic_menu_settings)
                    .withSelectable(false),
                DividerDrawerItem(),
                PrimaryDrawerItem().withIdentifier(108)
                    .withIconTintingEnabled(true)
                    .withName(R.string.menu_item8)
                    .withIcon(R.drawable.ic_menu_invate)
                    .withSelectable(false),
                PrimaryDrawerItem().withIdentifier(109)
                    .withIconTintingEnabled(true)
                    .withName(R.string.menu_item9)
                    .withIcon(R.drawable.ic_menu_help)
                    .withSelectable(false)
            )
            .withOnDrawerItemClickListener(object :Drawer.OnDrawerItemClickListener{
                override fun onItemClick(
                    view: View?,
                    position: Int,
                    drawerItem: IDrawerItem<*>
                ): Boolean {
                    when (position) {
                        7 ->   mainActivity.replaceFragment(SettingsFragment())
                        10 -> mainActivity.replaceFragment(HelpFragment())
                    }
                    return false
                }
            })
            .build()
    }
}