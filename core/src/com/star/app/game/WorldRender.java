package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class WorldRender {
    private GameController gc;
    private SpriteBatch batch;

    public WorldRender(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
    }

    // Отрисовка
    public void render () {
        ScreenUtils.clear(0, 0.5f, 0.2f, 1);
        batch.begin();
        gc.getBackground().render(batch);	// Передаем batch в метод render, чтоб batch отрисовал задний фон
        gc.getHero().render(batch);
        gc.getEnemyManager().render(batch);
        gc.getBulletController().render(batch);
        batch.end();
    }
}
