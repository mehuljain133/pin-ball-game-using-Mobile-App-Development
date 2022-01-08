package com.pinballpro.game;

import com.badlogic.gdx.physics.box2d.World;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


/**
 * this class creates the objects for collision needed in the game circles, walls and triangles
 */

public class GameObjects {

    public static Body createCircle (World world, float x, float y, float radius, boolean isStatic) {
        CircleShape sd = new CircleShape();
        sd.setRadius(radius);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = sd;
        fdef.density = 1.0f;
        fdef.friction = 0.2f;
        fdef.restitution = 0.5f;

        BodyDef bd = new BodyDef();
        // bd.isBullet = true;
        bd.allowSleep = true;
        bd.position.set(x, y);
        Body body = world.createBody(bd);
        body.createFixture(fdef);
        if (isStatic) {
            body.setType(BodyDef.BodyType.StaticBody);
        } else {
            body.setType(BodyDef.BodyType.DynamicBody);
        }
        return body;
    }
    /** Creates a wall by constructing a rectangle whose corners are (xmin,ymin) and (xmax,ymax), and rotating the box
     * counterclockwise through the given angle, with specified restitution. */
    public static Body createWall (World world, float xmin, float ymin, float xmax, float ymax, float angle, float restitution,float centerX,float centerY) {
        float cx = (xmin + xmax) / 2;
        float cy = (ymin + ymax) / 2;
        float hx = (xmax - xmin) / 2;
        float hy = (ymax - ymin) / 2;
        if (hx < 0) hx = -hx;
        if (hy < 0) hy = -hy;
        PolygonShape wallshape = new PolygonShape();
        wallshape.setAsBox(hx, hy, new Vector2(centerX, centerY), angle);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = wallshape;
        fdef.density = 1.0f;
        if (restitution > 0) fdef.restitution = restitution;

        BodyDef bd = new BodyDef();
        bd.position.set(cx, cy);
        Body wall = world.createBody(bd);
        wall.createFixture(fdef);
        wall.setType(BodyDef.BodyType.KinematicBody);
        return wall;
    }

    /** Creates a segment-like thin wall with 0.05 thickness going from (x1,y1) to (x2,y2) */
    public static Body createThinWall (World world, float x1, float y1, float x2, float y2, float restitution) {
        // determine center point and rotation angle for createWall
        float cx = (x1 + x2) / 2;
        float cy = (y1 + y2) / 2;
        float angle = (float)Math.atan2(y2 - y1, x2 - x1);
        float mag = (float)Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
        return createWall(world, cx - mag / 2, cy - 0.05f, cx + mag / 2, cy + 0.05f, angle, restitution,0f,0f);
    }
    public static Body createBumper (World world, float x, float y, float radius, boolean isStatic,float bouncyness) {
        CircleShape sd = new CircleShape();
        sd.setRadius(radius);

        FixtureDef fdef = new FixtureDef();
        fdef.shape = sd;
        fdef.density = 1.0f;
        fdef.friction = 0.2f;
        fdef.restitution = bouncyness;

        BodyDef bd = new BodyDef();
        // bd.isBullet = true;
        bd.allowSleep = true;
        bd.position.set(x, y);
        Body body = world.createBody(bd);
        body.createFixture(fdef);
        if (isStatic) {
            body.setType(BodyDef.BodyType.StaticBody);
        } else {
            body.setType(BodyDef.BodyType.DynamicBody);
        }
        return body;
    }


}
