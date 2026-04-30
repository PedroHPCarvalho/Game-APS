# AGENT_INSTRUCTIONS.md — AquaClean

## Papel do Agente

Você é o desenvolvedor responsável por implementar o jogo **AquaClean** do zero, usando LibGDX em um projeto gerado pelo **gdx-liftoff**. Siga as especificações de `GAME_DESIGN.md` e `ARCHITECTURE.md` rigorosamente. Execute as tarefas na ordem definida abaixo.

---

## Regras Gerais

1. **Nunca use números mágicos** — todas as constantes ficam em `GameConfig.java`.
2. **Sempre chame `dispose()`** em todo `Disposable` (Texture, Stage, SpriteBatch, ShapeRenderer, etc.).
3. **Não use campos `static`** para instâncias LibGDX.
4. **Resolução virtual**: `480 x 854` com `FitViewport`.
5. **Placeholders são válidos**: se não houver assets de imagem, use `ShapeRenderer`. O jogo deve ser jogável mesmo sem arquivos `.png`.
6. **Compile e teste mentalmente** cada classe antes de passar para a próxima tarefa.
7. **Idioma do código**: inglês (nomes de variáveis, métodos, classes). Comentários podem ser em português.

---

## Tarefas

### TASK-01 — GameConfig.java
**Arquivo**: `core/src/com/aquaclean/config/GameConfig.java`

Criar classe com todas as constantes públicas estáticas finais:
- `VIRTUAL_WIDTH = 480f`
- `VIRTUAL_HEIGHT = 854f`
- `INITIAL_TIME = 30f`
- `INITIAL_GOAL = 10`
- `GOAL_INCREMENT = 5`
- `TIME_BONUS = 15f`
- `MAX_TRASH_ON_SCREEN = 8`
- `SPAWN_INTERVAL_MIN = 1.5f`
- `SPAWN_INTERVAL_MAX = 3.5f`
- `TRASH_LIFE_MIN = 3f`
- `TRASH_LIFE_MAX = 6f`
- `SCORE_PER_TRASH = 10`
- `SCORE_PER_GOAL = 50`

---

### TASK-02 — TrashItem.java
**Arquivo**: `core/src/com/aquaclean/model/TrashItem.java`

Criar a entidade `TrashItem` conforme especificado em `ARCHITECTURE.md`.
- Incluir enum `TrashType { BOTTLE, CAN, BAG }` como classe interna pública.
- `bounds` deve ser um `com.badlogic.gdx.math.Rectangle`.
- Construtor deve receber `(float x, float y, float maxLife, TrashType type)` e inicializar todos os campos, incluindo `bounds` com tamanho `60x60`.

---

### TASK-03 — ScoreManager.java
**Arquivo**: `core/src/com/aquaclean/manager/ScoreManager.java`

Implementar conforme `ARCHITECTURE.md`:
- Construtor inicializa `timeRemaining = GameConfig.INITIAL_TIME`, `currentGoal = GameConfig.INITIAL_GOAL`.
- `addCollected()`: `collected++`, `score += SCORE_PER_TRASH`, chama `checkGoal()`.
- `checkGoal()`: se `collected >= currentGoal`, então `timeRemaining += TIME_BONUS`, `score += SCORE_PER_GOAL`, `currentGoal += GOAL_INCREMENT`, `collected = 0`. Deve retornar `boolean` indicando se a meta foi atingida.
- `update(float delta)`: `timeRemaining -= delta`. Nunca deixa `timeRemaining < 0`.
- `isGameOver()`: retorna `timeRemaining <= 0`.
- Getters para todos os campos necessários pelo HUD.

---

### TASK-04 — TrashManager.java
**Arquivo**: `core/src/com/aquaclean/manager/TrashManager.java`

Implementar conforme `ARCHITECTURE.md`:
- `spawnTimer` acumulado com intervalo aleatório gerado por `MathUtils.random(MIN, MAX)`.
- Spawn respeita `MAX_TRASH_ON_SCREEN`.
- Posição X: `MathUtils.random(40f, GameConfig.VIRTUAL_WIDTH - 100f)`.
- Posição Y: `MathUtils.random(100f, GameConfig.VIRTUAL_HEIGHT - 150f)` (evita HUD no topo).
- Tipo: escolhido aleatoriamente entre os valores de `TrashType`.
- `update(float delta, ScoreManager scoreManager)`:
  - Usa `Iterator` para remover itens `collected` ou `expired` da lista de forma segura.
  - Itens coletados chamam `scoreManager.addCollected()` antes de serem removidos.
  - Decrementa `lifeTimer` de todos os itens ativos.
- `handleTap(float x, float y)`: retorna `boolean`. Itera a lista e usa `bounds.contains(x, y)`. Marca o primeiro item tocado como `collected = true` e retorna `true`.
- `render(ShapeRenderer shape)`:
  - Cores por tipo: BOTTLE = azul claro (0.4, 0.7, 1, 1), CAN = cinza (0.6, 0.6, 0.6, 1), BAG = amarelo (1, 0.85, 0.2, 1).
  - Renderiza retângulo preenchido para o sprite placeholder.
  - Renderiza barra de vida (retângulo vermelho/verde) proporcional a `lifeTimer / maxLife`, logo abaixo do lixo.
- `dispose()`: limpa a lista.

---

### TASK-05 — HudRenderer.java
**Arquivo**: `core/src/com/aquaclean/ui/HudRenderer.java`

