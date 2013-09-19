package eu32k.spaceDingus.core.component;

import eu32k.gdx.artemis.base.Component;

public class SpeedComponent extends Component {

   public float speed;

   public SpeedComponent init(float speed) {
      this.speed = speed;
      return this;
   }
}
