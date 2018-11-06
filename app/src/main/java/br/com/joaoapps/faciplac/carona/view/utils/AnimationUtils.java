package br.com.joaoapps.faciplac.carona.view.utils;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;

import com.joaov.faciplac.caronasolidaria.R;


public class AnimationUtils {
    public static final String TRANSITION_X = "X";
    public static final String TRANSITION_Y = "Y";


    public static void startAnimation(View view, long timeDuration, long qtdMove, String positionTransition, OnAnimationListener onAnimationListener) {
        startAnimation(view, timeDuration, qtdMove, 1f, positionTransition, onAnimationListener);
    }

    public static void startAnimation(View view, long timeDuration, long qtdMove, String positionTransition) {
        startAnimation(view, timeDuration, qtdMove, 1f, positionTransition, null);
    }

    public static void startAnimation(View view, long timeDuration, long qtdMove, float alpha, String positionTransition, final OnAnimationListener onAnimationListener) {

        ViewPropertyAnimator viewPropertyAnimator = view.animate()
                .setDuration(timeDuration)
                .alpha(alpha)
                .setInterpolator(new AccelerateInterpolator());

        if (positionTransition.equals(TRANSITION_X)) {
            viewPropertyAnimator.translationX(qtdMove);
        } else {
            viewPropertyAnimator.translationY(qtdMove);
        }

        if (onAnimationListener != null) {
            viewPropertyAnimator.setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    onAnimationListener.animationFinalied();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }

        viewPropertyAnimator.start();
    }


    public static void setVisible(ViewGroup header, View view) {
        view.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(header);
        }
        view.setVisibility(View.VISIBLE);
    }

    public static void setInvisible(ViewGroup viewGroup, View view) {
        view.setVisibility(View.INVISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(viewGroup);
        }
        view.setVisibility(View.GONE);
    }

    public interface OnAnimationListener {
        void animationFinalied();
    }


    public static void changeAlpha(float alpha, final float toAlpha, int duration, final View view) {
        Animation fadeIn = new AlphaAnimation(alpha, toAlpha);
        fadeIn.setDuration(duration);
        AnimationSet animation = new AnimationSet(true);
        animation.addAnimation(fadeIn);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setAlpha(toAlpha);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.setAnimation(animation);

    }

    public static void configAnimatinoRecyclerView(Context context, RecyclerView recyclerView) {
        final LayoutAnimationController controller = android.view.animation.AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
    }


}
