package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.crystals.Crystals
import ktx.graphics.use

enum class Actions {
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
    GEOMANCY,
    RETURN
}

/**
 * ActionIcon class.
 * Used during [Fight] to select an action or subaction.
 *
 * TODO: Merge with [Icon]
 */
class ActionIcon(private var type: Actions, pos: Pair<Float, Float>, onClick: () -> Unit) : Button(pos, true, onClick) {

    companion object {
        val icons: Texture = Crystals.assetManager["Base_Attacks.png"]
        private const val SIZE = 12
    }

    private var x = getX()
    val y = 0
    override val size = Pair(SIZE.toFloat(), SIZE.toFloat())

    override fun draw(sR: ShapeRenderer, batch: SpriteBatch) = batch.use {
        if (visible) it.draw(icons, pos.first, pos.second, x, y, SIZE, SIZE)
    }

    fun changeType(action: Actions) {
        type = action
        x = getX()
    }

    private fun getX(): Int {
        return when (type) {
            Actions.ATTACK -> 12 * 2
            Actions.DEFENSE -> 12 * 1
            Actions.ITEMS -> 12 * 0
            Actions.W_MAGIC -> 12 * 3
            Actions.D_MAGIC -> 12 * 10
            Actions.MONK -> 12 * 17
            Actions.WARRIOR -> 12 * 27
            Actions.INVOKE -> 12 * 31
            Actions.ROB -> 12 * 38
            Actions.SING -> 12 * 45
            Actions.GEOMANCY -> 12 * 52
            Actions.RETURN -> 12 * 72
        }
    }
}