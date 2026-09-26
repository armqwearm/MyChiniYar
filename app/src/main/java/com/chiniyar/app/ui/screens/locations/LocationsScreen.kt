package com.chiniyar.app.ui.screens.locations

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.chiniyar.app.data.local.LocationDatabase
import com.chiniyar.app.data.local.LocationShareCodec
import com.chiniyar.app.data.local.SavedLocation
import kotlinx.coroutines.launch
import java.util.UUID

private val LocationCategories = listOf(
    "هتل",
    "فرودگاه",
    "ایستگاه",
    "نمایشگاه",
    "سفارت",
    "رستوران",
    "بازار",
    "شرکت",
    "شخصی"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationsScreen(
    database: LocationDatabase,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val locations by database.locations.collectAsState(initial = emptyList())

    var selectedIds by remember { mutableStateOf(emptySet<String>()) }
    var editing by remember { mutableStateOf<SavedLocation?>(null) }
    var showEditor by remember { mutableStateOf(false) }
    var importCandidates by remember { mutableStateOf<List<SavedLocation>?>(null) }
    var importError by remember { mutableStateOf<String?>(null) }
    var statusMessage by remember { mutableStateOf<String?>(null) }

    fun readClipboard(): String {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        return clipboard?.primaryClip
            ?.takeIf { it.itemCount > 0 }
            ?.getItemAt(0)
            ?.coerceToText(context)
            ?.toString()
            .orEmpty()
    }

    fun shareLocations(items: List<SavedLocation>) {
        if (items.isEmpty()) return
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, LocationShareCodec.export(items))
        }
        context.startActivity(Intent.createChooser(intent, "اشتراک‌گذاری مکان‌ها"))
    }

    fun openMaps(location: SavedLocation) {
        val target = location.mapsUrl.ifBlank {
            if (location.latitude != null && location.longitude != null) {
                LocationShareCodec.buildMapsUrl(location.latitude, location.longitude)
            } else {
                ""
            }
        }

        if (target.isBlank()) {
            statusMessage = "برای این مکان لینک نقشه‌ای ثبت نشده است."
            return
        }

        runCatching {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(target)))
        }.onFailure {
            statusMessage = "برنامه‌ای برای باز کردن این لینک پیدا نشد."
        }
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text("مکان‌های من", fontWeight = FontWeight.Bold)
                            Text(
                                "§{locations.size} مکان ذخیره شده",
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "بازگشت")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                val result = LocationShareCodec.import(readClipboard())
                                if (result.error != null) {
                                    importError = result.error
                                } else {
                                    importCandidates = result.locations
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.ContentPaste,
                                contentDescription = "دریافت مکان‌ها"
                            )
                        }
                        IconButton(
                            onClick = {
                                selectedIds =
                                    if (selectedIds.size == locations.size) emptySet()
                                    else locations.map { it.id }.toSet()
                            },
                            enabled = locations.isNotEmpty()
                        ) {
                            Icon(
                                Icons.Default.SelectAll,
                                contentDescription = "انتخاب همه"
                            )
                        }
                        IconButton(
                            onClick = {
                                shareLocations(locations.filter { it.id in selectedIds })
                            },
                            enabled = selectedIds.isNotEmpty()
                        ) {
                            Icon(
                                Icons.Default.Share,
                                contentDescription = "اشتراک‌گذاری"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            floatingActionButton = {
                Button(
                    onClick = {
                        editing = null
                        showEditor = true
                    },
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.size(6.dp))
                    Text("افزودن مکان")
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.86f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            "مکان‌های مهم سفرت را یکجا نگه دار.",
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "هتل، نمایشگاه، رستوران و هر مکان دیگری را ذخیره کن، حذف یا ویرایش کن و چند مکان را با یک پیام به دیگران بده.",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            "ثبت مکان با Google Maps: لینک Share را از Google Maps کپی و در فرم Paste کن.",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                statusMessage?.let { message ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    ) {
                        Text(
                            message,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                if (locations.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.86f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(42.dp)
                            )
                            Text(
                                "هنوز مکانی ثبت نکرده‌اید.",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "اولین مکان را با لینک Google Maps ثبت کنید.",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            OutlinedButton(
                                onClick = {
                                    editing = null
                                    showEditor = true
                                }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null)
                                Spacer(Modifier.size(6.dp))
                                Text("ثبت اولین مکان")
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(locations, key = { it.id }) { location ->
                            LocationCard(
                                location = location,
                                selected = location.id in selectedIds,
                                onToggle = {
                                    selectedIds =
                                        if (location.id in selectedIds) {
                                            selectedIds - location.id
                                        } else {
                                            selectedIds + location.id
                                        }
                                },
                                onMap = { openMaps(location) },
                                onEdit = {
                                    editing = location
                                    showEditor = true
                                },
                                onDelete = {
                                    scope.launch {
                                        database.remove(location.id)
                                        selectedIds = selectedIds - location.id
                                        statusMessage = "مکان حذف شد."
                                    }
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(4.dp))
            }
        }
    }

    if (showEditor) {
        LocationEditorDialog(
            initial = editing,
            onDismiss = { showEditor = false },
            onSave = { location ->
                scope.launch {
                    val success = if (editing == null) {
                        database.add(location)
                    } else {
                        database.update(location)
                    }

                    if (success) {
                        showEditor = false
                        statusMessage =
                            if (editing == null) "مکان با موفقیت ذخیره شد."
                            else "مکان با موفقیت ویرایش شد."
                    } else {
                        statusMessage = "ذخیره مکان انجام نشد."
                    }
                }
            }
        )
    }

    importCandidates?.let { candidates ->
        ImportLocationsDialog(
            locations = candidates,
            existing = locations,
            onDismiss = { importCandidates = null },
            onConfirm = { selected ->
                scope.launch {
                    val existingKeys = locations
                        .map { it.identityKey() }
                        .toHashSet()

                    val filtered = selected.filter {
                        it.identityKey() !in existingKeys
                    }

                    val inserted = database.addImported(filtered)
                    importCandidates = null

                    statusMessage = when {
                        selected.isEmpty() -> "هیچ مکانی انتخاب نشد."
                        inserted == 0 -> "همه‌ی مکان‌های انتخاب‌شده قبلاً ثبت شده بودند."
                        inserted < selected.size ->
                            "§{inserted} مکان جدید اضافه شد؛ موارد تکراری نادیده گرفته شدند."
                        else -> "§{inserted} مکان با موفقیت اضافه شد."
                    }
                }
            }
        )
    }

    importError?.let { error ->
        AlertDialog(
            onDismissRequest = { importError = null },
            title = { Text("دریافت مکان‌ها") },
            text = { Text(error) },
            confirmButton = {
                TextButton(onClick = { importError = null }) {
                    Text("متوجه شدم")
                }
            }
        )
    }
}

@Composable
private fun LocationCard(
    location: SavedLocation,
    selected: Boolean,
    onToggle: () -> Unit,
    onMap: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.88f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onToggle) {
                    Icon(
                        imageVector =
                            if (selected) Icons.Default.CheckBox
                            else Icons.Default.CheckBoxOutlineBlank,
                        contentDescription =
                            if (selected) "لغو انتخاب" else "انتخاب مکان",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(location.name, fontWeight = FontWeight.Bold)
                    Text(
                        location.category,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (location.description.isNotBlank()) {
                Text(
                    location.description,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (location.address.isNotBlank()) {
                Text(
                    location.address,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onMap) {
                    Icon(
                        Icons.Default.Map,
                        contentDescription = "باز کردن در Google Maps"
                    )
                }
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "ویرایش")
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "حذف",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationEditorDialog(
    initial: SavedLocation?,
    onDismiss: () -> Unit,
    onSave: (SavedLocation) -> Unit
) {
    var name by remember(initial) { mutableStateOf(initial?.name.orEmpty()) }
    var description by remember(initial) { mutableStateOf(initial?.description.orEmpty()) }
    var category by remember(initial) { mutableStateOf(initial?.category ?: "شخصی") }
    var mapsUrl by remember(initial) { mutableStateOf(initial?.mapsUrl.orEmpty()) }
    var address by remember(initial) { mutableStateOf(initial?.address.orEmpty()) }
    var error by remember(initial) { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (initial == null) "افزودن مکان" else "ویرایش مکان",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        error = null
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("نام مکان *") },
                    placeholder = { Text("مثلاً هتل من") }
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("نوع مکان") },
                    placeholder = { Text(LocationCategories.joinToString("، ")) }
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4,
                    label = { Text("توضیحات") },
                    placeholder = { Text("مثلاً نزدیک ورودی جنوبی نمایشگاه") }
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 1,
                    maxLines = 2,
                    label = { Text("آدرس، در صورت نیاز") }
                )

                OutlinedTextField(
                    value = mapsUrl,
                    onValueChange = {
                        mapsUrl = it
                        error = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 1,
                    maxLines = 3,
                    label = { Text("لینک Google Maps *") },
                    placeholder = {
                        Text("لینک Share از Google Maps را Paste کنید")
                    }
                )

                Text(
                    "Google Maps → Share → Copy link → اینجا Paste",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                error?.let {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cleanName = name.trim()
                    val cleanUrl = mapsUrl.trim()
                    val uri = runCatching { Uri.parse(cleanUrl) }.getOrNull()
                    val validUrl =
                        uri != null && uri.scheme?.lowercase() in setOf("http", "https")

                    error = when {
                        cleanName.isBlank() -> "نام مکان را وارد کنید."
                        cleanUrl.isBlank() -> "لینک Google Maps را وارد کنید."
                        !validUrl -> "لینک Google Maps باید یک لینک http یا https باشد."
                        else -> null
                    }

                    if (error == null) {
                        val coordinates =
                            LocationShareCodec.extractCoordinates(cleanUrl)

                        onSave(
                            SavedLocation(
                                id = initial?.id ?: UUID.randomUUID().toString(),
                                name = cleanName,
                                description = description.trim(),
                                category = category.trim().ifBlank { "شخصی" },
                                mapsUrl = cleanUrl,
                                address = address.trim(),
                                latitude = coordinates?.first,
                                longitude = coordinates?.second,
                                createdAt = initial?.createdAt
                                    ?: System.currentTimeMillis(),
                                updatedAt = System.currentTimeMillis()
                            )
                        )
                    }
                }
            ) {
                Text("ذخیره")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        }
    )
}

@Composable
private fun ImportLocationsDialog(
    locations: List<SavedLocation>,
    existing: List<SavedLocation>,
    onDismiss: () -> Unit,
    onConfirm: (List<SavedLocation>) -> Unit
) {
    val duplicateKeys = remember(existing) {
        existing.map { it.identityKey() }.toHashSet()
    }

    var selected by remember(locations, duplicateKeys) {
        mutableStateOf(
            locations
                .filter { it.identityKey() !in duplicateKeys }
                .toSet()
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "§{locations.size} مکان دریافت شد",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "موارد تکراری از قبل شناسایی می‌شوند و دوباره ذخیره نمی‌شوند.",
                    style = MaterialTheme.typography.bodySmall
                )

                LazyColumn(
                    modifier = Modifier.height(300.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(locations) { location ->
                        val duplicate = location.identityKey() in duplicateKeys

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = location in selected,
                                onCheckedChange = {
                                    selected =
                                        if (it) selected + location
                                        else selected - location
                                },
                                enabled = !duplicate
                            )

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    location.name,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    if (duplicate) "قبلاً ثبت شده"
                                    else location.category,
                                    style = MaterialTheme.typography.labelSmall,
                                    color =
                                        if (duplicate)
                                            MaterialTheme.colorScheme.error
                                        else
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selected.toList()) }
            ) {
                Text("افزودن §{selected.size} مکان")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("انصراف")
            }
        }
    )
}

private fun SavedLocation.identityKey(): String =
    "§{name.trim().lowercase()}|§{mapsUrl.trim().lowercase()}"
