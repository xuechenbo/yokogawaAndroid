package com.yokogawa.xc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.ImageViewerPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.lxj.xpopup.util.SmartGlideImageLoader;
import com.yokogawa.xc.R;
import com.yokogawa.xc.bean.NewExamBean;
import com.yokogawa.xc.dialog.CustomImageViewerPopup;
import com.yokogawa.xc.utils.CompareUtls;
import com.yokogawa.xc.utils.DateUtil;
import com.yokogawa.xc.utils.ExamUtils;
import com.yokogawa.xc.utils.MethodInputUtil;
import com.yokogawa.xc.utils.ModelTypeUtils;
import com.yokogawa.xc.utils.SmartNewGlideImageLoader;
import com.yokogawa.xc.utils.T;
import com.yokogawa.xc.utils.Utils;
import com.yokogawa.xc.view.CustomPopup;
import com.yokogawa.xc.view.NewExamIntegrationView;
import com.yokogawa.xc.view.RemarkLayout;

import java.util.List;

public class FormalExamItemAdapter extends BaseQuickAdapter<NewExamBean.Project.Check.Children, BaseViewHolder> {
    private boolean isEnable = true;
    List<Boolean> columnNameIsShow;
    private Context context;
    private String[] ArrayMode;

    public FormalExamItemAdapter(@Nullable List<NewExamBean.Project.Check.Children> data, boolean isEnable, List<Boolean> columnNameIsShow, Context context, String modeName) {
        //TODO  还是不要修改了。
        super(R.layout.formal_exam_item, data);
        this.isEnable = isEnable;
        this.columnNameIsShow = columnNameIsShow;
        this.context = context;
        ArrayMode = ModelTypeUtils.getModeArray(ModelTypeUtils.getModelHeader(modeName), modeName);
    }


    public void showBigImagePopup(ImageView imageView, String url) {
        Log.e(TAG, "showBigImagePopupUrl==" + url);
//        ImageViewerPopupView imageViewerPopupView = new XPopup.Builder(context)
//                .animationDuration(0)
//                .asImageViewer(imageView, url, new SmartGlideImageLoader());
//        imageViewerPopupView.show();

        //自定义的弹窗需要用asCustom来显示，之前的asImageViewer这些方法当然不能用了。
        CustomImageViewerPopup viewerPopup = new CustomImageViewerPopup(context);
        //自定义的ImageViewer弹窗需要自己手动设置相应的属性，必须设置的有srcView，url和imageLoader。
        viewerPopup.setSingleSrcView(imageView, url);
        viewerPopup.setXPopupImageLoader(new SmartNewGlideImageLoader(true, 0));
//        viewerPopup.setXPopupImageLoader(new SmartGlideImageLoader(false,0));
        new XPopup.Builder(context)
                .asCustom(viewerPopup)
                .show();
    }

