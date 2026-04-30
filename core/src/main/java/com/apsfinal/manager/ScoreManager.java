package com.apsfinal.manager;

import com.apsfinal.config.GameConfig;

/**
 * Gerencia pontuação, tempo e metas do jogo.
 */
public class ScoreManager {
    private float timeRemaining;
    private int currentGoal;
    private int collected;
    private int score;

    /**
     * Construtor inicializa os valores iniciais do jogo.
     */
    public ScoreManager() {
        this.timeRemaining = GameConfig.INITIAL_TIME;
        this.currentGoal = GameConfig.INITIAL_GOAL;
        this.collected = 0;
        this.score = 0;
    }

    /**
     * Adiciona um lixo coletado e verifica se atingiu a meta.
     */
    public void addCollected() {
        this.collected++;
        this.score += GameConfig.SCORE_PER_TRASH;
        checkGoal();
    }

    /**
     * Verifica se a meta de coleta foi atingida.
     * Se sim, aplica bônus de tempo e aumenta a meta.
     * @return true se a meta foi atingida, false caso contrário
     */
    public boolean checkGoal() {
        if (collected >= currentGoal) {
            timeRemaining += GameConfig.TIME_BONUS;
            score += GameConfig.SCORE_PER_GOAL;
            currentGoal += GameConfig.GOAL_INCREMENT;
            collected = 0;
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