package ax.battler.goldenaxe.scene;

import ax.battler.goldenaxe.infra.Animator;
import ax.battler.goldenaxe.infra.Audio;
import ax.battler.goldenaxe.infra.Dialog;
import ax.battler.goldenaxe.infra.GoldenAxeGame;
import ax.battler.goldenaxe.infra.Input;
import ax.battler.goldenaxe.infra.Offscreen;
import ax.battler.goldenaxe.infra.Resource;
import ax.battler.goldenaxe.infra.SceneManager;
import ax.battler.goldenaxe.infra.State;
import ax.battler.goldenaxe.infra.Terrain;
import ax.battler.goldenaxe.infra.Util;

import java.awt.*;

import static ax.battler.goldenaxe.infra.Settings.CANVAS_HEIGHT;
import static ax.battler.goldenaxe.infra.Settings.CANVAS_WIDTH;
import static ax.battler.goldenaxe.infra.Settings.KEY_CANCEL;
import static ax.battler.goldenaxe.infra.Settings.KEY_START_1;
import static ax.battler.goldenaxe.infra.Settings.KEY_START_2;

/**
 * Ending class.
 *
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class Ending extends State<GoldenAxeGame, SceneManager> {

  private Offscreen collisionMask;
  private Offscreen shadowLayer;
  private Animator animator;

  public Ending(SceneManager manager, GoldenAxeGame game) {
    super(manager, "ending", game);
  }

  private void reset() {
    collisionMask = null;
    shadowLayer = null;
    animator = null;
    Terrain.reset();
    Dialog.hide();
  }

  @Override
  public void onEnter() {
    reset();
    collisionMask = new Offscreen(CANVAS_WIDTH, CANVAS_HEIGHT);
    collisionMask.getG2d().setColor(Util.getColor("0x998000"));
    collisionMask.getG2d().fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    shadowLayer = new Offscreen(CANVAS_WIDTH, CANVAS_HEIGHT);
    Terrain.setCollisionMask(collisionMask.getImage());
    animator = Resource.getAnimators("ending").get("ending");
    animator.play();
    Audio.playMusic("ending");
  }

  @Override
  public void onExit() {
    reset();
    System.gc();
  }

  @Override
  public void fixedUpdate() {
    animator.fixedUpdate();

    if ((Input.isKeyJustPressed(KEY_START_1)
      || Input.isKeyJustPressed(KEY_START_2)
      || Input.isKeyJustPressed(KEY_CANCEL))
      && !manager.isTransiting() && animator.isFinished()) {

      Audio.stopMusic();
      manager.switchTo("ranking");
    }
  }

  @Override
  public void draw(Graphics2D g) {
    shadowLayer.clear();
    animator.drawShadow(shadowLayer, 0, 0, 0.75);
    animator.draw(g, shadowLayer, 0, 0);
  }

}
