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
}