- Recebe `ScoreManager` no construtor.
- Cria `BitmapFont` interno com escala `2f` para legibilidade mobile.
- `render(SpriteBatch batch, OrthographicCamera hudCamera)`:
  - Ativa o batch com `hudCamera`.
  - Topo esquerdo: `"TEMPO: " + (int)timeRemaining + "s"`.
  - Topo centro: `"LIXO: " + collected + "/" + currentGoal`.
  - Topo direito: `"SCORE: " + score`.
- `dispose()`: descarta a font.

---

### TASK-06 — AquaCleanGame.java
**Arquivo**: `core/src/com/aquaclean/AquaCleanGame.java`

- Extende `Game`.
- `SpriteBatch batch` e `ShapeRenderer shapeRenderer` públicos (compartilhados entre screens).
- `create()`: inicializa batch, shapeRenderer, navega para `new SplashScreen(this)`.
- `dispose()`: descarta batch e shapeRenderer.

> **Atenção**: o nome do pacote e da classe principal devem corresponder ao que o gdx-liftoff gerou. Ajuste o pacote se necessário.

---

### TASK-07 — SplashScreen.java
**Arquivo**: `core/src/com/aquaclean/screen/SplashScreen.java`

- Acumula `elapsed += delta`.
- Quando `elapsed >= 2f`: `game.setScreen(new MenuScreen(game))`, descarta a splash.
- `render()`: limpa tela com cor azul escura, exibe "AquaClean" centralizado via `BitmapFont` (escala 4f).
- Usa `OrthographicCamera` + `FitViewport`.

---

### TASK-08 — MenuScreen.java
**Arquivo**: `core/src/com/aquaclean/screen/MenuScreen.java`

- Usa `Stage` com `FitViewport`.
- `Table` centralizada com:
  - Label "AquaClean" (fonte grande).
  - `TextButton` "JOGAR" → `game.setScreen(new GameScreen(game))`.
  - `TextButton` "SAIR" → `Gdx.app.exit()`.
- Fundo: `glClearColor` azul (0.1f, 0.4f, 0.7f, 1f).
- `inputMultiplexer` com a Stage.
- `dispose()` descarta Stage.

---

### TASK-09 — GameScreen.java
**Arquivo**: `core/src/com/aquaclean/screen/GameScreen.java`

Esta é a tela principal. Implementar:

**Campos**:
- `TrashManager trashManager`
- `ScoreManager scoreManager`
- `HudRenderer hudRenderer`
- `OrthographicCamera camera` (jogo)
- `OrthographicCamera hudCamera` (HUD, não move)
- `FitViewport viewport`
- `boolean goalJustReached` + `float goalMessageTimer` (para mostrar "+15s" na tela)

**`show()`**: inicializa todos os campos, configura `Gdx.input.setInputProcessor` com `InputAdapter` para `touchDown`.

**`touchDown`**:
```java
float worldX = (screenX / (float) Gdx.graphics.getWidth()) * GameConfig.VIRTUAL_WIDTH;
float worldY = ((Gdx.graphics.getHeight() - screenY) / (float) Gdx.graphics.getHeight()) * GameConfig.VIRTUAL_HEIGHT;
trashManager.handleTap(worldX, worldY);
```

**`render(float delta)`**:
1. `scoreManager.update(delta)`.
2. `trashManager.update(delta, scoreManager)` — verifica se meta foi atingida via retorno de `addCollected` ou polling de `checkGoal`.
3. Se `scoreManager.isGameOver()` → `game.setScreen(new ResultScreen(game, scoreManager.getScore()))`.
4. Limpa tela (azul).
5. Renderiza fundo de água (ShapeRenderer, cor azul).
6. `trashManager.render(game.shapeRenderer)`.
7. `hudRenderer.render(game.batch, hudCamera)`.
8. Se `goalJustReached`: exibe "+15s" centralizado por 1.5s.

**`resize()`**: atualiza viewport.
**`dispose()`**: descarta trashManager, hudRenderer.

---

### TASK-10 — ResultScreen.java
**Arquivo**: `core/src/com/aquaclean/screen/ResultScreen.java`

- Recebe `score` no construtor.
- `Stage` com `Table`:
  - Label "GAME OVER".
  - Label "Pontuação: {score}".
  - `TextButton` "JOGAR NOVAMENTE" → `game.setScreen(new GameScreen(game))`.
  - `TextButton` "MENU" → `game.setScreen(new MenuScreen(game))`.
- Fundo vermelho escuro (0.5f, 0.05f, 0.05f, 1f).
- `dispose()` descarta Stage.

---

### TASK-11 — Verificação Final

Antes de encerrar, confirme:

- [ ] Todas as classes têm `dispose()` correto.
- [ ] Nenhum número mágico fora de `GameConfig.java`.
- [ ] `AndroidLauncher.java` aponta para `AquaCleanGame` (gerado pelo liftoff, apenas verificar).
- [ ] O jogo compila sem erros.
- [ ] O loop completo funciona: Menu → Jogo → GameOver → Menu.
- [ ] Toque em lixo funciona e decrementa o timer do jogo.
- [ ] Ao atingir meta, o tempo aumenta e a meta sobe.

---

## Ordem de Execução Recomendada

```
TASK-01 → TASK-02 → TASK-03 → TASK-04 → TASK-05 →
TASK-06 → TASK-07 → TASK-08 → TASK-09 → TASK-10 → TASK-11
```

Não pule tarefas. Cada task depende das anteriores.
