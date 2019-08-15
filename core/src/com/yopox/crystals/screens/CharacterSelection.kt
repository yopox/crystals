package com.yopox.crystals.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.yopox.crystals.*
import com.yopox.crystals.def.Jobs
import com.yopox.crystals.ui.Button
import com.yopox.crystals.ui.TextButton
import com.yopox.crystals.ui.Transition
import ktx.app.KtxScreen
import ktx.graphics.use

/**
 * Character selection screen
 *
 * TODO: Real nextJob / previousJob function
 * TODO: Change the state on click
 */
class CharacterSelection(private val game: Crystals) : KtxScreen, InputScreen {

    private val batch = SpriteBatch()
    override val camera = OrthographicCamera().also { it.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f) }
    override var blockInput = true
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)
    private val shapeRenderer = ShapeRenderer()

    private val buttons = ArrayList<Button>()
    private var job = Jobs.warrior
    private var nextJob = Jobs.warrior
    private var jobStats = job.statsDescription()
    private var state = ScreenState.TRANSITION_OP
    private var transition = false
    private var frame = 0

    init {
        // Buttons init
        val x = Util.WIDTH / 4 + 32f + 43
        val y = 16f

        buttons.add(TextButton(x, y, Util.TEXT_CONTINUE) {
            blockInput = true
            state = ScreenState.TRANSITION_EN
        })
        buttons.add(TextButton(x, y + 21, Util.TEXT_PREVIOUS) {
            nextJob = when (job.name) {
                Jobs.warrior.name -> Jobs.priest
                Jobs.priest.name -> Jobs.rogue
                else -> Jobs.warrior
            }
            transition = true
            blockInput = true
        })
        buttons.add(TextButton(x, y + 2 * 21, Util.TEXT_NEXT) {
            nextJob = when (job.name) {
                Jobs.warrior.name -> Jobs.rogue
                Jobs.rogue.name -> Jobs.priest
                else -> Jobs.warrior
            }
            transition = true
            blockInput = true
        })
    }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = this
    }

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        buttons.map { it.draw(shapeRenderer, batch) }
        drawClassDesc()

        drawClassTransition()

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

    private fun drawClassTransition() {
        if (transition) {
            frame++
            if (frame <= Transition.TRANSITION_TIME) {
                Transition.drawTransition(shapeRenderer, 8f, 8f, 96f, 16f, frame) {
                    job = nextJob
                    jobStats = job.statsDescription()
                }
                Transition.drawTransition(shapeRenderer, 8f, 29f, 96f, 32f, frame)
                Transition.drawTransition(shapeRenderer, 8f, Util.HEIGHT - Util.TITLE_OFFSET - Util.BIG_FONT_SIZE + 5f, 96f, 16f, frame)
            } else {
                frame = 0
                transition = false
                blockInput = false
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        camera.update()
        camera.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f)
    }

    override fun dispose() {
        batch.dispose()
    }

    private fun drawClassDesc() {
        Util.drawRect(shapeRenderer, 8f, 29f, 96f, 32f)

        val x0 = 12f
        val offset = 32f

        batch.use {
            Util.font.draw(it, job.desc, 12f, 56f)
            Util.font.draw(it, jobStats[0], x0, 24f)
            Util.font.draw(it, jobStats[1], x0 + offset, 24f)
            Util.font.draw(it, jobStats[2], x0 + 2 * offset, 24f)
            Util.bigFont.draw(it, job.name, 10f, Util.HEIGHT - Util.TITLE_OFFSET)
        }
    }

    override fun inputUp(x: Int, y: Int) {
        if (!blockInput) buttons.map { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        if (!blockInput) buttons.map { it.touch(x, y) }
    }

    private fun resetState() {
        job = Jobs.warrior
        nextJob = Jobs.warrior
        jobStats = job.statsDescription()
        transition = false
        state = ScreenState.TRANSITION_OP
        frame = 0
        blockInput = true
    }

}
