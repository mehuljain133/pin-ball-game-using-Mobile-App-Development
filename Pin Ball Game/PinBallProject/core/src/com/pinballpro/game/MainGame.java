package com.pinballpro.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;

/**
 * this class is the class encharged of all the game mechanics
 */

public class MainGame implements Screen,InputProcessor, ContactListener {
    MainClass game;
    private World world ;
    private Box2DDebugRenderer debuger ;
    private OrthographicCamera cam;
    private Body ball,leftFlipper,rightFlipper,leftWall,rightWall,topWall ;
    private Body bottomH,sideH;
    private Body rightRoundP1, rightRoundP2, rightRoundP3, rightRoundP4,rightRoundP5;
    private Body leftRoundP1, leftRoundP2, leftRoundP3, leftRoundP4,leftRoundP5;
    private Body leftTriangleP1,leftTriangleP2,leftTriangleP3;
    private Body rightTriangleP1,rightTriangleP2,rightTriangleP3;
    private Body leftBottomP1,leftBottomP2;
    private Body rightBottomP1;
    private Body cBumper1,cBumper2,cBumper3;
    private Body routers1P1,routers1P2,routers2P1,routers2P2;
    private Sprite ballSprite,background,bumper1sprite,bumper2sprite,bumper3sprite,leftStickSpr,rightStickSpr ,topPiece,quitButtom;
    private Array<Body> tmpBodies = new Array<Body>();
    private boolean ballLauched,dragging ,paused;
    private BitmapFont font;
    private String str = "",str2 = "";
    private int totalScore,lives;
    private float camW ,camH;
    private Sound flipperSR,flipperSI ,triangleS,routerS ,bumperS,ballRechargeS,ballLauchS;

