package eu32k.spaceDingus.core.system;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.common.RemoveMarker;
import eu32k.gdx.common.Time;
import eu32k.spaceDingus.core.component.ExpireComponent;

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
         RemoveMarker.markForRemovalRecursively(e);
      }
   }
}
