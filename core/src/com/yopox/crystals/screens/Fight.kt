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
import com.yopox.crystals.data.Progress
import com.yopox.crystals.def.Actions
import com.yopox.crystals.def.Icons
import com.yopox.crystals.def.Jobs
import com.yopox.crystals.logic.Crystal
import com.yopox.crystals.logic.Fighter
import com.yopox.crystals.ui.*
import ktx.app.KtxScreen
import ktx.graphics.use

/**
 * Fight Screen.
 *
 * TODO: Fight setup function
 */
class Fight(private val game: Crystals) : KtxScreen, InputScreen {

    /**
     * State of the fight :
     * @property MAIN consumes [Block] objects from [blocks]
     * @property CHOOSE_ACTION let the player choose the action for the turn
     * @property CHOOSE_SUBACTION let the player choose a subaction
     * @property CHOOSE_TARGET let the player choose a target
     */
    private enum class State {
        MAIN,
        CHOOSE_ACTION,
        CHOOSE_SUBACTION,
        CHOOSE_TARGET
    }

    /**
     * Main state components. Useful to tell the fight.
     * TODO: Screen shaking
     * TODO: Animations
     * TODO: Update HP/MP
     */
    private enum class BlockType {
        TEXT
    }

    private data class Block(val type: BlockType, var content: String)

    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    override val camera = OrthographicCamera().also { it.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f) }
    override var blockInput = true
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)

    private val buttons = ArrayList<ActionIcon>()
    private var state = ScreenState.TRANSITION_OP
    private var subState = State.MAIN
    private val icons = ArrayList<Icon>()

    private val blocks = ArrayList<Block>()
    private var currentBlock: Block? = null

    private object Intent {
        var category = Actions.ID.WARRIOR
        var action = Actions.ID.DEFENSE
        var target = ArrayList<Fighter>()
    }

    private object Navigation {
        var state: State = State.MAIN
        var selected = 0
        var frame = 0
        var navigating = false
        var oldStack = ArrayList<State>()

        infix fun to(newState: State) {
            oldStack.add(0, state)
            state = newState
            frame = 0
            navigating = true
        }

        fun rewind() {
            state = oldStack.removeAt(0)
            frame = 0
            navigating = true
        }
    }

    private object Dialog {
        const val BASE_COOLDOWN = 4
        const val END_COOLDOWN = 32
        var line1 = ""
        var line2 = ""
        var cooldown = BASE_COOLDOWN
    }

    init {
        // TODO: Generate fights

        buttons.add(ActionIcon(Actions.ID.ATTACK, Pair(10f, 5f)) { selectIcon(0) })
        buttons.add(ActionIcon(Actions.ID.DEFENSE, Pair(26f, 5f)) { selectIcon(1) })
        buttons.add(ActionIcon(Actions.ID.ITEMS, Pair(42f, 5f)) { selectIcon(2) })
        buttons.add(ActionIcon(Actions.ID.W_MAGIC, Pair(58f, 5f)) { selectIcon(3) })
        buttons.add(ActionIcon(Actions.ID.ROB, Pair(74f, 5f)) { selectIcon(4) })
        buttons.add(ActionIcon(Actions.ID.GEOMANCY, Pair(90f, 5f)) { selectIcon(5) })

    }

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        if (!blockInput) {
            update()
        }

        // Draw the Dialog box
        drawDialog()

        batch.use {
            // Draw the title
            Util.bigFont.draw(it, Util.TEXT_FIGHT, 10f, Util.HEIGHT - Util.TITLE_OFFSET)

            // Draw characters
            icons.map { icon -> icon.draw(shapeRenderer, it) }
        }

        if (Navigation.navigating) {
            Navigation.frame++
            if (Navigation.frame <= Transition.TRANSITION_TIME) {
                Transition.drawTransition(shapeRenderer,
                        0f, 0f, 112f, 22f, Navigation.frame)
                {
                    subState = Navigation.state
                    initState(subState)
                }
            } else {
                Navigation.navigating = false
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

    private fun update() {
        when (subState) {
            State.MAIN -> {

                if (currentBlock == null) {
                    if (blocks.size > 0) {
                        // New block
                        currentBlock = blocks.removeAt(0)
                        initBlock(currentBlock!!.type)
                    } else {
                        // No more blocks, [subState] set to [State.CHOOSE_ACTION] automatically
                        Navigation to State.CHOOSE_ACTION
                        blockInput = true
                    }

                } else {
                    // Consume block
                    when (currentBlock!!.type) {

                        BlockType.TEXT -> {
                            if (Dialog.cooldown > 0) {
                                // Waiting
                                Dialog.cooldown--
                            }

                            val len = currentBlock!!.content.length
                            if (len > 0 && Dialog.cooldown == 0) {
                                // Displaying the next character
                                Dialog.line1 += currentBlock!!.content[0]
                                currentBlock!!.content = currentBlock!!.content.substring(1)
                                Dialog.cooldown = if (len == 1) Dialog.END_COOLDOWN else Dialog.BASE_COOLDOWN
                            }

                            if (len == 0 && Dialog.cooldown == 0) {
                                // No more text to display
                                currentBlock = null
                            }
                        }
                    }
                }
            }
            State.CHOOSE_ACTION -> {
            }
            State.CHOOSE_SUBACTION -> {
            }
            State.CHOOSE_TARGET -> {
            }
        }
    }

    /**
     * Execute code when [currentBlock] changes.
     */
    private fun initBlock(blockType: BlockType) = when (blockType) {
        BlockType.TEXT -> {
            Dialog.line2 = Dialog.line1
            Dialog.line1 = ""
        }
    }

    private fun initState(state: State): Unit = when (state) {
        State.MAIN -> {
            Navigation.oldStack.clear()
        }
        State.CHOOSE_ACTION -> {
            Progress.player.setActionsIcons(buttons)
        }
        State.CHOOSE_SUBACTION -> {
            Progress.player.setSubactionIcons(Navigation.selected, buttons)
        }
        State.CHOOSE_TARGET -> {
            buttons[0].changeType(Actions.ID.RETURN)
            for (i in 1..5) buttons[i].hide()
        }
    }

    private fun drawDialog() {
        // Draw the dialog line
        Util.drawRect(shapeRenderer, -1f, -1f, 182f, 24f)

        // Draw the hero status
        // TODO: Real stats
        batch.use {
            Util.font.draw(it, "HP 23/40", 120f, 19f)
            Util.font.draw(it, "MP 8/17", 120f, 9f)
        }

        when (subState) {
            State.MAIN -> {
                batch.use {
                    Util.font.draw(it, Dialog.line1, 10f, 19f)
                    Util.font.draw(it, Dialog.line2, 10f, 9f)
                    Util.font.draw(it, Util.TEXT_LOG, 10f, 32f)
                }
            }
            State.CHOOSE_ACTION -> {
                batch.use {
                    Util.font.draw(it, Util.TEXT_ACTIONS, 10f, 32f)
                }
                buttons.map { it.draw(shapeRenderer, batch) }
            }
            State.CHOOSE_SUBACTION -> {
                batch.use {
                    Util.font.draw(it, Actions.categoryNames[Intent.category], 10f, 32f)
                }
                buttons.map { it.draw(shapeRenderer, batch) }
            }
            State.CHOOSE_TARGET -> {
                batch.use {
                    Util.font.draw(it, Util.TEXT_TARGET, 10f, 32f)
                }
                buttons.map { it.draw(shapeRenderer, batch) }
            }
        }
    }

    private fun selectIcon(i: Int) {
        if (!blockInput) {
            when (subState) {
                State.MAIN -> Unit
                State.CHOOSE_ACTION -> when {
                    i == 0 -> {
                        // Attack
                        Navigation to State.CHOOSE_TARGET
                        Intent.action = Actions.ID.ATTACK
                        blockInput = true
                    }
                    i == 1 -> {
                        // Defense
                        Navigation to State.MAIN
                        Intent.action = Actions.ID.DEFENSE
                        Intent.target.add(Progress.player)
                        blockInput = true
                    }
                    i == 2 -> {
                        // Item
                        TODO()
                    }
                    i > 2 -> {
                        // Crystal
                        Navigation to State.CHOOSE_SUBACTION
                        Navigation.selected = i - 3
                        Intent.category = buttons[i].type
                        blockInput = true
                    }
                    else -> {
                    }
                }
                State.CHOOSE_SUBACTION -> when (i) {
                    0 -> {
                        Navigation.rewind()
                        blockInput = true
                    }
                    else -> {
                    }
                }
                State.CHOOSE_TARGET -> when (i) {
                    0 -> {
                        Navigation.rewind()
                        blockInput = true
                    }
                    else -> {
                    }
                }
            }
        }
    }

    private fun selectTarget(i: Int) {

    }

    override fun show() {
        super.show()
        Gdx.input.inputProcessor = this
        // TODO: Remove test blocks
        icons.add(Icon(Progress.player.getIcon(), Pair(88f, 40f)) { selectTarget(1) })
        icons.add(Icon(Icons.Snake, Pair(56f, 40f)) { selectTarget(0) })
        blocks.add(Block(BlockType.TEXT, "Snake attacks!"))
        blocks.add(Block(BlockType.TEXT, "Get ready!"))
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