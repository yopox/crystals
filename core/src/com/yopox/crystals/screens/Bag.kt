package com.yopox.crystals.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.yopox.crystals.Crystals
import com.yopox.crystals.Util
import com.yopox.crystals.logic.equipment.Sword
import com.yopox.crystals.ui.TextButton
import com.yopox.crystals.ui.Tile
import ktx.graphics.use

class Bag(game: Crystals) : Screen(Util.TEXT_BAG, game) {

    private val tiles = mutableListOf<Tile>()
    private val text = mutableListOf<Texture>()

    init {
        repeat(4) {
            text.add(Sword.genTexture(Sword.Combination()))
        }
        buttons.add(TextButton(8f, 8f, "Regen") {
            text.clear()
            repeat(4) {
                text.add(Sword.genTexture(Sword.Combination()))
            }
        })
        Gdx.app.log("bag", "${text[0].width}")
    }

    override fun render(delta: Float) {
        super.render(delta)

        batch.use {b ->
            repeat(4) {i ->
                b.draw(text[i], 32 + 20f * i, 45f)
            }
        }

        drawTransitions()
    }

    override fun resetState() {

    }

    override fun inputUp(x: Int, y: Int) {
        tiles.forEach { it.lift(x, y) }
        buttons.forEach { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        tiles.forEach { it.touch(x, y) }
        buttons.forEach { it.touch(x, y) }
    }


}