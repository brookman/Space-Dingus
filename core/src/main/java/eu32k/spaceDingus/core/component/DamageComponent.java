package eu32k.spaceDingus.core.component;

import eu32k.gdx.artemis.base.Component;

public class DamageComponent extends Component {
   public float damage;
   public boolean nonrecurring;
   public boolean removeAfterDamage;

   public DamageComponent init(float damage, boolean nonrecurring, boolean removeAfterDamage) {
      this.damage = damage;
      this.nonrecurring = nonrecurring;
      this.removeAfterDamage = removeAfterDamage;
      return this;
   }

}
