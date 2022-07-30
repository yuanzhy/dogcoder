package com.yuanzhy.dogcoder.ide.intellij.startup

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.yuanzhy.dogcoder.ide.intellij.DogCoderPlugin


/**
 *
 * @author yuanzhy
 * @date 2022-07-20
 */
class UpdateTemplateStartupActivity : StartupActivity {

    override fun runActivity(project: Project) {
        DogCoderPlugin.updateTemplatesAsync()
    }
}