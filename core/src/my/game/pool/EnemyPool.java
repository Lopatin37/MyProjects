package my.game.pool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import my.game.base.SpritesPool;
import my.game.sprite.EnemyShip;

public class EnemyPool extends SpritesPool<EnemyShip> {
    private TextureAtlas atlas;

    public EnemyPool(TextureAtlas atlas){
        this.atlas = atlas;
    }
    @Override
    protected EnemyShip newObject() {
        return new EnemyShip(atlas);
    }


}
