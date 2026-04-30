# GAME_DESIGN.md — AquaClean

## Visão Geral

| Campo         | Valor                          |
|---------------|--------------------------------|
| **Nome**      | AquaClean                      |
| **Gênero**    | Clicker / Tap game             |
| **Tema**      | Poluição Hídrica               |
| **Plataforma**| Android                        |
| **Engine**    | LibGDX (gdx-liftoff)           |

---

## Conceito Central

O jogador é um catador de lixo que precisa limpar um rio poluído tocando nos lixos antes que eles afundem. Cada nível exige coletar uma quantidade mínima de lixo dentro de um tempo limite. Coletar lixo suficiente concede tempo extra; deixar lixos afundarem penaliza o jogador.

---

## Fluxo de Telas

```
SplashScreen → MenuScreen → GameScreen → ResultScreen (Win/GameOver)
                                ↑                |
                                └────────────────┘ (jogar novamente)
```

---

## Telas

### SplashScreen
- Exibe logo/nome do jogo por 2 segundos.
- Transição automática para MenuScreen.

### MenuScreen
- Título "AquaClean" centralizado.
- Botão **JOGAR**.
- Botão **SAIR**.
- Fundo com animação de água (scrolling horizontal de textura).

### GameScreen
- Fundo: imagem/textura de água.
- Lixos spawnam em posições aleatórias na tela.
- HUD superior: tempo restante | lixo coletado / meta.
- Jogador toca no lixo para coletá-lo.
- Lixo não tocado some após seu timer individual expirar.
- Ao atingir a meta de coleta → ganhar tempo bônus e continuar.
- Ao tempo chegar a 0 → GameOver.

### ResultScreen
- **Win**: mensagem de parabéns + pontuação final.
- **Game Over**: mensagem + pontuação final.
- Botão **JOGAR NOVAMENTE**.
- Botão **MENU**.

---

## Mecânicas

### Spawn de Lixo
- Lixos aparecem em intervalos aleatórios entre **1.5s e 3.5s**.
- Posição X e Y aleatórias dentro de uma área segura (evitando bordas e HUD).
- Máximo de **8 lixos simultâneos** na tela.
- Cada lixo tem um **timer de vida** individual entre **3s e 6s** (aleatório por lixo).

### Timer de Vida do Lixo
- Indicado por uma barra de progresso ou ícone de tempo embaixo do asset.
- Quando o timer zera, o lixo some com uma animação de afundamento.
- Lixo que some **não** penaliza (apenas não contribui para a meta).

### Coleta
- Toque no sprite do lixo → lixo some com animação de coleta (splash/pop).
- Incrementa o contador `coletados`.
- Reproduz efeito sonoro curto (opcional).

### Sistema de Meta e Tempo
- **Tempo inicial**: 30 segundos.
- **Meta inicial**: coletar 10 lixos.
- Ao atingir a meta:
  - `tempoRestante += 15s`
  - `meta += 5` (próxima meta é maior)
  - Feedback visual de bônus na tela ("+15s").
- Jogo termina quando `tempoRestante <= 0`.

### Pontuação
- `score += 10` por lixo coletado.
- `score += 50` ao atingir cada meta.

---

## Assets Necessários

### Sprites de Lixo (mínimo 3 tipos)
- `trash_bottle.png` — garrafa plástica
- `trash_can.png` — lata de alumínio
- `trash_bag.png` — sacola plástica

> Os assets podem ser formas geométricas coloridas (retângulo/círculo com label) gerado via ShapeRenderer do LibGDX caso não haja imagens disponíveis. O agente deve usar placeholders funcionais.

### Backgrounds
- `bg_water.png` — textura de água (pode ser cor sólida azul com ondas simples via ShapeRenderer).

### UI
- Fonte bitmap padrão do LibGDX (`BitmapFont` built-in) para texto de HUD.
- Botões simples via `TextButton` do Scene2D.

---

## Parâmetros Configuráveis (constantes em GameConfig.java)

```java
INITIAL_TIME        = 30f      // segundos
INITIAL_GOAL        = 10       // lixos para atingir meta
GOAL_INCREMENT      = 5        // lixos adicionais por meta atingida
TIME_BONUS          = 15f      // segundos de bônus ao atingir meta
MAX_TRASH_ON_SCREEN = 8
SPAWN_INTERVAL_MIN  = 1.5f
SPAWN_INTERVAL_MAX  = 3.5f
TRASH_LIFE_MIN      = 3f
TRASH_LIFE_MAX      = 6f
SCORE_PER_TRASH     = 10
SCORE_PER_GOAL      = 50
```
