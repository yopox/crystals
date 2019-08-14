package com.yopox.crystals.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.yopox.crystals.Crystals
import com.yopox.crystals.InputScreen
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.data.Def
import com.yopox.crystals.data.EVENT_TYPE
import com.yopox.crystals.data.Event
import com.yopox.crystals.data.Progress
import com.yopox.crystals.ui.*
import ktx.app.KtxScreen
import ktx.graphics.use

/**
 * Fight Screen.
 */
class Fight(private val game: Crystals) : KtxScreen, InputScreen {

    private enum class State {
        MAIN,
        CHOOSE_ACTION,
        CHOOSE_SUBACTION,
        CHOOSE_TARGET
    }

    private enum class BlockType {
        TEXT,
        TRANSITION
    }

    private data class Block(val type: BlockType, var content: String)

    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    override val camera = OrthographicCamera().also { it.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f) }
    override var blockInput = true
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)

    private val buttons = ArrayList<Button>()
    private var state = ScreenState.TRANSITION_OP
    private var subState = State.MAIN
    private val icons = ArrayList<Icon>()

    private val blocks = ArrayList<Block>()
    private var currentBlock: Block? = null

    private object dialog {
        const val BASE_COOLDOWN = 4
        var line1 = ""
        var line2 = ""
        var cooldown = BASE_COOLDOWN
        var newLine = true
    }

    init {
        icons.add(Icon(Def.Icons.Priest, Pair(88f, 36f)))
        icons.add(Icon(Def.Icons.Snake, Pair(56f, 36f)))

        buttons.add(ActionButton(ACTIONS.ATTACK, Pair(10f, 5f)) { Gdx.app.log("Fight", "ATTACK") })
        buttons.add(ActionButton(ACTIONS.DEFENSE, Pair(26f, 5f)) {})
        buttons.add(ActionButton(ACTIONS.ITEMS, Pair(42f, 5f)) {})
        buttons.add(ActionButton(ACTIONS.W_MAGIC, Pair(58f, 5f)) {})
        buttons.add(ActionButton(ACTIONS.ROB, Pair(74f, 5f)) {})
        buttons.add(ActionButton(ACTIONS.GEOMANCY, Pair(90f, 5f)) {})
    }

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        update()

        // Draw the dialog box
        drawDialog()

        batch.use {
            // Draw the title
            Util.bigFont.draw(it, Util.TEXT_FIGHT, 10f, Util.HEIGHT - Util.TITLE_OFFSET)

            // Draw characters
            icons.map { icon -> icon.draw(shapeRenderer, it) }
        }

        when (state) {
            ScreenState.TRANSITION_OP -> {
                if (Util.drawWipe(shapeRenderer, false, reverse = true)) {
                    state = ScreenState.MAIN
                    blockInput = false
                }
            }
            ScreenState.TRANSITION_EN -> {
                if (Util.drawWipe(shapeRenderer)) {
                    resetState()
                    game.setScreen<Trip>()
                }
            }
            else -> Unit
        }

    }

    private fun update() {
        when (subState) {
            State.MAIN -> {
                if (currentBlock == null) {
                    if (blocks.size > 0) {
                        currentBlock = blocks.removeAt(0)
                    } else {
                        subState = State.CHOOSE_ACTION
                    }
                } else {
                    when (currentBlock!!.type) {
                        BlockType.TEXT -> {
                            if (dialog.newLine) {
                                dialog.line2 = dialog.line1
                                dialog.newLine = false
                            }
                            if (dialog.cooldown > 0) {
                                dialog.cooldown--
                            } else {
                                dialog.line1 += currentBlock!!.content[0]
                                currentBlock!!.content = currentBlock!!.content.substring(1)
                                dialog.cooldown = dialog.BASE_COOLDOWN
                            }
                            if (currentBlock!!.content == "") {
                                currentBlock = null
                                dialog.newLine = true
                            }
                        }
                        BlockType.TRANSITION -> {
                            subState = State.valueOf(currentBlock!!.content)
                            currentBlock = null
                        }
                    }
                }
            }
            State.CHOOSE_ACTION -> TODO()
            State.CHOOSE_SUBACTION -> TODO()
            State.CHOOSE_TARGET -> TODO()
        }
    }

    private fun drawDialog() {
        Util.drawRect(shapeRenderer, -1f, -1f, 182f, 24f)
        when (subState) {
            State.MAIN -> {
                batch.use {
                    Util.font.draw(it, dialog.line1, 10f, 19f)
                    Util.font.draw(it, dialog.line2, 10f, 10f)
                }
            }
            State.CHOOSE_ACTION -> {
                batch.use {
                    Util.font.draw(it, Util.TEXT_ACTIONS, 10f, 31f)
                }
                buttons.map { it.draw(shapeRenderer, batch) }
            }
            State.CHOOSE_SUBACTION -> TODO()
            State.CHOOSE_TARGET -> TODO()
        }
    }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = this
        blocks.add(Block(BlockType.TEXT, "Snake attacks!"))
        blocks.add(Block(BlockType.TRANSITION, "CHOOSE_ACTION"))
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
        camera.update()
        camera.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f)
    }

    override fun dispose() {
        batch.dispose()
    }

    override fun inputUp(x: Int, y: Int) {
        buttons.map { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        buttons.map { it.touch(x, y) }
    }

    private fun resetState() {
        blockInput = true
        state = ScreenState.TRANSITION_OP
    }

}