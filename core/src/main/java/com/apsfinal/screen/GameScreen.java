package com.apsfinal.screen;

import com.apsfinal.AquaCleanGame;
import com.apsfinal.config.Difficulty;
import com.apsfinal.config.GameConfig;
import com.apsfinal.manager.ScoreManager;
import com.apsfinal.manager.TrashManager;
import com.apsfinal.ui.HudRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

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
    private Difficulty difficulty;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private boolean goalJustReached;
    private float goalMessageTimer;
    private boolean trashJustCollected;
    private float trashMessageTimer;
    private BitmapFont goalFont;
    private Vector3 touchPoint;
    private float virtualWidth;
    private float virtualHeight;

    public GameScreen(AquaCleanGame game) {
        this(game, Difficulty.MEDIUM);
    }

    public GameScreen(AquaCleanGame game, Difficulty difficulty) {
        this.game = game;
        this.difficulty = difficulty;
        this.camera = new OrthographicCamera();
        this.hudCamera = new OrthographicCamera();
        this.virtualWidth = GameConfig.VIRTUAL_WIDTH;
        this.virtualHeight = GameConfig.VIRTUAL_HEIGHT;
        this.hudCamera.viewportWidth = virtualWidth;
        this.hudCamera.viewportHeight = virtualHeight;
        this.hudCamera.position.set(virtualWidth / 2f, virtualHeight / 2f, 0f);
        this.hudCamera.update();
        this.viewport = new FitViewport(virtualWidth, virtualHeight, camera);
        this.trashManager = new TrashManager(difficulty);
        this.scoreManager = new ScoreManager(difficulty);
        this.hudRenderer = new HudRenderer(scoreManager, virtualWidth, virtualHeight);
        this.shapeRenderer = new ShapeRenderer();
        this.batch = new SpriteBatch();
        this.goalJustReached = false;
        this.goalMessageTimer = 0f;
        this.trashJustCollected = false;
        this.trashMessageTimer = 0f;
        this.goalFont = new BitmapFont();
        this.goalFont.getData().setScale(3f);
        this.touchPoint = new Vector3();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                touchPoint.set(screenX, screenY, 0f);
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
        int collectedThisFrame = trashManager.update(delta, scoreManager);
        if (collectedThisFrame > 0) {
            trashJustCollected = true;
            trashMessageTimer = 1.0f;
        }

        scoreManager.update(delta);
        if (scoreManager.isGameOver()) {
            game.setScreen(new ResultScreen(game, scoreManager.getScore()));
            return;
        }

        if (scoreManager.checkGoal()) {
            goalJustReached = true;
            goalMessageTimer = 1.5f;
        }

        if (goalJustReached) {
            goalMessageTimer -= delta;
            if (goalMessageTimer <= 0f) {
                goalJustReached = false;
            }
        }

        if (trashJustCollected) {
            trashMessageTimer -= delta;
            if (trashMessageTimer <= 0f) {
                trashJustCollected = false;
            }
        }

        Gdx.gl.glClearColor(0.2f, 0.5f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        hudCamera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.3f, 0.6f, 0.9f, 1f);
        shapeRenderer.rect(0f, 0f, virtualWidth, virtualHeight);
        trashManager.render(shapeRenderer);
        shapeRenderer.end();

        hudCamera.update();
        hudRenderer.render(batch, hudCamera);

        if (goalJustReached) {
            batch.setProjectionMatrix(hudCamera.combined);
            batch.begin();
            String bonusText = "+" + (int) difficulty.timeBonus + "s";
            com.badlogic.gdx.graphics.g2d.GlyphLayout bonusLayout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(goalFont, bonusText);
            float bonusX = (virtualWidth - bonusLayout.width) / 2f;
            float bonusY = virtualHeight / 2f + bonusLayout.height / 2f;
            goalFont.draw(batch, bonusLayout, bonusX, bonusY);
            batch.end();
        }

        if (trashJustCollected) {
            batch.setProjectionMatrix(hudCamera.combined);
            batch.begin();
            String trashText = "+" + difficulty.timePerTrash + "s";
            com.badlogic.gdx.graphics.g2d.GlyphLayout trashLayout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(goalFont, trashText);
            float trashX = (virtualWidth - trashLayout.width) / 2f;
            float trashY = virtualHeight / 2f - 50f + trashLayout.height / 2f;
            goalFont.draw(batch, trashLayout, trashX, trashY);
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        boolean isLandscape = width > height;
        if (isLandscape) {
            virtualWidth = GameConfig.VIRTUAL_HEIGHT;
            virtualHeight = GameConfig.VIRTUAL_WIDTH;
        } else {
            virtualWidth = GameConfig.VIRTUAL_WIDTH;
            virtualHeight = GameConfig.VIRTUAL_HEIGHT;
        }
        viewport.setWorldSize(virtualWidth, virtualHeight);
        viewport.update(width, height, true);
        hudCamera.viewportWidth = virtualWidth;
        hudCamera.viewportHeight = virtualHeight;
        camera.position.set(virtualWidth / 2f, virtualHeight / 2f, 0f);
        hudCamera.position.set(virtualWidth / 2f, virtualHeight / 2f, 0f);
        camera.update();
        hudCamera.update();
        if (trashManager != null) {
            trashManager.setVirtualBounds(virtualWidth, virtualHeight);
        }
        if (hudRenderer != null) {
            hudRenderer.setVirtualBounds(virtualWidth, virtualHeight);
        }
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