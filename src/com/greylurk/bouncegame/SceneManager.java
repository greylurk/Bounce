package com.greylurk.bouncegame;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.color.Color;

import android.hardware.SensorManager;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class SceneManager {
	private AllScenes mCurrentScene;
	private BaseGameActivity mActivity;
	private Engine mEngine;
	private Camera mCamera;
	private ITextureRegion mSplashTR, mPlayerTextureRegion;
	private Scene mSplashScene, mGameScene, mMenuScene;
	private PhysicsWorld mPhysicsWorld;
	protected static final FixtureDef WALL_FIX = PhysicsFactory
			.createFixtureDef(0.0f, 0.0f, 0.0f);
	protected static final FixtureDef PLAYER_FIX = PhysicsFactory
			.createFixtureDef(10.0f, 1.0f, 0.0f);

	public SceneManager(BaseGameActivity act, Engine eng, Camera cam) {
		mActivity = act;
		mEngine = eng;
		mCamera = cam;
	}

	public void loadSplashResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BitmapTextureAtlas splashTA = new BitmapTextureAtlas(
				mActivity.getTextureManager(), 256, 256);
		mSplashTR = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTA, mActivity, "splash.png", 0, 0);
		splashTA.load();
	}

	public void loadGameResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BitmapTextureAtlas textureAtlas = new BitmapTextureAtlas(
				mActivity.getTextureManager(), 64, 64);

		mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(textureAtlas, mActivity, "tree.png", 0, 0);
		textureAtlas.load();
	}

	public void loadMenuResources() {

	}

	public Scene createSplashScene() {
		mSplashScene = new Scene();
		mSplashScene.setBackground(new Background(1, 1, 1));
		Sprite icon = new Sprite(0, 0, mSplashTR,
				mEngine.getVertexBufferObjectManager());
		icon.setPosition((mCamera.getWidth() - icon.getWidth()) / 2,
				(mCamera.getHeight() - icon.getHeight()) / 2);
		mSplashScene.attachChild(icon);
		return mSplashScene;
	}

	public Scene createGameScene() {
		mGameScene = new Scene();
		mGameScene.setBackground(new Background(0, 125, 58));

		// Create a physics world and
		mPhysicsWorld = new PhysicsWorld(new Vector2(0,
				SensorManager.GRAVITY_EARTH), false);
		mGameScene.registerUpdateHandler(mPhysicsWorld);
		createWalls(mGameScene);
		createPlayerSprite(mGameScene);
		return mGameScene;

	}

	public Scene createMenuScene() {
		mMenuScene = new Scene();
		mMenuScene.setBackground(new Background(0, 0, 0));
		Sprite icon = new Sprite(0, 0, mSplashTR,
				mEngine.getVertexBufferObjectManager());
		icon.setPosition((mCamera.getWidth() - icon.getWidth()) / 2,
				(mCamera.getHeight() - icon.getHeight()) / 2);
		mMenuScene.attachChild(icon);
		return mMenuScene;
	}

	public AllScenes getCurrentScene() {
		return mCurrentScene;
	}

	public void setCurrentScene(AllScenes currentScene) {
		this.mCurrentScene = currentScene;
		switch (currentScene) {
		case SPLASH:
			mEngine.setScene(mSplashScene);
			break;
		case MENU:
			mEngine.setScene(mMenuScene);
			break;
		case GAME:
			mEngine.setScene(mGameScene);
			break;
		default:
			break;
		}
	}

	private void createWalls(Scene scene) {
		Rectangle ground = new Rectangle(0, scene.getHeight() - 15,
				scene.getWidth(), 15, mEngine.getVertexBufferObjectManager());
		ground.setColor(new Color(15, 50, 0));
		Body body = PhysicsFactory.createBoxBody(mPhysicsWorld, ground,
				BodyType.StaticBody, WALL_FIX);
		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(ground,
				body, false, false));
		mGameScene.attachChild(ground);
	}

	private void createPlayerSprite(Scene scene) {
		Sprite sPlayer = new Sprite(
				(scene.getWidth() - mPlayerTextureRegion.getWidth()) / 2,
				(scene.getHeight() - mPlayerTextureRegion.getHeight()) / 2,
				mPlayerTextureRegion, mEngine.getVertexBufferObjectManager());
		sPlayer.setRotation(45.0f);
		Body body = PhysicsFactory.createCircleBody(mPhysicsWorld, sPlayer,
				BodyType.DynamicBody, PLAYER_FIX);
		scene.attachChild(sPlayer);

		mPhysicsWorld.registerPhysicsConnector(new PhysicsConnector(sPlayer,
				body, true, false));
	}

	public enum AllScenes {
		SPLASH, MENU, GAME
	}
}
