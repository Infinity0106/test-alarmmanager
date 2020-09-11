package com.example.alarm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    var alarm: AlarmManagerBroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alarm = AlarmManagerBroadcastReceiver();
    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", alarm.toString())
        alarm?.SetAlarm(this.applicationContext);
    }
}