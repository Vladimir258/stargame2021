package com.star.app.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.GameController;
import com.star.app.game.WorldRender;
import com.star.app.screen.utils.Assets;

public class GameScreen  extends AbstractScreen{
    private GameController gameController;
    private WorldRender worldRender;

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    // Метод сработает когда окно с игрой станет активным
    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME); // Загрузить ресурсы для экрана GAME
        this.gameController = new GameController(batch);
        this.worldRender = new WorldRender(gameController, batch);
    }

    @Override
    public void render(float delta) {
        gameController.update(delta);
        worldRender.render();
    }

    @Override
    public void dispose() {
        gameController.dispose();
    }
}
