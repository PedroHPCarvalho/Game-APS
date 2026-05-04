package com.apsfinal.manager;

import com.apsfinal.config.Difficulty;
import com.apsfinal.config.GameConfig;

/**
 * Gerencia pontuação, tempo e metas do jogo.
 * Suporta diferentes níveis de dificuldade.
 */
public class ScoreManager {
    private Difficulty difficulty;
    private float timeRemaining;
    private float maxTime;
    private int currentGoal; // meta fixa
    private int collected;
    private int score;

    /**
     * Construtor padrão (usa dificuldade MÉDIO para compatibilidade).
     */
    public ScoreManager() {
        this(Difficulty.MEDIUM);
    }

    /**
     * Construtor com dificuldade específica.
     * @param difficulty nível de dificuldade
     */
    public ScoreManager(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.timeRemaining = difficulty.initialTime;
        this.maxTime = difficulty.maxTime;
        this.currentGoal = GameConfig.FIXED_GOAL;
        this.collected = 0;
        this.score = 0;
    }

    /**
     * Adiciona um lixo coletado e verifica se atingiu a meta.
     * Também adiciona tempo por lixo coletado (respeitando limite máximo).
     */
    public void addCollected() {
        // Adiciona tempo por lixo coletado (com limite máximo)
        timeRemaining = Math.min(timeRemaining + difficulty.timePerTrash, maxTime);
        
        this.collected++;
        this.score += GameConfig.SCORE_PER_TRASH;
        checkGoal();
    }

    /**
     * Verifica se a meta de coleta foi atingida.
     * Se sim, aplica bônus de tempo (respeitando limite máximo) e pontuação.
     * Não aumenta a meta (meta fixa).
     * @return true se a meta foi atingida, false caso contrário
     */
    public boolean checkGoal() {
        if (collected >= currentGoal) {
            // Aplica bônus de tempo respeitando o limite máximo
            timeRemaining = Math.min(timeRemaining + difficulty.timeBonus, maxTime);
            score += GameConfig.SCORE_PER_GOAL;
            collected = 0; // reinicia contador, meta permanece fixa
            return true;
        }
        return false;
    }

    /**
     * Atualiza o tempo restante do jogo.
     * @param delta tempo desde o último frame
     */
    public void update(float delta) {
        timeRemaining -= delta;
        if (timeRemaining < 0) {
            timeRemaining = 0;
        }
    }

    /**
     * Verifica se o jogo terminou (tempo esgotado).
     * @return true se o jogo acabou, false caso contrário
     */
    public boolean isGameOver() {
        return timeRemaining <= 0;
    }

    /**
     * Retorna a dificuldade atual.
     * @return dificuldade
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    // Getters
    public float getTimeRemaining() {
        return timeRemaining;
    }

    public int getCurrentGoal() {
        return currentGoal;
    }

    public int getCollected() {
        return collected;
    }

    public int getScore() {
        return score;
    }
}
