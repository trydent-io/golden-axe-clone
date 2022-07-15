package ax.battler.goldenaxe.scene;

import ax.battler.goldenaxe.actor.Player.PlayerCharacter;
import ax.battler.goldenaxe.infra.Animation;
import ax.battler.goldenaxe.infra.AnimationPlayer;
import ax.battler.goldenaxe.infra.Audio;
import ax.battler.goldenaxe.infra.Background;
import ax.battler.goldenaxe.infra.GoldenAxeGame;
import ax.battler.goldenaxe.infra.Input;
import ax.battler.goldenaxe.infra.Resource;
import ax.battler.goldenaxe.infra.SceneManager;
import ax.battler.goldenaxe.infra.State;
import ax.battler.goldenaxe.infra.TextRenderer;
import ax.battler.goldenaxe.infra.Transparency;
import ax.battler.goldenaxe.infra.Util;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ax.battler.goldenaxe.actor.Player.PlayerCharacter.AX;
import static ax.battler.goldenaxe.actor.Player.PlayerCharacter.GILIUS;
import static ax.battler.goldenaxe.actor.Player.PlayerCharacter.TYRIS;
import static ax.battler.goldenaxe.infra.Direction.RIGHT;
import static ax.battler.goldenaxe.infra.Settings.KEY_CANCEL;
import static ax.battler.goldenaxe.infra.Settings.KEY_PLAYER_1_ATTACK;
import static ax.battler.goldenaxe.infra.Settings.KEY_PLAYER_1_LEFT;
import static ax.battler.goldenaxe.infra.Settings.KEY_PLAYER_1_RIGHT;
import static ax.battler.goldenaxe.infra.Settings.KEY_PLAYER_2_ATTACK;
import static ax.battler.goldenaxe.infra.Settings.KEY_PLAYER_2_LEFT;
import static ax.battler.goldenaxe.infra.Settings.KEY_PLAYER_2_RIGHT;

/**
 * SelectPlayer class.
 *
 * @author Leonardo Ono (ono.leo80@gmail.com);
 */
public class SelectPlayer extends State<GoldenAxeGame, SceneManager> {

  private final List<Avatar> avatars = new ArrayList<>();
  private Background background;
  private Background backgroundSkeleton;
  private AnimationPlayer animationPlayer;
  private Avatar[] avatarsById;
  private int[] angles;
  private int[] dxs;
  private long startGameTime;
  public SelectPlayer(SceneManager manager, GoldenAxeGame game) {
    super(manager, "select_player", game);
  }

  private void reset() {
    background = null;
    animationPlayer = null;
    avatars.clear();
    avatarsById = null;
    angles = null;
    dxs = null;
    startGameTime = -1;
  }

  @Override
  public void onEnter() {
    reset();
    background = new Background("select_player_background"
      , 0.25, 0.0, 0.0, 0.0, 1.0, 1.0, true);

    backgroundSkeleton = new Background("select_player_skeleton"
      , 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, false);

    animationPlayer = Resource.getAnimationPlayer("select_player", null);
    avatarsById = new Avatar[3];
    avatars.add(avatarsById[0] = new Avatar(AX, Math.toRadians(90)));
    avatars.add(avatarsById[1] = new Avatar(TYRIS, Math.toRadians(330)));
    avatars.add(avatarsById[2] = new Avatar(GILIUS, Math.toRadians(210)));
    angles = new int[]{0, 120};
    dxs = new int[2];
  }

  @Override
  public void onExit() {
    reset();
    System.gc();
  }

  @Override
  public void fixedUpdate() {
    background.update();
    animationPlayer.update();

    if (Input.isKeyJustPressed(KEY_CANCEL)) {
      manager.switchTo("title");
      return;
    }

    if (GoldenAxeGame.characterPlayer1 == null) {
      Avatar selectedAvatar = updateSelection(
        0, KEY_PLAYER_1_LEFT, KEY_PLAYER_1_RIGHT, KEY_PLAYER_1_ATTACK);

      if (selectedAvatar != null && !selectedAvatar.selected) {
        GoldenAxeGame.characterPlayer1 = selectedAvatar.playerCharacter;
        selectedAvatar.select(0);
      }
    }
    if (GoldenAxeGame.characterPlayer2 == null
      && GoldenAxeGame.numberOfPlayers > 1) {

      Avatar selectedAvatar = updateSelection(
        1, KEY_PLAYER_2_LEFT, KEY_PLAYER_2_RIGHT, KEY_PLAYER_2_ATTACK);

      if (selectedAvatar != null && !selectedAvatar.selected) {
        GoldenAxeGame.characterPlayer2 = selectedAvatar.playerCharacter;
        selectedAvatar.select(1);
      }
    }

    int maxPlayers = GoldenAxeGame.numberOfPlayers;
    for (int playerIndex = 0; playerIndex < maxPlayers; playerIndex++) {
      for (Avatar avatar : avatars) {
        avatar.update(playerIndex);
      }
    }

    if (startGameTime < 0
      && ((GoldenAxeGame.numberOfPlayers == 1
      && GoldenAxeGame.characterPlayer1 != null)
      || (GoldenAxeGame.numberOfPlayers > 1
      && GoldenAxeGame.characterPlayer1 != null
      && GoldenAxeGame.characterPlayer2 != null))) {

      startGameTime = Util.getTime() + 1000;
    } else if (startGameTime > 0 && Util.getTime() >= startGameTime) {
      manager.switchTo("stage");
    }
  }

