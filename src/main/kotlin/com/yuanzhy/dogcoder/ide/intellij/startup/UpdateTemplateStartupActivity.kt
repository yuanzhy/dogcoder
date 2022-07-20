package com.yuanzhy.dogcoder.ide.intellij.startup

import com.alibaba.fastjson.JSON
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.yuanzhy.dogcoder.ide.intellij.settings.DogCoderSettings
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory
import java.io.IOException


/**
 *
 * @author yuanzhy
 * @date 2022-07-20
 */
class UpdateTemplateStartupActivity : StartupActivity {

    private val logger = LoggerFactory.getLogger(UpdateTemplateStartupActivity::class.java)

    override fun runActivity(project: Project) {
        val settings = DogCoderSettings.getInstance()
        val httpClient = HttpClients.custom().build()
        val httpGet = HttpGet("${settings.serviceUrl}/api/v1/template?lastUpdatedTime=${settings.lastUpdatedMillis}")
        try {
            val response = httpClient.execute(httpGet)
            val jsonArray = JSON.parseArray(EntityUtils.toString(response.entity))
//            jsonArray.getJSONObject()
        } catch (e: IOException) {
            logger.warn("更新模板失败：${e.message}")
        } finally {
            httpGet.reset()
        }
    }
}