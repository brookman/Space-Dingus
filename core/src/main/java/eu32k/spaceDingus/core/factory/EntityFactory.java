package eu32k.spaceDingus.core.factory;

import com.artemis.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.common.Bits;
import eu32k.spaceDingus.common.BodyBuilder;
import eu32k.spaceDingus.common.Directions;
import eu32k.spaceDingus.common.FixtureDefBuilder;
import eu32k.spaceDingus.common.PhysicsModel;
import eu32k.spaceDingus.common.RenderLayer;
import eu32k.spaceDingus.core.component.CameraTargetComponent;
import eu32k.spaceDingus.core.component.DamagableComponent;
import eu32k.spaceDingus.core.component.DamageComponent;
import eu32k.spaceDingus.core.component.ExpireComponent;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.PhysicsShieldComponent;
import eu32k.spaceDingus.core.component.PlayerControlledMovableComponent;
import eu32k.spaceDingus.core.component.ShieldComponent;
import eu32k.spaceDingus.core.component.SpeedComponent;
import eu32k.spaceDingus.core.component.SpriteComponent;
import eu32k.spaceDingus.core.component.StabilizerComponent;
import eu32k.spaceDingus.core.component.TransformComponent;
import eu32k.spaceDingus.core.component.engine.EngineComponent;
import eu32k.spaceDingus.core.component.ship.ShipComponent;
import eu32k.spaceDingus.core.component.weapon.PlayerControlledWeaponComponent;
import eu32k.spaceDingus.core.component.weapon.WeaponComponent;
import eu32k.spaceDingus.core.sceneGraph.component.LocalTransformComponent;
import eu32k.spaceDingus.core.sceneGraph.component.NodeComponent;

public class EntityFactory {

   private static com.artemis.World artemisWorld;

   private static FixtureDefBuilder fdBuilder = new FixtureDefBuilder();
   private static BodyBuilder bodyBuilder;
   private static World box2dWorld;
   private static AssetManager assets;

   public static void init(com.artemis.World artemisWorld, World box2dWorld, AssetManager assets) {
      EntityFactory.artemisWorld = artemisWorld;
      EntityFactory.box2dWorld = box2dWorld;
      EntityFactory.assets = assets;
      bodyBuilder = new BodyBuilder(box2dWorld);
   }

   private static Entity createGenericShip() {
      Entity e = artemisWorld.createEntity();
      e.addComponent(Pools.obtain(ShipComponent.class));
      e.addComponent(Pools.obtain(MovableComponent.class).init(50f, 50.0f));
      e.addComponent(Pools.obtain(NodeComponent.class).init());
      e.addComponent(Pools.obtain(StabilizerComponent.class).init(true, true));
      e.addComponent(Pools.obtain(TransformComponent.class).init());
      e.addComponent(Pools.obtain(DamagableComponent.class).init());
      e.addComponent(Pools.obtain(HealthComponent.class).init(100));
      e.addComponent(Pools.obtain(ShieldComponent.class).init(100));
      return e;
   }

   private static Entity createShipType1(float x, float y, Bits bits) {
      Entity e = createGenericShip();

      PhysicsModel shipModel = new PhysicsModel(box2dWorld, e, "ship.json", "Ship", 2.0f, 1.0f, 0.5f, bits, false);

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
      e.addComponent(Pools.obtain(SpriteComponent.class).init(shipModel.getTexturePath(), 1.0f, 1.0f, RenderLayer.ACTORS));
      e.addComponent(Pools.obtain(StabilizerComponent.class).init(true, true));

      createEngine(e, -0.45f, -0.31f, 0.0f, Directions.getDirections(true, false, false, false, true, false));
      createEngine(e, -0.45f, 0.31f, 0.0f, Directions.getDirections(true, false, false, false, false, true));
      createEngine(e, 0.45f, -0.31f, 180.0f, Directions.getDirections(false, true, false, false, false, true));
      createEngine(e, 0.45f, 0.31f, 180.0f, Directions.getDirections(false, true, false, false, true, false));

      createEngine(e, -0.25f, -0.45f, 90.0f, Directions.getDirections(false, false, true, false, false, true));
      createEngine(e, 0.25f, -0.45f, 90.0f, Directions.getDirections(false, false, true, false, true, false));
      createEngine(e, -0.25f, 0.45f, 270.0f, Directions.getDirections(false, false, false, true, true, false));
      createEngine(e, 0.25f, 0.45f, 270.0f, Directions.getDirections(false, false, false, true, false, true));
      createShield(e, bits, fixture, e.getComponent(ShieldComponent.class));

      return e;
   }

   public static Entity createEnemy(float x, float y) {
      Entity e = createShipType1(x, y, Bits.ENEMY);
      createWeapon(e, -0.25f, 0.31f);
      createWeapon(e, -0.25f, -0.31f);
      e.addToWorld();
      return e;
   }

