package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.star.app.StarGame;
import com.star.app.game.Hero;
import com.star.app.screen.utils.Assets;

public class ScreenManager {
    public enum ScreenType{ // Типы экранов
        GAME, MENU, GAMEOVER
    }
    public static final int SCREEN_WIDTH = 1280;    // Gdx.graphics.getWidth();
    public static final int HALF_SCREEN_WIDTH = SCREEN_WIDTH / 2;
    public static final int SCREEN_HEIGTH = 720;   // Gdx.graphics.getHeight();
    public static final int HALF_SCREEN_HEIGTH = SCREEN_HEIGTH / 2;

    private StarGame game;
    private SpriteBatch batch;
    private LoadingScreen loadingScreen;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private Screen targetScreen;
    private GameOverScreen gameOverScreen;
    private Viewport viewport;


    // Строки 30, 32, 36 - реализация этого класса как синглтона
    private static ScreenManager ourInstance = new ScreenManager();

    public static ScreenManager getInstance() {
        return ourInstance;
    }

    public Viewport getViewport() {
        return viewport;
    }

    private ScreenManager() {}

    public void init(StarGame game, SpriteBatch batch) {
        this.game = game;
        this.batch = batch;
        this.viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGTH); // fit игровой сцены под viewport
        this.gameScreen = new GameScreen(batch);
        this.menuScreen = new MenuScreen(batch);
        this.loadingScreen = new LoadingScreen(batch);
        this.gameOverScreen = new GameOverScreen(batch);
    }


    // Изменение размеров экрана
    public void resize(int width, int height) {
        viewport.update(width, height);
        viewport.apply();
    }

    // Вызывается когда идет смена экранов
    // Object...args - массив, чтоб по хитрому в switch в экран gameOverScreen передать Hero
    public void changeScreen(ScreenType type, Object...args) {
        Screen screen = game.getScreen();
        // Полностью убераем все ресурсы для текущего экрана
        Assets.getInstance().clear();
        if (screen != null) {
            screen.dispose();
        }

        game.setScreen(loadingScreen);
        switch (type) {
            case GAME:
                targetScreen = gameScreen;
                Assets.getInstance().loadAssets(ScreenType.GAME);
                break;
            case MENU:
                targetScreen = menuScreen;
                Assets.getInstance().loadAssets(ScreenType.MENU);
                break;
            case GAMEOVER:
                targetScreen = gameOverScreen;
                gameOverScreen.setDefeatedHero((Hero)args[0]);
                Assets.getInstance().loadAssets(ScreenType.GAMEOVER);
                break;
            default:
                break;
        }
    }

    public void goToTarget() {
        game.setScreen(targetScreen);
    }
}
