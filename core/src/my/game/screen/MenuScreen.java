package my.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import my.game.base.BaseScreen;

public class MenuScreen extends BaseScreen {
    private Vector2 pos;
    private Vector2 touch;
    private Vector2 aim;
    private Vector2 dir;
    Texture img;
    Texture background;

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        super.touchDown(screenX, screenY, pointer, button);
        touch.set(screenX,Gdx.graphics.getHeight() - screenY);

        if(touch.x < img.getWidth()/2) touch.x = img.getWidth()/2;
        if(touch.x > Gdx.graphics.getWidth() - img.getWidth()/2) touch.x = Gdx.graphics.getWidth() - img.getWidth()/2;
        if(touch.y < img.getHeight()/2) touch.y = img.getHeight()/2;
        if(touch.y > Gdx.graphics.getHeight() - img.getHeight()/2) touch.y = Gdx.graphics.getHeight() - img.getHeight()/2;
        aim.set(Math.round(touch.x - img.getWidth()/2 - (pos.x - img.getWidth()/2)), Math.round(touch.y - img.getHeight()/2 - (pos.y - img.getHeight()/2)));
        dir.set((int)aim.x/aim.len(), (int)aim.y/aim.len());
        dir.scl(5);
        return false;
    }

    @Override
    public void show() {
        super.show();
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        background = new Texture("StarWars.jpg");
        pos = new Vector2(img.getWidth()/2,img.getHeight()/2);
        touch = new Vector2(img.getWidth()/2,img.getHeight()/2);
        aim = new Vector2();
        dir = new Vector2();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0.5f,0.9f,0.4f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(background, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.draw(img,pos.x - img.getWidth()/2,pos.y - img.getHeight()/2);
        if(!(Math.abs(touch.x - pos.x) < 3) || !(Math.abs(touch.y - pos.y) < 3)){
            move();
        }
        batch.end();
    }

    @Override
    public void dispose() {
        img.dispose();
        background.dispose();
        super.dispose();
    }
    public void move(){
        pos.add(dir);
    }
}
