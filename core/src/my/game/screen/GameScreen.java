package my.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import my.game.base.BaseScreen;
import my.game.math.Rect;
import my.game.pool.BulletPool;
import my.game.pool.EnemyPool;
import my.game.pool.ExplosionPool;
import my.game.sprite.Background;
import my.game.sprite.Bullet;
import my.game.sprite.Enemy;
import my.game.sprite.Hero;
import my.game.sprite.Star;

public class GameScreen extends BaseScreen {
    private TextureAtlas mainAtlas;
    private TextureAtlas menuAtlas;
    private Texture bg;
    private Background background;
    private Star[] stars;
    private static final int STAR_COUNT = 256;
    private Hero hero;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private Enemy enemy;
    private Music music;
    private Sound explosionSound;

    private ExplosionPool explosionPool;

    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        mainAtlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        menuAtlas = new TextureAtlas(Gdx.files.internal("textures/menuAtlas.tpack"));
        stars = new Star[STAR_COUNT];
        for(int i = 0; i < STAR_COUNT; i++){
            stars[i] = new Star(menuAtlas);
        }
        bulletPool = new BulletPool();
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        explosionPool = new ExplosionPool(mainAtlas, explosionSound);
        enemyPool = new EnemyPool(mainAtlas, bulletPool, explosionPool);
        hero = new Hero(mainAtlas,bulletPool);
        enemy = new Enemy(mainAtlas, enemyPool);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();

    }

    @Override
    public void render(float delta) {
        update(delta);
        freeAllDestroyed();
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        hero.resize(worldBounds);
        enemy.resize(worldBounds);
        for(Star star : stars){
            star.resize(worldBounds);
        }
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void resume() {
        music.play();
    }

    @Override
    public void dispose() {
        bg.dispose();
        mainAtlas.dispose();
        menuAtlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        music.dispose();
        hero.dispose();
        explosionPool.dispose();
        explosionSound.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        hero.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode){
        hero.keyUp(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        hero.touchDown(touch,pointer,button);
        return  false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        hero.touchUp(touch,pointer,button);
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        hero.touchDragged(touch,pointer);
        return false;
    }

    private void update(float delta){
        for(Star star : stars){
            star.update(delta);
        }
        hero.update(delta);
        enemy.update(delta);
        bulletPool.updateActiveSprites(delta);
        enemyPool.updateActiveSprites(hero,delta);
        explosionPool.updateActiveSprites(delta);
    }

    private void freeAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
    }

    private void draw(){
        Gdx.gl.glClearColor(0.5f, 0.9f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        hero.draw(batch);
        for(Star star : stars){
            star.draw(batch);
        }
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        explosionPool.drawActiveSprites(batch);
        batch.end();
    }

}
