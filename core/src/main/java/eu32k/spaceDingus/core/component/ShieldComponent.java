package eu32k.spaceDingus.core.component;

import eu32k.gdx.artemis.base.Component;

public class ShieldComponent extends Component {
   public float shield;
   public float maxShield;

   public ShieldComponent init(float maxShield) {
      this.maxShield = maxShield;
      shield = maxShield;
      return this;
   }

   public ShieldComponent init(float maxShield, float shield) {
      this.maxShield = maxShield;
      this.shield = shield;
      return this;
   }
}