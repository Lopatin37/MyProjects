package my.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import my.game.base.Sprite;
import my.game.math.Rect;
import my.game.math.Rnd;

public class Star extends Sprite {
    private static final float STAR_HEIGHT = 0.007f;
    private Vector2 velocity;
    private float animateTimer;
    private float animateInterval = 1f;
    private Rect worldBounds;

    public Star(TextureAtlas atlas){
        super(atlas.findRegion("star"));
        velocity = new Vector2();
        velocity.set(Rnd.nextFloat(-0.005f,0.005f),Rnd.nextFloat(-0.5f,-0.2f));
        animateTimer = Rnd.nextFloat(0,1f);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(STAR_HEIGHT);
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getTop(), worldBounds.getBottom());
        pos.set(posX,posY);
        this.worldBounds = worldBounds;
    }

    @Override
    public void update(float delta) {
        pos.mulAdd(velocity,delta);
        if(getTop() < worldBounds.getBottom()) {
            setBottom(worldBounds.getTop());
        }
        if(getRight() < worldBounds.getLeft()){
            setLeft(worldBounds.getRight());
        }
        if(getLeft() > worldBounds.getRight()){
            setRight(worldBounds.getLeft());
        }
    }
}
