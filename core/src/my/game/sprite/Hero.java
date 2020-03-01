package my.game.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import my.game.base.Sprite;
import my.game.math.Rect;

public class Hero extends Sprite {
    private static final float SHIP_HEIGHT = 0.17f;
    private Vector2 velocity;
    private Rect worldBounds;
    private Vector2 buf;

    public Hero(TextureAtlas atlas) {
        super(atlas.findRegion("main_ship"));
        velocity = new Vector2();
        buf = new Vector2();
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(
                regions[frame],
                getLeft(), getBottom(),
                halfWidth, halfHeight,
                getWidth(), getHeight(),
                scale, scale,
                angle
        );
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(SHIP_HEIGHT);
        pos.set(0f,worldBounds.getBottom() + 0.1f);
        this.worldBounds = worldBounds;
    }



    @Override
    public void update(float delta) {
        if(buf.cpy().sub(pos).len() > velocity.len())  pos.add(velocity);
    }

    @Override
    public void touchDown(Vector2 touch, int pointer, int button) {
        if(touch.x < worldBounds.getLeft() + getHalfWidth()) touch.x = worldBounds.getLeft() + getHalfWidth();
        if(touch.x > worldBounds.getRight() - getHalfWidth()) touch.x = worldBounds.getRight() - getHalfWidth();
        if(touch.y < worldBounds.getBottom() + getHalfHeight()) touch.y = worldBounds.getBottom() + getHalfHeight();
        if(touch.y > worldBounds.getTop() - getHalfHeight()) touch.y = worldBounds.getTop() - getHalfHeight();
        buf.set(touch.cpy());
        velocity.set(touch.cpy().sub(pos));
        velocity.nor();
        velocity.scl(0.01f);
    }

    public void keyDown(int keyCode){
        switch (keyCode){
            case 19: velocity.set(0f,1f);
                buf.set(pos.x,pos.y + getHalfHeight());
                break;
            case 20: velocity.set(0f,-1f);
            buf.set(pos.x,pos.y - getHalfHeight());
                break;
            case 21: velocity.set(-1f,0f);
            buf.set(pos.x - getHalfWidth(),pos.y);
                break;
            case 22: velocity.set(1f,0f);
            buf.set(pos.x + getHalfWidth(),pos.y);
                break;
        }
        if(buf.x < worldBounds.getLeft() + getHalfWidth()) buf.x = worldBounds.getLeft() + getHalfWidth();
        if(buf.x > worldBounds.getRight() - getHalfWidth()) buf.x = worldBounds.getRight() - getHalfWidth();
        if(buf.y < worldBounds.getBottom() + getHalfHeight()) buf.y = worldBounds.getBottom() + getHalfHeight();
        if(buf.y > worldBounds.getTop() - getHalfHeight()) buf.y = worldBounds.getTop() - getHalfHeight();
        velocity.scl(0.01f);
    }
}
