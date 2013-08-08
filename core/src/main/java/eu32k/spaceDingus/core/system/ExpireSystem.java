package eu32k.spaceDingus.core.system;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.common.Time;
import eu32k.spaceDingus.core.component.ExpireComponent;
import eu32k.spaceDingus.core.component.PhysicsComponent;

public class ExpireSystem extends EntityProcessingSystem {

   private ComponentMapper<ExpireComponent> em;
   private ComponentMapper<PhysicsComponent> pm;

   @SuppressWarnings("unchecked")
   public ExpireSystem() {
      super(Aspect.getAspectForAll(ExpireComponent.class));
   }

   @Override
   protected void initialize() {
      em = world.getMapper(ExpireComponent.class);
      pm = world.getMapper(PhysicsComponent.class);
   }

   @Override
   protected void process(Entity e) {
      if (Time.getTime() >= em.get(e).expireAt) {
         if (pm.has(e)) {
            pm.get(e).delete();
         }

         Bag<Component> comps = new Bag<Component>();
         e.getComponents(comps);
         for (Component c : comps) {
            Pools.free(c);
         }
         e.deleteFromWorld();
      }
   }
}
