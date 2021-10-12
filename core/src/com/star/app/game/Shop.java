package com.star.app.game;

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
import com.star.app.screen.utils.Assets;

public class Shop extends Group {
    private Hero hero;
    private BitmapFont font24;

    public Shop(Hero hr) {
        this.hero = hr;
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");

        Pixmap pixmap = new Pixmap(400, 400, Pixmap.Format.RGB888); // Задний фон магазина
        pixmap.setColor(Color.rgb888(0.0f, 0.0f, 0.5f));
        pixmap.fill();

        Image image = new Image(new Texture(pixmap));
        this.addActor(image);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("smallButton");
        textButtonStyle.font = font24;
        skin.add("smallButton", textButtonStyle);


        final TextButton btnHpMax = new TextButton("Max HP add", textButtonStyle);
        btnHpMax.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(hero.isMoneyEnough(Hero.Skill.HP_MAX.cost)) { // Если есть денег равных стоимости HP_MAX
                    hero.upgrade(Hero.Skill.HP_MAX);
                    hero.decreaseMoney(Hero.Skill.HP_MAX.cost);
                }
            }
        });
        btnHpMax.setPosition(30, 340);
        this.addActor(btnHpMax);


        final TextButton btnHpAdd = new TextButton("HP add", textButtonStyle);
        btnHpAdd.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(hero.isMoneyEnough(Hero.Skill.HP.cost)) { // Если есть денег равных стоимости HP_MAX
                    hero.upgrade(Hero.Skill.HP);
                    hero.decreaseMoney(Hero.Skill.HP.cost);
                }
            }
        });
        btnHpAdd.setPosition(30, 300);
        this.addActor(btnHpAdd);


        final TextButton btnUpWPN = new TextButton("Weapon upgrade", textButtonStyle);
        btnUpWPN.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(hero.isMoneyEnough(Hero.Skill.WEAPON.cost)) { // Если есть денег равных стоимости HP_MAX
                    hero.upgrade(Hero.Skill.WEAPON);
                    hero.decreaseMoney(Hero.Skill.WEAPON.cost);
                }
            }
        });
        btnUpWPN.setPosition(30, 260);
        this.addActor(btnUpWPN);


        final TextButton btnClose = new TextButton("X", textButtonStyle);
        final Shop thisShop = this; // Хитрость с this для addListener'а
        btnClose.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                thisShop.setVisible(false);
                thisShop.hero.setPause(false);
            }
        });
        btnClose.setTransform(true);
        btnClose.setScale(0.5f);
        btnClose.setPosition(380, 380);
        this.addActor(btnClose);


        this.setPosition(20, 20);
        this.setVisible(false);
        skin.dispose();
    }
}
