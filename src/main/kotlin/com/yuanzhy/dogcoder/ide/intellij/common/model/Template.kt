package com.yuanzhy.dogcoder.ide.intellij.common.model

data class Template(
        val name: String,
        val prefix: String,
        val suffix: String,
        val desc: String,
        val label: List<String>,
        val content: String) {

    fun isMatch(keyword: String): Boolean {
        // TODO 模糊匹配
        return name.contains(keyword, true)
                || prefix.contains(keyword, true)
                || suffix.contains(keyword, true)
                || label.any { it.contains(keyword, true) }
    }
}
