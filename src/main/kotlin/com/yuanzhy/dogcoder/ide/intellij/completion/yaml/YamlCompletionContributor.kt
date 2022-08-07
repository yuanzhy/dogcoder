package com.yuanzhy.dogcoder.ide.intellij.completion.yaml

import com.intellij.codeInsight.completion.CompletionParameters
import com.yuanzhy.dogcoder.ide.intellij.completion.AbstractCompletionContributor

/**
 *
 * @author yuanzhy
 * @date 2022-07-14
 */
class YamlCompletionContributor : AbstractCompletionContributor("yaml") {

    override fun supports(parameters: CompletionParameters): Boolean {
        return super.supports(parameters) && !parameters.originalFile.name.startsWith("application")
    }
}