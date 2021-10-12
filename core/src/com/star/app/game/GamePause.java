package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class GamePause extends Group {
    private BitmapFont font32;
    private Hero hero;

    public GamePause(Hero hr) {
        this.hero = hr;
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf");

        Pixmap pixmap = new Pixmap(400, 400, Pixmap.Format.RGB888); // Задний фон магазина
        pixmap.setColor(Color.rgb888(0.0f, 0.5f, 0.0f));
        pixmap.fill();

        Image image = new Image(new Texture(pixmap));
        this.addActor(image);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("smallButton");
        textButtonStyle.font = font32;
        skin.add("smallButton", textButtonStyle);

        final GamePause thisPause = this; // Хитрость с this для addListener'а
        final TextButton btnClose = new TextButton("X", textButtonStyle);
        btnClose.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                thisPause.setVisible(false);
                thisPause.hero.setPause(false);
            }
        });
        btnClose.setTransform(true);
        btnClose.setScale(0.5f);
        btnClose.setPosition(380, 380);
        this.addActor(btnClose);


        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font32;
        skin.add("simpleButton", textButtonStyle);

        final TextButton btnResume = new TextButton("Resume", textButtonStyle);
        btnResume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                thisPause.setVisible(false);
                thisPause.hero.setPause(false);
            }
        });
        btnResume.setPosition(30, 300);
        this.addActor(btnResume);


        final TextButton btnOptions = new TextButton("Options", textButtonStyle);
        btnOptions.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                thisPause.setVisible(false);
                thisPause.hero.setPause(false);
            }
        });
        btnOptions.setPosition(30, 210);
        this.addActor(btnOptions);


        final TextButton btnNewGame = new TextButton("New Game", textButtonStyle);
        btnNewGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });
        btnNewGame.setPosition(30, 120);
        this.addActor(btnNewGame);


        final TextButton btnExit = new TextButton("Exit", textButtonStyle);
        btnExit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
        btnExit.setPosition(30, 30);
        this.addActor(btnExit);





        this.setPosition(20, 20);
        this.setVisible(false);
        skin.dispose();
    }

}
