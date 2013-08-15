package eu32k.spaceDingus.core.system.moving;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.TransformComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;
import eu32k.spaceDingus.core.sceneGraph.component.NodeComponent;

public class EngineSystem extends EntityProcessingSystem {

   private ComponentMapper<EngineComponent> em;
   private ComponentMapper<NodeComponent> nm;
   private ComponentMapper<TransformComponent> tm;
   private ComponentMapper<PhysicsComponent> pm;

   private ShapeRenderer sh;

   private Camera camera;

   @SuppressWarnings("unchecked")
   public EngineSystem(Camera camera) {
      super(Aspect.getAspectForAll(EngineComponent.class, NodeComponent.class, TransformComponent.class));
      sh = new ShapeRenderer();
      this.camera = camera;
   }

   @Override
   protected void initialize() {
      em = world.getMapper(EngineComponent.class);
      nm = world.getMapper(NodeComponent.class);
      tm = world.getMapper(TransformComponent.class);
      pm = world.getMapper(PhysicsComponent.class);
   }

   @Override
   protected void process(Entity e) {

      sh.setProjectionMatrix(camera.combined);

      EngineComponent enigne = em.get(e);

      if (!enigne.isRunning) {
         return;
      }

      NodeComponent node = nm.get(e);
      TransformComponent trans = tm.get(e);

      Entity partent = node.getParent();
      if (!pm.has(partent)) {
         return;
      }

      PhysicsComponent physicsComponent = pm.get(partent);
      Body body = physicsComponent.body;

      Vector2 force = new Vector2(MathUtils.cos(trans.getRotationAsRadians()), MathUtils.sin(trans.getRotationAsRadians())).nor().scl(enigne.thrust * world.delta);

      // System.out.println("--- force before is: " + body.getAngularVelocity());
      // System.out.println("applying: " + force.len() + " " + enigne.thrust + " " + world.delta);
      body.applyForce(force, new Vector2(trans.x, trans.y), true);
      // System.out.println("force after is: " + body.getAngularVelocity());

      // sh.setColor(1.0f, 0.0f, 0.0f, 1.0f);
      // sh.begin(ShapeType.Filled);
      // sh.circle(trans.x, trans.y, 0.05f, 10);
      // sh.end();
      //
      // sh.begin(ShapeType.Line);
      // sh.line(trans.x, trans.y, trans.x - force.x, trans.y - force.y);
      // sh.end();

   }
}
