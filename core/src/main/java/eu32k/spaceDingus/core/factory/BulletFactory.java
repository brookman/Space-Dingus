package eu32k.spaceDingus.core.factory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.extension.ExtendedWorld;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.artemis.extension.component.PhysicsComponent;
import eu32k.gdx.artemis.extension.component.TextureRegionComponent;
import eu32k.gdx.artemis.extension.factory.Factory;
import eu32k.gdx.common.PhysicsModel;
import eu32k.gdx.common.Textures;
import eu32k.spaceDingus.core.common.GameBits;
import eu32k.spaceDingus.core.common.Directions;
import eu32k.spaceDingus.core.component.DamageComponent;
import eu32k.spaceDingus.core.component.ExpireComponent;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.weapon.RocketComponent;
import eu32k.spaceDingus.core.component.weapon.TargetPositionComponent;

public class BulletFactory extends Factory {

   private AccessoryFactory af;

   public BulletFactory(ExtendedWorld world, Stage stage, AccessoryFactory accessoryFactory) {
      super(world, stage);
      af = accessoryFactory;
   }

   private Pool<Body> bulletBodyPool = new Pool<Body>() {
      @Override
      protected Body newObject() {
         FixtureDef fd = world.fdBuilder.boxShape(0.08f, 0.03f).density(0.5f).friction(1.0f).build();
         Body body = world.bodyBuilder.type(BodyType.DynamicBody).fixture(fd).bullet().categoryBits(GameBits.PLAYER_BULLET_CATEGORY).maskBits(GameBits.PLAYER_BULLET_MASK).build();
         body.setLinearDamping(0.0f);
         body.setAngularDamping(0.0f);
         body.getFixtureList().get(0).setRestitution(0.05f);
         body.resetMassData();
         return body;
      }
   };

   public Entity createBullet(Vector2 position, Vector2 velocity, float rotation) {
      Entity e = createActorEntity(position.x, position.y, 0.17f, 0.17f, rotation, null);

      Body body = bulletBodyPool.obtain();
      body.setLinearDamping(0.0f);
      body.setAngularDamping(0.0f);
      for (Fixture fixture : body.getFixtureList()) {
         fixture.setUserData(e);
      }

      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(body, bulletBodyPool);

      e.addComponent(pc);
      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get("textures/bullet.png"))));
      e.addComponent(Pools.obtain(DamageComponent.class).init(3.0f, true, false));
      e.addComponent(Pools.obtain(ExpireComponent.class).init(1500));

      pc.activate(position, rotation, velocity);

      e.addToWorld();
      return e;
   }

   public Entity createRocket(Vector2 position, Vector2 velocity, float rotation) {
      float size = 0.4f;
      Entity e = createActorEntity(position.x, position.y, size, size, rotation, null);

      PhysicsModel rocketModel = new PhysicsModel(world.box2dWorld, e, "rockets.json", "Rocket01", 0.5f, 1.0f, 0.0f, GameBits.PLAYER_BULLET, false, size);
      rocketModel.getBody().setAngularDamping(3.0f);
      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(rocketModel.getBody());

      e.addComponent(pc);
      e.addComponent(Pools.obtain(RocketComponent.class).init());
      e.addComponent(Pools.obtain(TargetPositionComponent.class).init(0, 0));
      e.addComponent(Pools.obtain(MovableComponent.class).init(50f, 50.0f));
      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get("textures/rocket.png"))));
      e.addComponent(Pools.obtain(DamageComponent.class).init(20.0f, true, true));
      e.addComponent(Pools.obtain(ExpireComponent.class).init(5000));

      Group g = world.getMapper(ActorComponent.class).get(e).actor;

      af.createEngine(g, -0.12f, -0.04f, 90f, 0.2f, Directions.getDirections(false, false, false, false, false, true), 0.15f);
      af.createEngine(g, -0.12f, 0.04f, -90f, 0.2f, Directions.getDirections(false, false, false, false, true, false), 0.15f);
      af.createEngine(g, -0.14f, 0f, 0f, 2.0f, Directions.getDirections(true, false, false, false, false, false), 0.3f);

      pc.activate(position, rotation, velocity);

      e.addToWorld();
      return e;
   }

   public Entity createAsteroid(float x, float y, int type) {
      Entity e = createActorEntity(x, y, 1.0f, 1.0f, 0, null);

      PhysicsModel asteroidModel = new PhysicsModel(world.box2dWorld, e, "asteroid.json", "Asteroid" + type, 1.0f, 1.0f, 0.0f, GameBits.SCENERY, false, 1.0f);

      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(asteroidModel.getBody());
      pc.activate(new Vector2(x, y), MathUtils.random(MathUtils.PI2), new Vector2(0, 0));
      // pc.activate(new Vector2(x, y), 0, new Vector2(0, 0));

      e.addComponent(pc);
      e.addComponent(Pools.obtain(HealthComponent.class).init(50));
      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get(asteroidModel.getTexturePath()))));

      e.addToWorld();

      return e;
   }
}
