package com.yopox.crystals.screens

import com.badlogic.gdx.Gdx
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
import com.yopox.crystals.logic.Item
import com.yopox.crystals.logic.Progress
import com.yopox.crystals.ui.*
import ktx.app.KtxScreen
import ktx.graphics.use

class Inn(private val game: Crystals) : KtxScreen, InputScreen {

    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    override val camera = OrthographicCamera().also { it.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f) }
    override var blockInput = true
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)

    private var state = ScreenState.TRANSITION_OP
    private val icons = ArrayList<Icon>()
    private val buttons = ArrayList<Button>()
    private var slept = false
    private var transition = false
    private var frame = 0
    private var stats = MutableList(2) { "" }
    private var statsX = MutableList(2) { 0f }
    private var reveal = false

    private var treasureTransition = false
    private var treasureFrame = 0
    private var treasure: Item? = null
    private var nextTreasure: Item? = null

    init {
        buttons.add(TextButton(10f, 7f, Util.TEXT_SLEEP) { sleep() })
        buttons.add(TextButton(10f + Util.BUTTON_WIDTH + 4, 7f, Util.TEXT_CONTINUE) {
            state = ScreenState.TRANSITION_EN
            blockInput = true
        })
    }

    fun setup(event: Event) {
        val iconH = 7f * 2 + Util.BUTTON_HEIGHT - 2
        // Add furniture
        icons.add(Icon(Icons.ID.BED, Pair(20f, iconH)))

        icons.add(Tile.genInnTile(Pair(64f, iconH)) { clickTile(0) })
        icons.add(Tile.genInnTile(Pair(64f + 16 + 2, iconH)) { clickTile(1) })

        icons.add(Icon(Icons.ID.POTION, Pair(52f, 53f)).apply { hide() })

        // Set stats text
        setStats()
    }

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        // Draw the treasure
        if (treasure != null) {
            Util.drawRect(shapeRenderer, 49f, 49f, 100f, 24f)
            batch.use {
                Util.font.draw(it, "Obtained :", 49f, 82f)
                Util.font.draw(it, treasure?.name, 49f + 23, 69f)
                Util.font.draw(it, treasure?.description, 49f + 23, 59f)
            }
            icons.last().draw(shapeRenderer, batch)
        }

        // Draw characters
        icons.forEach { icon -> icon.draw(shapeRenderer, batch) }

        // Draw the title
        batch.use {
            Util.bigFont.draw(it, Util.TEXT_INN, 10f, Util.HEIGHT - Util.TITLE_OFFSET)
            Util.font.draw(it, stats[0], statsX[0], 23f)
            Util.font.draw(it, stats[1], statsX[1], 13f)
        }

        // Draw buttons
        buttons.forEach { it.draw(shapeRenderer, batch) }


        // Draw transitions
        if (transition) {
            if (frame <= Transition.TRANSITION_TIME) {
                Transition.drawTransition(shapeRenderer,
                        10 + Util.BUTTON_WIDTH * 2 + 8, 7f, Util.WIDTH - 10 * 2 - Util.BUTTON_WIDTH * 2 - 4, 20f, frame)
                {
                    Progress.player.stats.hp = Progress.player.baseStats.hp
                    Progress.player.stats.mp = Progress.player.baseStats.mp
                    buttons[0].clickable = false
                    setStats()
                }
                frame++
            } else {
                transition = false
                blockInput = false
                frame = 0
            }
        }

        if (treasureTransition) {
            if (treasureFrame <= Transition.TRANSITION_TIME) {
                Transition.drawTransition(shapeRenderer,
                        49f, 49f, 100f, 33f, treasureFrame)
                {
                    treasure = nextTreasure
                    icons.last().apply {
                        setIcon(treasure?.icon)
                        show()
                    }
                }
                treasureFrame++
            } else {
                treasureTransition = false
                treasureFrame = 0
                blockInput = false
            }
        }

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

    private fun setStats() {
        val hero = Progress.player
        stats[0] = "HP ${hero.stats.hp}/${hero.baseStats.hp}"
        stats[1] = "MP ${hero.stats.mp}/${hero.baseStats.mp}"

        val x = 10 + Util.BUTTON_WIDTH * 2 + 8
        val remaining = Util.WIDTH - 10 * 2 - Util.BUTTON_WIDTH * 2 - 8
        statsX[0] = x + Util.textOffsetX(Util.font, stats[0], remaining)
        statsX[1] = x + Util.textOffsetX(Util.font, stats[1], remaining)
    }

    private fun sleep() {
        slept = true
        buttons[0].clickable = false
        transition = true
        blockInput = true
    }

    private fun clickTile(i: Int) {
        val tile = icons[i+1] as Tile
        if (!tile.firstTouched && (tile.treasure != null || tile.gold > 0)) {
            if (tile.id in Icons.changingIcons.keys) tile.setIcon(Icons.changingIcons[tile.id])
            tile.firstTouched = true
            blockInput = true
            treasureTransition = true
            nextTreasure = tile.treasure
        }
    }

    override fun inputUp(x: Int, y: Int) {
        icons.forEach { it.lift(x, y) }
        buttons.forEach { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        icons.forEach { it.touch(x, y) }
        buttons.forEach { it.touch(x, y) }
    }

    private fun resetState() {
        icons.clear()
        buttons[0].clickable = true
        slept = false
        state = ScreenState.TRANSITION_OP
        treasure = null
        treasureTransition = false
        treasureFrame = 0
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