package br.ufpe.cin.if710.rss

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import br.ufpe.cin.if710.rss.ParserRSS.parse
import br.ufpe.cin.if710.rss.db.SqlHelper

class MainActivity : BaseActivity() {
    private var RSS_FEED = ""
    lateinit var prefs: SharedPreferences
    lateinit var db: SqlHelper
    //conteudoRSS é um recycler view
    private lateinit var conteudoRSS: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        conteudoRSS = findViewById(R.id.conteudoRSS)
        viewManager = LinearLayoutManager(this)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        RSS_FEED = prefs.getString(rssfeed, getString(R.string.rssfeed))
        db = SqlHelper.getInstance(this)
        printRSS().execute(db)
    }

    override fun onResume() {
        super.onResume()
        try {
            RSS_FEED = prefs.getString(rssfeed, getString(R.string.rssfeed))
            Log.i("RSS_FEED", RSS_FEED)
            val downloadService = Intent(applicationContext, DownloadService::class.java)
            downloadService.data = Uri.parse(RSS_FEED)
            startService(downloadService)

            printRSS().execute(db)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    internal inner class printRSS : AsyncTask<SqlHelper, Void, List<ItemRSS>>() {
        override fun doInBackground(vararg db: SqlHelper): List<ItemRSS>? {
            if (db[0].getItens() != null)
                return db[0].getItens()!!
            return null
        }

        override fun onPostExecute(result: List<ItemRSS>?) {
            super.onPostExecute(result)
            if (result != null) {
                viewAdapter = RssAdapter(result, db)

                conteudoRSS = findViewById<RecyclerView>(R.id.conteudoRSS).apply {
                    setHasFixedSize(true)
                    layoutManager = viewManager
                    adapter = viewAdapter
                }
            }
        }
    }

    //adapter para lidar com o RSS Feed
    private inner class RssAdapter(private val result: List<ItemRSS>, private val db: SqlHelper) :
            RecyclerView.Adapter<CardChangeHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardChangeHolder {
            val v = layoutInflater.inflate(R.layout.itemlista, parent, false)
            return CardChangeHolder(v)
        }

        override fun onBindViewHolder(holder: CardChangeHolder, position: Int) {
            holder.bindModel(result.get(position))
            //quando clicar no TITULO ir pro site
            holder.title.setOnClickListener {
                val url = result[position].link
                val uri = Uri.parse(url)
                val intents = Intent(Intent.ACTION_VIEW, uri)
                db.markAsRead(url)
                startActivity(intents)
            }
        }

        override fun getItemCount(): Int {
            return result.size
        }

    }

    //CardHolder com as informações que vão aparecer na tela de cada página
    internal class CardChangeHolder(row: View) : RecyclerView.ViewHolder(row) {
        var title: TextView
        var pubDate: TextView

        init {
            title = row.findViewById(R.id.item_titulo)
            pubDate = row.findViewById(R.id.item_data)
        }

        fun bindModel(rss: ItemRSS) {
            title.text = rss.title
            pubDate.text = rss.pubDate
        }

    }

    companion object {
        val rssfeed = "uname"
    }
}
