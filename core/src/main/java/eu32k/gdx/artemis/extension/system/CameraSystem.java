package eu32k.gdx.artemis.extension.system;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.artemis.extension.component.CameraTargetComponent;

public class CameraSystem extends EntityProcessingSystem {

   private ComponentMapper<ActorComponent> am;
   private ComponentMapper<CameraTargetComponent> ctm;
   private Camera camera;

   @SuppressWarnings("unchecked")
   public CameraSystem(Camera camera) {
      super(Aspect.getAspectForAll(CameraTargetComponent.class, ActorComponent.class));
      this.camera = camera;
   }

   @Override
   protected void initialize() {
      am = world.getMapper(ActorComponent.class);
      ctm = world.getMapper(CameraTargetComponent.class);
   }

   @Override
   protected void process(Entity e) {
      Actor actor = am.get(e).actor;

      // Vector3 target = new Vector3(p.x, p.y, 6f);
      // camera.position.lerp(target, 0.1f);
      camera.position.x = actor.getX();
      camera.position.y = actor.getY();
      // camera.lookAt(p.x, p.y, 0);

      // float rotateBy = actor.getRotation() - currentRotation;
      // camera.rotate(Vector3.Z, rotateBy);
      // currentRotation = actor.getRotation();

      camera.up.set(0, 1, 0);
      if (ctm.get(e).rotateWithActor) {
         camera.up.rotate(Vector3.Z, actor.getRotation());
      }

      camera.update();

   }
}
