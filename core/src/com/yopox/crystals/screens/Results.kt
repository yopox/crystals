package com.yopox.crystals.screens

import com.yopox.crystals.Crystals
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.def.Icons
import com.yopox.crystals.logic.equipment.Item
import com.yopox.crystals.logic.Progress
import com.yopox.crystals.ui.Icon
import com.yopox.crystals.ui.TextButton
import com.yopox.crystals.ui.Tile
import com.yopox.crystals.ui.Transition
import ktx.graphics.use
import kotlin.math.max

class Results(game: Crystals) : Screen(Util.TEXT_RESULTS, game) {

    val hero: Icon
    val loot = ArrayList<Tile>()
    var progress = 0f
    var frame = 0
    var xpAmount = 0
    var addedXP = 0
    var xpX = 0f
    var transitionTime = 0

    var lootName = "LOOT"
    var coins = 0
    var coinsX = 0f

    var lootTransition = false
    var lootTransitionFrame = 0
    var lootTransitionTile: Tile? = null

    init {
        hero = Icon(Icons.ID.WARRIOR, Pair(22f, 43f))
        buttons.add(TextButton(79f + 36, 5f, Util.TEXT_CONTINUE) {
            if (frame == transitionTime) {
                state = ScreenState.ENDING
                blockInput = true
            }
        })
    }

    fun setup(xp: Int, loots: List<Item>, coins: Int) {
        xpAmount = xp
        this.coins = coins
        coinsX = Util.WIDTH - 49f - Util.textSize("$coins COINS", Util.font).first
        val p = xpAmount / Progress.XP_GAPS[Progress.player.level]
        val lastLevelXP = Progress.XP_LEVELS.getOrNull(Progress.player.level - 1) ?: 0
        progress = (Progress.player.xp - lastLevelXP).toFloat() / Progress.XP_GAPS[Progress.player.level - 1]
        progress = max(0f, progress)
        transitionTime = when {
            xp < 100 -> 60
            p < 0.2 -> 40
            p < 0.6 -> 100
            else -> 160
        }
        val spray = 100f / (loots.size + 1)
        val lootX = 10f
        for ((i, l) in loots.withIndex()) {
            loot.add(Tile(Icons.ID.UNKNOWN, Pair(lootX + (i + 1) * spray - 8, 8f)) { openLoot(loot[i]) }.apply { treasure = l })
        }
    }

    private fun openLoot(tile: Tile) {
        if (!tile.firstTouched) {
            tile.firstTouched = true
            blockInput = true
            lootTransition = true
            lootTransitionFrame = 0
            lootTransitionTile = tile
        } else {
            lootName = tile.treasure?.name ?: "LOOT"
        }
    }

    override fun render(delta: Float) {
        super.render(delta)

        hero.draw(shapeRenderer, batch)

        // XP bar
        if (state == ScreenState.MAIN && frame < transitionTime) {
            frame++

            // XP update
            val bonus = (xpAmount * Transition.easeOutQuad(frame / transitionTime.toFloat()) - addedXP).toInt()
            Progress.player.addXP(bonus)
            addedXP += bonus
            if (frame == transitionTime && addedXP != xpAmount) Progress.player.addXP(xpAmount - addedXP)

            // Progress bar update
            val lastLevelXP = Progress.XP_LEVELS.getOrNull(Progress.player.level - 1) ?: 0
            progress = (Progress.player.xp - lastLevelXP).toFloat() / Progress.XP_GAPS[Progress.player.level - 1]
            progress = max(progress, 0f)
        }
        val xp = "XP: ${Progress.player.xp}/${Progress.XP_LEVELS[Progress.player.level]}"
        xpX = Util.WIDTH - 20f - Util.textSize(xp, Util.font).first
        Util.drawRect(shapeRenderer, 44f, 44f, 96f, 6f)
        Util.drawFilledRect(shapeRenderer, 45f, 45f, 94f * progress, 4f)
        batch.use {
            Util.font.draw(it, "LVL ${Progress.player.level}", 44f, 58f)
            Util.font.draw(it, xp, xpX, 58f)
        }

        // Draw loot box
        Util.drawRect(shapeRenderer, 9f, 5f, 102f, 22f)
        loot.forEach { it.draw(shapeRenderer, batch) }
        batch.use {
            Util.font.draw(it, lootName, 9f, 35f)
            Util.font.draw(it, "$coins COINS", coinsX, 35f)
        }

        // Draw loot transition
        if (lootTransition) {
            if (lootTransitionFrame < Transition.TRANSITION_TIME) {
                Transition.drawTransition(shapeRenderer, lootTransitionTile!!.pos.first, lootTransitionTile!!.pos.second, 16f, 16f, lootTransitionFrame) {
                    lootTransitionTile?.apply { setIcon(treasure?.id ?: Icons.ID.UNKNOWN) }
                    lootName = lootTransitionTile?.treasure?.name ?: "LOOT"
                }
                lootTransitionFrame++
            } else {
                lootTransitionFrame = 0
                lootTransition = false
                blockInput = false
            }
        }

        drawTransitions()
    }

    override fun stateChange(st: ScreenState) = when (st) {
        ScreenState.ENDING -> {
            // Add revealed items
            loot.forEach { l ->
                l.treasure?.let {
                    Progress.addItem(it)
                }
            }
            // Add coins
            Progress.gold += coins
            // Next screen
            resetState(); game.setScreen<Trip>()
        }
        ScreenState.OPENING -> {
            state = ScreenState.MAIN; blockInput = false
        }
        else -> Unit
    }

    override fun resetState() {
        state = ScreenState.OPENING
        loot.clear()
        lootTransition = false
        progress = 0f
        frame = 0
        addedXP = 0
        lootName = "Loot"
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