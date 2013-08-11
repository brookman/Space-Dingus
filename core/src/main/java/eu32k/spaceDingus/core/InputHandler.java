package eu32k.spaceDingus.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;

import eu32k.spaceDingus.core.common.Directions;

public class InputHandler {

   public boolean leftMouse = false;
   public float mouseX = 0;
   public float mouseY = 0;
   public int directions;
   public boolean k1;

   private Camera camera;

   public InputHandler(Camera camera) {
      this.camera = camera;
      directions = 0;
   }

   public void update() {
      leftMouse = Gdx.input.isButtonPressed(Buttons.LEFT);
      // Vector3 temp = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0.5f);
      // camera.unproject(temp, SpaceDingus.viewport.x, SpaceDingus.viewport.y, SpaceDingus.viewport.width, SpaceDingus.viewport.height);

      Plane xyPlane = new Plane(new Vector3(0, 0, 1), 0);
      Ray ray = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY(), SpaceDingus.viewport.x, SpaceDingus.viewport.y, SpaceDingus.viewport.width, SpaceDingus.viewport.height);
      Vector3 intersection = new Vector3(0, 0, 0);
      Intersector.intersectRayPlane(ray, xyPlane, intersection);

      mouseX = intersection.x;
      mouseY = intersection.y;

      directions = Directions.getDirections(//
            Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP),//
            Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN),//
            Gdx.input.isKeyPressed(Input.Keys.Q),//
            Gdx.input.isKeyPressed(Input.Keys.E),//
            Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT),//
            Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT));
      k1 = Gdx.input.isKeyPressed(Input.Keys.NUM_1);
   }
}
