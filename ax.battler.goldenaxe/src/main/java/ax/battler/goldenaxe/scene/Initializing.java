package ax.battler.goldenaxe.scene;

import ax.battler.goldenaxe.infra.GoldenAxeGame;
import ax.battler.goldenaxe.infra.SceneManager;
import ax.battler.goldenaxe.infra.State;
import ax.battler.goldenaxe.infra.Util;

/**
 * Initializing class.
 *
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class Initializing extends State<GoldenAxeGame, SceneManager> {

  private long startTime;

  public Initializing(SceneManager manager, GoldenAxeGame game) {
    super(manager, "initializing", game);
  }

  @Override
  public void onEnter() {
    startTime = Util.getTime() + 500;
  }

  @Override
  public void fixedUpdate() {
    if (Util.getTime() >= startTime) {
      manager.switchTo("ol_presents");
    }
  }

}
