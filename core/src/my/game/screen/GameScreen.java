package my.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import my.game.base.BaseScreen;
import my.game.math.Rect;
import my.game.pool.BulletPool;
import my.game.pool.EnemyPool;
import my.game.pool.ExplosionPool;
import my.game.sprite.Background;
import my.game.sprite.Bullet;
import my.game.sprite.Enemy;
import my.game.sprite.EnemyShip;
import my.game.sprite.Hero;
import my.game.sprite.MessageGameOver;
import my.game.sprite.NewGame;
import my.game.sprite.Star;
import my.game.sprite.TrackingStar;
import my.game.utils.Font;

public class GameScreen extends BaseScreen {
    private static final float FONT_SIZE = 0.02f;
    private static final String FRAGS = "Frags:";
    private static final String HP = "HP:";
    private static final String LEVEL = "Level:";
    private static final float PADDING = 0.01f;
    private enum State{PLAYING, PAUSE, GAME_OVER}
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
    private MessageGameOver messageGameOver;
    private NewGame newGame;
    private ExplosionPool explosionPool;
    private State state;
    private State prevState;
    private Game game;

    private Font font;

    public static int frags;

    private StringBuilder sbFrags;
    private StringBuilder sbHp;
    private StringBuilder sbLevel;

    public GameScreen(Game game){
        this.game = game;
    }
    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        mainAtlas = new TextureAtlas(Gdx.files.internal("textures/mainAtlas.tpack"));
        menuAtlas = new TextureAtlas(Gdx.files.internal("textures/menuAtlas.tpack"));
        stars = new Star[STAR_COUNT];
        bulletPool = new BulletPool();
        explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
        explosionPool = new ExplosionPool(mainAtlas, explosionSound);
        enemyPool = new EnemyPool(mainAtlas, bulletPool, explosionPool);
        hero = new Hero(mainAtlas,bulletPool, explosionPool);
        for (int i = 0; i < STAR_COUNT; i++) {
            stars[i] = new Star(mainAtlas);
        }
        enemy = new Enemy(mainAtlas, enemyPool);
        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        music.setLooping(true);
        music.play();
        messageGameOver = new MessageGameOver(mainAtlas);
        newGame = new NewGame(mainAtlas, game);
        font = new Font("font/font.fnt", "font/font.png");
        font.setSize(FONT_SIZE);
        frags = 0;
        state = State.PLAYING;
        prevState = state;
        sbFrags = new StringBuilder();
        sbHp = new StringBuilder();
        sbLevel = new StringBuilder();
    }

    @Override
    public void render(float delta) {
        update(delta);
        checkCollisions();
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
        messageGameOver.resize(worldBounds);
        newGame.resize(worldBounds);
    }

    @Override
    public void pause() {
        prevState = state;
        state = State.PAUSE;
        music.pause();
    }

    @Override
    public void resume() {
        state = prevState;
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
        font.dispose();
        super.dispose();
    }

    public void checkCollisions(){
        if(state == State.GAME_OVER) {
            return;
        }
        List<EnemyShip> enemyShips = enemyPool.getActiveObjects();
        for(EnemyShip enemy : enemyShips){
            if (hero.isMe(enemy.pos)) {
                enemy.destroy();
                frags++;
            }
        }

        List<Bullet> bullets = bulletPool.getActiveObjects();
        for(Bullet bullet : bullets) {
            if (hero.isMe(bullet.pos)) {
                hero.damage(bullet.getDamage());
                bullet.destroy();
            }

        }

        for(EnemyShip enemyShip : enemyShips){
            for(Bullet bullet : bullets){
                if(bullet.getOwner() instanceof Hero){
                    if(enemyShip.isMe(bullet.pos)){
                        enemyShip.damage(bullet.getDamage());
                        bullet.destroy();
                        if (enemyShip.isDestroyed()) {
                            frags++;
                        }
                    }
                }
            }
        }
        if(hero.isDestroyed()){
            state = State.GAME_OVER;
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if(state == State.PLAYING) {
            hero.keyDown(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode){
        if(state == State.PLAYING) {
            hero.keyUp(keycode);
        }
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        if(state == State.PLAYING) {
            hero.touchDown(touch, pointer, button);
        }
        if(state == State.GAME_OVER){
            newGame.touchDown(touch, pointer, button);
        }
        return  false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        if(state == State.PLAYING) {
            hero.touchUp(touch, pointer, button);
        }
        if(state == State.GAME_OVER){
            newGame.touchUp(touch, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchDragged(Vector2 touch, int pointer) {
        if(state == State.PLAYING) {
            hero.touchDragged(touch, pointer);
        }
        return false;
    }

    private void update(float delta){
        for(Star star : stars){
            star.update(delta);
        }
        explosionPool.updateActiveSprites(delta);
        if(state == State.PLAYING) {
            hero.update(delta);
            enemy.update(delta);
            bulletPool.updateActiveSprites(delta);
            enemyPool.updateActiveSprites(hero, delta);
        }
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
        for(Star star : stars){
            star.draw(batch);
        }
        explosionPool.drawActiveSprites(batch);
        if(state == State.PLAYING) {
            hero.draw(batch);
            bulletPool.drawActiveSprites(batch);
            enemyPool.drawActiveSprites(batch);
        } else {
            messageGameOver.draw(batch);
            newGame.draw(batch);
        }
        printInfo();
        batch.end();
    }

    private void printInfo() {
        sbFrags.setLength(0);
        sbHp.setLength(0);
        sbLevel.setLength(0);
        font.draw(batch, sbFrags.append(FRAGS).append(frags), worldBounds.getLeft() + PADDING, worldBounds.getTop() - PADDING);
        font.draw(batch, sbHp.append(HP).append(hero.getHp()), worldBounds.pos.x, worldBounds.getTop() - PADDING, Align.center);
        font.draw(batch, sbLevel.append(LEVEL).append(EnemyShip.level), worldBounds.getRight() - PADDING, worldBounds.getTop() - PADDING, Align.right);
    }

}
