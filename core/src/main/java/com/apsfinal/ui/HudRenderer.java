package com.apsfinal.ui;

import com.apsfinal.config.GameConfig;
import com.apsfinal.manager.ScoreManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

/**
 * Renderiza o HUD (Heads-Up Display) do jogo.
 * Exibe tempo, lixo coletado e pontuação.
 */
public class HudRenderer {
    private ScoreManager scoreManager;
    private BitmapFont font;
    private Texture whitePixel;
    private float virtualWidth;
    private float virtualHeight;

    /**
     * Construtor recebe o gerenciador de pontuação.
     */
    public HudRenderer(ScoreManager scoreManager, float virtualWidth, float virtualHeight) {
        this.scoreManager = scoreManager;
        this.font = new BitmapFont();
        this.font.getData().setScale(2f);
        this.virtualWidth = virtualWidth;
        this.virtualHeight = virtualHeight;

        // Cria textura de 1x1 pixel branco
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        this.whitePixel = new Texture(pixmap);
        pixmap.dispose();
    }

    /**
     * Atualiza as dimensões virtuais do HUD.
     */
    public void setVirtualBounds(float width, float height) {
        this.virtualWidth = width;
        this.virtualHeight = height;
    }

    /**
     * Renderiza as informações do HUD na tela.
     */
    public void render(SpriteBatch batch, OrthographicCamera hudCamera) {
        // Atualiza a câmera com as dimensões virtuais atuais
        hudCamera.viewportWidth = virtualWidth;
        hudCamera.viewportHeight = virtualHeight;
        hudCamera.position.set(virtualWidth / 2f, virtualHeight / 2f, 0);
        hudCamera.update();

        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        // Obtém tempo restante
        float timeRemaining = scoreManager.getTimeRemaining();

        // Topo esquerdo: Tempo
        String timeText = "TEMPO: " + (int) timeRemaining + "s";
        font.draw(batch, timeText, 10f, virtualHeight - 20f);

        // Topo centro: Lixo coletado / Meta
        String goalText = "LIXO: " + scoreManager.getCollected() + "/" + scoreManager.getCurrentGoal();
        GlyphLayout goalLayout = new GlyphLayout(font, goalText);
        float goalX = (virtualWidth - goalLayout.width) / 2f;
        font.draw(batch, goalLayout, goalX, virtualHeight - 20f);

        // Topo direito: Score
        String scoreText = "SCORE: " + scoreManager.getScore();
        GlyphLayout scoreLayout = new GlyphLayout(font, scoreText);
        float scoreX = virtualWidth - scoreLayout.width - 10f;
        font.draw(batch, scoreLayout, scoreX, virtualHeight - 20f);

        batch.end();
    }

    /**
     * Descarta os recursos do HUD.
     */
    public void dispose() {
        if (font != null) {
            font.dispose();
        }
        if (whitePixel != null) {
            whitePixel.dispose();
        }
    }
}