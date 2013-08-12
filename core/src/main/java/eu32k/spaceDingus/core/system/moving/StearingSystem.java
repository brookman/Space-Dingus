package eu32k.spaceDingus.core.system.moving;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.Bag;

import eu32k.spaceDingus.core.common.Directions;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;
import eu32k.spaceDingus.core.sceneGraph.component.NodeComponent;

public class StearingSystem extends EntityProcessingSystem {

   private ComponentMapper<MovableComponent> mm;
   private ComponentMapper<NodeComponent> nm;
   private ComponentMapper<EngineComponent> em;

   @SuppressWarnings("unchecked")
   public StearingSystem() {
      super(Aspect.getAspectForAll(MovableComponent.class, NodeComponent.class));
   }

   @Override
   protected void initialize() {
      mm = world.getMapper(MovableComponent.class);
      nm = world.getMapper(NodeComponent.class);
      em = world.getMapper(EngineComponent.class);
   }

   @Override
   protected void process(Entity e) {
      MovableComponent movableComponent = mm.get(e);
      NodeComponent nodeComponent = nm.get(e);

      Bag<Entity> children = nodeComponent.getChildren();
      for (int i = 0; i < children.size(); i++) {
         Entity child = children.get(i);
         if (em.has(child)) {
            EngineComponent engine = em.get(child);
            engine.isRunning = Directions.compareOr(engine.directions, movableComponent.directions);
         }
      }
   }
}
