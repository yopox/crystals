package com.yopox.crystals

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxScreen
import ktx.graphics.use

/**
 * Trip Screen.
 *
 * TODO: Real chunk generation
 */
class Trip : KtxScreen {
    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    private val camera = OrthographicCamera()
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)
    private val posX1 = Util.getPositionOffset(Util.font, Util.TEXT_BAG, 36)
    private val posX2 = Util.getPositionOffset(Util.font, Util.TEXT_OPTIONS, 36)
    private val icons: Texture = Crystals.assetManager["aseprite/icons.png"]

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        // Draw the title
        batch.use {
            Util.bigFont.draw(it, Util.TEXT_TRIP, 10f, Util.HEIGHT - 10)
        }

        drawStatus()
        drawChunks()
        drawButtons()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        camera.update()
        camera.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f)
    }

    override fun dispose() {
        batch.dispose()
    }

    private fun drawStatus() {
        Util.drawRect(shapeRenderer, 8f, 8f, Util.WIDTH / 4, Util.HEIGHT / 2)
    }

    private fun drawChunks() {
        Util.drawRect(shapeRenderer, Util.WIDTH / 4 + 16f, Util.HEIGHT / 3, 16f, 3 * 14 + 4f)
        Util.drawRect(shapeRenderer, Util.WIDTH / 4 + 16f + 20f, Util.HEIGHT / 3, 16f, 3 * 14 + 4f)
        Util.drawRect(shapeRenderer, Util.WIDTH / 4 + 16f + 2 * 20f, Util.HEIGHT / 3, 16f, 3 * 14 + 4f)
        Util.drawRect(shapeRenderer, Util.WIDTH / 4 + 16f + 3 * 20f, Util.HEIGHT / 3, 16f, 3 * 14 + 4f)
        Util.drawRect(shapeRenderer, Util.WIDTH / 4 + 16f + 4 * 20f, Util.HEIGHT / 3, 16f, 3 * 14 + 4f)

        batch.use {
            for (i in 0..4) {
                for (j in 0..2) {
                    it.draw(icons, Util.WIDTH / 4 + 16f + 20 * i + 1, Util.HEIGHT / 3 + 1 + 15 * j, 14 * Crystals.events[i][j], 0, 14, 14)
                }
            }
        }

        shapeRenderer.use(ShapeRenderer.ShapeType.Filled) {
            it.color = Color.WHITE
            for (i in 0..4) {
                for (j in 1..2) {
                    val x = Util.WIDTH / 4 + 16f + 20 * i + 1
                    val y = Util.HEIGHT / 3 + 15 * j
                    it.rect(x, y, 14f, 1f)
                }
            }
        }
    }

    private fun drawButtons() {
        Util.drawRect(shapeRenderer, Util.WIDTH / 4 + 24f, 8f, 36f, 16f)
        Util.drawRect(shapeRenderer, Util.WIDTH / 4 + 32f + 36f, 8f, 36f, 16f)

        batch.use {
            Util.font.draw(it, "Bag", Util.WIDTH / 4 + 24f + posX1, 19f)
            Util.font.draw(it, "Options", Util.WIDTH / 4 + 32f + 36f + posX2, 19f)
        }
    }

}