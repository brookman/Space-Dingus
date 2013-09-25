package eu32k.gdx.artemis.extension.component;

import eu32k.gdx.artemis.base.Component;

public class CameraTargetComponent extends Component {
   public boolean rotateWithActor = false;

   public CameraTargetComponent init(boolean rotateWithActor) {
      this.rotateWithActor = rotateWithActor;
      return this;
   }
}
