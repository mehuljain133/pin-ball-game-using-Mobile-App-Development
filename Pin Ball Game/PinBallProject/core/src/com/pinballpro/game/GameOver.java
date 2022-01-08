package com.pinballpro.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.files.FileHandle;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * this is the game over screen that shows the new and ols scores
 */

public class GameOver  implements Screen,InputProcessor {


    private Texture playButton,exitButton,optionsButton;
    MainClass game ;
    private String name,initialT ,finalResult,fileInput,fileOutput;
    private BitmapFont font;
    private int totalScore ;
    private Sprite background,mainMenu;
    private OrthographicCamera cam;
    private FileHandle file;
    private String fileArray [];
    private ArrayList<String> fileContentToSave;
    private boolean added;
    public GameOver(MainClass game, final int totalScore){
        this.game = game;
        cam = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        fileContentToSave = new ArrayList<String>();
        fileInput = "";
        finalResult = "";
        added = false;
        font = new BitmapFont();
        font.setColor(0,1,.5f,1);
        font.getData().scale(2);
        name= "";
        background = new Sprite(new Texture("gameOver.jpg"));

        background.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        background.setCenter(0.2f,0.2f);
        background.setOriginCenter();
        mainMenu = new Sprite(new Texture("Main_Menu.png"));
        mainMenu.setSize(Gdx.graphics.getWidth()- Gdx.graphics.getWidth()/5,Gdx.graphics.getHeight()/8);
        this.totalScore = totalScore;
        //Gdx.input.setOnscreenKeyboardVisible(true);
       // MyTextInputListener listener = new MyTextInputListener();
        //this method is used to get the user name to save in the data base
        Gdx.input.getTextInput(new Input.TextInputListener() {
            @Override
            public void input(String text) {
                name = text ;
                name =  name + " : " + totalScore  ;
                if(!added){
                    fileContentToSave.add(name);
                    added = true;
                    sort();
                }

            }

            @Override
            public void canceled() {

            }
        }, "Name",initialT, "Enter Name");
        //this piece of code looks for the file if its the it reads if its not it creates one
        //it also saves the score from file into an arraylist
        file = Gdx.files.external("scores.dat");
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

        Gdx.gl.glClearColor(0, 0, 0, 1);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        //draw the information on screen
        //and gets the scores for the arraylist
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(cam.combined);

        for(int i = 0 ;  i < fileContentToSave.size();i++){
            finalResult += i + 1 +". " + fileContentToSave.get(i) + "\n";
        }
        mainMenu.setPosition( - (mainMenu.getWidth()/2 ), -(Gdx.graphics.getHeight()/2));
        game.batch.begin();
        background.draw(game.batch);
        mainMenu.draw(game.batch);
        font.draw(game.batch, finalResult,- (name.length()*9),300);
        game.batch.end();
        finalResult = "";
        //finalResult = name + ":" + totalScore ;


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

        //this is run when we exit the scene and it saves all the old and new scores into the data base file
        OutputStream out = null;

        try{

            out = file.write(false);
            System.out.println("TRYING TO SAVE");
            String temp = "";
            out.write(temp.getBytes());
            for(int i = 0 ; i < 10 && fileContentToSave.size() > 0 && i < fileContentToSave.size() ; i++){
                System.out.println("SAVED IN FILE" + fileContentToSave.get(i));
                temp = fileContentToSave.get(i) + "\n";
                out.write(temp.getBytes());


            }
            out.close();
        }catch(Exception ex){ System.out.println(ex);}
        finally {
            if(out != null)
                try{out.close();}catch (Exception ex){System.out.println(ex);}
        }


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
        //calls in the main menu screen
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
    private void sort(){
        //this method is a bubble sort method to sort the arraylist on decending order based on scores
        String temp = "";
        int temp1 =0 ,temp2 = 0;

        for(int i = 0 ; i < fileContentToSave.size() ; i++ ){
            String tempArray1[],tempArray2[];
            for(int k = 0 ; k <  fileContentToSave.size() -1 ;k++){

                tempArray1 = fileContentToSave.get(k).split(":");
                temp1 = Integer.parseInt(tempArray1[1].trim());
                tempArray2 =  fileContentToSave.get(k+1).split(":");
                temp2 = Integer.parseInt(tempArray2[1].trim());
                if(temp1 < temp2){
                    temp = fileContentToSave.get(k );
                    fileContentToSave.set(k, fileContentToSave.get(k+1));
                    fileContentToSave.set(k+1, temp);

                }
            }
        }
    }
}
