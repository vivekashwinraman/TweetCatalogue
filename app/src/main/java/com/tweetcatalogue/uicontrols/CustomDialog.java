package com.tweetcatalogue.uicontrols;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.tweetcatalogue.R;


/**
 * Created by vraman on 11/6/15.
 */
public class CustomDialog extends Dialog {

    public CustomDialog(Context context, int layout, String title) {
        super(context, R.style.CustomDialogSlideAnim);
        this.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        this.setContentView(layout);
        this.setTitle(title);
        this.setCanceledOnTouchOutside(false);
        this.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.mipmap.twitter);
    }
}
