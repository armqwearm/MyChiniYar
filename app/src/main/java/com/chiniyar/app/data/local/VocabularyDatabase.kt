package com.chiniyar.app.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/** Persistent, fully offline vocabulary database backed by Android SQLite. */
class VocabularyDatabase private constructor(context: Context) :
    SQLiteOpenHelper(context.applicationContext, DB_NAME, null, DB_VERSION) {

    private val _words = MutableStateFlow<List<VocabularyEntry>>(emptyList())
    val words: Flow<List<VocabularyEntry>> = _words.asStateFlow()

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE vocabulary (
                word TEXT PRIMARY KEY NOT NULL,
                pinyin TEXT NOT NULL DEFAULT '',
                meaning TEXT NOT NULL DEFAULT '',
                createdAt INTEGER NOT NULL
            )
        """.trimIndent())
        db.execSQL("CREATE INDEX vocabulary_createdAt_idx ON vocabulary(createdAt)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit

    suspend fun add(entry: VocabularyEntry): Boolean = withContext(Dispatchers.IO) {
        val word = entry.word.trim()
        if (word.isEmpty()) return@withContext false
        val values = ContentValues().apply {
            put("word", word)
            put("pinyin", entry.pinyin.trim())
            put("meaning", entry.meaning.trim())
            put("createdAt", entry.createdAt)
        }
        val inserted = writableDatabase.insertWithOnConflict(
            "vocabulary", null, values, SQLiteDatabase.CONFLICT_IGNORE
        ) != -1L
        refreshInternal()
        inserted
    }

    suspend fun remove(word: String): Boolean = withContext(Dispatchers.IO) {
        val deleted = writableDatabase.delete("vocabulary", "word = ?", arrayOf(word.trim())) > 0
        refreshInternal()
        deleted
    }

    suspend fun contains(word: String): Boolean = withContext(Dispatchers.IO) {
        readableDatabase.rawQuery(
            "SELECT 1 FROM vocabulary WHERE word = ? LIMIT 1", arrayOf(word.trim())
        ).use { it.moveToFirst() }
    }

    suspend fun allWords(): Set<String> = withContext(Dispatchers.IO) {
        buildSet {
            readableDatabase.rawQuery("SELECT word FROM vocabulary", null).use { cursor ->
                while (cursor.moveToNext()) add(cursor.getString(0))
            }
        }
    }

    suspend fun refresh() = withContext(Dispatchers.IO) { refreshInternal() }

    private fun refreshInternal() {
        val result = mutableListOf<VocabularyEntry>()
        readableDatabase.rawQuery(
            "SELECT word, pinyin, meaning, createdAt FROM vocabulary ORDER BY createdAt DESC", null
        ).use { cursor ->
            while (cursor.moveToNext()) {
                result += VocabularyEntry(
                    cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getLong(3)
                )
            }
        }
        _words.value = result
    }

    companion object {
        private const val DB_NAME = "chiniyar_vocabulary.db"
        private const val DB_VERSION = 1
        @Volatile private var instance: VocabularyDatabase? = null

        fun getInstance(context: Context): VocabularyDatabase = instance ?: synchronized(this) {
            instance ?: VocabularyDatabase(context).also { db ->
                instance = db
                db.refreshInternal()
            }
        }
    }
}
