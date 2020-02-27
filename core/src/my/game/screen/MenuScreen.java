package my.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import my.game.base.BaseScreen;
import my.game.math.Rect;
import my.game.sprite.Background;

public class MenuScreen extends BaseScreen {

    private Texture bg;
    private Vector2 pos;
    private Vector2 direction;
    private Vector2 buf;
    private Background background;
    private Logo logo;

    @Override
    public void show() {
        super.show();
        logo = new Logo();
        bg = new Texture("StarWars.jpg");
        pos = new Vector2();
        background = new Background(bg);
        direction = new Vector2();
        buf = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0.5f, 0.9f, 0.4f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        buf.set(touch);
        if(buf.sub(pos).len() > direction.len())  pos.add(direction);
        batch.draw(logo.getImg(), pos.x, pos.y, logo.getImgSize(), logo.getImgSize());
        batch.end();
    }

    @Override
    public void dispose() {
        logo.getImg().dispose();
        bg.dispose();
        super.dispose();
    }

    @Override
    public void resize(Rect worldBounds) {
        super.resize(worldBounds);
        background.resize(worldBounds);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer, int button) {
        direction.x = touch.x - pos.x;
        direction.y = touch.y - pos.y;
        direction.set(direction.x/direction.len(),direction.y/direction.len());
        direction.scl(0.01f);
        return super.touchDown(touch, pointer, button);
    }
}