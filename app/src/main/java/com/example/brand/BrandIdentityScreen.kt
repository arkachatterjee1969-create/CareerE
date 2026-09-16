package com.example.brand

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrandIdentityScreen(
  onNavigateToStudio: () -> Unit = {}
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var showGrid by remember { mutableStateOf(true) }
  var testScaleIndex by remember { mutableIntStateOf(3) } // 48dp default

  val testScales = listOf(16, 24, 32, 48, 64, 96, 128, 180)

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            CareereHorizontalLogo(iconSize = 28.dp, fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
              color = BurgundyPrimary.copy(alpha = 0.1f),
              shape = RoundedCornerShape(4.dp)
            ) {
              Text(
                "BRAND IDENTITY",
                style = MaterialTheme.typography.labelSmall,
                color = BurgundyPrimary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                fontWeight = FontWeight.Bold
              )
            }
          }
        },
        actions = {
          FilledTonalButton(
            onClick = onNavigateToStudio,
            colors = ButtonDefaults.filledTonalButtonColors(
              containerColor = BurgundyPrimary,
              contentColor = WarmIvory
            )
          ) {
            Icon(Icons.Default.Article, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Launch CV Studio", fontSize = 13.sp)
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = WarmIvory
        )
      )
    },
    containerColor = WarmIvory
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .verticalScroll(rememberScrollState())
    ) {
      // Hero Brand Statement
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(WarmIvorySurface)
          .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        CareereAppIcon(size = 96.dp)

        Spacer(modifier = Modifier.height(18.dp))

        CareereWordmark(fontSize = 38.sp)

        Text(
          "pronounced career-ay",
          style = MaterialTheme.typography.labelMedium,
          color = TextTertiary,
          modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          "Luxury editorial design meets modern productivity SaaS.\nA completely free, elegant, intelligent CV builder designed with Swiss precision and French typography.",
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondary,
          textAlign = TextAlign.Center,
          modifier = Modifier.widthIn(max = 580.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Quick Highlights Pill Row
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
          BrandPill("Geometric Monogram C")
          BrandPill("Folded Document Geometry")
          BrandPill("3 Precision Document Lines")
          BrandPill("100% Vector Scalable")
          BrandPill("Always Free Promise")
        }
      }

      // Tab Navigation Bar
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = WarmIvory,
        contentColor = BurgundyPrimary,
        divider = { HorizontalDivider(color = WarmStoneBorder) }
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = { Text("10 Brand Variants", fontSize = 13.sp) }
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = { Text("Symbol Construction", fontSize = 13.sp) }
        )
        Tab(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          text = { Text("Color System", fontSize = 13.sp) }
        )
        Tab(
          selected = selectedTab == 3,
          onClick = { selectedTab = 3 },
          text = { Text("Typography & Craft", fontSize = 13.sp) }
        )
      }

      // Tab Content
      when (selectedTab) {
        0 -> VariantsShowcaseTab(testScales, testScaleIndex) { testScaleIndex = it }
        1 -> ConstructionTab(showGrid) { showGrid = it }
        2 -> ColorPaletteTab()
        3 -> TypographyCraftTab()
      }

      Spacer(modifier = Modifier.height(48.dp))
    }
  }
}

@Composable
private fun BrandPill(text: String) {
  Surface(
    color = WarmStoneLight,
    shape = RoundedCornerShape(16.dp),
    border = BorderStroke(1.dp, WarmStoneBorder)
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.labelSmall,
      color = TextSecondary,
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
    )
  }
}

/**
 * Tab 1: The 10 Brand Variants
 */
