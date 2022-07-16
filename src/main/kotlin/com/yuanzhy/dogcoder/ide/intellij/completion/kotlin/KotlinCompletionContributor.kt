package com.yuanzhy.dogcoder.ide.intellij.completion.kotlin

import com.alibaba.fastjson.JSONArray
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiFile
import com.yuanzhy.dogcoder.ide.intellij.completion.AbstractCompletionContributor
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.nj2k.NewJavaToKotlinConverter.Companion.addImports
import org.jetbrains.kotlin.psi.KtFile

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
                psiFile.addImports(obj.map { FqName(it.toString()) })
            }
})