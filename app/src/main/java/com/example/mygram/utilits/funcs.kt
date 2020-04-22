package com.example.mygram.utilits

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mygram.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_settings.*

fun Activity.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToast(message: String) {
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity) {
    val intent =  Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    if (addStack)
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.dataContainer, fragment)
            .commit()
    else
        supportFragmentManager.beginTransaction()
            .replace(R.id.dataContainer, fragment)
            .commit()
}

fun Fragment.replaceFragment(fragment: Fragment) {
    this.fragmentManager?.beginTransaction()
        ?.addToBackStack(null)
        ?.replace(R.id.dataContainer, fragment)
        ?.commit()
}

fun hideKeyBoard() {
    val imm: InputMethodManager = APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken, 0)
}

fun CircleImageView.downloadAndSetImage(url: String) {
    Picasso.get()
        .load(url)
        .placeholder(R.drawable.default_photo)
        .into(this)
}