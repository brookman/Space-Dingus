package eu32k.spaceDingus.core.component;

import eu32k.gdx.artemis.base.Component;

public class MovableComponent extends Component {

   public float maxMovementSpeed;
   public float maxRotationSpeed;
   public int directions;

   public MovableComponent init(float maxMovementSpeed, float maxRotationSpeed) {
      this.maxMovementSpeed = maxMovementSpeed;
      this.maxRotationSpeed = maxRotationSpeed;
      directions = 0;
      return this;
   }
}
