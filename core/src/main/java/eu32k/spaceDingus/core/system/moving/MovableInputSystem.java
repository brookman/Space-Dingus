package eu32k.spaceDingus.core.system.moving;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.spaceDingus.core.InputHandler;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.PlayerControlledMovableComponent;

public class MovableInputSystem extends EntityProcessingSystem {

   private ComponentMapper<MovableComponent> mm;

   private InputHandler handler;

   @SuppressWarnings("unchecked")
   public MovableInputSystem(InputHandler handler) {
      super(Aspect.getAspectForAll(MovableComponent.class, PlayerControlledMovableComponent.class));
      this.handler = handler;
   }

   @Override
   protected void initialize() {
      mm = world.getMapper(MovableComponent.class);
   }

   @Override
   protected void process(Entity e) {
      MovableComponent movableComponent = mm.get(e);
      movableComponent.directions = handler.directions;
   }
}
