package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.star.app.screen.ScreenManager;

public class GameController {
    private Background background; // Наш бэкграунд
    private Hero hero;
    private EnemyController enemyController;
    private BulletController bulletController;

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

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.enemyController = new EnemyController(this);
        this.bulletController = new BulletController();
        for (int i = 0; i < 3; i++) {
            enemyController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGTH),
                    MathUtils.random(-200,200),MathUtils.random(-200,200), 1.0f);
        }
    }

    public void update(float dt) {
        background.update(dt);
        if(hero.getHp() > 0) {  // У коробля отказывает управление (еще дым прикрутить)
            hero.update(dt);
        }
        enemyController.update(dt);
        bulletController.update(dt);
        checkCollisions();
    }

    public void checkCollisions() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            BulletController.Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < enemyController.getActiveList().size(); j++) {
//                if (enemyController.getActiveList().get(j).getPosition().dst(b.getPosition()) < 32) {
//                    b.deactivate();
//                    enemyController.getActiveList().get(j).deactivate();
//                }
                EnemyController.Asteroid a = enemyController.getActiveList().get(j);
                if(a.getHitArea().contains(b.getPosition())) {
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
                hero.takeDamage(a.getHpMax()); // корабль получает урон равный жизньАстероида
                hero.push(a.getVelocity()); // при столкновении с астероидом нас отбрасывает на силу равную ускорению астероида
                a.takeDamage(a.getHpMax()); // астероид уничтожается
                break;
            }
        }

    }
}
