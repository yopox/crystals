package com.yopox.crystals

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxScreen

/**
 * Title Screen.
 *
 * TODO: Click to move to the next state
 * TODO: Blinking text
 */
class TitleScreen(assets: Assets) : KtxScreen {
    private val batch = SpriteBatch()
    private val titleFont = assets.titleFont
    private val font = assets.font
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)
    private var posX1: Float = 0.0f
    private var posX2: Float = 0.0f

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined

        batch.begin()
        titleFont.draw(batch, Util.TITLE, posX1 - Util.WIDTH / 2, Util.HEIGHT / 4)
        font.draw(batch, Util.NEWGAME, posX2 - Util.WIDTH / 2, -Util.HEIGHT / 8)
        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        camera.update()
        posX1 = Util.getPositionOffset(titleFont, Util.TITLE, Util.WIDTH.toInt())
        posX2 = Util.getPositionOffset(font, Util.NEWGAME, Util.WIDTH.toInt())
    }

    override fun dispose() {
        batch.dispose()
    }
}