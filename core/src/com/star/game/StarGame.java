package com.star.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class StarGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private Background background; // Наш бэкграунд
	private Hero hero;
	private EnemyManager enemyManager;

	public Hero getHero() {
		return hero;
	}
	
	@Override
	public void create () {
		batch = new SpriteBatch();		// Пространство для отрисовки игровой графики
		background = new Background(this);	// Инициализация бэкграунда
		hero = new Hero();
		enemyManager = new EnemyManager();
	}

	// Отрисовка
	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime(); // Берем дельта тайм для плавности отрисовки независимо от характеристик ПК
		update(dt);
		ScreenUtils.clear(0, 0.5f, 0.2f, 1);
		batch.begin();
		background.render(batch);	// Передаем batch в метод render, чтоб batch отрисовал задний фон
		hero.render(batch);
		enemyManager.render(batch);
		batch.end();
	}

	public void update(float dt) {
		background.update(dt);
		hero.update(dt);
		enemyManager.update(dt);
	}

	// Очистка ресурсов из видеопамяти
	@Override
	public void dispose () {
		batch.dispose();
	}
}
