package eu32k.spaceDingus.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;

import eu32k.spaceDingus.core.component.ActorComponent;
import eu32k.spaceDingus.core.component.PhysicsShieldComponent;
import eu32k.spaceDingus.core.component.ShieldComponent;

public class ShieldSystem extends EntityProcessingSystem {

   private ComponentMapper<ShieldComponent> sm;
   private ComponentMapper<PhysicsShieldComponent> pm;
   private ComponentMapper<ActorComponent> am;

   @SuppressWarnings("unchecked")
   public ShieldSystem() {
      super(Aspect.getAspectForAll(ShieldComponent.class, PhysicsShieldComponent.class));
   }

   @Override
   protected void initialize() {
      sm = world.getMapper(ShieldComponent.class);
      pm = world.getMapper(PhysicsShieldComponent.class);
      am = world.getMapper(ActorComponent.class);
   }

   @Override
   protected void process(Entity e) {
      float shield = sm.get(e).shield;
      float max = sm.get(e).maxShield;

      if (shield <= 0) {
         pm.get(e).disableCollision();
      } else {
         pm.get(e).enableCollision();
      }

      if (am.has(e)) {
         float value = shield / max;
         if (value > 0) {
            value = MathUtils.clamp(value, 0.3f, 1.0f);
         }
         am.get(e).actor.getColor().a = value;
      }
   }
}
