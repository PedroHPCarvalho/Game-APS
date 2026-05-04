package com.apsfinal.manager;

import com.apsfinal.config.GameConfig;
import com.apsfinal.config.Difficulty;
import com.apsfinal.model.TrashItem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Gerencia o spawn, atualização e remoção de lixos na tela.
 */
public class TrashManager {
    private List<TrashItem> activeTrash;
    private float spawnTimer;
    private float spawnInterval;
    private float virtualWidth;
    private float virtualHeight;
    private Difficulty difficulty;
    private float spawnMin;
    private float spawnMax;
    private float trashLifeMin;
    private float trashLifeMax;
    private int maxTrash;

    /**
     * Construtor padrão (usa dificuldade MÉDIO para compatibilidade).
     */
    public TrashManager() {
        this(Difficulty.MEDIUM);
    }

    /**
     * Construtor com dificuldade específica.
     * @param difficulty nível de dificuldade
     */
    public TrashManager(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.spawnMin = difficulty.spawnMin;
        this.spawnMax = difficulty.spawnMax;
        this.trashLifeMin = difficulty.trashLifeMin;
        this.trashLifeMax = difficulty.trashLifeMax;
        this.maxTrash = difficulty.maxTrash;
        this.activeTrash = new ArrayList<TrashItem>();
        this.spawnTimer = 0f;
        this.spawnInterval = MathUtils.random(spawnMin, spawnMax);
        // Inicializa com proporção retrato (padrão)
        this.virtualWidth = GameConfig.VIRTUAL_WIDTH;  // 480
        this.virtualHeight = GameConfig.VIRTUAL_HEIGHT; // 854
    }

    /**
     * Atualiza as dimensões virtuais conforme a orientação do dispositivo.
     * @param width nova largura virtual
     * @param height nova altura virtual
     */
    public void setVirtualBounds(float width, float height) {
        this.virtualWidth = width;
        this.virtualHeight = height;
    }

    /**
     * Atualiza o estado dos lixos.
     * @param delta tempo desde o último frame
     * @param scoreManager gerenciador de pontuação para notificar coletas
     * @return número de lixos coletados nesta atualização
     */
    public int update(float delta, ScoreManager scoreManager) {
        int collectedCount = 0;
        
        // Atualiza timers e remove lixos coletados ou expirados
        Iterator<TrashItem> iterator = activeTrash.iterator();
        while (iterator.hasNext()) {
            TrashItem item = iterator.next();
            if (item.collected) {
                scoreManager.addCollected();
                iterator.remove();
                collectedCount++;
            } else if (item.expired) {
                iterator.remove();
            } else {
                item.lifeTimer -= delta;
                if (item.lifeTimer <= 0) {
                    item.expired = true;
                }
            }
        }

        // Spawn de novos lixos
        spawnTimer += delta;
        if (spawnTimer >= spawnInterval && activeTrash.size() < maxTrash) {
            spawnTrash();
            spawnTimer = 0f;
            spawnInterval = MathUtils.random(spawnMin, spawnMax);
        }
        
        return collectedCount;
    }

    /**
     * Spawna um novo lixo com posição e tipo aleatórios.
     */
    private void spawnTrash() {
        float x = MathUtils.random(40f, virtualWidth - 100f);
        float y = MathUtils.random(100f, virtualHeight - 150f);
        float maxLife = MathUtils.random(trashLifeMin, trashLifeMax);
        TrashItem.TrashType[] types = TrashItem.TrashType.values();
        TrashItem.TrashType type = types[MathUtils.random(types.length - 1)];
        activeTrash.add(new TrashItem(x, y, maxLife, type));
    }

    /**
     * Verifica se um toque na tela atingiu algum lixo.
     * @param x coordenada X do toque
     * @param y coordenada Y do toque
     * @return true se coletou um lixo, false caso contrário
     */
    public boolean handleTap(float x, float y) {
        for (TrashItem item : activeTrash) {
            if (item.bounds.contains(x, y)) {
                item.collected = true;
                return true;
            }
        }
        return false;
    }

    /**
     * Renderiza os lixos e suas barras de vida.
     * @param shape ShapeRenderer para desenhar formas geométricas (já deve estar com begin() chamado)
     */
    public void render(ShapeRenderer shape) {
        for (TrashItem item : activeTrash) {
            // Define cor baseada no tipo
            switch (item.type) {
                case BOTTLE:
                    shape.setColor(new Color(0.4f, 0.7f, 1f, 1f)); // azul claro
                    break;
                case CAN:
                    shape.setColor(new Color(0.6f, 0.6f, 0.6f, 1f)); // cinza
                    break;
                case BAG:
                    shape.setColor(new Color(1f, 0.85f, 0.2f, 1f)); // amarelo
                    break;
            }
            // Desenha o lixo (retângulo placeholder)
            shape.rect(item.x, item.y, 60f, 60f);

            // Barra de vida
            float lifePercent = item.lifeTimer / item.maxLife;
            float barWidth = 60f;
            float barHeight = 5f;
            if (lifePercent > 0.5f) {
                shape.setColor(Color.GREEN);
            } else {
                shape.setColor(Color.RED);
            }
            shape.rect(item.x, item.y - 10f, barWidth * lifePercent, barHeight);
        }
    }

    /**
     * Limpa a lista de lixos ativos.
     */
    public void dispose() {
        activeTrash.clear();
    }
}