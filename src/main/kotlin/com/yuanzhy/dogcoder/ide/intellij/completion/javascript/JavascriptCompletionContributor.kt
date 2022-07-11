package com.yuanzhy.dogcoder.ide.intellij.completion.javascript

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.lang.javascript.psi.JSFile
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.util.TextRange

class JavascriptCompletionContributor : CompletionContributor() {

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        super.fillCompletionVariants(parameters, result)
        val module = ModuleUtil.findModuleForFile(parameters.originalFile) ?: return
        if (module.isDisposed) {
            return
        }
        val psiFile = parameters.originalFile as JSFile
        val s = parameters.editor.document.getText(TextRange(parameters.offset - parameters.invocationCount, parameters.offset)).trim()
        val rawList = arrayListOf(
                "fetch()",
                "axios.get()"
        )
        for (data in rawList) {
            if (data.contains(s.lowercase(), true)) {
                val element: LookupElementBuilder = LookupElementBuilder.create(data)
//                        .withIcon()
                        .withPresentableText(data + " pre")
                        .withTailText(data + " tail").withTypeText(data + " type", true).withTypeIconRightAligned(true)
//                        .withInsertHandler(getInsertHandler())
                result.addElement(element)
            }
        }
        result.stopHere()
    }
}