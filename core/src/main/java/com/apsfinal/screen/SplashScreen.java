package com.apsfinal.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.apsfinal.AquaCleanGame;
import com.apsfinal.config.GameConfig;

/**
 * Tela de splash que exibe o logo/nome do jogo por 2 segundos.
 */
public class SplashScreen implements Screen {
    private AquaCleanGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;
    private float elapsed;

    /**
     * Construtor recebe a instância do jogo.
     * @param game instância principal do jogo
     */
    public SplashScreen(AquaCleanGame game) {
        this.game = game;
        this.batch = game.batch;
        this.font = new BitmapFont();
        this.font.getData().setScale(4f); // Fonte grande para o título
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameConfig.VIRTUAL_WIDTH, GameConfig.VIRTUAL_HEIGHT, camera);
        this.elapsed = 0f;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        elapsed += delta;

        // Transição automática após 2 segundos
        if (elapsed >= 2f) {
            game.setScreen(new MenuScreen(game));
            dispose();
            return;
        }

        // Limpa a tela com azul escuro
        Gdx.gl.glClearColor(0.1f, 0.3f, 0.5f, 1f);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // Atualiza a câmera e viewport
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Renderiza o título centralizado
        batch.begin();
        font.draw(batch, "AquaClean", 
            GameConfig.VIRTUAL_WIDTH / 2f - 120f, 
            GameConfig.VIRTUAL_HEIGHT / 2f + 20f);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        if (font != null) {
            font.dispose();
        }
    }
}