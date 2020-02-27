package my.game.screen;

import com.badlogic.gdx.graphics.Texture;

public class Logo {
    private Texture img;
    private float imgSize;
    Logo(){
        img = new Texture("BadLogic.jpg");
        imgSize = 0.15f;
    }
    public Texture getImg(){
        return this.img;
    }

    public float getImgSize() {
        return imgSize;
    }
}
