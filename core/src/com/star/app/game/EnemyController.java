package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;

public class EnemyController extends ObjectPool<EnemyController.Asteroid> {

    class Asteroid implements Poolable {
        private Vector2 position;
        private Vector2 velocity;
        private int rotation;
        private int startPosition;
        private boolean active;

        public Vector2 getPosition() {
            return position;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        public Asteroid(int stPosition) {
            active = false;
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

        public void deactivate() {
            active = false;
        }

        public void activate() {
            active = true;
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

    private Texture textureAsteroid;

    @Override
    protected Asteroid newObject() {
        return new Asteroid(MathUtils.random(1, 4));
    }

    public EnemyController(int maxAsteroid) {
        this.textureAsteroid = new Texture("asteroid.png");
        for (int i = 0; i < maxAsteroid; i++) {
            this.setup();
        }
    }

    public void render(SpriteBatch batch) {
        // Отрисовываем астероиды
        for (int i = 0; i < activeList.size(); i ++) {
            batch.draw(textureAsteroid, activeList.get(i).position.x - 32, activeList.get(i).position.y - 32, 32, 32, 64, 64,
                    1, 1, activeList.get(i).rotation, 0, 0, 64, 64, false, false);
        }
    }


    public void setup() {
        getActiveElement().activate();
    }

    public void update(float dt) {
        // Обновляем координаты звезд

        for (int i = 0; i < activeList.size(); i ++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}

