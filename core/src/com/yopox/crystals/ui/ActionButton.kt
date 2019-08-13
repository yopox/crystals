package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.crystals.Crystals

enum class ACTIONS {
    ATTACK,
    DEFENSE,
    ITEMS,
    W_MAGIC,
    D_MAGIC,
    MONK,
    WARRIOR,
    INVOKE,
    ROB,
    SING,
    GEOMANCY
}

class ActionButton(type: ACTIONS, pos: Pair<Float, Float>, onClick: () -> Unit): Button(pos, true, onClick) {

    companion object {
        val icons: Texture = Crystals.assetManager["Base_Attacks.png"]
        private const val SIZE = 12
    }

    val x = when (type) {
        ACTIONS.ATTACK -> 12 * 2
        ACTIONS.DEFENSE -> 12
        ACTIONS.ITEMS -> 0
        ACTIONS.W_MAGIC -> 12 * 3
        ACTIONS.D_MAGIC -> 12 * 4
        ACTIONS.MONK -> 12 * 5
        ACTIONS.WARRIOR -> 12 * 6
        ACTIONS.INVOKE -> 12 * 7
        ACTIONS.ROB -> 12 * 8
        ACTIONS.SING -> 12 * 9
        ACTIONS.GEOMANCY -> 12 * 10
    }
    val y = 0

    override fun draw(sR: ShapeRenderer, batch: SpriteBatch) {
        batch.draw(icons, pos.first, pos.second, x, y, SIZE, SIZE)
    }

}