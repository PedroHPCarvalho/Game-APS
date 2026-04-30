package com.apsfinal.screen;

import com.apsfinal.AquaCleanGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Tela de resultado do jogo (Game Over ou Vitória).
 */
public class ResultScreen implements Screen {
    private AquaCleanGame game;
    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Skin skin;
    private int finalScore;

    /**
     * Construtor da tela de resultado.
     * @param game instância principal do jogo
     * @param score pontuação final do jogador
     */
    public ResultScreen(AquaCleanGame game, int score) {
        this.game = game;
        this.finalScore = score;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(480f, 854f, camera);
        this.stage = new Stage(viewport);
        // Cria skin programaticamente para evitar erro de arquivo ausente
        this.skin = new Skin();
        com.badlogic.gdx.graphics.g2d.BitmapFont skinFont = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        this.skin.add("default-font", skinFont);
        
        // Estilo para Label
        com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle labelStyle = new com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle();
        labelStyle.font = skinFont;
        this.skin.add("default", labelStyle);
        
        // Estilo para TextButton
        com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle buttonStyle = new com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle();
        buttonStyle.font = skinFont;
        this.skin.add("default", buttonStyle);

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        // Título GAME OVER
        Label gameOverLabel = new Label("GAME OVER", new Label.LabelStyle(new com.badlogic.gdx.graphics.g2d.BitmapFont(), Color.WHITE));
        gameOverLabel.setFontScale(3f);
        table.add(gameOverLabel).padBottom(30f).row();

        // Pontuação final
        Label scoreLabel = new Label("Pontuação: " + finalScore, new Label.LabelStyle(new com.badlogic.gdx.graphics.g2d.BitmapFont(), Color.WHITE));
        scoreLabel.setFontScale(2f);
        table.add(scoreLabel).padBottom(50f).row();

        // Botão JOGAR NOVAMENTE
        TextButton playAgainButton = new TextButton("JOGAR NOVAMENTE", skin);
        playAgainButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
        table.add(playAgainButton).size(com.badlogic.gdx.scenes.scene2d.ui.Value.percentWidth(0.5f, table), com.badlogic.gdx.scenes.scene2d.ui.Value.percentHeight(0.08f, table)).padBottom(20f).row();

        // Botão MENU
        TextButton menuButton = new TextButton("MENU", skin);
        menuButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
                dispose();
            }
        });
        table.add(menuButton).size(com.badlogic.gdx.scenes.scene2d.ui.Value.percentWidth(0.4f, table), com.badlogic.gdx.scenes.scene2d.ui.Value.percentHeight(0.08f, table));

        stage.addActor(table);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Fundo vermelho escuro
        Gdx.gl.glClearColor(0.5f, 0.05f, 0.05f, 1f);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Detecta orientação física
        boolean isLandscape = width > height;
        float newWorldWidth;
        float newWorldHeight;
        if (isLandscape) {
            // Paisagem: inverte para 16:9
            newWorldWidth = 854f;  // GameConfig.VIRTUAL_HEIGHT
            newWorldHeight = 480f; // GameConfig.VIRTUAL_WIDTH
        } else {
            // Retrato: mantém 9:16
            newWorldWidth = 480f; // GameConfig.VIRTUAL_WIDTH
            newWorldHeight = 854f; // GameConfig.VIRTUAL_HEIGHT
        }
        viewport.setWorldSize(newWorldWidth, newWorldHeight);
        viewport.update(width, height, true);
        // Atualiza a câmera
        if (camera != null) {
            camera.position.set(newWorldWidth / 2, newWorldHeight / 2, 0);
            camera.update();
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (skin != null) {
            skin.dispose();
        }
    }
}