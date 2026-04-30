package com.apsfinal.screen;

import com.apsfinal.AquaCleanGame;
import com.apsfinal.config.GameConfig;
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
 * Tela de menu principal do jogo.
 */
public class MenuScreen implements Screen {
    private AquaCleanGame game;
    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Skin skin;

    /**
     * Construtor da tela de menu.
     * @param game instância principal do jogo
     */
    public MenuScreen(AquaCleanGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(GameConfig.VIRTUAL_WIDTH, GameConfig.VIRTUAL_HEIGHT, camera);
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

        // Configura o InputProcessor para a Stage
        Gdx.input.setInputProcessor(stage);

        // Cria a tabela centralizada
        Table table = new Table();
        table.setFillParent(true);

        // Título do jogo
        Label titleLabel = new Label("AquaClean", new Label.LabelStyle(new com.badlogic.gdx.graphics.g2d.BitmapFont(), Color.WHITE));
        titleLabel.setFontScale(3f);
        table.add(titleLabel).padBottom(50f).row();

        // Botão JOGAR
        TextButton playButton = new TextButton("JOGAR", skin);
        playButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
                dispose();
            }
        });
        table.add(playButton).size(200f, 60f).padBottom(20f).row();

        // Botão SAIR
        TextButton exitButton = new TextButton("SAIR", skin);
        exitButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        table.add(exitButton).size(200f, 60f);

        stage.addActor(table);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Limpa a tela com azul (fundo de água)
        Gdx.gl.glClearColor(0.1f, 0.4f, 0.7f, 1f);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);

        // Atualiza e desenha a Stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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