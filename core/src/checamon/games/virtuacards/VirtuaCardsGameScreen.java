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
    private ArrayList<Integer> cardsTouched;
    private VirtuaCards game;
	private Deck fullDeck;



	public VirtuaCardsGameScreen (VirtuaCards g) {
        game = g;
		dragBuffer = new ArrayList<Point>();
        cardsTouched = new ArrayList<Integer>();
		dragCounter = 0;
		cardCounter = 0;

        Gdx.input.setCatchBackKey(true);
        //Gdx.app.setLogLevel(Application.LOG_ERROR);

		fullDeck = new Deck(new Texture("full_french_deck.png"));

		//init drawn cards
		for (int i = 0; i < fullDeck.getNumberOfCards(); i++)
			fullDeck.getDrawOrder().put(i,i);

		//fullDeck.getCards().get(52).setPosition(new Point(300f,100f));

		//fullDeck.shuffle(110f, 110f);

	}

    @Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.2f, 0.6f, 0.2f, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.input.setInputProcessor(this);

		game.batch.begin();

		fullDeck.drawCards(game.batch);

		game.batch.end();

	}


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Card c;
        //deck

        //Gdx.app.error("VirtuaCardsGameScreen", "TouchDragged");

        if (dragCounter == 0) {
            if (fullDeck.isTouchingCard(screenX, Gdx.graphics.getHeight() - screenY)) {
                c = fullDeck.getTouchedCard(screenX, Gdx.graphics.getHeight() - screenY);
                if (cardCounter == 2) { // Move deck of cards
                    if (cardsTouched.get(0).equals(cardsTouched.get(1)) && c.isDecked()) {
                        fullDeck.moveDeckTouched(screenX, Gdx.graphics.getHeight() - screenY);
                    }
                    else {
                        c.setCenter(new Point(screenX, Gdx.graphics.getHeight() - screenY));
                        c.setDecked(false);
                        c.setDeckId(0);
                    }
                }
                else {
                    c.setCenter(new Point(screenX, Gdx.graphics.getHeight() - screenY));
                    c.setDecked(false);
                    c.setDeckId(0);
                }

                dragBuffer.add(dragCounter, new Point(screenX, Gdx.graphics.getHeight() - screenY));
                dragCounter++;
            }
        }
        else if (fullDeck.isTouchingDraggedCard(screenX, Gdx.graphics.getHeight() - screenY)) {
            c = fullDeck.getTouchedDraggedCard(screenX, Gdx.graphics.getHeight() - screenY);
            if (cardCounter == 2) { // Move deck of cards
                if (cardsTouched.get(0).equals(cardsTouched.get(1)) && c.isDecked()) {
                    fullDeck.moveDeckTouchedDragged(screenX, Gdx.graphics.getHeight() - screenY);
                } else {
                    c.setCenter(new Point(screenX, Gdx.graphics.getHeight() - screenY));
                    c.setDecked(false);
                    c.setDeckId(0);
                }
            }
            else{
                c.setCenter(new Point(screenX, Gdx.graphics.getHeight() - screenY));
                c.setDecked(false);
                c.setDeckId(0);
            }

            dragBuffer.add(dragCounter, new Point(screenX, Gdx.graphics.getHeight() - screenY));
            dragCounter++;
        }
        else {
            dragCounter = 0;
            dragBuffer.clear();
            cardsTouched.clear();
            cardCounter = 0;
        }
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.BACK) {
            game.setScreen(new VirtuaCardsMainMenu(game));
            dispose();
        }
        return true;
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
        Card c;
        dragCounter = 0;
        dragBuffer.clear();
        //Gdx.app.error("VirtuaCardsGameScreen", "TouchDown");

        c = fullDeck.getTouchedCard(screenX, Gdx.graphics.getHeight() - screenY);
        if (c != null){
            cardsTouched.add(cardCounter,c.getId());
            cardCounter++;
        }else{
            cardsTouched.clear();
            cardCounter = 0;
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Card c;
        //Gdx.app.error("VirtuaCardsGameScreen", "TouchUp");
        if (dragCounter > 0) {
            c =	fullDeck.getTouchedDraggedCard(screenX, Gdx.graphics.getHeight() - screenY);
            if (c != null) {
                fullDeck.autoDeckCard(screenX, Gdx.graphics.getHeight() - screenY);

                if (Point.pointListInsideDoubleTouchedDrag(dragBuffer, 75, 150)) { //flip card
                    //fullDeck.getCards().get(52).setFaceUp(true);
                    c.toggleFaceUp();
                }

                if (cardCounter > 0){
                    cardCounter = 0;
                    cardsTouched.clear();
                }
            }
        }else{
            c =	fullDeck.getTouchedCard(screenX, Gdx.graphics.getHeight() - screenY);
            if (c != null){
                if (cardCounter == 3){ // three touches on the same card shuffles the sub deck
                    if (cardsTouched.get(0) == cardsTouched.get(1) && cardsTouched.get(1) == cardsTouched.get(2)){
                        fullDeck.shuffle(screenX, Gdx.graphics.getHeight() - screenY);
                    }
                    cardCounter = 0;
                    cardsTouched.clear();
                }
            }
        }

        if (cardCounter == 3){
            cardCounter = 0;
            cardsTouched.clear();
        }

        if (dragCounter > 0){
            dragCounter = 0;
            dragBuffer.clear();

        }

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }


    @Override
	public boolean scrolled(int amount) {
		return false;
	}

    @Override
    public void show() {

    }


    @Override
    public void resize(int width, int height) {

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

    }
}