package eu32k.spaceDingus.core.component;

import eu32k.gdx.artemis.base.Component;

public class DamageComponent extends Component {
   public float damage;
   public boolean nonrecurring;

   public DamageComponent init(float damage, boolean nonrecurring) {
      this.damage = damage;
      this.nonrecurring = nonrecurring;
      return this;
   }

}
