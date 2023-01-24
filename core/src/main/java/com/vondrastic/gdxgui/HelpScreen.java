package com.vondrastic.gdxgui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class HelpScreen extends Screen {
	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Rectangle nextBounds;
	Vector3 touchPoint;
	Texture helpImage;
	TextureRegion helpRegion;

	public HelpScreen (Game game) {
		super(game);

		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		nextBounds = new Rectangle(320 - 64, 0, 64, 64);
		touchPoint = new Vector3();
		batcher = new SpriteBatch();
		helpImage = Assets.loadTexture("data/help1.png");
		helpRegion = new TextureRegion(helpImage, 0, 0, 320, 480);
	}

	@Override
	public void resume () {
	}

	@Override
	public void pause () {
		helpImage.dispose();
	}

	@Override
	public void update (float deltaTime) {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (OverlapTester.pointInRectangle(nextBounds, touchPoint.x, touchPoint.y)) {
				// Assets.playSound(Assets.clickSound);
				//game.setScreen(new HelpScreen2(game)); removed to compile
				return;
			}
		}
	}

	@Override
	public void present (float deltaTime) {
		GL20 gl = Gdx.gl;
		gl.glClear(gl.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		//guiCam.apply(Gdx.gl);

		gl.glEnable(gl.GL_TEXTURE_2D);

		batcher.disableBlending();
		batcher.begin();
		batcher.draw(helpRegion, 0, 0, 320, 480);
		batcher.end();

		batcher.enableBlending();
		batcher.begin();
		//batcher.draw(Assets.arrow, 320, 0, -64, 64); Removed to compile
		batcher.end();

		gl.glDisable(gl.GL_BLEND);
	}

	@Override
	public void dispose () {
	}
}
