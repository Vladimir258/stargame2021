package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class BonusController extends ObjectPool<BonusController.Bonus> {

    class Bonus implements Poolable {
        private TextureRegion texture;
        private Vector2 position;
        private Vector2 velocity;

        private int size;
        private int type;

        private float angle; // Угол показа изображения
        private float rotationSpeed; // Скорость вращения
        private float scale; // Масштаб
        private boolean active;
        private Circle hitArea;
        private GameController gc;


        private final float BASE_SIZE = 32;
       // private final float BASE_RADIUS = BASE_SIZE / 2;

        public Circle getHitArea() {
            return hitArea;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        public Bonus(GameController gc) {
            this.gc = gc;
            this.position = new Vector2(0,0);
            this.velocity = new Vector2(0,0);
            this.hitArea = new Circle();
            active = false;

        }

        public void render(SpriteBatch batch) {
            batch.draw(texture, position.x - 16, position.y - 16, 16, 16,32,32,scale,scale,angle);
        }

        public void deactivate() {
            active = false;
        }

        public void activate(float x, float y, float vx, float vy, float scale, int size, int type) {
            this.position.set(x, y);
            this.velocity.set(vx, vy);
            this.angle = MathUtils.random(0.0f, 360.0f);
            this.rotationSpeed = MathUtils.random(-180.0f, 180.0f);
            this.hitArea.setPosition(position);

            this.scale = scale;
            this.active = true;
            this.hitArea.setRadius(BASE_SIZE * scale);

            this.size = size;
            this.type = type;
            setSizeAndType(size, type);
        }

        // Движение
        public void update(float dt) {
            position.mulAdd(velocity, dt);
            angle += rotationSpeed * dt;

            if(position.x < -200) {
                position.x = ScreenManager.SCREEN_WIDTH + 200;
            }
            if(position.x > ScreenManager.SCREEN_WIDTH + 200) {
                position.x = -200;
            }
            if(position.y < -200) {
                position.y = ScreenManager.SCREEN_HEIGTH + 200;
            }
            if(position.y > ScreenManager.SCREEN_HEIGTH + 200) {
                position.y = -200;
            }
            hitArea.setPosition(position);
        }

        public void setSizeAndType(int s, int t) {
            this.size = s;
            switch (t) {
                case 0:
                    this.texture = Assets.getInstance().getAtlas().findRegion("coin");
                    break;
                case 1:
                    this.texture = Assets.getInstance().getAtlas().findRegion("coin");
                    break;
                case 2:
                    this.texture = Assets.getInstance().getAtlas().findRegion("remkit");
                    break;
                case 3:
                    this.texture = Assets.getInstance().getAtlas().findRegion("remkit");
                    break;
                case 4:
                    this.texture = Assets.getInstance().getAtlas().findRegion("ammo");
                    break;
                case 5:
                    this.texture = Assets.getInstance().getAtlas().findRegion("ammo");
                    break;
                default:
                    break;
            }
        }

        public int getSize() {
            return this.size;
        }
        public int getType() {
            return this.type;
        }
    }



    private GameController gc;

    @Override
    protected Bonus newObject() {
        return new Bonus(this.gc);
    }

    public BonusController(GameController gc) {
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        // Отрисовываем астероиды
        for (int i = 0; i < activeList.size(); i ++) {
            Bonus b = activeList.get(i);
            b.render(batch);
        }
    }

    public void setup(float x, float y, float vx, float vy, float scale, int size, int type) {
        //TODO Исправить
        if(type < 6) { // Пока такая проверка
            getActiveElement().activate(x, y, vx, vy, scale, size, type);
        }
    }

    public void update(float dt) {
        // Обновляем координаты звезд

        for (int i = 0; i < activeList.size(); i ++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}

