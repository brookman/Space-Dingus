package eu32k.spaceDingus.core.factory;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Pools;

import eu32k.spaceDingus.core.common.Bits;
import eu32k.spaceDingus.core.common.PhysicsModel;
import eu32k.spaceDingus.core.common.RenderLayer;
import eu32k.spaceDingus.core.component.DamagableComponent;
import eu32k.spaceDingus.core.component.ExpireComponent;
import eu32k.spaceDingus.core.component.HealthComponent;
import eu32k.spaceDingus.core.component.ParticleEffectComponent;
import eu32k.spaceDingus.core.component.PhysicsComponent;
import eu32k.spaceDingus.core.component.PhysicsShieldComponent;
import eu32k.spaceDingus.core.component.ShieldComponent;
import eu32k.spaceDingus.core.component.SpriteComponent;

public class Misc {
   public static Entity createShield(Group parent, Bits bits, Fixture fixture, ShieldComponent shield) {
      Entity e = General.createActorEntity(0, 0, 0, parent);

      e.addComponent(shield);
      e.addComponent(Pools.obtain(PhysicsShieldComponent.class).init(bits, fixture));
      e.addComponent(Pools.obtain(SpriteComponent.class).init("textures/shield.png", 1.72f, 1.72f, RenderLayer.HIGHER_COMPONENTS_2));

      e.addToWorld();
      return e;
   }

   public static Entity createAsteroid(float x, float y) {
      Entity e = General.createActorEntity(x, y, 0, null);

      PhysicsModel asteroidModel = new PhysicsModel(EntityFactory.box2dWorld, e, "asteroid.json", "Asteroid", 1.0f, 1.0f, 0.3f, Bits.SCENERY, false);

      PhysicsComponent pc = Pools.obtain(PhysicsComponent.class).init(asteroidModel.getBody());
      pc.activate(new Vector2(x, y), MathUtils.random(MathUtils.PI2), new Vector2(0, 0));

      e.addComponent(pc);
      e.addComponent(Pools.obtain(SpriteComponent.class).init(asteroidModel.getTexturePath(), 1.0f, 1.0f, RenderLayer.ACTORS));
      e.addComponent(Pools.obtain(DamagableComponent.class).init());
      e.addComponent(Pools.obtain(HealthComponent.class).init(50));

      e.addToWorld();

      return e;
   }

   public static Entity createExplosion(float x, float y) {
      Entity e = General.createActorEntity(x, y, 0, null);

      ParticleEffect effect = new ParticleEffect();
      effect.load(Gdx.files.internal("particles/explosion2.txt"), Gdx.files.internal("textures"));
      effect.start();

      e.addComponent(Pools.obtain(ParticleEffectComponent.class).init(effect));
      e.addComponent(Pools.obtain(ExpireComponent.class).init(1000));
      e.addToWorld();
      return e;
   }

   public static Entity createMuzzleFlash(float x, float y, float rotation) {
      Entity e = General.createActorEntity(x, y, rotation, null);

      ParticleEffect effect = new ParticleEffect();
      effect.load(Gdx.files.internal("particles/muzzleFlash1.txt"), Gdx.files.internal("textures"));
      effect.start();

      e.addComponent(Pools.obtain(ParticleEffectComponent.class).init(effect));
      e.addComponent(Pools.obtain(ExpireComponent.class).init(500));
      e.addToWorld();
      return e;
   }
}
