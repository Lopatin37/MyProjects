package my.game.sprite;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import my.game.base.Sprite;
import my.game.math.Rect;
import my.game.pool.BulletPool;
import my.game.pool.EnemyPool;

public class Enemy extends Sprite {
    private EnemyPool enemyPool;
    private TextureAtlas atlas;
    private Rect worldBounds;
    private float interval = 2f;
    private float timer;



    public Enemy(TextureAtlas atlas, EnemyPool enemyPool){
        this.atlas = atlas;
        this.enemyPool = enemyPool;
    }

    public void update(float delta) {
        if(timer < interval){
            timer += delta;
        }else{
            timer = 0;
            newEnemy();
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
    }

    private void newEnemy(){
        EnemyShip enemyShip = enemyPool.obtain();
        enemyShip.set(worldBounds);
    }
}
