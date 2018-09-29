package br.ufpe.cin.if710.rss.broadcast

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import br.ufpe.cin.if710.rss.MainActivity
import br.ufpe.cin.if710.rss.R
import br.ufpe.cin.if710.rss.db.SqlHelper

//Use um BroadcastReceiver registrado dinamicamente, para quando o usuário estiver com o
// app em primeiro plano, a atualização da lista de itens ser feita de forma automática;

class UpdateReceiver: BroadcastReceiver(){
    override fun onReceive(ctx: Context?, i: Intent?) {
        Log.i("update", "entrou no update")
        Toast.makeText(ctx, "update receiver!!", Toast.LENGTH_LONG).show()
        //val db = SqlHelper.getInstance(ctx!!)
        //MainActivity().printRSS().execute(db)
    }
}