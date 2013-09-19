package eu32k.spaceDingus.core.component;

import eu32k.gdx.artemis.base.Component;

public class DamagableComponent extends Component {
   public float currentDamage = 0;

   public DamagableComponent init() {
      currentDamage = 0;
      return this;
   }
}
