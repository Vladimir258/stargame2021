package com.star.app.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.StarGame;
import com.star.app.screen.ScreenManager;

public class Background {
    // Звезды часть bg, поэтому создадим внутренний класс звезда в классе bg
    private class Star{
        private Vector2 position;
        private Vector2 velocity;
        float scale;

        public Star() {
            //  В конструкторе рандомно создаем звезду, координаты и скорость с направлением
            this.position = new Vector2(MathUtils.random(-200, ScreenManager.SCREEN_WIDTH + 200), MathUtils.random(-200, ScreenManager.SCREEN_HEIGTH + 200));
            this.velocity = new Vector2(MathUtils.random(-40, -5), 0);
            this.scale = Math.abs(velocity.x) / 40.0f * 0.8f;// Задаем размер звезд
        }
        // Движение звезд
        public void update(float dt) {
            position.x += (velocity.x - gc.getHero().getVelocity().x * 0.1) * dt;
            position.y += (velocity.y - gc.getHero().getVelocity().y * 0.1) * dt;

            // Если звезда зашла за экран слева, перемещаем ее за экран справа
            // чтоб массив звезд циклично бегал от начала к концу, бессконечное звездное пространство
            if(position.x < -200) {
                position.x = ScreenManager.SCREEN_WIDTH + 200;
                position.y = MathUtils.random(-200, ScreenManager.SCREEN_HEIGTH + 200);
                scale = Math.abs(velocity.x) / 40.0f * 0.8f; // Задаем размер звезд
            }
        }
    }

    private final int STAR_COUNT = 1000; // Максимальное количество звезд
    private GameController gc;
    private Texture textureCosmos;
    private Texture textureStar;
    private Star[] stars;

    public Background(GameController gc) {
        this.textureCosmos = new Texture("bg.png");
        this.textureStar = new Texture("star16.png");
        this.gc = gc;
        this.stars = new Star[STAR_COUNT]; // заполняем массив звезд 34 - 37
        for (int i = 0; i < stars.length; i++) {
            stars[i] =  new Star();
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(textureCosmos, 0, 0);
        // Отрисовываем звезды
        for (Star star : stars) {
            batch.draw(textureStar, star.position.x - 8, star.position.y - 8, 8, 8,
                    16, 16, star.scale, star.scale, 0, 0, 0, 16, 16, false, false);

//            // Мерцание звезд
//            if (MathUtils.random(0, 1000) < 1) {
//                batch.draw(textureStar, stars[i].position.x - 8, stars[i].position.y - 8, 8, 8,
//                        16, 16, stars[i].scale * 2, stars[i].scale * 2, 0, 0, 0, 16, 16, false, false);
//            }
        }
    }

    public void update(float dt) {
        // Обновляем координаты звезд
        for (Star star : stars) {
            star.update(dt);
        }
    }
}
