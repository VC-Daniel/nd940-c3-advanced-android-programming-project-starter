package com.notificationsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Holds fragments that allow the user to download a repository and then view the details of
 * the download once it has completed.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
