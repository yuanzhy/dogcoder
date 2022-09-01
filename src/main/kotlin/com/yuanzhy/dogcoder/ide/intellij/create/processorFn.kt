package com.yuanzhy.dogcoder.ide.intellij.create

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtilCore
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
//        if (JavaHighlightUtil.isJavaHashBangScript(psiFile)) return
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
        val directory: PsiDirectory = psiFile.containingDirectory ?: return
        val dirPackage = JavaDirectoryService.getInstance().getPackage(directory) ?: return
        val packageName = dirPackage.qualifiedName

        val text = VfsUtilCore.loadText(vFile)
        WriteCommandAction.runWriteCommandAction(project) {
            vFile.setBinaryContent("package $packageName\n$text".toByteArray())
            vFile.refresh(false, false)
        }
    }
}

val handleGo = fun(vFile: VirtualFile, project: Project) {
    val psiFile = PsiManager.getInstance(project).findFile(vFile) ?: return
    if (vFile.extension == "go") {
        val pFile = vFile.parent ?: return
        val directory: PsiDirectory = psiFile.containingDirectory ?: return
        var packageName: String? = ""
        if (pFile.children.size > 1) {
            for (sFile in pFile.children) {
                if (sFile == vFile || sFile.extension != "go") {
                    continue
                }
                val otherPsiFile = PsiManager.getInstance(project).findFile(sFile) ?: continue
                try {
                    val method = otherPsiFile::class.java.getDeclaredMethod("getPackageName")
                    packageName = method.invoke(otherPsiFile) as String
                    if (packageName.isNullOrEmpty()) {
                        continue
                    }
                } catch (e: Exception) {
                    break
                }
            }
        }
        if (packageName.isNullOrEmpty()) {
            packageName = directory.name
        }
        val text = VfsUtilCore.loadText(vFile)
        WriteCommandAction.runWriteCommandAction(project) {
            vFile.setBinaryContent("package $packageName\n$text".toByteArray())
            vFile.refresh(false, false)
        }
    }
}