package com.yokogawa.xc.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.Rotate;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.XPopupImageLoader;
import com.lxj.xpopup.photoview.OnMatrixChangedListener;
import com.lxj.xpopup.photoview.PhotoView;
import com.lxj.xpopup.util.ImageDownloadTarget;
import com.lxj.xpopup.util.SSIVListener;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.File;

public class SmartNewGlideImageLoader implements XPopupImageLoader {


    private int errImg;
    private boolean mBigImage;

    public SmartNewGlideImageLoader() {
    }

    public SmartNewGlideImageLoader(int errImgRes) {
        errImg = errImgRes;
    }

    public SmartNewGlideImageLoader(boolean bigImage, int errImgRes) {
        this(errImgRes);
        mBigImage = bigImage;
    }


    @Override
    public void loadSnapshot(@NonNull Object uri, @NonNull PhotoView snapshot, @Nullable ImageView srcView) {
        if (mBigImage) {
//            if (srcView != null && srcView.getDrawable() != null) {
//                try {
//                    snapshot.setImageDrawable(srcView.getDrawable().getConstantState().newDrawable());
//                } catch (Exception e) {
//                }
//            }
//            Glide.with(snapshot).downloadOnly().load(uri)
//                    .into(new ImageDownloadTarget() {
//                        @Override
//                        public void onLoadFailed(Drawable errorDrawable) {
//                            super.onLoadFailed(errorDrawable);
//                        }
//
//                        @Override
//                        public void onResourceReady(@NonNull File resource, Transition<? super File> transition) {
//                            super.onResourceReady(resource, transition);
//                            int degree = XPopupUtils.getRotateDegree(resource.getAbsolutePath());
//                            int maxW = XPopupUtils.getAppWidth(snapshot.getContext());
//                            int maxH = XPopupUtils.getScreenHeight(snapshot.getContext());
//                            int[] size = XPopupUtils.getImageSize(resource);
//                            if (size[0] > maxW || size[1] > maxH) {
//                                //缩放加载
//                                Bitmap rawBmp = XPopupUtils.getBitmap(resource, maxW, maxH);
//                                snapshot.setImageBitmap(XPopupUtils.rotate(rawBmp, degree, size[0] / 2f, size[1] / 2f));
//                                Log.e("TAG", "onResourceReady: ===if");
//                            } else {
//                                Glide.with(snapshot).load(resource).apply(new RequestOptions().override(600, 350)).into(snapshot);
//                                Log.e("TAG", "onResourceReady: ===else  0==" + size[0] + "   1==" + size[1]);
//                            }
//                        }
//                    });
        } else {
            Glide.with(snapshot).load(uri).override(Target.SIZE_ORIGINAL).into(snapshot);
        }
    }

