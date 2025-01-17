package ax.battler.goldenaxe.actor;

import ax.battler.goldenaxe.infra.Actor;
import ax.battler.goldenaxe.infra.Camera;
import ax.battler.goldenaxe.infra.Resource;
import ax.battler.goldenaxe.infra.Util;
import ax.battler.goldenaxe.scene.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Water class.
 * <p>
 * Specific ax.battler.goldenaxe.actor for water present in the turtle village stage
 * as this was difficult to achieve using existing generic solution.
 *
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class Water extends Actor {

  private final BufferedImage[] frames = new BufferedImage[3];
  private final int[] waterOffset = {0, 28};
  private double frameIndex;
  private double tx;
  private double ty;
  private double minTy;

  public Water(Stage stage) {
    super(stage, "water", 0, 0, null, null, null);
    for (int i = 0; i < 3; i++) {
      frames[i] = Resource.getImage("water_" + i);
    }
    collisionCheckEnabled = false;
  }

  @Override
  public void start() {
    minTy = 490;
  }

  @Override
  public void fixedUpdate() {
    frameIndex += 0.1;
    if (frameIndex > 2) {
      frameIndex = 0;
    }
    tx -= 4;
    ty = minTy + 3 * Math.sin(Util.getTime() * 0.002);
    minTy -= 0.1;
    if (minTy < 470) {
      minTy = 470;
    }
    if (Camera.getX() >= 2180 && Camera.getY() <= 250) {
      destroy();
    }
  }

  @Override
  public void draw(Graphics2D g, int cameraX, int cameraY) {
    int px = (int) (tx - cameraX - waterOffset[(int) frameIndex]) % 56;
    int py = (int) (ty - cameraY);
    for (int i = 0; i < 6; i++) {
      g.drawImage(frames[(int) (frameIndex)], px + i * 56, py, null);
    }
  }

}
