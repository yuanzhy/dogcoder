package com.yuanzhy.dogcoder.ide.intellij.common

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vcs.VcsShowConfirmationOption
import com.intellij.util.ui.ConfirmationDialog

/**
 *
 * @author yuanzhy
 * @date 2022-07-21
 */
object DogCoderUI {

    private val options = object: VcsShowConfirmationOption {
        override fun getValue(): VcsShowConfirmationOption.Value? {
            return null
        }
        override fun setValue(value: VcsShowConfirmationOption.Value?) {
        }
        override fun isPersistent(): Boolean {
            return false
        }
    }

    fun showInfo(message: String) {
        Messages.showMessageDialog(message, "提示", Messages.getInformationIcon())
    }

    fun confirm(message: String, project: Project): Boolean {
        return ConfirmationDialog.requestForConfirmation(options, project, message, "confirm", Messages.getInformationIcon())
    }
}