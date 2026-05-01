package com.apsfinal.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
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
    private float virtualWidth;
    private float virtualHeight;

    public GameScreen(AquaCleanGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.hudCamera = new OrthographicCamera();
        // Inicializa com proporção retrato (padrão)
        this.virtualWidth = GameConfig.VIRTUAL_WIDTH;  // 480
        this.virtualHeight = GameConfig.VIRTUAL_HEIGHT; // 854
        // Configura a câmera do HUD com as dimensões virtuais
        this.hudCamera.viewportWidth = virtualWidth;
        this.hudCamera.viewportHeight = virtualHeight;
        this.hudCamera.position.set(virtualWidth / 2, virtualHeight / 2, 0);
        this.hudCamera.update();
        this.viewport = new FitViewport(virtualWidth, virtualHeight, camera);
        this.trashManager = new TrashManager();
        this.scoreManager = new ScoreManager();
        this.hudRenderer = new HudRenderer(scoreManager, virtualWidth, virtualHeight);
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
        shapeRenderer.rect(0, 0, virtualWidth, virtualHeight);
        
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
            com.badlogic.gdx.graphics.g2d.GlyphLayout bonusLayout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(goalFont, "+15s");
            float bonusX = (virtualWidth - bonusLayout.width) / 2f;
            float bonusY = virtualHeight / 2f + bonusLayout.height / 2f;
            goalFont.draw(batch, bonusLayout, bonusX, bonusY);
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {
        // Detecta orientação física
        boolean isLandscape = width > height;
        
        // Ajusta dimensões virtuais conforme a orientação
        if (isLandscape) {
            // Paisagem: inverte para 16:9
            virtualWidth = GameConfig.VIRTUAL_HEIGHT;  // 854
            virtualHeight = GameConfig.VIRTUAL_WIDTH; // 480
        } else {
            // Retrato: mantém 9:16
            virtualWidth = GameConfig.VIRTUAL_WIDTH;  // 480
            virtualHeight = GameConfig.VIRTUAL_HEIGHT; // 854
        }
        
        // Atualiza o viewport com as novas dimensões virtuais
        viewport.setWorldSize(virtualWidth, virtualHeight);
        viewport.update(width, height, true);
        
        // Atualiza a câmera do jogo e do HUD
        // Atualiza as dimensões da viewport da câmera do HUD para as novas dimensões virtuais
        hudCamera.viewportWidth = virtualWidth;
        hudCamera.viewportHeight = virtualHeight;
        camera.position.set(virtualWidth / 2, virtualHeight / 2, 0);
        hudCamera.position.set(virtualWidth / 2, virtualHeight / 2, 0);
        camera.update();
        hudCamera.update();
        
        // Passa as novas dimensões para o TrashManager e HudRenderer
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