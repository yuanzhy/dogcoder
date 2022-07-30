package com.yuanzhy.dogcoder.ide.intellij.create

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.io.exists
import com.yuanzhy.dogcoder.ide.intellij.settings.DogCoderSettings
import com.yuanzhy.dogcoder.ide.intellij.settings.DogCoderSettingsListener
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.NotDirectoryException
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name

class NewGroup: ActionGroup() {

    private val logger = LoggerFactory.getLogger(NewGroup::class.java)

    private var children: Array<AnAction> = emptyArray()

    init {
        ApplicationManager.getApplication().messageBus.connect().subscribe(DogCoderSettingsListener.TOPIC, object: DogCoderSettingsListener {
            override fun afterChanged() {
                children = emptyArray()
            }
        })
    }

    private fun initChildren() {
        if (children.isEmpty()) {
            val list = ArrayList<AnAction>()
            val path = Path(DogCoderSettings.getInstance().localPath, "samples")
            if (path.exists()) {
                try {
                    for(c in Files.list(path)) {
                        recursive(c, list)
                    }
                } catch (e: Exception) {
                    logger.warn("获取模板失败：${e.message}")
                }

            }
            children = list.toTypedArray()
        }
    }

    private fun recursive(path: Path, list: MutableList<AnAction>) {
        try {
            val children = path.listDirectoryEntries()
            if (children.isEmpty()) {
                list.add(SampleAction(path.name, path.absolutePathString()))
            } else {
                val clist = ArrayList<AnAction>()
                for (c in children) {
                    recursive(c, clist)
                }
                if (clist.isEmpty() || clist.size != children.size) { // size not eq , dir and file mixed
                    list.add(SampleAction(path.name, path.absolutePathString()))
                } else {
                    val actionGroup = DefaultActionGroup(path.name, clist)
                    actionGroup.isPopup = true
                    list.add(actionGroup)
                }
            }
        } catch (e: NotDirectoryException) {
            // ignore
//            if (path.isDirectory()) {
//                list.add(SampleAction(path.name, path.absolutePathString()))
//            }
        }
    }

    override fun getChildren(e: AnActionEvent?): Array<AnAction> {
        initChildren()
        return children
    }
}