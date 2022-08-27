package com.yuanzhy.dogcoder.ide.intellij.completion

import com.intellij.codeInsight.actions.ReformatCodeProcessor
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.util.TextRange
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
        val document = parameters.editor.document
        val line = document.getLineNumber(parameters.offset)
        val range = TextRange(document.getLineStartOffset(line), document.getLineEndOffset(line))
        var key = document.getText(range).trim()
//        val key = position.text.trim()
        if (/*key.isEmpty() ||*/ key.endsWith(")")) {
            return
        }
        if (key.contains(" ")) {
            key = key.substringAfterLast(" ").trim();
        }
        beforeCollectElement(parameters)
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
        return parameters.completionType == CompletionType.BASIC
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