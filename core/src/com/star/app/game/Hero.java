package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Hero {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity; // вектор скорости
    private float angle;
    private float enginePower;
    public float fireTimer;
    private int score; // Сколько баллов набрали
    private int scoreView; // Сколько баллов отображаем
    private int hp;
    private int hpMax;
    private Circle hitArea;
    private StringBuilder sbScore;
    private StringBuilder sbHP;
    private StringBuilder sbGameOver;
    private StringBuilder sbAmmo;
    private Weapon currentWeapon;

    private final float BASE_SIZE = 64;
    private final float BASE_RADIUS = BASE_SIZE / 2;

    public int getHp() {
        return hp;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public int getHpMax() {
        return hpMax;
    }

    public float getAngle() {
        return angle;
    }

    public void addScore(int amount) {
        this.score += amount;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("ship");
        this.position = new Vector2(640, 360);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;
        this.enginePower = 500.0f;

        this.hitArea = new Circle();
        this.hitArea.setPosition(position);
        this.hitArea.setRadius(BASE_RADIUS);

        this.hpMax = 100; // Чтоб при разбиении астероидов у следующих жизнь была меньше
        this.hp = hpMax;

        this.sbScore = new StringBuilder();
        this.sbHP = new StringBuilder();
        this.sbGameOver = new StringBuilder();
        this.sbAmmo = new StringBuilder();

        this.currentWeapon = new Weapon(
                gc, this, "Laser", 0.2f, 1,
                600, 10000,
                new Vector3[]{
                        new Vector3(28, 0, 0),
                        new Vector3(28, 70, 10),
                        new Vector3(28, -70, -10)
                }
        );
    }

    public boolean takeDamage(int amout) {
        hp -= amout;
        if(hp <= 0) {
            // уничтожение корабля
            return true;
        } else {
            return false;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1, angle);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        sbScore.clear();
        sbHP.clear();
        sbGameOver.clear();
        sbAmmo.clear();
        sbScore.append("SCORE: ").append(scoreView); // Получаем счет
        font.draw(batch, sbScore, 20, 700); // Выводим счет на экран
        sbHP.append("HP: ").append(hp); // Получаем hp корабля
        font.draw(batch, sbHP, 20, 50); // Выводим hp корабля на экран
        sbAmmo.append("Ammo: ").append(currentWeapon.getCurBullets()).append("/").append(currentWeapon.getMaxBullets()); // Получаем количество патронов
        font.draw(batch, sbAmmo, 1030, 50); // Выводим количество патронов на экран

        if(hp <= 0) {
            sbGameOver.append("Game Over");
            font.draw(batch, sbGameOver, 540, 400); //
        }
    }

    public void update(float dt) {

        this.hitArea.setPosition(position);
        updateScore(dt);
        checkSpaceBorders();
        controlShip(dt);

        if(velocity.len() > 50.0f) {
            float bxL = position.x + MathUtils.cosDeg(angle + 140) * 40;
            float byL = position.y + MathUtils.sinDeg(angle + 140) * 40;
            float bxR = position.x + MathUtils.cosDeg(angle - 140) * 40;
            float byR = position.y + MathUtils.sinDeg(angle - 140) * 40;

            for (int i = 0; i < 5; i++) {
                gc.getParticleController().setup(
                        bxL + MathUtils.random(-4,4),
                        byL + MathUtils.random(-4,4),
                        velocity.x * -0.3f + MathUtils.random(-4,4),
                        velocity.y * -0.3f + MathUtils.random(-4,4),
                        0.2f, 1.2f, 0.2f,
                        1.0f, 0.3f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 1.0f
                );
                gc.getParticleController().setup(
                        bxR + MathUtils.random(-4,4),
                        byR + MathUtils.random(-4,4),
                        velocity.x * -0.3f + MathUtils.random(-4,4),
                        velocity.y * -0.3f + MathUtils.random(-4,4),
                        0.2f, 1.2f, 0.2f,
                        1.0f, 0.3f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 1.0f
                );
            }
        }
    }

    public void updateScore(float dt) {
        if(scoreView < score) {       // эффект плавного набора скорости на экране
            scoreView += 1000 * dt;
            if(scoreView > score) {
                scoreView = score;
            }
        }
    }

    public void push(Vector2 pushVector) {
        velocity.set(pushVector);
    }

    public void checkSpaceBorders() {
        if(position.x < 32.0f) {
            position.x = 32.0f;
            velocity.x *= -1; // Отскакивание от стены
        }
        if(position.x > ScreenManager.SCREEN_WIDTH - 32.0f) {
            position.x = ScreenManager.SCREEN_WIDTH - 32.0f;
            velocity.x *= -1;
        }
        if(position.y < 32.0f) {
            position.y = 32.0f;
            velocity.y *= -1; // Отскакивание от стены
        }
        if(position.y > ScreenManager.SCREEN_HEIGTH - 32.0f) {
            position.y = ScreenManager.SCREEN_HEIGTH - 32.0f;
            velocity.y *= -1;
        }
    }

    public void controlShip(float dt) {
        fireTimer += dt;
        if(Gdx.input.isKeyPressed(Input.Keys.P)) {

            if(fireTimer > 0.2f) {
                fireTimer = 0.0f;

                currentWeapon.fire();

            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            angle += 180.0f * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            angle -= 180.0f * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += MathUtils.cosDeg(angle) * enginePower * dt;
            velocity.y += MathUtils.sinDeg(angle) * enginePower * dt;
        } else
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.x -= MathUtils.cosDeg(angle) * enginePower / 2 * dt;
            velocity.y -= MathUtils.sinDeg(angle) * enginePower / 2 * dt;
        }

        position.mulAdd(velocity,dt); // Сложить и умножить. Вместо двух строк position.x += velocity.x * dt;

        float stopKoef = 1.0f - dt; // Коэффецинет торможения
        if (stopKoef < 0) {
            stopKoef = 0;
        }
        velocity.scl(stopKoef); // scl() - Умножение вектора на скаляр
    }
}

//    add() - Сложение двух векторов
//    sub() - Вычитание векторов
//    scl() - Умножение вектора на скаляр
//    len() - Получение длины вектора
//    nor() - Нормирование вектора
//    cpy() - Копирование вектора
//    dot() - Скалярное произведение
