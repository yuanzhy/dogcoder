package com.yuanzhy.dogcoder.ide.intellij.create

import com.intellij.codeInsight.daemon.impl.analysis.JavaHighlightUtil
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.FileTypeUtils
import org.jetbrains.kotlin.psi.KtFile

/**
 *
 * @author yuanzhy
 * @date 2022-07-21
 */

val handleJava = fun(vFile: VirtualFile, project: Project) {
    val psiFile = PsiManager.getInstance(project).findFile(vFile)
    if (psiFile is PsiJavaFile) {
        if (FileTypeUtils.isInServerPageFile(psiFile)) return
        if (JavaHighlightUtil.isJavaHashBangScript(psiFile)) return
        val directory: PsiDirectory = psiFile.getContainingDirectory() ?: return
        val dirPackage = JavaDirectoryService.getInstance().getPackage(directory) ?: return
//        val packageStatement = psiFile.packageStatement
        val packageName = dirPackage.qualifiedName
        WriteCommandAction.runWriteCommandAction(project) {
            psiFile.packageName = packageName
            if (psiFile.classes.isNotEmpty() && psiFile.classes[0].name != vFile.nameWithoutExtension) {
                psiFile.classes[0].setName(vFile.nameWithoutExtension)
            }
        }
    }
}

val handleKt = fun(vFile: VirtualFile, project: Project) {
    val psiFile = PsiManager.getInstance(project).findFile(vFile)
    if (psiFile is KtFile) {

    }
}