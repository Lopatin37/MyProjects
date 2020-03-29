package my.game.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import my.game.base.Sprite;
import my.game.math.Rect;
import my.game.pool.BulletPool;
import my.game.pool.ExplosionPool;

public class Hero extends Sprite {
    public final float DAMAGE_ANIMATE_INTERVAL = 0.01f;
    public float damage_animate_timer;
    private static final float SHIP_HEIGHT = 0.17f;
    private Vector2 v0;
    private Vector2 v;
    private Rect worldBounds;
    private boolean pressedLeft;
    private boolean pressedRight;
    private Vector2 touch;
    private boolean toRight;
    private boolean toLeft;
    private Sound soundShoot;

    private BulletPool bulletPool;
    private TextureRegion bulletRegion;
    private final Vector2 bulletV;
    private final Vector2 bulletPos;
    private int damage;
    private ExplosionPool explosionPool;

    private float interval;
    private float timer;
    private int hp;

    public Hero(TextureAtlas atlas, BulletPool bulletPool, ExplosionPool explosionPool) {
        super(atlas.findRegion("main_ship"), 1, 2, 2);
        v0 = new Vector2(0.005f,0f);
        v =  new Vector2();
        hp = 100;
        interval = 0.2f;
        pressedLeft = false;
        pressedRight = false;
        this.bulletPool = bulletPool;
        this.explosionPool = explosionPool;
        this.bulletRegion = atlas.findRegion("bulletMainShip");
        this.bulletV = new Vector2(0, 0.5f);
        this.bulletPos = new Vector2();
        this.damage = 2;
        this.touch = new Vector2();
        soundShoot = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        damage_animate_timer = 0f;
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
        pos.add(v);
        if(getLeft() <= worldBounds.getLeft()) stop();
        if(getRight() >= worldBounds.getRight()) stop();
        if(toRight && pos.x >= touch.x) stop();
        if(toLeft && pos.x <=touch.x) stop();
        timer += delta;
        if(timer >= interval){
            shoot();
            timer = 0f;
        }
        damage_animate_timer += delta;
        if (damage_animate_timer >= DAMAGE_ANIMATE_INTERVAL) {
            frame = 0;
        }
    }

    public void dispose(){
        soundShoot.dispose();
    }

    @Override
    public void touchDown(Vector2 touch, int pointer, int button) {
        if(touch.x < pos.x){
            toLeft = true;
            toRight = false;
           moveLeft();
        }else{
            toLeft = false;
            toRight = true;
           moveRight();
        }
        this.touch.set(touch.x,pos.y);
    }

    @Override
    public void touchUp(Vector2 touch, int pointer, int button) {
        stop();
        toLeft = false;
        toRight = false;
    }

    @Override
    public void touchDragged(Vector2 touch, int pointer) {
        if(touch.x < pos.x){
            stop();
            moveLeft();
            toLeft = true;
            toRight = false;
        }
        if(touch.x > pos.x){
            stop();
            moveRight();
            toLeft = false;
            toRight = true;
        }
        this.touch.set(touch.x,pos.y);
        this.touch.set(touch.x,pos.y);
    }

    public void keyDown(int keyCode){
        toRight = false;
        toLeft = false;
        switch (keyCode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
        }
    }

    public void keyUp(int keyCode){
        switch (keyCode){
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if(pressedRight){
                    moveRight();
                }else{
                    stop();
                }
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if(pressedLeft){
                    moveLeft();
                }else{
                    stop();
                }
                break;
        }
    }

    private void moveLeft(){
        if(!(getLeft() <= worldBounds.getLeft())) {
            v.set(v0).rotate(180);
        }
    }
    private void moveRight(){
        if(!(getRight() >= worldBounds.getRight())) {
            v.set(v0);
        }
    }
    private void stop(){
        v.setZero();
        toLeft= false;
        toRight = false;
    }

    private void shoot() {
        soundShoot.play(1f);
        Bullet bullet = bulletPool.obtain();
        bulletPos.set(pos.x, getTop());
        bullet.set(this, bulletRegion, bulletPos, bulletV, 0.01f, worldBounds, 1);
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

    public Vector2 getV() {
        return v;
    }

    public int getHp() {
        if(hp < 0) {return 0;}
        return hp;
    }
}
