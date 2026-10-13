package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.model.VisualItemType
import kotlinx.coroutines.launch

@Composable
fun VisualItemRenderer(
    itemType: VisualItemType,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    tapIndex: Int? = null,
    onItemTapped: (() -> Unit)? = null
) {
    val scaleAnim = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(size)
            .scale(scaleAnim.value)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = onItemTapped != null
            ) {
                scope.launch {
                    scaleAnim.animateTo(1.28f, spring(stiffness = Spring.StiffnessHigh))
                    scaleAnim.animateTo(1.0f, spring(dampingRatio = Spring.DampingRatioMediumBouncy))
                }
                onItemTapped?.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height

            when (itemType) {
                // World 1 - Forest
                VisualItemType.ACORN -> drawAcorn(w, h)
                VisualItemType.MUSHROOM -> drawMushroom(w, h)
                VisualItemType.LEAF -> drawLeaf(w, h)
                VisualItemType.PINE_CONE -> drawPineCone(w, h)
                VisualItemType.WOOD_LOG -> drawWoodLog(w, h)
                VisualItemType.FOREST_BERRY -> drawBerry(w, h)

                // World 2 - Fruits
                VisualItemType.APPLE -> drawApple(w, h)
                VisualItemType.BANANA -> drawBanana(w, h)
                VisualItemType.ORANGE -> drawOrange(w, h)
                VisualItemType.STRAWBERRY -> drawStrawberry(w, h)
                VisualItemType.WATERMELON -> drawWatermelon(w, h)
                VisualItemType.GRAPES -> drawGrapes(w, h)
                VisualItemType.MANGO -> drawMango(w, h)

                // World 3 - Vegetables
                VisualItemType.CARROT -> drawCarrot(w, h)
                VisualItemType.TOMATO -> drawTomato(w, h)
                VisualItemType.CORN -> drawCorn(w, h)
                VisualItemType.BROCCOLI -> drawBroccoli(w, h)
                VisualItemType.EGGPLANT -> drawEggplant(w, h)
                VisualItemType.POTATO -> drawPotato(w, h)

                // World 4 - Animals
                VisualItemType.BUNNY -> drawBunny(w, h)
                VisualItemType.ELEPHANT -> drawElephant(w, h)
                VisualItemType.BIRD -> drawBird(w, h)
                VisualItemType.FISH -> drawFish(w, h)
                VisualItemType.DUCK -> drawDuck(w, h)
                VisualItemType.TURTLE -> drawTurtle(w, h)
                VisualItemType.BUTTERFLY -> drawButterfly(w, h)
                VisualItemType.CAT -> drawCat(w, h)
                VisualItemType.CHICK -> drawChick(w, h)
                VisualItemType.MONKEY -> drawMonkey(w, h)

                // World 5 - Magic Plants
                VisualItemType.SUNFLOWER -> drawSunflower(w, h)
                VisualItemType.ROSE -> drawRose(w, h)
                VisualItemType.SPROUT -> drawSprout(w, h)
                VisualItemType.TREE -> drawTree(w, h)
                VisualItemType.CLOVER -> drawClover(w, h)
                VisualItemType.LEAF_MONSTERA -> drawMonstera(w, h)
            }
        }

        // Tap index bubble (e.g., child counted "1", "2", "3")
        if (tapIndex != null && tapIndex > 0) {
            Surface(
                shape = CircleShape,
                color = Color(0xFFFF5722),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 4.dp, y = (-4).dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$tapIndex",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}

// ---------------- Drawing Helpers for 2D Cartoon Visual Items ----------------

private fun DrawScope.drawApple(w: Float, h: Float) {
    // Apple body
    val bodyPath = Path().apply {
        moveTo(w * 0.5f, h * 0.3f)
        cubicTo(w * 0.15f, h * 0.15f, w * 0.05f, h * 0.75f, w * 0.5f, h * 0.92f)
        cubicTo(w * 0.95f, h * 0.75f, w * 0.85f, h * 0.15f, w * 0.5f, h * 0.3f)
    }
    drawPath(bodyPath, Brush.radialGradient(listOf(Color(0xFFFF5252), Color(0xFFD32F2F)), center = Offset(w * 0.4f, h * 0.45f)))

    // Apple highlight shine
    drawCircle(Color(0x66FFFFFF), radius = w * 0.12f, center = Offset(w * 0.35f, h * 0.42f))

    // Stem
    val stemPath = Path().apply {
        moveTo(w * 0.5f, h * 0.3f)
        cubicTo(w * 0.52f, h * 0.15f, w * 0.58f, h * 0.12f, w * 0.62f, h * 0.1f)
    }
    drawPath(stemPath, Color(0xFF5D4037), style = Stroke(width = w * 0.08f))

    // Green leaf
    val leafPath = Path().apply {
        moveTo(w * 0.54f, h * 0.22f)
        cubicTo(w * 0.75f, h * 0.12f, w * 0.82f, h * 0.25f, w * 0.54f, h * 0.22f)
    }
    drawPath(leafPath, Color(0xFF4CAF50))
}

private fun DrawScope.drawBanana(w: Float, h: Float) {
    val bananaPath = Path().apply {
        moveTo(w * 0.2f, h * 0.25f)
        cubicTo(w * 0.85f, h * 0.3f, w * 0.8f, h * 0.85f, w * 0.25f, h * 0.75f)
        cubicTo(w * 0.65f, h * 0.7f, w * 0.7f, h * 0.45f, w * 0.2f, h * 0.25f)
    }
    drawPath(bananaPath, Brush.verticalGradient(listOf(Color(0xFFFFEE58), Color(0xFFFBC02D))))
    // Tips
    drawCircle(Color(0xFF6D4C41), radius = w * 0.06f, center = Offset(w * 0.2f, h * 0.25f))
    drawCircle(Color(0xFF689F38), radius = w * 0.05f, center = Offset(w * 0.25f, h * 0.75f))
}

private fun DrawScope.drawOrange(w: Float, h: Float) {
    drawCircle(
        Brush.radialGradient(listOf(Color(0xFFFFB74D), Color(0xFFF57C00)), center = Offset(w * 0.4f, h * 0.45f)),
        radius = w * 0.4f,
        center = Offset(w * 0.5f, h * 0.55f)
    )
    // Leaf
    val leafPath = Path().apply {
        moveTo(w * 0.5f, h * 0.18f)
        cubicTo(w * 0.75f, h * 0.08f, w * 0.8f, h * 0.22f, w * 0.5f, h * 0.18f)
    }
    drawPath(leafPath, Color(0xFF43A047))
}

private fun DrawScope.drawStrawberry(w: Float, h: Float) {
    val berryPath = Path().apply {
        moveTo(w * 0.5f, h * 0.9f)
        cubicTo(w * 0.1f, h * 0.65f, w * 0.15f, h * 0.35f, w * 0.5f, h * 0.32f)
        cubicTo(w * 0.85f, h * 0.35f, w * 0.9f, h * 0.65f, w * 0.5f, h * 0.9f)
    }
    drawPath(berryPath, Brush.radialGradient(listOf(Color(0xFFFF5252), Color(0xFFC62828)), center = Offset(w * 0.4f, h * 0.5f)))

    // Seeds
    val seedOffsets = listOf(
        Offset(w * 0.35f, h * 0.5f),
        Offset(w * 0.6f, h * 0.52f),
        Offset(w * 0.48f, h * 0.68f),
        Offset(w * 0.38f, h * 0.75f),
        Offset(w * 0.55f, h * 0.4f)
    )
    seedOffsets.forEach { pos ->
        drawCircle(Color(0xFFFFEB3B), radius = w * 0.035f, center = pos)
    }

    // Green crown
    val crownPath = Path().apply {
        moveTo(w * 0.3f, h * 0.32f)
        lineTo(w * 0.5f, h * 0.15f)
        lineTo(w * 0.7f, h * 0.32f)
        lineTo(w * 0.5f, h * 0.25f)
        close()
    }
    drawPath(crownPath, Color(0xFF4CAF50))
}

private fun DrawScope.drawWatermelon(w: Float, h: Float) {
    // Watermelon slice
    val rindPath = Path().apply {
        moveTo(w * 0.1f, h * 0.45f)
        cubicTo(w * 0.25f, h * 0.92f, w * 0.75f, h * 0.92f, w * 0.9f, h * 0.45f)
        lineTo(w * 0.1f, h * 0.45f)
    }
    drawPath(rindPath, Color(0xFF2E7D32))

    val fleshPath = Path().apply {
        moveTo(w * 0.16f, h * 0.45f)
        cubicTo(w * 0.28f, h * 0.84f, w * 0.72f, h * 0.84f, w * 0.84f, h * 0.45f)
        close()
    }
    drawPath(fleshPath, Color(0xFFFF5252))

    // Black seeds
    drawCircle(Color(0xFF212121), radius = w * 0.03f, center = Offset(w * 0.35f, h * 0.58f))
    drawCircle(Color(0xFF212121), radius = w * 0.03f, center = Offset(w * 0.5f, h * 0.65f))
    drawCircle(Color(0xFF212121), radius = w * 0.03f, center = Offset(w * 0.65f, h * 0.58f))
}

private fun DrawScope.drawGrapes(w: Float, h: Float) {
    val grapeColor = Color(0xFF8E24AA)
    val centers = listOf(
        Offset(w * 0.4f, h * 0.4f), Offset(w * 0.6f, h * 0.4f),
        Offset(w * 0.3f, h * 0.55f), Offset(w * 0.5f, h * 0.55f), Offset(w * 0.7f, h * 0.55f),
        Offset(w * 0.4f, h * 0.7f), Offset(w * 0.6f, h * 0.7f),
        Offset(w * 0.5f, h * 0.82f)
    )
    centers.forEach { center ->
        drawCircle(grapeColor, radius = w * 0.13f, center = center)
        drawCircle(Color(0x44FFFFFF), radius = w * 0.05f, center = Offset(center.x - w * 0.04f, center.y - w * 0.04f))
    }
    // Stem
    drawLine(Color(0xFF5D4037), start = Offset(w * 0.5f, h * 0.35f), end = Offset(w * 0.5f, h * 0.15f), strokeWidth = w * 0.06f)
}

private fun DrawScope.drawMango(w: Float, h: Float) {
    val mangoPath = Path().apply {
        moveTo(w * 0.4f, h * 0.2f)
        cubicTo(w * 0.95f, h * 0.25f, w * 0.85f, h * 0.85f, w * 0.35f, h * 0.8f)
        cubicTo(w * 0.05f, h * 0.65f, w * 0.1f, h * 0.3f, w * 0.4f, h * 0.2f)
    }
    drawPath(mangoPath, Brush.linearGradient(listOf(Color(0xFFFFB300), Color(0xFFFF7043))))
}

private fun DrawScope.drawCarrot(w: Float, h: Float) {
    // Carrot cone
    val carrotPath = Path().apply {
        moveTo(w * 0.35f, h * 0.3f)
        cubicTo(w * 0.4f, h * 0.25f, w * 0.6f, h * 0.25f, w * 0.65f, h * 0.3f)
        lineTo(w * 0.53f, h * 0.92f)
        lineTo(w * 0.47f, h * 0.92f)
        close()
    }
    drawPath(carrotPath, Brush.verticalGradient(listOf(Color(0xFFFF9800), Color(0xFFF57C00))))

    // Green leafy top
    val topPath = Path().apply {
        moveTo(w * 0.5f, h * 0.26f)
        lineTo(w * 0.35f, h * 0.1f)
        lineTo(w * 0.5f, h * 0.16f)
        lineTo(w * 0.5f, h * 0.06f)
        lineTo(w * 0.55f, h * 0.16f)
        lineTo(w * 0.68f, h * 0.1f)
        close()
    }
    drawPath(topPath, Color(0xFF4CAF50))
}

private fun DrawScope.drawTomato(w: Float, h: Float) {
    drawCircle(
        Brush.radialGradient(listOf(Color(0xFFFF5252), Color(0xFFD32F2F)), center = Offset(w * 0.45f, h * 0.48f)),
        radius = w * 0.38f,
        center = Offset(w * 0.5f, h * 0.56f)
    )
    drawCircle(Color(0x66FFFFFF), radius = w * 0.1f, center = Offset(w * 0.38f, h * 0.46f))
    // Sepals
    val sepals = Path().apply {
        moveTo(w * 0.5f, h * 0.2f)
        lineTo(w * 0.36f, h * 0.24f)
        lineTo(w * 0.5f, h * 0.28f)
        lineTo(w * 0.64f, h * 0.24f)
        close()
    }
    drawPath(sepals, Color(0xFF388E3C))
}

private fun DrawScope.drawCorn(w: Float, h: Float) {
    drawRoundRect(
        color = Color(0xFFFFD54F),
        topLeft = Offset(w * 0.36f, h * 0.25f),
        size = Size(w * 0.28f, h * 0.6f),
        cornerRadius = CornerRadius(w * 0.14f, w * 0.14f)
    )
    // Green husks
    val leftHusk = Path().apply {
        moveTo(w * 0.36f, h * 0.85f)
        cubicTo(w * 0.18f, h * 0.65f, w * 0.25f, h * 0.45f, w * 0.38f, h * 0.38f)
    }
    drawPath(leftHusk, Color(0xFF66BB6A), style = Stroke(width = w * 0.06f))
}

private fun DrawScope.drawBroccoli(w: Float, h: Float) {
    // Stem
    drawRoundRect(
        color = Color(0xFF81C784),
        topLeft = Offset(w * 0.42f, h * 0.55f),
        size = Size(w * 0.16f, h * 0.35f),
        cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
    )
    // Florets
    val floretColor = Color(0xFF2E7D32)
    drawCircle(floretColor, radius = w * 0.22f, center = Offset(w * 0.5f, h * 0.38f))
    drawCircle(floretColor, radius = w * 0.18f, center = Offset(w * 0.32f, h * 0.45f))
    drawCircle(floretColor, radius = w * 0.18f, center = Offset(w * 0.68f, h * 0.45f))
}

private fun DrawScope.drawEggplant(w: Float, h: Float) {
    val eggPath = Path().apply {
        moveTo(w * 0.5f, h * 0.25f)
        cubicTo(w * 0.85f, h * 0.4f, w * 0.8f, h * 0.88f, w * 0.5f, h * 0.9f)
        cubicTo(w * 0.2f, h * 0.88f, w * 0.25f, h * 0.4f, w * 0.5f, h * 0.25f)
    }
    drawPath(eggPath, Color(0xFF5E35B1))
    // Green cap
    val cap = Path().apply {
        moveTo(w * 0.5f, h * 0.15f)
        lineTo(w * 0.38f, h * 0.28f)
        lineTo(w * 0.5f, h * 0.24f)
        lineTo(w * 0.62f, h * 0.28f)
        close()
    }
    drawPath(cap, Color(0xFF43A047))
}

private fun DrawScope.drawPotato(w: Float, h: Float) {
    drawRoundRect(
        color = Color(0xFFBCAAA4),
        topLeft = Offset(w * 0.2f, h * 0.3f),
        size = Size(w * 0.6f, h * 0.45f),
        cornerRadius = CornerRadius(w * 0.25f, w * 0.2f)
    )
    // Freckles
    drawCircle(Color(0xFF8D6E63), radius = w * 0.03f, center = Offset(w * 0.38f, h * 0.45f))
    drawCircle(Color(0xFF8D6E63), radius = w * 0.035f, center = Offset(w * 0.6f, h * 0.55f))
}

// Forest items
private fun DrawScope.drawAcorn(w: Float, h: Float) {
    // Nut body
    val body = Path().apply {
        moveTo(w * 0.3f, h * 0.45f)
        cubicTo(w * 0.3f, h * 0.8f, w * 0.5f, h * 0.92f, w * 0.5f, h * 0.92f)
        cubicTo(w * 0.5f, h * 0.92f, w * 0.7f, h * 0.8f, w * 0.7f, h * 0.45f)
        close()
    }
    drawPath(body, Color(0xFF8D6E63))

    // Cap
    drawRoundRect(
        color = Color(0xFF5D4037),
        topLeft = Offset(w * 0.24f, h * 0.32f),
        size = Size(w * 0.52f, h * 0.22f),
        cornerRadius = CornerRadius(w * 0.12f, w * 0.12f)
    )
    // Stalk
    drawLine(Color(0xFF3E2723), start = Offset(w * 0.5f, h * 0.32f), end = Offset(w * 0.5f, h * 0.18f), strokeWidth = w * 0.06f)
}

private fun DrawScope.drawMushroom(w: Float, h: Float) {
    // Stem
    drawRoundRect(
        color = Color(0xFFFFF9C4),
        topLeft = Offset(w * 0.4f, h * 0.48f),
        size = Size(w * 0.2f, h * 0.42f),
        cornerRadius = CornerRadius(w * 0.08f, w * 0.08f)
    )
    // Red Cap
    val cap = Path().apply {
        moveTo(w * 0.15f, h * 0.55f)
        cubicTo(w * 0.2f, h * 0.15f, w * 0.8f, h * 0.15f, w * 0.85f, h * 0.55f)
        close()
    }
    drawPath(cap, Color(0xFFE53935))
    // Polka dots
    drawCircle(Color.White, radius = w * 0.06f, center = Offset(w * 0.4f, h * 0.36f))
    drawCircle(Color.White, radius = w * 0.05f, center = Offset(w * 0.6f, h * 0.34f))
    drawCircle(Color.White, radius = w * 0.045f, center = Offset(w * 0.5f, h * 0.45f))
}

private fun DrawScope.drawLeaf(w: Float, h: Float) {
    val leaf = Path().apply {
        moveTo(w * 0.2f, h * 0.8f)
        cubicTo(w * 0.15f, h * 0.35f, w * 0.65f, h * 0.15f, w * 0.85f, h * 0.2f)
        cubicTo(w * 0.85f, h * 0.65f, w * 0.55f, h * 0.85f, w * 0.2f, h * 0.8f)
    }
    drawPath(leaf, Brush.linearGradient(listOf(Color(0xFF66BB6A), Color(0xFF2E7D32))))
    drawLine(Color(0xFF1B5E20), start = Offset(w * 0.2f, h * 0.8f), end = Offset(w * 0.78f, h * 0.25f), strokeWidth = w * 0.04f)
}

private fun DrawScope.drawPineCone(w: Float, h: Float) {
    drawRoundRect(
        color = Color(0xFF6D4C41),
        topLeft = Offset(w * 0.3f, h * 0.25f),
        size = Size(w * 0.4f, h * 0.6f),
        cornerRadius = CornerRadius(w * 0.2f, w * 0.2f)
    )
    val scaleLines = listOf(h * 0.4f, h * 0.52f, h * 0.64f, h * 0.76f)
    scaleLines.forEach { y ->
        drawLine(Color(0xFF4E342E), start = Offset(w * 0.32f, y), end = Offset(w * 0.68f, y), strokeWidth = w * 0.04f)
    }
}

private fun DrawScope.drawWoodLog(w: Float, h: Float) {
    drawCircle(Color(0xFF8D6E63), radius = w * 0.35f, center = Offset(w * 0.5f, h * 0.5f))
    drawCircle(Color(0xFFBCAAA4), radius = w * 0.26f, center = Offset(w * 0.5f, h * 0.5f))
    drawCircle(Color(0xFF8D6E63), radius = w * 0.14f, center = Offset(w * 0.5f, h * 0.5f))
}

private fun DrawScope.drawBerry(w: Float, h: Float) {
    val berryColor = Color(0xFF3949AB)
    drawCircle(berryColor, radius = w * 0.18f, center = Offset(w * 0.4f, h * 0.55f))
    drawCircle(berryColor, radius = w * 0.18f, center = Offset(w * 0.62f, h * 0.55f))
    drawCircle(berryColor, radius = w * 0.16f, center = Offset(w * 0.5f, h * 0.4f))
    drawCircle(Color(0x55FFFFFF), radius = w * 0.06f, center = Offset(w * 0.46f, h * 0.36f))
}

// Animals
private fun DrawScope.drawBunny(w: Float, h: Float) {
    // Face
    drawCircle(Color(0xFFF5F5F5), radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.62f))
    // Ears
    drawRoundRect(Color(0xFFEEEEEE), topLeft = Offset(w * 0.32f, h * 0.15f), size = Size(w * 0.12f, h * 0.35f), cornerRadius = CornerRadius(w * 0.06f, w * 0.06f))
    drawRoundRect(Color(0xFFFF80AB), topLeft = Offset(w * 0.34f, h * 0.2f), size = Size(w * 0.08f, h * 0.26f), cornerRadius = CornerRadius(w * 0.04f, w * 0.04f))
    drawRoundRect(Color(0xFFEEEEEE), topLeft = Offset(w * 0.56f, h * 0.15f), size = Size(w * 0.12f, h * 0.35f), cornerRadius = CornerRadius(w * 0.06f, w * 0.06f))
    drawRoundRect(Color(0xFFFF80AB), topLeft = Offset(w * 0.58f, h * 0.2f), size = Size(w * 0.08f, h * 0.26f), cornerRadius = CornerRadius(w * 0.04f, w * 0.04f))
    // Eyes & Nose
    drawCircle(Color(0xFF212121), radius = w * 0.035f, center = Offset(w * 0.42f, h * 0.6f))
    drawCircle(Color(0xFF212121), radius = w * 0.035f, center = Offset(w * 0.58f, h * 0.6f))
    drawCircle(Color(0xFFFF4081), radius = w * 0.035f, center = Offset(w * 0.5f, h * 0.68f))
}

private fun DrawScope.drawElephant(w: Float, h: Float) {
    val gray = Color(0xFF90A4AE)
    drawCircle(gray, radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.5f))
    // Big Ears
    drawCircle(Color(0xFFB0BEC5), radius = w * 0.2f, center = Offset(w * 0.25f, h * 0.48f))
    drawCircle(Color(0xFFB0BEC5), radius = w * 0.2f, center = Offset(w * 0.75f, h * 0.48f))
    // Eyes
    drawCircle(Color.Black, radius = w * 0.03f, center = Offset(w * 0.43f, h * 0.45f))
    drawCircle(Color.Black, radius = w * 0.03f, center = Offset(w * 0.57f, h * 0.45f))
    // Trunk
    val trunk = Path().apply {
        moveTo(w * 0.47f, h * 0.56f)
        cubicTo(w * 0.48f, h * 0.75f, w * 0.6f, h * 0.78f, w * 0.65f, h * 0.7f)
    }
    drawPath(trunk, gray, style = Stroke(width = w * 0.1f))
}

