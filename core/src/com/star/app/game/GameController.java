package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.star.app.screen.ScreenManager;

public class GameController {
    private Background background; // Наш бэкграунд
    private Hero hero;
    private EnemyController enemyController;
    private BulletController bulletController;
    private ParticleController particleController;

    public BulletController getBulletController() {
        return bulletController;
    }

    public Hero getHero() {
        return hero;
    }

    public Background getBackground() {
        return background;
    }

    public EnemyController getEnemyManager() {
        return enemyController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }



    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.enemyController = new EnemyController(this);
        this.bulletController = new BulletController(this);
        this.particleController = new ParticleController();
        for (int i = 0; i < 3; i++) {
            enemyController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGTH),
                    MathUtils.random(-200,200),MathUtils.random(-200,200), 1.0f);
        }
    }

    public void update(float dt) {
        background.update(dt);
        enemyController.update(dt);
        bulletController.update(dt);
        particleController.update(dt);
        checkCollisions();
        if(hero.getHp() > 0) {  // У коробля отказывает управление (еще дым прикрутить)
            hero.update(dt);
        }
    }

    public void checkCollisions() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            BulletController.Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < enemyController.getActiveList().size(); j++) {
                EnemyController.Asteroid a = enemyController.getActiveList().get(j);
                if(a.getHitArea().contains(b.getPosition())) {

                    particleController.setup(
                            b.getPosition().x + MathUtils.random(-4, 4),
                            b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30),
                            b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f, 2.3f, 1.7f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 0.0f
                    );

                    b.deactivate();
                    if(a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                    }
                    break; // Выходим чтоб не сбить еще астероиды
                }
            }
        }

        for (int i = 0; i < enemyController.getActiveList().size(); i++) {  // Урон для коробля
            EnemyController.Asteroid a = enemyController.getActiveList().get(i);
            if(a.getHitArea().contains(hero.getPosition())) { // При столкновении с астероидом

//                particleController.setup(
//                        hero.getPosition().x + MathUtils.random(-14, 14),
//                        hero.getPosition().y + MathUtils.random(-14, 14),
//                        hero.getVelocity().x + MathUtils.random(-130, 130),
//                        hero.getVelocity().y + MathUtils.random(-130, 130),
//                        0.2f, 2.3f, 1.7f,
//                        1.0f, 1.0f, 1.0f, 1.0f,
//                        0.0f, 0.0f, 1.0f, 1.0f
//                );

                hero.takeDamage(a.getHpMax()); // корабль получает урон равный жизньАстероида
                hero.push(a.getVelocity()); // при столкновении с астероидом нас отбрасывает на силу равную ускорению астероида
                a.takeDamage(a.getHpMax()); // астероид уничтожается
                break;
            }
        }

    }
}
