package com.yopox.crystals.screens

import com.badlogic.gdx.Gdx
import com.yopox.crystals.Crystals
import com.yopox.crystals.ScreenState
import com.yopox.crystals.Util
import com.yopox.crystals.def.Fighters
import com.yopox.crystals.logic.Progress
import com.yopox.crystals.ui.TextButton
import com.yopox.crystals.ui.Transition
import ktx.graphics.use

/**
 * Character selection screen
 *
 * TODO: Real nextJobId / previousJob function
 * TODO: Change the state on click
 */
class CharacterSelection(game: Crystals) : Screen("", game) {

    private var hero = Fighters.heroes[0]
    private var nextHero = Fighters.heroes[0]
    private var transition = false
    private var frame = 0

    init {
        // Buttons init
        val x = Util.WIDTH / 4 + 32f + 43
        val y = 16f

        buttons.add(TextButton(x, y, Util.TEXT_CONTINUE) {
            blockInput = true
            Progress.player = hero
            state = ScreenState.ENDING
        })
        buttons.add(TextButton(x, y + 21, Util.TEXT_PREVIOUS) {
            nextHero = Util.next(Fighters.heroes, hero)
            transition = true
            blockInput = true
        })
        buttons.add(TextButton(x, y + 2 * 21, Util.TEXT_NEXT) {
            nextHero = Util.previous(Fighters.heroes, hero)
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

        drawTransitions()
    }

    private fun drawClassTransition() {
        if (transition) {
            frame++
            if (frame <= Transition.TRANSITION_TIME) {
                Transition.drawTransition(shapeRenderer, 8f, 8f, 96f, 16f, frame) {
                    hero = nextHero
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

    private fun drawClassDesc() {
        Util.drawRect(shapeRenderer, 8f, 29f, 96f, 32f)

        val x0 = 12f
        val offset = 32f

        batch.use {
            Util.font.draw(it, hero.desc, 12f, 56f)
            Util.font.draw(it, "HP: ${hero.baseStats.hp}\nMP: ${hero.baseStats.mp}", x0, 24f)
            Util.font.draw(it, "ATK: ${hero.baseStats.atk}\nWIS: ${hero.baseStats.wis}", x0 + offset, 24f)
            Util.font.draw(it, "DEF: ${hero.baseStats.def}\nSPD: ${hero.baseStats.spd}", x0 + 2 * offset, 24f)
            Util.bigFont.draw(it, hero.name, 10f, Util.HEIGHT - Util.TITLE_OFFSET)
        }
    }

    override fun inputUp(x: Int, y: Int) {
        if (!blockInput) buttons.map { it.lift(x, y) }
    }

    override fun inputDown(x: Int, y: Int) {
        if (!blockInput) buttons.map { it.touch(x, y) }
    }

    override fun resetState() {
        hero = Fighters.heroes[0]
        nextHero = Fighters.heroes[0]
        transition = false
        state = ScreenState.OPENING
        frame = 0
        blockInput = true
    }

}
