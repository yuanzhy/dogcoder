package com.yuanzhy.dogcoder.ide.intellij.settings

import com.intellij.configurationStore.APP_CONFIG
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.yuanzhy.dogcoder.ide.intellij.DogCoderPlugin
import org.jdom.Element

/**
 *
 * @author yuanzhy
 * @date 2022-07-20
 */
@State(name="DogCoderSettings",
        storages=[Storage("$APP_CONFIG/DogCoderSettings.xml")])
class DogCoderSettings(
        internal var serviceUrl: String = "http://dogcoder.yuanzhy.com",
        internal var localPath: String = DogCoderPlugin.getPluginPath().toFile().absolutePath,
        internal var lastUpdatedMillis: Long = 0,
) : PersistentStateComponent<Element> {

    companion object {

        fun getInstance(): DogCoderSettings {
            return ApplicationManager.getApplication().getService(DogCoderSettings::class.java)
        }
    }

    override fun getState(): Element {
        val element = Element("DogCoderSettings")
        element.setAttribute("serviceUrl", serviceUrl)
        element.setAttribute("localPath", localPath)
        element.setAttribute("lastUpdatedMillis", lastUpdatedMillis.toString())
        return element
    }

    override fun loadState(state: Element) {
        val serviceUrl = state.getAttributeValue("serviceUrl")
        if (!serviceUrl.isNullOrEmpty()) {
            this.serviceUrl = serviceUrl
        }
        val localPath = state.getAttributeValue("localPath")
        if (!localPath.isNullOrEmpty()) {
            this.localPath = localPath
        }
        val lastUpdatedMillis = state.getAttributeValue("lastUpdatedMillis")
        if (!localPath.isNullOrEmpty()) {
            this.lastUpdatedMillis = lastUpdatedMillis.toLong()
        }
    }
}