@Composable
private fun VariantsShowcaseTab(
  testScales: List<Int>,
  selectedScaleIndex: Int,
  onSelectScale: (Int) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(24.dp)
  ) {
    // Section Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text("The 10 Official Variants", style = MaterialTheme.typography.titleLarge, color = NearBlack)
        Text("Engineered for optical clarity across mobile, print, and web", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
      }
    }

    // 1. Primary Horizontal Logo
    VariantCard(
      number = "01",
      title = "Primary Horizontal Logo",
      description = "Full identity lockup with App Icon and Editorial Wordmark"
    ) {
      CareereHorizontalLogo(iconSize = 44.dp, fontSize = 26.sp)
    }

    // 2. Official App Icon
    VariantCard(
      number = "02",
      title = "Official App Icon (Squircle)",
      description = "Deep Burgundy #72232B canvas with Warm Ivory #FAF7F2 monogram"
    ) {
      CareereAppIcon(size = 80.dp)
    }

    // 3. Standalone Monogram
    VariantCard(
      number = "03",
      title = "Standalone Monogram C",
      description = "Pure vector geometry for stamps, letterheads, and watermarks"
    ) {
      CareereMonogram(
        size = 64.dp,
        bodyColor = BurgundyPrimary,
        foldColor = WarmStone,
        creaseColor = MutedRose,
        linesColor = BurgundyPrimary
      )
    }

    // 4. Black Monochrome
    VariantCard(
      number = "04",
      title = "Black Monochrome (#1F1F1F)",
      description = "High-contrast single-ink for official government & black-and-white printing"
    ) {
      CareereMonogram(
        size = 56.dp,
        bodyColor = NearBlack,
        foldColor = Color(0xFF666666),
        creaseColor = Color(0xFF999999),
        linesColor = NearBlack
      )
    }

    // 5. White Monochrome (Reversed)
    VariantCard(
      number = "05",
      title = "White Monochrome (Reversed)",
      description = "Reversed for deep charcoal backgrounds, dark mode, and foil stamping"
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(NearBlack, RoundedCornerShape(8.dp))
          .padding(20.dp),
        contentAlignment = Alignment.Center
      ) {
        CareereMonogram(
          size = 56.dp,
          bodyColor = Color.White,
          foldColor = Color(0xFFCCCCCC),
          creaseColor = Color(0xFFAAAAAA),
          linesColor = Color.White
        )
      }
    }

    // 6. Burgundy Monochrome
    VariantCard(
      number = "06",
      title = "Burgundy Monochrome",
      description = "Tonal expression using Careeré signature burgundy shades"
    ) {
      CareereMonogram(
        size = 56.dp,
        bodyColor = BurgundyPrimary,
        foldColor = BurgundyDark,
        creaseColor = BurgundyLight,
        linesColor = BurgundyPrimary
      )
    }

    // 7. Light-Background Version
    VariantCard(
      number = "07",
      title = "Light-Background Lockup",
      description = "Optimal display on Warm Ivory and cream European stationery"
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(WarmIvory, RoundedCornerShape(8.dp))
          .border(1.dp, WarmStoneBorder, RoundedCornerShape(8.dp))
          .padding(20.dp),
        contentAlignment = Alignment.Center
      ) {
        CareereHorizontalLogo(iconSize = 36.dp, fontSize = 22.sp, iconOnSquare = false)
      }
    }

    // 8. Dark-Background Version
    VariantCard(
      number = "08",
      title = "Dark-Background Lockup",
      description = "Rich contrast on near-black executive surfaces and dark mode"
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(NearBlack, RoundedCornerShape(8.dp))
          .padding(20.dp),
        contentAlignment = Alignment.Center
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          CareereAppIcon(size = 36.dp)
          Text(
            "Careeré",
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 22.sp,
            color = WarmIvory
          )
        }
      }
    }

    // 9. Favicon Version (16px, 24px, 32px)
    VariantCard(
      number = "09",
      title = "Favicon & App Badges",
      description = "Engineered with thickened strokes for browser tabs & notification bars"
    ) {
      Row(
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          CareereAppIcon(size = 32.dp)
          Text("32px", style = MaterialTheme.typography.labelSmall, color = TextTertiary, modifier = Modifier.padding(top = 4.dp))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          CareereAppIcon(size = 24.dp)
          Text("24px", style = MaterialTheme.typography.labelSmall, color = TextTertiary, modifier = Modifier.padding(top = 4.dp))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          CareereAppIcon(size = 16.dp)
          Text("16px", style = MaterialTheme.typography.labelSmall, color = TextTertiary, modifier = Modifier.padding(top = 4.dp))
        }
      }
    }

    // 10. Small-Size Scaler Interactive Tool
    VariantCard(
      number = "10",
      title = "Small-Size Scalability Test Bench",
      description = "Verify vector fidelity and stroke clarity from 16px to 180px"
    ) {
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          testScales.forEachIndexed { index, scale ->
            FilterChip(
              selected = selectedScaleIndex == index,
              onClick = { onSelectScale(index) },
              label = { Text("${scale}px", fontSize = 11.sp) }
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
          modifier = Modifier
            .size(200.dp)
            .background(WarmIvorySurface, RoundedCornerShape(12.dp))
            .border(1.dp, WarmStoneBorder, RoundedCornerShape(12.dp)),
          contentAlignment = Alignment.Center
        ) {
          val activeDp = testScales[selectedScaleIndex].dp
          CareereAppIcon(size = activeDp)
        }

        Text(
          "Rendering at exactly ${testScales[selectedScaleIndex]}dp × ${testScales[selectedScaleIndex]}dp",
          style = MaterialTheme.typography.labelSmall,
          color = TextSecondary,
          modifier = Modifier.padding(top = 8.dp)
        )
      }
    }
  }
}

