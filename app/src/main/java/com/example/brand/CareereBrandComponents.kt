package com.example.brand

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

/**
 * Geometric Careeré Monogram "C" constructed from a folded document.
 * Includes:
 * 1. Letter C outer profile with sheet-of-paper geometry
 * 2. Refined folded top-right page corner
 * 3. Three subtle horizontal document lines
 * 4. Micro-size optical compensation for small screens (16-24px)
 */
@Composable
fun CareereMonogram(
  modifier: Modifier = Modifier,
  size: Dp = 48.dp,
  bodyColor: Color = WarmIvory,
  foldColor: Color = WarmStone,
  creaseColor: Color = MutedRose,
  linesColor: Color = WarmIvory,
  showGrid: Boolean = false,
  gridColor: Color = MutedRose.copy(alpha = 0.25f)
) {
  Canvas(
    modifier = modifier.size(size)
  ) {
    val w = this.size.width
    val h = this.size.height

    if (showGrid) {
      drawConstructionGrid(w, h, gridColor)
    }

    drawCareereGeometry(
      w = w,
      h = h,
      bodyColor = bodyColor,
      foldColor = foldColor,
      creaseColor = creaseColor,
      linesColor = linesColor
    )
  }
}

/**
 * Renders the precision vector geometry of the Careeré monogram
 */
private fun DrawScope.drawCareereGeometry(
  w: Float,
  h: Float,
  bodyColor: Color,
  foldColor: Color,
  creaseColor: Color,
  linesColor: Color
) {
  // Proportions mapped to 0..1 bounding box
  val left = w * 0.18f
  val right = w * 0.82f
  val top = h * 0.14f
  val bottom = h * 0.86f

  val spineWidth = (right - left) * 0.28f
  val armHeight = (bottom - top) * 0.19f
  val cornerFoldSize = (right - left) * 0.32f

  // 1. Folded Corner coordinates:
  // Top-right of document folds down-left
  val foldX1 = right - cornerFoldSize // where fold begins on top edge
  val foldY1 = top
  val foldX2 = right                  // where fold ends on right edge
  val foldY2 = top + cornerFoldSize

  // Outer C Path (Letter C shaped like a folded sheet of paper)
  val cPath = Path().apply {
    // Top-left corner
    moveTo(left, top + armHeight * 0.4f)
    // Left vertical spine
    lineTo(left, bottom - armHeight * 0.4f)
    // Bottom-left corner curve
    quadraticTo(left, bottom, left + armHeight * 0.4f, bottom)
    // Bottom arm extending right
    lineTo(right - armHeight * 0.2f, bottom)
    quadraticTo(right, bottom, right, bottom - armHeight * 0.2f)
    lineTo(right, bottom - armHeight)
    // Inner bottom cut
    lineTo(left + spineWidth + armHeight * 0.2f, bottom - armHeight)
    quadraticTo(left + spineWidth, bottom - armHeight, left + spineWidth, bottom - armHeight - armHeight * 0.2f)
    // Inner vertical spine edge
    lineTo(left + spineWidth, top + armHeight + armHeight * 0.2f)
    quadraticTo(left + spineWidth, top + armHeight, left + spineWidth + armHeight * 0.2f, top + armHeight)
    // Inner top cut
    lineTo(right - cornerFoldSize * 0.7f, top + armHeight)
    lineTo(right - cornerFoldSize * 0.7f, top + cornerFoldSize)
    lineTo(foldX2, foldY2)
    // Up along the fold cut to the top edge
    lineTo(foldX1, foldY1)
    // Along top edge to top-left
    lineTo(left + armHeight * 0.4f, top)
    quadraticTo(left, top, left, top + armHeight * 0.4f)
    close()
  }

  drawPath(cPath, color = bodyColor)

  // 2. Refined Folded Page Corner (Triangle flap)
  val foldFlap = Path().apply {
    moveTo(foldX1, foldY1)
    lineTo(foldX1, foldY2)
    lineTo(foldX2, foldY2)
    close()
  }
  drawPath(foldFlap, color = foldColor)

  // Crease line between document body and fold flap
  drawLine(
    color = creaseColor,
    start = Offset(foldX1, foldY1),
    end = Offset(foldX2, foldY2),
    strokeWidth = maxOf(1f, w * 0.022f),
    cap = StrokeCap.Round
  )

  // 3. Three Subtle Horizontal Document Lines inside the C's aperture
  // Optical adjustments: scale thickness with size, ensure visible at >=16px
  val lineHeight = maxOf(1.2f, h * 0.038f)
  val lineX = left + spineWidth * 1.35f
  val lineMaxX = right - (right - left) * 0.08f
  val apertureTop = top + armHeight * 1.35f
  val apertureBottom = bottom - armHeight * 1.15f
  val gap = (apertureBottom - apertureTop) / 2.5f

  // Line 1
  val y1 = apertureTop + gap * 0.2f
  val len1 = (lineMaxX - lineX) * 0.92f
  drawRoundRect(
    color = linesColor.copy(alpha = 0.85f),
    topLeft = Offset(lineX, y1),
    size = Size(len1, lineHeight),
    cornerRadius = CornerRadius(lineHeight / 2f, lineHeight / 2f)
  )

  // Line 2
  val y2 = y1 + gap
  val len2 = (lineMaxX - lineX) * 0.74f
  drawRoundRect(
    color = linesColor.copy(alpha = 0.85f),
    topLeft = Offset(lineX, y2),
    size = Size(len2, lineHeight),
    cornerRadius = CornerRadius(lineHeight / 2f, lineHeight / 2f)
  )

  // Line 3
  val y3 = y2 + gap
  val len3 = (lineMaxX - lineX) * 0.88f
  drawRoundRect(
    color = linesColor.copy(alpha = 0.85f),
    topLeft = Offset(lineX, y3),
    size = Size(len3, lineHeight),
    cornerRadius = CornerRadius(lineHeight / 2f, lineHeight / 2f)
  )
}

