package com.yuanzhy.dogcoder.ide.intellij

import com.intellij.codeInsight.CodeInsightSettings
import com.intellij.ide.GeneralSettings
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.PluginId
import java.nio.file.Path

object DogCoderPlugin {

    const val PLUGIN_ID = "com.yuanzhy.dogcoder.ide.intellij"

    private val plugin: IdeaPluginDescriptor = PluginManagerCore.getPlugin(PluginId.getId(PLUGIN_ID))!!
    fun getPlugin(): IdeaPluginDescriptor {
        return plugin
    }

    fun getVersion(): String = getPlugin().version
//    fun getPluginPath(): Path = getPlugin().pluginPath

    fun getPluginPath(): Path = Path.of("/home/yuanzhy/git/yuanzhy/dogcoder/src/main/resources/")
}