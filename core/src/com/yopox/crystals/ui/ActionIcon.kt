package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.crystals.Crystals
import com.yopox.crystals.Util
import com.yopox.crystals.def.Actions
import com.yopox.crystals.def.Actions.ID.*
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
        if (visible) when (clicked) {
            true -> Util.drawFilledRect(sR, pos.first, pos.second, size.first, size.second)
            else -> it.draw(icons, pos.first, pos.second, x, y, SIZE, SIZE)
        }
    }

    fun changeType(action: Actions.ID) {
        type = action
        x = getX()
    }

    private fun getX(): Int {
        return when (type) {
            ATTACK -> 12 * 2
            DEFENSE -> 12 * 1
            ITEMS -> 12 * 0
            W_MAGIC -> 12 * 3
            D_MAGIC -> 12 * 10
            MONK -> 12 * 17
            WARRIOR -> 12 * 24
            INVOKE -> 12 * 31
            ROBBING -> 12 * 38
            SONGS -> 12 * 45
            GEOMANCY -> 12 * 52
            RETURN -> 12 * 72
            HEAL -> 12 * 4
            HEAL2 -> 12 * 5
            CURE -> 12 * 6
            BARRIER -> 12 * 7
            BEAMS -> 12 * 8
            BALL -> 12 * 9
            WIND -> 12 * 11
            FIRE -> 12 * 12
            WATER -> 12 * 13
            POISON -> 12 * 14
            LIGHTNING -> 12 * 15
            ENERGY -> 12 * 16
            MEDITATION -> 12 * 18
            FOCUS -> 12 * 19
            KICK -> 12 * 20
            PUNCH -> 12 * 21
            CHAINS -> 12 * 22
            SHURIKEN -> 12 * 23
            DOUBLE -> 12 * 25
            MASSIVE -> 12 * 26
            SHIELD -> 12 * 27
            INSULT -> 12 * 28
            STORM -> 12 * 29
            JUMP -> 12 * 30
            FAIRY -> 12 * 32
            TAME -> 12 * 33
            GOLEM -> 12 * 34
            RAIKU -> 12 * 35
            WENDIGO -> 12 * 36
            DEAD_KING -> 12 * 37
            DAGGER -> 12 * 39
            ESCAPE -> 12 * 40
            ROB -> 12 * 41
            BURGLE -> 12 * 42
            BOMB -> 12 * 43
            BRIBE -> 12 * 44
            SING -> 12 * 46
            BEWITCH -> 12 * 47
            HIDE -> 12 * 48
            FIREWORKS -> 12 * 49
            WINE -> 12 * 50
            SAUSAGE -> 12 * 51
            EARTHQUAKE -> 12 * 53
            PREDICT -> 12 * 54
            TORNADO -> 12 * 55
            LOCKED -> 12 * 63
            WAIT -> 12 * 62
        }
    }
}