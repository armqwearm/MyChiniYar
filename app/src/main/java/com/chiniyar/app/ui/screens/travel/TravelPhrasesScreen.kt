package com.chiniyar.app.ui.screens.travel

import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import java.util.Locale

private data class TravelPhrase(val chinese: String, val pinyin: String, val persian: String)

private val travelPhrases = listOf(
    TravelPhrase("你好", "Nǐ hǎo", "سلام"), TravelPhrase("谢谢", "Xièxie", "ممنون"),
    TravelPhrase("不客气", "Bú kèqi", "خواهش می‌کنم"), TravelPhrase("对不起", "Duìbuqǐ", "ببخشید / متأسفم"),
    TravelPhrase("没关系", "Méi guānxi", "اشکالی ندارد"), TravelPhrase("请问，洗手间在哪里？", "Qǐngwèn, xǐshǒujiān zài nǎlǐ?", "ببخشید، سرویس بهداشتی کجاست؟"),
    TravelPhrase("这个多少钱？", "Zhège duōshao qián?", "این چقدر قیمت دارد؟"), TravelPhrase("太贵了", "Tài guì le", "خیلی گران است"),
    TravelPhrase("可以便宜一点吗？", "Kěyǐ piányi yìdiǎn ma?", "می‌شود کمی ارزان‌تر حساب کنید؟"), TravelPhrase("我要这个", "Wǒ yào zhège", "این را می‌خواهم"),
    TravelPhrase("不要辣", "Bú yào là", "تند نباشد"), TravelPhrase("我不吃猪肉", "Wǒ bù chī zhūròu", "من گوشت خوک نمی‌خورم"),
    TravelPhrase("请给我一瓶水", "Qǐng gěi wǒ yì píng shuǐ", "لطفاً یک بطری آب به من بدهید"), TravelPhrase("我想去这里", "Wǒ xiǎng qù zhèlǐ", "می‌خواهم به اینجا بروم"),
    TravelPhrase("请带我去这个地址", "Qǐng dài wǒ qù zhège dìzhǐ", "لطفاً من را به این آدرس ببرید"), TravelPhrase("地铁站在哪里？", "Dìtiě zhàn zài nǎlǐ?", "ایستگاه مترو کجاست؟"),
    TravelPhrase("火车站在哪里？", "Huǒchē zhàn zài nǎlǐ?", "ایستگاه قطار کجاست؟"), TravelPhrase("机场怎么走？", "Jīchǎng zěnme zǒu?", "چطور به فرودگاه بروم؟"),
    TravelPhrase("请帮帮我", "Qǐng bāngbang wǒ", "لطفاً به من کمک کنید"), TravelPhrase("我听不懂", "Wǒ tīng bù dǒng", "متوجه نمی‌شوم"),
    TravelPhrase("请说慢一点", "Qǐng shuō màn yìdiǎn", "لطفاً کمی آهسته‌تر صحبت کنید"), TravelPhrase("你会说英语吗？", "Nǐ huì shuō Yīngyǔ ma?", "انگلیسی صحبت می‌کنید؟"),
    TravelPhrase("请写下来", "Qǐng xiě xiàlái", "لطفاً آن را بنویسید"), TravelPhrase("我需要医生", "Wǒ xūyào yīshēng", "به پزشک نیاز دارم"),
    TravelPhrase("医院在哪里？", "Yīyuàn zài nǎlǐ?", "بیمارستان کجاست؟"), TravelPhrase("我迷路了", "Wǒ mílù le", "گم شده‌ام"),
    TravelPhrase("有无线网络吗？", "Yǒu wúxiàn wǎngluò ma?", "وای‌فای دارید؟"), TravelPhrase("密码是多少？", "Mìmǎ shì duōshao?", "رمز چیست؟"),
    TravelPhrase("请给我一张票", "Qǐng gěi wǒ yì zhāng piào", "لطفاً یک بلیت به من بدهید"), TravelPhrase("我要结账", "Wǒ yào jiézhàng", "می‌خواهم حساب کنم")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelPhrasesScreen(onBack: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val context = LocalContext.current
    val mainHandler = remember { Handler(Looper.getMainLooper()) }
    var isSpeaking by remember { mutableStateOf(false) }
    var speakingPhrase by remember { mutableStateOf<String?>(null) }
    var ttsError by remember { mutableStateOf(false) }
    var ttsReady by remember { mutableStateOf(false) }
    var pendingPhrase by remember { mutableStateOf<TravelPhrase?>(null) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    DisposableEffect(context) {
        var initializedEngine: TextToSpeech? = null
        initializedEngine = TextToSpeech(context) { status ->
            mainHandler.post {
                val engine = initializedEngine ?: tts
                if (status != TextToSpeech.SUCCESS || engine == null) { ttsReady = false; ttsError = true; pendingPhrase = null; return@post }
                val languageResult = engine.setLanguage(Locale.SIMPLIFIED_CHINESE)
                if (languageResult == TextToSpeech.LANG_MISSING_DATA || languageResult == TextToSpeech.LANG_NOT_SUPPORTED) { ttsReady = false; ttsError = true; pendingPhrase = null; return@post }
                ttsReady = true; ttsError = false
                pendingPhrase?.let { phrase -> pendingPhrase = null; engine.stop(); speakingPhrase = phrase.chinese; isSpeaking = true; engine.speak(phrase.chinese, TextToSpeech.QUEUE_FLUSH, null, "travel_${phrase.chinese.hashCode()}") }
            }
        }
        tts = initializedEngine
        initializedEngine?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) { mainHandler.post { isSpeaking = true; ttsError = false } }
            override fun onDone(utteranceId: String?) { mainHandler.post { isSpeaking = false; speakingPhrase = null } }
            override fun onError(utteranceId: String?) { mainHandler.post { isSpeaking = false; speakingPhrase = null; ttsError = true } }
        })
        onDispose { initializedEngine?.stop(); initializedEngine?.shutdown(); tts = null; ttsReady = false; pendingPhrase = null }
    }

    fun speak(phrase: TravelPhrase) {
        val engine = tts
        if (engine == null || !ttsReady) { pendingPhrase = phrase; ttsError = false; return }
        val result = engine.setLanguage(Locale.SIMPLIFIED_CHINESE)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) { ttsReady = false; pendingPhrase = null; ttsError = true; return }
        engine.stop(); ttsError = false; speakingPhrase = phrase.chinese; isSpeaking = true
        engine.speak(phrase.chinese, TextToSpeech.QUEUE_FLUSH, null, "travel_${phrase.chinese.hashCode()}")
    }

    fun stopSpeaking() { pendingPhrase = null; tts?.stop(); isSpeaking = false; speakingPhrase = null }

    Scaffold(containerColor = colors.background, topBar = {
        TopAppBar(title = { Text("عبارات سفر", fontWeight = FontWeight.Bold) }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "بازگشت") } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = colors.background))
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = colors.primaryContainer)) {
                Column(modifier = Modifier.fillMaxWidth().padding(18.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(7.dp)) {
                    Text("۳۰ عبارت ضروری سفر به چین", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
                    Text("برای هر عبارت روی آیکون 🔊 بزنید تا تلفظ چینی پخش شود.", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
                }
            }
            if (ttsError) Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = colors.errorContainer), shape = RoundedCornerShape(16.dp)) {
                Text("تلفظ چینی روی دستگاه در دسترس نیست. بسته صدای زبان چینی را در تنظیمات Text-to-Speech دستگاه نصب یا فعال کنید.", modifier = Modifier.fillMaxWidth().padding(14.dp), style = MaterialTheme.typography.bodyMedium, color = colors.onErrorContainer, textAlign = TextAlign.Center)
            }
            if (isSpeaking) Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = colors.secondaryContainer), shape = RoundedCornerShape(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("در حال پخش تلفظ…", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                    IconButton(onClick = ::stopSpeaking) { Icon(Icons.Default.Stop, contentDescription = "توقف پخش") }
                }
            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().weight(1f)) {
                items(travelPhrases) { phrase ->
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(containerColor = colors.surface), elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)) {
                        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            IconButton(onClick = { if (speakingPhrase == phrase.chinese) stopSpeaking() else speak(phrase) }, modifier = Modifier.size(46.dp).background(colors.primaryContainer, RoundedCornerShape(14.dp))) { Icon(if (speakingPhrase == phrase.chinese) Icons.Default.Stop else Icons.Default.VolumeUp, contentDescription = if (speakingPhrase == phrase.chinese) "توقف تلفظ" else "پخش تلفظ", tint = colors.primary) }
                            Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
                                Text(phrase.chinese, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                                Text(phrase.pinyin, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium, color = colors.secondary, textAlign = TextAlign.Center)
                                Text(phrase.persian, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyLarge, color = colors.onSurfaceVariant, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
                item { Spacer(Modifier.size(12.dp)) }
            }
        }
    }
}
