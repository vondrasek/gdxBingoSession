package com.vondrastic.gdxgui;

import com.badlogic.gdx.Game;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class gdxgui extends Game {
	@Override
	public void create() {
		setScreen(new scrSplash());
	}
}