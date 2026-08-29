package com.chiniyar.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "vocabulary")
data class VocabularyEntity(
    @PrimaryKey val word: String,
    val pinyin: String,
    val meaning: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<VocabularyEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM vocabulary WHERE word = :word)")
    suspend fun contains(word: String): Boolean

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insert(entry: VocabularyEntity)

    @Delete
    suspend fun delete(entry: VocabularyEntity)
}

@Database(entities = [VocabularyEntity::class], version = 1, exportSchema = false)
abstract class VocabularyDatabase : RoomDatabase() {
    abstract fun vocabularyDao(): VocabularyDao

    companion object {
        @Volatile private var INSTANCE: VocabularyDatabase? = null

        fun getInstance(context: Context): VocabularyDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    VocabularyDatabase::class.java,
                    "chiniyar_vocabulary.db"
                ).build().also { INSTANCE = it }
            }
    }
}
