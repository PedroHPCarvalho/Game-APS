package com.apsfinal.config;

/**
 * Configurações globais do jogo AquaClean.
 * Todas as constantes devem ficar nesta classe (nenhum número mágico no código).
 */
public class GameConfig {
    // Resolução virtual
    public static final float VIRTUAL_WIDTH = 480f;
    public static final float VIRTUAL_HEIGHT = 854f;

    // Tempo e metas
    public static final float INITIAL_TIME = 30f;
    public static final int INITIAL_GOAL = 10;
    public static final int GOAL_INCREMENT = 5;
    public static final float TIME_BONUS = 15f;

    // Lixos
    public static final int MAX_TRASH_ON_SCREEN = 8;
    public static final float SPAWN_INTERVAL_MIN = 1.5f;
    public static final float SPAWN_INTERVAL_MAX = 3.5f;
    public static final float TRASH_LIFE_MIN = 3f;
    public static final float TRASH_LIFE_MAX = 6f;

    // Pontuação
    public static final int SCORE_PER_TRASH = 10;
    public static final int SCORE_PER_GOAL = 50;

    // Meta fixa (todas as dificuldades)
    public static final int FIXED_GOAL = 10;

        // Dificuldade: Fácil
        public static final float EASY_INITIAL_TIME = 45f;
        public static final float EASY_TIME_BONUS = 5f;
        public static final float EASY_TIME_PER_TRASH = 2f;
        public static final float EASY_MAX_TIME = 45f;
        public static final float EASY_SPAWN_MIN = 1f;
        public static final float EASY_SPAWN_MAX = 2.5f;
        public static final float EASY_TRASH_LIFE_MIN = 5f;
        public static final float EASY_TRASH_LIFE_MAX = 8f;
        public static final int EASY_MAX_TRASH = 6;

    // Dificuldade: Médio
    public static final float MEDIUM_INITIAL_TIME = 30f;
    public static final float MEDIUM_TIME_BONUS = 5f;
    public static final float MEDIUM_TIME_PER_TRASH = 1.5f;
    public static final float MEDIUM_MAX_TIME = 30f;
    public static final float MEDIUM_SPAWN_MIN = 1.5f;
    public static final float MEDIUM_SPAWN_MAX = 3.5f;
    public static final float MEDIUM_TRASH_LIFE_MIN = 3f;
    public static final float MEDIUM_TRASH_LIFE_MAX = 6f;
    public static final int MEDIUM_MAX_TRASH = 8;

    // Dificuldade: Difícil
    public static final float HARD_INITIAL_TIME = 20f;
    public static final float HARD_TIME_BONUS = 3f;
    public static final float HARD_TIME_PER_TRASH = 1f;
    public static final float HARD_MAX_TIME = 20f;
    public static final float HARD_SPAWN_MIN = 0.8f;
    public static final float HARD_SPAWN_MAX = 2f;
    public static final float HARD_TRASH_LIFE_MIN = 2f;
    public static final float HARD_TRASH_LIFE_MAX = 4f;
    public static final int HARD_MAX_TRASH = 10;
}