   public static Entity createPlayerShip(float x, float y) {
      Entity e = createShipType1(x, y, Bits.PLAYER);

      e.addComponent(new PlayerControlledMovableComponent());

      // ObjLoader loader = new ObjLoader();
      // Model model = loader.loadModel(Gdx.files.internal("3dmodels/dark_fighter_6.object"), new TextureProvider() {
      // @Override
      // public Texture load(String fileName) {
      // return Textures.get(fileName);
      // }
      // });
      // e.addComponent(Pools.obtain(PolygonModelComponent.class).init(model));

      e.addComponent(Pools.obtain(CameraTargetComponent.class));

      createWeapon(e, 0.25f, 0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));
      createWeapon(e, 0.25f, -0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));
      // createWeapon(e, -0.25f, 0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));
      // createWeapon(e, -0.25f, -0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));

      e.addToWorld();

      return e;
   }

   public static Entity createAsteroid(float x, float y) {
      Entity e = artemisWorld.createEntity();

      PhysicsModel asteroidModel = new PhysicsModel(box2dWorld, e, "asteroid.json", "Asteroid", 1.0f, 1.0f, 0.3f, Bits.SCENERY, false);

      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(asteroidModel.getBody());
      pc.activate(new Vector2(x, y), MathUtils.random(MathUtils.PI2), new Vector2(0, 0));

      e.addComponent(pc);
      e.addComponent(Pools.obtain(TransformComponent.class).init());
      e.addComponent(Pools.obtain(SpriteComponent.class).init(asteroidModel.getTexturePath(), 1.0f, 1.0f, RenderLayer.ACTORS));
      e.addComponent(Pools.obtain(DamagableComponent.class).init());
      e.addComponent(Pools.obtain(HealthComponent.class).init(50));

      e.addToWorld();

      return e;
   }

   public static Entity createWeapon(Entity parent, float x, float y) {
      Entity e = artemisWorld.createEntity();

      e.addComponent(Pools.obtain(TransformComponent.class).init());
      e.addComponent(Pools.obtain(LocalTransformComponent.class).init(x, y, 0));
      e.addComponent(Pools.obtain(NodeComponent.class).init(parent));
      e.addComponent(Pools.obtain(SpriteComponent.class).init("textures/gun.png", 0.2f, 0.2f, RenderLayer.HIGHER_COMPONENTS_1));
      e.addComponent(Pools.obtain(WeaponComponent.class).init(MathUtils.random(100, 200)));
      e.addComponent(Pools.obtain(SpeedComponent.class).init(7.0f));

      e.addToWorld();

      return e;
   }

   public static Entity createEngine(Entity parent, float x, float y, float rotation, int directions) {
      Entity e = artemisWorld.createEntity();

      e.addComponent(Pools.obtain(TransformComponent.class));
      e.addComponent(Pools.obtain(LocalTransformComponent.class).init(x, y, rotation));
      e.addComponent(Pools.obtain(NodeComponent.class).init(parent));
      e.addComponent(Pools.obtain(SpriteComponent.class).init("textures/engine1.png", 0.5f, 0.5f, RenderLayer.LOWER_COMPONENTS_2));
      e.addComponent(Pools.obtain(EngineComponent.class).init(50.0f, directions));

      e.addToWorld();

      return e;
   }

   private static Pool<Body> bulletBodyPool = new Pool<Body>() {
      @Override
      protected Body newObject() {
         FixtureDef fd = fdBuilder.boxShape(0.08f, 0.03f).density(0.5f).friction(1.0f).build();
         Body body = bodyBuilder.type(BodyType.DynamicBody).fixture(fd).bullet().categoryBits(Bits.PLAYER_BULLET_CATEGORY).maskBits(Bits.PLAYER_BULLET_MASK).build();
         body.resetMassData();
         return body;
      }
   };

   public static Entity createBullet(Vector2 position, Vector2 velocity, float rotation) {
      Entity e = artemisWorld.createEntity();

      Body body = bulletBodyPool.obtain();
      body.setUserData(e);

      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(body, bulletBodyPool);

      e.addComponent(pc);
      e.addComponent(Pools.obtain(TransformComponent.class));
      e.addComponent(Pools.obtain(SpriteComponent.class).init("textures/bullet2.png", 0.17f, 0.17f, RenderLayer.HIGHER_COMPONENTS_1));
      e.addComponent(Pools.obtain(DamageComponent.class).init(0.3f, true));
      e.addComponent(Pools.obtain(ExpireComponent.class).init(1000));

      pc.activate(position, rotation, velocity);

      e.addToWorld();
      return e;
   }

   public static Entity createShield(Entity parent, Bits bits, Fixture fixture, ShieldComponent shield) {
      Entity e = artemisWorld.createEntity();

      e.addComponent(shield);
      e.addComponent(Pools.obtain(TransformComponent.class).init());
      e.addComponent(Pools.obtain(LocalTransformComponent.class).init());
      e.addComponent(Pools.obtain(NodeComponent.class).init(parent));
      e.addComponent(Pools.obtain(PhysicsShieldComponent.class).init(bits, fixture));
      e.addComponent(Pools.obtain(SpriteComponent.class).init("textures/shield.png", 1.72f, 1.72f, RenderLayer.HIGHER_COMPONENTS_2));

      e.addToWorld();
      return e;
   }

   public static Entity createBackground() {
      Entity e = artemisWorld.createEntity();
      e.addComponent(Pools.obtain(TransformComponent.class));
      SpriteComponent s = Pools.obtain(SpriteComponent.class).init("textures/bullet2.png", 1.0f, 1.0f, RenderLayer.BACKGROUND);
      e.addComponent(s);
      e.addToWorld();
      return e;
   }
}