/**
 * Visual geometric construction grid for design system inspection
 */
private fun DrawScope.drawConstructionGrid(w: Float, h: Float, gridColor: Color) {
  val stroke = 0.8f
  // Outer bounds
  drawRect(color = gridColor, topLeft = Offset(0f, 0f), size = Size(w, h), style = Stroke(stroke))
  // Center crosshairs
  drawLine(color = gridColor, start = Offset(w / 2f, 0f), end = Offset(w / 2f, h), strokeWidth = stroke)
  drawLine(color = gridColor, start = Offset(0f, h / 2f), end = Offset(w, h / 2f), strokeWidth = stroke)
  // Golden ratio & 45-degree guide
  drawLine(color = gridColor, start = Offset(0f, 0f), end = Offset(w, h), strokeWidth = stroke)
  drawLine(color = gridColor, start = Offset(w, 0f), end = Offset(0f, h), strokeWidth = stroke)
  // Safe zone circle
  drawCircle(color = gridColor, radius = minOf(w, h) * 0.44f, style = Stroke(stroke))
}

/**
 * 1. Primary App Icon (Rounded square, deep burgundy #72232B, warm ivory folded C monogram)
 */
@Composable
fun CareereAppIcon(
  modifier: Modifier = Modifier,
  size: Dp = 64.dp,
  elevation: Dp = 4.dp
) {
  val cornerRadius = size * 0.22f
  Box(
    modifier = modifier
      .size(size)
      .clip(RoundedCornerShape(cornerRadius))
      .background(BurgundyPrimary)
      .border(0.75.dp, BurgundyLight.copy(alpha = 0.35f), RoundedCornerShape(cornerRadius)),
    contentAlignment = Alignment.Center
  ) {
    CareereMonogram(
      size = size * 0.65f,
      bodyColor = WarmIvory,
      foldColor = WarmStone,
      creaseColor = MutedRose,
      linesColor = WarmIvory
    )
  }
}

/**
 * 2. Primary Horizontal Logo (Icon + Wordmark Careeré)
 */
@Composable
fun CareereHorizontalLogo(
  modifier: Modifier = Modifier,
  iconSize: Dp = 36.dp,
  fontSize: TextUnit = 22.sp,
  textColor: Color = NearBlack,
  accentColor: Color = BurgundyPrimary,
  iconOnSquare: Boolean = true
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    if (iconOnSquare) {
      CareereAppIcon(size = iconSize)
    } else {
      CareereMonogram(size = iconSize, bodyColor = accentColor, foldColor = WarmStone, creaseColor = MutedRose, linesColor = accentColor)
    }

    CareereWordmark(
      fontSize = fontSize,
      textColor = textColor,
      accentColor = accentColor
    )
  }
}

/**
 * Editorial Wordmark: "Careeré"
 * Features refined serif typography with the signature accented "é"
 */
@Composable
fun CareereWordmark(
  modifier: Modifier = Modifier,
  fontSize: TextUnit = 24.sp,
  textColor: Color = NearBlack,
  accentColor: Color = BurgundyPrimary
) {
  val text = buildAnnotatedString {
    withStyle(SpanStyle(color = textColor, fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, letterSpacing = (-0.2).sp)) {
      append("Career")
    }
    withStyle(SpanStyle(color = accentColor, fontFamily = FontFamily.Serif, fontWeight = FontWeight.SemiBold, letterSpacing = (-0.2).sp)) {
      append("é")
    }
  }

  Text(
    text = text,
    fontSize = fontSize,
    lineHeight = fontSize * 1.15f,
    modifier = modifier
  )
}
