package eu32k.spaceDingus.core.system;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.systems.VoidEntitySystem;
import eu32k.spaceDingus.core.component.DamageComponent;
import eu32k.spaceDingus.core.component.HealthComponent;

public class CollisionDamageSystem extends VoidEntitySystem implements ContactListener {

   private ComponentMapper<DamageComponent> dm;
   private ComponentMapper<HealthComponent> hm;

   private World box2dWorld;

   public CollisionDamageSystem(World box2dWorld) {
      this.box2dWorld = box2dWorld;
   }

   @Override
   protected void initialize() {
      dm = world.getMapper(DamageComponent.class);
      hm = world.getMapper(HealthComponent.class);
      box2dWorld.setContactListener(this);
   }

   @Override
   protected void processSystem() {
      // NOP
   }

   @Override
   public void beginContact(Contact contact) {
      if (!contact.isTouching()) {
         return;
      }

      Body bodyA = contact.getFixtureA().getBody();
      Body bodyB = contact.getFixtureB().getBody();
      Entity entityA = (Entity) contact.getFixtureA().getUserData();
      Entity entityB = (Entity) contact.getFixtureB().getUserData();

      // Vector2 resulting = new Vector2(bodyA.getLinearVelocity()).sub(bodyB.getLinearVelocity());
      // float speed = resulting.len();

      if (dm.has(entityA) && hm.has(entityB)) {
         hm.get(entityB).health -= dm.get(entityA).damage;
         hm.get(entityB).health = Math.max(hm.get(entityB).health, 0);
         if (dm.get(entityA).nonrecurring) {
            // bodyA.setLinearDamping(1.0f);
            // bodyA.setAngularDamping(1.0f);
            dm.get(entityA).damage = 0;
         }
      }
      if (dm.has(entityB) && hm.has(entityA)) {
         hm.get(entityA).health -= dm.get(entityB).damage;
         hm.get(entityA).health = Math.max(hm.get(entityA).health, 0);
         if (dm.get(entityB).nonrecurring) {
            // bodyB.setLinearDamping(1.0f);
            // bodyB.setAngularDamping(1.0f);
            dm.get(entityB).damage = 0;
         }
      }
   }

   @Override
   public void endContact(Contact contact) {
      // NOP
   }

   @Override
   public void preSolve(Contact contact, Manifold oldManifold) {
      // NOP
   }

   @Override
   public void postSolve(Contact contact, ContactImpulse impulse) {
      // NOP
   }
}
