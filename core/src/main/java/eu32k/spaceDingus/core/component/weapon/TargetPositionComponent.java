package eu32k.spaceDingus.core.component.weapon;

import eu32k.gdx.artemis.base.Component;

public class TargetPositionComponent extends Component {

   public float x = 0;
   public float y = 0;
   public boolean enabled = true;

   public TargetPositionComponent init(float x, float y) {
      this.x = x;
      this.y = y;
      enabled = true;
      return this;
   }
}
