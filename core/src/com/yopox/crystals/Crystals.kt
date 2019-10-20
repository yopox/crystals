package com.yopox.crystals

import com.badlogic.gdx.Screen
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.yopox.crystals.screens.*
import ktx.app.KtxGame
import ktx.freetype.registerFreeTypeFontLoaders

/**
 * Main class.
 *
 * Loads the fonts, initialize the different game states and start the title screen.
 *
 * TODO: Move assets loading elsewhere
 */
class Crystals : KtxGame<Screen>() {

    companion object {
        lateinit var assetManager: AssetManager
    }

    override fun create() {
        assetManager = initiateAssetManager()
        with(assetManager) {
            load("icons.png", Texture::class.java)
            load("1BitPack.png", Texture::class.java)
            load("Base_Attacks.png", Texture::class.java)
            load("temple.png", Texture::class.java)
            load("circle.png", Texture::class.java)
            finishLoading()
        }

        Util.genFonts()
        ShaderProgram.pedantic = false

        addScreen(TitleScreen(this))
        addScreen(CharacterSelection(this))
        addScreen(Trip(this))
        addScreen(Display(this))
        addScreen(Fight(this))
        addScreen(Inn(this))
        addScreen(GameOver(this))
        addScreen(Temple(this))
        setScreen<TitleScreen>()
    }

    private fun initiateAssetManager(): AssetManager {
        val assetManager = AssetManager()
        assetManager.registerFreeTypeFontLoaders()
        return assetManager
    }

}
