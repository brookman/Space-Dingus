package eu32k.spaceDingus.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.core.common.Time;
import eu32k.spaceDingus.core.component.ExpireComponent;
import eu32k.spaceDingus.core.component.RemoveMeComponent;

public class ExpireSystem extends EntityProcessingSystem {

   private ComponentMapper<ExpireComponent> em;

   @SuppressWarnings("unchecked")
   public ExpireSystem() {
      super(Aspect.getAspectForAll(ExpireComponent.class));
   }

   @Override
   protected void initialize() {
      em = world.getMapper(ExpireComponent.class);
   }

   @Override
   protected void process(Entity e) {
      if (Time.getTime() >= em.get(e).expireAt) {
         e.addComponent(Pools.get(RemoveMeComponent.class).obtain());
         world.changedEntity(e);
      }
   }
}
