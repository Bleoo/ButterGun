package io.github.bleoo.butter;

import android.app.Activity;
import android.support.annotation.NonNull;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by bleoo on 2017/9/30.
 */

public class Butter {

    private Butter() {
        throw new AssertionError("No instances.");
    }

    /**
     * 绑定 Activity
     *
     * @param activity 目标为 Activity
     */
    public static void bind(@NonNull Activity activity) {
        try {
            String qualifiedName = activity.getClass().getName();
            Class generateClass = Class.forName(qualifiedName + "_ViewBinding");
            generateClass.getConstructor(activity.getClass()).newInstance(activity);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
