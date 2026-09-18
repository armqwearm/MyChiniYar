package com.chiniyar.app.ui.screens.exhibitions

data class ChinaExhibition(
    val title: String,
    val date: String,
    val city: String,
    val venue: String,
    val category: String,
    val summary: String,
    val usefulFor: String,
    val officialUrl: String
)

val chinaExhibitions = listOf(
    ChinaExhibition(
        title = "نمایشگاه کانتون (Canton Fair) – دوره پاییز",
        date = "۱۵–۱۹ اکتبر، ۲۳–۲۷ اکتبر، ۳۱ اکتبر–۴ نوامبر ۲۰۲۶",
        city = "گوانگژو",
        venue = "Canton Fair Complex",
        category = "خرید و تجارت عمومی",
        summary = "یکی از مهم‌ترین رویدادهای تأمین کالا در چین با سه فاز. فاز اول روی لوازم خانگی، الکترونیک، اتوماسیون صنعتی، ماشین‌آلات، برق، خودرو و قطعات و سخت‌افزار متمرکز است؛ فاز دوم کالاهای خانه، دکور، هدیه و مبلمان و فاز سوم پوشاک، اسباب‌بازی، سلامت، تجهیزات پزشکی، غذا و کالاهای مصرفی را پوشش می‌دهد.",
        usefulFor = "واردات کالا، پیدا کردن تأمین‌کننده، الکترونیک، تجهیزات صنعتی، کالاهای مصرفی",
        officialUrl = "https://www.cantonfair.org.cn/en-US?oid=25236"
    ),
    ChinaExhibition(
        title = "China International Industry Fair (CIIF)",
        date = "۱۲–۱۶ اکتبر ۲۰۲۶",
        city = "شانگهای",
        venue = "National Exhibition and Convention Center (Shanghai)",
        category = "صنعت و اتوماسیون",
        summary = "نمایشگاه جامع صنعتی برای مشاهده فناوری‌ها، تجهیزات و راهکارهای تولید، رباتیک، اتوماسیون و زنجیره صنعتی. برای بازدید صنعتی و مذاکره با سازندگان تجهیزات مناسب است.",
        usefulFor = "اتوماسیون، رباتیک، ماشین‌آلات، تولید، برق و الکترونیک صنعتی",
        officialUrl = "https://www.ciif-expos.cn/"
    ),
    ChinaExhibition(
        title = "CMEF – China International Medical Equipment Fair",
        date = "۲۱–۲۴ اکتبر ۲۰۲۶",
        city = "پکن",
        venue = "Capital International Exhibition & Convention Center",
        category = "پزشکی و سلامت",
        summary = "رویداد تخصصی تجهیزات پزشکی با حوزه‌هایی مانند تصویربرداری پزشکی، تشخیص آزمایشگاهی، رباتیک جراحی، سلامت هوشمند و کاربردهای هوش مصنوعی در پزشکی.",
        usefulFor = "تجهیزات پزشکی، آزمایشگاهی، بیمارستانی، سلامت هوشمند",
        officialUrl = "https://www.cmef.com.cn/en"
    ),
    ChinaExhibition(
        title = "CIAME – نمایشگاه ماشین‌آلات کشاورزی چین",
        date = "۲۶–۲۸ اکتبر ۲۰۲۶",
        city = "تیانجین",
        venue = "Tianjin, China",
        category = "کشاورزی و ماشین‌آلات",
        summary = "نمایشگاه تخصصی ماشین‌آلات و تجهیزات کشاورزی و قطعات. برای پیدا کردن سازنده، نماینده و تأمین‌کننده تجهیزات کشاورزی و بررسی فناوری‌های جدید مفید است.",
        usefulFor = "ماشین‌آلات کشاورزی، قطعات، تجهیزات مزرعه و واردات",
        officialUrl = "https://en.camf.com.cn/"
    ),
    ChinaExhibition(
        title = "CIIE – China International Import Expo",
        date = "۵–۱۰ نوامبر ۲۰۲۶",
        city = "شانگهای",
        venue = "National Exhibition and Convention Center (Shanghai)",
        category = "تجارت بین‌الملل",
        summary = "نمایشگاه ملی با تمرکز بر واردات به چین؛ حوزه‌های آن خودرو و حمل‌ونقل هوشمند، تجهیزات پزشکی، صنعت هوشمند و فناوری اطلاعات، کالاهای مصرفی، غذا و کشاورزی و خدمات تجاری است.",
        usefulFor = "ورود به بازار چین، صادرات به چین، غذا، فناوری، تجهیزات پزشکی و مصرفی",
        officialUrl = "https://www.ciie.org/"
    ),
    ChinaExhibition(
        title = "bauma CHINA",
        date = "۲۳–۲۶ نوامبر ۲۰۲۶ و ۲۴–۲۷ نوامبر ۲۰۲۶",
        city = "شانگهای",
        venue = "SWEECC و SNIEC، پودونگ",
        category = "ماشین‌آلات سنگین",
        summary = "نمایشگاه تخصصی ماشین‌آلات ساختمانی، معدنی، ماشین‌آلات مصالح ساختمانی و خودروهای عمرانی. در سال ۲۰۲۶ با مدل «یک نمایشگاه، دو محل» برگزار می‌شود و امکان بازدید از دو مرکز نمایشگاهی با یک نشان ورود فراهم شده است.",
        usefulFor = "ماشین‌آلات عمرانی، معدن، قطعات هیدرولیک، تجهیزات ساخت‌وساز",
        officialUrl = "https://bauma-china.com/en/"
    ),
    ChinaExhibition(
        title = "Automechanika Shanghai",
        date = "۲–۵ دسامبر ۲۰۲۶",
        city = "شانگهای",
        venue = "National Exhibition and Convention Center (Shanghai)",
        category = "خودرو و قطعات",
        summary = "رویداد بزرگ زنجیره خودروی چین با تمرکز بر قطعات، تجهیزات تعمیرگاهی، خودروهای انرژی نو، ارتباط هوشمند، ساخت هوشمند و فناوری‌های خودرو. برنامه تخصصی آن نشست‌ها و سمینارهای فنی متعددی دارد.",
        usefulFor = "قطعات خودرو، تعمیرگاه، تجهیزات خدمات خودرو، EV و قطعات الکترونیکی خودرو",
        officialUrl = "https://automechanika-shanghai.hk.messefrankfurt.com/shanghai/en.html"
    ),
    ChinaExhibition(
        title = "SIOF – نمایشگاه بین‌المللی اپتیک شانگهای",
        date = "۲۳–۲۵ فوریه ۲۰۲۷",
        city = "شانگهای",
        venue = "Shanghai New International Expo Centre",
        category = "اپتیک و عینک",
        summary = "نمایشگاه تخصصی صنعت عینک و اپتیک؛ مناسب برای شناخت تولیدکنندگان، برندها، فناوری و تجهیزات اپتومتری و زنجیره تأمین این صنعت.",
        usefulFor = "عینک، لنز، تجهیزات اپتومتری و واردات",
        officialUrl = "https://www.siof.cn/en/"
    ),
    ChinaExhibition(
        title = "SEMICON China",
        date = "۲۴–۲۶ مارس ۲۰۲۷",
        city = "شانگهای",
        venue = "Shanghai New International Expo Centre (SNIEC)",
        category = "نیمه‌رسانا و الکترونیک",
        summary = "رویداد تخصصی زنجیره تولید نیمه‌رسانا؛ از تجهیزات و مواد تا طراحی، ساخت، بسته‌بندی و تست. برای دنبال کردن فناوری‌های ساخت تراشه و ملاقات با تأمین‌کنندگان صنعتی مناسب است.",
        usefulFor = "نیمه‌رسانا، تجهیزات تولید، مواد، MEMS، سنسور، طراحی و تولید الکترونیک",
        officialUrl = "https://www.semiconchina.org/en/1"
    ),
    ChinaExhibition(
        title = "CIFF Guangzhou – نمایشگاه بین‌المللی مبلمان چین",
        date = "۱۸–۲۱ مارس و ۲۸–۳۱ مارس ۲۰۲۷",
        city = "گوانگژو",
        venue = "Canton Fair Complex / PWTC Expo",
        category = "مبلمان و خانه",
        summary = "دو فاز تخصصی برای مبلمان منزل، دکور و منسوجات خانه، فضای باز و سپس فضاهای اداری و تجاری و فناوری ساخت مبلمان. پوشش گسترده زنجیره تأمین این صنعت، آن را برای خریداران و واردکنندگان کاربردی می‌کند.",
        usefulFor = "مبلمان، دکوراسیون، تجهیزات و مواد اولیه مبلمان، هتل و دفتر",
        officialUrl = "https://www.cifffurniturefair.com/"
    ),
    ChinaExhibition(
        title = "CMEF Spring – نمایشگاه تجهیزات پزشکی چین",
        date = "۹–۱۲ آوریل ۲۰۲۷",
        city = "شانگهای",
        venue = "National Exhibition and Convention Center (Shanghai)",
        category = "پزشکی و سلامت",
        summary = "دوره بهاره CMEF برای آشنایی با سازندگان، فناوری‌ها و محصولات جدید زنجیره تجهیزات پزشکی و سلامت. اطلاعات ثبت‌نام بازدیدکنندگان از سایت رسمی منتشر می‌شود.",
        usefulFor = "تجهیزات پزشکی، بیمارستانی، تشخیصی، سلامت و فناوری پزشکی",
        officialUrl = "https://www.cmef.com.cn/en"
    ),
    ChinaExhibition(
        title = "CIMT – China International Machine Tool Show",
        date = "۱۹–۲۴ آوریل ۲۰۲۷",
        city = "پکن",
        venue = "نمایشگاه بین‌المللی پکن",
        category = "ماشین‌ابزار و تولید",
        summary = "نمایشگاه تخصصی ماشین‌ابزار و فناوری تولید؛ مناسب برای بررسی ماشین‌های CNC، تجهیزات تولید، ابزار و راهکارهای کارخانه‌ای و پیدا کردن سازندگان و تأمین‌کنندگان صنعتی.",
        usefulFor = "CNC، ماشین‌ابزار، ابزار برش، تولید صنعتی و تجهیزات کارخانه",
        officialUrl = "https://www.cimtshow.com/"
    ),
    ChinaExhibition(
        title = "Bakery China",
        date = "۱۷–۲۰ مه ۲۰۲۷",
        city = "شانگهای",
        venue = "National Exhibition and Convention Center (Shanghai)",
        category = "غذا و تجهیزات غذایی",
        summary = "نمایشگاه تخصصی زنجیره کامل نانوایی و شیرینی شامل مواد اولیه، تجهیزات، بسته‌بندی و خدمات. هم‌زمان رویدادهای تخصصی غذا و نوشیدنی نیز برگزار می‌شوند.",
        usefulFor = "مواد غذایی، تجهیزات نانوایی، بسته‌بندی و واردات",
        officialUrl = "https://www.bakerychina.com/en/page/bakery-china"
    ),
    ChinaExhibition(
        title = "SIAL Shanghai",
        date = "۱۸–۲۰ مه ۲۰۲۷",
        city = "شانگهای",
        venue = "Shanghai New International Expo Centre",
        category = "غذا و نوشیدنی",
        summary = "نمایشگاه بین‌المللی غذا و نوشیدنی با تمرکز بر تأمین جهانی، مواد غذایی، لبنیات، غذاهای آماده، غلات و نوشیدنی‌ها و ارتباط بین خریداران و تأمین‌کنندگان.",
        usefulFor = "غذا، نوشیدنی، صادرات به چین، تأمین مواد غذایی و توزیع",
        officialUrl = "https://www.sialchina.com/"
    ),
    ChinaExhibition(
        title = "Prolight + Sound Guangzhou",
        date = "۲۷–۳۰ مه ۲۰۲۷",
        city = "گوانگژو",
        venue = "Guangzhou, China",
        category = "صوت، نور و تجهیزات رویداد",
        summary = "نمایشگاه تخصصی تجهیزات صوتی، نوری و فناوری‌های رویداد و سرگرمی. ورود برای بازدیدکنندگان تجاری با ثبت‌نام آنلاین انجام می‌شود.",
        usefulFor = "سیستم صوتی، نورپردازی، تجهیزات صحنه و رویداد",
        officialUrl = "https://prolight-sound-guangzhou.hk.messefrankfurt.com/guangzhou/en.html"
    ),
    ChinaExhibition(
        title = "CIOF – China International Optics Fair",
        date = "۷–۹ سپتامبر ۲۰۲۷",
        city = "پکن",
        venue = "China International Exhibition Centre",
        category = "اپتیک و چشم‌پزشکی",
        summary = "نمایشگاه تخصصی صنعت اپتیک، عینک و اپتومتری. برای یافتن تولیدکنندگان، تجهیزات فروشگاه‌های اپتیک و فناوری‌های مرتبط مناسب است.",
        usefulFor = "اپتیک، عینک، تجهیزات اپتومتری و زنجیره تأمین",
        officialUrl = "https://www.ciof.cn/en/"
    )
)
