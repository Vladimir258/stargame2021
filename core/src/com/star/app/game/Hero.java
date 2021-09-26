package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;

public class Hero {
    private GameController gc;
    private Texture texture;
    private Vector2 position;
    private Vector2 velocity; // вектор скорости
    private float angel;
    private float enginePower;
    public float fireTimer;

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Hero(GameController gc) {
        this.gc = gc;
        this.texture = new Texture("ship.png");
        this.position = new Vector2(640, 360);
        this.velocity = new Vector2(0, 0);
        this.angel = 0.0f;
        this.enginePower = 500.0f;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1, angel,
                0,0,64,64,false,false);
    }

    public void update(float dt) {

        fireTimer += dt;
        if(Gdx.input.isKeyPressed(Input.Keys.P)) {
            if(fireTimer > 0.2f) {
                fireTimer = 0.0f;
                gc.getBulletController().setup(position.x, position.y,
                        MathUtils.cosDeg(angel) * 500,
                        MathUtils.sinDeg(angel) * 500); // Пока пуля вылетает из центра корабля
            }
        }


        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            angel += 180.0f * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            angel -= 180.0f * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            velocity.x += MathUtils.cosDeg(angel) * enginePower * dt;
            velocity.y += MathUtils.sinDeg(angel) * enginePower * dt;
        } else
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            velocity.x -= MathUtils.cosDeg(angel) * enginePower / 2 * dt;
            velocity.y -= MathUtils.sinDeg(angel) * enginePower / 2 * dt;
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
