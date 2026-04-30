package com.apsfinal.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.apsfinal.AquaCleanGame;
import com.apsfinal.config.GameConfig;
import com.apsfinal.manager.TrashManager;
import com.apsfinal.manager.ScoreManager;
import com.apsfinal.ui.HudRenderer;

/**
 * Tela principal do jogo onde ocorre a ação.
 */
public class GameScreen implements Screen {
    private AquaCleanGame game;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private Viewport viewport;
    private TrashManager trashManager;
    private ScoreManager scoreManager;
    private HudRenderer hudRenderer;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private boolean goalJustReached;
    private float goalMessageTimer;
    private BitmapFont goalFont;
    private Vector3 touchPoint;

    public GameScreen(AquaCleanGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.hudCamera = new OrthographicCamera();
        this.viewport = new FitViewport(GameConfig.VIRTUAL_WIDTH, GameConfig.VIRTUAL_HEIGHT, camera);
        this.trashManager = new TrashManager();
        this.scoreManager = new ScoreManager();
        this.hudRenderer = new HudRenderer(scoreManager);
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.goalJustReached = false;
        this.goalMessageTimer = 0f;
        this.goalFont = new BitmapFont();
        this.goalFont.getData().setScale(3f);
        this.touchPoint = new Vector3();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touchPoint.set(screenX, screenY, 0);
                viewport.unproject(touchPoint);
                trashManager.handleTap(touchPoint.x, touchPoint.y);
                return true;
            }
        });
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        scoreManager.update(delta);
        if (scoreManager.isGameOver()) {
            game.setScreen(new ResultScreen(game, scoreManager.getScore()));
            return;
        }

        trashManager.update(delta, scoreManager);
        if (scoreManager.checkGoal()) {
            goalJustReached = true;
            goalMessageTimer = 1.5f;
        }

        if (goalJustReached) {
            goalMessageTimer -= delta;
            if (goalMessageTimer <= 0) {
                goalJustReached = false;
            }
        }

        Gdx.gl.glClearColor(0.2f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        hudCamera.update();

        // Render background and trash with ShapeRenderer
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        
        // Background
        shapeRenderer.setColor(0.3f, 0.6f, 0.9f, 1f);
        shapeRenderer.rect(0, 0, GameConfig.VIRTUAL_WIDTH, GameConfig.VIRTUAL_HEIGHT);
        
        // Trash items
        trashManager.render(shapeRenderer);
        
        shapeRenderer.end();

        // Render HUD with SpriteBatch
        hudCamera.update();
        hudRenderer.render(batch, hudCamera);

        // Show time bonus message
        if (goalJustReached) {
            batch.setProjectionMatrix(hudCamera.combined);
            batch.begin();
            goalFont.draw(batch, "+15s", 
                GameConfig.VIRTUAL_WIDTH / 2f - 60f, 
                GameConfig.VIRTUAL_HEIGHT / 2f);
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        if (trashManager != null) {
            trashManager.dispose();
        }
        if (hudRenderer != null) {
            hudRenderer.dispose();
        }
        if (goalFont != null) {
            goalFont.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
    }
}