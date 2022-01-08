package com.pinballpro.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

/**
 * this is the class that shows the scores
 */

public class Scores  implements Screen,InputProcessor {

    private MainClass game;
    private FileHandle file;
    private String fileInput,finalResult,name;
    private String fileArray [];
    private ArrayList<String> fileContentToSave;
    private Sprite background,mainMenu;
    private BitmapFont font;
    public Scores(MainClass game){

        this.game = game;
        background = new Sprite(new Texture("background3.jpg"));
        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        mainMenu = new Sprite(new Texture("Main_Menu.png"));
        mainMenu.setSize(Gdx.graphics.getWidth()- Gdx.graphics.getWidth()/5,Gdx.graphics.getHeight()/8);
        font = new BitmapFont();
        font.setColor(0,1,.5f,1);
        font.getData().scale(2);
        name= "";
        fileInput = "";
        finalResult = "";
        fileContentToSave = new ArrayList<String>();
        file = Gdx.files.external("scores.dat");
        //reads the file with scores and saves in the araylist
        if(file.exists())
            fileInput = file.readString();
        if(fileInput.length() > 0){
            System.out.println("CONTENT IN FILE" + fileInput);
            fileArray = fileInput.split("\n");
            for(int i = 0 ; i < fileArray.length ; i++){

                fileContentToSave.add(fileArray[i]);
            }

        }
        Gdx.input.setInputProcessor(this);
        Gdx.gl.glClearColor(1, 0, 1, 1);

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //draw the scores
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        for(int i = 0 ;  i < fileContentToSave.size();i++){
            finalResult += i + 1 +". " + fileContentToSave.get(i) + "\n";
        }

        mainMenu.setPosition( 0+ mainMenu.getWidth()/8, 0);
        game.batch.begin();
        background.draw(game.batch);
        mainMenu.draw(game.batch);
        font.draw(game.batch, finalResult,- (name.length()*9),Gdx.graphics.getHeight());
        game.batch.end();
        finalResult = "";

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
    public void dispose() {

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
        //calls in the new screen
        game.setScreen(new MainMenu(game));

        this.dispose();
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
}
