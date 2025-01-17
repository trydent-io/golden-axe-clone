package ax.battler.goldenaxe.actor.magic;

import ax.battler.goldenaxe.infra.Actor;
import ax.battler.goldenaxe.infra.Animator;
import ax.battler.goldenaxe.infra.Collider;
import ax.battler.goldenaxe.infra.Magic;
import ax.battler.goldenaxe.infra.Resource;
import ax.battler.goldenaxe.scene.Stage;

import java.awt.*;

/**
 * FireC (magic) class.
 *
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public class FireC extends Magic {

  private final Animator animator;

  public FireC(Stage stage, Actor owner) {
    super(stage, "magic_fire_c", owner);

    animator = Resource.getAnimators("magic_fire_c").get("magic_fire_c");
  }

  @Override
  public void create() {
    consumedPotions = 6;
    collider = new Collider(0, 0, 1, 1, 30, 3);
  }

  @Override
  public boolean isFinished() {
    return animator.isFinished();
  }


  @Override
  public void fixedUpdate() {
    super.fixedUpdate();
    animator.fixedUpdate();
    wz = 9999;
  }

  @Override
  public void draw(Graphics2D g, int cameraX, int cameraY) {
    animator.draw(g, null, 0, 0);
  }

  @Override
  public void use() {
    animator.play();
    destroyed = false;
    stage.addActor(this);
  }

}
