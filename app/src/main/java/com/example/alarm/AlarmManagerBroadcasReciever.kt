package com.example.alarm

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import java.lang.Boolean
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*


class AlarmManagerBroadcastReceiver : BroadcastReceiver(){
    private val ONE_TIME : String = "onetime"

    @SuppressLint("InvalidWakeLockTag")
    override fun onReceive(p0: Context?, p1: Intent?) {
        val pm = p0!!.getSystemService(Context.POWER_SERVICE) as PowerManager;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!pm.isIgnoringBatteryOptimizations("com.example.alarm")) {
                p1?.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                p1?.setData(Uri.parse("package:com.example.alarm"));
            }
        }

        var wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "AlarmManagerBroadcastReceiver")
        //Acquire the lock
        wl.acquire()

        //You can do the processing here.
        //TODO: create own logic here
        val extras: Bundle? = p1?.getExtras()
        val msgStr = StringBuilder()

        if (extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)) {
            //Make sure this intent has been sent by the one-time timer button.
            msgStr.append("One time Timer : ")
        }
        val formatter: Format = SimpleDateFormat("hh:mm:ss a")
        msgStr.append(formatter.format(Date()))

        Toast.makeText(p0, msgStr, Toast.LENGTH_LONG).show()
        Log.d("TAG", msgStr.toString())

        //Release the lock
        wl.release()
    }

    fun SetAlarm(context: Context) {
        val am =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmManagerBroadcastReceiver::class.java)
        intent.putExtra(ONE_TIME, Boolean.FALSE)
        val pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // We want the alarm to go off 3 seconds from now.
        var firstTime = SystemClock.elapsedRealtime()
        firstTime += 1000.toLong() //start 3 seconds after first register.

        //After after 5 seconds
        am.setRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            firstTime,
            (1000*60*5).toLong(),
            pi
        )
    }
}