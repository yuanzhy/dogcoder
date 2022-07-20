package com.yuanzhy.dogcoder.ide.intellij.completion

import com.intellij.codeInsight.actions.ReformatCodeProcessor
import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.InsertHandler
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.module.ModuleUtil
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.yuanzhy.dogcoder.ide.intellij.common.DcIcons
import com.yuanzhy.dogcoder.ide.intellij.common.model.Template
import com.yuanzhy.dogcoder.ide.intellij.common.model.TemplateCollection

/**
 *
 * @author yuanzhy
 * @date 2022-07-13
 */
abstract class AbstractCompletionContributor(type: String, protected val insertHandler: InsertHandler<in LookupElement>? = null) : CompletionContributor() {

    protected val templateCollection = TemplateCollection(type)

    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        super.fillCompletionVariants(parameters, result)
        val module = ModuleUtil.findModuleForFile(parameters.originalFile) ?: return
        if (module.isDisposed || !supports(parameters)) {
            return
        }
        val position = parameters.originalPosition ?: return
        beforeCollectElement(parameters)
        val key = position.text.trim()
        for (template in templateCollection) {
            if (template.isMatch(key)) {
                val content = getContent(template)
                val element: LookupElementBuilder = LookupElementBuilder.create(template.imports ?: content, content)
                        .withIcon(DcIcons.LOGO_S)
                        .withPresentableText(template.prefix)
                        .withTailText(template.suffix)
                        .withTypeText(template.desc, true)
                        .withTypeIconRightAligned(true)
                        .withLookupStrings(template.labels)
                        .withInsertHandler(insertHandler)
                result.addElement(element)
            }
        }
//        afterCollectElement(parameters)
//        result.stopHere()
    }

    protected open fun getContent(template: Template): String {
        return template.content
    }

    protected open fun supports(parameters: CompletionParameters): Boolean {
        return true
    }

    protected open fun beforeCollectElement(parameters: CompletionParameters) {
    }

//    protected open fun afterCollectElement(parameters: CompletionParameters) { }


    protected abstract class DogCoderInsertHandler : InsertHandler<LookupElement> {

        override fun handleInsert(context: InsertionContext, item: LookupElement) {
            // format
            val editor = context.editor
            val psiFile = PsiDocumentManager.getInstance(context.project).getPsiFile(editor.document) ?: return
            editor.selectionModel.setSelection(context.startOffset, context.tailOffset)
            ReformatCodeProcessor(psiFile, editor.selectionModel).runWithoutProgress()
            editor.selectionModel.removeSelection()
            // after format
            afterInsert(context, item, psiFile)
        }

        protected abstract fun afterInsert(context: InsertionContext, item: LookupElement, psiFile: PsiFile)
    }
}