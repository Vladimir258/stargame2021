package com.star.app.game;

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
        this.enemyController = new EnemyController(10); // Пока количество астероидов так
        this.bulletController = new BulletController();
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        enemyController.update(dt);
        bulletController.update(dt);
        checkCollisions();
    }

    public void checkCollisions() {
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            BulletController.Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < enemyController.getActiveList().size(); j++) {
                if (enemyController.getActiveList().get(j).getPosition().dst(b.getPosition()) < 32) {
                    b.deactivate();
                    enemyController.getActiveList().get(j).deactivate();
                }
            }
        }
    }
}
