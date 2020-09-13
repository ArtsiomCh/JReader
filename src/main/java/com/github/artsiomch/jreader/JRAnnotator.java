package com.github.artsiomch.jreader;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

public class JRAnnotator implements Annotator {
  @Override
  public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
    if (psiElement instanceof PsiKeyword) {
      if (psiElement.textMatches("abstract")) {
        JRUtils.markAbstract(annotationHolder);
      } else if (psiElement.textMatches("public")) {
        JRUtils.markPublic(annotationHolder);
      } else if (psiElement.textMatches("private")) {
        JRUtils.markPrivate(annotationHolder);
      }
    } else if (psiElement instanceof PsiJavaCodeReferenceElement
        && psiElement.textMatches("Deprecated")) {
      JRUtils.markDeprecated(psiElement, annotationHolder);
    }
  }

}
