package com.example.android.popcorn.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;

public class ApiUtils {
    public static Drawable getDrawableApiSave(Context context, int id) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            drawable = context.getResources().getDrawable(id);
        } else {
            drawable = context.getDrawable(id);
        }
        return drawable;
    }
}
