package br.ufpe.cin.if710.rss

import android.annotation.SuppressLint
import android.app.IntentService
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import br.ufpe.cin.if710.rss.ParserRSS.parse
import br.ufpe.cin.if710.rss.db.SqlHelper
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadService : IntentService("DownloadService") {
    var inseridos: Int = 0
    //lateinit var db: SqlHelper

    override fun onHandleIntent(i: Intent?){
        var in_: InputStream? = null
        var db = SqlHelper.getInstance(this)
        var rssFeed = ""
        try {
            inseridos = 0
            val url = URL(i!!.data.toString())
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            in_ = conn.getInputStream()
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var count: Int = in_.read(buffer)
            while (count != -1) {
                out.write(buffer, 0, count)
                count = in_.read(buffer)
            }
            val response = out.toByteArray()
            rssFeed = String(response, charset("UTF-8"))
            val parsedRss = parse(rssFeed)
            for(i in parsedRss) {
                if(db.getItemRSS(i.link) == null){
                    db.insertItem(i)
                    Log.i("inseriu", i.title)
                    inseridos++
                }
            }


        } catch (e: IOException) {
            e.printStackTrace()
        }finally {
            if(in_ != null) {
                in_.close()
            }
            if(inseridos > 0)
                sendBroadcast(Intent(completo))
        }
    }
    companion object {
        val completo = "br.ufpe.cin.if710.rss.completo"
    }
}