package my.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import my.game.base.BaseScreen;
import my.game.math.Rect;
import my.game.sprite.Background;
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
        hero = new Hero(mainAtlas);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
        hero.resize(worldBounds);
        for(Star star : stars){
            star.resize(worldBounds);
        }
    }

    @Override
    public void dispose() {
        bg.dispose();
        mainAtlas.dispose();
        menuAtlas.dispose();
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        hero.keyDown(keycode);
        return false;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        hero.touchDown(touch,pointer,button);
        return  false;
    }



    private void update(float delta){
        for(Star star : stars){
            star.update(delta);
        }
        hero.update(delta);
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
        batch.end();
    }

}
