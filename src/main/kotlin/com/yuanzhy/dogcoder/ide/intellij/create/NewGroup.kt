package com.yuanzhy.dogcoder.ide.intellij.create

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.util.io.exists
import com.yuanzhy.dogcoder.ide.intellij.DogCoderPlugin
import org.slf4j.LoggerFactory

class NewGroup: ActionGroup() {

    private val logger = LoggerFactory.getLogger(NewGroup::class.java)

    private val children: Array<AnAction>
    init {
        val list = ArrayList<AnAction>()
        val path = DogCoderPlugin.getPluginPath().resolve("samples")
        if (path.exists()) {
            try {
                // TODO 递归获取目录
//                Files.list(path)
            } catch (e: Exception) {
                logger.warn("获取模板失败：${e.message}")
            }

        }
        children = list.toTypedArray()
    }

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        return children
    }
}