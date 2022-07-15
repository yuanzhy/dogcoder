package com.yuanzhy.dogcoder.ide.intellij.completion.kotlin

import com.alibaba.fastjson.JSONArray
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiFile
import com.yuanzhy.dogcoder.ide.intellij.completion.AbstractCompletionContributor

/**
 *
 * @author yuanzhy
 * @date 2022-07-15
 */
class KotlinCompletionContributor : AbstractCompletionContributor("python",
        object : DogCoderInsertHandler() {
            override fun afterInsert(context: InsertionContext, item: LookupElement, psiFile: PsiFile) {
                val obj = item.`object`
                if (obj !is JSONArray) {
                    return
                }
                // TODO kotlin import
//                for (i in 0 until obj.size) {
//                    val imports = obj.getJSONObject(i)
//                    val from = imports.getString("from")
//                    if (from.isNullOrEmpty()) {
//                        AddImportHelper.addImportStatement(
//                                psiFile,
//                                imports.getString("import"),
//                                imports.getString("as"),
//                                null,
//                                null,
//                        )
//                    } else {
//                        AddImportHelper.addFromImportStatement(
//                                psiFile,
//                                from,
//                                imports.getString("import"),
//                                imports.getString("as"),
//                                null,
//                                null,
//                        )
//                    }
//                }
            }
})