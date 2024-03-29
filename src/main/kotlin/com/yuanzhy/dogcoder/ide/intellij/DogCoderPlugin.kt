package com.yuanzhy.dogcoder.ide.intellij

import com.alibaba.fastjson.JSON
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.util.io.createDirectories
import com.yuanzhy.dogcoder.ide.intellij.settings.DogCoderSettings
import com.yuanzhy.dogcoder.ide.intellij.settings.DogCoderSettingsListener
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.writeText

object DogCoderPlugin {

    private val logger = LoggerFactory.getLogger(DogCoderPlugin::class.java)

    const val PLUGIN_ID = "com.yuanzhy.dogcoder.ide.intellij"

    private val CONFIG_PATH = File(PathManager.getOptionsPath(), "dogcoder").absolutePath

    fun getPluginPath(): Path {
        return Path(PathManager.getPluginsPath(), "dogcoder")
    }

    fun getConfigPath(): String {
        return CONFIG_PATH
    }

    fun updateTemplatesAsync() {
        ProgressManager.getInstance().run(object: Task.Backgroundable(null, "DogCoder data init...") {
            override fun run(indicator: ProgressIndicator) {
                updateTemplates()
                ApplicationManager.getApplication().messageBus.syncPublisher(DogCoderSettingsListener.TOPIC).afterChanged()
            }
        })
    }

    private fun updateTemplates() {
        val settings = DogCoderSettings.getInstance()
        val httpClient = HttpClients.custom().build()
        val templateGet = HttpGet("${settings.serviceUrl}/api/v1/template?lastUpdatedTime=${settings.lastUpdatedMillis}")
        val sampleGet = HttpGet("${settings.serviceUrl}/api/v1/samples?lastUpdatedTime=${settings.lastUpdatedMillis}")
        try {
            val templateResponse = httpClient.execute(templateGet)
            val tArray = JSON.parseArray(EntityUtils.toString(templateResponse.entity))
            if (settings.lastUpdatedMillis == 0L && tArray.isNotEmpty()) {
                deleteDirectory(File(settings.localPath, "templates"))
            }
            val templateFolder = Path(settings.localPath, "templates")
            for (i in 0 until tArray.size) {
                val json = tArray.getJSONObject(i)
                val type = json.getString("type")
                val name = json.getString("name")
                val content = json.getString("content")
                if (type.isNullOrEmpty() || name.isNullOrEmpty() || content.isNullOrEmpty()) {
                    logger.warn("数据错误，忽略：${json.toJSONString()}")
                    continue
                }
                val category = json.getString("category")
                json.fluentRemove("type").fluentRemove("name").fluentRemove("content").fluentRemove("category")
                val path = if (category.isNullOrEmpty()) templateFolder.resolve(type) else templateFolder.resolve(type).resolve(category)
                val jsonPath = path.resolve("$name.json")
                Files.deleteIfExists(jsonPath)
                createFile(jsonPath).writeText(json.toJSONString())
                val contentPath = path.resolve(name)
                Files.deleteIfExists(contentPath)
                createFile(contentPath).writeText(content)
            }
            // samples
            val sampleResponse = httpClient.execute(sampleGet)
            val sArray = JSON.parseArray(EntityUtils.toString(sampleResponse.entity))
            if (settings.lastUpdatedMillis == 0L && sArray.isNotEmpty()) {
                deleteDirectory(File(settings.localPath, "samples"))
            }
            val sampleFolder = Path(settings.localPath, "samples")
            for (i in 0 until sArray.size) {
                val json = sArray.getJSONObject(i)
                val pathStr = json.getString("path")
                val names = json.getJSONArray("names")
                val contents = json.getJSONArray("contents")
                if (pathStr.isNullOrEmpty() || names.isNullOrEmpty() || contents.isNullOrEmpty() || names.size != contents.size) {
                    logger.warn("数据错误，忽略：${json.toJSONString()}")
                    continue
                }
                val path = sampleFolder.resolve(pathStr)
                for (n in 0 until names.size) {
                    val filePath = path.resolve(names.getString(n))
                    Files.deleteIfExists(filePath)
                    createFile(path.resolve(names.getString(n))).writeText(contents.getString(n))
                }
            }
            settings.lastUpdatedMillis = System.currentTimeMillis()
        } catch (e: IOException) {
            logger.warn("更新模板失败：${e.message}")
        } finally {
            templateGet.reset()
            sampleGet.reset()
        }
    }

    private fun createFile(path: Path): Path {
        path.parent?.createDirectories()
        return Files.createFile(path)
    }

    private fun deleteDirectory(file: File) {
        val list = file.listFiles() //无法做到list多层文件夹数据
        if (list != null) {
            for (temp in list) { //先去递归删除子文件夹及子文件
                deleteDirectory(temp) //注意这里是递归调用
            }
        }
        file.delete()
    }
}