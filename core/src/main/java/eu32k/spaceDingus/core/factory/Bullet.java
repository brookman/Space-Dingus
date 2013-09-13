package eu32k.spaceDingus.core.factory;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.core.common.Bits;
import eu32k.spaceDingus.core.common.Directions;
import eu32k.spaceDingus.core.common.RenderLayer;
import eu32k.spaceDingus.core.component.ActorComponent;
import eu32k.spaceDingus.core.component.DamageComponent;
import eu32k.spaceDingus.core.component.ExpireComponent;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.SpriteComponent;
import eu32k.spaceDingus.core.component.weapon.TargetPositionComponent;

public class Bullet {

   private static Pool<Body> bulletBodyPool = new Pool<Body>() {
      @Override
      protected Body newObject() {
         FixtureDef fd = EntityFactory.fdBuilder.boxShape(0.08f, 0.03f).density(0.5f).friction(1.0f).build();
         Body body = EntityFactory.bodyBuilder.type(BodyType.DynamicBody).fixture(fd).bullet().categoryBits(Bits.PLAYER_BULLET_CATEGORY).maskBits(Bits.PLAYER_BULLET_MASK).build();
         body.setLinearDamping(0.0f);
         body.resetMassData();
         return body;
      }
   };

   private static Pool<Body> bulletBodyPool2 = new Pool<Body>() {
      @Override
      protected Body newObject() {
         FixtureDef fd = EntityFactory.fdBuilder.boxShape(0.08f, 0.08f).density(0.5f).friction(1.0f).build();
         Body body = EntityFactory.bodyBuilder.type(BodyType.DynamicBody).fixture(fd).bullet().categoryBits(Bits.PLAYER_BULLET_CATEGORY).maskBits(Bits.PLAYER_BULLET_MASK).build();
         body.resetMassData();
         return body;
      }
   };

   private static Pool<Body> rocketPool = new Pool<Body>() {
      @Override
      protected Body newObject() {
         FixtureDef fd = EntityFactory.fdBuilder.boxShape(0.12f, 0.05f).density(0.5f).friction(1.0f).build();
         Body body = EntityFactory.bodyBuilder.type(BodyType.DynamicBody).fixture(fd).bullet().categoryBits(Bits.PLAYER_BULLET_CATEGORY).maskBits(Bits.PLAYER_BULLET_MASK).build();
         // body.setLinearDamping(0.2f);
         body.setAngularDamping(2.0f);
         body.resetMassData();
         return body;
      }
   };

   public static Entity createBullet(Vector2 position, Vector2 velocity, float rotation) {
      Entity e = General.createActorEntity(position.x, position.y, rotation, null);

      Body body = bulletBodyPool.obtain();
      body.setUserData(e);

      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(body, bulletBodyPool);

      e.addComponent(pc);
      e.addComponent(Pools.obtain(SpriteComponent.class).init("textures/bullet.png", 0.17f, 0.17f, RenderLayer.HIGHER_COMPONENTS_1));
      e.addComponent(Pools.obtain(DamageComponent.class).init(0.3f, true));
      e.addComponent(Pools.obtain(ExpireComponent.class).init(1500));

      pc.activate(position, rotation, velocity);

      e.addToWorld();
      return e;
   }

   public static Entity createBullet2(Vector2 position, Vector2 velocity, float rotation) {
      Entity e = General.createActorEntity(position.x, position.y, rotation, null);

      Body body = bulletBodyPool2.obtain();
      body.setUserData(e);

      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(body, bulletBodyPool2);

      e.addComponent(pc);
      e.addComponent(Pools.obtain(SpriteComponent.class).init("textures/bullet2.png", 0.6f, 0.6f, RenderLayer.HIGHER_COMPONENTS_1));
      e.addComponent(Pools.obtain(DamageComponent.class).init(0.3f, true));
      e.addComponent(Pools.obtain(ExpireComponent.class).init(1000));

      pc.activate(position, rotation, velocity);

      e.addToWorld();
      return e;
   }

   public static Entity createRocket(Vector2 position, Vector2 velocity, float rotation) {
      Entity e = General.createActorEntity(position.x, position.y, rotation, null);

      Body body = rocketPool.obtain();
      body.setUserData(e);

      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(body, rocketPool);

      e.addComponent(pc);
      e.addComponent(Pools.obtain(TargetPositionComponent.class).init(0, 0));
      e.addComponent(Pools.obtain(MovableComponent.class).init(50f, 50.0f));
      e.addComponent(Pools.obtain(SpriteComponent.class).init("textures/rocket.png", 0.4f, 0.4f, RenderLayer.HIGHER_COMPONENTS_1));
      e.addComponent(Pools.obtain(DamageComponent.class).init(10.0f, true));
      e.addComponent(Pools.obtain(ExpireComponent.class).init(5000));

      Group g = (Group) EntityFactory.artemisWorld.getMapper(ActorComponent.class).get(e).actor;

      Enigne.createEngine(g, -0.17f, -0.05f, 90f, 0.2f, Directions.getDirections(false, false, false, false, false, true), 0.4f);
      Enigne.createEngine(g, -0.17f, 0.05f, -90f, 0.2f, Directions.getDirections(false, false, false, false, true, false), 0.4f);
      Enigne.createEngine(g, -0.2f, 0f, 0f, 3.0f, Directions.getDirections(true, false, false, false, false, false), 0.4f);

      pc.activate(position, rotation, velocity);

      e.addToWorld();
      return e;
   }

}
