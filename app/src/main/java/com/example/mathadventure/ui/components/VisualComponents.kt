package com.example.mathadventure.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mathadventure.localization.MathFormatter
import com.example.mathadventure.localization.NumeralFormat

@Composable
fun RobotMascotView(
    message: String,
    modifier: Modifier = Modifier,
    companionEmoji: String = "🤖",
    mood: String = "happy"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "robot_anim")
    val bounce by infiniteTransition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .rotate(bounce)
                    .background(
                        brush = Brush.radialGradient(
                            listOf(Color(0xFF67E8F9), Color(0xFF0284C7))
                        ),
                        shape = CircleShape
                    )
                    .border(3.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = companionEmoji,
                    fontSize = 32.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VisualArrayView(
    rows: Int,
    columns: Int,
    itemEmoji: String = "🍎",
    modifier: Modifier = Modifier
) {
    val clampedRows = rows.coerceIn(1, 10)
    val clampedCols = columns.coerceIn(1, 10)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF1F5F9))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (r in 0 until clampedRows) {
            Row(
                modifier = Modifier.padding(vertical = 2.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                for (c in 0 until clampedCols) {
                    var isSelected by remember { mutableStateOf(false) }
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) Color(0xFFFEF08A) else Color.White)
                            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(8.dp))
                            .clickable { isSelected = !isSelected },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = itemEmoji, fontSize = 20.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun NumberLineView(
    jumpCount: Int,
    stepSize: Int,
    isBackward: Boolean = false,
    format: NumeralFormat = NumeralFormat.WESTERN,
    modifier: Modifier = Modifier
) {
    val total = jumpCount * stepSize
    val maxVal = (total + stepSize).coerceAtLeast(10)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF8FAFC))
            .padding(16.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
        ) {
            val width = size.width
            val height = size.height
            val lineY = height - 25f

            // Baseline
            drawLine(
                color = Color(0xFF64748B),
                start = Offset(20f, lineY),
                end = Offset(width - 20f, lineY),
                strokeWidth = 4f
            )

            val effectiveWidth = width - 80f
            val stepPx = effectiveWidth / maxVal.toFloat()

            // Draw ticks
            for (i in 0..maxVal step stepSize.coerceAtLeast(1)) {
                val x = 40f + (i * stepPx)
                drawLine(
                    color = Color(0xFF94A3B8),
                    start = Offset(x, lineY - 8f),
                    end = Offset(x, lineY + 8f),
                    strokeWidth = 3f
                )
            }

            // Draw Jumps
            val path = Path()
            for (j in 0 until jumpCount) {
                val startVal = if (!isBackward) j * stepSize else total - (j * stepSize)
                val endVal = if (!isBackward) (j + 1) * stepSize else total - ((j + 1) * stepSize)

                val startX = 40f + (startVal * stepPx)
                val endX = 40f + (endVal * stepPx)
                val midX = (startX + endX) / 2f
                val controlY = lineY - 55f

                path.reset()
                path.moveTo(startX, lineY)
                path.quadraticTo(midX, controlY, endX, lineY)

                drawPath(
                    path = path,
                    color = if (!isBackward) Color(0xFF2563EB) else Color(0xFFDC2626),
                    style = Stroke(width = 4f)
                )

                // Draw arrow/dot
                drawCircle(
                    color = if (!isBackward) Color(0xFF1D4ED8) else Color(0xFFB91C1C),
                    radius = 5f,
                    center = Offset(endX, lineY)
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = MathFormatter.format("0", format),
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF64748B)
            )
            Text(
                text = if (!isBackward)
                    "${MathFormatter.format(jumpCount, format)} × ${MathFormatter.format(stepSize, format)} = ${MathFormatter.format(total, format)}"
                else
                    "${MathFormatter.format(total, format)} ÷ ${MathFormatter.format(stepSize, format)} = ${MathFormatter.format(jumpCount, format)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = MathFormatter.format(maxVal, format),
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF64748B)
            )
        }
    }
}

@Composable
fun NumericKeypad(
    onDigitClick: (String) -> Unit,
    onBackspace: () -> Unit,
    onSubmit: () -> Unit,
    format: NumeralFormat = NumeralFormat.WESTERN,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val rows = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("⌫", "0", "✔")
        )

        for (row in rows) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (key in row) {
                    val isAction = key == "⌫" || key == "✔"
                    val bgColor = when (key) {
                        "✔" -> Color(0xFF10B981)
                        "⌫" -> Color(0xFFEF4444)
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                    val textColor = when (key) {
                        "✔", "⌫" -> Color.White
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .testTag("keypad_btn_$key")
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                when (key) {
                                    "⌫" -> onBackspace()
                                    "✔" -> onSubmit()
                                    else -> onDigitClick(key)
                                }
                            },
                        color = bgColor,
                        shape = RoundedCornerShape(14.dp),
                        tonalElevation = 2.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            val displayText = if (key in "0".."9") MathFormatter.format(key, format) else key
                            Text(
                                text = displayText,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        }
                    }
                }
            }
        }
    }
}
