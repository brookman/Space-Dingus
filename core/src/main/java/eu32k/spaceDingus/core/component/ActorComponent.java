package eu32k.spaceDingus.core.component;

import com.artemis.Component;
import com.artemis.Entity;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorComponent extends Component {

   public Entity entity;
   public Actor actor;

   public ActorComponent init(Entity entity, Actor actor) {
      this.entity = entity;
      this.actor = actor;
      return this;
   }
}