@Composable
private fun VariantCard(
  number: String,
  title: String,
  description: String,
  content: @Composable () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, WarmStoneBorder),
    shape = RoundedCornerShape(12.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(18.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              number,
              style = MaterialTheme.typography.labelMedium,
              color = BurgundyPrimary,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, color = NearBlack)
          }
          Text(
            description,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(WarmIvorySurface, RoundedCornerShape(8.dp))
          .padding(20.dp),
        contentAlignment = Alignment.Center
      ) {
        content()
      }
    }
  }
}

/**
 * Tab 2: Symbol Construction & Engineering Grid
 */
@Composable
private fun ConstructionTab(
  showGrid: Boolean,
  onToggleGrid: (Boolean) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text("Symbol Geometry & Blueprint", style = MaterialTheme.typography.titleLarge, color = NearBlack)
        Text("Optical alignment and mathematical document construction", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
      }
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Grid Overlay", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        Switch(
          checked = showGrid,
          onCheckedChange = onToggleGrid,
          modifier = Modifier.padding(start = 6.dp)
        )
      }
    }

    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = BorderStroke(1.dp, WarmStoneBorder),
      shape = RoundedCornerShape(12.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(240.dp)
            .background(BurgundyPrimary, RoundedCornerShape(32.dp))
            .border(1.dp, BurgundyLight.copy(alpha = 0.4f), RoundedCornerShape(32.dp)),
          contentAlignment = Alignment.Center
        ) {
          CareereMonogram(
            size = 170.dp,
            bodyColor = WarmIvory,
            foldColor = WarmStone,
            creaseColor = MutedRose,
            linesColor = WarmIvory,
            showGrid = showGrid
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(color = WarmStoneBorder)

        Spacer(modifier = Modifier.height(16.dp))

        // Architectural Specification Grid
        Text("Geometric Principles", style = MaterialTheme.typography.titleMedium, color = NearBlack)

        Spacer(modifier = Modifier.height(12.dp))

        BlueprintRow(
          label = "Letterform Foundation",
          detail = "Built from the capital 'C' with an inner 30° vertical spine ratio."
        )
        BlueprintRow(
          label = "Negative Space Document",
          detail = "The inner counter form mimics an A4 standard sheet proportions."
        )
        BlueprintRow(
          label = "45° Fold Corner",
          detail = "The upper right corner features an engineered 45-degree fold facet."
        )
        BlueprintRow(
          label = "3 Document Lines",
          detail = "Three calibrated micro-strokes evoke resume bullet lines without clutter."
        )
        BlueprintRow(
          label = "Squircle Squaring",
          detail = "Super-elliptical n=4 rounded squircle provides seamless Android/iOS continuity."
        )
      }
    }
  }
}

@Composable
private fun BlueprintRow(label: String, detail: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 6.dp),
    verticalAlignment = Alignment.Top
  ) {
    Text("•", color = BurgundyPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
    Column {
      Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = NearBlack)
      Text(detail, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
    }
  }
}

/**
 * Tab 3: Official Color System
 */
@Composable
private fun ColorPaletteTab() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    Text("Official Color System", style = MaterialTheme.typography.titleLarge, color = NearBlack)
    Text(
      "Harmonious European palette inspired by fine burgundy leather bindings, warm ivory cotton paper, and Parisian ink.",
      style = MaterialTheme.typography.bodySmall,
      color = TextSecondary
    )

    ColorSwatchCard(
      name = "Primary Burgundy",
      hex = "#72232B",
      role = "Primary Brand Color • App Icon Canvas • Section Anchors",
      color = BurgundyPrimary,
      textColor = WarmIvory
    )

    ColorSwatchCard(
      name = "Near Black",
      hex = "#1F1F1F",
      role = "High-Contrast Editorial Typography • Precision Lines",
      color = NearBlack,
      textColor = WarmIvory
    )

    ColorSwatchCard(
      name = "Warm Ivory",
      hex = "#FAF7F2",
      role = "Primary Application Canvas • Document Paper Tone",
      color = WarmIvory,
      textColor = NearBlack,
      hasBorder = true
    )

    ColorSwatchCard(
      name = "Muted Rose",
      hex = "#C9A3A6",
      role = "Secondary Accent • Page Crease • Interactive Subtlety",
      color = MutedRose,
      textColor = NearBlack
    )

    ColorSwatchCard(
      name = "Warm Stone",
      hex = "#D8D1C7",
      role = "Folded Document Flap • Hairline Dividers • Structural Borders",
      color = WarmStone,
      textColor = NearBlack
    )
  }
}

