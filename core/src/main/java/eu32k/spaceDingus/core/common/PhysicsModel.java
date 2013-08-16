package eu32k.spaceDingus.core.common;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsModel {

   private Body body;
   private BodyEditorLoader loader;
   private String modelName;

   public PhysicsModel(World box2dWorld, Entity e, String fileName, String modelName, float density, float friction, float restitution, Bits bits, boolean bullet) {
      this.modelName = modelName;
      FixtureDef fixtureDef = new FixtureDef();
      fixtureDef.density = density;
      fixtureDef.friction = friction;
      fixtureDef.restitution = restitution;
      fixtureDef.filter.categoryBits = bits.category;
      fixtureDef.filter.maskBits = bits.mask;

      BodyDef bodyDef = new BodyDef();
      bodyDef.type = BodyType.DynamicBody;
      bodyDef.bullet = bullet;

      body = box2dWorld.createBody(bodyDef);
      body.setUserData(e);

      body.setLinearDamping(0.1f);
      body.setAngularDamping(0.1f);

      loader = new BodyEditorLoader(Gdx.files.internal("models/" + fileName));
      loader.attachFixture(body, modelName, fixtureDef, 1);
      body.resetMassData();
   }

   public Body getBody() {
      return body;
   }

   public String getTexturePath() {
      return "models/" + loader.getImagePath(modelName);
   }
}