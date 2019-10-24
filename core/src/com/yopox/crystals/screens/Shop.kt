package com.yopox.crystals.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.yopox.crystals.Crystals
import com.yopox.crystals.InputScreen
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.def.Icons
import com.yopox.crystals.logic.Event
import com.yopox.crystals.logic.Progress
import com.yopox.crystals.ui.*
import ktx.app.KtxScreen
import ktx.graphics.use

class Shop(private val game: Crystals) : KtxScreen, InputScreen {

    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    override val camera = OrthographicCamera().also { it.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f) }
    override var blockInput = true
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)

    private var state = ScreenState.TRANSITION_OP
    private val icons = ArrayList<Icon>()
    private val items = ArrayList<Tile>()
    private val buttons = ArrayList<Button>()
    var goldX = 0f
    var selectedItem: Tile? = null

    init {
        buttons.add(TextButton(79f - 40, 5f, Util.TEXT_SELL) { sell() })
        buttons.add(TextButton(79f, 5f, Util.TEXT_BUY) { buy() })
        buttons.add(TextButton(79f + 40, 5f, Util.TEXT_LEAVE) {
            state = ScreenState.TRANSITION_EN
            blockInput = true
        })
    }

    private fun sell() {
        Gdx.app.log("Shop", "Sell")
    }

    private fun buy() {
        Gdx.app.log("Shop", "Buy")
    }

    fun setup(event: Event) {
        val benchX = 4f
        val benchY = 24f
        icons.add(Icon(Icons.ID.BENCH_LEFT, Pair(benchX, benchY)))
        icons.add(Icon(Icons.ID.BENCH_MIDDLE, Pair(benchX + 14, benchY)))
        icons.add(Icon(Icons.ID.BENCH_MIDDLE, Pair(benchX + 2 * 14, benchY)))
        icons.add(Icon(Icons.ID.BENCH_MIDDLE, Pair(benchX + 3 * 14, benchY)))
        icons.add(Icon(Icons.ID.BENCH_LEFT, Pair(benchX + 4 * 14, benchY)).apply { flip() })

        // Generate shop items
        val itemsNb = 3
        val spray = (16 * 5) / (itemsNb + 1)
        val itemX = benchX - 4

        items.add(Tile(Icons.ID.CRYSTAL, Pair(itemX + spray - 8, benchY + 17)) { selectedItem = items[0] })
        items.add(Tile(Icons.ID.SWORD, Pair(itemX + 2 * spray - 8, benchY + 17)) { selectedItem = items[1] })
        items.add(Tile(Icons.ID.BEER, Pair(itemX + 3 * spray - 8, benchY + 17)) { selectedItem = items[2] })

        goldX = Util.WIDTH - 7f - Util.textSize("${Progress.gold} GOLD", Util.font).first
    }

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        // Draw the title
        batch.use {
            Util.bigFont.draw(it, Util.TEXT_SHOP, 10f, Util.HEIGHT - Util.TITLE_OFFSET)
            Util.font.draw(it, "${Progress.gold} GOLD", goldX, 81f)
        }

        selectedItem?.let {
            Util.drawRect(shapeRenderer, it.pos.first - 1, it.pos.second - 1, 18f, 18f)
        }

        icons.forEach { it.draw(shapeRenderer, batch) }
        items.forEach { it.draw(shapeRenderer, batch) }
        buttons.forEach { it.draw(shapeRenderer, batch) }

        Util.drawRect(shapeRenderer, 79f, 25f, 76f, 46f)

        when (state) {
            ScreenState.TRANSITION_OP -> {
                if (Transition.drawWipe(shapeRenderer, false, reverse = true)) {
                    state = ScreenState.MAIN
                    blockInput = false
                }
            }
            ScreenState.TRANSITION_EN -> {
                if (Transition.drawWipe(shapeRenderer)) {
                    resetState()
                    game.setScreen<Trip>()
                }
            }
            else -> Unit
        }
    }

    private fun resetState() {
        icons.clear()
    }

    override fun inputUp(x: Int, y: Int) {
        buttons.forEach { it.lift(x, y) }
        items.forEach { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        buttons.forEach { it.touch(x, y) }
        items.forEach { it.touch(x, y) }
    }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = this
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        camera.update()
        camera.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f)
    }

    override fun dispose() {
        batch.dispose()
    }
}