private fun DrawScope.drawBird(w: Float, h: Float) {
    drawCircle(Color(0xFF42A5F5), radius = w * 0.26f, center = Offset(w * 0.48f, h * 0.52f))
    // Beak
    val beak = Path().apply {
        moveTo(w * 0.68f, h * 0.5f)
        lineTo(w * 0.88f, h * 0.54f)
        lineTo(w * 0.68f, h * 0.58f)
        close()
    }
    drawPath(beak, Color(0xFFFFB300))
    // Eye
    drawCircle(Color.White, radius = w * 0.06f, center = Offset(w * 0.56f, h * 0.46f))
    drawCircle(Color.Black, radius = w * 0.03f, center = Offset(w * 0.58f, h * 0.46f))
}

private fun DrawScope.drawFish(w: Float, h: Float) {
    val fishColor = Color(0xFFFF7043)
    val body = Path().apply {
        moveTo(w * 0.2f, h * 0.5f)
        cubicTo(w * 0.4f, h * 0.2f, w * 0.75f, h * 0.35f, w * 0.85f, h * 0.5f)
        cubicTo(w * 0.75f, h * 0.65f, w * 0.4f, h * 0.8f, w * 0.2f, h * 0.5f)
    }
    drawPath(body, fishColor)
    // Tail fin
    val tail = Path().apply {
        moveTo(w * 0.25f, h * 0.5f)
        lineTo(w * 0.1f, h * 0.32f)
        lineTo(w * 0.1f, h * 0.68f)
        close()
    }
    drawPath(tail, Color(0xFFFFAB91))
    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.72f, h * 0.48f))
}

