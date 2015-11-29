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
    //private ArrayList<Deck> subdecks;

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
        if (i < numberOfCards - 1)
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
        if (i < numberOfCards - 1)
            setCardDrawOrderOnTop(i);

        return cardResult;
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

    private void setRandomCardDraw(ArrayList<Integer> subDeck){
        ArrayList<Integer> originalOrder = (ArrayList<Integer>) subDeck.clone();

        Collections.shuffle(subDeck);
        Integer replace;
        for (int i = 0; i < originalOrder.size(); i++){
            replace = drawOrder.get(originalOrder.get(i));
            drawOrder.put(originalOrder.get(i),drawOrder.get(subDeck.get(i)));
            if (i < originalOrder.size() - 1)
                drawOrder.put(subDeck.get(i),replace);
        }

    }

    public void shuffle(float x, float y){
        //TODO Change this method, the top may not work when cards already unsorted

        ArrayList<Integer> subDeck = new ArrayList<Integer>();
        Card c;
        int top = numberOfCards-1;
        int i = numberOfCards-1;
        int index = 0;
        boolean exit = false;

        while (i >= 0  && !exit){
            c = this.getTouchedCard(x,y,top);
            if (c == null)
                exit = true;
            else{
                subDeck.add(index,c.getId());
                top = c.getId() - 1;
                index++;
                i--;
            }
        }

        if (!subDeck.isEmpty()){
            setRandomCardDraw(subDeck);
        }

    }

    public void moveDeckTouched(float x, float y){
        Card c = getTouchedCard(x,y);
        Card card;
        float deltaX;
        float deltaY;

        Rectangle r =  c.getCardRectangle();
        c.setCenter(new Point(x,y));
        Rectangle r2 = c.getCardRectangle();

        deltaX = r2.getX() - r.getX();
        deltaY = r2.getY() - r.getY();

        //Move all cards in the deck
        int i = c.getId() - 1;

        while (i >= 0){
            card = cards.get(drawOrder.get(i));
            if (drawOrder.get(i) > -1 && card.isTouched(x, y)) {
                card.setPosition(new Point(card.getCardSprite().getX() + deltaX, card.getCardSprite().getY() + deltaY));
            }else{
                i--;
            }
        }

    }

    public void moveDeckTouchedDragged(float x, float y){
        Card c = getTouchedCard(x,y);
        Card card;
        float deltaX;
        float deltaY;

        Rectangle r =  c.getCardRectangle();
        c.setCenter(new Point(x,y));
        Rectangle r2 = c.getCardRectangle();

        deltaX = r2.getX() - r.getX();
        deltaY = r2.getY() - r.getY();

        //Move all cards in the deck
        int i = c.getId() - 1;

        while (i >= 0){
            card = cards.get(drawOrder.get(i));
            if (drawOrder.get(i) > -1 && card.isTouchedDragged(x, y)) {
                card.setPosition(new Point(card.getCardSprite().getX() + deltaX, card.getCardSprite().getY() + deltaY));
            }else{
                i--;
            }
        }
    }

    public void autoDeckCard(float x, float y){
        Card c = getTouchedCard(x,y);
        Card below = getTouchedCard(x,y, numberOfCards - 2);

        // Detect if overlapping enough to deck automatically
        // by default cards are 150 x 190
        if (below != null) {
            if (((c.getCardSprite().getX() <= below.getCardSprite().getX() + 35) && (c.getCardSprite().getX() >= below.getCardSprite().getX() - 35)) &&
                    ((c.getCardSprite().getY() <= below.getCardSprite().getY() + 35) && (c.getCardSprite().getY() >= below.getCardSprite().getY() - 35))) {
                Rectangle r = below.getCardRectangle();
                c.setPosition(new Point(r.getX(), r.getY()));
                c.setDecked(true);
            }else{
                c.setDecked(false);
            }
        }
    }

    public HashMap<Integer,Integer> getDrawOrder() {
        return drawOrder;
    }

    public int getNumberOfCards() {
        return numberOfCards;
    }
}
