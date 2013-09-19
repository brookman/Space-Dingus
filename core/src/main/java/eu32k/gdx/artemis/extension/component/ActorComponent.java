package eu32k.gdx.artemis.extension.component;

import eu32k.gdx.artemis.base.Component;
import eu32k.gdx.artemis.extension.EntityActor;

public class ActorComponent extends Component {

   public EntityActor actor;

   public ActorComponent init(EntityActor actor) {
      this.actor = actor;
      return this;
   }
}
