package eu32k.spaceDingus.core.system.moving;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.artemis.extension.component.PhysicsComponent;
import eu32k.spaceDingus.core.common.Directions;
import eu32k.spaceDingus.core.component.MovableComponent;
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

      float currentRotation = actor.getRotation() * MathUtils.degreesToRadians;

      Vector2 target = new Vector2(targetPos.x - actor.getX(), targetPos.y - actor.getY()).nor();
      Vector2 current = new Vector2(pm.get(e).body.getLinearVelocity()).nor();
      Vector2 currentRotationVec = new Vector2(MathUtils.cos(currentRotation), MathUtils.sin(currentRotation)).nor();

      Vector2 aimTo = new Vector2(target).sub(current).nor();

      Vector2 aimToCorrected = new Vector2(target).scl(2).add(aimTo).nor();

      // DebugRenderer.begin(ShapeType.Line);
      //
      // DebugRenderer.getRenderer().setColor(1, 1, 1, 1);
      // DebugRenderer.getRenderer().line(pm.get(e).body.getPosition(), new Vector2(pm.get(e).body.getPosition()).add(current));
      //
      // DebugRenderer.getRenderer().setColor(0, 1, 0, 1);
      // DebugRenderer.getRenderer().line(pm.get(e).body.getPosition(), new Vector2(pm.get(e).body.getPosition()).add(target));
      //
      // DebugRenderer.getRenderer().setColor(1, 0, 0, 1);
      // DebugRenderer.getRenderer().line(pm.get(e).body.getPosition(), new Vector2(pm.get(e).body.getPosition()).add(aimTo));
      //
      // DebugRenderer.getRenderer().setColor(0, 0, 1, 1);
      // DebugRenderer.getRenderer().line(pm.get(e).body.getPosition(), new Vector2(pm.get(e).body.getPosition()).add(currentRotationVec));
      //
      // DebugRenderer.getRenderer().setColor(1, 0, 1, 1);
      // DebugRenderer.getRenderer().line(pm.get(e).body.getPosition(), new Vector2(pm.get(e).body.getPosition()).add(aimToCorrected));
      //
      // DebugRenderer.end();

      float targetRotation = MathUtils.atan2(aimToCorrected.y, aimToCorrected.x);

      while (targetRotation < 0) {
         targetRotation += MathUtils.PI2;
      }

      while (currentRotation < 0) {
         currentRotation += MathUtils.PI2;
      }

      while (targetRotation >= MathUtils.PI2) {
         targetRotation -= MathUtils.PI2;
      }

      while (currentRotation >= MathUtils.PI2) {
         currentRotation -= MathUtils.PI2;
      }

      // DebugRenderer.begin(ShapeType.Line);
      //
      // DebugRenderer.getRenderer().setColor(1, 1, 1, 1);
      // Vector2 direction = new Vector2(pm.get(e).body.getPosition());
      // direction.add(new Vector2(MathUtils.cos(currentRotation), MathUtils.sin(currentRotation)));
      // DebugRenderer.getRenderer().line(pm.get(e).body.getPosition(), direction);
      //
      // DebugRenderer.getRenderer().setColor(0, 1, 0, 1);
      // direction = new Vector2(pm.get(e).body.getPosition());
      // direction.add(new Vector2(MathUtils.cos(targetRotation), MathUtils.sin(targetRotation)));
      // DebugRenderer.getRenderer().line(pm.get(e).body.getPosition(), direction);
      //
      // DebugRenderer.end();

      float diff = currentRotation - targetRotation;

      if (Math.abs(diff) > MathUtils.PI) {
         diff -= Math.signum(diff) * MathUtils.PI2;
      }

      // System.out.println(currentRotation + " " + targetRotation + " " + diff);

      // currentRotation = MathUtils.atan2(pm.get(e).body.getLinearVelocity().y, pm.get(e).body.getLinearVelocity().x);
      //
      //
      // float diff = currentRotation - targetRotation;
      // if (Math.abs(diff) > MathUtils.PI) {
      // diff = diff - MathUtils.PI2;
      // }
      //
      boolean left = diff < 0 && pm.get(e).body.getAngularVelocity() > -1;
      boolean right = diff > 0 && pm.get(e).body.getAngularVelocity() < 1;
      movableComponent.directions = Directions.getDirections(Math.abs(diff) < Math.PI * 0.25f, false, false, false, left, right);
   }
}
