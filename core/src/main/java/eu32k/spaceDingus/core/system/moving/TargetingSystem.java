package eu32k.spaceDingus.core.system.moving;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

import eu32k.spaceDingus.core.common.Directions;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.TargetPositionComponent;
import eu32k.spaceDingus.core.component.TransformComponent;

public class TargetingSystem extends EntityProcessingSystem {

   private ComponentMapper<MovableComponent> mm;
   private ComponentMapper<TransformComponent> tm;
   private ComponentMapper<TargetPositionComponent> tpm;

   private Camera camera;
   private ShapeRenderer sh;

   @SuppressWarnings("unchecked")
   public TargetingSystem(Camera camera) {
      super(Aspect.getAspectForAll(MovableComponent.class, TransformComponent.class, TargetPositionComponent.class));
      this.camera = camera;
      sh = new ShapeRenderer();
   }

   @Override
   protected void initialize() {
      mm = world.getMapper(MovableComponent.class);
      tm = world.getMapper(TransformComponent.class);
      tpm = world.getMapper(TargetPositionComponent.class);
   }

   @Override
   protected void process(Entity e) {
      MovableComponent movableComponent = mm.get(e);
      TransformComponent thisPos = tm.get(e);
      TargetPositionComponent targetPos = tpm.get(e);

      sh.setProjectionMatrix(camera.combined);
      sh.setColor(1.0f, 0.0f, 1.0f, 1.0f);
      sh.begin(ShapeType.Line);
      sh.line(thisPos.x, thisPos.y, targetPos.x, targetPos.y);
      sh.end();

      float targetRotation = MathUtils.atan2(Math.abs(targetPos.y - thisPos.y), Math.abs(targetPos.x - thisPos.x));

      sh.setColor(0.0f, 1.0f, 0.0f, 1.0f);
      sh.begin(ShapeType.Line);
      sh.line(thisPos.x, thisPos.y, thisPos.x + MathUtils.cos(targetRotation) * 0.5f, thisPos.y + MathUtils.sin(targetRotation) * 0.5f);
      sh.end();

      float currentRotation = thisPos.getRotationAsRadians();

      sh.setColor(0.0f, 0.0f, 1.0f, 1.0f);
      sh.begin(ShapeType.Line);
      sh.line(thisPos.x, thisPos.y, thisPos.x + MathUtils.cos(currentRotation) * 0.5f, thisPos.y + MathUtils.sin(currentRotation) * 0.5f);
      sh.end();

      float diff = targetRotation - currentRotation;

      diff -= MathUtils.PI;

      // System.out.println(diff);

      if (diff > 0) {
         movableComponent.directions = Directions.getDirections(false, false, false, false, false, true);
      } else if (diff < 0) {
         movableComponent.directions = Directions.getDirections(false, false, false, false, true, false);
      }

   }
}