    public MainGame(MainClass game){

        world = new World(new Vector2(0, -10.0f), true);
        debuger = new Box2DDebugRenderer();
        // W = 80 H = 142 // these are the basic width and hieght i need to zoom in

        camW = Gdx.graphics.getWidth()/80;
        camH = Gdx.graphics.getHeight()/142 ;

        cam = new OrthographicCamera(Gdx.graphics.getWidth()/camW,Gdx.graphics.getHeight()/camH);
        totalScore = 0;
        lives = 3;
        str2 = "Balls:" + lives ;
        paused = false;
        //=========================initialising all the sprites necessary======================
        background = new Sprite(new Texture("background3.jpg"));
        background.setSize(Gdx.graphics.getWidth()/camW,Gdx.graphics.getHeight()/camH);
        float w = 0.5f;
        float h = 0.5f;
        background.setCenter(w,h);
        background.setOriginCenter();
        topPiece =  new Sprite(new Texture("topPiece.png"));
        topPiece.setSize(Gdx.graphics.getWidth()/camW,10.8f);
        topPiece.setCenter(0.5f,0.5f);
        topPiece.setOriginCenter();
        quitButtom = new Sprite(new Texture("Quit1.png"));
        quitButtom.setSize(21.6f,10.8f);
        quitButtom.setCenter(0.5f,0.5f);
        quitButtom.setOriginCenter();

        bumper1sprite = new Sprite(new Texture("bigball.png"));
        bumper1sprite.setSize(16,16);
        bumper1sprite.setCenter(w,h);
        bumper1sprite.setOriginCenter();

        bumper2sprite = new Sprite(new Texture("bigball.png"));
        bumper2sprite.setSize(8,8);

        bumper3sprite = new Sprite(new Texture("bigball.png"));
        bumper3sprite.setSize(8,8);

        leftStickSpr = new Sprite(new Texture("stickLeft.png"));
        leftStickSpr.setSize(10,10);

        rightStickSpr = new Sprite(new Texture("stickRight.png"));
        rightStickSpr.setSize(10,10);

        ballSprite = new Sprite(new Texture("ball21.png"));

        ballSprite.setSize(4,4);
        ballSprite.setCenter(0.5f,0.5f);
        ballSprite.setOriginCenter();

//================setting up the audios files=========================================================
        flipperSI = Gdx.audio.newSound(Gdx.files.internal("inguageFlipper.wav"));
        flipperSR = Gdx.audio.newSound(Gdx.files.internal("releaseFipper.wav"));
        triangleS =  Gdx.audio.newSound(Gdx.files.internal("triangle.wav"));
        routerS = Gdx.audio.newSound(Gdx.files.internal("router.wav"));
        bumperS = Gdx.audio.newSound(Gdx.files.internal("bumpers1.wav"));
        ballRechargeS = Gdx.audio.newSound(Gdx.files.internal("ballRecharge.wav"));
        ballLauchS = Gdx.audio.newSound(Gdx.files.internal("ballLauch.wav"));

        font = new BitmapFont();
        font.getData().setScale( 0.25f, 0.25f);
        font.setColor(0,0,0,1);
        this.game = game ;
        this.world.setContactListener(this);
        //=================creating all my collision objects=========================================
        ball = GameObjects.createCircle(world,36.5f,-55,2,false);
        ballLauched = false ;
        ball.setBullet(true);
        ball.setUserData(ballSprite);

        //making the flippers
        leftFlipper = GameObjects.createWall(world,-22,-50,-10,-51,0f,0f,6,0f);
        leftFlipper.setAngularVelocity(-10);

       // leftFlipper
        rightFlipper = GameObjects.createWall(world,10,-50,22,-51,0f,0f,-6,0f);
        rightFlipper.setAngularVelocity(10);

        //making the walls
        leftWall = GameObjects.createWall(world,-39,60,-39,-70,0f,0f,0f,0f);
        rightWall = GameObjects.createWall(world,39,60,39,-70,0f,0f,0f,0f);
        topWall = GameObjects.createWall(world,-39,60,39,60,0f,0f,0f,0f);

        // ball holder
        bottomH = GameObjects.createThinWall(world,39,-60,34,-60,0f);
        sideH = GameObjects.createThinWall(world,34,-60,34,-30,0f);

        //round connors
        rightRoundP1 =  GameObjects.createThinWall(world,39,40,37,44f,0f);
        rightRoundP2 =  GameObjects.createThinWall(world,37,44,34,48,0f);
        rightRoundP3 =  GameObjects.createThinWall(world,34,48,30,53,0f);
        rightRoundP4 =  GameObjects.createThinWall(world,30,53,25,57,0f);
        rightRoundP5 =  GameObjects.createThinWall(world,25,57,19,60,0f);

        leftRoundP1 =  GameObjects.createThinWall(world,-39,40,-37,44f,0f);
        leftRoundP2 =  GameObjects.createThinWall(world,-37,44,-34,48,0f);
        leftRoundP3 =  GameObjects.createThinWall(world,-34,48,-30,53,0f);
        leftRoundP4 =  GameObjects.createThinWall(world,-30,53,-25,57,0f);
        leftRoundP5 =  GameObjects.createThinWall(world,-25,57,-19,60,0f);

        //triangles
        leftTriangleP1 = GameObjects.createThinWall(world,-24,-29,-16,-40,0f);
        leftTriangleP2 = GameObjects.createThinWall(world,-24,-29,-24,-37,0f);
        leftTriangleP3 = GameObjects.createThinWall(world,-24,-37,-16,-40,0f);

        rightTriangleP1 = GameObjects.createThinWall(world,16,-40,24,-29,0f);
        rightTriangleP2 = GameObjects.createThinWall(world,24,-29,24,-37,0f);
        rightTriangleP3 = GameObjects.createThinWall(world,24,-37,16,-40,0f);

        //bottom pieces
        leftBottomP1 = GameObjects.createThinWall(world,-34,-44,-16,-50,0f);
        leftBottomP2 = GameObjects.createThinWall(world,-34,-44,-34,-25,0f);
        rightBottomP1 = GameObjects.createThinWall(world,16,-50,34,-44,0f);

        //circle bumpers
        cBumper1 = GameObjects.createBumper(world,0,0,6,true,1);
        cBumper1.setUserData(bumper1sprite);
        cBumper2 = GameObjects.createBumper(world,-20,30,3,true,1);
        cBumper2.setUserData(bumper2sprite);
        cBumper3 = GameObjects.createBumper(world,20,30,3,true,1);
        cBumper3.setUserData(bumper3sprite);

        //routers
        routers1P1 = GameObjects.createCircle(world,-7.5f,30,.1f,true);
        routers1P2 = GameObjects.createThinWall(world,-12.5f,30,-2.5f,30,0f);
        routers1P2.setType(BodyDef.BodyType.DynamicBody);
        //joint for routers
        RevoluteJointDef rDef = new RevoluteJointDef();
        rDef.bodyA = routers1P1;
        rDef.bodyB = routers1P2;
        rDef.collideConnected = false;
        world.createJoint(rDef);

        routers2P1 = GameObjects.createCircle(world,7.5f,30,.1f,true);
        routers2P2 = GameObjects.createThinWall(world,2.5f,30,12.5f,30,0f);
        routers2P2.setType(BodyDef.BodyType.DynamicBody);

        rDef = new RevoluteJointDef();
        rDef.bodyA = routers2P1;
        rDef.bodyB = routers2P2;
        rDef.collideConnected = false;
        world.createJoint(rDef);




        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.input.setInputProcessor(this);


    }
    @Override
    public void show() {
        //draw the present score
        game.batch.begin();

            font.draw(game.batch,str,0,70);

        game.batch.end();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //updating events in game : router rotation and ball speed
        routers1P2.setAngularDamping(2);
        routers2P2.setAngularDamping(2);
        ball.setLinearVelocity(ball.getLinearVelocity().x,ball.getLinearVelocity().y- .65f);

        //launching the ball
        if(bottomH.getPosition().y > -30){

            bottomH.setTransform(bottomH.getPosition().x ,-80,0f);
            bottomH.setLinearVelocity(0,0);
        }

        //checking for lives
        if(lives == 0)
            game.setScreen(new GameOver(game,totalScore));
        //================releasing flippers==================================
        if( leftFlipper.getAngle() > .485f  ){
            leftFlipper.setAngularVelocity(0);
            leftFlipper.setTransform(leftFlipper.getWorldCenter(),.485f);
        }
        if( leftFlipper.getAngle() < -0.3f  ){
            leftFlipper.setAngularVelocity(0);
            leftFlipper.setTransform(leftFlipper.getWorldCenter(),-0.3f);
        }
        if( rightFlipper.getAngle() > .305f  ){
            rightFlipper.setAngularVelocity(0);
            rightFlipper.setTransform(rightFlipper.getWorldCenter(),.30f);
        }
        if( rightFlipper.getAngle() < -0.490f  ){
            rightFlipper.setAngularVelocity(0);
            rightFlipper.setTransform(rightFlipper.getWorldCenter(),-0.485f);
        }
        //checking if the ball is bellow the screen to reposition it==========
        if(ball.getPosition().y < -75){
            bottomH.setTransform(bottomH.getPosition().x,-60,0);
            ball.setTransform(36.5f,-55,0f);
            ball.setLinearVelocity(new Vector2(0f,0f));
            ball.setAngularVelocity(0f);
            ballRechargeS.play();
            ballLauched = false;
            lives = lives - 1;
            str2 = "Balls:" + lives ;
        }
        //animation for the triangles========
        if(leftTriangleP1.getPosition().x > -17 ){
            leftTriangleP1.setLinearVelocity(-100,-100);

        }
        if(leftTriangleP1.getPosition().x < -20){
            leftTriangleP1.setLinearVelocity(0,0);
            leftTriangleP1.setTransform(-20 ,-34.5f,0f);

        }
        if(rightTriangleP1.getPosition().x < 17 ){
            rightTriangleP1.setLinearVelocity(100,-100);

        }
        if(rightTriangleP1.getPosition().x > 20){
            rightTriangleP1.setLinearVelocity(0,0);
            rightTriangleP1.setTransform(20 ,-34.5f,0f);
           // System.out.println("tranformed");

        }
        //adding up the points from the routers
        if(routers1P2.getAngularVelocity() != 0){

            if(routers1P2.getAngularVelocity() < 0)
                totalScore+=-1*(routers1P2.getAngularVelocity()/5) ;
            else
                totalScore+=routers1P2.getAngularVelocity()/5 ;
        }



        if(routers2P2.getAngularVelocity() != 0){
            //System.out.println(" " + routers2P2.getAngularVelocity());
            if(routers2P2.getAngularVelocity() < 0)
                totalScore+=-1*(routers2P2.getAngularVelocity()/5) ;
            else
                totalScore+=routers2P2.getAngularVelocity()/5 ;
        }
        //pause system but not fully implemented
        if(paused)
            world.step(0f, 6, 2);
        else
            world.step(1/60f, 6, 2);
        debuger.render(world, cam.combined);
        game.batch.setProjectionMatrix(cam.combined);
        str = str + totalScore ;
        //drawing every thing on the screen
        game.batch.begin();

            background.draw(game.batch,0.6f);

            topPiece.setPosition(- topPiece.getWidth()/2,71 - topPiece.getHeight());
            topPiece.draw(game.batch);
            quitButtom.setPosition(-40,71 - quitButtom.getHeight());
            quitButtom.draw(game.batch);
            font.draw(game.batch,str,0,70);
            font.draw(game.batch,str2,20,70);
            Sprite sprite = (Sprite) ball.getUserData();


            sprite.setPosition(ball.getPosition().x - sprite.getWidth()/2,ball.getPosition().y - sprite.getWidth()/2 );
            sprite.setRotation(MathUtils.radiansToDegrees * ball.getAngle());
            sprite.draw(game.batch);
            sprite = (Sprite) cBumper1.getUserData();
            sprite.setPosition(cBumper1.getPosition().x -sprite.getWidth()/2,cBumper1.getPosition().y -sprite.getWidth()/2);
            sprite.setRotation(MathUtils.radiansToDegrees * cBumper1.getAngle());
            sprite.draw(game.batch);

            sprite = (Sprite) cBumper2.getUserData();
            sprite.setPosition(cBumper2.getPosition().x -sprite.getWidth()/2,cBumper2.getPosition().y -sprite.getWidth()/2);
            sprite.draw(game.batch);

            sprite = (Sprite) cBumper3.getUserData();
            sprite.setPosition(cBumper3.getPosition().x -sprite.getWidth()/2,cBumper3.getPosition().y -sprite.getWidth()/2);
            sprite.draw(game.batch);

            leftStickSpr.setOrigin(2.1f,leftStickSpr.getHeight()-2.2f);
            leftStickSpr.setPosition(leftFlipper.getPosition().x -0.3f,leftFlipper.getPosition().y  - ((leftStickSpr.getHeight()/2 )+ 4) );
            leftStickSpr.setRotation((MathUtils.radiansToDegrees * leftFlipper.getAngle())+48f);
            leftStickSpr.draw(game.batch);
            //==========================================
            rightStickSpr.setOrigin(rightStickSpr.getWidth()-2.2f,rightStickSpr.getHeight()-2.2f);
            rightStickSpr.setPosition(rightFlipper.getPosition().x - 9.5f ,rightFlipper.getPosition().y - 9  );
            rightStickSpr.setRotation((MathUtils.radiansToDegrees * rightFlipper.getAngle())-48f);
            rightStickSpr.draw(game.batch);

        world.getBodies(tmpBodies);
        game.batch.end();
        str = "";

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
        //the ifs checks for user inputs and makes the flipper mechanism
        //it checks on wich side is the user input and decides wich flipper to use or to launch the ball
        //it also plays the engaging sounds
        if(screenX < Gdx.graphics.getWidth()/5 && screenY < Gdx.graphics.getHeight()/12 )
            game.setScreen(new GameOver(game,totalScore));
        if(screenX < Gdx.graphics.getWidth()/2 && screenY >  Gdx.graphics.getHeight()/2)
        {
            leftFlipper.setAngularVelocity(10f);
            flipperSI.play();

        }

        if(screenX > Gdx.graphics.getWidth()/2 && screenY > Gdx.graphics.getHeight()/2 && screenX < Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/5)
        {
            rightFlipper.setAngularVelocity(-10f);
            flipperSI.play();

        }

        if( screenY >  Gdx.graphics.getHeight()/2 && screenX >  Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/5 && !ballLauched){

            bottomH.setLinearVelocity(0,10000);
            ballLauchS.play();
            ballLauched = true;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        //the ifs check for user input and decides wich flipper to release
        //also plays the release sound
        if(screenX < Gdx.graphics.getWidth()/2 && screenY >  Gdx.graphics.getHeight()/2)
        {
            leftFlipper.setAngularVelocity(-10f);
            flipperSR.play();
        }
        if(screenX > Gdx.graphics.getWidth()/2 && screenY > Gdx.graphics.getHeight()/2 && screenX < Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/5){
            rightFlipper.setAngularVelocity(10f);
            flipperSR.play();
        }
        dragging = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        //this ifs is to fix the problem if u drag ur finger out of the area for working with the flipper
        //this make so the flipper releases if u drag the finger out of range
        if(!(screenX <  Gdx.graphics.getWidth()/2 && screenY > Gdx.graphics.getHeight()/2)&& !(screenX >  Gdx.graphics.getWidth()/2 && screenY >  Gdx.graphics.getHeight()/2 && screenX <  Gdx.graphics.getWidth() - Gdx.graphics.getWidth()/5) && !dragging){
            leftFlipper.setAngularVelocity(-10f);
            rightFlipper.setAngularVelocity(10f);
            dragging = true;
        }
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
    public void beginContact(Contact contact) {

        //these is are for checking collision and add up the scores based on the collision object each object has a different point assigned
        if(contact.getFixtureA().getBody() == leftTriangleP1 || contact.getFixtureB().getBody() == leftTriangleP1 ) {
            leftTriangleP1.setLinearVelocity(10000, 10000);
            triangleS.play();

            totalScore+= 10;
        }
        if(contact.getFixtureA().getBody() == rightTriangleP1 || contact.getFixtureB().getBody() == rightTriangleP1 ) {
            rightTriangleP1.setLinearVelocity(-10000, 10000);
            triangleS.play();

            totalScore+= 10;
        }
        if(contact.getFixtureA().getBody() == cBumper1 || contact.getFixtureB().getBody() == cBumper1 ) {
            Gdx.input.vibrate(100);
            bumperS.play();
            totalScore += 10;
        }
        if(contact.getFixtureA().getBody() == cBumper2 || contact.getFixtureB().getBody() == cBumper2 ) {
            Gdx.input.vibrate(100);
            bumperS.play();
            totalScore +=20;
        }
        if(contact.getFixtureA().getBody() == cBumper3 || contact.getFixtureB().getBody() == cBumper3 ) {
            Gdx.input.vibrate(100);
            bumperS.play();
            totalScore += 20;
        }
        if(contact.getFixtureA().getBody() == routers1P2 || contact.getFixtureB().getBody() == routers1P2 ) {
            float x =0;
            if(routers1P2.getAngularVelocity() < 0)
                 x = -1*routers1P2.getAngularVelocity();
            else
                x = routers1P2.getAngularVelocity();
            if(x > 0) {
                routerS.play();
            }

        }
        if(contact.getFixtureA().getBody() == routers2P2 || contact.getFixtureB().getBody() == routers2P2 ) {
            float x =0;
            if(routers2P2.getAngularVelocity() < 0)
                x = -1*routers2P2.getAngularVelocity();
            else
                x = routers2P2.getAngularVelocity();
            if(x > 0){
                routerS.play();
            }

        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
