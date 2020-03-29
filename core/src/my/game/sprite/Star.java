package my.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import my.game.base.Sprite;
import my.game.math.Rect;
import my.game.math.Rnd;

public class Star extends Sprite {
    private final float ANIMATE_PULSE_INTERVAL = Rnd.nextFloat(0.4f,0.8f);
    private float animatePulseTimer;
    private float animateSpeed;

    private static final float STAR_HEIGHT = 0.007f;
    private float bufHeight;
    protected final Vector2 velocity;
    private Rect worldBounds;

    public Star(TextureAtlas atlas){
        super(atlas.findRegion("star"));
        velocity = new Vector2();
        velocity.set(Rnd.nextFloat(-0.005f,0.005f),Rnd.nextFloat(-0.5f,-0.2f));
        animatePulseTimer = 0f;
        bufHeight = STAR_HEIGHT;
        animateSpeed = Rnd.nextFloat(0.0001f,0.0004f);
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
        checkAndHandleBounds();
        bufHeight += animateSpeed;
        setHeightProportion(bufHeight);
        animatePulseTimer += delta;
        if(animatePulseTimer > ANIMATE_PULSE_INTERVAL){
            setHeightProportion(STAR_HEIGHT);
            bufHeight = STAR_HEIGHT;
            animatePulseTimer = 0f;
        }

    }

    public void checkAndHandleBounds() {
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