private fun DrawScope.drawDuck(w: Float, h: Float) {
    drawCircle(Color(0xFFFFEE58), radius = w * 0.28f, center = Offset(w * 0.45f, h * 0.55f))
    // Beak
    val beak = Path().apply {
        moveTo(w * 0.65f, h * 0.52f)
        lineTo(w * 0.85f, h * 0.55f)
        lineTo(w * 0.65f, h * 0.6f)
        close()
    }
    drawPath(beak, Color(0xFFFF9800))
    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.52f, h * 0.48f))
}

private fun DrawScope.drawTurtle(w: Float, h: Float) {
    // Shell
    drawCircle(Color(0xFF43A047), radius = w * 0.28f, center = Offset(w * 0.48f, h * 0.52f))
    drawCircle(Color(0xFF2E7D32), radius = w * 0.16f, center = Offset(w * 0.48f, h * 0.52f))
    // Head
    drawCircle(Color(0xFF81C784), radius = w * 0.12f, center = Offset(w * 0.78f, h * 0.52f))
    drawCircle(Color.Black, radius = w * 0.025f, center = Offset(w * 0.82f, h * 0.5f))
}

private fun DrawScope.drawButterfly(w: Float, h: Float) {
    val wingColor = Color(0xFFBA68C8)
    drawCircle(wingColor, radius = w * 0.18f, center = Offset(w * 0.32f, h * 0.4f))
    drawCircle(wingColor, radius = w * 0.18f, center = Offset(w * 0.68f, h * 0.4f))
    drawCircle(Color(0xFFCE93D8), radius = w * 0.14f, center = Offset(w * 0.35f, h * 0.65f))
    drawCircle(Color(0xFFCE93D8), radius = w * 0.14f, center = Offset(w * 0.65f, h * 0.65f))
    // Body
    drawRoundRect(Color(0xFF4A148C), topLeft = Offset(w * 0.46f, h * 0.32f), size = Size(w * 0.08f, h * 0.42f), cornerRadius = CornerRadius(w * 0.04f, w * 0.04f))
}

