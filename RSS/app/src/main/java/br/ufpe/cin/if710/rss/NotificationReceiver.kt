package br.ufpe.cin.if710.rss

import android.app.ActivityManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {
    val contentTitle = "Novidades!"
    val contentText = "tem notícia não lida no feed"

    override fun onReceive(ctx: Context, intent: Intent) {
        val mNotificationIntent = Intent(ctx.applicationContext, MainActivity::class.java)
        val mContentIntent = PendingIntent.getActivity(ctx.applicationContext, 0, mNotificationIntent, 0)
        if(!isForeground(ctx)) {
            val notification = NotificationCompat.Builder(ctx.applicationContext, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setAutoCancel(true)
                    .setContentIntent(mContentIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .build()
            val notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager!!.notify(265,notification)
        }
     }

    fun isForeground(ctx: Context): Boolean{
        var aManager: ActivityManager = ctx.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var list: List<ActivityManager.RunningAppProcessInfo>? = aManager.runningAppProcesses
        if(list == null) {
            return false
        }
        val pName: String = ctx.packageName
        for(i in list){
            if(i.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && i.processName.equals(pName)){
                return true
            }
        }
        return false
    }
    companion object {
        private val NOTIFICATION_CHANNEL_ID = "br.ufpe.cin.if710.notificacoes"
    }
}