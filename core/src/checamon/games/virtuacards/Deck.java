package checamon.games.virtuacards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


/**
 * Created by angelcheca on 17/11/15.
 */
public class Deck {
    private ArrayList<Card> cards;
    private HashMap<Integer,Integer> drawOrder;
    private int numberOfCards;
    private int deckCounter;
    private int stackCounter;

    public Deck(Texture texture) {
        try {
            TextureRegion[][] cc = new TextureRegion(texture).split(texture.getWidth()/13,texture.getHeight()/5);


            this.cards = new ArrayList();
            this.drawOrder = new HashMap<Integer,Integer>();
            int index = 0;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 13; j++){
                    this.cards.add(index, new Card(cc[i][j], index, cc[4][2]));
                    this.drawOrder.put(index, -1);
                    index++;
                }
            }

            this.numberOfCards = 54;
            this.deckCounter = 1;
            this.stackCounter = 0;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void drawCard(SpriteBatch batch, int cardIndex, float x, float y, float sizeX, float sizeY){
        try {
            batch.draw(cards.get(cardIndex).getCard(),x,y,sizeX, sizeY);
            cards.get(cardIndex).setPosition(new Point(x,y));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void drawCards(SpriteBatch batch) {

        int i = 0;
        while (i < numberOfCards){
            if (drawOrder.get(i) > -1)
                cards.get(drawOrder.get(i)).drawCardSprite(batch);
            i++;
        }
    }

    public boolean isTouchingCard(float x, float y){

        boolean exit = false;
        boolean result = false;
        int i = 0;

        while (i < numberOfCards && !exit){
            if (drawOrder.get(i) > -1){
                if (cards.get(drawOrder.get(i)).isTouched(x, y)) {
                    result = true;
                    exit = true;
                }
            }
            i++;
        }
        return result;
    }

    public boolean isTouchingDraggedCard(float x, float y){

        boolean exit = false;
        boolean result = false;
        int i = 0;

        while (i < numberOfCards && !exit){
            if (drawOrder.get(i) > -1){
                if (cards.get(drawOrder.get(i)).isTouchedDragged(x, y)) {
                    result = true;
                    exit = true;
                }
            }
            i++;
        }
        return result;
    }

    public boolean isCardTouchedTopOfDeck(float x, float y){
        boolean exit = false;
        boolean result = false;
        int touchCounter = 0;
        int i = 0;

        while (i < numberOfCards && !exit){
            if (drawOrder.get(i) > -1){
                if (cards.get(drawOrder.get(i)).isTouched(x, y)) {
                    if (touchCounter == 0)
                        touchCounter++;
                    else if (touchCounter == 1) {
                        exit = true;
                        result = true;
                    }
                }
            }
            i++;
        }
        return result;
    }

    public boolean isCardTouchedDraggedTopOfDeck(float x, float y){
        boolean exit = false;
        boolean result = false;
        int touchCounter = 0;
        int i = 0;

        while (i < numberOfCards && !exit){
            if (drawOrder.get(i) > -1){
                if (cards.get(drawOrder.get(i)).isTouchedDragged(x, y)) {
                    if (touchCounter == 0)
                        touchCounter++;
                    else if (touchCounter == 1) {
                        exit = true;
                        result = true;
                    }
                }
            }
            i++;
        }
        return result;
    }

    public Card getTouchedDraggedCard(float x, float y){

        Card cardResult = null;
        int i = numberOfCards - 1;
        boolean exit = false;

        while (i >= 0 && !exit){
                if (cards.get(drawOrder.get(i)).isTouchedDragged(x, y)) {
                    cardResult = cards.get(drawOrder.get(i));
                    exit = true;
                }else {
                    i--;
                }
            }

        //TODO Change this to not move to the top until is sure no other cards need to move
        if (i >= 0 && i < numberOfCards - 1)
            setCardDrawOrderOnTop(i);
        return cardResult;
    }

    public Card getTouchedCard(float x, float y){

        Card cardResult = null;
        int i = numberOfCards - 1;
        boolean exit = false;

        while (i >= 0 && !exit){
                if (drawOrder.get(i) > -1 && cards.get(drawOrder.get(i)).isTouched(x, y)) {
                    cardResult = cards.get(drawOrder.get(i));
                    exit = true;
                }else{
                    i--;
                }
        }
        //TODO Change this to not move to the top until is sure no other cards need to move
        if (i >= 0 && i < numberOfCards - 1)
            setCardDrawOrderOnTop(i);

        return cardResult;
    }

    public Integer getTouchedCardIndex(float x, float y){

        int i = numberOfCards - 1;
        boolean exit = false;

        while (i >= 0 && !exit){
            if (drawOrder.get(i) > -1 && cards.get(drawOrder.get(i)).isTouched(x, y)) {
                exit = true;
            }else{
                i--;
            }
        }
        return (Integer)i;
    }

    public Integer getCardIndex(Card c){
        int i = numberOfCards - 1;
        boolean exit = false;

        while (i >= 0 && !exit){
            if (drawOrder.get(i) == c.getId()) {
                exit = true;
            }else{
                i--;
            }
        }
        return (Integer)i;

    }

    public Card getTouchedCard(float x, float y, int top){

        Card cardResult = null;
        int i = top;
        boolean exit = false;

        while (i >= 0 && !exit){
            if (drawOrder.get(i) > -1 && cards.get(drawOrder.get(i)).isTouched(x, y)) {
                cardResult = cards.get(drawOrder.get(i));
                exit = true;
            }else{
                i--;
            }
        }
        return cardResult;
    }

    public void shuffle(float x, float y){

        ArrayList<Integer> subDeck = new ArrayList<Integer>();
        Card c;
        int i = numberOfCards-1;
        int index = 0;
        boolean exit = false;

        while (i >= 0  && !exit){
            c = this.getTouchedCard(x, y, i);
            if (c == null)
                exit = true;
            else{
                subDeck.add(index,c.getId());
                index++;
                i--;
            }
        }

        if (!subDeck.isEmpty()){
            setRandomCardDraw(subDeck);
        }

    }

    public void moveDeckTouched(float x, float y){
        Card card;
        int deckId = -1;
        int cardDeck = -1;
        int lastIndex = -1;

        //Move all cards in the deck
        int i =  getTouchedCardIndex(x,y);
        lastIndex = i;

        while (i >= 0){
            card = cards.get(drawOrder.get(i));
            cardDeck = card.getDeckId();
            if (cardDeck == deckId) {
                card.setCenter(new Point(x, y));
                setCardDrawOrderOnTopSubDeck(i, lastIndex);
                lastIndex--;
            }
            else if (deckId == -1){
                if (drawOrder.get(i) > -1 && card.isTouched(x, y)) {
                    deckId = card.getDeckId();
                    card.setCenter(new Point(x, y));
                }
            }
            i--;
        }

    }

    public void moveDeckTouchedDragged(float x, float y){
        Card card;
        int deckId = -1;
        int cardDeck = -1;
        int lastIndex = -1;

        //Move all cards in the deck
        int i =  getTouchedCardIndex(x,y);
        lastIndex = i;

        while (i >= 0){
            card = cards.get(drawOrder.get(i));
            cardDeck = card.getDeckId();
            if (cardDeck == deckId) {
                card.setCenter(new Point(x, y));
                setCardDrawOrderOnTopSubDeck(i, lastIndex);
                lastIndex--;
            }
            else if (deckId == -1){
                if (drawOrder.get(i) > -1 && card.isTouchedDragged(x, y)) {
                    deckId = card.getDeckId();
                    card.setCenter(new Point(x, y));
                }
            }
            i--;
        }

    }

    public void setDeckPosition(int belowDeckId, int aboveDeckId, float x, float y){
        Card card;
        int cardDeck = -1;

        //Move all cards in the deck
        int i =  numberOfCards - 1;

        while (i >= 0){
            card = cards.get(drawOrder.get(i));
            cardDeck = card.getDeckId();
            if (cardDeck == aboveDeckId) {
                card.setPosition(new Point(x, y));
                card.setDeckId(belowDeckId);
            }
            i--;
        }

    }

    public Card getTouchedCardDifferentDeck(float x, float y, int deckiId){
        Card cardResult = null;
        int i = numberOfCards - 1;
        boolean exit = false;

        while (i >= 0 && !exit){
            cardResult = cards.get(drawOrder.get(i));
            if (drawOrder.get(i) > -1 && cardResult.isTouched(x, y) && cardResult.getDeckId() != deckiId) {
                exit = true;
            }else{
                i--;
            }
        }
        if (!exit)
            cardResult = null;

        return cardResult;
    }

    public void autoDeckCard(float x, float y) {
        Card c = getTouchedCard(x, y);
        Card below;
        if (!c.isDecked()) {
            below = getTouchedCard(x, y, numberOfCards - 2);
        } else {
            below = getTouchedCardDifferentDeck(x, y, c.getDeckId());
        }

        // Detect if overlapping enough to deck automatically
        // by default cards are 150 x 190
        if (below != null && !c.isDecked()) {
            if (((c.getCardSprite().getX() <= below.getCardSprite().getX() + 35) && (c.getCardSprite().getX() >= below.getCardSprite().getX() - 35)) &&
                    ((c.getCardSprite().getY() <= below.getCardSprite().getY() + 35) && (c.getCardSprite().getY() >= below.getCardSprite().getY() - 35))) {
                Rectangle r = below.getCardRectangle();
                c.setPosition(new Point(r.getX(), r.getY()));
                c.setDecked(true);
                if (below.getDeckId() == 0) {
                    deckCounter++;
                    below.setDecked(true);
                    below.setDeckId(deckCounter);
                    c.setDeckId(deckCounter);
                } else {
                    c.setDeckId(below.getDeckId());
                }
            }
        } else if (below != null && c.isDecked()) {
            if (((c.getCardSprite().getX() <= below.getCardSprite().getX() + 35) && (c.getCardSprite().getX() >= below.getCardSprite().getX() - 35)) &&
                    ((c.getCardSprite().getY() <= below.getCardSprite().getY() + 35) && (c.getCardSprite().getY() >= below.getCardSprite().getY() - 35))) {
                Rectangle r = below.getCardRectangle();
                if (below.getDeckId() == 0) {
                    below.setDecked(true);
                    below.setDeckId(c.getDeckId());
                    setDeckPosition(c.getDeckId(), c.getDeckId(), r.getX(), r.getY());
                } else {
                    setDeckPosition(below.getDeckId(), c.getDeckId(), r.getX(), r.getY());
                }
            }
            //TODO resetDeckIds(); complex to compute... not worth it
        }
    }

    public void autoStackCard(float x, float y){

    }

    public void toggleFaceUpDeck(Card c, boolean originalDecked, float x, float y){
        int deckId = c.getDeckId();
        int i = numberOfCards - 1;
        int cardDeck;
        boolean exit = false;
        Card card;

        if (!originalDecked){
            c.toggleFaceUp();
        }
        else{
            while (i >= 0 && !exit){
                card = cards.get(drawOrder.get(i));
                cardDeck = card.getDeckId();
                if (cardDeck == deckId) {
                    card.toggleFaceUp();
                }
                i--;
            }
        }
    }

    public HashMap<Integer,Integer> getDrawOrder() {
        return drawOrder;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }

    public int getDeckCounter() {
        return deckCounter;
    }

    public void setDeckCounter(int deckCounter) {
        this.deckCounter = deckCounter;
    }

    private void setCardDrawOrderOnTop(int index){

        int i = index;
        int next = 0;
        int replace = drawOrder.get(index);;

        while (i < numberOfCards){
            if (i < numberOfCards-1) {
                next = drawOrder.get(i + 1);
                drawOrder.put(i,next);
                i++;
            }else{
                drawOrder.put(i,replace);
                i++;

            }
        }
    }

    private void setCardDrawOrderOnTopSubDeck(int index, int last){

        int i = index;
        int next = 0;
        int replace = drawOrder.get(index);

        while (i < last){
            if (i < last - 1) {
                next = drawOrder.get(i + 1);
                drawOrder.put(i,next);
                i++;
            }else{
                drawOrder.put(i,replace);
                i++;

            }
        }
    }

    private void setRandomCardDraw(ArrayList<Integer> subDeck){
        ArrayList<Integer> originalOrder = (ArrayList<Integer>) subDeck.clone();

        Collections.shuffle(subDeck);
        Integer replace;
        for (int i = 0; i < originalOrder.size(); i++){
            replace = drawOrder.get(originalOrder.get(i));
            drawOrder.put(originalOrder.get(i),drawOrder.get(subDeck.get(i)));
            drawOrder.put(subDeck.get(i),replace);
        }

    }

}
