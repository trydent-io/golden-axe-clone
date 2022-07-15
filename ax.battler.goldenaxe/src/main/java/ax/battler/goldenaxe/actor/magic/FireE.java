package ax.battler.goldenaxe.actor.magic;

import ax.battler.goldenaxe.actor.Projectile;
import ax.battler.goldenaxe.actor.Projectile.ProjectileState;
import ax.battler.goldenaxe.infra.Actor;
import ax.battler.goldenaxe.infra.Animation;
import ax.battler.goldenaxe.infra.Audio;
import ax.battler.goldenaxe.infra.Camera;
import ax.battler.goldenaxe.infra.Collider;
import ax.battler.goldenaxe.infra.Magic;
import ax.battler.goldenaxe.infra.Terrain;
import ax.battler.goldenaxe.infra.Util;
import ax.battler.goldenaxe.scene.Stage;

import java.util.ArrayList;
import java.util.List;

import static ax.battler.goldenaxe.infra.Settings.CANVAS_WIDTH;

/**
 * FireE (magic) class.
 *
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class FireE extends Magic {

  private static final int MAX_FIRE_BALL_COUNT = 20;
  private final List<Projectile> fireBalls = new ArrayList<>();
  private Projectile lava;
  private long nextSpawnFireBallTime;

  public FireE(Stage stage, Actor owner) {
    super(stage, "magic_fire_e", owner);
  }

  @Override
  public void create() {
    consumedPotions = 7;
    collider = new Collider(0, 0, 1, 1, 60, 6);

    lava = new Projectile(stage
      , "magic_lava", "magic_fire", "fire_lava");

    elements.add(lava);

    for (int i = 0; i < MAX_FIRE_BALL_COUNT; i++) {
      Projectile fireBall = new Projectile(stage
        , "fire_ball_" + i, "magic_fire", "empty");

      fireBall.setDestroyed(true);
      elements.add(fireBall);
      fireBalls.add(fireBall);
    }
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    // spawn fire balls
    if (Util.getTime() < finishedTime - 1000) {

      for (Projectile rock : fireBalls) {
        if (rock.isDestroyed()
          && Util.getTime() >= nextSpawnFireBallTime) {

          ProjectileState projectileState =
            (ProjectileState) rock.getStateManager()
              .getState("animating");

          projectileState.setStateAnimationId("fire_ball");
          Animation animation =
            rock.getAnimationPlayer().getAnimation("fire_ball");

          animation.setParameter("vx", "" + Util.random(-2, 2));
          animation.setParameter("vy", "" + Util.random(-7, -4));
          animation.setParameter("vz", "0.0");

          rock.spawn(owner, direction
            , owner.getWx(), owner.getWy(), owner.getWz());

          rock.setWx(lava.getWx() + Util.random(-16, 16));
          rock.setWz(lava.getWz() + 1.0);
          rock.setWy(
            lava.getWy() - 64.0 + Util.random(-16, 16));

          nextSpawnFireBallTime = Util.getTime() + 200;
        }
      }
    }
  }

  @Override
  public void use() {
    destroyed = false;
    stage.addActor(this);
    lava.spawn(owner, direction
      , owner.getWx(), owner.getWy(), owner.getWz());

    lava.setWx(Camera.getX() + CANVAS_WIDTH / 2);
    double ez = Terrain.getMagicPathZ((int) lava.getWx());
    lava.setWz(ez);

    double ey = Terrain.getHeight(
      lava.getWx(), lava.getWz());

    if (ey < Terrain.getDeathHeight()) {
      lava.setWy(ey);
    }

    Audio.playSound("magic_fire_e");
    nextSpawnFireBallTime = Util.getTime() + 100;
    finishedTime = Util.getTime() + 4000;
  }

}
