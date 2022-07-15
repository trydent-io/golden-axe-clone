package ax.battler.goldenaxe.infra;

import ax.battler.goldenaxe.infra.Animator.AnimatorEvent;

/**
 * AnimatorListener class.
 *
 * @author Leonardo Ono (ono.leo80@gmail.com)
 */
public interface AnimatorEventListener {

  void onAnimatorEventTriggered(AnimatorEvent event);

}
