package eu32k.spaceDingus.core.system.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Camera;

import eu32k.spaceDingus.core.component.CameraTargetComponent;
import eu32k.spaceDingus.core.component.TransformComponent;

public class CameraSystem extends EntityProcessingSystem {

   private ComponentMapper<TransformComponent> tm;

   private Camera camera;

   @SuppressWarnings("unchecked")
   public CameraSystem(Camera camera) {
      super(Aspect.getAspectForAll(CameraTargetComponent.class, TransformComponent.class));
      this.camera = camera;
   }

   @Override
   protected void initialize() {
      tm = world.getMapper(TransformComponent.class);
   }

   @Override
   protected void process(Entity e) {
      TransformComponent p = tm.get(e);
      // Vector3 target = new Vector3(p.x, p.y, 6f);
      // camera.position.lerp(target, 0.1f);
      camera.position.x = p.x;
      camera.position.y = p.y;
      // camera.lookAt(p.x, p.y, 0);
      camera.update();
   }
}
