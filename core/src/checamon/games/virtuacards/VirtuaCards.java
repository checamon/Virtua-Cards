package checamon.games.virtuacards;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;


public class VirtuaCards extends ApplicationAdapter implements InputProcessor {
	private SpriteBatch batch;

	private int dragCounter;
	private int cardCounter;
	private ArrayList<Point> dragBuffer;

	private Deck fullDeck;

	@Override
	public void create () {
		batch = new SpriteBatch();
		dragBuffer = new ArrayList<Point>();
		dragCounter = 0;
		cardCounter = 0;

		fullDeck = new Deck(new Texture("full_french_deck.png"));

		//init drawn cards
		fullDeck.getDrawOrder().put(0,11);
		fullDeck.getDrawOrder().put(1,2);
		fullDeck.getCards().get(2).setPosition(new Point (200f,300f));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.2f, 0.6f, 0.2f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.input.setInputProcessor(this);
		batch.begin();

		fullDeck.drawCards(batch);

		batch.end();

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
			cardCounter++;
		}
		else {
			dragCounter = 0;
			dragBuffer.clear();
			cardCounter = 0;
		}

		return true;
	}


	/**
	 * Called when a key was pressed
	 *
	 */
	@Override
	public boolean keyDown(int keycode) {
		return false;
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
		cardCounter = 0;

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

		//if (Point.pointListInsideDoubleTouchedDrag(dragBuffer, 75, 50) && dragCounter > 0)

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

}