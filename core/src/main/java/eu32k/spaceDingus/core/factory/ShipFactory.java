package eu32k.spaceDingus.core.factory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pools;

import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.managers.GroupManager;
import eu32k.gdx.artemis.extension.EntityActor;
import eu32k.gdx.artemis.extension.ExtendedWorld;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.artemis.extension.component.CameraTargetComponent;
import eu32k.gdx.artemis.extension.component.PhysicsComponent;
import eu32k.gdx.artemis.extension.component.TextureRegionComponent;
import eu32k.gdx.artemis.extension.factory.Factory;
import eu32k.gdx.common.Bits;
import eu32k.gdx.common.PhysicsModel;
import eu32k.gdx.common.Textures;
import eu32k.spaceDingus.core.common.Directions;
import eu32k.spaceDingus.core.common.GameBits;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.MovableComponent;
import eu32k.spaceDingus.core.component.PlayerControlledMovableComponent;
import eu32k.spaceDingus.core.component.StabilizerComponent;
import eu32k.spaceDingus.core.component.weapon.PlayerControlledWeaponComponent;
import eu32k.spaceDingus.core.component.weapon.WeaponComponent;

public class ShipFactory extends Factory {

   private AccessoryFactory af;

   public ShipFactory(ExtendedWorld world, Stage stage, AccessoryFactory accessoryFactory) {
      super(world, stage);
      af = accessoryFactory;
   }

   private Entity createGenericShip(float x, float y) {
      Entity e = createActorEntity(x, y, 1, 1, 0, null);

      e.addComponent(Pools.obtain(MovableComponent.class).init(50f, 50.0f));
      e.addComponent(Pools.obtain(StabilizerComponent.class).init(true, true));
      e.addComponent(Pools.obtain(HealthComponent.class).init(100));
      return e;
   }

   private Entity createShipType1(float x, float y, Bits bits) {
      Entity e = createGenericShip(x, y);

      PhysicsModel shipModel = new PhysicsModel(world.box2dWorld, e, "ship.json", "Ship2", 2.0f, 1.0f, 0.0f, bits, false, 1.0f);

      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(shipModel.getBody());
      pc.activate(new Vector2(x, y), 0, new Vector2(0, 0));

      e.addComponent(pc);
      e.addComponent(Pools.obtain(StabilizerComponent.class).init(true, true));
      e.addComponent(Pools.obtain(TextureRegionComponent.class).init(new TextureRegion(Textures.get("models/ship2.png"))));

      Group g = world.getMapper(ActorComponent.class).get(e).actor;

      // af.createEngine(g, -0.45f, -0.31f, 0.0f, 50.0f, Directions.get(Directions.TRANSLATE_FORWARD, Directions.ROTATE_LEFT), 0.5f);
      // af.createEngine(g, -0.45f, 0.31f, 0.0f, 50.0f, Directions.get(Directions.TRANSLATE_FORWARD, Directions.ROTATE_RIGHT), 0.5f);
      // af.createEngine(g, 0.45f, -0.31f, 180.0f, 50.0f, Directions.get(Directions.TRANSLATE_BACKWARD, Directions.ROTATE_RIGHT), 0.5f);
      // af.createEngine(g, 0.45f, 0.31f, 180.0f, 50.0f, Directions.get(Directions.TRANSLATE_BACKWARD, Directions.ROTATE_LEFT), 0.5f);
      //
      // af.createEngine(g, -0.25f, -0.45f, 90.0f, 50.0f, Directions.get(Directions.TRANSLATE_LEFT, Directions.ROTATE_RIGHT), 0.5f);
      // af.createEngine(g, 0.25f, -0.45f, 90.0f, 50.0f, Directions.get(Directions.TRANSLATE_LEFT, Directions.ROTATE_LEFT), 0.5f);
      // af.createEngine(g, -0.25f, 0.45f, 270.0f, 50.0f, Directions.get(Directions.TRANSLATE_RIGHT, Directions.ROTATE_LEFT), 0.5f);
      // af.createEngine(g, 0.25f, 0.45f, 270.0f, 50.0f, Directions.get(Directions.TRANSLATE_RIGHT, Directions.ROTATE_RIGHT), 0.5f);

      af.createEngine(g, -0.375f, -0.0f, 0.0f, 50.0f, Directions.get(Directions.TRANSLATE_FORWARD), 0.5f);
      af.createEngine(g, 0.1252f, -0.21875f, 180.0f, 20.0f, Directions.get(Directions.TRANSLATE_BACKWARD), 0.2f);
      af.createEngine(g, 0.1252f, 0.21875f, 180.0f, 20.0f, Directions.get(Directions.TRANSLATE_BACKWARD), 0.2f);

      af.createEngine(g, -0.34375f, -0.25f, 90.0f, 20.0f, Directions.get(Directions.TRANSLATE_LEFT, Directions.ROTATE_RIGHT), 0.2f);
      af.createEngine(g, 0.1f, -0.21875f, 90.0f, 20.0f, Directions.get(Directions.TRANSLATE_LEFT, Directions.ROTATE_LEFT), 0.2f);
      af.createEngine(g, -0.34375f, 0.25f, 270.0f, 20.0f, Directions.get(Directions.TRANSLATE_RIGHT, Directions.ROTATE_LEFT), 0.2f);
      af.createEngine(g, 0.1f, 0.21875f, 270.0f, 20.0f, Directions.get(Directions.TRANSLATE_RIGHT, Directions.ROTATE_RIGHT), 0.2f);

      FixtureDef fixtureDef = new FixtureDef();
      fixtureDef.density = 0.0f;
      fixtureDef.friction = 1.0f;
      fixtureDef.restitution = 0.5f;
      fixtureDef.filter.categoryBits = bits.category;
      fixtureDef.filter.maskBits = bits.mask;
      CircleShape shape = new CircleShape();
      shape.setRadius(0.5f);
      fixtureDef.shape = shape;

      Fixture fixture = shipModel.getBody().createFixture(fixtureDef);
      fixture.setRestitution(0.0f);

      af.createShield(g, bits, fixture, 100.0f);

      return e;
   }

   public Entity createEnemy(float x, float y) {
      Entity e = createShipType1(x, y, GameBits.ENEMY);
      world.getManager(GroupManager.class).add(e, "ENEMY");

      EntityActor ea = world.getMapper(ActorComponent.class).get(e).actor;

      world.getMapper(PhysicsComponent.class).get(e).body.setTransform(new Vector2(x, y), 20.0f);
      // af.createWeapon(g, -0.25f, 0.31f);
      // af.createWeapon(g, -0.25f, -0.31f);
      e.addToWorld();
      return e;
   }

   public Entity createPlayerShip(float x, float y) {
      Entity e = createShipType1(x, y, GameBits.PLAYER);

      e.addComponent(new PlayerControlledMovableComponent());
      e.addComponent(Pools.obtain(CameraTargetComponent.class).init(false));

      Group g = world.getMapper(ActorComponent.class).get(e).actor;

      af.createWeapon(g, 0.125f, 0.0f, 0, 500, WeaponComponent.BULLET_TYPE_NORMAL, true).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class).init());
      af.createWeapon(g, -0.25f, 0.125f, 30, 1500, WeaponComponent.BULLET_TYPE_ROCKET, false).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class).init());
      af.createWeapon(g, -0.25f, -0.125f, 330, 1500, WeaponComponent.BULLET_TYPE_ROCKET, false).addComponent(Pools.obtain(PlayerControlledWeaponComponent.class).init());

      e.addToWorld();

      return e;
   }
}
