package checamon.games.virtuacards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by angelcheca on 17/11/15.
 */
public class Card {
    private TextureRegion card;
    private Point position;
    private Sprite cardSprite;
    private Sprite backSprite;
    private float sizeX;
    private float sizeY;
    private int id;
    private boolean faceUp;
    private boolean decked;
    private int deckId;
    //private boolean onTable;
    //private float orientation;

    public Card(Texture deckImage, Point p, float sizeX, float sizeY, float u, float v, float u2, float v2) {

        TextureRegion c = new TextureRegion(deckImage,u,v,u2,v2);
        this.cardSprite = new Sprite(c);
        this.card = c;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.position = p;
        this.cardSprite.setPosition(p.getX(),p.getY());
        this.cardSprite.setSize(sizeX,sizeY);
    }

    public Card(Texture deckImage, float u, float v, float u2, float v2) {

        TextureRegion c = new TextureRegion(deckImage,u,v,u2,v2);
        this.cardSprite = new Sprite(c);
        this.card = c;
        this.sizeX = 100f;
        this.sizeY = 130f;
        this.position = new Point(100f, 100f);
    }

    public Card(TextureRegion region, int id, TextureRegion backRegion) {

        this.card = region;
        this.sizeX = 150f;
        this.sizeY = 190f;
        this.position = new Point(100f, 100f);
        this.cardSprite = new Sprite(region);
        this.cardSprite.setSize(sizeX, sizeY);
        this.cardSprite.setPosition(position.getX(), position.getY());
        this.backSprite = new Sprite(backRegion);
        this.backSprite.setSize(sizeX, sizeY);
        this.backSprite.setPosition(position.getX(), position.getY());
        this.id = id;
        this.faceUp = false;
        this.decked = true;
        this.deckId = 1;
    }


    public void drawCardSprite(SpriteBatch batch){
        //this.cardSprite.setCenter(this.cardSprite.getX() + 1, this.cardSprite.getY() + 1);
        if (faceUp)
            this.cardSprite.draw(batch);
        else
            this.backSprite.draw(batch);
    }

    public boolean isTouched(float x, float y){
        return this.getCardRectangle().contains(x, y);
    }

    public boolean isTouchedDragged(float x, float y){
        Rectangle r = this.getCardRectangle();
        float h = r.getHeight();
        float w = r.getWidth();
        r.setHeight(r.getHeight() * 20);
        r.setWidth(r.getWidth() * 20);

        r.setPosition(r.getX() - (r.getWidth() - w)/2, r.getY() - (r.getHeight() - h)/2);
        return r.contains(x, y);
    }

    public Rectangle getCardRectangle(){
        Rectangle r;
        if (faceUp)
            r = this.cardSprite.getBoundingRectangle();
        else
            r = this.backSprite.getBoundingRectangle();
        return r;
    }

    public void setSize(float sizeX, float sizeY){
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public TextureRegion getCard() {

        return card;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.cardSprite.setPosition(position.getX(), position.getY());
        this.backSprite.setPosition(position.getX(), position.getY());
        this.position.setCoordinates(position.getX(),position.getY());
    }

    public void setCenter(Point position) {
        this.cardSprite.setCenter(position.getX(), position.getY());
        this.backSprite.setCenter(position.getX(), position.getY());
        this.position.setCoordinates(position.getX(),position.getY());
    }

    public void toggleFaceUp(){
        faceUp = !faceUp;
    }

    public Sprite getCardSprite() {
        return this.cardSprite;
    }

    public int getId() {
        return id;
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }

    public Sprite getBackSprite() {
        return backSprite;
    }

    public void setBackSprite(Sprite backSprite) {
        this.backSprite = backSprite;
    }

    public boolean isDecked() {
        return decked;
    }

    public void setDecked(boolean decked) {
        this.decked = decked;
    }

    public int getDeckId() {
        return deckId;
    }

    public void setDeckId(int deckId) {
        this.deckId = deckId;
    }
}