private fun DrawScope.drawCat(w: Float, h: Float) {
    drawCircle(Color(0xFFFFB74D), radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.58f))
    // Pointy ears
    val leftEar = Path().apply {
        moveTo(w * 0.28f, h * 0.48f)
        lineTo(w * 0.32f, h * 0.22f)
        lineTo(w * 0.45f, h * 0.38f)
        close()
    }
    drawPath(leftEar, Color(0xFFFFA726))
    val rightEar = Path().apply {
        moveTo(w * 0.72f, h * 0.48f)
        lineTo(w * 0.68f, h * 0.22f)
        lineTo(w * 0.55f, h * 0.38f)
        close()
    }
    drawPath(rightEar, Color(0xFFFFA726))
    // Eyes & Nose
    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.42f, h * 0.55f))
    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.58f, h * 0.55f))
    drawCircle(Color(0xFFE91E63), radius = w * 0.03f, center = Offset(w * 0.5f, h * 0.64f))
}

private fun DrawScope.drawChick(w: Float, h: Float) {
    drawCircle(Color(0xFFFFF176), radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.54f))
    // Beak
    val beak = Path().apply {
        moveTo(w * 0.46f, h * 0.56f)
        lineTo(w * 0.54f, h * 0.56f)
        lineTo(w * 0.5f, h * 0.64f)
        close()
    }
    drawPath(beak, Color(0xFFFF9800))
    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.42f, h * 0.48f))
    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.58f, h * 0.48f))
}

