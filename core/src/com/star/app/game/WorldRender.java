package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class WorldRender {
    private GameController gc;
    private SpriteBatch batch;
    public BitmapFont font32;
    private StringBuilder sb;

    // Рисовать будем не через batch выводя сразу на экран, а через frameBuffer
    // для шейдинга кадра
    private FrameBuffer frameBuffer;
    private TextureRegion frameBufferRegion;
    private ShaderProgram shaderProgram;

    public WorldRender(GameController gc, SpriteBatch batch) {
        this.gc = gc;
        this.batch = batch;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf", BitmapFont.class);
        this.sb = new StringBuilder();

        this.frameBuffer = new FrameBuffer(Pixmap.Format.RGB888, ScreenManager.SCREEN_WIDTH,
                ScreenManager.SCREEN_HEIGTH, false);
        this.frameBufferRegion = new TextureRegion(frameBuffer.getColorBufferTexture());
        // Переворачиваем из оконной в декартовую ситсему координат
        this.frameBufferRegion.flip(false,true);

        this.shaderProgram = new ShaderProgram(Gdx.files.internal("shaders/vertex.glsl").readString(),
                Gdx.files.internal("shaders/fragment.glsl").readString());
        if(!shaderProgram.isCompiled()) {
            throw new IllegalArgumentException("Error compiling shader: " + shaderProgram.getLog());
        }
    }

    // Отрисовка
    public void render () {
        ScreenUtils.clear(0, 0.5f, 0.2f, 1);
        batch.begin();
        gc.getBackground().render(batch);	// Передаем batch в метод render, чтоб batch отрисовал задний фон
        gc.getEnemyManager().render(batch);
        gc.getHero().render(batch);
        gc.getBulletController().render(batch);
        gc.getParticleController().render(batch);
        gc.getBonusController().render(batch);
        gc.getHero().renderGUI(batch,font32);

        if(gc.getRoundTimer() <= 3.0f) {
            sb.clear();
            sb.append("Level ").append(gc.getLevel());
            font32.draw(batch, sb, 0, (float)(ScreenManager.SCREEN_HEIGTH / 2), ScreenManager.SCREEN_WIDTH, Align.center, false);
        }

        batch.end();

        gc.getStage().draw();
    }
}
