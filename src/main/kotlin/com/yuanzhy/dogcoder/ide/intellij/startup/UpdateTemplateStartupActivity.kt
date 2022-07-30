package com.yuanzhy.dogcoder.ide.intellij.startup

import com.alibaba.fastjson.JSON
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.util.io.createFile
import com.yuanzhy.dogcoder.ide.intellij.settings.DogCoderSettings
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.writeText


/**
 *
 * @author yuanzhy
 * @date 2022-07-20
 */
class UpdateTemplateStartupActivity : StartupActivity {

    private val logger = LoggerFactory.getLogger(UpdateTemplateStartupActivity::class.java)

    override fun runActivity(project: Project) {
        ProgressManager.getInstance().run(object: Backgroundable(null, "DogCoder init...") {
            override fun run(indicator: ProgressIndicator) {
                val settings = DogCoderSettings.getInstance()
                val httpClient = HttpClients.custom().build()
                val templateGet = HttpGet("${settings.serviceUrl}/api/v1/template?lastUpdatedTime=${settings.lastUpdatedMillis}")
                val sampleGet = HttpGet("${settings.serviceUrl}/api/v1/samples?lastUpdatedTime=${settings.lastUpdatedMillis}")
                try {
                    val templateResponse = httpClient.execute(templateGet)
                    val tArray = JSON.parseArray(EntityUtils.toString(templateResponse.entity))
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
                        jsonPath.createFile().writeText(json.toJSONString())
                        val contentPath = path.resolve(name)
                        Files.deleteIfExists(contentPath)
                        contentPath.createFile().writeText(content)
                    }
                    // samples
                    val sampleResponse = httpClient.execute(sampleGet)
                    val sArray = JSON.parseArray(EntityUtils.toString(sampleResponse.entity))
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
                            path.resolve(names.getString(n)).createFile().writeText(contents.getString(n))
                        }
                    }
                    settings.lastUpdatedMillis = System.currentTimeMillis()
                } catch (e: IOException) {
                    logger.warn("更新模板失败：${e.message}")
                } finally {
                    templateGet.reset()
                    sampleGet.reset();
                }
            }
        })

    }
}