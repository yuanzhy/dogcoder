package com.yuanzhy.dogcoder.ide.intellij.common.model

import com.alibaba.fastjson.JSON
import com.google.gson.Gson
import com.yuanzhy.dogcoder.ide.intellij.DogCoderPlugin
import java.nio.file.Files
import kotlin.io.path.extension
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.readBytes

class TemplateCollection(private val type: String) : Iterable<Template> {

    private val templates = ArrayList<Template>()

    override fun iterator(): Iterator<Template> {
        if (templates.isEmpty()) {
            val path = DogCoderPlugin.getPluginPath().resolve("templates/$type")
            Files.list(path).filter { it.extension == "json" }.map {
                val json = JSON.parseObject(it.readBytes().decodeToString())
                val content = it.resolveSibling(it.nameWithoutExtension).readBytes().decodeToString().replace("\r\n", "\n")
                Template(it.nameWithoutExtension,
                        json.getString("prefix"),
                        json.getString("suffix"),
                        json.getString("desc"),
                        json.getJSONArray("label").toJavaList(String::class.java),
                        content
                )
            }.forEach {
                templates.add(it)
            }
        }
        return templates.iterator()
    }
}