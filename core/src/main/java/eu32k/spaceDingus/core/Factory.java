package eu32k.spaceDingus.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.managers.GroupManager;
import eu32k.gdx.artemis.extension.EntityActor;
import eu32k.gdx.artemis.extension.ExtendedWorld;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.artemis.extension.component.CameraTargetComponent;
import eu32k.gdx.artemis.extension.component.ParticleEffectComponent;
import eu32k.gdx.artemis.extension.component.PhysicsComponent;
import eu32k.gdx.artemis.extension.component.TextureRegionComponent;
import eu32k.gdx.common.PhysicsModel;
import eu32k.gdx.common.Textures;
import eu32k.spaceDingus.core.common.Bits;
import eu32k.spaceDingus.core.common.Directions;
import eu32k.spaceDingus.core.component.DamagableComponent;
import eu32k.spaceDingus.core.component.DamageComponent;
import eu32k.spaceDingus.core.component.ExpireComponent;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.PhysicsShieldComponent;
import eu32k.spaceDingus.core.component.PlayerControlledMovableComponent;
import eu32k.spaceDingus.core.component.ShieldComponent;
import eu32k.spaceDingus.core.component.SpeedComponent;
import eu32k.spaceDingus.core.component.StabilizerComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;
import eu32k.spaceDingus.core.component.weapon.PlayerControlledWeaponComponent;
import eu32k.spaceDingus.core.component.weapon.TargetPositionComponent;
import eu32k.spaceDingus.core.component.weapon.WeaponComponent;

public class Factory {

   private ExtendedWorld world;

   public Factory(ExtendedWorld world) {
      this.world = world;
   }

   // GENERAL -------------------------------------------------

   private Entity createActorEntity(float x, float y, float width, float height, float rotation, Group parent) {
      Entity e = world.createEntity();

      // float halfWidth = width / 2.0f;
      // float halfHeight = height / 2.0f;

      EntityActor actor = new EntityActor(world, e);
      actor.setOriginX(0);
      actor.setOriginY(0);
      actor.setX(x);
      actor.setY(y);
      actor.setWidth(width);
      actor.setHeight(height);
      actor.setRotation(rotation);

      if (parent == null) {
         world.stage.addActor(actor);
      } else {
         parent.addActor(actor);
      }

      e.addComponent(Pools.obtain(ActorComponent.class).init(actor));
      return e;
   }

   // SHIP -------------------------------------------------

   private Entity createGenericShip(float x, float y) {
      Entity e = createActorEntity(x, y, 1f, 1f, 0, null);

      e.addComponent(Pools.obtain(MovableComponent.class).init(50f, 50.0f));
      e.addComponent(Pools.obtain(StabilizerComponent.class).init(true, true));
      e.addComponent(Pools.obtain(DamagableComponent.class).init());
      e.addComponent(Pools.obtain(HealthComponent.class).init(100));
      return e;
   }

