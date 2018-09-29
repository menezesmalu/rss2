package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceFragment

class PreferenceActivity: Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Carrega um layout que contem um fragmento
        setContentView(R.layout.fragment_prefs)

    }

    // Fragmento que mostra a preference com link
    class PrefsFragment : PreferenceFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            // Carrega preferences a partir de um XML
            addPreferencesFromResource(R.xml.preferencias)

        }

        companion object {
            protected val TAG = "PreferenceFragment"
        }
    }
}