    @Override
    protected void convert(BaseViewHolder helper, NewExamBean.Project.Check.Children item) {
        try {
            //会导致 复用item没有answer字段,管理系统出现问题
//            helper.setIsRecyclable(false);
            NewExamIntegrationView examIntegrationView = helper.getView(R.id.examView);
            TextView missView = helper.getView(R.id.ed_small);
            if (columnNameIsShow.get(3)) {
                helper.setGone(R.id.con_specil, true)
                        .setText(R.id.tv_specil, item.getSpec());
                examIntegrationView.setPadding(130, 8, 0, 8);
            } else {
                examIntegrationView.setPadding(15, 8, 0, 8);
            }
            if (columnNameIsShow.get(5)) {
                helper.setGone(R.id.lin, true)
                        .setGone(R.id.lin_RemarkBench, true);
//                    .setText(R.id.remark, item.getAttention());
                RemarkLayout remarkLayout = helper.getView(R.id.remarkLayout);
                remarkLayout.setData(item.getAttention(), item.getAttentionUrl());
                remarkLayout.setOnExChangeListener(new RemarkLayout.OnExamItemChangeListener() {
                    @Override
                    public void imageClickListener(@Nullable AppCompatImageView imageView, int tag1) {
                        int tag = (int) imageView.getTag();
                        showBigImagePopup(imageView, item.getAttentionUrl().get(tag));
                    }
                });
            } else {
                helper.setGone(R.id.lin, false);
            }
            examIntegrationView.setData(item.getChildrenContent(), isEnable, item.getCheckType(), item.getRemark(), item.getImageUrl(), item.getShowValue());
            List<String> result = examIntegrationView.getResult(item.getAnswer());
            item.setAnswer(Utils.FortListToString(result));
            examIntegrationView.setAnswer(result, item.getIsNG());
            helper.getView(R.id.con1).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
//                final XPopup.Builder builder = new XPopup.Builder(context)
//                        .watchView(helper.getView(R.id.con1));
//                view.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        builder.asAttachList(new String[]{"置顶", "复制", "删除"}, null,
//                                new OnSelectListener() {
//                                    @Override
//                                    public void onSelect(int position, String text) {
//                                        Log.e(TAG, "onSelect: ====" + text);
//                                    }
//                                })
//                                .show();
//                        return false;
//                    }
//                });
                    return false;
                }
            });

            examIntegrationView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MethodInputUtil.hideSoftInput(context, examIntegrationView);
                }
            });

            examIntegrationView.setOnExChangeListener(new NewExamIntegrationView.OnExamItemChangeListener() {

                //TODO 图片点击
                @Override
                public void imageClickListener(@Nullable AppCompatImageView imageView, int tag1) {
                    Log.e(TAG, "imageClickListener:====" + imageView.getTag() + "        remark====" + item.getImageUrl().size());
                    int tag = (int) imageView.getTag();
                    showBigImagePopup(imageView, item.getImageUrl().get(tag));
                }

                //TODO 下拉弹窗
                @Override
                public void OnButtonClick1(@Nullable AppCompatEditText editText) {
                    int tag = (int) editText.getTag();
                    String modeRuleNumber = item.getModeRuleNumber();
                    //通过modeRuleNumber 获取型名位数。
                    //TODO 如果有规则，判断是否ng
//                new XPopup.Builder(context)
//                        .atView(editText)  // 依附于所点击的View
//                        .asAttachList(item.getRuleContent().split(","), new int[]{}, new OnSelectListener() {
//                            @Override
//                            public void onSelect(int position, String text) {
//                                editText.setText(text);
//                            }
//                        }).show();
                }

                //TODO EditText获取到焦点
                @Override
                public void onHasFocus(@Nullable AppCompatEditText editText, boolean b) {
                    int id = editText.getId();
                    if (id == 0) {
                        return;
                    }
                    //前后联动
                    if (item.getCheckType().contains("前后联动")) {
                        String contStr = "";
                        //固定判断为  数据比较,前后联动
                        for (int i = 0; i < examIntegrationView.getChildCount(); i++) {
                            View childAt = examIntegrationView.getChildAt(i);
                            if (childAt instanceof EditText) {
                                if (childAt.getId() == 0) {
                                    contStr = ((EditText) childAt).getText().toString();
                                } else if (childAt.getId() == id) {
                                    if (((EditText) childAt).getText().toString().trim().isEmpty()) {
                                        ((EditText) childAt).setText(contStr);
                                    }
                                }
                            }
                        }
                    }
                }


                //TODO EditText失去焦点
                @Override
                public void onNoHasFocus(@Nullable AppCompatEditText editText, boolean b) {
                    int id = editText.getId();
                    //数据比较  对应两个空  只给第一个数据比较
                    String[] arrayStr = ExamUtils.getArrayStr(item.getCheckType());
                    String trNum = editText.getText().toString().trim();
                    Log.e(TAG, "失去焦点===" + trNum);
                    //TODO 如果有规则  判断是否NG
                    if (item.getCheckType().equals("数据比较") || item.getCheckType().contains("前后联动")) {
                        Log.e(TAG, "比较的值====" + item.getRemark());
                        if (id != 0) {
                            //只有一个数据比较，多个空的情况，默认只第一个数据比较
                            return;
                        }
                        if (!trNum.isEmpty() && !item.getRemark().equals("无") && !item.getRemark().contains("-")) {
                            String remark = item.getRemark();
                            if (CompareUtls.isNgFlag(remark, trNum, item.getCheckType())) {
                                setOK(item, editText);
                            } else {
                                setNG(item, editText);
                            }
                        }
                    }
                }

                //TODO EditText文本改变
                @Override
                public void onMultEdittextChange(@NonNull AppCompatEditText editText, @NonNull String string) {
                    //规则内容
                    //保存到bean
                    if (string.isEmpty()) {
                        editText.setTextColor(Color.BLUE);
                        string = " ";
                    }
                    result.set((int) editText.getTag(), string);
                    item.setAnswer(Utils.FortListToString(result));
                }

                //TODO 选择框  多选一  单选
                @Override
                public void OnMultCheckChange(@Nullable CheckBox checkBox, boolean isCheck) {
                    //外置位 获取焦点
                    setFource(checkBox, missView);
                    int tag = (int) checkBox.getTag();
                    int id = checkBox.getId();
                    if (isCheck) {
                        for (int i = 0; i < examIntegrationView.getChildCount(); i++) {
                            View childAt = examIntegrationView.getChildAt(i);
                            if (childAt instanceof CheckBox) {
                                int tag1 = (int) childAt.getTag();
                                if (tag != tag1) {
                                    ((CheckBox) childAt).setChecked(false);
                                    result.set(tag1, "false");
                                } else {
                                    result.set(tag, "true");
                                }
                            }
                        }
                        //TODO 指图书多选一
                        if (item.getModeRuleNumber().trim().equals("0") && !item.getRemark().isEmpty() && Utils.isNumber(item.getRemark()) && item.getCheckType().equals("多选一")) {
                            String remark = item.getRemark();
                            if (Utils.isNumber(remark)) {
                                if (id != Integer.parseInt(remark)) {
                                    item.setIsNG("0");
                                    checkBox.setBackground(context.getDrawable(R.drawable.check_style_ng_check));
                                    shoDialog();
                                } else {
                                    item.setIsNG("1");
                                }
                            }
                        } else if (item.getCheckType().equals("多选一")) {
                            //TODO 单个多选一
                            try {
                                setCheck(id, item, checkBox);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else if (item.getCheckType().contains("多选一")) {
                            //TODO  是否有+   目前只有 是否显示+多选一 情况
                            if (item.getModeRuleNumber().contains("+")) {
                                try {
                                    setMutCheck(id, item, checkBox);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {

                            }
                        }
                    } else {
                        item.setIsNG("");
                        result.set(tag, "false");
                    }
                    item.setAnswer(Utils.FortListToString(result));
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "adapter报错");
        }


    }


    //点击取消 软件盘显示
    private void setFource(CheckBox checkBox, TextView missView) {
        missView.setFocusable(true);
        missView.setFocusableInTouchMode(true);
        missView.requestFocus();
        missView.requestFocusFromTouch();
        MethodInputUtil.hideSoftInput(context, checkBox);
    }


    public void setNG(NewExamBean.Project.Check.Children item, EditText editText) {
        editText.setTextColor(Color.RED);
        item.setIsNG("0");
        shoDialog();
    }

    public void setOK(NewExamBean.Project.Check.Children item, EditText editText) {
        editText.setTextColor(Color.BLUE);
        item.setIsNG("1");
    }


    /**
     * @param id       b
     * @param item
     * @param checkBox
     */
    public void setCheck(int id, NewExamBean.Project.Check.Children item, CheckBox checkBox) throws Exception {
        Log.e(TAG, "setCheck:===" + item.getRemark());
        boolean singleProduct = Utils.isNumber(item.getModeRuleNumber());
        if (!singleProduct || item.getModeRuleNumber().isEmpty()) {
            return;
        }
        int strNum = Integer.parseInt(item.getModeRuleNumber());
        Log.e("ddddd", "setCheck: ======" + ArrayMode.length + "----" + strNum);
        if (ArrayMode.length <= strNum) {
            return;
        }
        int loaction = ExamUtils.getLoaction(item.getRemark(), ArrayMode[strNum]);
        Log.e(TAG, "location====" + loaction);
        if (loaction != -1) {
            if (id != loaction) {
                item.setIsNG("0");
                checkBox.setBackground(context.getDrawable(R.drawable.check_style_ng_check));
                shoDialog();
            } else {
                item.setIsNG("1");
            }
        }
    }

    public void setMutCheck(int id, NewExamBean.Project.Check.Children item, CheckBox checkBox) throws Exception {
        String[] modSplite = ExamUtils.getArrayAddStr(item.getModeRuleNumber());
        String[] aRemark = ExamUtils.getArrayAddStr(item.getRemark());
        if (aRemark.length == 2) {
            int strNum = Integer.parseInt(modSplite[1]);
            if (ArrayMode.length <= strNum) {
                return;
            }
            int loaction = ExamUtils.getLoaction(aRemark[1], ArrayMode[strNum]);
            Log.e(TAG, "location====" + loaction);
            if (loaction != -1) {
                if (id != loaction) {
                    item.setIsNG("0");
                    checkBox.setBackground(context.getDrawable(R.drawable.check_style_ng_check));
                    shoDialog();
                } else {
                    item.setIsNG("1");
                }
            }
        }
    }


    public void shoDialog() {
        CustomPopup customPopup = new CustomPopup(context, "1");
        customPopup.setOnSubmitListener(new CustomPopup.OnSubmitListener() {
            @Override
            public void onsubmitClick(String trim) {
                T.show(trim);
                customPopup.dismiss();
            }
        });
        new XPopup.Builder(context)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(customPopup)
                .show();
    }


}
