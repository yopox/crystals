package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.crystals.Crystals
import ktx.graphics.use

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
    GEOMANCY,
    RETURN
}

/**
 * ActionIcon class.
 * Used during [Fight] to select an action or subaction.
 *
 * TODO: Merge with [Icon]
 */
class ActionIcon(private var type: ACTIONS, pos: Pair<Float, Float>, onClick: () -> Unit) : Button(pos, true, onClick) {

    companion object {
        val icons: Texture = Crystals.assetManager["Base_Attacks.png"]
        private const val SIZE = 12
    }

    private var x = getX()
    val y = 0
    override val size = Pair(SIZE.toFloat(), SIZE.toFloat())
    private var visible = true

    override fun draw(sR: ShapeRenderer, batch: SpriteBatch) = batch.use {
        if (visible) it.draw(icons, pos.first, pos.second, x, y, SIZE, SIZE)
    }

    fun changeType(action: ACTIONS) {
        type = action
        x = getX()
    }

    fun hide() {
        visible = false
    }

    fun show() {
        visible = true
    }

    private fun getX(): Int {
        return when (type) {
            ACTIONS.ATTACK -> 12 * 2
            ACTIONS.DEFENSE -> 12 * 1
            ACTIONS.ITEMS -> 12 * 0
            ACTIONS.W_MAGIC -> 12 * 3
            ACTIONS.D_MAGIC -> 12 * 10
            ACTIONS.MONK -> 12 * 17
            ACTIONS.WARRIOR -> 12 * 27
            ACTIONS.INVOKE -> 12 * 31
            ACTIONS.ROB -> 12 * 38
            ACTIONS.SING -> 12 * 45
            ACTIONS.GEOMANCY -> 12 * 52
            ACTIONS.RETURN -> 12 * 72
        }
    }
}