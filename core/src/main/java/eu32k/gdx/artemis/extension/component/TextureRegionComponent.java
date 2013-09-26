package eu32k.gdx.artemis.extension.component;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import eu32k.gdx.artemis.base.Component;

public class TextureRegionComponent extends Component {

   public TextureRegion textureRegion;
   public boolean show = true;

   public TextureRegionComponent init(TextureRegion textureRegion) {
      this.textureRegion = textureRegion;
      show = true;
      return this;
   }
}
