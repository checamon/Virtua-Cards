package checamon.games.virtuacards;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/**
 * Created by angelcheca on 28/11/15.
 */
public class VirtuaCards extends Game {
    SpriteBatch batch;

    //TODO Declare loading screen

    public void create() {
        batch = new SpriteBatch();

        //TODO Initialize Loading Screen

        this.setScreen(new VirtuaCardsMainMenu(this));
    }

    public void render() {
        super.render(); // important!
    }

    public void dispose() {
        batch.dispose();
    }
}
