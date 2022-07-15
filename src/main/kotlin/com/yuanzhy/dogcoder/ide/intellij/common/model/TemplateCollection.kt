package com.yuanzhy.dogcoder.ide.intellij.common.model

import com.alibaba.fastjson.JSON
import com.yuanzhy.dogcoder.ide.intellij.DogCoderPlugin
import org.slf4j.LoggerFactory
import java.nio.file.Files
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readBytes

class TemplateCollection(private val type: String) : Iterable<Template> {

    private val logger = LoggerFactory.getLogger(TemplateCollection::class.java)

    private val templates = ArrayList<Template>()

    override fun iterator(): Iterator<Template> {
        if (templates.isEmpty()) {
            val path = DogCoderPlugin.getPluginPath().resolve("templates/$type")
            try {
                Files.list(path).filter { it.extension == "json" }.map {
                    val json = JSON.parseObject(it.readBytes().decodeToString())
                    val content = it.resolveSibling(it.nameWithoutExtension).readBytes().decodeToString().replace("\r\n", "\n")
                    val labels = json.getJSONArray("labels")?.toJavaList(String::class.java) ?: emptyList()
                    val imports = json.getJSONArray("imports")
                    Template(it.nameWithoutExtension,
                            json.getString("prefix"),
                            json.getString("suffix"),
                            json.getString("desc"),
                            labels,
                            imports,
                            content
                    )
                }.forEach {
                    templates.add(it)
                }
            } catch (e: Exception) {
                logger.warn("获取模板失败：${e.message}")
            }
        }
        return templates.iterator()
    }
}