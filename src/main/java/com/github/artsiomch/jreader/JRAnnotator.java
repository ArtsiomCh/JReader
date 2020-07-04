package com.github.artsiomch.jreader;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class JRAnnotator implements Annotator {
  @Override
  public void annotate(@NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
    if (psiElement instanceof PsiKeyword) {
      if (psiElement.textMatches("abstract")) {
        annotationHolder.createWeakWarningAnnotation(psiElement, "").setTextAttributes(ABSTRACT);
      } else if (psiElement.textMatches("public")) {
        annotationHolder.createInfoAnnotation(psiElement, "").setTextAttributes(PUBLIC);
      } else if (psiElement.textMatches("private")) {
        annotationHolder.createInfoAnnotation(psiElement, "").setTextAttributes(PRIVATE);
      }
    } else if (psiElement instanceof PsiJavaCodeReferenceElement
        && psiElement.textMatches("Deprecated")) {
      Document document = psiElement.getContainingFile().getViewProvider().getDocument();
      if (document == null) return;
      int lineNumber = document.getLineNumber(psiElement.getTextOffset());
      int lineStartOffset = document.getLineStartOffset(lineNumber);
      int lineEndOffset = document.getLineEndOffset(lineNumber);
      annotationHolder
          .createInfoAnnotation(new TextRange(lineStartOffset, lineEndOffset + 1), "")
          .setTextAttributes(DEPRECTED);
      /*
            PsiElement parentToHighlight = PsiTreeUtil.getParentOfType(psiElement, PsiModifierList.class);
            if (parentToHighlight != null) {
              annotationHolder.createInfoAnnotation(parentToHighlight, "").setTextAttributes(DEPRECTED);
            }
      */
    }
  }

  private static final TextAttributesKey ABSTRACT =
      createTextAttributesKey(
          "abstract", Color.decode("#FF0CEA"), null, DefaultLanguageHighlighterColors.KEYWORD);

  private static final TextAttributesKey PUBLIC =
      createTextAttributesKey(
          "public", Color.decode("#00CC1D"), null, DefaultLanguageHighlighterColors.KEYWORD);

  private static final TextAttributesKey PRIVATE =
      createTextAttributesKey(
          "private", Color.decode("#299999"), null, DefaultLanguageHighlighterColors.KEYWORD);

  private static final TextAttributesKey DEPRECTED =
      createTextAttributesKey(
          "deprecated", null, Color.decode(/*"#1B0D0D"*/"#270F0F"), DefaultLanguageHighlighterColors.METADATA);

  @NotNull
  private static TextAttributesKey createTextAttributesKey(
      String externalName,
      Color frgColor,
      Color bkgColor,
      TextAttributesKey baseTextAttributesKey) {
    TextAttributes textAttributes = baseTextAttributesKey.getDefaultAttributes().clone();
    if (frgColor != null) textAttributes.setForegroundColor(frgColor);
    if (bkgColor != null) textAttributes.setBackgroundColor(bkgColor);
    return TextAttributesKey.createTextAttributesKey(externalName, textAttributes);
  }
}
