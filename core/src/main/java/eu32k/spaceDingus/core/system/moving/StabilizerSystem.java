package eu32k.spaceDingus.core.system.moving;

import com.badlogic.gdx.physics.box2d.Body;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.extension.component.PhysicsComponent;
import eu32k.spaceDingus.core.common.Directions;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.StabilizerComponent;

public class StabilizerSystem extends EntityProcessingSystem {

   private ComponentMapper<StabilizerComponent> sm;
   private ComponentMapper<MovableComponent> mm;
   private ComponentMapper<PhysicsComponent> pm;

   @SuppressWarnings("unchecked")
   public StabilizerSystem() {
      super(Aspect.getAspectForAll(StabilizerComponent.class, MovableComponent.class, PhysicsComponent.class));
   }

   @Override
   protected void initialize() {
      sm = world.getMapper(StabilizerComponent.class);
      mm = world.getMapper(MovableComponent.class);
      pm = world.getMapper(PhysicsComponent.class);
   }

   @Override
   protected void process(Entity e) {
      StabilizerComponent stabilizerComponent = sm.get(e);
      MovableComponent movableComponent = mm.get(e);
      Body body = pm.get(e).body;
      if (stabilizerComponent.rotation) {

         if (!Directions.compareOr(movableComponent.directions, Directions.ROTATE_LEFT) && !Directions.compareOr(movableComponent.directions, Directions.ROTATE_RIGHT)) {
            if (body.getAngularVelocity() < -0.1) {
               movableComponent.directions |= Directions.ROTATE_LEFT;
            } else if (body.getAngularVelocity() > 0.1) {
               movableComponent.directions |= Directions.ROTATE_RIGHT;
            }
         }
      }

      // if (stabilizerComponent.translation) {
      // if (!movableComponent.directions.rotateLeft && !movableComponent.directions.rotateRight) {
      // if (body.getAngularVelocity() < -0.05) {
      // movableComponent.directions.rotateLeft = true;
      // } else if (body.getAngularVelocity() > 0.05) {
      // movableComponent.directions.rotateRight = true;
      // }
      // }
      // }
   }
}
