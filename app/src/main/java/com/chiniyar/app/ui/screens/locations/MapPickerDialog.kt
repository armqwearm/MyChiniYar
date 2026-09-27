package com.chiniyar.app.ui.screens.locations

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.viewinterop.AndroidView
import org.json.JSONObject
import java.util.Locale

@Composable
fun MapPickerDialog(
    initialLatitude: Double?,
    initialLongitude: Double?,
    onDismiss: () -> Unit,
    onConfirm: (latitude: Double, longitude: Double) -> Unit
) {
    val context = LocalContext.current
    val defaultLatitude = initialLatitude ?: 35.8617
    val defaultLongitude = initialLongitude ?: 104.1954

    var center by remember(initialLatitude, initialLongitude) {
        mutableStateOf(
            if (initialLatitude != null && initialLongitude != null) {
                initialLatitude to initialLongitude
            } else {
                null
            }
        )
    }
    var picked by remember(initialLatitude, initialLongitude) {
        mutableStateOf<Pair<Double, Double>?>(center)
    }
    var webView by remember { mutableStateOf<WebView?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "انتخاب موقعیت روی نقشه",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "نقشه را جابه‌جا کن، نقطه دلخواه را وسط علامت قرار بده و سپس تأیید کن.",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "بستن")
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = {
                            createMapWebView(
                                context = it,
                                latitude = defaultLatitude,
                                longitude = defaultLongitude,
                                onCenter = { lat, lng ->
                                    center = lat to lng
                                },
                                onPick = { lat, lng ->
                                    picked = lat to lng
                                }
                            ).also { webView = it }
                        }
                    )

                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(42.dp)
                    )
                }

                Text(
                    text = center?.let { (lat, lng) ->
                        String.format(Locale.US, "مرکز نقشه: %.6f, %.6f", lat, lng)
                    } ?: "مرکز نقشه در حال آماده‌سازی است…",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium
                )

                OutlinedButton(
                    onClick = {
                        webView?.evaluateJavascript("pickCurrentPoint()", null)
                    },
                    enabled = webView != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Map, contentDescription = null)
                    Text("انتخاب همین نقطه")
                }

                picked?.let { (lat, lng) ->
                    Text(
                        String.format(Locale.US, "موقعیت انتخاب‌شده: %.6f, %.6f", lat, lng),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val selection = picked ?: return@Button
                            onConfirm(selection.first, selection.second)
                        },
                        enabled = picked != null,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("تأیید موقعیت")
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("انصراف")
                    }
                }
            }
        }
    }

    DisposableEffect(webView) {
        onDispose {
            webView?.removeJavascriptInterface("Android")
            webView?.stopLoading()
            webView?.destroy()
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
private fun createMapWebView(
    context: android.content.Context,
    latitude: Double,
    longitude: Double,
    onCenter: (Double, Double) -> Unit,
    onPick: (Double, Double) -> Unit
): WebView {
    val mainHandler = Handler(Looper.getMainLooper())
    val webView = WebView(context)

    webView.settings.javaScriptEnabled = true
    webView.settings.domStorageEnabled = true
    webView.settings.allowFileAccess = false
    webView.settings.allowContentAccess = false
    webView.webViewClient = WebViewClient()

    webView.addJavascriptInterface(
        object {
            @JavascriptInterface
            fun onCenter(lat: String, lng: String) {
                val pair = parseCoordinates(lat, lng) ?: return
                mainHandler.post { onCenter(pair.first, pair.second) }
            }

            @JavascriptInterface
            fun onPick(lat: String, lng: String) {
                val pair = parseCoordinates(lat, lng) ?: return
                mainHandler.post { onPick(pair.first, pair.second) }
            }
        },
        "Android"
    )

    webView.loadDataWithBaseURL(
        "https://www.openstreetmap.org/",
        buildMapHtml(latitude, longitude),
        "text/html",
        "UTF-8",
        null
    )
    return webView
}

private fun parseCoordinates(
    latitudeText: String,
    longitudeText: String
): Pair<Double, Double>? {
    val latitude = latitudeText.toDoubleOrNull()
    val longitude = longitudeText.toDoubleOrNull()
    return if (
        latitude != null && longitude != null &&
        latitude in -90.0..90.0 &&
        longitude in -180.0..180.0
    ) {
        latitude to longitude
    } else {
        null
    }
}

private fun buildMapHtml(latitude: Double, longitude: Double): String =
    """
    <!doctype html>
    <html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <link rel="stylesheet"
              href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css">
        <style>
            html, body, #map {
                height: 100%;
                width: 100%;
                margin: 0;
                padding: 0;
                overflow: hidden;
            }
            body {
                background: #e9eef2;
            }
            .leaflet-control-attribution {
                font-size: 9px;
            }
        </style>
    </head>
    <body>
        <div id="map"></div>
        <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
        <script>
            const map = L.map('map', {
                zoomControl: true,
                attributionControl: true
            }).setView([$latitude, $longitude], 15);

            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                maxZoom: 19,
                attribution: '&copy; OpenStreetMap contributors'
            }).addTo(map);

            function reportCenter() {
                const c = map.getCenter();
                if (window.Android) {
                    window.Android.onCenter(String(c.lat), String(c.lng));
                }
            }

            function pickCurrentPoint() {
                const c = map.getCenter();
                if (window.Android) {
                    window.Android.onPick(String(c.lat), String(c.lng));
                }
            }

            map.on('move', reportCenter);
            map.on('moveend', reportCenter);
            setTimeout(reportCenter, 800);
        </script>
    </body>
    </html>
    """.trimIndent()
