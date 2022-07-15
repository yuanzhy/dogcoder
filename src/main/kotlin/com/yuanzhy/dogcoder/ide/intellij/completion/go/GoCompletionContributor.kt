package com.yuanzhy.dogcoder.ide.intellij.completion.go

import com.alibaba.fastjson.JSONArray
import com.goide.psi.GoFile
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.PsiFile
import com.yuanzhy.dogcoder.ide.intellij.common.model.Template
import com.yuanzhy.dogcoder.ide.intellij.completion.AbstractCompletionContributor

/**
 *
 * @author yuanzhy
 * @date 2022-07-14
 */
class GoCompletionContributor: AbstractCompletionContributor("go",
        object: DogCoderInsertHandler() {
            override fun afterInsert(context: InsertionContext, item: LookupElement, psiFile: PsiFile) {
                val obj = item.`object`
                if (obj !is JSONArray) {
                    return
                }
                psiFile as GoFile
                for (i in  0 until obj.size) {
                    val imports = obj.getJSONObject(i)
                    psiFile.addImport(imports.getString("import"), imports.getString("alias"))
                }
            }
        }
) {
    override fun getContent(template: Template): String {
        return "// ${template.name}\n${template.content}"
    }
}