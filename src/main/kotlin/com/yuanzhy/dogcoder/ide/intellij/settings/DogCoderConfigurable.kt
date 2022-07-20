package com.yuanzhy.dogcoder.ide.intellij.settings

import com.intellij.openapi.options.SearchableConfigurable
import javax.swing.JComponent

/**
 *
 * @author yuanzhy
 * @date 2022-07-20
 */
class DogCoderConfigurable : SearchableConfigurable {

    override fun createComponent(): JComponent? {
        TODO("Not yet implemented")
    }

    override fun isModified(): Boolean {
        TODO("Not yet implemented")
    }

    override fun apply() {
//        val newSelect = ui.selectList.selectedItem.toString()
//        val newIsScroll = ui.scrollCheckbox.isSelected
//        // 通知变更
//        ApplicationManager.getApplication().messageBus
//                .syncPublisher<FormSettingsChangedListener>(FormSettingsChangedListener.TOPIC)
//                .beforeChange(FormSettings(newSelect, newIsScroll))
//        // 持久化
//        formSettings.select = newSelect
//        formSettings.isScroll = newIsScroll
    }

    override fun getDisplayName(): String {
        return "DogCoder"
    }

    override fun getId(): String {
        return "DogCoderConfigurable"
    }
}