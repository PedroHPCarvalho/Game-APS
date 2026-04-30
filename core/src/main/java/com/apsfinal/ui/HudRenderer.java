package com.apsfinal.ui;

import com.apsfinal.manager.ScoreManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Renderiza o HUD (Heads-Up Display) do jogo.
 * Exibe tempo, lixo coletado e pontuação.
 */
public class HudRenderer {
    private ScoreManager scoreManager;
    private BitmapFont font;

    /**
     * Construtor recebe o gerenciador de pontuação.
     * @param scoreManager instância do ScoreManager
     */
    public HudRenderer(ScoreManager scoreManager) {
        this.scoreManager = scoreManager;
        this.font = new BitmapFont();
        this.font.getData().setScale(2f); // Escala para legibilidade mobile
    }

    /**
     * Renderiza as informações do HUD na tela.
     * @param batch SpriteBatch para desenhar o texto
     * @param hudCamera câmera do HUD (não se move)
     */
    public void render(SpriteBatch batch, OrthographicCamera hudCamera) {
        batch.setProjectionMatrix(hudCamera.combined);
        batch.begin();

        // Topo esquerdo: Tempo
        String timeText = "TEMPO: " + (int) scoreManager.getTimeRemaining() + "s";
        font.draw(batch, timeText, 10f, hudCamera.viewportHeight - 20f);

        // Topo centro: Lixo coletado / Meta
        String goalText = "LIXO: " + scoreManager.getCollected() + "/" + scoreManager.getCurrentGoal();
        com.badlogic.gdx.graphics.g2d.GlyphLayout goalLayout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(font, goalText);
        float goalX = (hudCamera.viewportWidth - goalLayout.width) / 2f;
        font.draw(batch, goalLayout, goalX, hudCamera.viewportHeight - 20f);

        // Topo direito: Score
        String scoreText = "SCORE: " + scoreManager.getScore();
        com.badlogic.gdx.graphics.g2d.GlyphLayout scoreLayout = new com.badlogic.gdx.graphics.g2d.GlyphLayout(font, scoreText);
        float scoreX = hudCamera.viewportWidth - scoreLayout.width - 10f;
        font.draw(batch, scoreLayout, scoreX, hudCamera.viewportHeight - 20f);

        batch.end();
    }

    /**
     * Descarta os recursos do HUD.
     */
    public void dispose() {
        if (font != null) {
            font.dispose();
        }
    }
}