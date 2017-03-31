package com.afunx.easytransition.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.support.annotation.Nullable;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

/**
 * MimicFade mimic android.transition.Fade.
 * Reflection is used if necessary.
 *
 * Created by afunx on 30/03/2017.
 */
public class MimicFade extends Visibility {

    private static final String PROPNAME_TRANSITION_ALPHA = "android:fade:transitionAlpha";

    private static boolean DBG = true;

    private static final String LOG_TAG = "MimicFade";

    public static final int IN = Visibility.MODE_IN;

    public static final int OUT = Visibility.MODE_OUT;

    public MimicFade(){
    }

    // Use hide method float View.getTransitionAlpha() by reflection
    private static float getTransitionAlpha(View view) {
        try {
            Method method = View.class.getDeclaredMethod("getTransitionAlpha");
            method.setAccessible(true);
            return (Float) method.invoke(view);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    // Use hide method float View.setTransitionAlpha() by reflection
    private static void setTransitionAlpha(View view, float alpha) {
        try {
            Method method = View.class.getDeclaredMethod("setTransitionAlpha", float.class);
            method.setAccessible(true);
            method.invoke(view, alpha);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        super.captureStartValues(transitionValues);
        transitionValues.values.put(PROPNAME_TRANSITION_ALPHA,
                getTransitionAlpha(transitionValues.view));
    }

    /**
     * Utility method to handle creating and running the Animator.
     */
    @Nullable
    private Animator createAnimation(final View view, float startAlpha, final float endAlpha) {
        if (startAlpha == endAlpha) {
            return null;
        }
        setTransitionAlpha(view,startAlpha);
        final ObjectAnimator anim = ObjectAnimator.ofFloat(view, "transitionAlpha", endAlpha);
        if (DBG) {
            Log.d(LOG_TAG, "Created animator " + anim);
        }
        final FadeAnimatorListener listener = new FadeAnimatorListener(view);
        anim.addListener(listener);
        anim.addPauseListener(listener);
        addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                setTransitionAlpha(view, 1);
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }
        });
        return anim;
    }

    @Override
    public Animator onAppear(ViewGroup sceneRoot, View view,
                             TransitionValues startValues,
                             TransitionValues endValues) {
        if (DBG) {
            View startView = (startValues != null) ? startValues.view : null;
            Log.d(LOG_TAG, "Fade.onAppear: startView, startVis, endView, endVis = " +
                    startView + ", " + view);
        }
        float startAlpha = getStartAlpha(startValues, 0);
        if (startAlpha == 1) {
            startAlpha = 0;
        }
        return createAnimation(view, startAlpha, 1);
    }

    @Override
    public Animator onDisappear(ViewGroup sceneRoot, final View view, TransitionValues startValues,
                                TransitionValues endValues) {
        float startAlpha = getStartAlpha(startValues, 1);
        return createAnimation(view, startAlpha, 0.5f);
    }

    private static float getStartAlpha(TransitionValues startValues, float fallbackValue) {
        float startAlpha = fallbackValue;
        if (startValues != null) {
            Float startAlphaFloat = (Float) startValues.values.get(PROPNAME_TRANSITION_ALPHA);
            if (startAlphaFloat != null) {
                startAlpha = startAlphaFloat;
            }
        }
        return startAlpha;
    }

    private static class FadeAnimatorListener extends AnimatorListenerAdapter {
        private final View mView;
        private boolean mLayerTypeChanged = false;

        private FadeAnimatorListener(View view) {
            mView = view;
        }

        @Override
        public void onAnimationStart(Animator animator) {
            if (mView.hasOverlappingRendering() && mView.getLayerType() == View.LAYER_TYPE_NONE) {
                mLayerTypeChanged = true;
                mView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            }
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            setTransitionAlpha(mView, 1);
            if (mLayerTypeChanged) {
                mView.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        }
    }
}
