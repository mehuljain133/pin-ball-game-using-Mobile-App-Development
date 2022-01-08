package com.pinballpro.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pinballpro.game.MainMenu;
//this class just calls in the main menu class
public class MainClass extends Game {
	public SpriteBatch batch ;
	@Override
	public void create() {
		batch = new SpriteBatch();
		this.setScreen(new MainMenu(this));

	}
	@Override
	public  void render()
	{

		super.render();
	}

}
