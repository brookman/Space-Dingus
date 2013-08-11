package eu32k.spaceDingus.core.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.Bag;
import com.badlogic.gdx.physics.box2d.Body;

import eu32k.spaceDingus.core.common.Directions;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;
import eu32k.spaceDingus.core.component.ship.ShipComponent;
import eu32k.spaceDingus.core.sceneGraph.component.NodeComponent;

public class ShipSystem extends EntityProcessingSystem {

   private ComponentMapper<MovableComponent> mm;
   private ComponentMapper<PhysicsComponent> pm;
   private ComponentMapper<NodeComponent> nm;
   private ComponentMapper<EngineComponent> em;

   @SuppressWarnings("unchecked")
   public ShipSystem() {
      super(Aspect.getAspectForAll(ShipComponent.class, MovableComponent.class, PhysicsComponent.class, NodeComponent.class));
   }

   @Override
   protected void initialize() {
      mm = world.getMapper(MovableComponent.class);
      pm = world.getMapper(PhysicsComponent.class);
      nm = world.getMapper(NodeComponent.class);
      em = world.getMapper(EngineComponent.class);
   }

   @Override
   protected void process(Entity e) {

      MovableComponent movableComponent = mm.get(e);
      PhysicsComponent physicsComponent = pm.get(e);
      Body body = physicsComponent.body;
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
