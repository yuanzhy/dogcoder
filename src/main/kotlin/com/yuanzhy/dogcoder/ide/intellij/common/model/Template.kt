package com.yuanzhy.dogcoder.ide.intellij.common.model

import com.alibaba.fastjson.JSONArray

data class Template(
        val name: String,
        val prefix: String,
        val suffix: String,
        val desc: String,
        val labels: List<String>,
        val imports: JSONArray?,
        val content: String) {

    fun isMatch(keyword: String): Boolean {
        if (keyword.isEmpty()) {
            return true
        }
        val kw = keyword.replace(".", "")
        val contains = name.contains(kw, true)
                || prefix.contains(kw, true)
                || suffix.contains(kw, true)
                || labels.any { it.contains(kw, true) }
        if (contains) {
            return true
        }
        return fuzzyMatch(kw)
    }

    /**
     * 模糊匹配
     */
    private fun fuzzyMatch(keyword: String): Boolean {
        val arr = arrayOf(name, prefix, suffix)
        for (s in arr) {
            if (s.isEmpty()) {
                continue
            }
            val match = fuzzyMatch(keyword, s)
            if (match) {
                return true
            }
        }
        return false
    }

    private fun fuzzyMatch(keyword: String, source: String): Boolean {
        var m = source.lowercase()
        for (c in keyword.lowercase()) {
            if (m.isEmpty()) {
                return false
            }
            if (!m.contains(c)) {
                return false
            }
            m = m.substringAfter(c)
        }
        return true
    }
}
