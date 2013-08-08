package eu32k.spaceDingus.core.component;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class PolygonModelComponent extends Component {

   public Model model;
   public ModelInstance modelInstance;

   public PolygonModelComponent init(Model model) {
      this.model = model;
      modelInstance = new ModelInstance(model);
      return this;
   }

   public PolygonModelComponent init(Model model, Vector3 rotation, float angle) {
      init(model);
      modelInstance.transform.setToRotation(new Vector3(1, 0, 0), 90);
      return this;
   }
}
