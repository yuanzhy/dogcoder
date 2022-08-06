package com.yuanzhy.dogcoder.ide.intellij.create

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.ui.DialogBuilder
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.util.io.copy
import com.yuanzhy.dogcoder.ide.intellij.common.DogCoderUI
import java.awt.GridLayout
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import kotlin.io.path.name

class SampleAction(name: String, private val path: String): AnAction(name) {

    private val handlerMap = mapOf(
            Pair("java", handleJava),
            Pair("kt", handleKt),
    )

    override fun actionPerformed(e: AnActionEvent) {
        val vf = e.getData(LangDataKeys.VIRTUAL_FILE) ?: return DogCoderUI.showInfo("请选择目录")
        val project = e.project ?: return DogCoderUI.showInfo("project获取失败")
        val toFolder = if (vf.isDirectory) vf else vf.parent
        val children = Files.list(Paths.get(path)).collect(Collectors.toList())
        if (children.isEmpty()) {
            return DogCoderUI.showInfo("暂未实现")
        }
        val pane = JPanel(GridLayout(children.size + 1, 1, 5, 5))
        pane.add(JLabel("将在 ${toFolder.name} 中创建以下文件"))
        val fieldList = ArrayList<JTextField>()
        for (path in children) {
            // TODO 支持目录提示
//            if (path.isDirectory()) {
//            }
            val tf = JTextField(path.name)
            tf.isEditable = false
            pane.add(tf)
            fieldList.add(tf)
        }
        val builder = DialogBuilder().title("New DogCoder Sample").centerPanel(pane)
        builder.setOkOperation {
            val duplications = ArrayList<String>()
            for (tf in fieldList) {
                if (tf.text.isBlank()) {
                    return@setOkOperation DogCoderUI.showInfo("请输入名称")
                }
                if (toFolder.findChild(tf.text) != null) {
                    duplications.add(tf.text.trim())
                }
            }
            val isCreate = if (duplications.isNotEmpty()) {
                DogCoderUI.confirm("${duplications.joinToString(", ")} 该操作将覆盖已有文件，是否继续？", project)
            } else {
                true
            }
            if (isCreate) {
//                val toPath = toFolder.toNioPath()
                val toPath = Paths.get(toFolder.path)
                for (i in 0 until children.size) {
                    val cpath = children[i]
                    val tf = fieldList[i]
                    cpath.copy(toPath.resolve(tf.text.trim()))
                }
                toFolder.refresh(false, true)
                VfsUtilCore.processFilesRecursively(toFolder) {
                    if (!it.isDirectory) {
                        val fn = handlerMap[it.extension]
                        if (fn != null) {
                            try {
                                fn(it, project)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                    return@processFilesRecursively true
                }
                builder.dialogWrapper.close(DialogWrapper.OK_EXIT_CODE)
            }
        }
        builder.show()
    }
}