package com.star.app.screen.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.star.app.screen.ScreenManager;

// Данный класс является синглтоном
public class Assets {
    private static final Assets ourInstance = new Assets(); // Создание данного синглтона
    public static Assets getInstance() {                    // Получение данного синглтона
        return ourInstance;
    }

    private AssetManager assetManager;      // Менеджер ассетов
    private TextureAtlas textureAtlas;      // Получение текстур

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public TextureAtlas getAtlas() {
        return textureAtlas;
    }

    private Assets() {
        assetManager = new AssetManager();
    }

    public void loadAssets(ScreenManager.ScreenType type) { // Загрузка ресурсов
        switch (type) {
            case GAMEOVER:
                assetManager.load("images/game.pack", TextureAtlas.class);
                createStandardFont(72);
                createStandardFont(48);
                createStandardFont(24);
                assetManager.load("audio/music.mp3", Music.class);
                break;
            case MENU:
                assetManager.load("images/game.pack", TextureAtlas.class);
                createStandardFont(72);
                createStandardFont(24);
                assetManager.load("audio/music.mp3", Music.class);
                break;
            case GAME:
                assetManager.load("audio/mortal.mp3", Music.class);
                assetManager.load("audio/shoot.mp3", Sound.class);
                assetManager.load("images/game.pack", TextureAtlas.class);
                createStandardFont(32);
                createStandardFont(24);
                // Подождать пока загрузятся все ресурсы (строки 39, 40)
               // assetManager.finishLoading();   - убрали когда сделали LoadingScreen, так ка теперь здесь не нужно ждать загрузки ресурсов
               // вместо этого у нас есть экран с отображением загрузки
                break;
        }
    }

    private void createStandardFont(int size) {                             // Генерация атласов текстур для шрифтов на ходу
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter fontParameter = new
                FreetypeFontLoader.FreeTypeFontLoaderParameter();
        fontParameter.fontFileName = "fonts/font32.ttf";
        fontParameter.fontParameters.size = size;
        fontParameter.fontParameters.color = Color.WHITE;
        fontParameter.fontParameters.shadowOffsetX = 1;
        fontParameter.fontParameters.shadowOffsetY = 1;
        fontParameter.fontParameters.shadowColor = Color.DARK_GRAY;
        assetManager.load("fonts/font" + size + ".ttf", BitmapFont.class, fontParameter);
    }
    // Метод дает ссылку на атлас текстуру
    public void makeLinks() {
        textureAtlas = assetManager.get("images/game.pack", TextureAtlas.class);
    }

    public void clear() {       // Очистка ресурсов из памяти
        assetManager.clear();
    }
 }
