package checamon.games.virtuacards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by angelcheca on 28/11/15.
 */
public class VirtuaCardsMainMenu implements Screen, InputProcessor {
    final VirtuaCards game;
    private boolean exitScreen;
    private Skin skin;

    private TextButton playButton;
    private TextButton settingsButton;
    private TextButton exitButton;


    public VirtuaCardsMainMenu (final VirtuaCards g){
        game = g;
        exitScreen = false;
        skin = new Skin();

        Gdx.input.setInputProcessor(this);


        Pixmap pixmap = new Pixmap(400, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.GREEN);
        pixmap.fill();

        BitmapFont bfont= new BitmapFont();
        bfont.getData().setScale(2f,2f);
        skin.add("default", bfont);

        skin.add("white", new Texture(pixmap));

        TextButtonStyle buttonStyle;

        buttonStyle = new TextButtonStyle();
        buttonStyle.up = skin.newDrawable("white", Color.GRAY);
        buttonStyle.down = skin.newDrawable("white", Color.BLUE);
        buttonStyle.over = skin.newDrawable("white",Color.LIGHT_GRAY);
        buttonStyle.font = skin.getFont("default");

        skin.add("buttonStyle",buttonStyle);

        float x = Gdx.graphics.getWidth()/2 - 200;
        float y = Gdx.graphics.getHeight()/2 - 200;

        playButton = new TextButton("PLAY",buttonStyle);
        playButton.setPosition(x, y);
        settingsButton = new TextButton("SETTINGS",buttonStyle);
        settingsButton.setPosition(x,y - 105f);
        exitButton = new TextButton("EXIT",buttonStyle);
        exitButton.setPosition(x,y - 210f);

    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {

    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0f, 0.2f, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        //TODO Draw loading screen the first time
        try {
            playButton.draw(game.batch, 2f);
            settingsButton.draw(game.batch, 2f);
            exitButton.draw(game.batch, 2f);
        }
        catch (Throwable e) {
        Gdx.app.error("Virtua Cards", "VirtuaCardsMainMenu - render", e);
        }
        game.batch.end();

        if (exitScreen)
            dispose();
    }

    /**
     * @param width
     * @param height
     * @see ApplicationListener#resize(int, int)
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * @see ApplicationListener#pause()
     */
    @Override
    public void pause() {

    }

    /**
     * @see ApplicationListener#resume()
     */
    @Override
    public void resume() {

    }

    /**
     * Called when this screen is no longer the current screen for a {@link Game}.
     */
    @Override
    public void hide() {

    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }


    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        boolean buttonClicked = false;
        Rectangle r = new Rectangle(playButton.getX(), playButton.getY(), playButton.getWidth(), playButton.getHeight());
        buttonClicked = r.contains(screenX,Gdx.graphics.getHeight() - screenY);

        if (buttonClicked) // play button clicked
        {
            game.setScreen(new VirtuaCardsGameScreen(game));
            exitScreen = true;
        }
        else { // exit button clicked
            r.set(exitButton.getX(), exitButton.getY(), exitButton.getWidth(), exitButton.getHeight());
            buttonClicked = r.contains(screenX,Gdx.graphics.getHeight() - screenY);
            if (buttonClicked){
                Gdx.app.exit();
            }
        }
        return true;
    }


    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }


    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }


    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
