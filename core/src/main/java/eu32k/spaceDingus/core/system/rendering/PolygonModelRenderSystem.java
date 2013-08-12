package eu32k.spaceDingus.core.system.rendering;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.lights.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.lights.Lights;
import com.badlogic.gdx.math.Vector3;

import eu32k.spaceDingus.core.component.PolygonModelComponent;
import eu32k.spaceDingus.core.component.TransformComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;

public class PolygonModelRenderSystem extends EntityProcessingSystem {

   private ComponentMapper<PolygonModelComponent> pm;
   private ComponentMapper<TransformComponent> tm;
   private ComponentMapper<EngineComponent> em;

   private Camera camera;
   private ModelBatch modelBatch;
   private Lights lights;

   @SuppressWarnings("unchecked")
   public PolygonModelRenderSystem(Camera camera) {
      super(Aspect.getAspectForAll(PolygonModelComponent.class, TransformComponent.class));
      this.camera = camera;

      lights = new Lights();
      lights.ambientLight.set(0.3f, 0.3f, 0.3f, 1f);
      lights.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0f, 0.1f, -0.5f));
   }

   @Override
   protected void initialize() {
      pm = world.getMapper(PolygonModelComponent.class);
      tm = world.getMapper(TransformComponent.class);
      em = world.getMapper(EngineComponent.class);
      modelBatch = new ModelBatch();
   }

   @Override
   protected void begin() {
      modelBatch.begin(camera);
   }

   @Override
   public void process(Entity e) {
      ModelInstance modelInstance = pm.get(e).modelInstance;
      TransformComponent pos = tm.get(e);

      modelInstance.transform.setToTranslation(pos.x, pos.y, 0);
      modelInstance.transform.rotate(new Vector3(1, 0, 0), 90);
      modelInstance.transform.rotate(new Vector3(0, 1, 0), pos.getRotation() + 180);
      // modelInstance.transform.rotate(new Vector3(1, 0, 0), 90);
      modelInstance.transform.scale(0.02f, 0.02f, 0.02f);

      modelBatch.render(modelInstance, lights);
   }

   @Override
   protected void end() {
      modelBatch.end();
   }
}
