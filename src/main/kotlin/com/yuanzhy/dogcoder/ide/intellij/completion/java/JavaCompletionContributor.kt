package com.yuanzhy.dogcoder.ide.intellij.completion.java

import com.alibaba.fastjson.JSONArray
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.InsertionContext
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.JavaPsiFacade
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiMethod
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.findParentInFile
import com.yuanzhy.dogcoder.ide.intellij.common.model.Template
import com.yuanzhy.dogcoder.ide.intellij.completion.AbstractCompletionContributor

/**
 *
 * @author yuanzhy
 * @date 2022-07-13
 */
class JavaCompletionContributor : AbstractCompletionContributor("java",
        object: DogCoderInsertHandler() {
            override fun afterInsert(context: InsertionContext, item: LookupElement, psiFile: PsiFile) {
                val obj = item.`object`
                if (obj !is JSONArray) {
                    return
                }
                psiFile as PsiJavaFile
                val psiFacade = JavaPsiFacade.getInstance(context.project)
                val scope = GlobalSearchScope.allScope(context.project)
                for (i in  0 until obj.size) {
                    psiFacade.findClass(obj.getString(i), scope)?.let { psiFile.importClass(it) }
                }
            }
        }
) {

    private val docComment = """
    /**
     * {name}
     */
    """.trimIndent()

    private var needMethod = false

    override fun beforeCollectElement(parameters: CompletionParameters) {
        super.beforeCollectElement(parameters)
        needMethod = parameters.originalPosition!!.findParentInFile { it is PsiMethod } == null
    }

    override fun getContent(template: Template): String {
        return if (needMethod) {
            docComment.replace("{name}", template.name) + "\npublic static void ${template.name.replace(".", "")}() {${template.content}}"
        } else if (template.name.startsWith("new")) {
            template.content
        } else {
            "// ${template.name}\n${template.content}"
        }
    }
}