   private Entity createShipType1(float x, float y, Bits bits) {
      Entity e = createGenericShip(x, y);

      PhysicsModel shipModel = new PhysicsModel(world.box2dWorld, e, "ship.json", "Ship", 2.0f, 1.0f, 0.5f, bits, false);

      FixtureDef fixtureDef = new FixtureDef();
      fixtureDef.density = 0.0f;
      fixtureDef.friction = 1.0f;
      fixtureDef.restitution = 0.5f;
      fixtureDef.filter.categoryBits = bits.category;
      fixtureDef.filter.maskBits = bits.mask;
      CircleShape shape = new CircleShape();
      shape.setRadius(0.75f);
      fixtureDef.shape = shape;

      Fixture fixture = shipModel.getBody().createFixture(fixtureDef);

      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(shipModel.getBody());
      pc.activate(new Vector2(x, y), 0, new Vector2(0, 0));

      e.addComponent(pc);
      e.addComponent(Pools.obtain(StabilizerComponent.class).init(true, true));
      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get("models/ship.png"))));

      Group g = world.getMapper(ActorComponent.class).get(e).actor;

      createEngine(g, -0.45f, -0.31f, 0.0f, 50.0f, Directions.getDirections(Directions.TRANSLATE_FORWARD, Directions.ROTATE_LEFT), 0.5f);
      createEngine(g, -0.45f, 0.31f, 0.0f, 50.0f, Directions.getDirections(Directions.TRANSLATE_FORWARD, Directions.ROTATE_RIGHT), 0.5f);
      createEngine(g, 0.45f, -0.31f, 180.0f, 50.0f, Directions.getDirections(Directions.TRANSLATE_BACKWARD, Directions.ROTATE_RIGHT), 0.5f);
      createEngine(g, 0.45f, 0.31f, 180.0f, 50.0f, Directions.getDirections(Directions.TRANSLATE_BACKWARD, Directions.ROTATE_LEFT), 0.5f);

      createEngine(g, -0.25f, -0.45f, 90.0f, 50.0f, Directions.getDirections(false, false, true, false, false, true), 0.5f);
      createEngine(g, 0.25f, -0.45f, 90.0f, 50.0f, Directions.getDirections(false, false, true, false, true, false), 0.5f);
      createEngine(g, -0.25f, 0.45f, 270.0f, 50.0f, Directions.getDirections(false, false, false, true, true, false), 0.5f);
      createEngine(g, 0.25f, 0.45f, 270.0f, 50.0f, Directions.getDirections(false, false, false, true, false, true), 0.5f);
      createShield(g, bits, fixture, 100.0f);

      return e;
   }

   public Entity createEnemy(float x, float y) {
      Entity e = createShipType1(x, y, Bits.ENEMY);
      world.getManager(GroupManager.class).add(e, "ENEMY");

      Group g = world.getMapper(ActorComponent.class).get(e).actor;
      createWeapon(g, -0.25f, 0.31f);
      createWeapon(g, -0.25f, -0.31f);
      e.addToWorld();
      return e;
   }

   public Entity createPlayerShip(float x, float y) {
      Entity e = createShipType1(x, y, Bits.PLAYER);

      e.addComponent(new PlayerControlledMovableComponent());

      // ObjLoader loader = new ObjLoader();
      // Model model =
      // loader.loadModel(Gdx.files.internal("3dmodels/dark_fighter_6.object"),
      // new TextureProvider() {
      // @Override
      // public Texture load(String fileName) {
      // return Textures.get(fileName);
      // }
      // });
      // e.addComponent(Pools.obtain(PolygonModelComponent.class).init(model));

      e.addComponent(Pools.obtain(CameraTargetComponent.class));

      Group g = world.getMapper(ActorComponent.class).get(e).actor;

      createWeapon(g, 0.25f, 0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));
      createWeapon(g, 0.25f, -0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));
      createWeapon(g, -0.25f, 0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));
      createWeapon(g, -0.25f, -0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));

      e.addToWorld();

      return e;
   }

   // BULLET -------------------------------------------------

   private Pool<Body> bulletBodyPool = new Pool<Body>() {
      @Override
      protected Body newObject() {
         FixtureDef fd = world.fdBuilder.boxShape(0.08f, 0.03f).density(0.5f).friction(1.0f).build();
         Body body = world.bodyBuilder.type(BodyType.DynamicBody).fixture(fd).bullet().categoryBits(Bits.PLAYER_BULLET_CATEGORY).maskBits(Bits.PLAYER_BULLET_MASK).build();
         body.setLinearDamping(0.0f);
         body.resetMassData();
         return body;
      }
   };

   private Pool<Body> bulletBodyPool2 = new Pool<Body>() {
      @Override
      protected Body newObject() {
         FixtureDef fd = world.fdBuilder.boxShape(0.08f, 0.08f).density(0.5f).friction(1.0f).build();
         Body body = world.bodyBuilder.type(BodyType.DynamicBody).fixture(fd).bullet().categoryBits(Bits.PLAYER_BULLET_CATEGORY).maskBits(Bits.PLAYER_BULLET_MASK).build();
         body.resetMassData();
         return body;
      }
   };

   private Pool<Body> rocketPool = new Pool<Body>() {
      @Override
      protected Body newObject() {
         FixtureDef fd = world.fdBuilder.boxShape(0.12f, 0.05f).density(0.5f).friction(1.0f).build();
         Body body = world.bodyBuilder.type(BodyType.DynamicBody).fixture(fd).bullet().categoryBits(Bits.PLAYER_BULLET_CATEGORY).maskBits(Bits.PLAYER_BULLET_MASK).build();
         // body.setLinearDamping(0.2f);
         body.setAngularDamping(2.0f);
         body.resetMassData();
         return body;
      }
   };

   public Entity createBullet(Vector2 position, Vector2 velocity, float rotation) {
      Entity e = createActorEntity(position.x, position.y, 0.17f, 0.17f, rotation, null);

      Body body = bulletBodyPool.obtain();
      body.setUserData(e);

      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(body, bulletBodyPool);

      e.addComponent(pc);
      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get("textures/bullet.png"))));
      e.addComponent(Pools.obtain(DamageComponent.class).init(0.3f, true));
      e.addComponent(Pools.obtain(ExpireComponent.class).init(1500));

      pc.activate(position, rotation, velocity);

      e.addToWorld();
      return e;
   }

   public Entity createBullet2(Vector2 position, Vector2 velocity, float rotation) {
      Entity e = createActorEntity(position.x, position.y, 0.6f, 0.6f, rotation, null);

      Body body = bulletBodyPool2.obtain();
      body.setUserData(e);

      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(body, bulletBodyPool2);

      e.addComponent(pc);
      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get("textures/bullet2.png"))));
      e.addComponent(Pools.obtain(DamageComponent.class).init(0.3f, true));
      e.addComponent(Pools.obtain(ExpireComponent.class).init(1000));

      pc.activate(position, rotation, velocity);

      e.addToWorld();
      return e;
   }

   public Entity createRocket(Vector2 position, Vector2 velocity, float rotation) {
      Entity e = createActorEntity(position.x, position.y, 0.4f, 0.4f, rotation, null);

      Body body = rocketPool.obtain();
      body.setUserData(e);

      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(body, rocketPool);

      e.addComponent(pc);
      e.addComponent(Pools.obtain(TargetPositionComponent.class).init(0, 0));
      e.addComponent(Pools.obtain(MovableComponent.class).init(50f, 50.0f));
      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get("textures/rocket.png"))));
      e.addComponent(Pools.obtain(DamageComponent.class).init(10.0f, true));
      e.addComponent(Pools.obtain(ExpireComponent.class).init(5000));

      Group g = world.getMapper(ActorComponent.class).get(e).actor;

      createEngine(g, -0.17f, -0.05f, 90f, 0.2f, Directions.getDirections(false, false, false, false, false, true), 0.4f);
      createEngine(g, -0.17f, 0.05f, -90f, 0.2f, Directions.getDirections(false, false, false, false, true, false), 0.4f);
      createEngine(g, -0.2f, 0f, 0f, 3.0f, Directions.getDirections(true, false, false, false, false, false), 0.4f);

      pc.activate(position, rotation, velocity);

      e.addToWorld();
      return e;
   }

   // ENGINE -------------------------------------------------

   public Entity createEngine(Group parent, float x, float y, float rotation, float thrust, int directions, float size) {
      Entity e = createActorEntity(x, y, size, size, rotation, parent);

      e.addComponent(Pools.obtain(EngineComponent.class).init(thrust, directions));
      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get("textures/engine1.png"))));

      e.addToWorld();

      return e;
   }

   // WEAPON -------------------------------------------------

   public Entity createWeapon(Group parent, float x, float y) {
      Entity e = createActorEntity(x, y, 0.2f, 0.2f, 0, parent);

      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get("textures/gun.png"))));
      e.addComponent(Pools.obtain(WeaponComponent.class).init(MathUtils.random(500, 1500)));
      e.addComponent(Pools.obtain(SpeedComponent.class).init(7.0f));

      e.addToWorld();

      return e;
   }

   // MISC -------------------------------------------------

   public Entity createShield(Group parent, Bits bits, Fixture fixture, float shield) {
      Entity e = createActorEntity(0f, 0f, 1.7f, 1.7f, 0, parent);

      e.addComponent(Pools.obtain(ShieldComponent.class).init(shield));
      e.addComponent(Pools.obtain(PhysicsShieldComponent.class).init(bits, fixture));
      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get("textures/shield.png"))));

      e.addToWorld();
      return e;
   }

   public Entity createAsteroid(float x, float y) {
      Entity e = createActorEntity(x, y, 1.0f, 1.0f, 0, null);
      PhysicsModel asteroidModel = new PhysicsModel(world.box2dWorld, e, "asteroid.json", "Asteroid", 1.0f, 1.0f, 0.3f, Bits.SCENERY, false);

      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(asteroidModel.getBody());
      pc.activate(new Vector2(x, y), MathUtils.random(MathUtils.PI2), new Vector2(0, 0));
      // pc.activate(new Vector2(x, y), 0, new Vector2(0, 0));

      e.addComponent(pc);
      e.addComponent(Pools.obtain(DamagableComponent.class).init());
      e.addComponent(Pools.obtain(HealthComponent.class).init(50));
      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get(asteroidModel.getTexturePath()))));

      e.addToWorld();

      return e;
   }

   // public static Entity createExplosion(float x, float y) {
   // Entity e = General.createActorEntity(x, y, 0, null);
   // Entity e = General.createActorEntity(x, y, 0.2f, 0.2f, 0, null, new TextureRegion(Textures.get("textures/gun.png")));
   //
   // ParticleEffect effect = new ParticleEffect();
   // effect.load(Gdx.files.internal("particles/explosion2.txt"), Gdx.files.internal("textures"));
   // effect.start();
   //
   // e.addComponent(Pools.obtain(ParticleEffectComponent.class).init(effect));
   // e.addComponent(Pools.obtain(ExpireComponent.class).init(1000));
   // e.addToWorld();
   // return e;
   // }
   //
   public Entity createMuzzleFlash(float x, float y, float rotation, Group parent) {
      Entity e = createActorEntity(x, y, 1.0f, 1.0f, rotation, parent);

      ParticleEffect effect = new ParticleEffect();
      effect.load(Gdx.files.internal("particles/muzzleFlash1.txt"), Gdx.files.internal("textures"));
      effect.start();

      e.addComponent(Pools.obtain(ParticleEffectComponent.class).init(effect));
      e.addComponent(Pools.obtain(ExpireComponent.class).init(500));
      e.addToWorld();
      return e;
   }

}
