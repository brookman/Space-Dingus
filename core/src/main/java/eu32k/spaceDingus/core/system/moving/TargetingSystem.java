package eu32k.spaceDingus.core.system.moving;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

import eu32k.spaceDingus.core.common.Directions;
import eu32k.spaceDingus.core.component.ActorComponent;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.weapon.TargetPositionComponent;

public class TargetingSystem extends EntityProcessingSystem {

   private ComponentMapper<MovableComponent> mm;
   private ComponentMapper<ActorComponent> am;
   private ComponentMapper<TargetPositionComponent> tpm;
   private ComponentMapper<PhysicsComponent> pm;

   @SuppressWarnings("unchecked")
   public TargetingSystem() {
      super(Aspect.getAspectForAll(MovableComponent.class, ActorComponent.class, TargetPositionComponent.class, PhysicsComponent.class));
   }

   @Override
   protected void initialize() {
      mm = world.getMapper(MovableComponent.class);
      am = world.getMapper(ActorComponent.class);
      tpm = world.getMapper(TargetPositionComponent.class);
      pm = world.getMapper(PhysicsComponent.class);
   }

   @Override
   protected void process(Entity e) {
      MovableComponent movableComponent = mm.get(e);
      Actor actor = am.get(e).actor;
      TargetPositionComponent targetPos = tpm.get(e);

      if (!targetPos.enabled) {
         movableComponent.directions = Directions.getDirections(true, false, false, false, false, false);
         return;
      }

      float targetRotation = MathUtils.atan2(targetPos.y - actor.getY(), targetPos.x - actor.getX());
      float currentRotation = actor.getRotation() * MathUtils.degreesToRadians;
      float diff = currentRotation - targetRotation;
      if (Math.abs(diff) > MathUtils.PI) {
         diff = diff - MathUtils.PI2;
      }

      boolean left = diff < 0 && pm.get(e).body.getAngularVelocity() < 3;
      boolean right = diff > 0 && pm.get(e).body.getAngularVelocity() > -3;
      movableComponent.directions = Directions.getDirections(true, false, false, false, left, right);
   }
}
