package com.greylurk.bouncegame;

import java.io.IOException;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class GameActivity extends BaseGameActivity {
	/** Constants for camera setup **/
	protected static final int CAMERA_WIDTH = 1280;
	protected static final int CAMERA_HEIGHT = 720;

	private Scene mScene;
	private BitmapTextureAtlas playerTexture;
	private ITextureRegion playerTextureRegion;

	@Override
	public EngineOptions onCreateEngineOptions() {
		Camera mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		EngineOptions options = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(
						CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);
		return options;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		loadGfx();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	private void loadGfx() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		playerTexture = new BitmapTextureAtlas(getTextureManager(), 64, 64);

		playerTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(playerTexture, this, "tree.png", 0, 0);
		playerTexture.load();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		// TODO Auto-generated method stub
		this.mScene = new Scene();
		mScene.setBackground(new Background(0, 125, 58));
		pOnCreateSceneCallback.onCreateSceneFinished(this.mScene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		Sprite sPlayer = new Sprite(
				(CAMERA_WIDTH - playerTextureRegion.getWidth()) / 2,
				(CAMERA_HEIGHT - playerTextureRegion.getHeight()) / 2,
				playerTextureRegion, mEngine.getVertexBufferObjectManager());
		sPlayer.setRotation(45.0f);
		mScene.attachChild(sPlayer);
		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}

}
