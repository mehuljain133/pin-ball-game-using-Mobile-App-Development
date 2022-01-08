package com.pinballpro.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * main menu class that deals with the main menu buttons
 */

public class MainMenu   implements Screen, InputProcessor {
    //SpriteBatch batch;
    private Sprite playButton,exitButton,optionsButton;
    private Sprite background;
    MainClass game ;

    public MainMenu(MainClass mainClass) {
        this.game = mainClass;
        game.batch = new SpriteBatch();
        background = new Sprite(new Texture("background3.jpg"));
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        playButton = new Sprite(new Texture("Play1.png"));
        playButton.setSize(Gdx.graphics.getWidth()- Gdx.graphics.getWidth()/5,Gdx.graphics.getHeight()/8);
        optionsButton = new Sprite(new Texture("Scores.png"));
        optionsButton.setSize(Gdx.graphics.getWidth()- Gdx.graphics.getWidth()/5,Gdx.graphics.getHeight()/8);
        exitButton = new Sprite(new Texture("Quit1.png"));
        exitButton.setSize(Gdx.graphics.getWidth()- Gdx.graphics.getWidth()/5,Gdx.graphics.getHeight()/8);

        Gdx.input.setInputProcessor(this);
        Gdx.gl.glClearColor(1, 0, 1, 1);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //some calculations for findind the positions of the buttons on different screen sizes
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        background.draw(game.batch);
        playButton.setPosition(  Gdx.graphics.getWidth()/2 - (playButton.getWidth()/2 ) , (Gdx.graphics.getHeight()/3)*3- (playButton.getWidth()/2 ));
        playButton.draw(game.batch);

        optionsButton.setPosition(Gdx.graphics.getWidth()/2 - (optionsButton.getWidth()/2 ), (Gdx.graphics.getHeight()/3)*2- (playButton.getWidth() /2));
        optionsButton.draw(game.batch);

        exitButton.setPosition(Gdx.graphics.getWidth()/2 - (exitButton.getWidth()/2 ), (Gdx.graphics.getHeight()/3)- (playButton.getWidth()/2));
        exitButton.draw(game.batch);

        game.batch.end();


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }



    @Override
    public boolean keyDown(int keycode) {


        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //this here is the calculation to find wich button did the user select since i didn`t use generic buttons from android
        int xMin = (int)( Gdx.graphics.getWidth()/2 - (playButton.getWidth()/2 ));
        int xMax = (int)( Gdx.graphics.getWidth()/2 + (playButton.getWidth()/2 ));
        if( screenX > xMin && screenX< xMax && screenY > (Gdx.graphics.getHeight()/3)- (playButton.getWidth()/2) &&  screenY < (Gdx.graphics.getHeight()/3)- (playButton.getWidth()/2) + (playButton.getHeight()) )
        {
            this.dispose();
            game.setScreen(new MainGame(game));

        }
        else if(screenX > xMin &&screenX < xMax &&  screenY >(Gdx.graphics.getHeight()/3) *2 - (playButton.getWidth()/2)&&  screenY < (Gdx.graphics.getHeight()/3)*2 +(playButton.getHeight()))
        {
            this.dispose();
            game.setScreen(new Scores(game));
        }
        else if(screenX > xMin && screenX < xMax &&  screenY > (Gdx.graphics.getHeight()/3)*3 - (playButton.getWidth()/2) &&  screenY < (Gdx.graphics.getHeight()/3)*3 - (playButton.getWidth()/2) + (playButton.getHeight()) )
        {
            Gdx.app.exit();
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
    @Override
    public void dispose () {

        playButton.getTexture().dispose();
        optionsButton.getTexture().dispose();
        exitButton.getTexture().dispose();
    }
}
