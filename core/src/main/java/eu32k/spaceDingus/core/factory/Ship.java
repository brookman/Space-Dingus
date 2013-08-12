package eu32k.spaceDingus.core.factory;

import com.artemis.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.core.common.Bits;
import eu32k.spaceDingus.core.common.Directions;
import eu32k.spaceDingus.core.common.PhysicsModel;
import eu32k.spaceDingus.core.common.RenderLayer;
import eu32k.spaceDingus.core.component.CameraTargetComponent;
import eu32k.spaceDingus.core.component.DamagableComponent;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.PlayerControlledMovableComponent;
import eu32k.spaceDingus.core.component.ShieldComponent;
import eu32k.spaceDingus.core.component.SpriteComponent;
import eu32k.spaceDingus.core.component.StabilizerComponent;
import eu32k.spaceDingus.core.component.TransformComponent;
import eu32k.spaceDingus.core.component.weapon.PlayerControlledWeaponComponent;
import eu32k.spaceDingus.core.sceneGraph.component.NodeComponent;

public class Ship {

   private static Entity createGenericShip() {
      Entity e = EntityFactory.artemisWorld.createEntity();
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

      PhysicsModel shipModel = new PhysicsModel(EntityFactory.box2dWorld, e, "ship.json", "Ship", 2.0f, 1.0f, 0.5f, bits, false);

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

      Enigne.createEngine(e, -0.45f, -0.31f, 0.0f, Directions.getDirections(true, false, false, false, true, false));
      Enigne.createEngine(e, -0.45f, 0.31f, 0.0f, Directions.getDirections(true, false, false, false, false, true));
      Enigne.createEngine(e, 0.45f, -0.31f, 180.0f, Directions.getDirections(false, true, false, false, false, true));
      Enigne.createEngine(e, 0.45f, 0.31f, 180.0f, Directions.getDirections(false, true, false, false, true, false));

      Enigne.createEngine(e, -0.25f, -0.45f, 90.0f, Directions.getDirections(false, false, true, false, false, true));
      Enigne.createEngine(e, 0.25f, -0.45f, 90.0f, Directions.getDirections(false, false, true, false, true, false));
      Enigne.createEngine(e, -0.25f, 0.45f, 270.0f, Directions.getDirections(false, false, false, true, true, false));
      Enigne.createEngine(e, 0.25f, 0.45f, 270.0f, Directions.getDirections(false, false, false, true, false, true));
      Misc.createShield(e, bits, fixture, e.getComponent(ShieldComponent.class));

      return e;
   }

   public static Entity createEnemy(float x, float y) {
      Entity e = createShipType1(x, y, Bits.ENEMY);
      Weapon.createWeapon(e, -0.25f, 0.31f);
      Weapon.createWeapon(e, -0.25f, -0.31f);
      e.addToWorld();
      return e;
   }

   public static Entity createPlayerShip(float x, float y) {
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

      Weapon.createWeapon(e, 0.25f, 0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));
      // createWeapon(e, 0.25f,
      // -0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));
      // createWeapon(e, -0.25f,
      // 0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));
      // createWeapon(e, -0.25f,
      // -0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));

      e.addToWorld();

      return e;
   }

}
