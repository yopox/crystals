package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.crystals.Crystals
import com.yopox.crystals.def.Actions
import ktx.graphics.use

/**
 * ActionIcon class.
 * Used during [Fight] to select an action or subaction.
 *
 * TODO: Merge with [Icon]
 */
class ActionIcon(var type: Actions.ID, pos: Pair<Float, Float>, onClick: () -> Unit) : Button(pos, true, onClick) {

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

    fun changeType(action: Actions.ID) {
        type = action
        x = getX()
    }

    private fun getX(): Int {
        return when (type) {
            Actions.ID.ATTACK -> 12 * 2
            Actions.ID.DEFENSE -> 12 * 1
            Actions.ID.ITEMS -> 12 * 0
            Actions.ID.W_MAGIC -> 12 * 3
            Actions.ID.D_MAGIC -> 12 * 10
            Actions.ID.MONK -> 12 * 17
            Actions.ID.WARRIOR -> 12 * 24
            Actions.ID.INVOKE -> 12 * 31
            Actions.ID.ROBBING -> 12 * 38
            Actions.ID.SONGS -> 12 * 45
            Actions.ID.GEOMANCY -> 12 * 52
            Actions.ID.RETURN -> 12 * 72
            Actions.ID.HEAL -> 12 * 4
            Actions.ID.HEAL2 -> 12 * 5
            Actions.ID.CURE -> 12 * 6
            Actions.ID.BARRIER -> 12 * 7
            Actions.ID.BEAMS -> 12 * 8
            Actions.ID.BALL -> 12 * 9
            Actions.ID.WIND -> 12 * 11
            Actions.ID.FIRE -> 12 * 12
            Actions.ID.WATER -> 12 * 13
            Actions.ID.POISON -> 12 * 14
            Actions.ID.LIGHTNING -> 12 * 15
            Actions.ID.ENERGY -> 12 * 16
            Actions.ID.MEDITATION -> 12 * 18
            Actions.ID.FOCUS -> 12 * 19
            Actions.ID.KICK -> 12 * 20
            Actions.ID.PUNCH -> 12 * 21
            Actions.ID.CHAINS -> 12 * 22
            Actions.ID.SHURIKEN -> 12 * 23
            Actions.ID.DOUBLE -> 12 * 25
            Actions.ID.MASSIVE -> 12 * 26
            Actions.ID.SHIELD -> 12 * 27
            Actions.ID.INSULT -> 12 * 28
            Actions.ID.STORM -> 12 * 29
            Actions.ID.JUMP -> 12 * 30
            Actions.ID.FAIRY -> 12 * 32
            Actions.ID.TAME -> 12 * 33
            Actions.ID.GOLEM -> 12 * 34
            Actions.ID.RAIKU -> 12 * 35
            Actions.ID.WENDIGO -> 12 * 36
            Actions.ID.DEAD_KING -> 12 * 37
            Actions.ID.DAGGER -> 12 * 39
            Actions.ID.ESCAPE -> 12 * 40
            Actions.ID.ROB -> 12 * 41
            Actions.ID.BURGLE -> 12 * 42
            Actions.ID.BOMB -> 12 * 43
            Actions.ID.BRIBE -> 12 * 44
            Actions.ID.SING -> 12 * 46
            Actions.ID.BEWITCH -> 12 * 47
            Actions.ID.HIDE -> 12 * 48
            Actions.ID.FIREWORKS -> 12 * 49
            Actions.ID.WINE -> 12 * 50
            Actions.ID.SAUSAGE -> 12 * 51
            Actions.ID.EARTHQUAKE -> 12 * 53
            Actions.ID.PREDICT -> 12 * 54
            Actions.ID.TORNADO -> 12 * 55
            Actions.ID.LOCKED -> 12 * 63
        }
    }
}