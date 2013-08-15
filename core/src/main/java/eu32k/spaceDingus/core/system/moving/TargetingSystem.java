package eu32k.spaceDingus.core.system.moving;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;

import eu32k.spaceDingus.core.common.Directions;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.TargetPositionComponent;
import eu32k.spaceDingus.core.component.TransformComponent;

public class TargetingSystem extends EntityProcessingSystem {

   private ComponentMapper<MovableComponent> mm;
   private ComponentMapper<TransformComponent> tm;
   private ComponentMapper<TargetPositionComponent> tpm;
   private ComponentMapper<PhysicsComponent> pm;

   @SuppressWarnings("unchecked")
   public TargetingSystem() {
      super(Aspect.getAspectForAll(MovableComponent.class, TransformComponent.class, TargetPositionComponent.class, PhysicsComponent.class));
   }

   @Override
   protected void initialize() {
      mm = world.getMapper(MovableComponent.class);
      tm = world.getMapper(TransformComponent.class);
      tpm = world.getMapper(TargetPositionComponent.class);
      pm = world.getMapper(PhysicsComponent.class);
   }

   @Override
   protected void process(Entity e) {
      MovableComponent movableComponent = mm.get(e);
      TransformComponent thisPos = tm.get(e);
      TargetPositionComponent targetPos = tpm.get(e);

      float targetRotation = MathUtils.atan2(targetPos.y - thisPos.y, targetPos.x - thisPos.x);
      float currentRotation = thisPos.getRotationAsRadians();
      float diff = currentRotation - targetRotation;
      if (Math.abs(diff) > MathUtils.PI) {
         diff = diff - MathUtils.PI2;
      }

      boolean left = diff < 0 && pm.get(e).body.getAngularVelocity() < 3;
      boolean right = diff > 0 && pm.get(e).body.getAngularVelocity() > -3;
      movableComponent.directions = Directions.getDirections(true, false, false, false, left, right);
   }
}
