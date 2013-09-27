package eu32k.spaceDingus.java;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import eu32k.spaceDingus.core.SpaceDingus;

public class SpaceDingusDesktop {
   public static void main(String[] args) {
      LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();

      cfg.title = "Space-Dingus";
      cfg.useGL20 = true;
      cfg.samples = 4;
      cfg.vSyncEnabled = true;
      cfg.resizable = true;
      cfg.width = 1000;
      cfg.height = 600;
      cfg.addIcon("textures/icons/icon_small.png", FileType.Local);

      new LwjglApplication(new SpaceDingus(), cfg);
   }
}
