package com.yokogawa.xc.ui.act;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.permissionx.guolindev.PermissionX;
import com.yokogawa.xc.R;
import com.yokogawa.xc.base.BaseActivity;
import com.yokogawa.xc.event.RefresToken;
import com.yokogawa.xc.utils.BitmapAndStringUtils;
import com.yokogawa.xc.utils.SpUtils;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.view.LinePathView;
import com.yokogawa.xc.worknet.HttpResult;
import com.yokogawa.xc.worknet.ResultCallback;
import com.yokogawa.xc.worknet.RetrofitNet;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class DrawNameActivity extends BaseActivity {

    private LinePathView draName;
    private ImageView ivName, iv_StrImage;

    @Override
    public int getLayoutId() {
        return R.layout.act_draw_name;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initView() {
        findViewById(R.id.tv_back).setOnClickListener(view -> finish());
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("制作签名");
        draName = findViewById(R.id.draName);
        draName.setPenColor(Color.BLACK);
        ivName = findViewById(R.id.iv_name);
        iv_StrImage = findViewById(R.id.iv_StrImage);
        findViewById(R.id.bt_save).setOnClickListener(view -> {
            //判断权限
            permission();
        });
        findViewById(R.id.bt_clear).setOnClickListener(view -> draName.clear());
        findViewById(R.id.bt_Saveclear).setOnClickListener(view -> {
            SpUtils.getInstance(this).remove("signImageString");

        });
        String signImageString = SpUtils.getInstance(this).getString("signImageString", "");
        if (!signImageString.isEmpty()) {
            Bitmap bitmap = BitmapAndStringUtils.convertStringToIcon(signImageString);
            if (bitmap != null) {
                iv_StrImage.setImageBitmap(bitmap);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void permission() {
        PermissionX.init(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .explainReasonBeforeRequest()
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        try {
                            String uri = draName.saveImage(DrawNameActivity.this, false, 0);
                            //保存成图片，上传到服务器
                            uploadHeadImage(uri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Bitmap bitMap = draName.saveToBitmap(true, 100);
                        ivName.setImageBitmap(bitMap);
                        //将bitmap转成字符串
                        String ImageBitmapString = BitmapAndStringUtils.convertIconToString(bitMap);
//                        Log.e("TAG", "permission: " + ImageBitmapString);
                        SpUtils.getInstance(this).save("signImageString", ImageBitmapString);
                        Bitmap bitmap = BitmapAndStringUtils.convertStringToIcon(ImageBitmapString);
                        if (bitmap != null) {
                            iv_StrImage.setImageBitmap(bitmap);
                        }
                    } else {
                        Toast.makeText(DrawNameActivity.this, "这些权限被拒绝：" + deniedList.toString(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    public void uploadHeadImage(String path) {
        File file = new File(path);
        Log.e("TAG", "uploadHeadImage: File=====" + file.getAbsolutePath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RetrofitNet.getInstance().getApi().updateSign(getTokenMsg(), body)
                .enqueue(new ResultCallback<HttpResult<String>>() {
                    @Override
                    public void onSuccess(Response<HttpResult<String>> response) {
                        T.show( "上传成功");
                        EventBus.getDefault().post(new RefresToken());
                        finish();
                    }

                    @Override
                    public void onFail(String message) {
                        T.show("上传失败" + message);
                    }

                });
    }
}