@Composable
private fun ColorSwatchCard(
  name: String,
  hex: String,
  role: String,
  color: Color,
  textColor: Color,
  hasBorder: Boolean = false
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(1.dp, WarmStoneBorder),
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(64.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(color)
          .then(if (hasBorder) Modifier.border(1.dp, WarmStoneBorder, RoundedCornerShape(8.dp)) else Modifier)
      )

      Spacer(modifier = Modifier.width(16.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(name, style = MaterialTheme.typography.titleMedium, color = NearBlack)
          Text(hex, style = MaterialTheme.typography.labelLarge, color = BurgundyPrimary, fontWeight = FontWeight.Bold)
        }
        Text(role, style = MaterialTheme.typography.bodySmall, color = TextSecondary, modifier = Modifier.padding(top = 2.dp))
      }
    }
  }
}

/**
 * Tab 4: Typography & Craft
 */
@Composable
private fun TypographyCraftTab() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(20.dp),
    verticalArrangement = Arrangement.spacedBy(20.dp)
  ) {
    Text("Typography & Brand Voice", style = MaterialTheme.typography.titleLarge, color = NearBlack)
    Text(
      "The Careeré wordmark celebrates high-contrast French editorial serif typography with an accented signature 'é'.",
      style = MaterialTheme.typography.bodySmall,
      color = TextSecondary
    )

    Card(
      modifier = Modifier.fillMaxWidth(),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      border = BorderStroke(1.dp, WarmStoneBorder),
      shape = RoundedCornerShape(12.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp)
      ) {
        Text(
          "Wordmark Breakdown",
          style = MaterialTheme.typography.labelMedium,
          color = BurgundyPrimary,
          fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        CareereWordmark(fontSize = 44.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          "• The base 'Career' is set in high-contrast editorial serif, evoking classic French publishing and literary prestige.\n" +
          "• The acute accent on the final 'é' (career-ay) serves as a subtle signature accent mark, colored in Primary Burgundy.\n" +
          "• Paired with Swiss Sans-Serif (Grotesque / Inter) for all UI controls, forms, and data inputs to ensure effortless readability.",
          style = MaterialTheme.typography.bodyMedium,
          color = TextSecondary,
          lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        HorizontalDivider(color = WarmStoneBorder)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Type Hierarchy Pairing", style = MaterialTheme.typography.titleMedium, color = NearBlack)

        Spacer(modifier = Modifier.height(12.dp))

        TypeSampleRow("Display Serif", "Alexandre Laurent", 28.sp, FontFamily.Serif, FontWeight.Normal)
        TypeSampleRow("Headline Serif", "Senior Software Architect", 18.sp, FontFamily.Serif, FontWeight.Medium)
        TypeSampleRow("UI Title (Swiss)", "Work Experience & History", 15.sp, FontFamily.SansSerif, FontWeight.SemiBold)
        TypeSampleRow("Body (Swiss)", "Spearheaded distributed streaming infrastructure handling 180M+ daily events.", 13.sp, FontFamily.SansSerif, FontWeight.Normal)
        TypeSampleRow("Metadata", "Paris, France • 2022 – Present", 11.sp, FontFamily.SansSerif, FontWeight.Medium)
      }
    }
  }
}

@Composable
private fun TypeSampleRow(label: String, sample: String, size: androidx.compose.ui.unit.TextUnit, family: FontFamily, weight: FontWeight) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp)
  ) {
    Text(label, style = MaterialTheme.typography.labelSmall, color = BurgundyPrimary, fontWeight = FontWeight.Bold)
    Text(
      sample,
      fontSize = size,
      fontFamily = family,
      fontWeight = weight,
      color = NearBlack,
      modifier = Modifier.padding(top = 2.dp)
    )
  }
}
