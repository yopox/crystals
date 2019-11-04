package com.yopox.crystals.screens

import com.yopox.crystals.Crystals
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.def.Icons
import com.yopox.crystals.logic.Item
import com.yopox.crystals.logic.Progress
import com.yopox.crystals.ui.*
import ktx.graphics.use

class Results(game: Crystals) : Screen(Util.TEXT_RESULTS, game) {

    val hero: Icon
    val loot = ArrayList<Tile>()
    var progress = 0f
    var frame = 0
    var xpAmount = 40
    var addedXP = 0
    var xpX = 0f
    var transitionTime = 60

    init {
        hero = Icon(Icons.ID.WARRIOR, Pair(22f, 43f))
        buttons.add(TextButton(79f + 40, 5f, Util.TEXT_CONTINUE) {
            state = ScreenState.ENDING
            blockInput = true
        })
        setup(2000, listOf())
    }

    fun setup(xp: Int, loot: List<Item>) {
        xpAmount = xp
        val p = xpAmount / Progress.XP_GAPS[Progress.player.level]
        transitionTime = when {
            xp < 100 -> 60
            p < 0.2 -> 40
            p < 0.6 -> 100
            else -> 160
        }
    }

    override fun render(delta: Float) {
        super.render(delta)

        hero.draw(shapeRenderer, batch)
        loot.forEach { it.draw(shapeRenderer, batch) }

        // XP bar
        if (frame < transitionTime) {
            frame++

            // XP update
            val bonus = (xpAmount * Transition.easeOutQuad(frame / transitionTime.toFloat()) - addedXP).toInt()
            Progress.player.addXP(bonus)
            addedXP += bonus
            if (frame == transitionTime && addedXP != xpAmount) Progress.player.addXP(xpAmount - addedXP)

            // Progress bar update
            val lastLevelXP = Progress.XP_LEVELS.getOrNull(Progress.player.level - 1) ?: 0
            progress = (Progress.player.xp - lastLevelXP).toFloat() / Progress.XP_GAPS[Progress.player.level - 1]
        }
        val xp = "XP: ${Progress.player.xp}/${Progress.XP_LEVELS[Progress.player.level]}"
        xpX = Util.WIDTH - 20f - Util.textSize(xp, Util.font).first
        Util.drawRect(shapeRenderer, 44f, 44f, 96f, 6f)
        Util.drawFilledRect(shapeRenderer, 45f, 45f, 94f * progress, 4f)
        batch.use {
            Util.font.draw(it, "LVL ${Progress.player.level}", 44f, 58f)
            Util.font.draw(it, xp, xpX, 58f)
        }

        drawTransitions()
    }

    override fun resetState() {

    }

    override fun inputUp(x: Int, y: Int) {
        buttons.forEach { it.lift(x, y) }
        loot.forEach { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        buttons.forEach { it.touch(x, y) }
        loot.forEach { it.touch(x, y) }
    }
}