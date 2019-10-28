package com.yopox.crystals.screens

import com.yopox.crystals.Crystals
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.logic.Event
import com.yopox.crystals.ui.Screen
import com.yopox.crystals.ui.TextButton
import com.yopox.crystals.ui.Tile

class Garden(game: Crystals) : Screen(Util.TEXT_GARDEN, game) {

    val icons = ArrayList<Tile>()

    init {
        buttons.add(TextButton(Util.WIDTH / 2 - Util.BUTTON_WIDTH / 2, 5f, Util.TEXT_CONTINUE) { state = ScreenState.ENDING; blockInput = true })
    }

    fun setup(event: Event) {
        repeat(5) { n ->
            icons.add(Tile.genGardenTile(Pair(36f + 18 * n, Util.HEIGHT / 2 - 12)))
        }
    }

    override fun render(delta: Float) {
        super.render(delta)

        icons.forEach { it.draw(shapeRenderer, batch) }

        drawTransitions()
    }

    override fun inputUp(x: Int, y: Int) {
        buttons.forEach { it.lift(x, y) }
        icons.forEach { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        buttons.forEach { it.touch(x, y) }
        icons.forEach { it.touch(x, y) }
    }

    override fun resetState() {
        icons.clear()
        state = ScreenState.OPENING
    }

}