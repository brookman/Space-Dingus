package eu32k.spaceDingus.core.system.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Actor;

import eu32k.spaceDingus.core.component.ActorComponent;
import eu32k.spaceDingus.core.component.CameraTargetComponent;

public class CameraSystem extends EntityProcessingSystem {

   private ComponentMapper<ActorComponent> am;

   private Camera camera;

   @SuppressWarnings("unchecked")
   public CameraSystem(Camera camera) {
      super(Aspect.getAspectForAll(CameraTargetComponent.class, ActorComponent.class));
      this.camera = camera;
   }

   @Override
   protected void initialize() {
      am = world.getMapper(ActorComponent.class);
   }

   @Override
   protected void process(Entity e) {
      Actor actor = am.get(e).actor;
      // Vector3 target = new Vector3(p.x, p.y, 6f);
      // camera.position.lerp(target, 0.1f);
      camera.position.x = actor.getX();
      camera.position.y = actor.getY();
      // camera.lookAt(p.x, p.y, 0);
      camera.update();
   }
}
