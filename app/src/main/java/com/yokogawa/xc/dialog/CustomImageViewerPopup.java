package com.yokogawa.xc.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.lxj.xpopup.animator.PopupAnimator;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.yokogawa.xc.R;

public class CustomImageViewerPopup extends ImageViewerPopupView {
    public CustomImageViewerPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_image_viewer_popup;
    }

}
