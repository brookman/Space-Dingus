package eu32k.spaceDingus.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;

import eu32k.spaceDingus.core.component.MovableComponent;

public class MovableResetSystem extends EntityProcessingSystem {

   private ComponentMapper<MovableComponent> mm;

   @SuppressWarnings("unchecked")
   public MovableResetSystem() {
      super(Aspect.getAspectForAll(MovableComponent.class));
   }

   @Override
   protected void initialize() {
      mm = world.getMapper(MovableComponent.class);
   }

   @Override
   protected void process(Entity e) {
      MovableComponent movableComponent = mm.get(e);
      movableComponent.directions = 0;
   }
}
