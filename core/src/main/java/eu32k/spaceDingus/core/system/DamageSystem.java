package eu32k.spaceDingus.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

import eu32k.spaceDingus.core.component.DamagableComponent;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.ShieldComponent;

public class DamageSystem extends EntityProcessingSystem {

   private ComponentMapper<DamagableComponent> dm;
   private ComponentMapper<ShieldComponent> sm;
   private ComponentMapper<HealthComponent> hm;

   @SuppressWarnings("unchecked")
   public DamageSystem() {
      super(Aspect.getAspectForAll(DamagableComponent.class).one(ShieldComponent.class, HealthComponent.class));
   }

   @Override
   protected void initialize() {
      dm = world.getMapper(DamagableComponent.class);
      sm = world.getMapper(ShieldComponent.class);
      hm = world.getMapper(HealthComponent.class);
   }

   @Override
   protected void process(Entity e) {
      DamagableComponent damage = dm.get(e);
      if (sm.has(e)) {
         ShieldComponent shield = sm.get(e);
         if (shield.shield >= damage.currentDamage) {
            shield.shield -= damage.currentDamage;
            damage.currentDamage = 0;
            return;
         }
         damage.currentDamage -= shield.shield;
         shield.shield = 0;
      }

      if (hm.has(e)) {
         HealthComponent health = hm.get(e);
         if (health.health >= damage.currentDamage) {
            health.health -= damage.currentDamage;
            damage.currentDamage = 0;
            return;
         }
         health.health = 0;
         damage.currentDamage = 0;
      }
   }
}
