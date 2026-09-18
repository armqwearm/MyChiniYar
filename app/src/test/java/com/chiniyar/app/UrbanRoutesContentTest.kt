package com.chiniyar.app

import org.junit.Assert.assertTrue
import org.junit.Test

class UrbanRoutesContentTest {
    @Test
    fun metroManPlayStoreUrlIsValid() {
        val url = "https://play.google.com/store/apps/details?id=com.xinlukou.metroman"
        assertTrue(url.startsWith("https://play.google.com/store/apps/details?id="))
        assertTrue(url.endsWith("com.xinlukou.metroman"))
    }
}
