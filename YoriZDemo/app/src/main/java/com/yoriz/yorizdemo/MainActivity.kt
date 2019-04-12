package com.yoriz.yorizdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController

/**
 * Created by yoriz
 * on 2019/2/28 4:23 PM.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp() = findNavController(this, R.id.my_nav_host_fragment).navigateUp()
}