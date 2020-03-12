package my.game.pool;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import my.game.base.SpritesPool;
import my.game.sprite.EnemyShip;
import my.game.sprite.Hero;

public class EnemyPool extends SpritesPool<EnemyShip> {
    private TextureAtlas atlas;
    private BulletPool bulletPool;
    private ExplosionPool explosionPool;

    public EnemyPool(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool){
        this.atlas = atlas;
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
    }
    @Override
    protected EnemyShip newObject() {
        String size = "enemy0";
        if(Math.random() > 0.45f){
            size = "enemy1";
        }
        if (Math.random() > 0.75f){
            size = "enemy2";
        }
        return new EnemyShip(atlas, size, bulletPool, explosionPool);
    }

}
