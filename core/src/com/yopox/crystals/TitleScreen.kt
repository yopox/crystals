package com.yopox.crystals

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxScreen
import ktx.graphics.use

/**
 * Title Screen.
 *
 * TODO: Click to move to the next state
 * TODO: Blinking text
 */
class TitleScreen : KtxScreen {
    private val batch = SpriteBatch()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)
    private var posX1 = Util.textOffsetX(Util.bigFont, Util.TEXT_TITLE, Util.WIDTH.toInt())
    private var posX2 = Util.textOffsetX(Util.font, Util.TEXT_NEWGAME, Util.WIDTH.toInt())

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined

        batch.use {
            Util.bigFont.draw(it, Util.TEXT_TITLE, posX1 - Util.WIDTH / 2, Util.HEIGHT / 4)
            Util.font.draw(it, Util.TEXT_NEWGAME, posX2 - Util.WIDTH / 2, -Util.HEIGHT / 8)
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        camera.update()
    }

    override fun dispose() {
        batch.dispose()
    }
}