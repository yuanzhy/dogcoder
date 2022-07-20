package com.yuanzhy.dogcoder.ide.intellij.settings

import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JComponent

/**
 *
 * @author yuanzhy
 * @date 2022-07-20
 */
class DogCoderConfigurable : SearchableConfigurable {

    private val ui = ConfigUI()
    private val settings = DogCoderSettings()

    init {
        ui.serviceUrlField.text = settings.serviceUrl
        ui.localPathField.text = settings.localPath
    }
    override fun createComponent(): JComponent {
        return ui.rootPane
    }

    override fun isModified(): Boolean {
        return settings.serviceUrl != ui.serviceUrlField.text
                || settings.localPath != ui.localPathField.text
    }

    override fun apply() {
        settings.serviceUrl = ui.serviceUrlField.text
        settings.localPath = ui.localPathField.text
    }

    override fun getDisplayName(): String {
        return "DogCoder"
    }

    override fun getId(): String {
        return "DogCoderConfigurable"
    }
}