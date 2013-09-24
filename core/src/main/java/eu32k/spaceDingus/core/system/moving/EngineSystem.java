package eu32k.spaceDingus.core.system.moving;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.EntityProcessingSystem;
import eu32k.gdx.artemis.extension.EntityActor;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.artemis.extension.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;

public class EngineSystem extends EntityProcessingSystem {

   private ComponentMapper<EngineComponent> em;
   private ComponentMapper<ActorComponent> am;
   private ComponentMapper<PhysicsComponent> pm;

   @SuppressWarnings("unchecked")
   public EngineSystem() {
      super(Aspect.getAspectForAll(EngineComponent.class, ActorComponent.class));

   }

   @Override
   protected void initialize() {
      em = world.getMapper(EngineComponent.class);
      am = world.getMapper(ActorComponent.class);
      pm = world.getMapper(PhysicsComponent.class);
   }

   @Override
   protected void process(Entity e) {
      EngineComponent enigne = em.get(e);
      if (!enigne.isRunning) {
         return;
      }

      EntityActor actor = am.get(e).actor;

      Entity partent = ((EntityActor) actor.getParent()).getEntity();
      if (!pm.has(partent)) {
         return;
      }

      PhysicsComponent physicsComponent = pm.get(partent);
      Body body = physicsComponent.body;

      Vector2 force = new Vector2(MathUtils.cos(actor.getRotationOnStage() * MathUtils.degreesToRadians), MathUtils.sin(actor.getRotationOnStage() * MathUtils.degreesToRadians)).nor().scl(
            enigne.thrust * world.delta);

      // System.out.println("--- force before is: " + body.getAngularVelocity());
      // System.out.println("applying: " + force.len() + " " + enigne.thrust + " " + world.delta);
      body.applyForce(force, actor.getPositionOnStage(), true);
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
