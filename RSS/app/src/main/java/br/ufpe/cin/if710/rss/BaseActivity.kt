package br.ufpe.cin.if710.rss

import android.app.Activity
import android.content.Intent
import android.view.Menu
import android.view.MenuItem


open class BaseActivity : Activity() {

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.actbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_prefs -> {
                startActivity(Intent(applicationContext, PreferenceActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}