package com.yuanzhy.dogcoder.ide.intellij.common.model

data class SampleFolder(val name: String, val children: List<SampleFolder> = ArrayList())