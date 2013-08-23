package eu32k.spaceDingus.core.factory;

import com.artemis.Entity;
import com.artemis.managers.GroupManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.core.common.Bits;
import eu32k.spaceDingus.core.common.Directions;
import eu32k.spaceDingus.core.common.PhysicsModel;
import eu32k.spaceDingus.core.common.RenderLayer;
import eu32k.spaceDingus.core.component.ActorComponent;
import eu32k.spaceDingus.core.component.CameraTargetComponent;
import eu32k.spaceDingus.core.component.DamagableComponent;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.PlayerControlledMovableComponent;
import eu32k.spaceDingus.core.component.ShieldComponent;
import eu32k.spaceDingus.core.component.SpriteComponent;
import eu32k.spaceDingus.core.component.StabilizerComponent;
import eu32k.spaceDingus.core.component.weapon.PlayerControlledWeaponComponent;

public class Ship {

   private static Entity createGenericShip() {
      Entity e = General.createGroupEntity(0, 0, 0, null);
      e.addComponent(Pools.obtain(MovableComponent.class).init(50f, 50.0f));
      e.addComponent(Pools.obtain(StabilizerComponent.class).init(true, true));
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

      Group g = (Group) EntityFactory.artemisWorld.getMapper(ActorComponent.class).get(e).actor;

      Enigne.createEngine(g, -0.45f, -0.31f, 0.0f, 50.0f, Directions.getDirections(true, false, false, false, true, false), 0.5f);
      Enigne.createEngine(g, -0.45f, 0.31f, 0.0f, 50.0f, Directions.getDirections(true, false, false, false, false, true), 0.5f);
      Enigne.createEngine(g, 0.45f, -0.31f, 180.0f, 50.0f, Directions.getDirections(false, true, false, false, false, true), 0.5f);
      Enigne.createEngine(g, 0.45f, 0.31f, 180.0f, 50.0f, Directions.getDirections(false, true, false, false, true, false), 0.5f);

      Enigne.createEngine(g, -0.25f, -0.45f, 90.0f, 50.0f, Directions.getDirections(false, false, true, false, false, true), 0.5f);
      Enigne.createEngine(g, 0.25f, -0.45f, 90.0f, 50.0f, Directions.getDirections(false, false, true, false, true, false), 0.5f);
      Enigne.createEngine(g, -0.25f, 0.45f, 270.0f, 50.0f, Directions.getDirections(false, false, false, true, true, false), 0.5f);
      Enigne.createEngine(g, 0.25f, 0.45f, 270.0f, 50.0f, Directions.getDirections(false, false, false, true, false, true), 0.5f);
      Misc.createShield(g, bits, fixture, e.getComponent(ShieldComponent.class));

      return e;
   }

   public static Entity createEnemy(float x, float y) {
      Entity e = createShipType1(x, y, Bits.ENEMY);
      EntityFactory.artemisWorld.getManager(GroupManager.class).add(e, "ENEMY");

      Group g = (Group) EntityFactory.artemisWorld.getMapper(ActorComponent.class).get(e).actor;
      Weapon.createWeapon(g, -0.25f, 0.31f);
      Weapon.createWeapon(g, -0.25f, -0.31f);
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

      Group g = (Group) EntityFactory.artemisWorld.getMapper(ActorComponent.class).get(e).actor;

      Weapon.createWeapon(g, 0.25f, 0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));
      Weapon.createWeapon(g, 0.25f, -0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));
      Weapon.createWeapon(g, -0.25f, 0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));
      Weapon.createWeapon(g, -0.25f, -0.31f).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class));

      e.addToWorld();

      return e;
   }

}
