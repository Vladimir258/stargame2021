package com.star.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class EnemyManager {
    class Asteroid {
        private Vector2 position;
        private Vector2 velocity;
        private int rotation;
        private int startPosition;

        public Asteroid(int stPosition) {
            // В конструкторе рандомно создаем астероид, координаты и скорость
            startPosition = stPosition; // Точка появления астероида
            this.rotation = 0;
            switch (startPosition) {
                case 1:
                    this.position = new Vector2(ScreenManager.SCREEN_WIDTH + 100, MathUtils.random(0, ScreenManager.SCREEN_HEIGTH));
                    this.velocity = new Vector2(MathUtils.random(80, 300), 0);
                    break;
                case 2:
                    this.position = new Vector2(-100, MathUtils.random(0, ScreenManager.SCREEN_HEIGTH));
                    this.velocity = new Vector2(MathUtils.random(-300, -80), 0);
                    break;
                case 3:
                    this.position = new Vector2(MathUtils.random(0,ScreenManager.SCREEN_WIDTH ), -50);
                    this.velocity = new Vector2(0, MathUtils.random(80, 300));
                    break;
                case 4:
                    this.position = new Vector2(MathUtils.random(0,ScreenManager.SCREEN_WIDTH ), ScreenManager.SCREEN_HEIGTH + 50);
                    this.velocity = new Vector2(0, MathUtils.random(-300, -80));
                    break;
                default:
                    break;
            }
            System.out.println(startPosition);
        }

        // Движение
        public void update(float dt) {
            rotation += 2;
            switch (startPosition) {
                case 1:
                    position.x -= velocity.x * dt;
                    if(position.x < -100) {
                        position.x = ScreenManager.SCREEN_WIDTH;
                        position.y = MathUtils.random(32, ScreenManager.SCREEN_HEIGTH - 32);
                    }
                    break;
                case 2:
                    position.x -= velocity.x * dt;
                    if(position.x > ScreenManager.SCREEN_WIDTH + 100) {
                        position.x = 0;
                        position.y = MathUtils.random(32, ScreenManager.SCREEN_HEIGTH - 32);
                    }
                    break;
                case 3:
                    position.y += velocity.y * dt;
                    if(position.y > ScreenManager.SCREEN_HEIGTH + 100) {
                        position.x = MathUtils.random(32, ScreenManager.SCREEN_WIDTH - 32);
                        position.y = 0;
                    }
                    break;
                case 4:
                    position.y += velocity.y * dt;
                    if(position.y < - 100) {
                        position.x = MathUtils.random(32, ScreenManager.SCREEN_WIDTH - 32);
                        position.y = ScreenManager.SCREEN_HEIGTH + 100;
                    }
                    break;
                default:
                    break;
            }

        }
    }

    private final int ASTEROID_COUNT = 10; // Максимальное количество астероидов
    private StarGame game;
    private Texture textureAsteroid;
    private EnemyManager.Asteroid[] asteroids;

    public EnemyManager() {
        this.textureAsteroid = new Texture("asteroid.png");
        this.asteroids = new EnemyManager.Asteroid[ASTEROID_COUNT]; // заполняем массив звезд 34 - 37
        for (int i = 0; i < asteroids.length; i++) {
            asteroids[i] =  new EnemyManager.Asteroid(MathUtils.random(1, 4));
        }
    }

    public void render(SpriteBatch batch) {
        // Отрисовываем астероиды
        for (Asteroid asteroid : asteroids) {
            batch.draw(textureAsteroid, asteroid.position.x - 32, asteroid.position.y - 32, 32, 32, 64, 64,
                    1, 1, asteroid.rotation, 0, 0, 64, 64, false, false);
        }
    }

    public void update(float dt) {
        // Обновляем координаты звезд
        for (Asteroid asteroid : asteroids) {
            asteroid.update(dt);
        }
    }
}

