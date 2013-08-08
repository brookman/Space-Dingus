package eu32k.spaceDingus.core.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Sprite;

import eu32k.spaceDingus.common.Textures;

public class SpriteComponent extends Component {

   public com.badlogic.gdx.graphics.g2d.Sprite sprite;
   public float alpha = 1.0f;
   public int layer = 0;

   public SpriteComponent init(String texturePath, float width, float height, int layer) {
      if (sprite == null) {
         sprite = new Sprite();
      }
      sprite.setRegion(Textures.get(texturePath));
      sprite.setSize(width, height);
      this.layer = layer;
      return this;
   }
}