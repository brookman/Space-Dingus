package eu32k.spaceDingus.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

import eu32k.spaceDingus.core.component.DeathAnimationComponent;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.TransformComponent;

public class DeathSystem extends EntityProcessingSystem {

   private ComponentMapper<HealthComponent> hm;
   private ComponentMapper<PhysicsComponent> phm;
   private ComponentMapper<TransformComponent> tm;
   private ComponentMapper<DeathAnimationComponent> dm;

   @SuppressWarnings("unchecked")
   public DeathSystem() {
      super(Aspect.getAspectForAll(HealthComponent.class));
   }

   @Override
   protected void initialize() {
      hm = world.getMapper(HealthComponent.class);
      phm = world.getMapper(PhysicsComponent.class);
      tm = world.getMapper(TransformComponent.class);
      dm = world.getMapper(DeathAnimationComponent.class);
   }

   @Override
   protected void process(Entity e) {
      if (hm.get(e).health <= 0) {

         if (dm.has(e) && tm.has(e)) {
            TransformComponent pos = tm.get(e);
            dm.get(e).createAnimation(pos.x, pos.y, pos.rotation);
         }

         if (phm.has(e)) {
            phm.get(e).delete();
         }
         e.deleteFromWorld();
      }
   }
}
