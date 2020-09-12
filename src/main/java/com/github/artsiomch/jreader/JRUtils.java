package com.github.artsiomch.jreader;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class JRUtils {

  private static final TextAttributes ABSTRACT =
      createTextAttributesKey(
          Color.decode("#FF0CEA"), null, DefaultLanguageHighlighterColors.KEYWORD);
  private static final TextAttributes PUBLIC =
      createTextAttributesKey(
          Color.decode("#00CC1D"), null, DefaultLanguageHighlighterColors.KEYWORD);
  private static final TextAttributes PRIVATE =
      createTextAttributesKey(
          Color.decode("#299999"), null, DefaultLanguageHighlighterColors.KEYWORD);
  private static final TextAttributes OVERRIDE =
      createTextAttributesKey(
          null, null, DefaultLanguageHighlighterColors.METADATA);
  private static final TextAttributes DEPRECATED =
      createTextAttributesKey(
          null, Color.decode("#270F0F"), DefaultLanguageHighlighterColors.METADATA);

  @NotNull
  private static TextAttributes createTextAttributesKey(
      Color frgColor, Color bkgColor, TextAttributesKey baseTextAttributesKey) {

    TextAttributes textAttributes = baseTextAttributesKey.getDefaultAttributes().clone();
    if (frgColor != null) textAttributes.setForegroundColor(frgColor);
    if (bkgColor != null) textAttributes.setBackgroundColor(bkgColor);
    // hack to force repaint color in Kotlin keywords
    else textAttributes.setBackgroundColor(Color.decode("#000000"));

    return textAttributes;
  }

  static void markPublic(@NotNull AnnotationHolder annotationHolder) {
    annotationHolder
        .newSilentAnnotation(HighlightSeverity.INFORMATION)
        .enforcedTextAttributes(PUBLIC)
        .create();
  }

  static void markPrivate(@NotNull AnnotationHolder annotationHolder) {
    annotationHolder
        .newSilentAnnotation(HighlightSeverity.INFORMATION)
        .enforcedTextAttributes(PRIVATE)
        .create();
  }

  static void markOverride(@NotNull AnnotationHolder annotationHolder) {
    annotationHolder
        .newSilentAnnotation(HighlightSeverity.INFORMATION)
        .enforcedTextAttributes(OVERRIDE)
        .create();
  }

  static void markAbstract(
      @NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
    annotationHolder
        .createInfoAnnotation(psiElement, "")
        .setEnforcedTextAttributes(JRUtils.ABSTRACT);
  }

  static void markDeprecated(
      @NotNull PsiElement psiElement, @NotNull AnnotationHolder annotationHolder) {
    Document document = psiElement.getContainingFile().getViewProvider().getDocument();
    if (document == null) return;
    int lineNumber = document.getLineNumber(psiElement.getTextOffset());
    int lineStartOffset = document.getLineStartOffset(lineNumber);
    int lineEndOffset = document.getLineEndOffset(lineNumber);
    annotationHolder
        .createInfoAnnotation(new TextRange(lineStartOffset, lineEndOffset + 1), "")
        .setEnforcedTextAttributes(DEPRECATED);
    /*
          PsiElement parentToHighlight = PsiTreeUtil.getParentOfType(psiElement, PsiModifierList.class);
          if (parentToHighlight != null) {
            annotationHolder.createInfoAnnotation(parentToHighlight, "").setTextAttributes(DEPRECTED);
          }
    */
  }
}
