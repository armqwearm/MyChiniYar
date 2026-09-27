package com.chiniyar.app.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import java.util.UUID

data class SavedLocation(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val category: String = "شخصی",
    val mapsUrl: String,
    val address: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

class LocationDatabase private constructor(context: Context) :
    SQLiteOpenHelper(context.applicationContext, DB_NAME, null, DB_VERSION) {

    private val _locations = MutableStateFlow<List<SavedLocation>>(emptyList())
    val locations: Flow<List<SavedLocation>> = _locations.asStateFlow()

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE locations (
                id TEXT PRIMARY KEY NOT NULL,
                name TEXT NOT NULL,
                description TEXT NOT NULL DEFAULT '',
                category TEXT NOT NULL DEFAULT 'شخصی',
                mapsUrl TEXT NOT NULL,
                address TEXT NOT NULL DEFAULT '',
                latitude REAL,
                longitude REAL,
                createdAt INTEGER NOT NULL,
                updatedAt INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX locations_updatedAt_idx ON locations(updatedAt)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit

    suspend fun add(location: SavedLocation): Boolean = withContext(Dispatchers.IO) {
        val normalized = location.normalized().withCoordinatesFromUrl()
        if (!normalized.hasValidCoordinates() || normalized.name.isBlank() || normalized.mapsUrl.isBlank()) return@withContext false

        val inserted = writableDatabase.insertWithOnConflict(
            "locations",
            null,
            contentValues(normalized),
            SQLiteDatabase.CONFLICT_IGNORE
        ) != -1L

        if (inserted) refreshInternal()
        inserted
    }

    suspend fun update(location: SavedLocation): Boolean = withContext(Dispatchers.IO) {
        val normalized = location.copy(updatedAt = System.currentTimeMillis()).normalized().withCoordinatesFromUrl()
        if (!normalized.hasValidCoordinates() || normalized.name.isBlank() || normalized.mapsUrl.isBlank()) return@withContext false

        val updated = writableDatabase.update(
            "locations",
            contentValues(normalized),
            "id = ?",
            arrayOf(normalized.id)
        ) > 0

        if (updated) refreshInternal()
        updated
    }

    suspend fun remove(id: String): Boolean = withContext(Dispatchers.IO) {
        val deleted = writableDatabase.delete("locations", "id = ?", arrayOf(id)) > 0
        if (deleted) refreshInternal()
        deleted
    }

    suspend fun addImported(locations: List<SavedLocation>): Int = withContext(Dispatchers.IO) {
        if (locations.isEmpty()) return@withContext 0

        var inserted = 0
        writableDatabase.beginTransaction()
        try {
            locations.forEach { location ->
                val normalized = location.copy(
                    id = UUID.randomUUID().toString(),
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                ).normalized().withCoordinatesFromUrl()

                if (!normalized.hasValidCoordinates() || normalized.name.isBlank() || normalized.mapsUrl.isBlank()) return@forEach

                val rowId = writableDatabase.insertWithOnConflict(
                    "locations",
                    null,
                    contentValues(normalized),
                    SQLiteDatabase.CONFLICT_IGNORE
                )
                if (rowId != -1L) inserted++
            }
            writableDatabase.setTransactionSuccessful()
        } finally {
            writableDatabase.endTransaction()
        }

        if (inserted > 0) refreshInternal()
        inserted
    }

    suspend fun refresh() = withContext(Dispatchers.IO) {
        refreshInternal()
    }

    private fun contentValues(location: SavedLocation): ContentValues =
        ContentValues().apply {
            put("id", location.id)
            put("name", location.name)
            put("description", location.description)
            put("category", location.category)
            put("mapsUrl", location.mapsUrl)
            put("address", location.address)
            location.latitude?.let { put("latitude", it) } ?: putNull("latitude")
            location.longitude?.let { put("longitude", it) } ?: putNull("longitude")
            put("createdAt", location.createdAt)
            put("updatedAt", location.updatedAt)
        }

    private fun refreshInternal() {
        val result = mutableListOf<SavedLocation>()
        readableDatabase.rawQuery(
            """
            SELECT id, name, description, category, mapsUrl, address,
                   latitude, longitude, createdAt, updatedAt
            FROM locations
            ORDER BY updatedAt DESC
            """.trimIndent(),
            null
        ).use { cursor ->
            while (cursor.moveToNext()) {
                result += SavedLocation(
                    id = cursor.getString(0),
                    name = cursor.getString(1),
                    description = cursor.getString(2),
                    category = cursor.getString(3),
                    mapsUrl = cursor.getString(4),
                    address = cursor.getString(5),
                    latitude = cursor.getDoubleOrNull(6),
                    longitude = cursor.getDoubleOrNull(7),
                    createdAt = cursor.getLong(8),
                    updatedAt = cursor.getLong(9)
                )
            }
        }
        _locations.value = result
    }

    private fun SavedLocation.normalized(): SavedLocation =
        copy(
            name = name.trim(),
            description = description.trim(),
            category = category.trim().ifBlank { "شخصی" },
            mapsUrl = mapsUrl.trim(),
            address = address.trim()
        )

    private fun SavedLocation.withCoordinatesFromUrl(): SavedLocation {
        if (hasValidCoordinates()) return this
        val coordinates = LocationShareCodec.extractCoordinates(mapsUrl) ?: return this
        return copy(latitude = coordinates.first, longitude = coordinates.second)
    }

    private fun SavedLocation.hasValidCoordinates(): Boolean =
        latitude != null && longitude != null &&
            latitude in -90.0..90.0 && longitude in -180.0..180.0

    private fun Cursor.getDoubleOrNull(index: Int): Double? =
        if (isNull(index)) null else getDouble(index)

    companion object {
        private const val DB_NAME = "chiniyar_locations.db"
        private const val DB_VERSION = 1

        @Volatile
        private var instance: LocationDatabase? = null

        fun getInstance(context: Context): LocationDatabase =
            instance ?: synchronized(this) {
                instance ?: LocationDatabase(context).also { db ->
                    instance = db
                    db.refreshInternal()
                }
            }
    }
}
