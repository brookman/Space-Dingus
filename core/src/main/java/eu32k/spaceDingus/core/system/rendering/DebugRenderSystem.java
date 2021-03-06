package eu32k.spaceDingus.core.system.rendering;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

import eu32k.gdx.artemis.base.Aspect;
import eu32k.gdx.artemis.base.ComponentMapper;
import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.base.EntitySystem;
import eu32k.gdx.artemis.base.utils.ImmutableBag;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.artemis.extension.component.PhysicsComponent;
import eu32k.gdx.common.DebugRenderer;
import eu32k.spaceDingus.core.InputHandler;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.PlayerControlledMovableComponent;

public class DebugRenderSystem extends EntitySystem {

   private ComponentMapper<ActorComponent> am;
   private ComponentMapper<HealthComponent> hm;
   private ComponentMapper<PhysicsComponent> phm;
   private ComponentMapper<PlayerControlledMovableComponent> plm;

   private InputHandler inputHandler;
   private World box2dWorld;
   private Camera camera;
   private ShapeRenderer shapeRenderer;
   private Box2DDebugRenderer debugRenderer;

   private boolean k1WasPressed = false;

   private enum DebugMode {
      NONE, TEXT_ONLY, SIMPLE_PHYSICS, EXTENDED_PHYSICS, ALL;

      public int getIndex(DebugMode m) {
         for (int i = 0; i < values().length; i++) {
            if (values()[i] == m) {
               return i;
            }
         }
         return -1;
      }

      public DebugMode next() {
         int index = getIndex(this);
         index = (index + 1) % values().length;
         return values()[index];
      }
   };

   private DebugMode mode = DebugMode.NONE;

   @SuppressWarnings("unchecked")
   public DebugRenderSystem(InputHandler inputHandler, World box2dWorld, Camera camera) {
      super(Aspect.getAspectForAll(ActorComponent.class));
      this.inputHandler = inputHandler;
      this.box2dWorld = box2dWorld;
      this.camera = camera;
   }

   @Override
   protected void initialize() {
      am = world.getMapper(ActorComponent.class);
      hm = world.getMapper(HealthComponent.class);
      phm = world.getMapper(PhysicsComponent.class);
      plm = world.getMapper(PlayerControlledMovableComponent.class);

      shapeRenderer = new ShapeRenderer();

      debugRenderer = new Box2DDebugRenderer();
      debugRenderer.setDrawAABBs(true);
      debugRenderer.setDrawBodies(true);
      // debugRenderer.setDrawContacts(true);
      debugRenderer.setDrawInactiveBodies(true);
      debugRenderer.setDrawJoints(true);
      debugRenderer.setDrawVelocities(true);

   }

   @Override
   protected void begin() {
      shapeRenderer.setProjectionMatrix(camera.combined);
   }

   public void drawShapes(Entity e) {
      Actor actor = am.get(e).actor;

      if (phm.has(e)) {
         Body body = phm.get(e).body;

         shapeRenderer.begin(ShapeType.Line);

         shapeRenderer.setColor(1, 1, 1, 1.0f);
         shapeRenderer.line(new Vector2(actor.getX(), actor.getY()), new Vector2(0, 0));

         shapeRenderer.end();
      }

   }

   public void drawText(Entity e) {
      // if (nm.has(e) && nm.get(e).getParent() != null) {
      // return;
      // }
      Actor actor = am.get(e).actor;

      StringBuffer s = new StringBuffer();
      s.append("\n\n\n\n");
      if (plm.has(e)) {
         s.append("Player\n");
      }

      s.append("X:" + format(actor.getX()) + " Y:" + format(actor.getY()) + "\n");
      if (hm.has(e)) {
         s.append("HP:" + format(hm.get(e).health) + "/" + format(hm.get(e).maxHealth) + "\n");
      }

      if (phm.has(e)) {
         Body body = phm.get(e).body;
         s.append("Speed:" + format(body.getLinearVelocity().len()) + "\n");
         s.append("Angle:" + format(body.getAngle() * MathUtils.radiansToDegrees) + "\n");
         s.append("Angular speed:" + format(body.getAngularVelocity()) + "\n");
      }

      String result = s.toString();
      DebugRenderer.drawText(result, actor.getX(), actor.getY(), false);
   }

   private static String format(float f) {
      String num = "" + MathUtils.round(f * 100.0f) / 100.0f;
      if (f >= 0.0f) {
         num = " " + num;
      }
      String arr[] = num.split("\\.");
      String suffix = arr[arr.length - 1];
      if (suffix.length() == 1) {
         return num + "0";
      }
      return num;
   }

   @Override
   protected boolean checkProcessing() {
      return true;
   }

   @Override
   protected void processEntities(ImmutableBag<Entity> entities) {
      if (!k1WasPressed && inputHandler.k1) {
         mode = mode.next();
      }
      k1WasPressed = inputHandler.k1;

      if (mode == DebugMode.NONE) {
         return;
      }

      if (mode == DebugMode.SIMPLE_PHYSICS || mode == DebugMode.EXTENDED_PHYSICS || mode == DebugMode.ALL) {
         debugRenderer.render(box2dWorld, camera.combined);
      }

      if (mode == DebugMode.EXTENDED_PHYSICS || mode == DebugMode.ALL) {
         for (int i = 0; i < entities.size(); i++) {
            drawShapes(entities.get(i));
         }
      }

      if (mode == DebugMode.TEXT_ONLY || mode == DebugMode.ALL) {
         for (int i = 0; i < entities.size(); i++) {
            drawText(entities.get(i));
         }
      }
   }
}
