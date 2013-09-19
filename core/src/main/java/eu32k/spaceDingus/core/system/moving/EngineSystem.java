package eu32k.spaceDingus.core.system.moving;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.artemis.extension.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;

public class EngineSystem extends EntityProcessingSystem {

   private ComponentMapper<EngineComponent> em;
   private ComponentMapper<ActorComponent> am;
   private ComponentMapper<PhysicsComponent> pm;

   private ShapeRenderer sh;

   private Camera camera;

   @SuppressWarnings("unchecked")
   public EngineSystem(Camera camera) {
      super(Aspect.getAspectForAll(EngineComponent.class, ActorComponent.class));
      sh = new ShapeRenderer();
      this.camera = camera;
   }

   @Override
   protected void initialize() {
      em = world.getMapper(EngineComponent.class);
      am = world.getMapper(ActorComponent.class);
      pm = world.getMapper(PhysicsComponent.class);
   }

   @Override
   protected void process(Entity e) {
      //
      // sh.setProjectionMatrix(camera.combined);
      //
      // EngineComponent enigne = em.get(e);
      //
      // if (!enigne.isRunning) {
      // return;
      // }
      //
      // NodeComponent node = nm.get(e);
      // TransformComponent trans = tm.get(e);
      //
      // Entity partent = node.getParent();
      // if (!pm.has(partent)) {
      // return;
      // }
      //
      // PhysicsComponent physicsComponent = pm.get(partent);
      // Body body = physicsComponent.body;
      //
      // Vector2 force = new Vector2(MathUtils.cos(trans.getRotationAsRadians()), MathUtils.sin(trans.getRotationAsRadians())).nor().scl(enigne.thrust * world.delta);
      //
      // // System.out.println("--- force before is: " + body.getAngularVelocity());
      // // System.out.println("applying: " + force.len() + " " + enigne.thrust + " " + world.delta);
      // body.applyForce(force, new Vector2(trans.x, trans.y), true);
      // // System.out.println("force after is: " + body.getAngularVelocity());
      //
      // // sh.setColor(1.0f, 0.0f, 0.0f, 1.0f);
      // // sh.begin(ShapeType.Filled);
      // // sh.circle(trans.x, trans.y, 0.05f, 10);
      // // sh.end();
      // //
      // // sh.begin(ShapeType.Line);
      // // sh.line(trans.x, trans.y, trans.x - force.x, trans.y - force.y);
      // // sh.end();

   }
}
