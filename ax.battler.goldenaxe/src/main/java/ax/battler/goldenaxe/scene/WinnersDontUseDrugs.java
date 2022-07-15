package ax.battler.goldenaxe.scene;

import ax.battler.goldenaxe.infra.GoldenAxeGame;
import ax.battler.goldenaxe.infra.Input;
import ax.battler.goldenaxe.infra.Resource;
import ax.battler.goldenaxe.infra.SceneManager;
import ax.battler.goldenaxe.infra.State;
import ax.battler.goldenaxe.infra.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

import static ax.battler.goldenaxe.infra.Settings.KEY_START_1;
import static ax.battler.goldenaxe.infra.Settings.KEY_START_2;

/**
 * TestScene class.
 *
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class WinnersDontUseDrugs extends State<GoldenAxeGame, SceneManager> {

  private final BufferedImage winnersDontUseDrugs;
  private long startTime;

  public WinnersDontUseDrugs(SceneManager manager, GoldenAxeGame game) {
    super(manager, "winners_dont_use_drugs", game);
    winnersDontUseDrugs = Resource.getImage("winners_dont_use_drugs");
  }

  @Override
  public void onEnter() {
    startTime = Util.getTime() + 5000;
  }

  @Override
  public void fixedUpdate() {
    if (!manager.isTransiting()
      && (Util.getTime() >= startTime
      || Input.isKeyJustPressed(KEY_START_1)
      || Input.isKeyJustPressed(KEY_START_2))) {

      manager.switchTo("title");
    }
  }

  @Override
  public void draw(Graphics2D g) {
    g.drawImage(winnersDontUseDrugs, 0, 0, null);
  }

}
