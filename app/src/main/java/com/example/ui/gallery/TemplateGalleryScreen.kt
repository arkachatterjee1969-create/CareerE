package com.example.ui.gallery

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brand.CareereAppIcon
import com.example.model.TemplateCategory
import com.example.template.TemplateDefinition
import com.example.template.TemplateRepository
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateGalleryScreen(
  currentTemplateId: String,
  onSelectTemplate: (String) -> Unit,
  onClose: () -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }
  var selectedCategory by remember { mutableStateOf(TemplateCategory.ALL) }
  var atsOnly by remember { mutableStateOf(false) }
  var photoOnly by remember { mutableStateOf(false) }

  val filteredTemplates = remember(searchQuery, selectedCategory, atsOnly, photoOnly) {
    TemplateRepository.filter(
      category = selectedCategory,
      atsOnly = atsOnly,
      photoOnly = photoOnly,
      query = searchQuery
    )
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            CareereAppIcon(size = 28.dp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text("50+ Professional Templates", style = MaterialTheme.typography.titleMedium, color = NearBlack)
              Text("Free • No Watermark • ATS Optimized", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }
          }
        },
        actions = {
          IconButton(onClick = onClose) {
            Icon(Icons.Default.Close, contentDescription = "Close")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmIvory)
      )
    },
    containerColor = WarmIvory
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
    ) {
      // Search Bar
      OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("Search templates by name, style, or industry...", fontSize = 13.sp) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
        trailingIcon = {
          if (searchQuery.isNotEmpty()) {
            IconButton(onClick = { searchQuery = "" }) {
              Icon(Icons.Default.Clear, contentDescription = "Clear")
            }
          }
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
          focusedContainerColor = Color.White,
          unfocusedContainerColor = Color.White,
          focusedBorderColor = BurgundyPrimary,
          unfocusedBorderColor = WarmStoneBorder
        ),
        shape = RoundedCornerShape(10.dp)
      )

      // Category Filter Chips
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState())
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        TemplateCategory.values().forEach { category ->
          FilterChip(
            selected = selectedCategory == category,
            onClick = { selectedCategory = category },
            label = { Text(category.displayName, fontSize = 12.sp) },
            colors = FilterChipDefaults.filterChipColors(
              selectedContainerColor = BurgundyPrimary,
              selectedLabelColor = WarmIvory
            )
          )
        }
      }

      // Quick Toggles (ATS Only, Photo Supported)
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        FilterChip(
          selected = atsOnly,
          onClick = { atsOnly = !atsOnly },
          label = { Text("ATS Safe (90%+)", fontSize = 11.sp) },
          leadingIcon = {
            if (atsOnly) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
          }
        )

        FilterChip(
          selected = photoOnly,
          onClick = { photoOnly = !photoOnly },
          label = { Text("Photo Ready", fontSize = 11.sp) },
          leadingIcon = {
            if (photoOnly) Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
          }
        )

        Spacer(modifier = Modifier.weight(1f))
        Text(
          "${filteredTemplates.size} available",
          style = MaterialTheme.typography.labelSmall,
          color = TextSecondary
        )
      }

      // Template Grid
      LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = Modifier
          .fillMaxWidth()
          .height(500.dp)
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        if (filteredTemplates.isEmpty() && searchQuery.isEmpty()) {
          // Show placeholders
          items(6) {
             Box(modifier = Modifier
               .width(260.dp)
               .height(200.dp)
               .clip(RoundedCornerShape(12.dp))
               .background(WarmStoneLight)
             )
          }
        } else {
          items(filteredTemplates, key = { it.id }) { item ->
            Box(modifier = Modifier.width(260.dp)) {
              TemplateCard(
                template = item,
                isSelected = item.id == currentTemplateId,
                onSelect = {
                  onSelectTemplate(item.id)
                  onClose()
                }
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun TemplateCard(
  template: TemplateDefinition,
  isSelected: Boolean,
  onSelect: () -> Unit
) {
  val accentColor = try {
    Color(android.graphics.Color.parseColor(template.primaryAccentHex))
  } catch (e: Exception) {
    BurgundyPrimary
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onSelect() },
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = BorderStroke(
      width = if (isSelected) 2.dp else 1.dp,
      color = if (isSelected) BurgundyPrimary else WarmStoneBorder
    ),
    shape = RoundedCornerShape(12.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      // Mini Visual Blueprint
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(WarmIvorySurface)
          .border(0.8.dp, WarmStoneBorder, RoundedCornerShape(8.dp))
          .padding(10.dp)
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
          // Header preview line
          Box(
            modifier = Modifier
              .fillMaxWidth(0.55f)
              .height(8.dp)
              .background(accentColor, RoundedCornerShape(2.dp))
          )
          Box(
            modifier = Modifier
              .fillMaxWidth(0.35f)
              .height(4.dp)
              .background(TextTertiary.copy(alpha = 0.5f), RoundedCornerShape(2.dp))
          )

          HorizontalDivider(color = WarmStoneBorder, thickness = 0.8.dp, modifier = Modifier.padding(vertical = 4.dp))

          // Body mock lines
          Box(
            modifier = Modifier
              .fillMaxWidth(0.85f)
              .height(4.dp)
              .background(TextSecondary.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
          )
          Box(
            modifier = Modifier
              .fillMaxWidth(0.70f)
              .height(4.dp)
              .background(TextSecondary.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
          )
          Box(
            modifier = Modifier
              .fillMaxWidth(0.90f)
              .height(4.dp)
              .background(TextSecondary.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
          )
        }

        // Active selection tag
        if (isSelected) {
          Surface(
            modifier = Modifier.align(Alignment.TopEnd),
            color = BurgundyPrimary,
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              "ACTIVE",
              color = WarmIvory,
              fontSize = 9.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Title and Category
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = template.name,
          style = MaterialTheme.typography.titleSmall,
          color = NearBlack,
          fontWeight = FontWeight.Bold
        )

        Surface(
          color = if (template.isAtsSafe) StatusSuccess.copy(alpha = 0.12f) else WarmStoneLight,
          shape = RoundedCornerShape(4.dp)
        ) {
          Text(
            text = "ATS ${template.atsScore}%",
            color = if (template.isAtsSafe) StatusSuccess else TextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
          )
        }
      }

      Text(
        text = template.description,
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary,
        maxLines = 2,
        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
      )

      // Actions
      Button(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
          containerColor = if (isSelected) WarmStoneLight else BurgundyPrimary,
          contentColor = if (isSelected) NearBlack else WarmIvory
        ),
        shape = RoundedCornerShape(6.dp)
      ) {
        Text(if (isSelected) "Current Template" else "Use This Template", fontSize = 12.sp)
      }
    }
  }
}
