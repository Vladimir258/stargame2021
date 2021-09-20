package com.star.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Hero {
    private Texture texture;
    private Vector2 position;
    private Vector2 lastDisplacement;
    private float angel;

    public Vector2 getLastDisplacement() {
        return lastDisplacement;
    }

    public Hero() {
        this.texture = new Texture("ship.png");
        this.position = new Vector2(640, 360);
        this.lastDisplacement = new Vector2(0, 0);
        this.angel = 0.0f;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1, angel,
                0,0,64,64,false,false);
    }

    public void update(float dt) {
        if(Gdx.input.isKeyPressed(Input.Keys.A)) {
            angel += 180.0f * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) {
            angel -= 180.0f * dt;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.x += MathUtils.cosDeg(angel) * 240.0f * dt;
            position.y += MathUtils.sinDeg(angel) * 240.0f * dt;
            lastDisplacement.set( MathUtils.cosDeg(angel) * 240.0f * dt, MathUtils.sinDeg(angel) * 240.0f * dt);
        } else
        if(Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.x -= MathUtils.cosDeg(angel) * 120.0f * dt;
            position.y -= MathUtils.sinDeg(angel) * 120.0f * dt;
            lastDisplacement.set( MathUtils.cosDeg(angel) * -120.0f * dt, MathUtils.sinDeg(angel) * -120.0f * dt);
        } else {
            lastDisplacement.set(0, 0);
        }

        if(position.x < 32.0f) {
            position.x = 32.0f;
        }
        if(position.x > ScreenManager.SCREEN_WIDTH - 32.0f) {
            position.x = ScreenManager.SCREEN_WIDTH - 32.0f;
        }
        if(position.y < 32.0f) {
            position.y = 32.0f;
        }
        if(position.y > ScreenManager.SCREEN_HEIGTH - 32.0f) {
            position.y = ScreenManager.SCREEN_HEIGTH - 32.0f;
        }
    }
}
