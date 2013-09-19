package eu32k.spaceDingus.core.component.engine;

import eu32k.gdx.artemis.base.Component;

public class EngineComponent extends Component {

   public float thrust;
   public boolean isRunning;
   public int directions;

   public EngineComponent init(float thrust, int directions) {
      this.thrust = thrust;
      this.directions = directions;
      isRunning = false;
      return this;
   }
}