private fun DrawScope.drawMonkey(w: Float, h: Float) {
    drawCircle(Color(0xFF795548), radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.52f))
    // Round ears
    drawCircle(Color(0xFF8D6E63), radius = w * 0.12f, center = Offset(w * 0.24f, h * 0.52f))
    drawCircle(Color(0xFF8D6E63), radius = w * 0.12f, center = Offset(w * 0.76f, h * 0.52f))
    // Muzzle
    drawCircle(Color(0xFFFFCCBC), radius = w * 0.18f, center = Offset(w * 0.5f, h * 0.6f))
    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.43f, h * 0.48f))
    drawCircle(Color.Black, radius = w * 0.035f, center = Offset(w * 0.57f, h * 0.48f))
}

// Plants
private fun DrawScope.drawSunflower(w: Float, h: Float) {
    val petalColor = Color(0xFFFFD54F)
    for (i in 0..7) {
        val angle = i * (kotlin.math.PI.toFloat() / 4f)
        val cx = w * 0.5f + (w * 0.24f * kotlin.math.cos(angle))
        val cy = h * 0.5f + (h * 0.24f * kotlin.math.sin(angle))
        drawCircle(petalColor, radius = w * 0.12f, center = Offset(cx, cy))
    }
    drawCircle(Color(0xFF5D4037), radius = w * 0.2f, center = Offset(w * 0.5f, h * 0.5f))
}

