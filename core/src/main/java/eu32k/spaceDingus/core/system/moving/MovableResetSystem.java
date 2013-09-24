package eu32k.spaceDingus.core.system.moving;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
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
      mm.get(e).directions = 0;
   }
}
