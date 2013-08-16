package eu32k.spaceDingus.core;

import com.artemis.managers.GroupManager;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Scaling;

import eu32k.spaceDingus.core.factory.EntityFactory;
import eu32k.spaceDingus.core.factory.Misc;
import eu32k.spaceDingus.core.factory.Ship;
import eu32k.spaceDingus.core.sceneGraph.SceneGraphWorld;
import eu32k.spaceDingus.core.sceneGraph.system.SceneGraphSystem;
import eu32k.spaceDingus.core.system.CollisionDamageSystem;
import eu32k.spaceDingus.core.system.DamageSystem;
import eu32k.spaceDingus.core.system.DeathSystem;
import eu32k.spaceDingus.core.system.ExpireSystem;
import eu32k.spaceDingus.core.system.PhysicsSystem;
import eu32k.spaceDingus.core.system.RemoveSystem;
import eu32k.spaceDingus.core.system.ShieldSystem;
import eu32k.spaceDingus.core.system.WeaponInputSystem;
import eu32k.spaceDingus.core.system.WeaponSystem;
import eu32k.spaceDingus.core.system.moving.AimingSystem;
import eu32k.spaceDingus.core.system.moving.EngineSystem;
import eu32k.spaceDingus.core.system.moving.MovableInputSystem;
import eu32k.spaceDingus.core.system.moving.MovableResetSystem;
import eu32k.spaceDingus.core.system.moving.StabilizerSystem;
import eu32k.spaceDingus.core.system.moving.StearingSystem;
import eu32k.spaceDingus.core.system.moving.TargetingSystem;
import eu32k.spaceDingus.core.system.rendering.CameraSystem;
import eu32k.spaceDingus.core.system.rendering.DebugRenderSystem;
import eu32k.spaceDingus.core.system.rendering.HealthRenderSystem;
import eu32k.spaceDingus.core.system.rendering.SpriteRenderSystem;

public class SpaceDingus implements ApplicationListener {

   private static final float VIRTUAL_WIDTH = 8.0f;
   private static final float VIRTUAL_HEIGHT = 4.8f;

   private Camera camera;
   private com.artemis.World artemisWorld;
   private World box2dWorld;
   public static Rectangle viewport;
   private InputHandler inputHandler;

   @Override
   public void create() {
      camera = new OrthographicCamera(VIRTUAL_WIDTH * 2, VIRTUAL_HEIGHT * 2);
      // camera = new PerspectiveCamera(67, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
      // camera.position.set(0f, 0f, 3f);
      // camera.near = 0.05f;
      // camera.far = 20f;
      // camera.update();

      inputHandler = new InputHandler(camera);

      artemisWorld = new SceneGraphWorld();
      box2dWorld = new World(new Vector2(0, 0), true);

      artemisWorld.setManager(new GroupManager());

      EntityFactory.init(artemisWorld, box2dWorld, null);

      artemisWorld.setSystem(new PhysicsSystem(box2dWorld));
      artemisWorld.setSystem(new SceneGraphSystem());
      artemisWorld.setSystem(new CollisionDamageSystem(box2dWorld));

      artemisWorld.setSystem(new MovableResetSystem());
      artemisWorld.setSystem(new AimingSystem());
      artemisWorld.setSystem(new TargetingSystem());
      artemisWorld.setSystem(new MovableInputSystem(inputHandler));
      artemisWorld.setSystem(new StabilizerSystem());
      artemisWorld.setSystem(new StearingSystem());
      artemisWorld.setSystem(new EngineSystem(camera));

      artemisWorld.setSystem(new DamageSystem());
      artemisWorld.setSystem(new ShieldSystem());

      artemisWorld.setSystem(new WeaponInputSystem(inputHandler));
      artemisWorld.setSystem(new WeaponSystem());

      artemisWorld.setSystem(new ExpireSystem());
      artemisWorld.setSystem(new DeathSystem());
      artemisWorld.setSystem(new RemoveSystem());

      artemisWorld.setSystem(new CameraSystem(camera));
      artemisWorld.setSystem(new SpriteRenderSystem(camera));
      // artemisWorld.setSystem(new PolygonModelRenderSystem(camera));
      artemisWorld.setSystem(new DebugRenderSystem(inputHandler, box2dWorld, camera));
      artemisWorld.setSystem(new HealthRenderSystem(camera));

      artemisWorld.initialize();

      createEntities();
   }

   private static void createEntities() {
      // EntityFactory.createBackground();
      Ship.createPlayerShip(0, 0);
      Ship.createEnemy(1, 1);
      Misc.createAsteroid(3, 0);
      Misc.createAsteroid(-2, 1);
   }

   @Override
   public void dispose() {
      // NOP
   }

   @Override
   public void render() {
      inputHandler.update();

      Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);
      Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

      artemisWorld.setDelta(Gdx.graphics.getDeltaTime());
      artemisWorld.process();
   }

   @Override
   public void resize(int width, int height) {
      Vector2 newVirtualRes = new Vector2(0f, 0f);
      Vector2 crop = new Vector2(width, height);
      newVirtualRes.set(Scaling.fit.apply(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, width, height));
      crop.sub(newVirtualRes);
      crop.scl(.5f);
      viewport = new Rectangle(crop.x, crop.y, newVirtualRes.x, newVirtualRes.y);
   }

   @Override
   public void pause() {
   }

   @Override
   public void resume() {
   }
}