private fun DrawScope.drawRose(w: Float, h: Float) {
    drawCircle(Color(0xFFE91E63), radius = w * 0.3f, center = Offset(w * 0.5f, h * 0.5f))
    drawCircle(Color(0xFFC2185B), radius = w * 0.2f, center = Offset(w * 0.5f, h * 0.5f))
    drawCircle(Color(0xFF880E4F), radius = w * 0.1f, center = Offset(w * 0.5f, h * 0.5f))
}

private fun DrawScope.drawSprout(w: Float, h: Float) {
    drawLine(Color(0xFF66BB6A), start = Offset(w * 0.5f, h * 0.85f), end = Offset(w * 0.5f, h * 0.45f), strokeWidth = w * 0.08f)
    val leftLeaf = Path().apply {
        moveTo(w * 0.5f, h * 0.45f)
        cubicTo(w * 0.2f, h * 0.35f, w * 0.25f, h * 0.25f, w * 0.5f, h * 0.45f)
    }
    drawPath(leftLeaf, Color(0xFF4CAF50))
    val rightLeaf = Path().apply {
        moveTo(w * 0.5f, h * 0.45f)
        cubicTo(w * 0.8f, h * 0.35f, w * 0.75f, h * 0.25f, w * 0.5f, h * 0.45f)
    }
    drawPath(rightLeaf, Color(0xFF4CAF50))
}

