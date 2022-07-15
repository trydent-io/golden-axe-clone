package ax.battler.goldenaxe.infra;

import ax.battler.goldenaxe.actor.Player;
import ax.battler.goldenaxe.actor.Player.PlayerCharacter;
import ax.battler.goldenaxe.scene.Disclaimer;
import ax.battler.goldenaxe.scene.Ending;
import ax.battler.goldenaxe.scene.Initializing;
import ax.battler.goldenaxe.scene.Intro;
import ax.battler.goldenaxe.scene.OLPresents;
import ax.battler.goldenaxe.scene.OldMap;
import ax.battler.goldenaxe.scene.Ranking;
import ax.battler.goldenaxe.scene.SelectPlayer;
import ax.battler.goldenaxe.scene.Stage;
import ax.battler.goldenaxe.scene.Title;
import ax.battler.goldenaxe.scene.WinnersDontUseDrugs;

import java.awt.*;

import static ax.battler.goldenaxe.actor.Player.PlayerCharacter.AX;
import static ax.battler.goldenaxe.infra.Settings.CANVAS_HEIGHT;
import static ax.battler.goldenaxe.infra.Settings.CANVAS_WIDTH;

/**
 * GoldenAxeGame class.
 *
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class GoldenAxeGame {

  public static int numberOfPlayers = 1;

  public static PlayerCharacter characterPlayer1;
  public static PlayerCharacter characterPlayer2;

  public static Player player1;
  public static Player player2;

  public static int stage = 0;
  public static String stageId;

  public static int introIndex = 0;
  public static boolean showingDemo = false;
  public static PlayerCharacter demoCharacter = AX;
  public static int demoPotionsCount = 2;
  public static int demoStage = 100;
  private final SceneManager sceneManager = new SceneManager();

  public static void reset() {
    numberOfPlayers = 1;
    characterPlayer1 = null;
    characterPlayer2 = null;
    player1 = null;
    player2 = null;
    stage = 0;
    stageId = "";
    introIndex = -1;
    showingDemo = false;
  }

  // ---

  public static void setNextDemoCharacter() {
    introIndex++;
    if (introIndex > 2) {
      return;
    }
    showingDemo = true;
    demoCharacter = PlayerCharacter.values()[introIndex];
    demoPotionsCount = Util.random(1, demoCharacter.maxPotions);
    demoStage = 100 + introIndex;
  }

  public void start() {
    Audio.start();

    sceneManager.addState(new Initializing(sceneManager, this));
    sceneManager.addState(new OLPresents(sceneManager, this));
    sceneManager.addState(new Disclaimer(sceneManager, this));
    sceneManager.addState(new WinnersDontUseDrugs(sceneManager, this));
    sceneManager.addState(new Title(sceneManager, this));
    sceneManager.addState(new Intro(sceneManager, this));
    sceneManager.addState(new SelectPlayer(sceneManager, this));
    sceneManager.addState(new Stage(sceneManager, this));
    sceneManager.addState(new OldMap(sceneManager, this));
    sceneManager.addState(new Ending(sceneManager, this));
    sceneManager.addState(new Ranking(sceneManager, this));
    sceneManager.startAll();

    sceneManager.switchTo("initializing");
    //sceneManager.switchTo("ol_presents");
    //sceneManager.switchTo("title");
    //sceneManager.switchTo("intro");
    //sceneManager.switchTo("select_player");
    //sceneManager.switchTo("stage");
    //sceneManager.switchTo("old_map");
    //sceneManager.switchTo("ending");
    //sceneManager.switchTo("ranking");
  }

  public void update(long delta) {
    sceneManager.update(delta);
  }

  public void fixedUpdate() {
    Audio.update();
    sceneManager.fixedUpdate();
  }

  public void draw(Graphics2D g) {
    g.setBackground(Color.BLACK);
    g.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
    sceneManager.draw(g);
  }

}
