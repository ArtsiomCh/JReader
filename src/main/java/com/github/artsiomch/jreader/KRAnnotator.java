package com.github.artsiomch.jreader;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.psi.PsiElement;
import org.jetbrains.kotlin.psi.KtDeclarationModifierList;
import org.jetbrains.kotlin.psi.KtNameReferenceExpression;
import org.jetbrains.annotations.NotNull;

public class KRAnnotator implements Annotator {
  @Override
  public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
    if (psiElement.getParent() == null) return;

    if (psiElement.getParent() instanceof KtDeclarationModifierList) {
      if (psiElement.textMatches("private")) {
        JRUtils.markPrivate(annotationHolder);
      } else if (psiElement.textMatches("public")) {
        JRUtils.markPublic(annotationHolder);
      }
    } else if (psiElement.getParent() instanceof KtNameReferenceExpression
        && psiElement.textMatches("Deprecated")) {
      JRUtils.markDeprecated(psiElement, annotationHolder);
    }
  }
}
