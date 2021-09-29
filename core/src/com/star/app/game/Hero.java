package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
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

    public int getScore() {
        return score;
    }

    public int getScoreView() {
        return scoreView;
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
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1, angle);
    }

    public void update(float dt) {

        fireTimer += dt;

        if(scoreView < score) {       // эффект плавного набора скорости на экране
            scoreView += 1000 * dt;
            if(scoreView > score) {
                scoreView = score;
            }
        }

        if(Gdx.input.isKeyPressed(Input.Keys.P)) {
            if(fireTimer > 0.2f) {
                fireTimer = 0.0f;

                float wx;
                float wy;

                wx = position.x + MathUtils.cosDeg(angle + 90) * 25; // Место вылета пули x
                wy = position.y + MathUtils.sinDeg(angle + 90) * 25; // Место вылета пули y

                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * 500,
                        MathUtils.sinDeg(angle) * 500); // Пока пуля вылетает из центра корабля

                wx = position.x + MathUtils.cosDeg(angle - 90) * 25; // Место вылета пули x
                wy = position.y + MathUtils.sinDeg(angle - 90) * 25; // Место вылета пули y

                gc.getBulletController().setup(wx, wy,
                        MathUtils.cosDeg(angle) * 500,
                        MathUtils.sinDeg(angle) * 500); // Пока пуля вылетает из центра корабля
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
        float stopKoef = 1.0f - 1.0f * dt; // Коэффецинет торможения

        if (stopKoef < 0) {
            stopKoef = 0;
        }
        velocity.scl(stopKoef); // scl() - Умножение вектора на скаляр

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
}

//    add() - Сложение двух векторов
//    sub() - Вычитание векторов
//    scl() - Умножение вектора на скаляр
//    len() - Получение длины вектора
//    nor() - Нормирование вектора
//    cpy() - Копирование вектора
//    dot() - Скалярное произведение
