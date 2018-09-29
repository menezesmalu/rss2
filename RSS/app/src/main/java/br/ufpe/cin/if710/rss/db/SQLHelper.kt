package br.ufpe.cin.if710.rss.db

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import br.ufpe.cin.if710.rss.RSSFiles.ItemRSS
import org.jetbrains.anko.db.*
import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper


class SqlHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DB_VERSION) {
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        //Nome do Banco de Dados
        private val DATABASE_NAME = "rss"
        //Nome da tabela do Banco a ser usada
        val DATABASE_TABLE = "items"
        //Versão atual do banco
        private val DB_VERSION = 1

        private var db: SqlHelper? = null

        //Definindo Singleton
        fun getInstance(c: Context): SqlHelper {
            if (db == null) {
                db = SqlHelper(c.applicationContext) as SqlHelper
            }
            return db as SqlHelper
        }

        //Definindo constantes que representam os campos do banco de dados
        val ROWID = RssProviderContract._ID
        val TITLE = RssProviderContract.TITLE
        val DATE = RssProviderContract.DATE
        val DESCRIPTION = RssProviderContract.DESCRIPTION
        val LINK = RssProviderContract.LINK
        val UNREAD = RssProviderContract.UNREAD
        val TABLE = "items"
        val _ID = "_id"
        val columns = arrayOf(_ID, TITLE, DATE, DESCRIPTION, LINK, UNREAD)

        private var instance: SqlHelper? = null

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(DATABASE_TABLE, true,
                _ID to INTEGER + PRIMARY_KEY,
                TITLE to TEXT,
                DATE to TEXT,
                DESCRIPTION to TEXT,
                LINK to TEXT,
                UNREAD to TEXT)
    }

    fun insertItem(item: ItemRSS): Long {
        return insertItem(item.title, item.pubDate, item.description, item.link)
    }

    fun insertItem(title: String, pubDate: String, description: String, link: String): Long {
        var database = db!!.writableDatabase
        database!!.insert(DATABASE_TABLE, TITLE to title, DATE to pubDate, DESCRIPTION to description, LINK to link, UNREAD to "false")
        return database.maximumSize
    }

    @SuppressLint("Recycle")
    fun getItemRSS(link: String): ItemRSS? {
        var database = db!!.readableDatabase
        var item : ItemRSS? = null
        // define a condição pra substituir
        val whereClause = "$LINK == ?"
        // define o argumento da condição
        val whereArgs = arrayOf(
                link)
        //fazendo a query
        val cursor = database.query(DATABASE_TABLE, columns,whereClause,whereArgs,null,null,null)
        cursor.moveToFirst()
        if(cursor.count > 0) {
            do {
                val ititle = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                val ilink = cursor.getString(cursor.getColumnIndexOrThrow(LINK))
                val idate = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
                val idesc = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
                item = ItemRSS(ititle, ilink, idate, idesc)
            } while (cursor.moveToNext())
        }
        return  item
    }

    fun markAsUnRead(link:String): Boolean{
        return markAs(link, "false")
    }

    fun markAsRead(link: String): Boolean {
        return markAs(link, "true")
    }
    fun markAs(link: String, value: String): Boolean{
        var database = db!!.writableDatabase
        val newValues = ContentValues()
        newValues.put(UNREAD, value)

        // define a condição pra substituir
        val whereClause = "$LINK == ?"

        // define o argumento da condição
        val whereArgs = arrayOf(
                link)
        val amountOfUpdatedColumns = database.update(DATABASE_TABLE, newValues, whereClause, whereArgs)
        if(value == "true") return true
        return false
    }

    fun getItens():List<ItemRSS>?{
        var database = db!!.readableDatabase
        var list : MutableList<ItemRSS>? = mutableListOf()
        // define a condição pra substituir
        val whereClause = "$UNREAD == ?"
        // define o argumento da condição
        val whereArgs = arrayOf(
                "false")
        //fazendo a query
        val cursor = database.query(DATABASE_TABLE, columns,whereClause,whereArgs,null,null,null)
        cursor.moveToFirst()
        if(cursor.count == 0)
            return null
        do {
            val ititle = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
            val ilink = cursor.getString(cursor.getColumnIndexOrThrow(LINK))
            val idate = cursor.getString(cursor.getColumnIndexOrThrow(DATE))
            val idesc = cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION))
            list!!.add(ItemRSS(ititle, ilink, idate, idesc))
        } while (cursor.moveToNext())

        return list
    }
}

// Access property for Context
val Context.database: SqlHelper
    get() = SqlHelper.getInstance(applicationContext)
