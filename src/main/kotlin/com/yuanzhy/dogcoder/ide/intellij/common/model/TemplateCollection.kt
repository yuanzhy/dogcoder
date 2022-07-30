package com.yuanzhy.dogcoder.ide.intellij.common.model

import com.alibaba.fastjson.JSON
import com.intellij.openapi.application.ApplicationManager
import com.intellij.util.io.isDirectory
import com.yuanzhy.dogcoder.ide.intellij.settings.DogCoderSettings
import com.yuanzhy.dogcoder.ide.intellij.settings.DogCoderSettingsListener
import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readBytes

class TemplateCollection(private val type: String) : Iterable<Template> {

    private val logger = LoggerFactory.getLogger(TemplateCollection::class.java)

    private val templates = ArrayList<Template>()

    init {
        ApplicationManager.getApplication().messageBus.connect().subscribe(DogCoderSettingsListener.TOPIC, object: DogCoderSettingsListener{
            override fun afterChanged() {
                templates.clear()
            }
        })
    }

    override fun iterator(): Iterator<Template> {
        if (templates.isEmpty()) {
            val path = Path(DogCoderSettings.getInstance().localPath,"templates/$type")
            try {
                iteJson(path)
                Files.list(path).filter { it.isDirectory() }.forEach {
                    iteJson(it)
                }
            } catch (e: Exception) {
                logger.warn("获取模板失败：${e.message}")
            }
        }
        return templates.iterator()
    }

    private fun iteJson(path: Path) {
        Files.list(path).filter { it.extension == "json" }.map {
            val json = JSON.parseObject(it.readBytes().decodeToString())
            val content = it.resolveSibling(it.nameWithoutExtension).readBytes().decodeToString().replace("\r\n", "\n")
            val labels = json.getJSONArray("labels")?.toJavaList(String::class.java) ?: emptyList()
            val imports = json.getJSONArray("imports")
            var suffix = json.getString("suffix")
            if (suffix == null) suffix = ""
            Template(it.nameWithoutExtension,
                    json.getString("prefix"),
                    suffix,
                    json.getString("desc"),
                    labels,
                    imports,
                    content
            )
        }.forEach {
            templates.add(it)
        }
    }
}