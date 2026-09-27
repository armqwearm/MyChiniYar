package com.chiniyar.app.data.local

import java.net.HttpURLConnection
import java.net.URI

object GoogleMapsUrlResolver {
    private val shortHosts = setOf(
        "goo.gl",
        "www.goo.gl",
        "maps.app.goo.gl"
    )

    fun resolve(rawUrl: String): String {
        val original = LocationShareCodec.cleanMapsUrl(rawUrl)
        if (original.isBlank()) return original

        val host = runCatching {
            URI(original).host?.lowercase()
        }.getOrNull()

        if (host !in shortHosts) return original

        var current = original
        repeat(MAX_REDIRECTS) {
            val connection = runCatching {
                (URI(current).toURL().openConnection() as HttpURLConnection).apply {
                    instanceFollowRedirects = false
                    requestMethod = "GET"
                    connectTimeout = TIMEOUT_MS
                    readTimeout = TIMEOUT_MS
                    setRequestProperty("User-Agent", USER_AGENT)
                    setRequestProperty("Accept", "text/html,application/xhtml+xml,*/*;q=0.8")
                }
            }.getOrNull() ?: return current

            try {
                when (connection.responseCode) {
                    in 300..399 -> {
                        val location = connection.getHeaderField("Location")
                            ?: return current
                        val next = URI(current).resolve(location).toString()
                        if (next == current) return current
                        current = next
                    }
                    else -> return current
                }
            } finally {
                connection.disconnect()
            }
        }

        return current
    }

    private const val MAX_REDIRECTS = 6
    private const val TIMEOUT_MS = 7000
    private const val USER_AGENT =
        "Mozilla/5.0 (Linux; Android) AppleWebKit/537.36 Chrome/Android ChiniYar"
}
