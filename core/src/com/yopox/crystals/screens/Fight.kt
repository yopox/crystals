package com.yopox.crystals.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.yopox.crystals.Crystals
import com.yopox.crystals.InputScreen
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.def.Actions
import com.yopox.crystals.def.Fighters
import com.yopox.crystals.def.Spells
import com.yopox.crystals.logic.Hero
import com.yopox.crystals.logic.Progress
import com.yopox.crystals.logic.fight.Fighter
import com.yopox.crystals.logic.fight.Spell
import com.yopox.crystals.logic.fight.Target
import com.yopox.crystals.logic.fight.Team
import com.yopox.crystals.screens.Fight.State.*
import com.yopox.crystals.ui.ActionIcon
import com.yopox.crystals.ui.Icon
import com.yopox.crystals.ui.Transition
import ktx.app.KtxScreen
import ktx.graphics.use
import kotlin.math.ceil

/**
 * Fight Screen.
 */
class Fight(private val game: Crystals) : KtxScreen, InputScreen {

    /**
     * State of the fight :
     * @property MAIN consumes [Block] objects from [blocks]
     * @property CHOOSE_ACTION let the player choose the action for the turn
     * @property CHOOSE_SUBACTION let the player choose a subaction
     * @property CHOOSE_TARGET let the player choose a targets
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
    enum class BlockType {
        TEXT,
        DAMAGE,
        KO,
        FAINT,
        UPDATE_HP,
        UPDATE_MP,
        WIN
    }

    data class Block(val type: BlockType, var content: String = "", var int1: Int = 0, var int2: Int = 0)


    private val batch = SpriteBatch()
    private val shapeRenderer = ShapeRenderer()
    override val camera = OrthographicCamera().also { it.position.set(Util.WIDTH / 2, Util.HEIGHT / 2, 0f) }
    override var blockInput = true
    private val viewport = FitViewport(Util.WIDTH, Util.HEIGHT, camera)

    private val buttons = ArrayList<ActionIcon>()
    private var state = ScreenState.TRANSITION_OP
    private var subState = MAIN
    private val icons = ArrayList<Icon>()

    private val blocks = ArrayList<Block>()
    private var currentBlock: Block? = null
    private val fighters = ArrayList<Fighter>()
    private var stats = mutableListOf("", "")
    private var hero = 1
    private var turn = 0
    private var victory = false

    internal object Intent {
        var category = Actions.ID.WARRIOR
        var action = Actions.ID.DEFENSE
        var targets = ArrayList<Fighter>()
    }

    data class Move(val fighter: Fighter, val spell: Spell, val targets: ArrayList<Fighter>)

    private object Navigation {
        var state: State = MAIN
        var selected = 0
        var frame = 0
        var navigating = false
        var oldStack = ArrayList<State>()

        // TODO: Find a way to set [blockInput] = true here
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
        const val BASE_COOLDOWN = 3
        const val END_COOLDOWN = 40
        var line1 = ""
        var line2 = ""
        var cooldown = END_COOLDOWN

        fun reset() {
            line1 = ""; line2 = ""; cooldown = END_COOLDOWN
        }
    }

    init {
        buttons.add(ActionIcon(Actions.ID.ATTACK, Pair(10f, 5f)) { selectIcon(0) })
        buttons.add(ActionIcon(Actions.ID.DEFENSE, Pair(26f, 5f)) { selectIcon(1) })
        buttons.add(ActionIcon(Actions.ID.ITEMS, Pair(42f, 5f)) { selectIcon(2) })
        buttons.add(ActionIcon(Actions.ID.W_MAGIC, Pair(58f, 5f)) { selectIcon(3) })
        buttons.add(ActionIcon(Actions.ID.ROB, Pair(74f, 5f)) { selectIcon(4) })
        buttons.add(ActionIcon(Actions.ID.GEOMANCY, Pair(90f, 5f)) { selectIcon(5) })
    }

    fun setup() {
        // Add fighters
        fighters.clear()
        fighters.add(Fighters.random())
        if (Math.random() < 0.4) fighters.add(Fighters.random())
        if (Math.random() < 0.05) fighters.add(Fighters.random())
        fighters.add(Progress.player)

        // Set battleId
        fighters.withIndex().forEach { it.value.battleId = it.index }

        // Heal fighters
        fighters.forEach { it.prepare() }

        // Get the hero
        hero = fighters.lastIndex
        stats[0] = "HP ${fighters[hero].stats.hp}/${fighters[hero].baseStats.hp}"
        stats[1] = "MP ${fighters[hero].stats.mp}/${fighters[hero].baseStats.mp}"

        // Add enemies icons
        val enemies = fighters.filter { it.team == Team.ENEMIES }
        for (i in 0..enemies.lastIndex)
            icons.add(Icon(enemies[i].icon, Pair(56f - 20 * i, 36f)) { selectTarget(i) })

        // Add allies icons
        val allies = fighters.filter { it.team == Team.ALLIES }
        for (i in 0..allies.lastIndex)
            icons.add(Icon(allies[i].icon, Pair(88f + 20 * i, 36f)) { selectTarget(i + enemies.size) })
        icons.forEach { it.clickable = false }

        // Opening message
        var text = fighters.filter { it.team == Team.ENEMIES }.map { it.name }.reduce { n1, n2 -> "$n1, $n2" }
        text += when (fighters.filter { it.team == Team.ENEMIES }.size) {
            1 -> " attacks!"
            else -> " attack!"
        }
        blocks.add(Block(BlockType.TEXT, text))
    }

    override fun render(delta: Float) {
        batch.projectionMatrix = camera.combined
        shapeRenderer.projectionMatrix = camera.combined

        // Draw characters
        icons.map { icon -> icon.draw(shapeRenderer, batch) }

        if (!blockInput) {
            update()
        }

        // Draw the Dialog box
        drawDialog()

        batch.use {
            // Draw the title
            Util.bigFont.draw(it, Util.TEXT_FIGHT, 10f, Util.HEIGHT - Util.TITLE_OFFSET)
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
                    if (victory) game.setScreen<Trip>()
                    else game.setScreen<GameOver>()
                    resetState()
                }
            }
            else -> Unit
        }

    }

    private fun update() {
        when (subState) {
            MAIN -> {

                if (currentBlock == null) {
                    if (blocks.size > 0) {
                        // New block
                        currentBlock = blocks.removeAt(0)
                        initBlock(currentBlock!!.type)
                    } else {
                        // No more blocks, [subState] set to [State.CHOOSE_ACTION] automatically

                        if (turn > 0 && !victory) {
                            // 1/20 max MP heal
                            val hero = fighters[hero]
                            stats[1] = "MP ${hero.stats.mp}/${hero.baseStats.mp}"
                        }
                        turn++

                        Navigation to CHOOSE_ACTION
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
                        BlockType.KO -> {
                            val icon = icons[currentBlock!!.int1]
                            currentBlock!!.int2 += 1
                            Transition.drawTransition(
                                    shapeRenderer, icon.pos.first, icon.pos.second,
                                    icon.size.first, icon.size.second, currentBlock!!.int2)
                            { icon.hide() }
                            if (currentBlock!!.int2 == Transition.TRANSITION_TIME)
                                currentBlock = null
                        }
                        BlockType.DAMAGE -> {
                            currentBlock!!.int2 += 1

                            // Draw wipe
                            val icon = icons[currentBlock!!.int1]
                            val size = Util.textSize(currentBlock!!.content)
                            val x0 = icon.pos.first + (icon.size.first - size.first) / 2
                            val y0 = icon.pos.second + icon.size.second + 2
                            Transition.drawTransition(shapeRenderer, x0 - 2, y0, size.first + 5, 8f, currentBlock!!.int2, Util.POPUP_STOP_TIME)
                            Util.font.color = Color.BLACK
                            batch.use {
                                Util.font.draw(it, currentBlock!!.content, x0, y0 + size.second + 1)
                            }
                            Util.font.color = Color.WHITE

                            // Flash character
                            if (currentBlock!!.int2 <= 3 * 8) {
                                icon.visible = (currentBlock!!.int2 / 8) % 2 == 1
                            }
                            if (currentBlock!!.int2 == Transition.TRANSITION_TIME + Util.POPUP_STOP_TIME)
                                currentBlock = null
                        }
                        else -> currentBlock = null
                    }
                }
            }
            CHOOSE_ACTION -> {
            }
            CHOOSE_SUBACTION -> {
            }
            CHOOSE_TARGET -> {
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
        BlockType.WIN -> {
            victory = true
            state = ScreenState.TRANSITION_EN
        }
        BlockType.FAINT -> {
            victory = false
            state = ScreenState.TRANSITION_EN
        }
        BlockType.UPDATE_HP -> stats[0] = "HP ${currentBlock!!.int1}/${fighters[hero].baseStats.hp}"
        BlockType.UPDATE_MP -> stats[1] = "MP ${currentBlock!!.int1}/${fighters[hero].baseStats.mp}"

        else -> Unit
    }

    private fun initState(state: State): Unit = when (state) {
        MAIN -> {
            Navigation.oldStack.clear()
            Intent.targets.clear()
            Dialog.reset()
        }
        CHOOSE_ACTION -> {
            Progress.player.setActionsIcons(buttons)
        }
        CHOOSE_SUBACTION -> {
            Progress.player.setSubactionIcons(Navigation.selected, buttons)
        }
        CHOOSE_TARGET -> {
            icons.forEach { it.clickable = true }
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
            Util.font.draw(it, stats[0], 120f, 19f)
            Util.font.draw(it, stats[1], 120f, 9f)
        }

        when (subState) {
            MAIN -> {
                batch.use {
                    Util.font.draw(it, Dialog.line1, 10f, 19f)
                    Util.font.draw(it, Dialog.line2, 10f, 9f)
                    Util.font.draw(it, Util.TEXT_LOG, 10f, 32f)
                }
            }
            CHOOSE_ACTION -> {
                batch.use {
                    Util.font.draw(it, Util.TEXT_ACTIONS, 10f, 32f)
                }
                buttons.map { it.draw(shapeRenderer, batch) }
            }
            CHOOSE_SUBACTION -> {
                batch.use {
                    Util.font.draw(it, Actions.categoryNames[Intent.category], 10f, 32f)
                }
                buttons.map { it.draw(shapeRenderer, batch) }
            }
            CHOOSE_TARGET -> {
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
                MAIN -> Unit
                CHOOSE_ACTION -> when {
                    i == 0 -> {
                        // Attack
                        Navigation to CHOOSE_TARGET
                        Intent.action = Actions.ID.ATTACK
                        blockInput = true
                    }
                    i == 1 -> {
                        // Defense
                        Intent.action = Actions.ID.DEFENSE
                        computeTurn()
                    }
                    i == 2 -> {
                        // Item
                    }
                    i > 2 -> {
                        // Crystal
                        Navigation to CHOOSE_SUBACTION
                        Navigation.selected = i - 3
                        Intent.category = buttons[i].type
                        blockInput = true
                    }
                    else -> {
                    }
                }
                CHOOSE_SUBACTION -> when (i) {
                    0 -> {
                        Navigation.rewind()
                        blockInput = true
                    }
                    else -> {
                        // Get the corresponding action
                        val crystal = (fighters[hero] as Hero).crystals[Navigation.selected]
                        val spell = crystal.spells[i - 1]
                        if (i - 1 < crystal.unlocked && spell.cost <= fighters[hero].stats.mp) {
                            // The spell is unlocked
                            Intent.action = spell.id
                            (fighters[hero] as Hero).useCrystal(crystal)
                            when (spell.target) {
                                Target.SINGLE -> {
                                    Navigation to CHOOSE_TARGET
                                    blockInput = true
                                }
                                Target.ENEMIES -> {
                                    val enemies = fighters.filter { it.team != fighters[hero].team }
                                    Intent.targets.addAll(enemies)
                                    computeTurn()
                                }
                                Target.ALL -> {
                                    Intent.targets.addAll(fighters)
                                    computeTurn()
                                }
                                Target.SELF -> {
                                    Intent.targets.add(fighters[hero])
                                    computeTurn()
                                }
                            }
                        }
                    }
                }
                CHOOSE_TARGET -> if (i == 0) {
                    Navigation.rewind()
                    blockInput = true
                } else Unit
            }
        }
    }

    private fun selectTarget(i: Int) {
        if (!blockInput && subState == CHOOSE_TARGET) {
            Intent.targets.add(fighters[i])
            computeTurn()
        }
    }

    private fun computeTurn() {
        icons.forEach { it.clickable = false }
        blockInput = true

        // Get fighters moves
        val alive = fighters.filter { it.alive }
        val moves = alive.map { it.getMove(fighters) }.toMutableList()
        moves.sortBy { -it.fighter.stats.spd }

        // Execute moves
        for (move in moves) {
            if (move.fighter.alive)
                blocks.addAll(move.fighter.beginTurn(fighters))

            if (move.fighter.alive) {
                blocks.addAll(move.spell.use(move))
                if (move.fighter.alive) {
                    move.fighter.healMP(ceil((move.fighter.baseStats.mp / 20.0)).toInt())
                }
            }

            // Game Over condition
            if (!fighters.filter { it.team == Team.ALLIES }.map { it.alive }.contains(true))
                blocks.add(Block(BlockType.FAINT))

            // Win condition
            if (!fighters.filter { it.team == Team.ENEMIES }.map { it.alive }.contains(true))
                blocks.add(Block(BlockType.WIN))
        }

        Navigation to MAIN
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

    override fun inputUp(x: Int, y: Int) {
        buttons.map { it.lift(x, y) }
        icons.map { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        buttons.map { it.touch(x, y) }
        icons.map { it.touch(x, y) }
    }

    private fun resetState() {
        blockInput = true
        state = ScreenState.TRANSITION_OP
        blocks.clear()
        subState = MAIN
        icons.clear()
        fighters.clear()
        currentBlock = null
        turn = 0
        victory = false
        Dialog.reset()
    }

}