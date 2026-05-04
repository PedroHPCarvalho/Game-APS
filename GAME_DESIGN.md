# GAME_DESIGN.md --- AquaClean

## Visão Geral

  Campo            Valor
  ---------------- ----------------------
  **Nome**         AquaClean
  **Gênero**       Clicker / Tap Game
  **Tema**         Poluição Hídrica
  **Plataforma**   Android
  **Engine**       LibGDX (gdx-liftoff)

------------------------------------------------------------------------

## Conceito Central

O jogador controla um catador de lixo responsável por limpar um rio
poluído.

Lixos aparecem aleatoriamente na tela e devem ser coletados antes de
desaparecerem. O objetivo é atingir uma meta fixa de coleta dentro do
tempo limite.

Ao atingir a meta, o jogador recebe bônus de tempo e continua jogando. A
dificuldade é definida pelo modo selecionado, alterando velocidade de
spawn, tempo de vida dos lixos e quantidade máxima simultânea em tela.

O jogo termina quando o tempo restante chega a zero.

------------------------------------------------------------------------

## Fluxo de Telas

``` text
SplashScreen → MenuScreen → DifficultyScreen → GameScreen → ResultScreen
                                  ↑                |
                                  └────────────────┘ (jogar novamente)
```

------------------------------------------------------------------------

## Telas

### SplashScreen

-   Exibe logo/nome do jogo por 2 segundos.
-   Transição automática para `MenuScreen`.

### MenuScreen

-   Título **AquaClean** centralizado.
-   Botão **JOGAR**.
-   Botão **SAIR**.
-   Fundo azul simulando água.

### DifficultyScreen

-   Título **SELECIONE A DIFICULDADE**.
-   Botões:
    -   **FÁCIL**
    -   **MÉDIO**
    -   **DIFÍCIL**
-   Botão **VOLTAR** para `MenuScreen`.

Cada dificuldade altera parâmetros de gameplay.

### GameScreen

-   Fundo de água.
-   Spawn aleatório de lixos.
-   HUD superior:
    -   tempo restante
    -   lixo coletado / meta
    -   pontuação
-   Jogador toca no lixo para coletá-lo.
-   Ao atingir a meta:
    -   ganha bônus de tempo
    -   recebe pontuação bônus
    -   contador reinicia
    -   continua jogando

### ResultScreen

-   Exibe **GAME OVER**.
-   Mostra pontuação final.
-   Botão **JOGAR NOVAMENTE**.
-   Botão **MENU**.

------------------------------------------------------------------------

## Mecânicas

### Spawn de Lixo

-   Spawn aleatório entre intervalo mínimo e máximo definido pela
    dificuldade.
-   Posição aleatória dentro de área segura (sem sobrepor HUD).
-   Quantidade máxima de lixos simultâneos definida por dificuldade.

Tipos: - `BOTTLE` - `CAN` - `BAG`

### Timer de Vida do Lixo

Cada lixo possui timer individual.

-   Ao expirar:
    -   lixo desaparece
    -   não penaliza o jogador

Barra visual de vida exibida abaixo do sprite.

### Coleta

-   Toque no lixo para coletar.
-   Incrementa contador de coleta.
-   Incrementa pontuação.
-   Remove lixo da tela.

### Sistema de Meta e Tempo

-   Meta fixa de **10 lixos** para todas as dificuldades.
-   Ao atingir a meta:
    -   adiciona bônus de tempo
    -   adiciona bônus de score
    -   reinicia contador de coletados

Importante: - O tempo possui limite máximo igual ao tempo inicial da
dificuldade. - Isso evita partidas infinitas.

Exemplo:

``` java
timeRemaining = Math.min(timeRemaining + bonusTime, maxTime);
```

### Pontuação

-   `+10` por lixo coletado
-   `+50` ao atingir meta

------------------------------------------------------------------------

## Sistema de Dificuldade

  Parâmetro                Fácil      Médio        Difícil
  ------------------------ ---------- ------------ ----------
  Tempo inicial / máximo   45s        30s          20s
  Meta fixa                10         10           10
  Bônus por meta           +20s       +15s         +10s
  Vida do lixo             5s--8s     3s--6s       2s--4s
  Spawn                    1s--2.5s   1.5s--3.5s   0.8s--2s
  Máx lixos na tela        6          8            10

A dificuldade aumenta pela pressão de gameplay: - mais spawn - menor
tempo de vida - maior quantidade de lixos simultâneos

Não há aumento progressivo de meta.

------------------------------------------------------------------------

## Assets Necessários

### Sprites de Lixo

-   `trash_bottle.png`
-   `trash_can.png`
-   `trash_bag.png`

Placeholders podem ser usados: - formas geométricas via `ShapeRenderer`

### Background

-   `bg_water.png`

ou cor sólida azul.

### UI

-   `BitmapFont`
-   `TextButton`

------------------------------------------------------------------------

## Parâmetros Configuráveis (`GameConfig.java`)

``` java
// score
SCORE_PER_TRASH = 10;
SCORE_PER_GOAL = 50;

// meta
FIXED_GOAL = 10;

// easy
EASY_INITIAL_TIME = 45f;
EASY_TIME_BONUS = 20f;
EASY_MAX_TIME = 45f;
EASY_SPAWN_MIN = 1f;
EASY_SPAWN_MAX = 2.5f;
EASY_TRASH_LIFE_MIN = 5f;
EASY_TRASH_LIFE_MAX = 8f;
EASY_MAX_TRASH = 6;

// medium
MEDIUM_INITIAL_TIME = 30f;
MEDIUM_TIME_BONUS = 15f;
MEDIUM_MAX_TIME = 30f;
MEDIUM_SPAWN_MIN = 1.5f;
MEDIUM_SPAWN_MAX = 3.5f;
MEDIUM_TRASH_LIFE_MIN = 3f;
MEDIUM_TRASH_LIFE_MAX = 6f;
MEDIUM_MAX_TRASH = 8;

// hard
HARD_INITIAL_TIME = 20f;
HARD_TIME_BONUS = 10f;
HARD_MAX_TIME = 20f;
HARD_SPAWN_MIN = 0.8f;
HARD_SPAWN_MAX = 2f;
HARD_TRASH_LIFE_MIN = 2f;
HARD_TRASH_LIFE_MAX = 4f;
HARD_MAX_TRASH = 10;
```

------------------------------------------------------------------------

## Melhorias Futuras

-   animação de coleta (splash/pop)
-   animação de afundamento
-   efeitos sonoros
-   leaderboard/local highscore
-   tela de vitória por tempo sobrevivido ou metas concluídas
