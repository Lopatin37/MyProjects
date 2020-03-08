package my.game.sprite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import my.game.base.Sprite;
import my.game.math.Rect;
import my.game.math.Rnd;

public class EnemyShip extends Sprite {
    private static final float SHIP_HEIGHT = 0.17f;
    private Rect worldBounds;
    private Vector2 buf;
    private float directionInTheEnd;

    public EnemyShip(TextureAtlas atlas){
        super(atlas.findRegion("enemy0"), 1, 2, 2);
        buf = new Vector2();
        directionInTheEnd = Rnd.nextFloat(-1f,1f);
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
        pos.set(Rnd.nextFloat(worldBounds.getLeft(),worldBounds.getRight()),worldBounds.getTop());
        this.worldBounds = worldBounds;
    }

    public void set(Rect worldBounds){
        setHeightProportion(SHIP_HEIGHT);
        pos.set(Rnd.nextFloat(worldBounds.getLeft(),worldBounds.getRight()),worldBounds.getTop());
        this.worldBounds = worldBounds;
    }

    public void update(Hero hero) {
        if(this.pos.y >= -0.1f) {
            buf.set(hero.pos);
            buf.sub(this.pos).scl(0.005f);
            this.pos.add(buf);
        }else{
            if(directionInTheEnd <= 0) {this.pos.add(-0.002f,-0.002f);}
            else {this.pos.add(0.002f,-0.002f);}
        }
        if(isOutside(worldBounds)){
            destroy();
        }
    }
}
