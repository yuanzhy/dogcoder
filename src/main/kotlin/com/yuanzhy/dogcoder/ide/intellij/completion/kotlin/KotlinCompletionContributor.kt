package com.yuanzhy.dogcoder.ide.intellij.completion.kotlin

import com.alibaba.fastjson.JSONArray
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiFile
import com.yuanzhy.dogcoder.ide.intellij.completion.AbstractCompletionContributor
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.resolve.ImportPath

/**
 *
 * @author yuanzhy
 * @date 2022-07-15
 */
class KotlinCompletionContributor : AbstractCompletionContributor("kotlin",
        object : DogCoderInsertHandler() {
            override fun afterInsert(context: InsertionContext, item: LookupElement, psiFile: PsiFile) {
                val obj = item.`object`
                if (obj !is JSONArray) {
                    return
                }
                psiFile as KtFile
                val psiFactory = KtPsiFactory(psiFile.project)
                obj.map {
                    val ktImport = psiFactory.createImportDirective(ImportPath.fromString(it.toString()))
                    psiFile.importList?.add(ktImport)
                }
            }
})