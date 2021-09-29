package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.screen.utils.Assets;

public class WorldRender {
    private GameController gc;
    private SpriteBatch batch;
    public BitmapFont font32;
    private StringBuilder stringBuilder; //

    public WorldRender(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);
        this.stringBuilder = new StringBuilder();
    }

    // Отрисовка
    public void render () {
        ScreenUtils.clear(0, 0.5f, 0.2f, 1);
        batch.begin();
        gc.getBackground().render(batch);	// Передаем batch в метод render, чтоб batch отрисовал задний фон
        gc.getEnemyManager().render(batch);
        gc.getHero().render(batch);
        gc.getBulletController().render(batch);
        stringBuilder.clear();
        stringBuilder.append("SCORE: ").append(gc.getHero().getScoreView()); // Получаем счет
        font32.draw(batch, stringBuilder, 20, 700); // Выводим счет на экран
        batch.end();
    }
}
