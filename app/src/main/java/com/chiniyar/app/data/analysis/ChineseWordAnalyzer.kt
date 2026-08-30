package com.chiniyar.app.data.analysis

import net.sourceforge.pinyin4j.PinyinHelper

/** Lightweight on-device Chinese word extraction and pinyin conversion. */
class ChineseWordAnalyzer {
    private val lexicon = setOf(
        "你好", "您好", "谢谢", "再见", "对不起", "没关系", "不客气", "请问", "可以", "不可以",
        "喜欢", "不喜欢", "学习", "中文", "汉语", "中国", "中国人", "今天", "明天", "昨天", "现在",
        "时间", "时候", "什么", "为什么", "怎么", "哪里", "哪个", "多少", "非常", "真的", "已经",
        "正在", "一起", "需要", "知道", "认识", "觉得", "希望", "开始", "结束", "工作", "学校",
        "老师", "学生", "朋友", "家人", "孩子", "爸爸", "妈妈", "哥哥", "姐姐", "弟弟", "妹妹",
        "吃饭", "喝水", "睡觉", "起床", "上班", "回家", "买东西", "东西", "商店", "饭店", "医院",
        "机场", "车站", "地铁", "出租车", "飞机", "火车", "汽车", "手机", "电脑", "电话", "问题",
        "办法", "机会", "世界", "生活", "国家", "城市", "北京", "上海", "广州", "深圳", "日本",
        "美国", "英国", "人民币", "美元", "价格", "便宜", "贵", "好吃", "漂亮", "高兴", "快乐",
        "天气", "下雨", "下雪", "太阳", "春天", "夏天", "秋天", "冬天", "早上", "晚上", "下午",
        "上午", "这里", "那里", "因为", "所以", "但是", "如果", "虽然", "然后", "还有",
        "没有", "不是", "不要", "不能", "不会", "应该", "可能", "当然", "还要", "找到",
        "看到", "听到", "告诉", "帮助", "使用", "打开", "关闭", "下载", "安装", "信息", "语言"
    )

    fun segment(text: String): List<String> {
        val chars = text.filter(::isChinese)
        if (chars.isBlank()) return emptyList()
        val result = mutableListOf<String>()
        var index = 0
        while (index < chars.length) {
            var match: String? = null
            for (length in minOf(4, chars.length - index) downTo 2) {
                val candidate = chars.substring(index, index + length)
                if (candidate in lexicon) {
                    match = candidate
                    break
                }
            }
            if (match == null) match = chars[index].toString()
            result += match
            index += match.length
        }
        return result.distinct().take(20)
    }

    /** Returns numeric-tone pinyin, e.g. 学习 -> xue2 xi2. */
    fun pinyin(word: String): String = buildString {
        word.forEachIndexed { index, char ->
            if (index > 0) append(' ')
            append(PinyinHelper.toHanyuPinyinStringArray(char)?.firstOrNull() ?: char)
        }
    }

    private fun isChinese(char: Char): Boolean =
        char.code in 0x3400..0x4DBF || char.code in 0x4E00..0x9FFF
}
