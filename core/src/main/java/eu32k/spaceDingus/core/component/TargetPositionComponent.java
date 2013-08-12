package eu32k.spaceDingus.core.component;

import com.artemis.Component;

public class TargetPositionComponent extends Component {

   public float x = 0;
   public float y = 0;

   public TargetPositionComponent init(float x, float y) {
      this.x = x;
      this.y = y;
      return this;
   }
}
