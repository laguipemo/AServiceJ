package net.iessanclemente.a19lazaropm.aservice.utils;

import android.widget.PopupMenu;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class UtilsMenu {

    public static void setForceShowIcon(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> popupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method method = popupHelper.getMethod("setForceShowIcon", boolean.class);
                    method.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
