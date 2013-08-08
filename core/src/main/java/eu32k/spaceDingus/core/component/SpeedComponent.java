package eu32k.spaceDingus.core.component;

import com.artemis.Component;

public class SpeedComponent extends Component {

   public float speed;

   public SpeedComponent init(float speed) {
      this.speed = speed;
      return this;
   }
}
