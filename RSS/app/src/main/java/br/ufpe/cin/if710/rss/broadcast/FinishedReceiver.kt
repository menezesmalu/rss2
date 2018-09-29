package br.ufpe.cin.if710.rss.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class FinishedReceiver : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, i: Intent?) {
        Toast.makeText(ctx, "RSS baixado", Toast.LENGTH_LONG).show()
        Log.i("FinishedReceiver", "baixou o rss")
    }
}