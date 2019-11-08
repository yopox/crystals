package com.yopox.crystals.screens

import com.badlogic.gdx.Gdx
import com.yopox.crystals.Crystals
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.def.Icons
import com.yopox.crystals.logic.Progress
import com.yopox.crystals.logic.equipment.Sword
import com.yopox.crystals.ui.Icon
import com.yopox.crystals.ui.TextButton
import ktx.graphics.use
import kotlin.math.floor

class Bag(game: Crystals) : Screen(Util.TEXT_BAG, game) {

    private val icons = mutableListOf<Icon>()
    private var page = 0

    init {
        buttons.add(TextButton(100f, 5f, Util.TEXT_CONTINUE) {
            blockInput = true
            state = ScreenState.ENDING
        })
        repeat(12) { i ->
            icons.add(Icon(Icons.ID.DOT, Pair(25f + 19 * (i % 6), 43f - 19 * floor(i / 6f))) { select(i) })
        }
    }

    private fun select(i: Int) {
        Gdx.app.log("Bag", "Clicked on $i")
    }

    fun setup() {
        Progress.items.keys.forEachIndexed { i, item ->
            if (i < 12) {
                if (item is Sword) {
                    icons[i].setIcon(item.swordId)
                } else {
                    icons[i].setIcon(item.id)
                }
            }
        }
    }

    override fun render(delta: Float) {
        super.render(delta)

        icons.forEach { it.draw(shapeRenderer, batch) }
        batch.use {
            Util.font.draw(it, "Page ${page + 1}/1", 44f, 16f)
        }

        drawTransitions()
    }

    override fun resetState() {
        page = 0
        icons.forEach { it.setIcon(Icons.ID.DOT) }
    }

    override fun inputUp(x: Int, y: Int) {
        icons.forEach { it.lift(x, y) }
        buttons.forEach { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        icons.forEach { it.touch(x, y) }
        buttons.forEach { it.touch(x, y) }
    }


}