package com.chiniyar.app.ui.screens.locations

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebChromeClient
import android.webkit.ConsoleMessage
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
    var mapReady by remember { mutableStateOf(false) }
    var mapError by remember { mutableStateOf<String?>(null) }
    var webView by remember { mutableStateOf<WebView?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text("انتخاب موقعیت روی نقشه", fontWeight = FontWeight.Bold)
                        Text(
                            "نقشه را جابه‌جا کن، محل دلخواه را زیر علامت وسط قرار بده و بعد تأیید کن.",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Right,
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
                        .height(390.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        modifier = Modifier.fillMaxWidth(),
                        factory = {
                            createMapWebView(
                                context = it,
                                latitude = defaultLatitude,
                                longitude = defaultLongitude,
                                onReady = { mapReady = true },
                                onCenter = { lat, lng ->
                                    center = lat to lng
                                    mapError = null
                                },
                                onPick = { lat, lng ->
                                    picked = lat to lng
                                    center = lat to lng
                                },
                                onError = { mapError = it }
                            ).also { webView = it }
                        }
                    )

                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(42.dp)
                    )

                    if (!mapReady && mapError == null) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                        ) {
                            Text(
                                "در حال آماده‌سازی نقشه…",
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                mapError?.let {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Text(
                            it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            textAlign = TextAlign.Right,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Text(
                    text = center?.let { (lat, lng) ->
                        String.format(Locale.US, "موقعیت مرکز: %.6f, %.6f", lat, lng)
                    } ?: "موقعیت مرکز هنوز آماده نشده است.",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium
                )

                OutlinedButton(
                    onClick = {
                        webView?.evaluateJavascript("window.AndroidPickCurrentPoint && AndroidPickCurrentPoint()", null)
                    },
                    enabled = mapReady,
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
                            val selection = picked ?: center ?: return@Button
                            onConfirm(selection.first, selection.second)
                        },
                        enabled = mapReady && (picked != null || center != null),
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
    onReady: () -> Unit,
    onCenter: (Double, Double) -> Unit,
    onPick: (Double, Double) -> Unit,
    onError: (String) -> Unit
): WebView {
    val mainHandler = Handler(Looper.getMainLooper())
    val webView = WebView(context)

    webView.settings.javaScriptEnabled = true
    webView.settings.domStorageEnabled = true
    webView.settings.allowFileAccess = false
    webView.settings.allowContentAccess = false
    webView.settings.loadWithOverviewMode = false
    webView.settings.useWideViewPort = false
    webView.settings.cacheMode = android.webkit.WebSettings.LOAD_DEFAULT
    webView.settings.userAgentString =
        "ChiniYar/1.2.13 (Android; map picker; OpenStreetMap)"

    webView.webChromeClient = object : WebChromeClient() {
        override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
            if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.ERROR) {
                mainHandler.post {
                    onError(
                        "خطای نقشه: ${consoleMessage.message()}"
                    )
                }
            }
            return true
        }
    }

    webView.webViewClient = object : WebViewClient() {
        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            if (request.isForMainFrame) {
                mainHandler.post {
                    onError("بارگذاری نقشه انجام نشد. اتصال اینترنت را بررسی کنید.")
                }
            }
        }
    }

    webView.addJavascriptInterface(
        object {
            @JavascriptInterface
            fun onReady() {
                mainHandler.post { onReady() }
            }

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

            @JavascriptInterface
            fun onError() {
                mainHandler.post {
                    onError("نقشه دریافت نشد. در صورت اتصال اینترنت، دوباره تلاش کنید.")
                }
            }
        },
        "Android"
    )

    webView.loadDataWithBaseURL(
        "https://appassets.androidplatform.net/",
        buildMapHtml(latitude, longitude),
        "text/html",
        "UTF-8",
        null
    )

    mainHandler.postDelayed({
        if (!webView.isAttachedToWindow) return@postDelayed
        webView.evaluateJavascript(
            "(typeof window.Android !== 'undefined' && document.readyState !== 'loading')"
        ) { result ->
            if (result != "true") {
                onError("WebView نقشه اجرا نشد. لطفاً WebView سیستم و اتصال اینترنت را بررسی کنید.")
            }
        }
    }, 5000)

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

private fun buildMapHtml(latitude: Double, longitude: Double): String {
    val escapedLat = String.format(Locale.US, "%.7f", latitude)
    val escapedLng = String.format(Locale.US, "%.7f", longitude)

    return """
    <!doctype html>
    <html>
    <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <style>
            html, body, #map {
                width: 100%;
                height: 100%;
                margin: 0;
                padding: 0;
                overflow: hidden;
                background: #e7edf0;
                font-family: sans-serif;
                touch-action: none;
            }
            #map {
                position: relative;
                overflow: hidden;
            }
            #tiles {
                position: absolute;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
            }
            .tile {
                position: absolute;
                width: 256px;
                height: 256px;
                background: #dfe7ea;
                object-fit: cover;
                user-select: none;
                -webkit-user-drag: none;
            }
            #controls {
                position: absolute;
                top: 10px;
                left: 10px;
                z-index: 10;
                display: flex;
                flex-direction: column;
                gap: 6px;
            }
            button {
                width: 42px;
                height: 42px;
                border: 0;
                border-radius: 12px;
                background: rgba(255,255,255,.94);
                color: #173b4b;
                font-size: 24px;
                box-shadow: 0 2px 8px rgba(0,0,0,.18);
            }
            #credit {
                position: absolute;
                bottom: 2px;
                right: 4px;
                z-index: 10;
                padding: 2px 5px;
                font-size: 9px;
                color: #233b43;
                background: rgba(255,255,255,.78);
                border-radius: 5px;
            }
        </style>
    </head>
    <body>
        <div id="map">
            <div id="tiles"></div>
            <div id="controls">
                <button onclick="changeZoom(1)">+</button>
                <button onclick="changeZoom(-1)">−</button>
            </div>
            <div id="credit">© OpenStreetMap contributors</div>
        </div>
        <script>
            const TILE = 256;
            const map = document.getElementById('map');
            const tiles = document.getElementById('tiles');

            let zoom = 12;
            let centerLat = $escapedLat;
            let centerLng = $escapedLng;
            let dragStart = null;
            let dragMoved = false;
            let renderTimer = null;
            let tileLoaded = false;
            let initialTileWatch = null;

            function clampLat(lat) {
                return Math.max(-85.05112878, Math.min(85.05112878, lat));
            }

            function worldSize() {
                return TILE * Math.pow(2, zoom);
            }

            function lonToX(lon) {
                return (lon + 180) / 360 * worldSize();
            }

            function latToY(lat) {
                const r = clampLat(lat) * Math.PI / 180;
                return (1 - Math.log(Math.tan(r) + 1 / Math.cos(r)) / Math.PI) / 2 * worldSize();
            }

            function xToLon(x) {
                return x / worldSize() * 360 - 180;
            }

            function yToLat(y) {
                const n = Math.PI - 2 * Math.PI * y / worldSize();
                return 180 / Math.PI * Math.atan(0.5 * (Math.exp(n) - Math.exp(-n)));
            }

            function normalizeX(x) {
                const size = worldSize();
                while (x < 0) x += size;
                while (x >= size) x -= size;
                return x;
            }

            function render() {
                const w = map.clientWidth;
                const h = map.clientHeight;
                const cx = lonToX(centerLng);
                const cy = latToY(centerLat);
                const minTileX = Math.floor((cx - w / 2) / TILE) - 1;
                const maxTileX = Math.floor((cx + w / 2) / TILE) + 1;
                const minTileY = Math.floor((cy - h / 2) / TILE) - 1;
                const maxTileY = Math.floor((cy + h / 2) / TILE) + 1;
                const n = Math.pow(2, zoom);

                tiles.innerHTML = '';

                for (let ty = minTileY; ty <= maxTileY; ty++) {
                    if (ty < 0 || ty >= n) continue;
                    for (let tx = minTileX; tx <= maxTileX; tx++) {
                        const wrappedX = ((tx % n) + n) % n;
                        const img = document.createElement('img');
                        img.className = 'tile';
                        img.draggable = false;
                                    img.onload = function() { tileLoaded = true; };
                        img.src = 'https://tile.openstreetmap.org/' + zoom + '/' + wrappedX + '/' + ty + '.png';
                        img.style.left = (tx * TILE - cx + w / 2) + 'px';
                        img.style.top = (ty * TILE - cy + h / 2) + 'px';
                        img.onerror = function() {
                            this.style.background = '#cfd9dd';
                        };
                        tiles.appendChild(img);
                    }
                }

                reportCenter();
            }

            function reportCenter() {
                if (window.Android) {
                    window.Android.onCenter(String(centerLat), String(centerLng));
                }
            }

            function pickCurrentPoint() {
                if (window.Android) {
                    window.Android.onPick(String(centerLat), String(centerLng));
                }
            }

            function changeZoom(delta) {
                const next = Math.max(3, Math.min(18, zoom + delta));
                if (next === zoom) return;
                const oldX = lonToX(centerLng);
                const oldY = latToY(centerLat);
                zoom = next;
                const newSize = worldSize();
                const scale = newSize / (TILE * Math.pow(2, zoom - delta));
                const centerX = oldX * scale;
                const centerY = oldY * scale;
                centerLng = xToLon(normalizeX(centerX));
                centerLat = yToLat(Math.max(0, Math.min(newSize, centerY)));
                render();
            }

            map.addEventListener('pointerdown', function(e) {
                dragStart = { x: e.clientX, y: e.clientY };
                dragMoved = false;
                map.setPointerCapture(e.pointerId);
            });

            map.addEventListener('pointermove', function(e) {
                if (!dragStart) return;
                const dx = e.clientX - dragStart.x;
                const dy = e.clientY - dragStart.y;
                if (Math.abs(dx) + Math.abs(dy) > 3) dragMoved = true;

                const cx = normalizeX(lonToX(centerLng) - dx);
                const cy = Math.max(0, Math.min(worldSize(), latToY(centerLat) - dy));
                centerLng = xToLon(cx);
                centerLat = yToLat(cy);
                dragStart = { x: e.clientX, y: e.clientY };

                clearTimeout(renderTimer);
                renderTimer = setTimeout(render, 25);
            });

            map.addEventListener('pointerup', function(e) {
                dragStart = null;
                if (!dragMoved) pickCurrentPoint();
                render();
            });

            map.addEventListener('pointercancel', function() {
                dragStart = null;
            });

            window.AndroidPickCurrentPoint = pickCurrentPoint;

            window.addEventListener('resize', render);
            render();
            if (initialTileWatch === null) {
                initialTileWatch = setTimeout(function() {
                    if (!tileLoaded && window.Android) {
                        window.Android.onError();
                    }
                }, 6000);
            }
            if (window.Android) window.Android.onReady();
    </script>
    </body>
    </html>
    """.trimIndent()
}
