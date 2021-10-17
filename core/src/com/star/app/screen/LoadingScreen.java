package com.star.app.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.star.app.screen.utils.Assets;

public class LoadingScreen extends AbstractScreen {

    private Texture texture;

    public LoadingScreen(SpriteBatch batch) {
        super(batch);
        Pixmap pixmap = new Pixmap(1280, 20, Pixmap.Format.RGB888); // Pixmap - рисунок, где каждому каналу цвета выделяется по 8 бит RGB888
        pixmap.setColor(Color.GREEN);
        pixmap.fill();
        this.texture = new Texture(pixmap); // Создаем текстуру с рисунок pixmap
        pixmap.dispose();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
        if(Assets.getInstance().getAssetManager().update()) { // getAssetManager - Хранит в себе все ресурсы. update - вернет true, если ресурсы подгрузились
            Assets.getInstance().makeLinks();
            ScreenManager.getInstance().goToTarget(); // goToTarget - экран в который мы перейдем по окончанию загрузки
        }
        batch.begin();
        // getProgress - говорит сколько процентов ресурсов подгрузилось. из getProgress удобно делать отображение загрузки игры)))
        //  от x=0 прогресс загрузки начинает ползьти до ((___1280 * Assets.getInstance().getAssetManager().getProgress()___))
        batch.draw(texture, 0, 0, 1280 * Assets.getInstance().getAssetManager().getProgress(), 20);
        batch.end();
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
