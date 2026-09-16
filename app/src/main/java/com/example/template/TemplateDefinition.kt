package com.example.template

import com.example.model.TemplateCategory

enum class LayoutStructure {
  SINGLE_COLUMN,       // Classic linear ATS structure
  TWO_COLUMN_LEFT,     // Sidebar on left (30/70)
  TWO_COLUMN_RIGHT,    // Sidebar on right (70/30)
  HEADER_ACCENT,       // Bold top header block with single-column body
  SPLIT_PANEL,         // Clean vertical dual pane
  MODERN_CARD,         // Elegant bordered content blocks
  BORDERED_MINIMAL     // Subtle architectural hairline borders
}

enum class TypographyStyle {
  SERIF_EDITORIAL,     // Refined French/European serif headings
  SANS_SWISS,          // Clean Swiss modernist grotesque sans-serif
  MONO_TECHNICAL,      // Minimalist technical monospace accents
  HUMANIST_CLEAN       // Warm open humanist sans
}

enum class DividerStyle {
  NONE,
  HAIRLINE,
  SOLID_BAR,
  ACCENT_DOT,
  DOUBLE_LINE
}

data class TemplateDefinition(
  val id: String,
  val name: String,
  val category: TemplateCategory,
  val atsScore: Int,                 // 0 to 100 ATS suitability score
  val isAtsSafe: Boolean,
  val supportsPhoto: Boolean,
  val layout: LayoutStructure,
  val typography: TypographyStyle,
  val primaryAccentHex: String,
  val secondaryColorHex: String,
  val dividerStyle: DividerStyle,
  val description: String,
  val tags: List<String> = emptyList()
)
