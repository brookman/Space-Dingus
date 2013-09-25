package eu32k.spaceDingus.core.system;

import com.badlogic.gdx.math.MathUtils;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.PhysicsShieldComponent;
import eu32k.spaceDingus.core.component.ShieldComponent;

public class ShieldSystem extends EntityProcessingSystem {

   private ComponentMapper<ShieldComponent> sm;
   private ComponentMapper<PhysicsShieldComponent> pm;
   private ComponentMapper<HealthComponent> hm;
   private ComponentMapper<ActorComponent> am;

   @SuppressWarnings("unchecked")
   public ShieldSystem() {
      super(Aspect.getAspectForAll(ShieldComponent.class, HealthComponent.class, PhysicsShieldComponent.class));
   }

   @Override
   protected void initialize() {
      sm = world.getMapper(ShieldComponent.class);
      pm = world.getMapper(PhysicsShieldComponent.class);
      hm = world.getMapper(HealthComponent.class);
      am = world.getMapper(ActorComponent.class);
   }

   @Override
   protected void process(Entity e) {
      float health = hm.get(e).health;
      float max = hm.get(e).maxHealth;

      if (health <= 0) {
         pm.get(e).disableCollision();
      } else {
         pm.get(e).enableCollision();
      }

      if (am.has(e)) {
         float value = health / max;
         if (value > 0) {
            value = MathUtils.clamp(value, 0.3f, 1.0f);
         }
         am.get(e).actor.getColor().a = value;
      }
   }
}
