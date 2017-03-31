package com.afunx.easytransition.model;

import android.transition.Transition;
import android.util.SparseArray;

import com.afunx.easytransition.transition.MimicFade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by afunx on 29/03/2017.
 */

public class TransitionItems {


    public static List<TransitionItem> ITEMS = new ArrayList<>();

    public static SparseArray<TransitionItem> ITEMS_MAP = new SparseArray<>();

    private static void addItem(TransitionItem item) {
        ITEMS.add(item);
        ITEMS_MAP.put(item.id, item);
    }

    static {
        addItem(new TransitionItem(1, android.transition.Explode.class));
        addItem(new TransitionItem(2, android.transition.Fade.class));
        addItem(new TransitionItem(3, android.transition.Slide.class));
        addItem(new TransitionItem(4, android.transition.AutoTransition.class));
        addItem(new TransitionItem(5, MimicFade.class));
    }

    public static class TransitionItem {
        private int id;
        private Class clazz;

        public TransitionItem(int id, Class clazz) {
            this.id = id;
            this.clazz = clazz;
        }

        public int getId() {
            return id;
        }

        public String getItemName() {
            return clazz.getSimpleName();
        }

        public String getClassName() {
            return clazz.getName();
        }

        public Transition createTransition() {
            try {
                return (Transition) clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public String toString() {
            return getItemName();
        }
    }
}
