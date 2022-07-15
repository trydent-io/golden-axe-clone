package ax.battler.goldenaxe.scene;

import ax.battler.goldenaxe.infra.Animator;
import ax.battler.goldenaxe.infra.Audio;
import ax.battler.goldenaxe.infra.Dialog;
import ax.battler.goldenaxe.infra.GoldenAxeGame;
import ax.battler.goldenaxe.infra.Resource;
import ax.battler.goldenaxe.infra.SceneManager;
import ax.battler.goldenaxe.infra.State;

import java.awt.*;

/**
 * OLPresents class.
 *
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class OLPresents extends State<GoldenAxeGame, SceneManager> {

  private Animator animator;

  public OLPresents(SceneManager manager, GoldenAxeGame game) {
    super(manager, "ol_presents", game);
  }

  private void reset() {
    animator = null;
    Dialog.hide();
  }

  @Override
  public void onEnter() {
    reset();
    animator = Resource.getAnimators("ol_presents").get("ol_presents");
    animator.play();
    Audio.stopMusic();
  }

  @Override
  public void onExit() {
    reset();
    System.gc();
  }

  @Override
  public void fixedUpdate() {
    animator.fixedUpdate();
    if (!manager.isTransiting() && animator.isFinished()) {
      manager.switchTo("disclaimer");
    }
  }

  @Override
  public void draw(Graphics2D g) {
    animator.draw(g, null, 0, 0);
  }

}
