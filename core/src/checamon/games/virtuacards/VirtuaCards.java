package checamon.games.virtuacards;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;


public class VirtuaCards extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Sprite sprite;
	Texture img, deck1;
	BitmapFont font;
	String debugText;

	TextureRegion deck1region;
	TextureRegion[][] regions;
	TextureRegion deck2region;
	Sprite sprite2;

	private float x, y;
	private int dragCounter;
	private ArrayList<Point> dragBuffer;
	private Sprite touchedSprite;

	Deck fullDeck;
	Texture deck;

	int cardCounter = 0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		deck1 = new Texture("deck1pic.png");
		TextureRegion[][] cc = new TextureRegion(deck1).split(deck1.getWidth()/2,deck1.getHeight()/2);
		deck1region = new TextureRegion(cc[0][0]);
		deck2region = new TextureRegion(cc[0][1]);

		try {
			deck = new Texture("full_french_deck.png");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		font = new BitmapFont();
		font.setColor(Color.BLUE);
		//sprite = new Sprite(deck1);
		sprite = new Sprite(deck1region);

		sprite2 = new Sprite(deck2region);
		sprite2.setPosition(50f, 50f);
		sprite2.scale(-0.75f);

		x = 0;
		y = 0;
		sprite.setPosition(x, y);
		sprite.scale(-0.75f);

		debugText = "LOADING ...";
		if (deck == null)
			debugText += " DECK IS NULL";
		dragCounter = 0;
		dragBuffer = new ArrayList();

		fullDeck = new Deck(deck);
		touchedSprite = null;

		fullDeck.getDrawOrder().put(0, 13);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.2f, 0.6f, 0.2f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.input.setInputProcessor(this);
		batch.begin();
		//sprite.draw(batch);
		//sprite2.draw(batch);

		fullDeck.drawCards(batch);

/*
		if (touchedSprite != null)
			touchedSprite.draw(batch);
*/


		//font.draw(batch, debugText, 20, 40);
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
		 if (fullDeck.isTouchingCard(screenX, Gdx.graphics.getHeight() - screenY)){
			fullDeck.getTouchedCard(screenX, Gdx.graphics.getHeight() - screenY).setPosition(new Point(screenX, Gdx.graphics.getHeight() - screenY));
			/*touchedSprite = fullDeck.getTouchedCard(screenX, Gdx.graphics.getHeight() - screenY).getCardSprite();
			if (touchedSprite != null)
				touchedSprite.setCenter(screenX, Gdx.graphics.getHeight() - screenY);*/
			cardCounter++;
/*
			debugText = "Card: X="  + String.valueOf(touchedSprite.getX()) + ", Y="  + String.valueOf(touchedSprite.getY()) +
					"; Touch X=" + String.valueOf(screenX) + ", Y=" + String.valueOf( Gdx.graphics.getHeight() - screenY) + " DragCounter=" + String.valueOf(dragCounter) +
					" DragBuffer Length: " + String.valueOf(dragBuffer.size()) + " Touch Card Counter = " + toString().valueOf(cardCounter);
*/
		}
		else if (dragCounter > 0) {
			dragCounter = 0;
			dragBuffer.clear();
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
		debugText = "Touch DOWN X=" + String.valueOf(screenX) + ", Y=" + String.valueOf(screenY) + " Image X=" +
				String.valueOf(sprite.getX()) + " Y=" + String.valueOf(sprite.getY()) + " DragCounter=" + String.valueOf(dragCounter) + " DragBuffer Length: " + String.valueOf(dragBuffer.size());;

		/*Rectangle r =  sprite.getBoundingRectangle();

		if (r.contains(screenX,Gdx.graphics.getHeight() - screenY)) // It is touching the deck
		{
			batch.begin();
			sprite.draw(batch);
			batch.end();
		}
*/

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

		if (Point.pointListInsideDoubleTouchedDrag(dragBuffer, 75, 50) && dragCounter > 0)
		{//double swipe, left/right recognised
			debugText = "DoubleSwipe UP X=" + String.valueOf(screenX) + ", Y=" + String.valueOf(screenY) + " Image X=" +
					String.valueOf(sprite.getX()) + " Y=" + String.valueOf(sprite.getY()) + " DragCounter=" + String.valueOf(dragCounter) + " DragBuffer Length: " + String.valueOf(dragBuffer.size());

			//fullDeck.getDrawOrder().put(0, 11);
		}
		else
		{
			debugText = "Touch UP X=" + String.valueOf(screenX) + ", Y=" + String.valueOf(screenY) + " Image X=" +
					String.valueOf(sprite.getX()) + " Y=" + String.valueOf(sprite.getY()) + " DragCounter=" + String.valueOf(dragCounter) + " DragBuffer Length: " + String.valueOf(dragBuffer.size());

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

}