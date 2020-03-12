package my.game.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import my.game.base.BaseScreen;
import my.game.math.Rect;
import my.game.sprite.Background;
import my.game.sprite.ButtonExit;
import my.game.sprite.ButtonPlay;
import my.game.sprite.Star;

public class MenuScreen extends BaseScreen {
    private Game game;
    private Texture bg;
    private Background background;
    private TextureAtlas atlas;
    private Star[] stars;
    private static final int STAR_COUNT = 256;
    private ButtonExit buttonExit;
    private ButtonPlay buttonPlay;



    public MenuScreen(Game game){
        this.game = game;
    }
    @Override
    public void show() {
        super.show();
        bg = new Texture("textures/bg.png");
        background = new Background(bg);
        atlas = new TextureAtlas(Gdx.files.internal("textures/menuAtlas.tpack"));
        buttonExit = new ButtonExit(atlas);
        buttonPlay = new ButtonPlay(atlas,game);
        stars = new Star[STAR_COUNT];
        for(int i = 0; i < STAR_COUNT; i++){
            stars[i] = new Star(atlas);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        update(delta);
        draw();
    }
    private void draw(){
        Gdx.gl.glClearColor(0.5f, 0.9f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        buttonExit.draw(batch);
        buttonPlay.draw(batch);
        for(Star star : stars){
            star.draw(batch);
        }
        batch.end();
    }

    private void update(float delta){
        for(Star star : stars){
            star.update(delta);
        }
    }

    @Override
    public void dispose() {
        bg.dispose();
        atlas.dispose();
        super.dispose();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        buttonExit.resize(worldBounds);
        buttonPlay.resize(worldBounds);
        background.resize(worldBounds);
        for(Star star : stars){
            star.resize(worldBounds);
        }
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        buttonExit.touchDown(touch,pointer,button);
        buttonPlay.touchDown(touch,pointer,button);
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer, int button) {
        buttonExit.touchUp(touch,pointer,button);
        buttonPlay.touchUp(touch,pointer,button);
        return false;
    }
}