package com.chiniyar.app.data.repository

import com.chiniyar.app.core.model.DictionaryEntry
import com.chiniyar.app.domain.repository.DictionaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InMemoryDictionaryRepository : DictionaryRepository {
    private val entries = listOf(
        DictionaryEntry("你好", "nǐ hǎo", "سلام", "عبارت", "你好！很高兴认识你。"),
        DictionaryEntry("谢谢", "xièxie", "ممنون / متشکرم", "عبارت", "谢谢你的帮助。"),
        DictionaryEntry("再见", "zàijiàn", "خداحافظ", "عبارت", "明天再见。"),
        DictionaryEntry("中国", "Zhōngguó", "چین", "اسم", "中国是一个很大的国家。"),
        DictionaryEntry("北京", "Běijīng", "پکن", "اسم", "北京是中国的首都。"),
        DictionaryEntry("上海", "Shànghǎi", "شانگهای", "اسم", "上海是一个国际化城市。"),
        DictionaryEntry("学习", "xuéxí", "مطالعه کردن / یادگیری", "فعل", "我每天学习中文。"),

        // 30 essential travel phrases.
        DictionaryEntry("请问，洗手间在哪里？", "qǐngwèn, xǐshǒujiān zài nǎlǐ?", "ببخشید، سرویس بهداشتی کجاست؟", "سفر", "请问，洗手间在哪里？"),
        DictionaryEntry("多少钱？", "duōshao qián?", "چقدر قیمت دارد؟", "سفر", "这个多少钱？"),
        DictionaryEntry("太贵了。", "tài guì le.", "خیلی گران است.", "سفر", "这个太贵了。"),
        DictionaryEntry("可以便宜一点吗？", "kěyǐ piányi yìdiǎn ma?", "می‌شود کمی ارزان‌تر حساب کنید؟", "سفر", "可以便宜一点吗？"),
        DictionaryEntry("我不要这个。", "wǒ bú yào zhège.", "این را نمی‌خواهم.", "سفر", "我不要这个，谢谢。"),
        DictionaryEntry("我要这个。", "wǒ yào zhège.", "این را می‌خواهم.", "سفر", "我要这个。"),
        DictionaryEntry("请给我一个。", "qǐng gěi wǒ yí ge.", "لطفاً یکی به من بدهید.", "سفر", "请给我一个。"),
        DictionaryEntry("我听不懂。", "wǒ tīng bù dǒng.", "متوجه نمی‌شوم.", "سفر", "对不起，我听不懂。"),
        DictionaryEntry("请说慢一点。", "qǐng shuō màn yìdiǎn.", "لطفاً کمی آهسته‌تر صحبت کنید.", "سفر", "请说慢一点。"),
        DictionaryEntry("你会说英语吗？", "nǐ huì shuō Yīngyǔ ma?", "انگلیسی صحبت می‌کنید؟", "سفر", "你会说英语吗？"),
        DictionaryEntry("我不会说中文。", "wǒ bú huì shuō Zhōngwén.", "من چینی صحبت نمی‌کنم.", "سفر", "对不起，我不会说中文。"),
        DictionaryEntry("请帮帮我。", "qǐng bāngbang wǒ.", "لطفاً به من کمک کنید.", "سفر", "请帮帮我，我迷路了。"),
        DictionaryEntry("我迷路了。", "wǒ mílù le.", "گم شده‌ام.", "سفر", "对不起，我迷路了。"),
        DictionaryEntry("这里怎么走？", "zhèlǐ zěnme zǒu?", "از اینجا چطور برویم؟", "سفر", "去地铁站怎么走？"),
        DictionaryEntry("地铁站在哪里？", "dìtiě zhàn zài nǎlǐ?", "ایستگاه مترو کجاست؟", "سفر", "最近的地铁站在哪里？"),
        DictionaryEntry("火车站在哪里？", "huǒchē zhàn zài nǎlǐ?", "ایستگاه قطار کجاست؟", "سفر", "火车站在哪里？"),
        DictionaryEntry("机场在哪里？", "jīchǎng zài nǎlǐ?", "فرودگاه کجاست؟", "سفر", "机场在哪里？"),
        DictionaryEntry("请带我去这个地址。", "qǐng dài wǒ qù zhège dìzhǐ.", "لطفاً مرا به این آدرس ببرید.", "سفر", "请带我去这个地址。"),
        DictionaryEntry("我想去……", "wǒ xiǎng qù…", "می‌خواهم به … بروم.", "سفر", "我想去北京。"),
        DictionaryEntry("出租车在哪里？", "chūzūchē zài nǎlǐ?", "تاکسی کجاست؟", "سفر", "出租车在哪里？"),
        DictionaryEntry("请打表。", "qǐng dǎbiǎo.", "لطفاً تاکسی‌متر را روشن کنید.", "سفر", "请打表，谢谢。"),
        DictionaryEntry("可以刷卡吗？", "kěyǐ shuākǎ ma?", "می‌توانم با کارت پرداخت کنم؟", "سفر", "这里可以刷卡吗？"),
        DictionaryEntry("可以用手机支付吗？", "kěyǐ yòng shǒujī zhīfù ma?", "می‌توانم با موبایل پرداخت کنم؟", "سفر", "可以用手机支付吗？"),
        DictionaryEntry("我要买这个。", "wǒ yào mǎi zhège.", "می‌خواهم این را بخرم.", "سفر", "我要买这个。"),
        DictionaryEntry("不要辣。", "bú yào là.", "تند نباشد / فلفل نداشته باشد.", "رستوران", "请不要辣。"),
        DictionaryEntry("少放一点辣椒。", "shǎo fàng yìdiǎn làjiāo.", "لطفاً کمی فلفل کمتری بریزید.", "رستوران", "少放一点辣椒，谢谢。"),
        DictionaryEntry("我不吃猪肉。", "wǒ bù chī zhūròu.", "من گوشت خوک نمی‌خورم.", "رستوران", "对不起，我不吃猪肉。"),
        DictionaryEntry("有没有素食？", "yǒu méiyǒu sùshí?", "غذای گیاهی دارید؟", "رستوران", "请问，有没有素食？"),
        DictionaryEntry("请给我菜单。", "qǐng gěi wǒ càidān.", "لطفاً منو را بدهید.", "رستوران", "请给我菜单，谢谢。"),
        DictionaryEntry("我要结账。", "wǒ yào jiézhàng.", "می‌خواهم حساب کنم / صورتحساب را می‌خواهم.", "رستوران", "请帮我结账。")
    )

    override fun search(query: String): Flow<List<DictionaryEntry>> = flow {
        val normalized = query.trim()
        emit(if (normalized.isEmpty()) entries else entries.filter {
            it.hanzi.contains(normalized, ignoreCase = true) ||
                it.pinyin.contains(normalized, ignoreCase = true) ||
                it.meaningFa.contains(normalized, ignoreCase = true)
        })
    }
}
