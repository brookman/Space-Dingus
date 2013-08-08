package eu32k.spaceDingus.core.component;

import com.artemis.Component;

public class HealthComponent extends Component {
   public float health;
   public float maxHealth;

   public HealthComponent init(float maxHealth) {
      this.maxHealth = maxHealth;
      health = maxHealth;
      return this;
   }

   public HealthComponent init(float maxHealth, float health) {
      this.maxHealth = maxHealth;
      this.health = health;
      return this;
   }
}