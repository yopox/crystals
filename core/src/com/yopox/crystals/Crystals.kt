package com.yopox.crystals

import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.yopox.crystals.screens.CharacterSelection
import com.yopox.crystals.screens.TitleScreen
import com.yopox.crystals.screens.Trip
import ktx.app.KtxGame
import ktx.freetype.registerFreeTypeFontLoaders

/**
 * Main class.
 *
 * Loads the fonts, initialize the different game states and start the title screen.
 *
 * TODO: Move assets loading elsewhere
 * TODO: Move map creation
 */
class Crystals : KtxGame<Screen>() {

    companion object {
        lateinit var assetManager: AssetManager
        lateinit var events: Array<Array<Int>>
    }

    override fun create() {
        assetManager = initiateAssetManager()
        with(assetManager) {
            load("aseprite/icons.png", Texture::class.java)
            finishLoading()
        }

        Util.genFonts()

        events = Array(5) { Array(3) {(Math.random() * 5).toInt()} }

        addScreen(TitleScreen(this))
        addScreen(CharacterSelection(this))
        addScreen(Trip())
        setScreen<TitleScreen>()
    }

    private fun initiateAssetManager(): AssetManager {
        val assetManager = AssetManager()
        assetManager.registerFreeTypeFontLoaders()
        return assetManager
    }

}