    @Override
    public View loadImage(int position, @NonNull Object url,
                          @NonNull ImageViewerPopupView popupView, @NonNull PhotoView snapshot,
                          @NonNull ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        final View imageView = mBigImage ? buildBigImageView(popupView, progressBar, position)
                : buildPhotoView(popupView, snapshot, position);
        final Context context = imageView.getContext();
        if (snapshot != null && snapshot.getDrawable() != null && ((int) snapshot.getTag()) == position) {
            if (imageView instanceof PhotoView) {
                try {
                    ((PhotoView) imageView).setImageDrawable(snapshot.getDrawable().getConstantState().newDrawable());
                } catch (Exception e) {
                }
            } else {
                ((SubsamplingScaleImageView) imageView).setImage(ImageSource.bitmap(XPopupUtils.view2Bitmap(snapshot)));
            }
        }
        Glide.with(imageView).downloadOnly().load(url)
                .into(new ImageDownloadTarget() {
                    @Override
                    public void onLoadFailed(Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        progressBar.setVisibility(View.GONE);
                        if (imageView instanceof PhotoView) {
                            ((PhotoView) imageView).setImageResource(errImg);
                            ((PhotoView) imageView).setZoomable(false);
                        } else {
                            ((SubsamplingScaleImageView) imageView).setImage(ImageSource.resource(errImg));
                        }
                    }

                    @Override
                    public void onResourceReady(@NonNull File resource, Transition<? super File> transition) {
                        super.onResourceReady(resource, transition);
                        int maxW = XPopupUtils.getScreenHeight(context);
                        int maxH = XPopupUtils.getScreenWidth(context);

                        int[] size = getImageSize(resource);
                        int degree = XPopupUtils.getRotateDegree(resource.getAbsolutePath());
                        //photo view加载
                        if (imageView instanceof PhotoView) {
                            progressBar.setVisibility(View.GONE);
                            ((PhotoView) imageView).setZoomable(true);
                            Log.e("TAG", "onResourceReady!   0=" + size[0] + "  1=" + size[1]);
                            Log.e("TAG", "onResourceReady!   0 screen=" + maxW + "  1 screen=" + maxH);
                            if (true) {

                                //TODO: 可能导致大图GIF展示不出来
                                Bitmap rawBmp = getBitmap(resource, 600, 600);
                                Log.e("TAG", "onResourceReady!   000=" + rawBmp.getWidth() + "  111=" + rawBmp.getHeight());
                                ((PhotoView) imageView).setImageBitmap(XPopupUtils.rotate(rawBmp, degree, 500, 500));
                            } else {
                                Glide.with(imageView).load(resource)
                                        .transform(new Rotate(degree))
                                        .apply(new RequestOptions().error(errImg).override(size[0], size[1])).into(((PhotoView) imageView));
                            }
                        } else {
                            //大图加载
                            SubsamplingScaleImageView bigImageView = (SubsamplingScaleImageView) imageView;
                            boolean longImage = false;
                            if (size[1] * 1f / size[0] > XPopupUtils.getScreenHeight(context) * 1f / XPopupUtils.getAppWidth(context)) {
                                longImage = true;
                                bigImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_START);
                            } else {
                                longImage = false;
                                bigImageView.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_INSIDE);
                            }
                            bigImageView.setOrientation(degree);
                            bigImageView.setOnImageEventListener(new SSIVListener(bigImageView, progressBar, errImg, longImage));
                            Bitmap preview = XPopupUtils.getBitmap(resource, XPopupUtils.getAppWidth(context), XPopupUtils.getScreenHeight(context));
                            bigImageView.setImage(ImageSource.uri(Uri.fromFile(resource)).dimensions(size[0], size[1]),
                                    ImageSource.cachedBitmap(preview));
                        }
                    }
                });
        return imageView;
    }


    public static int[] getImageSize(File file) {
        if (file == null) return new int[]{0, 0};
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), opts);
        return new int[]{500, 500};
    }

    public static Bitmap getBitmap(final File file, final int maxWidth, final int maxHeight) {
        if (file == null) return null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        options.inJustDecodeBounds = false;
        options.outHeight = 500;
        options.outWidth = 500;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    public static int calculateInSampleSize(final BitmapFactory.Options options,
                                            final int maxWidth,
                                            final int maxHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        Log.e("TAG", "options: width==" + width + "height==" + height);
        int inSampleSize = 1;
        while (height > maxHeight || width > maxWidth) {
            height >>= 1;
            width >>= 1;
            inSampleSize <<= 1;
            Log.e("TAG", "calculateInSampleSize: width==" + width + "height==" + height);
            Log.e("TAG", "calculateInSampleSize: inSampleSize==" + inSampleSize);
        }
        return inSampleSize;
    }

    @Override
    public File getImageFile(@NonNull Context context, @NonNull Object uri) {
        try {
            return Glide.with(context).downloadOnly().load(uri).submit().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private PhotoView buildPhotoView(final ImageViewerPopupView popupView, final PhotoView snapshotView, final int realPosition) {
        final PhotoView photoView = new PhotoView(popupView.getContext());
        photoView.setZoomable(false);
        photoView.setOnMatrixChangeListener(new OnMatrixChangedListener() {
            @Override
            public void onMatrixChanged(RectF rect) {
                if (snapshotView != null) {
                    Matrix matrix = new Matrix();
                    photoView.getSuppMatrix(matrix);
                    snapshotView.setSuppMatrix(matrix);
                }
            }
        });
        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupView.dismiss();
            }
        });
        if (popupView.longPressListener != null) {
            photoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    popupView.longPressListener.onLongPressed(popupView, realPosition);
                    return false;
                }
            });
        }
        return photoView;
    }


    private SubsamplingScaleImageView buildBigImageView(
            final ImageViewerPopupView popupView, ProgressBar progressBar, final int realPosition) {
        final SubsamplingScaleImageView ssiv = new SubsamplingScaleImageView(popupView.getContext());
        ssiv.setOnStateChangedListener(new SubsamplingScaleImageView.DefaultOnStateChangedListener() {
            @Override
            public void onCenterChanged(PointF newCenter, int origin) {
                super.onCenterChanged(newCenter, origin);
                //TODO 同步SubsamplingScaleImageView的滚动给snapshot
//                    Log.e("tag", "y: " + newCenter.y   + " vh: "+ ssiv.getMeasuredHeight()
//                    + "  dy: "+ (newCenter.y - ssiv.getMeasuredHeight()/2));
            }
        });
        ssiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupView.dismiss();
            }
        });
        if (popupView.longPressListener != null) {
            ssiv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    popupView.longPressListener.onLongPressed(popupView, realPosition);
                    return false;
                }
            });
        }
        return ssiv;
    }
}
