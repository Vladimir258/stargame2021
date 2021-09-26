package com.star.app.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.GameController;
import com.star.app.game.WorldRender;

public class GameScreen  extends AbstractScreen{

    private SpriteBatch batch;
    private GameController gameController;
    private WorldRender worldRender;

    public GameScreen(SpriteBatch batch) {
        this.batch = batch;
    }

    // Метод сработает когда окно с игрой станет активным
    @Override
    public void show() {
        this.gameController = new GameController();
        this.worldRender = new WorldRender(gameController, batch);
    }

    @Override
    public void render(float delta) {
        gameController.update(delta);
        worldRender.render();

    }
}
