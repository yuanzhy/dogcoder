package com.yuanzhy.dogcoder.ide.intellij.completion.javascript

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.util.TextRange
import com.yuanzhy.dogcoder.ide.intellij.common.Icons
import com.yuanzhy.dogcoder.ide.intellij.common.model.TemplateCollection

class JavascriptCompletionContributor : CompletionContributor() {

    private val templateCollection = TemplateCollection("javascript")

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        super.fillCompletionVariants(parameters, result)
        val module = ModuleUtil.findModuleForFile(parameters.originalFile) ?: return
        if (module.isDisposed) {
            return
        }
//        val psiFile = parameters.originalFile as JSFile
        // 格式化，首次获取
        val key = parameters.editor.document.getText(TextRange(parameters.offset - parameters.invocationCount, parameters.offset)).trim()
        for (template in templateCollection) {
            if (template.isMatch(key)) {
                val element: LookupElementBuilder = LookupElementBuilder.create(template.content)
                        .withIcon(Icons.LOGO_S)
                        .withPresentableText(template.prefix)
                        .withTailText(template.suffix)
                        .withTypeText(template.desc, true)
                        .withTypeIconRightAligned(true)
//                        .withInsertHandler(getInsertHandler())
                result.addElement(element)
            }
        }
        result.stopHere()
    }
}