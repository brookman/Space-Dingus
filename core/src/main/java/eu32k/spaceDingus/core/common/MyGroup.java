package eu32k.spaceDingus.core.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;

public class MyGroup extends Group {
   private TextureRegion textureRegion;

   public MyGroup(TextureRegion texture) {
      this.textureRegion = texture;
   }

   @Override
   public void draw(SpriteBatch batch, float parentAlpha) {
      if (textureRegion == null) {
         return;
      }
      Color color = getColor();
      batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
      batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());

      // batch.draw(sprite, getX() - sprite.getWidth() / 2, getY() - sprite.getHeight() / 2, sprite.getWidth() / 2, sprite.getHeight() / 2, sprite.getWidth(), sprite.getHeight(), 1f, 1f,
      // getRotation());
   }
}