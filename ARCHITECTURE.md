# ARCHITECTURE.md — AquaClean

## Stack

- **Engine**: LibGDX (gerado via gdx-liftoff)
- **Linguagem**: Java 11+
- **Módulos**: `core/` (lógica), `android/` (launcher)
- **UI**: Scene2D (Stage + Actors) para menus e HUD
- **Renderização de jogo**: SpriteBatch + ShapeRenderer

---

## Estrutura de Pacotes (`core/src/`)

```
com.aquaclean/
├── AquaCleanGame.java          # Classe principal (Game), gerencia screens
├── config/
│   └── GameConfig.java         # Todas as constantes do jogo
├── screen/
│   ├── SplashScreen.java
│   ├── MenuScreen.java
│   ├── GameScreen.java
│   └── ResultScreen.java
├── model/
│   └── TrashItem.java          # Entidade lixo (posição, timer, tipo, estado)
├── manager/
│   ├── TrashManager.java       # Spawn, update e remoção de lixos
│   └── ScoreManager.java       # Pontuação e metas
└── ui/
    └── HudRenderer.java        # Renderiza HUD (tempo, score, meta)
```

---

## Classe Principal

### `AquaCleanGame.java`
- Extende `Game` do LibGDX.
- No `create()`: inicializa `AssetManager`, define `SpriteBatch` compartilhado, navega para `SplashScreen`.
- Expõe métodos `navigateTo(Screen screen)` para troca de telas.
- Gerencia `dispose()` do batch e assets globais.

---

## Screens

Todas as screens implementam `Screen` do LibGDX.

### `SplashScreen`
- Exibe título por 2s via `delta` acumulado.
- Chama `game.navigateTo(new MenuScreen(game))` após o tempo.

### `MenuScreen`
- Usa `Stage` + `Table` do Scene2D.
- Botão JOGAR → `game.navigateTo(new GameScreen(game))`.
- Botão SAIR → `Gdx.app.exit()`.
- Fundo: ShapeRenderer ou textura de água.

### `GameScreen`
- Responsabilidade central do jogo.
- Possui instâncias de `TrashManager`, `ScoreManager`, `HudRenderer`.
- Loop `render(delta)`:
  1. Limpa tela.
  2. Atualiza `TrashManager` (spawn + timers).
  3. Verifica input de toque via `Gdx.input` ou `InputProcessor`.
  4. Verifica condição de GameOver.
  5. Renderiza fundo → lixos → HUD.
- Ao GameOver: `game.navigateTo(new ResultScreen(game, score, false))`.
- Ao atingir meta: aplica bônus de tempo via `ScoreManager`.

### `ResultScreen`
- Recebe `score` e `boolean win` no construtor.
- Exibe mensagem e pontuação.
- Botões JOGAR NOVAMENTE e MENU.

---

## Modelo

### `TrashItem.java`
```java
public class TrashItem {
    public float x, y;           // posição na tela
    public float lifeTimer;      // tempo restante de vida
    public float maxLife;        // tempo máximo (para calcular % da barra)
    public TrashType type;       // enum: BOTTLE, CAN, BAG
    public boolean collected;    // foi tocado pelo jogador?
    public boolean expired;      // timer chegou a zero?
    public Rectangle bounds;     // hitbox para detecção de toque

    // Enum interno
    public enum TrashType { BOTTLE, CAN, BAG }
}
```

---

## Managers

### `TrashManager.java`
- Lista `List<TrashItem> activeTrash`.
- `spawnTimer` acumulado; quando >= intervalo aleatório → spawna novo `TrashItem`.
- `update(float delta)`:
  - Decrementa `lifeTimer` de cada item.
  - Marca `expired = true` quando `lifeTimer <= 0`.
  - Remove itens `collected` ou `expired` da lista.
  - Spawna novos lixos respeitando `MAX_TRASH_ON_SCREEN`.
- `handleTap(float screenX, float screenY)`:
  - Converte coordenadas de toque para coordenadas de mundo.
  - Verifica colisão com `bounds` de cada `TrashItem`.
  - Marca primeiro item tocado como `collected = true`.
  - Retorna `true` se coletou, `false` caso contrário.
- `render(SpriteBatch batch, ShapeRenderer shape)`:
  - Renderiza sprite ou forma geométrica de cada lixo.
  - Renderiza barra de vida proporcional (`lifeTimer / maxLife`).

### `ScoreManager.java`
- Campos: `score`, `collected`, `currentGoal`, `timeRemaining`.
- `addCollected()`: incrementa `collected` e `score`.
- `checkGoal()`: verifica se `collected >= currentGoal`; se sim, aplica bônus.
- `update(float delta)`: decrementa `timeRemaining`.
- `isGameOver()`: retorna `timeRemaining <= 0`.

---

## HUD

### `HudRenderer.java`
- Recebe `ScoreManager` no construtor.
- `render(SpriteBatch batch)`:
  - Tempo restante (topo esquerdo).
  - `coletados / meta` (topo centro).
  - Score (topo direito).
- Usa `BitmapFont` padrão do LibGDX. Escala ajustada para mobile.

---

## Input

- `GameScreen` implementa `InputProcessor` ou usa `InputAdapter`.
- `touchDown(int screenX, int screenY, ...)`:
  - Converte Y (LibGDX Y invertido): `worldY = Gdx.graphics.getHeight() - screenY`.
  - Chama `trashManager.handleTap(screenX, worldY)`.

---

## Viewport e Câmera

- `FitViewport` com resolução virtual `480 x 854` (proporção 9:16).
- `OrthographicCamera` vinculada ao viewport.
- Garante consistência visual em diferentes tamanhos de tela Android.

---

## Assets (placeholder)

Caso não haja imagens, o agente deve implementar via `ShapeRenderer`:

| Asset         | Placeholder                          |
|---------------|--------------------------------------|
| Água (fundo)  | `glClearColor` azul + ondas simples  |
| BOTTLE        | Retângulo azul claro                 |
| CAN           | Retângulo cinza                      |
| BAG           | Retângulo amarelo                    |

Quando arquivos `.png` estiverem em `android/assets/`, substituir por `Texture` + `Sprite`.

---

## Convenções

- Todos os `Disposable` (Texture, SpriteBatch, Stage, ShapeRenderer) devem ser descartados no `dispose()` da respectiva Screen.
- Não usar `static` para instâncias LibGDX (causam vazamento de memória entre screens).
- Constantes APENAS em `GameConfig.java`. Nenhum número mágico no código.
