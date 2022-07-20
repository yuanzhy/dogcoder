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
        ProgressManager.getInstance().run(object: Backgroundable(null, "DogCoder templates init...") {
            override fun run(indicator: ProgressIndicator) {
                val settings = DogCoderSettings.getInstance()
                val httpClient = HttpClients.custom().build()
                val httpGet = HttpGet("${settings.serviceUrl}/api/v1/template?lastUpdatedTime=${settings.lastUpdatedMillis}")
                try {
                    val response = httpClient.execute(httpGet)
                    val jsonArray = JSON.parseArray(EntityUtils.toString(response.entity))
                    val templateFolder = Path(settings.localPath, "templates")
                    for (i in 0 until jsonArray.size) {
                        val json = jsonArray.getJSONObject(i)
                        val type = json.getString("type")
                        val name = json.getString("name")
                        val content = json.getString("content")
                        if (type.isNullOrEmpty() || name.isNullOrEmpty() || content.isNullOrEmpty()) {
                            logger.warn("数据错误，忽略：${json.toJSONString()}")
                            continue
                        }
                        json.fluentRemove("type").fluentRemove("name").fluentRemove("content")
                        val jsonPath = templateFolder.resolve(type).resolve("$name.json")
                        jsonPath.createFile().writeText(json.toJSONString())
                        val contentPath = templateFolder.resolve(type).resolve(name)
                        contentPath.createFile().writeText(content)
                    }
                    settings.lastUpdatedMillis = System.currentTimeMillis()
                } catch (e: IOException) {
                    logger.warn("更新模板失败：${e.message}")
                } finally {
                    httpGet.reset()
                }
            }
        })

    }
}