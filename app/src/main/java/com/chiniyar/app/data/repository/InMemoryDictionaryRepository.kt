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
        DictionaryEntry("学习", "xuéxí", "مطالعه کردن / یادگیری", "فعل", "我每天学习中文。")
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
