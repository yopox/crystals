package com.yopox.crystals.ui

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.yopox.crystals.Util
import ktx.graphics.use
import kotlin.math.min

object Transition {
    /**
     * Transition time (in frames).
     */
    const val TRANSITION_TIME = 30
    const val HALF_TT = TRANSITION_TIME / 2
    const val DISPLAY_LEN = 40
    private const val TRANSITION_STUN = 2
    private var TRANSITION_FRAME = 0

    /**
     * Maps [0; 1] to [0; 1] with out cubic easing.
     */
    fun easeOutQuad(t: Float): Float {
        return t * (2 - t)
    }

    /**
     * Draws a white wipe transition.
     *
     * @return Boolean if the transition is over
     */
    fun drawWipe(sR: ShapeRenderer, leftToRight: Boolean = true, reverse: Boolean = false): Boolean {
        var progress = min(TRANSITION_FRAME, TRANSITION_TIME)
        if (reverse) progress = TRANSITION_TIME - progress

        val width = easeOutQuad(progress.toFloat() / TRANSITION_TIME) * Util.WIDTH
        val offset = when (leftToRight) {
            true -> 0f
            else -> Util.WIDTH - width
        }

        sR.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.WHITE
            it.rect(offset, 0f, width, Util.HEIGHT)
        }

        if (TRANSITION_FRAME == TRANSITION_TIME + TRANSITION_STUN) {
            TRANSITION_FRAME = 0
            return true
        }

        TRANSITION_FRAME++
        return false
    }

    /**
     * Draws a [TRANSITION_TIME] frames wipe transition.
     */
    fun drawTransition(
            sR: ShapeRenderer,
            x0: Float,
            y0: Float,
            width: Float,
            height: Float,
            frame: Int,
            stopTime: Int = 0,
            onMid: () -> Unit = {}
    ) {
        var x = 0f
        var w = 0f

        // Update
        when {
            frame < HALF_TT -> {
                x = x0
                w = width * easeOutQuad(frame.toFloat() / HALF_TT)
            }
            frame < TRANSITION_TIME + stopTime -> {
                val f2 = if (frame < HALF_TT + stopTime) HALF_TT else frame - stopTime

                w = width * easeOutQuad((TRANSITION_TIME - f2).toFloat() / HALF_TT)
                x = x0 + width - w
            }
        }

        // Mid transition callback
        if (frame == HALF_TT) {
            onMid()
        }

        // Draw
        Util.drawFilledRect(sR, x, y0, w, height)
    }

    /**
     * Draws a [TRANSITION_TIME] frames black wipe transition.
     */
    fun drawInvertTransition(
            sR: ShapeRenderer,
            x0: Float,
            y0: Float,
            width: Float,
            height: Float,
            frame: Int,
            stopTime: Int = 0,
            onMid: () -> Unit = {}
    ) {
        var x = 0f
        var w = 0f

        // Update
        if (frame < HALF_TT) {
            w = width * (1 - easeOutQuad(frame.toFloat() / HALF_TT))
            x = x0 + width - w
        } else if (frame < TRANSITION_TIME + stopTime) {
            val f2 = if (frame < HALF_TT + stopTime) HALF_TT else frame - stopTime
            w = width * easeOutQuad((f2 - HALF_TT).toFloat() / HALF_TT)
            x = x0
        }

        // Mid transition callback
        if (frame == HALF_TT) {
            onMid()
        }

        // Draw
        Util.drawFilledRect(sR, x, y0, w, height, true)
    }
}