package com.yopox.crystals

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Camera

/**
 * Derives from [InputProcessor].
 * Uses [camera] to get x and y pointer coordinates.
 * Only [inputUp] and [inputDown] need to be overridden.
 */
interface InputScreen : InputProcessor {

    val camera: Camera
    var blockInput: Boolean

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (pointer > 0 || button > 0 || blockInput) return false
        val (x, y) = Util.unproject(camera, screenX, screenY)
        inputUp(x, y)
        return true
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (pointer > 0 || button > 0 || blockInput) return false
        val (x, y) = Util.unproject(camera, screenX, screenY)
        inputDown(x, y)
        return true
    }

    fun inputUp(x: Int, y: Int)

    fun inputDown(x: Int, y: Int)

    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false

    override fun keyTyped(character: Char): Boolean = false

    override fun scrolled(amount: Int): Boolean = false

    override fun keyUp(keycode: Int): Boolean = false

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean = false

    override fun keyDown(keycode: Int): Boolean = false

}