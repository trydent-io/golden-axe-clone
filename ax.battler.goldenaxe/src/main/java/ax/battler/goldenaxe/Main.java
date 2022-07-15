package ax.battler.goldenaxe;

import ax.battler.goldenaxe.infra.Display;
import ax.battler.goldenaxe.infra.GameCanvas;
import ax.battler.goldenaxe.infra.GoldenAxeGame;
import ax.battler.goldenaxe.infra.Resource;

import javax.swing.*;

/**
 * Main class.
 * <p>
 * Game entry point.
 *
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class Main {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      GameCanvas gameCanvas = new GameCanvas(new GoldenAxeGame());
      Display display = new Display(gameCanvas);
      display.setTitle("Golden Axe");
      display.setIconImage(Resource.getImage("icon"));
      display.start();
    });
  }

}
