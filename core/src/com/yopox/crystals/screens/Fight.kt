package com.yopox.crystals.screens

import com.badlogic.gdx.graphics.Color
import com.yopox.crystals.Crystals
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.def.Actions
import com.yopox.crystals.def.Fighters
import com.yopox.crystals.def.Icons
import com.yopox.crystals.logic.Hero
import com.yopox.crystals.logic.Progress
import com.yopox.crystals.logic.fight.Fighter
import com.yopox.crystals.logic.fight.Spell
import com.yopox.crystals.logic.fight.Target
import com.yopox.crystals.logic.fight.Team
import com.yopox.crystals.screens.Fight.State.*
import com.yopox.crystals.ui.ActionIcon
import com.yopox.crystals.ui.Icon
import com.yopox.crystals.ui.Screen
import com.yopox.crystals.ui.Transition
import ktx.graphics.use
import kotlin.math.ceil

/**
 * Fight Screen.
 */
class Fight(game: Crystals) : Screen(Util.TEXT_FIGHT, game) {

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
     * Main state components.
     * TODO: Screen shaking
     * TODO: Animations
     */
    enum class BlockType {
        TEXT,
        DAMAGE,
        KO,
        FAINT,
        UPDATE_HP,
        UPDATE_MP,
        LEARN,
        WIN
    }

    data class Block(val type: BlockType, var content: String = "", var int1: Int = 0, var int2: Int = 0)

    private var subState = MAIN
    private val icons = ArrayList<Icon>()
    private val learn: Icon

    var actionButtons = ArrayList<ActionIcon>()
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
        actionButtons.add(ActionIcon(Actions.ID.ATTACK, Pair(10f, 5f)) { selectIcon(0) })
        actionButtons.add(ActionIcon(Actions.ID.DEFENSE, Pair(26f, 5f)) { selectIcon(1) })
        actionButtons.add(ActionIcon(Actions.ID.ITEMS, Pair(42f, 5f)) { selectIcon(2) })
        actionButtons.add(ActionIcon(Actions.ID.W_MAGIC, Pair(58f, 5f)) { selectIcon(3) })
        actionButtons.add(ActionIcon(Actions.ID.ROB, Pair(74f, 5f)) { selectIcon(4) })
        actionButtons.add(ActionIcon(Actions.ID.GEOMANCY, Pair(90f, 5f)) { selectIcon(5) })

        learn = Icon(Icons.ID.EXCLAMATION, Pair(0f, 0f)).apply { hide() }
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
        super.render(delta)

        // Draw characters
        icons.map { icon -> icon.draw(shapeRenderer, batch) }
        learn.draw(shapeRenderer, batch)

        if (!blockInput) {
            update()
        }

        // Draw the Dialog box
        drawDialog()

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

        drawTransitions()
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
                        BlockType.LEARN -> {
                            currentBlock!!.int2 += 1

                            // Draw wipe
                            val t1 = Transition.TRANSITION_TIME
                            val stopTime = 48
                            if (currentBlock!!.int2 < t1 + stopTime)
                                Transition.drawInvertTransition(
                                        shapeRenderer,
                                        learn.pos.first, learn.pos.second,
                                        16f, 16f,
                                        currentBlock!!.int2, stopTime) {}

                            if (currentBlock!!.int2 == t1 + stopTime - 1) {
                                learn.hide()
                                currentBlock = null
                            }
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
            state = ScreenState.ENDING
        }
        BlockType.FAINT -> {
            victory = false
            state = ScreenState.ENDING
        }
        BlockType.UPDATE_HP -> stats[0] = "HP ${currentBlock!!.int1}/${fighters[hero].baseStats.hp}"
        BlockType.UPDATE_MP -> stats[1] = "MP ${currentBlock!!.int1}/${fighters[hero].baseStats.mp}"
        BlockType.LEARN -> {
            learn.pos = Pair(icons[currentBlock!!.int1].pos.first, icons[currentBlock!!.int1].pos.second + 18)
            learn.show()
        }
        else -> Unit
    }

    private fun initState(state: State) = when (state) {
        MAIN -> {
            Navigation.oldStack.clear()
            Intent.targets.clear()
            Dialog.reset()
        }
        CHOOSE_ACTION -> {
            Progress.player.setActionsIcons(actionButtons)
        }
        CHOOSE_SUBACTION -> {
            Progress.player.setSubactionIcons(Navigation.selected, actionButtons)
        }
        CHOOSE_TARGET -> {
            icons.forEach { it.clickable = true }
            actionButtons[0].changeType(Actions.ID.RETURN)
            actionButtons.forEachIndexed { i, icon -> if (i != 0) icon.hide() }
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
                actionButtons.map { it.draw(shapeRenderer, batch) }
            }
            CHOOSE_SUBACTION -> {
                batch.use {
                    Util.font.draw(it, Actions.categoryNames[Intent.category], 10f, 32f)
                }
                actionButtons.map { it.draw(shapeRenderer, batch) }
            }
            CHOOSE_TARGET -> {
                batch.use {
                    Util.font.draw(it, Util.TEXT_TARGET, 10f, 32f)
                }
                actionButtons.map { it.draw(shapeRenderer, batch) }
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
                        Intent.category = actionButtons[i].type
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

    override fun stateChange(st: ScreenState) = when (st) {
        ScreenState.ENDING -> {
            if (victory) {
                game.getScreen<Results>().setup(0, listOf())
                game.setScreen<Results>()
            }
            else game.setScreen<GameOver>()
            resetState()
        }
        ScreenState.OPENING -> {
            state = ScreenState.MAIN; blockInput = false
        }
        else -> Unit
    }

    override fun inputUp(x: Int, y: Int) {
        actionButtons.forEach { it.lift(x, y) }
        icons.forEach { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        actionButtons.forEach { it.touch(x, y) }
        icons.forEach { it.touch(x, y) }
    }

    override fun resetState() {
        blockInput = true
        state = ScreenState.OPENING
        blocks.clear()
        subState = MAIN
        icons.clear()
        fighters.clear()
        currentBlock = null
        turn = 0
        victory = false
        Dialog.reset()
        icons.forEach { it.reset() }
    }

    fun stateChange() {

    }

}