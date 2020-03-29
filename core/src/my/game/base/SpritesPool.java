package my.game.base;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;

import my.game.math.Rnd;
import my.game.sprite.Hero;

public abstract class SpritesPool<T extends Sprite> {

    protected final List<T> activeObjects = new ArrayList<>();

    protected final List<T> freeObjects = new ArrayList<>();

    protected abstract T newObject();

    public T obtain() {
        T object;
        System.out.println("21");
        if (freeObjects.isEmpty()) {
            object = newObject();
        } else {
            int i = (int)Rnd.nextFloat(0, freeObjects.size());
            object = freeObjects.remove(i);
        }
        activeObjects.add(object);
        System.out.println(getClass().getName() + " active/free: " + activeObjects.size() + "/" + freeObjects.size());
        return object;
    }

    public void updateActiveSprites(float delta) {
        for (Sprite sprite : activeObjects) {
            if (!sprite.isDestroyed()) {
                sprite.update(delta);
            }
        }
    }

    public void updateActiveSprites(Hero hero, float delta) {
        for (Sprite sprite : activeObjects) {
            if (!sprite.isDestroyed()) {
                sprite.update(hero,delta);
            }
        }
    }

    public void drawActiveSprites(SpriteBatch batch) {
        for (Sprite sprite : activeObjects) {
            if (!sprite.isDestroyed()) {
                sprite.draw(batch);
            }
        }
    }

    public void  freeAllDestroyedActiveObjects() {
        for (int i = 0; i < activeObjects.size(); i++) {
            T sprite = activeObjects.get(i);
            if (sprite.isDestroyed()) {
                free(sprite);
                i--;
                sprite.flushDestroy();
            }
        }
    }

    public void dispose() {
        activeObjects.clear();
        freeObjects.clear();
        for(Sprite sprite : activeObjects){
            sprite.dispose();
        }
    }

    public List<T> getActiveObjects() {
        return activeObjects;
    }

    private void free(T object) {
        if (activeObjects.remove(object)) {
            freeObjects.add(object);
        }
        System.out.println(getClass().getName() + " active/free: " + activeObjects.size() + "/" + freeObjects.size());
    }
}
