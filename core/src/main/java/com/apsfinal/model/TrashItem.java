package com.apsfinal.model;

import com.badlogic.gdx.math.Rectangle;

/**
 * Entidade que representa um lixo no jogo.
 */
public class TrashItem {
    public float x, y;           // posição na tela
    public float lifeTimer;      // tempo restante de vida
    public float maxLife;        // tempo máximo (para calcular % da barra)
    public TrashType type;       // enum: BOTTLE, CAN, BAG
    public boolean collected;    // foi tocado pelo jogador?
    public boolean expired;      // timer chegou a zero?
    public Rectangle bounds;     // hitbox para detecção de toque

    // Enum interno para tipos de lixo
    public enum TrashType { BOTTLE, CAN, BAG }

    /**
     * Construtor do TrashItem.
     * @param x posição X inicial
     * @param y posição Y inicial
     * @param maxLife tempo máximo de vida
     * @param type tipo do lixo
     */
    public TrashItem(float x, float y, float maxLife, TrashType type) {
        this.x = x;
        this.y = y;
        this.maxLife = maxLife;
        this.lifeTimer = maxLife;
        this.type = type;
        this.collected = false;
        this.expired = false;
        this.bounds = new Rectangle(x, y, 60f, 60f);
    }
}