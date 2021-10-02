package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.ObjectPool;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class BulletController extends ObjectPool<BulletController.Bullet> {

    class Bullet implements Poolable {
        private Vector2 position;
        private Vector2 velocity;
        private boolean active;

        public Vector2 getPosition() {
            return position;
        }

        public Vector2 getVelocity() {
            return velocity;
        }

        @Override
        public boolean isActive() {
            return active;
        }

        public Bullet() {
            this.position = new Vector2(0, 0);
            this.velocity = new Vector2(0, 0);
            this.active = false;
        }

        public void deactivate() {
            active = false;
        }

        public void activate(float x, float y, float vx, float vy) {
            position.set(x, y);
            velocity.set(vx, vy);
            active = true;
        }

        public void update(float dt) {
            position.mulAdd(velocity, dt);

            gc.getParticleController().setup(
                    position.x + MathUtils.random(-4, 4),
                    position.y + MathUtils.random(-4, 4),
                    velocity.x * -0.3f + MathUtils.random(-4, 4),
                    velocity.y * -0.3f + MathUtils.random(-4, 4),
                    0.1f, 2f, 0.2f,
                    0.0f, 1.0f, 0.1f, 1.0f,
                    1.0f, 1.0f, 1.0f, 1.0f
            );

            if (position.x < -20 || position.x > ScreenManager.SCREEN_WIDTH + 20 ||
                    position.y < -20 || position.y > ScreenManager.SCREEN_HEIGTH + 20) {
                deactivate();
            }
        }
    }

    private GameController gc;
    private TextureRegion bulletTexture;

    @Override
    protected Bullet newObject() {
        return new Bullet();
    }

    public BulletController(GameController gc) {
        this.bulletTexture = Assets.getInstance().getAtlas().findRegion("bullet");
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            BulletController.Bullet  b = activeList.get(i);
            batch.draw(bulletTexture, b.getPosition().x - 16, b.getPosition().y - 16);
        }
    }
    // Активация пули
    public void setup(float x, float y, float vx, float vy) {
        getActiveElement().activate(x, y, vx, vy);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
