package checamon.games.virtuacards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.HashMap;



/**
 * Created by angelcheca on 17/11/15.
 */
public class Deck {
    private ArrayList<Card> cards;
    private HashMap<Integer,Integer> drawOrder;
    private int numberOfCards;
    private Sprite result;

    public Deck(Texture texture) {
        try {
            TextureRegion[][] cc = new TextureRegion(texture).split(texture.getWidth()/13,texture.getHeight()/5);


            this.cards = new ArrayList();
            this.drawOrder = new HashMap<Integer,Integer>();
            int index = 0;
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 13; j++){
                    this.cards.add(index, new Card(cc[i][j]));
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

    public void drawCards(SpriteBatch batch)
    {
        boolean exit = false;
        Sprite aux;
        int i = 0;
        while (i < numberOfCards && !exit){
            if (drawOrder.get(i) > -1){
                cards.get(drawOrder.get(i)).drawCardSprite(batch);
                i++;
            }
            else
                exit = true;
        }

    }

    public boolean isTouchingCard(float x, float y){

        boolean exit = false;
        boolean result = false;
        int i = 0;

        while (i < numberOfCards && !exit){
            if (drawOrder.get(i) > -1){
                if (cards.get(drawOrder.get(i)).isTouched(x,y)) {
                    result = true;
                    exit = true;
                }
                i++;
            }
            else
                exit = true;
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
                i++;
            }
            else
                exit = true;
        }
        return result;
    }

    public Card getTouchedDraggedCard(float x, float y){

        boolean exit = false;
        this.result = null;
        Card cardResult = null;
        int i = 0;

        while (i < numberOfCards && !exit){
            if (drawOrder.get(i) > -1){
                if (cards.get(drawOrder.get(i)).isTouchedDragged(x, y)) {

                    this.result = cards.get(drawOrder.get(i)).getCardSprite();
                    cardResult = cards.get(drawOrder.get(i));
                    exit = true;
                }
                i++;
            }
            else
                exit = true;
        }
        return cardResult;
    }

    public Card getTouchedCard(float x, float y){

        boolean exit = false;
        this.result = null;
        Card cardResult = null;
        int i = 0;

        while (i < numberOfCards && !exit){
            if (drawOrder.get(i) > -1){
                if (cards.get(drawOrder.get(i)).isTouched(x, y)) {

                    this.result = cards.get(drawOrder.get(i)).getCardSprite();
                    cardResult = cards.get(drawOrder.get(i));
                    exit = true;
                }
                i++;
            }
            else
                exit = true;
        }
        return cardResult;
    }

    public HashMap<Integer,Integer> getDrawOrder() {
        return drawOrder;
    }
}