private fun DrawScope.drawTree(w: Float, h: Float) {
    drawRoundRect(Color(0xFF795548), topLeft = Offset(w * 0.42f, h * 0.55f), size = Size(w * 0.16f, h * 0.35f), cornerRadius = CornerRadius(w * 0.04f, w * 0.04f))
    drawCircle(Color(0xFF4CAF50), radius = w * 0.28f, center = Offset(w * 0.5f, h * 0.4f))
    drawCircle(Color(0xFF388E3C), radius = w * 0.2f, center = Offset(w * 0.38f, h * 0.42f))
}

private fun DrawScope.drawClover(w: Float, h: Float) {
    val leafColor = Color(0xFF43A047)
    drawCircle(leafColor, radius = w * 0.14f, center = Offset(w * 0.36f, h * 0.38f))
    drawCircle(leafColor, radius = w * 0.14f, center = Offset(w * 0.64f, h * 0.38f))
    drawCircle(leafColor, radius = w * 0.14f, center = Offset(w * 0.36f, h * 0.62f))
    drawCircle(leafColor, radius = w * 0.14f, center = Offset(w * 0.64f, h * 0.62f))
    drawLine(Color(0xFF2E7D32), start = Offset(w * 0.5f, h * 0.55f), end = Offset(w * 0.5f, h * 0.88f), strokeWidth = w * 0.06f)
}

private fun DrawScope.drawMonstera(w: Float, h: Float) {
    val monstera = Path().apply {
        moveTo(w * 0.5f, h * 0.85f)
        cubicTo(w * 0.1f, h * 0.6f, w * 0.15f, h * 0.2f, w * 0.5f, h * 0.15f)
        cubicTo(w * 0.85f, h * 0.2f, w * 0.9f, h * 0.6f, w * 0.5f, h * 0.85f)
    }
    drawPath(monstera, Color(0xFF1B5E20))
    // Cutouts
    drawCircle(Color.White, radius = w * 0.05f, center = Offset(w * 0.35f, h * 0.45f))
    drawCircle(Color.White, radius = w * 0.05f, center = Offset(w * 0.65f, h * 0.45f))
    drawCircle(Color.White, radius = w * 0.04f, center = Offset(w * 0.4f, h * 0.6f))
    drawCircle(Color.White, radius = w * 0.04f, center = Offset(w * 0.6f, h * 0.6f))
}
