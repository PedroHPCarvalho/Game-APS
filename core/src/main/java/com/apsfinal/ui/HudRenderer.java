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
        font.draw(batch, "TEMPO: " + (int) scoreManager.getTimeRemaining() + "s", 10f, 790f);

        // Topo centro: Lixo coletado / Meta
        font.draw(batch, "LIXO: " + scoreManager.getCollected() + "/" + scoreManager.getCurrentGoal(), 150f, 790f);

        // Topo direito: Score
        font.draw(batch, "SCORE: " + scoreManager.getScore(), 320f, 790f);

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