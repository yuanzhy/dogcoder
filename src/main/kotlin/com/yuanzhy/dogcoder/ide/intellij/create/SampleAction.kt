package com.yuanzhy.dogcoder.ide.intellij.create

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class SampleAction(name: String, private val path: String): AnAction(name) {

    override fun actionPerformed(e: AnActionEvent) {
        println("创建了一个sample")
    }
}