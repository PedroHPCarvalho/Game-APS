package com.apsfinal.config;

/**
 * Enum representando os níveis de dificuldade do jogo.
 * Cada dificuldade encapsula seus parâmetros específicos.
 */
public enum Difficulty {
    EASY(
        GameConfig.EASY_INITIAL_TIME,
        GameConfig.EASY_TIME_BONUS,
        GameConfig.EASY_TIME_PER_TRASH,
        GameConfig.EASY_MAX_TIME,
        GameConfig.EASY_SPAWN_MIN,
        GameConfig.EASY_SPAWN_MAX,
        GameConfig.EASY_TRASH_LIFE_MIN,
        GameConfig.EASY_TRASH_LIFE_MAX,
        GameConfig.EASY_MAX_TRASH
    ),
    MEDIUM(
        GameConfig.MEDIUM_INITIAL_TIME,
        GameConfig.MEDIUM_TIME_BONUS,
        GameConfig.MEDIUM_TIME_PER_TRASH,
        GameConfig.MEDIUM_MAX_TIME,
        GameConfig.MEDIUM_SPAWN_MIN,
        GameConfig.MEDIUM_SPAWN_MAX,
        GameConfig.MEDIUM_TRASH_LIFE_MIN,
        GameConfig.MEDIUM_TRASH_LIFE_MAX,
        GameConfig.MEDIUM_MAX_TRASH
    ),
    HARD(
        GameConfig.HARD_INITIAL_TIME,
        GameConfig.HARD_TIME_BONUS,
        GameConfig.HARD_TIME_PER_TRASH,
        GameConfig.HARD_MAX_TIME,
        GameConfig.HARD_SPAWN_MIN,
        GameConfig.HARD_SPAWN_MAX,
        GameConfig.HARD_TRASH_LIFE_MIN,
        GameConfig.HARD_TRASH_LIFE_MAX,
        GameConfig.HARD_MAX_TRASH
    );

    // Parâmetros
    public final float initialTime;
    public final float timeBonus;
    public final float timePerTrash;
    public final float maxTime;
    public final float spawnMin;
    public final float spawnMax;
    public final float trashLifeMin;
    public final float trashLifeMax;
    public final int maxTrash;

    Difficulty(float initialTime, float timeBonus, float timePerTrash, float maxTime,
                float spawnMin, float spawnMax,
                float trashLifeMin, float trashLifeMax,
                int maxTrash) {
        this.initialTime = initialTime;
        this.timeBonus = timeBonus;
        this.timePerTrash = timePerTrash;
        this.maxTime = maxTime;
        this.spawnMin = spawnMin;
        this.spawnMax = spawnMax;
        this.trashLifeMin = trashLifeMin;
        this.trashLifeMax = trashLifeMax;
        this.maxTrash = maxTrash;
    }
}