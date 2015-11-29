package checamon.games.virtuacards;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class VirtuaCardsGameScreen implements Screen, InputProcessor {

	private int dragCounter;
	private int cardCounter;
	private ArrayList<Point> dragBuffer;
    private VirtuaCards game;

	private Deck fullDeck;



	public VirtuaCardsGameScreen (VirtuaCards g) {
        game = g;
		dragBuffer = new ArrayList<Point>();
		dragCounter = 0;
		cardCounter = 0;

        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);

		fullDeck = new Deck(new Texture("full_french_deck.png"));

		//init drawn cards
		for (int i = 0; i < fullDeck.getNumberOfCards(); i++)
			fullDeck.getDrawOrder().put(i,i);

		//fullDeck.getCards().get(52).setPosition(new Point(300f,100f));

		fullDeck.shuffle(110f, 110f);

	}

    @Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.6f, 0.2f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();

		fullDeck.drawCards(game.batch);

		game.batch.end();

	}

	/**
	 * Called when a finger or the mouse was dragged.
	 *
	 * @param screenX
	 * @param screenY
	 * @param pointer the pointer for the event.  @return whether the input was processed
	 */
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		//deck
		if (dragCounter == 0) {
			if (fullDeck.isTouchingCard(screenX, Gdx.graphics.getHeight() - screenY)) {
				fullDeck.getTouchedCard(screenX, Gdx.graphics.getHeight() - screenY).setCenter(new Point(screenX, Gdx.graphics.getHeight() - screenY));

				dragBuffer.add(dragCounter, new Point(screenX, Gdx.graphics.getHeight() - screenY));
				dragCounter++;
			}
		}
		else if (fullDeck.isTouchingDraggedCard(screenX, Gdx.graphics.getHeight() - screenY)) {
			fullDeck.getTouchedDraggedCard(screenX, Gdx.graphics.getHeight() - screenY).setCenter(new Point(screenX, Gdx.graphics.getHeight() - screenY));

			dragBuffer.add(dragCounter, new Point(screenX, Gdx.graphics.getHeight() - screenY));
			dragCounter++;

		}
		else {
			dragCounter = 0;
			dragBuffer.clear();
		}
		cardCounter = 0;
		return true;
	}


	/**
	 * Called when a key was pressed
	 *
	 */
	@Override
	public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK) {
            game.setScreen(new VirtuaCardsMainMenu(game));
            dispose();
        }
		return true;
	}

	/**
	 * Called when a key was released
	 *
	 */
	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	/**
	 * Called when a key was typed
	 *
	 * @param character The character
	 * @return whether the input was processed
	 */
	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	/**
	 *
	 * @param screenX The x coordinate, origin is in the upper left corner
	 * @param screenY The y coordinate, origin is in the upper left corner
	 * @param pointer the pointer for the event.
	 * @param button  the button
	 * @return whether the input was processed
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		dragCounter = 0;
		dragBuffer.clear();
		cardCounter = 1;


		return true;
	}

	/**
	 *
	 * @param screenX
	 * @param screenY
	 * @param pointer the pointer for the event.
	 * @param button  the button   @return whether the input was processed
	 */
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Card c;

        if (dragCounter > 0) {
            c =	fullDeck.getTouchedDraggedCard(screenX, Gdx.graphics.getHeight() - screenY);
            if (c != null) {
                fullDeck.autoDeckCard(screenX, Gdx.graphics.getHeight() - screenY);
                if (Point.pointListInsideDoubleTouchedDrag(dragBuffer, 75, 150)) { //flip card
                    //fullDeck.getCards().get(52).setFaceUp(true);
                    c.toggleFaceUp();
                }
            }
        }

		dragCounter = 0;
		dragBuffer.clear();
		cardCounter = 0;

		return true;
	}

	/**
	 * Called when the mouse was moved without any buttons being pressed. Will not be called on iOS.
	 *
	 * @param screenX
	 * @param screenY
	 * @return whether the input was processed
	 */
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	/**
	 * Called when the mouse wheel was scrolled. Will not be called on iOS.
	 *
	 * @param amount the scroll amount, -1 or 1 depending on the direction the wheel was scrolled.
	 * @return whether the input was processed.
	 */
	@Override
	public boolean scrolled(int amount) {
		return false;
	}

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    @Override
    public void show() {

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
}