  private Avatar updateSelection(
    int playerIndex, int leftKey, int rightKey, int fireKey) {

    if (angles[playerIndex] % 120 == 0) {
      Avatar selectedAvatar = avatarsById[angles[playerIndex] / 120];
      if (dxs[playerIndex] != 0 && !selectedAvatar.selected) {
        dxs[playerIndex] = 0;
      }
      if (dxs[playerIndex] == 0 && selectedAvatar.selected) {
        dxs[playerIndex] = 3;
        return null;
      }
      if (dxs[playerIndex] == 0
        && Input.isKeyJustPressed(fireKey)) {

        return selectedAvatar;
      }
    }

    if (Input.isKeyPressed(leftKey)) {
      dxs[playerIndex] = -3;
    } else if (Input.isKeyPressed(rightKey)) {
      dxs[playerIndex] = 3;
    }

    angles[playerIndex] += dxs[playerIndex];
    angles[playerIndex] = angles[playerIndex] % 360;
    if (angles[playerIndex] < 0) {
      angles[playerIndex] += 360;
    }
    return null;
  }

  @Override
  public void draw(Graphics2D g) {
    background.draw(g, 0.0, 0.0);
    backgroundSkeleton.draw(g, 0.0, 0.0);
    int maxPlayers = GoldenAxeGame.numberOfPlayers;
    for (int playerIndex = 0; playerIndex < maxPlayers; playerIndex++) {
      for (Avatar avatar : avatars) {
        avatar.updateAngle(angles[playerIndex]);
      }
      Collections.sort(avatars);
      for (Avatar avatar : avatars) {
        avatar.draw(g, playerIndex, 70.0 + 137 * playerIndex, 160.0);
      }
      if (angles[playerIndex] % 120 == 0) {
        Avatar selectedAvatar = avatarsById[angles[playerIndex] / 120];
        if (!selectedAvatar.selected) {
          String avatarName = selectedAvatar.playerCharacter.name();
          int fixCenter = avatarName.length() / 2;
          TextRenderer.draw(g, avatarName
            , 9 + 17 * playerIndex - fixCenter, 8);
        }
      }
    }
  }

  private class Avatar implements Comparable<Avatar> {

    String id;
    PlayerCharacter playerCharacter;
    double x;
    double z;
    double angleOffset;
    double scale;
    boolean selected;
    boolean[] burn = new boolean[2];
    double[] burnFrame = new double[2];

    Avatar(PlayerCharacter playerCharacter, double angleOffset) {
      this.id = playerCharacter.name().toLowerCase();
      this.playerCharacter = playerCharacter;
      this.angleOffset = angleOffset;
    }

    void update(int currentPlayerIndex) {
      if (burn[currentPlayerIndex]) {
        burnFrame[currentPlayerIndex] += 0.25;
        Animation animation = animationPlayer.getAnimation("burning");
        if (burnFrame[currentPlayerIndex]
          >= animation.getFramesCount() - 0.01) {

          burnFrame[currentPlayerIndex] =
            animation.getFramesCount() - 0.01;
        }
      }
    }

    void updateAngle(int angleInDeg) {
      double angle = Math.toRadians(angleInDeg);
      x = 24 * Math.cos(angle + angleOffset);
      z = 14 * Math.sin(angle + angleOffset);
      scale = 0.6 + 0.4 * ((z + 16) / 32.0);
    }

    void draw(Graphics2D g, int currentPlayerIndex
      , double offsetX, double offsetZ) {

      double scaleTmp = scale;
      if (burn[currentPlayerIndex]) {
        scaleTmp += 0.1;
        animationPlayer.setAnimation("burning", false);
        animationPlayer.setCurrentAnimationFrameIndex(
          burnFrame[currentPlayerIndex]);
      } else {
        animationPlayer.setAnimation(id, false);
      }

      Composite originalComposite = g.getComposite();
      g.setComposite(Transparency.getComposite((int) (255 * scale)));
      animationPlayer.draw(g, x + offsetX, 0.0, z + offsetZ
        , 0, 0, RIGHT, false, scaleTmp, scaleTmp, 0.0);

      g.setComposite(originalComposite);
    }

    void select(int playerIndex) {
      selected = true;
      burn[playerIndex ^ 1] = true;
      avatars.forEach(avatar -> {
        if (avatar != Avatar.this) {
          avatar.burn[playerIndex] = true;
        }
      });
      Audio.playSound("char_selected");
    }

    @Override
    public int compareTo(Avatar o) {
      return (int) Math.signum(z - o.z);
    }

  }

}
