package eu32k.spaceDingus.core.system.moving;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;

public class StearingSystem extends EntityProcessingSystem {

   private ComponentMapper<MovableComponent> mm;
   private ComponentMapper<EngineComponent> em;

   @SuppressWarnings("unchecked")
   public StearingSystem() {
      super(Aspect.getAspectForAll(MovableComponent.class));
   }

   @Override
   protected void initialize() {
      mm = world.getMapper(MovableComponent.class);
      em = world.getMapper(EngineComponent.class);
   }

   @Override
   protected void process(Entity e) {
      // MovableComponent movableComponent = mm.get(e);
      // NodeComponent nodeComponent = nm.get(e);
      //
      // Bag<Entity> children = nodeComponent.getChildren();
      // for (int i = 0; i < children.size(); i++) {
      // Entity child = children.get(i);
      // if (em.has(child)) {
      // EngineComponent engine = em.get(child);
      // engine.isRunning = Directions.compareOr(engine.directions, movableComponent.directions);
      // }
      // }
   }
}
