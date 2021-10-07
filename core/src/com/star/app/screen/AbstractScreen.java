package com.star.app.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AbstractScreen implements Screen {

    protected SpriteBatch batch;

    public AbstractScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    // Метод срабатывает при изменении размеров окна
    @Override
    public void resize(int width, int height) {
        ScreenManager.getInstance().resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    // Метода срабатывает при выходе из приложения
    @Override
    public void dispose() {

    }
}
