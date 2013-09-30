package eu32k.spaceDingus.core;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;

import eu32k.gdx.artemis.base.managers.GroupManager;
import eu32k.gdx.artemis.extension.ExtendedWorld;
import eu32k.gdx.artemis.extension.system.CameraSystem;
import eu32k.gdx.artemis.extension.system.PhysicsSystem;
import eu32k.gdx.artemis.extension.system.RemoveSystem;
import eu32k.gdx.common.DebugRenderer;
import eu32k.spaceDingus.core.factory.AccessoryFactory;
import eu32k.spaceDingus.core.factory.BulletFactory;
import eu32k.spaceDingus.core.factory.MiscFactory;
import eu32k.spaceDingus.core.factory.ShipFactory;
import eu32k.spaceDingus.core.system.CollisionDamageSystem;
import eu32k.spaceDingus.core.system.DeathSystem;
import eu32k.spaceDingus.core.system.ExpireSystem;
import eu32k.spaceDingus.core.system.PreRemoveSystem;
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
import eu32k.spaceDingus.core.system.rendering.DebugRenderSystem;
import eu32k.spaceDingus.core.system.rendering.HealthRenderSystem;
import eu32k.spaceDingus.core.system.rendering.ParticleEffectSystem;

public class SpaceDingus implements ApplicationListener {

   private static final float VIRTUAL_WIDTH = 8.0f;
   private static final float VIRTUAL_HEIGHT = 4.8f;

   private OrthographicCamera camera;
   private ExtendedWorld artemisWorld;
   private World box2dWorld;
   public static Rectangle viewport;
   private InputHandler inputHandler;

   private AccessoryFactory accessoryFactory;
   private BulletFactory bulletFactory;
   private MiscFactory miscFactory;
   private ShipFactory shipFactory;

   private Stage gameStage;
   private Stage hudStage;

   @Override
   public void create() {
      camera = new OrthographicCamera(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
      camera.zoom = 1.0f;
      inputHandler = new InputHandler(camera);

      gameStage = new Stage(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, true);
      gameStage.setCamera(camera);
      hudStage = new Stage(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, true);

      DebugRenderer.init(camera);

      box2dWorld = new World(new Vector2(0, 0), true);
      artemisWorld = new ExtendedWorld(box2dWorld, gameStage);
      artemisWorld.setManager(new GroupManager());

      accessoryFactory = new AccessoryFactory(artemisWorld, gameStage);
      bulletFactory = new BulletFactory(artemisWorld, gameStage, accessoryFactory);
      miscFactory = new MiscFactory(artemisWorld, gameStage);
      shipFactory = new ShipFactory(artemisWorld, gameStage, accessoryFactory);

      artemisWorld.setSystem(new PhysicsSystem(box2dWorld));
      artemisWorld.setSystem(new CollisionDamageSystem(box2dWorld));

      artemisWorld.setSystem(new MovableResetSystem());
      artemisWorld.setSystem(new AimingSystem());
      artemisWorld.setSystem(new TargetingSystem());
      artemisWorld.setSystem(new MovableInputSystem(inputHandler));
      artemisWorld.setSystem(new StabilizerSystem());
      artemisWorld.setSystem(new StearingSystem());
      artemisWorld.setSystem(new EngineSystem());

      artemisWorld.setSystem(new ShieldSystem());

      artemisWorld.setSystem(new WeaponInputSystem(inputHandler));
      artemisWorld.setSystem(new WeaponSystem(bulletFactory, miscFactory));

      artemisWorld.setSystem(new ExpireSystem());
      artemisWorld.setSystem(new DeathSystem());
      artemisWorld.setSystem(new PreRemoveSystem(miscFactory));
      artemisWorld.setSystem(new RemoveSystem());

      artemisWorld.setSystem(new ParticleEffectSystem());

      artemisWorld.setSystem(new CameraSystem(camera));
      artemisWorld.setSystem(new DebugRenderSystem(inputHandler, box2dWorld, camera));
      artemisWorld.setSystem(new HealthRenderSystem(camera));

      artemisWorld.initialize();

      createEntities();
   }

   private void createEntities() {

      shipFactory.createPlayerShip(0, 0);
      shipFactory.createEnemy(1, 1);
      shipFactory.createEnemy(2, 1);
      miscFactory.createAsteroid(3, 0, 1);
      miscFactory.createAsteroid(-2, 1, 2);
      miscFactory.createAsteroid(-2, -2, 3);
      miscFactory.createAsteroid(-3, 0, 2);
   }

   @Override
   public void dispose() {
      // NOP
   }

   @Override
   public void render() {
      inputHandler.update();

      // Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);
      Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
      Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
      Gdx.gl.glEnable(GL20.GL_BLEND);
      Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

      if (!inputHandler.paused) {
         artemisWorld.setDelta(Gdx.graphics.getDeltaTime());
         artemisWorld.process();
      }

      gameStage.act(Gdx.graphics.getDeltaTime());
      gameStage.draw();

      hudStage.act(Gdx.graphics.getDeltaTime());
      hudStage.draw();

      DebugRenderer.drawText("FPS: " + Gdx.graphics.getFramesPerSecond(), Gdx.graphics.getWidth() / 2.0f, 15, true);
      DebugRenderer.drawText("Mouse: aim & shoot bullet\nSpace: FIRE ZE MISSILES!", Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() - 25, 0.002f, true);
      if (inputHandler.paused) {
         DebugRenderer.drawText("Paused", Gdx.graphics.getWidth() / 2.0f, Gdx.graphics.getHeight() / 2.0f, 0.004f, true);
      }
   }

   @Override
   public void resize(int width, int height) {
      Vector2 newVirtualRes = new Vector2(0f, 0f);
      Vector2 crop = new Vector2(width, height);
      newVirtualRes.set(Scaling.fit.apply(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, width, height));
      crop.sub(newVirtualRes);
      crop.scl(.5f);
      viewport = new Rectangle(crop.x, crop.y, newVirtualRes.x, newVirtualRes.y);
      resize2(width, height);
   }

   public void resize2(int width, int height) {
      Vector2 size = Scaling.fit.apply(8.0f, 4.8f, width, height);
      int viewportX = (int) (width - size.x) / 2;
      int viewportY = (int) (height - size.y) / 2;
      int viewportWidth = (int) size.x;
      int viewportHeight = (int) size.y;
      Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);
      gameStage.setViewport(gameStage.getWidth(), gameStage.getHeight(), true, viewportX, viewportY, viewportWidth, viewportHeight);
   }

   @Override
   public void pause() {
      System.out.println("pause");
   }

   @Override
   public void resume() {
   }
}
