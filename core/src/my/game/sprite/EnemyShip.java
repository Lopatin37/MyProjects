package my.game.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import my.game.base.Sprite;
import my.game.math.Rect;
import my.game.math.Rnd;
import my.game.pool.BulletPool;
import my.game.pool.ExplosionPool;
import my.game.screen.GameScreen;


public class EnemyShip extends Sprite {

    public final float DAMAGE_ANIMATE_INTERVAL = 0.01f;
    public float damage_animate_timer;

    private static final float SMALL_SHIP_HEIGHT = 0.12f;
    private static final float MID_SHIP_HEIGHT = 0.2f;
    private static final float BIG_SHIP_HEIGHT = 0.3f;

    private final int SMALL_SHIP_HP = 1;
    private final int MID_SHIP_HP = 6;
    private final int BIG_SHIP_HP = 25;

    private final float SMALL_SHOOT = 0.01f;
    private final float MID_SHOOT = 0.02f;
    private final float BIG_SHOOT = 0.03f;
    public int hp;

    private String size;
    private Rect worldBounds;
    private Vector2 buf;
    private float directionInTheEnd;

    private Sound soundShoot;
    private BulletPool bulletPool;
    private TextureRegion bulletRegion;
    private final Vector2 bulletV;
    private final Vector2 bulletPos;
    private ExplosionPool explosionPool;

    private float interval = 0.4f;
    private float timer = interval;

    public static int level = 1;

    public EnemyShip(TextureAtlas atlas, String size, BulletPool bulletPool, ExplosionPool explosionPool){
        super(atlas.findRegion(size), 1, 2, 2);
        this.size = size;
        buf = new Vector2();
        directionInTheEnd = Rnd.nextFloat(-1f,1f);
        this.explosionPool = explosionPool;
        this.bulletPool = bulletPool;
        soundShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        this.bulletRegion = atlas.findRegion("bulletEnemy");
        this.bulletV = new Vector2(0, -0.5f);
        this.bulletPos = new Vector2();
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
        switch (size){
            case "enemy0":setHeightProportion(SMALL_SHIP_HEIGHT);
            break;
            case "enemy1":setHeightProportion(MID_SHIP_HEIGHT);
            break;
            case "enemy2":setHeightProportion(BIG_SHIP_HEIGHT);
            break;
        }

        pos.set(Rnd.nextFloat(worldBounds.getLeft(),worldBounds.getRight()),worldBounds.getTop());
        this.worldBounds = worldBounds;
    }

    public void set(Rect worldBounds){
        switch (size){
            case "enemy0":setHeightProportion(SMALL_SHIP_HEIGHT);
                pos.set(Rnd.nextFloat(worldBounds.getLeft(),worldBounds.getRight()),worldBounds.getTop() + SMALL_SHIP_HEIGHT);
                this.hp = SMALL_SHIP_HP;
                break;
            case "enemy1":setHeightProportion(MID_SHIP_HEIGHT);
                pos.set(Rnd.nextFloat(worldBounds.getLeft(),worldBounds.getRight()),worldBounds.getTop() + MID_SHIP_HEIGHT);
                this.hp = MID_SHIP_HP;
                break;
            case "enemy2":setHeightProportion(BIG_SHIP_HEIGHT);
                pos.set(Rnd.nextFloat(worldBounds.getLeft(),worldBounds.getRight()),worldBounds.getTop() + BIG_SHIP_HEIGHT);
                this.hp = BIG_SHIP_HP;
                break;
        }

        this.worldBounds = worldBounds;
    }

    public void update(Hero hero, float delta) {
        if (getTop() >= worldBounds.getTop()) {
            moveOutScreen();
        }else {
        moveOnScreen(hero);}

        if(getTop() <= worldBounds.getTop()) {
            timer += delta;
            if (timer >= interval) {
                shoot();
                timer = 0f;
            }
        }

        if(getTop() <= worldBounds.getBottom()){
            destroy();
        }

        damage_animate_timer += delta;
        if (damage_animate_timer >= DAMAGE_ANIMATE_INTERVAL) {
            frame = 0;
        }
    }

    public void dispose(){
        soundShoot.dispose();
    }

    public void moveOutScreen(){
        this.pos.add(0f,-0.01f);
    }

    public void moveOnScreen(Hero hero){
        switch (size){
            case "enemy0":if(this.pos.y >= -0.1f) {
                buf.set(hero.pos);
                buf.sub(this.pos).scl(0.005f);
                this.pos.add(buf);
            }else{
                if(directionInTheEnd <= 0) {this.pos.add(-0.002f,-0.002f);}
                else {this.pos.add(0.002f,-0.002f);}
            }
                break;
            case "enemy1":if(this.pos.y >= -0.1f) {
                buf.set(hero.pos);
                buf.sub(this.pos).scl(0.0025f);
                this.pos.add(buf);
            }else{
                if(directionInTheEnd <= 0) {this.pos.add(-0.001f,-0.001f);}
                else {this.pos.add(0.001f,-0.001f);}
            }
                break;
            case "enemy2":if(this.pos.y >= -0.1f) {
                buf.set(hero.pos);
                buf.sub(this.pos).scl(0.001f);
                this.pos.add(buf);
            }else{
                if(directionInTheEnd <= 0) {this.pos.add(-0.0005f,-0.0005f);}
                else {this.pos.add(0.0005f,-0.0005f);}
            }
                break;

        }
    }

    private void shoot() {
        soundShoot.play(1f);
        level = GameScreen.frags / 10 + 1;
        Bullet bullet = bulletPool.obtain();
        bulletPos.set(pos.x, getBottom());
        switch (size){
            case "enemy0":bullet.set(this, bulletRegion, bulletPos, bulletV, SMALL_SHOOT, worldBounds, 1 * level);
                break;
            case "enemy1":bullet.set(this, bulletRegion, bulletPos, bulletV, MID_SHOOT, worldBounds, 10 * level);
                break;
            case "enemy2":bullet.set(this, bulletRegion, bulletPos, bulletV, BIG_SHOOT, worldBounds, 15 * level);
                break;
        }

    }

    @Override
    public void destroy() {
        super.destroy();
        boom();
    }

    protected void boom() {
        Explosion explosion = explosionPool.obtain();
        explosion.set(getHeight(), pos);
    }

    public void damage(int damage){
        this.hp -= damage;
        if (hp <= 0) {
            destroy();
        }
        damage_animate_timer = 0f;
        frame = 1;
    }


}
