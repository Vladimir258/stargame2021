package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class GameController {
    private Background background; // Наш бэкграунд
    private Hero hero;
    private EnemyController enemyController;
    private BulletController bulletController;
    private ParticleController particleController;
    private BonusController bonusController;
    private Stage stage;
    private int level;
    private float roundTimer;
    private Music music;

    public float getRoundTimer() {
        return roundTimer;
    }

    public int getLevel() {
        return level;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public Hero getHero() {
        return hero;
    }

    public Background getBackground() {
        return background;
    }

    public Stage getStage() {
        return stage;
    }

    public EnemyController getEnemyManager() {
        return enemyController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public BonusController getBonusController() {
        return bonusController;
    }


    public GameController(SpriteBatch batch) {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.enemyController = new EnemyController(this);
        this.bulletController = new BulletController(this);
        this.bonusController = new BonusController(this);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.stage.addActor(hero.getShop());
        this.stage.addActor(hero.getGamePause());
        Gdx.input.setInputProcessor(stage);
        this.particleController = new ParticleController();
        this.level = 1;
        this.roundTimer = 0.0f;
        this.music = Assets.getInstance().getAssetManager().get("audio/mortal.mp3");
        this.music.setLooping(true);
        this.music.play();
        generateBigAsteroids(1);
    }

    // Метод включения следюущего уровня
    private void generateBigAsteroids(int count) {
        for (int i = 0; i < count; i++) {
            enemyController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH),
                    MathUtils.random(0, ScreenManager.SCREEN_HEIGTH),
                    MathUtils.random(-200,200),MathUtils.random(-200,200), 1.0f);
        }
    }

    public void update(float dt) {
        if (!hero.isPause()) {

            roundTimer += dt;
            background.update(dt);
            enemyController.update(dt);
            bulletController.update(dt);
            particleController.update(dt);
            bonusController.update(dt);
            checkCollisions();
            hero.update(dt);
            if (!hero.isAlive()) {  // У коробля отказывает управление (еще дым прикрутить)
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER, hero);
            }
            // Если закончились астероиды
            if(enemyController.getActiveList().size() == 0) {
                level++;
                // (level <= 3 ? level : 3) вместо просто (level), чтоб после 3его уровня не появлялось больше 3х астероидов
                generateBigAsteroids(level <= 3 ? level : 3);
                roundTimer = 0.0f;
            }
            stage.act(dt);
        }
    }

    public void checkCollisions() {
        // Столкновение корабля с бонусом
        for (int i = 0; i < bonusController.getActiveList().size(); i++) {
            BonusController.Bonus a = bonusController.getActiveList().get(i);
            // TODO непойму почему hero переодически скачет от бобнуса к бонусу, получилось забавно
            // TODO но где-то ошибка, может метод dst() заменить в comeToHero. Подумаю
           if(hero.comeToHero(a)) {
              // a.setPosition(hero.getPosition());
               // Слежение за мышкой делается подобно
               Vector2  tmpVec = new Vector2().set(hero.getPosition()).sub(a.getPosition()).nor();
               a.getVelocity().mulAdd(tmpVec, 200.0f);
           };

            if(a.getHitArea().contains(hero.getPosition())) { // При столкновении с астероидом
                hero.useBonus(a.getSize(),a.getType()); // При столкновении с бонусом кораблю перепадают бонусы)))

                for (int j = 0; j < 16; j++) {
                    float angle = 6.28f / 16.0f * j;
                    particleController.setup(
                            hero.getPosition().x + MathUtils.random(-4, 4),
                            hero.getPosition().y + MathUtils.random(-4, 4),
                            (float) Math.cos(angle) * 100,
                            (float) Math.sin(angle) * 100,
                            0.8f, 3.0f, 1.8f,
                            0.0f, 1.0f, 0.0f, 1.0f,
                            1.0f, 1.0f, 1.0f, 0.5f
                    );
                }
                a.deactivate(); //
                break;
            }
        }

        // Столкновение астероида с пулей
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
        // Столкновение корабля с астероидом
        for (int i = 0; i < enemyController.getActiveList().size(); i++) {  // Урон для коробля
            EnemyController.Asteroid a = enemyController.getActiveList().get(i);
            if(a.getHitArea().contains(hero.getPosition())) { // При столкновении с астероидом
                hero.takeDamage(level * 2 + a.getHpMax()); // корабль получает урон равный жизньАстероида
                hero.push(a.getVelocity()); // при столкновении с астероидом нас отбрасывает на силу равную ускорению астероида
                a.takeDamage(a.getHpMax()); // астероид уничтожается
                break;
            }
        }
    }

    public  void dispose() {
        background.dispose();
    }
}
