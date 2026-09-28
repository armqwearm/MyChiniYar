package com.chiniyar.app.data.local

import android.net.Uri
import android.util.Base64
import org.json.JSONArray
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.Locale

object LocationShareCodec {
    const val VERSION = 1
    const val START_MARKER = "---CHINIYAR_LOCATIONS_V1---"
    const val END_MARKER = "---END_CHINIYAR_LOCATIONS---"

    data class ImportResult(
        val locations: List<SavedLocation>,
        val error: String? = null
    )

    fun export(locations: List<SavedLocation>): String {
        require(locations.isNotEmpty())

        val payload = JSONObject()
            .put("version", VERSION)
            .put(
                "locations",
                JSONArray().apply {
                    locations.forEach { location ->
                        put(
                            JSONObject()
                                .put("id", location.id)
                                .put("name", location.name)
                                .put("description", location.description)
                                .put("category", location.category)
                                .put("mapsUrl", location.mapsUrl)
                                .put("address", location.address)
                                .putNullable("latitude", location.latitude)
                                .putNullable("longitude", location.longitude)
                        )
                    }
                }
            )
            .toString()

        val encoded = Base64.encodeToString(
            payload.toByteArray(StandardCharsets.UTF_8),
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )

        return buildString {
            appendLine("📍 مکان‌های من در چینی‌یار")
            appendLine()
            locations.forEachIndexed { index, location ->
                appendLine("\${index + 1}. \${location.name}")
                appendLine("نوع: \${location.category}")
                if (location.description.isNotBlank()) {
                    appendLine("توضیحات: \${location.description}")
                }
                if (location.address.isNotBlank()) {
                    appendLine("آدرس: \${location.address}")
                }
                appendLine("🗺️ \${location.mapsUrl}")
                appendLine()
            }
            appendLine("تعداد مکان‌ها: \${locations.size}")
            appendLine()
            appendLine("برای وارد کردن این مکان‌ها در چینی‌یار، کل این متن را کپی و در بخش «دریافت مکان‌ها» Paste کنید.")
            appendLine()
            appendLine(START_MARKER)
            appendLine(encoded)
            appendLine(END_MARKER)
        }
    }

    fun cleanMapsUrl(rawUrl: String): String =
        rawUrl
            .replace("\u200B", "")
            .replace("\u200C", "")
            .replace("\u200D", "")
            .replace("\uFEFF", "")
            .replace("\r", "")
            .replace("\n", "")
            .trim()
            .filterNot { it.isWhitespace() }

    fun isHttpUrl(rawUrl: String): Boolean {
        val clean = cleanMapsUrl(rawUrl)
        val uri = runCatching { Uri.parse(clean) }.getOrNull()
        return uri != null && uri.scheme?.lowercase() in setOf("http", "https") && !uri.host.isNullOrBlank()
    }

    fun import(text: String): ImportResult {
        val start = text.indexOf(START_MARKER)
        val end = text.indexOf(END_MARKER)

        if (start < 0 || end <= start) {
            return ImportResult(emptyList(), "بسته‌ی مکان‌های چینی‌یار در متن پیدا نشد.")
        }

        val encoded = text
            .substring(start + START_MARKER.length, end)
            .trim()

        if (encoded.isBlank()) {
            return ImportResult(emptyList(), "داده‌ی مکان‌ها خالی است.")
        }

        return try {
            val jsonText = String(
                Base64.decode(
                    encoded,
                    Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
                ),
                StandardCharsets.UTF_8
            )
            val root = JSONObject(jsonText)

            if (root.optInt("version", -1) != VERSION) {
                return ImportResult(emptyList(), "نسخه‌ی بسته‌ی مکان‌ها پشتیبانی نمی‌شود.")
            }

            val jsonLocations = root.optJSONArray("locations")
                ?: return ImportResult(emptyList(), "فهرست مکان‌ها در بسته پیدا نشد.")

            val result = buildList {
                for (index in 0 until jsonLocations.length()) {
                    val item = jsonLocations.optJSONObject(index) ?: continue
                    val name = item.optString("name").trim()
                    val mapsUrl = cleanMapsUrl(item.optString("mapsUrl").trim())
                    if (name.isBlank()) continue
                    if (mapsUrl.isNotBlank() && !isHttpUrl(mapsUrl)) continue

                    val explicitCoordinates =
                        item.optNullableDouble("latitude") to item.optNullableDouble("longitude")
                    val coordinates =
                        if (isValidCoordinates(explicitCoordinates.first, explicitCoordinates.second)) {
                            explicitCoordinates.first!! to explicitCoordinates.second!!
                        } else {
                            extractCoordinates(mapsUrl)
                        }

                    add(
                        SavedLocation(
                            name = name,
                            description = item.optString("description").trim(),
                            category = item.optString("category").trim().ifBlank { "شخصی" },
                            mapsUrl = mapsUrl,
                            address = item.optString("address").trim(),
                            latitude = coordinates?.first,
                            longitude = coordinates?.second
                        )
                    )
                }
            }

            if (result.isEmpty()) {
                ImportResult(emptyList(), "هیچ مکان معتبر و قابل واردکردنی پیدا نشد.")
            } else {
                ImportResult(result)
            }
        } catch (_: Throwable) {
            ImportResult(emptyList(), "متن اشتراک‌گذاری‌شده خراب یا ناقص است.")
        }
    }

    fun extractCoordinates(mapsUrl: String): Pair<Double, Double>? {
        val url = cleanMapsUrl(mapsUrl)
        if (url.isBlank()) return null

        val uri = runCatching { Uri.parse(url) }.getOrNull()
        val queryCandidates = listOf(
            uri?.getQueryParameter("query"),
            uri?.getQueryParameter("q"),
            uri?.getQueryParameter("ll")
        )

        queryCandidates.forEach { candidate ->
            parsePair(candidate)?.let { return it }
        }

        val patterns = listOf(
            Regex("""!3d(-?\d+(?:\.\d+)?)!4d(-?\d+(?:\.\d+)?)"""),
            Regex("""@(-?\d+(?:\.\d+)?),(-?\d+(?:\.\d+)?)""")
        )

        for (pattern in patterns) {
            pattern.find(url)?.let { match ->
                val lat = match.groupValues[1].toDoubleOrNull()
                val lng = match.groupValues[2].toDoubleOrNull()
                if (isValidCoordinates(lat, lng)) return lat!! to lng!!
            }
        }

        return null
    }

    fun buildMapsUrl(latitude: Double, longitude: Double): String =
        String.format(
            Locale.US,
            "https://www.google.com/maps/search/?api=1&query=%.7f,%.7f",
            latitude,
            longitude
        )

    private fun parsePair(value: String?): Pair<Double, Double>? {
        if (value.isNullOrBlank()) return null
        val match = Regex("""(-?\d+(?:\.\d+)?)\s*,\s*(-?\d+(?:\.\d+)?)""").find(value)
            ?: return null
        val lat = match.groupValues[1].toDoubleOrNull()
        val lng = match.groupValues[2].toDoubleOrNull()
        return if (isValidCoordinates(lat, lng)) lat!! to lng!! else null
    }

    private fun isValidCoordinates(latitude: Double?, longitude: Double?): Boolean =
        latitude != null && longitude != null &&
            latitude in -90.0..90.0 &&
            longitude in -180.0..180.0

    private fun JSONObject.putNullable(key: String, value: Double?): JSONObject =
        if (value == null) put(key, JSONObject.NULL) else put(key, value)

    private fun JSONObject.optNullableDouble(key: String): Double? =
        if (isNull(key)) null else optDouble(key, Double.NaN).takeUnless { it.isNaN() }
}
