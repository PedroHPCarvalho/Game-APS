package com.apsfinal.manager;

import com.apsfinal.config.GameConfig;
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

    /**
     * Construtor inicializa a lista e o primeiro intervalo de spawn.
     */
    public TrashManager() {
        this.activeTrash = new ArrayList<TrashItem>();
        this.spawnTimer = 0f;
        this.spawnInterval = MathUtils.random(GameConfig.SPAWN_INTERVAL_MIN, GameConfig.SPAWN_INTERVAL_MAX);
    }

    /**
     * Atualiza o estado dos lixos.
     * @param delta tempo desde o último frame
     * @param scoreManager gerenciador de pontuação para notificar coletas
     */
    public void update(float delta, ScoreManager scoreManager) {
        // Atualiza timers e remove lixos coletados ou expirados
        Iterator<TrashItem> iterator = activeTrash.iterator();
        while (iterator.hasNext()) {
            TrashItem item = iterator.next();
            if (item.collected) {
                scoreManager.addCollected();
                iterator.remove();
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
        if (spawnTimer >= spawnInterval && activeTrash.size() < GameConfig.MAX_TRASH_ON_SCREEN) {
            spawnTrash();
            spawnTimer = 0f;
            spawnInterval = MathUtils.random(GameConfig.SPAWN_INTERVAL_MIN, GameConfig.SPAWN_INTERVAL_MAX);
        }
    }

    /**
     * Spawna um novo lixo com posição e tipo aleatórios.
     */
    private void spawnTrash() {
        float x = MathUtils.random(40f, GameConfig.VIRTUAL_WIDTH - 100f);
        float y = MathUtils.random(100f, GameConfig.VIRTUAL_HEIGHT - 150f);
        float maxLife = MathUtils.random(GameConfig.TRASH_LIFE_MIN, GameConfig.TRASH_LIFE_MAX);
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