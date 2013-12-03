package eu32k.spaceDingus.core.factory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Pools;

import eu32k.gdx.artemis.base.Entity;
import eu32k.gdx.artemis.extension.EntityActor;
import eu32k.gdx.artemis.extension.ExtendedWorld;
import eu32k.gdx.artemis.extension.component.ActorComponent;
import eu32k.gdx.artemis.extension.component.ParticleEffectComponent;
import eu32k.gdx.artemis.extension.component.PhysicsComponent;
import eu32k.gdx.artemis.extension.component.TextureRegionComponent;
import eu32k.gdx.artemis.extension.factory.Factory;
import eu32k.gdx.common.PhysicsModel;
import eu32k.gdx.common.Textures;
import eu32k.spaceDingus.core.common.GameBits;
import eu32k.spaceDingus.core.component.ExpireComponent;
import eu32k.spaceDingus.core.component.HealthComponent;

public class MiscFactory extends Factory {

   public MiscFactory(ExtendedWorld world, Stage stage) {
      super(world, stage);
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

   public Entity createExplosion(Entity at) {
      EntityActor actor = world.getMapper(ActorComponent.class).get(at).actor;

      Entity e = createActorEntity(actor.getPositionOnStage().x, actor.getPositionOnStage().y, 1.0f, 1.0f, 0, null);

      ParticleEffect effect = new ParticleEffect();
      effect.load(Gdx.files.internal("particles/explosion2.txt"), Gdx.files.internal("textures"));
      effect.start();

      e.addComponent(Pools.obtain(ParticleEffectComponent.class).init(effect));
      e.addComponent(Pools.obtain(ExpireComponent.class).init(2000));
      e.addToWorld();
      return e;
   }
}
