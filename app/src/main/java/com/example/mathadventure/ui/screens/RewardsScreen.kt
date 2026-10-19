package com.example.mathadventure.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathadventure.data.model.BadgesCatalog
import com.example.mathadventure.localization.AppStrings
import com.example.mathadventure.localization.MathFormatter
import com.example.mathadventure.ui.viewmodel.MathAdventureViewModel
import com.example.mathadventure.ui.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsScreen(
    uiState: UiState,
    viewModel: MathAdventureViewModel,
    modifier: Modifier = Modifier
) {
    val strings = AppStrings(uiState.language)
    val profile = uiState.activeProfile
    val currentStars = profile?.stars ?: 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.rewards, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("rewards_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.back)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Stats summary card
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⭐", fontSize = 28.sp)
                            Text(
                                text = MathFormatter.format(profile?.stars ?: 0, uiState.numeralFormat),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF854D0E)
                            )
                            Text(strings.stars, style = MaterialTheme.typography.labelSmall, color = Color(0xFF78350F))
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🪙", fontSize = 28.sp)
                            Text(
                                text = MathFormatter.format(profile?.coins ?: 0, uiState.numeralFormat),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF9A3412)
                            )
                            Text(strings.coins, style = MaterialTheme.typography.labelSmall, color = Color(0xFF7C2D12))
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔥", fontSize = 28.sp)
                            Text(
                                text = MathFormatter.format(profile?.currentStreak ?: 1, uiState.numeralFormat),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFDC2626)
                            )
                            Text(strings.streak, style = MaterialTheme.typography.labelSmall, color = Color(0xFF991B1B))
                        }
                    }
                }
            }

            // Robot Customization Wardrobe
            item {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (strings.isAr) "خزانة أزياء الروبوت كوانتم" else "Robo-Chip Customization Wardrobe",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val skins = listOf(
                        Pair("🤖", if (strings.isAr) "الأزرق الكلاسيكي" else "Classic Blue"),
                        Pair("🦾", if (strings.isAr) "الدرع الذهبي" else "Golden Armor"),
                        Pair("🛸", if (strings.isAr) "الدرع الكوني" else "Cosmic Shield")
                    )
                    skins.forEach { (emoji, label) ->
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(14.dp))
                                .border(2.dp, Color(0xFFCBD5E1), RoundedCornerShape(14.dp)),
                            color = Color.White
                        ) {
                            Column(
                                modifier = Modifier.padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(emoji, fontSize = 32.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }

            // Badges Section Header
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (strings.isAr) "لوحة الأوسمة والإنجازات" else "Hall of Badges & Trophies",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(BadgesCatalog.ALL) { badge ->
                val isUnlocked = currentStars >= badge.requiredStars
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUnlocked) Color.White else Color(0xFFF1F5F9)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 2.dp else 0.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(if (isUnlocked) Color(0xFFFEF08A) else Color(0xFFE2E8F0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isUnlocked) badge.emoji else "🔒",
                                fontSize = 24.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (strings.isAr) badge.titleAr else badge.titleEn,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isUnlocked) Color(0xFF0F172A) else Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = if (strings.isAr) badge.descAr else badge.descEn,
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isUnlocked) Color(0xFF475569) else Color(0xFF94A3B8)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isUnlocked) Color(0xFFDCFCE7) else Color(0xFFE2E8F0)
                        ) {
                            Text(
                                text = if (isUnlocked)
                                    (if (strings.isAr) "مكتمل ✔" else "Unlocked")
                                else
                                    "⭐ ${MathFormatter.format(badge.requiredStars, uiState.numeralFormat)}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isUnlocked) Color(0xFF166534) else Color(0xFF475569),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
