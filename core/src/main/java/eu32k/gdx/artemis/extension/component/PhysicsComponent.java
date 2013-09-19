package eu32k.gdx.artemis.extension.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

import eu32k.gdx.artemis.base.Component;

public class PhysicsComponent extends Component {

   public Body body;

   private Pool<Body> pool;

   public PhysicsComponent init(Body body, Pool<Body> pool) {
      init(body);
      this.pool = pool;
      return this;
   }

   public PhysicsComponent init(Body body) {
      this.body = body;
      body.setActive(false);
      pool = null;
      return this;
   }

   public void activate(Vector2 position, float rotation, Vector2 velocity) {
      body.setTransform(position, rotation);
      body.setLinearVelocity(velocity);
      body.setAngularVelocity(0);
      body.setActive(true);
   }

   public void deactivate() {
      body.setActive(false);
   }

   public void delete() {
      deactivate();
      if (pool != null) {
         pool.free(body);
      } else {
         body.getWorld().destroyBody(body);
      }
   }
}
