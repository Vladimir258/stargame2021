package com.star.app;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.screen.GameScreen;
import com.star.app.screen.ScreenManager;

public class StarGame extends Game {
	private SpriteBatch batch;

	
	@Override
	public void create () {
		batch = new SpriteBatch();		// Пространство для отрисовки игровой графики
		ScreenManager.getInstance().init(this, batch);
		ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
	}

	// Отрисовка
	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime(); // Берем дельта тайм для плавности отрисовки независимо от характеристик ПК
		getScreen().render(dt);
	}

	// Очистка ресурсов из видеопамяти
	@Override
	public void dispose () {
		batch.dispose();
	}
}
