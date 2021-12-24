package com.alan.module.main.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.alan.module.main.R;
import com.alan.mvvm.base.coil.CoilUtils;
import com.alan.mvvm.base.http.responsebean.SimilarityBean;
import com.alan.mvvm.base.http.responsebean.SoundResultBean;
import com.alan.mvvm.base.http.responsebean.UserInfoBean;
import com.alan.mvvm.common.helper.SpHelper;
import com.alan.mvvm.common.views.SpiderView;
import com.bumptech.glide.Glide;
import com.hyphenate.util.DensityUtil;
import com.socks.library.KLog;

import java.util.ArrayList;

public class ShareView extends FrameLayout {
    private Context mContext;
    private static int IMAGE_WIDTH = 1080;
    private static int IMAGE_HEIGHT = 1920;
    private ImageView iv_avatar;
    private TextView tv_name;
    private TextView tv_star;
    private SpiderView spiderview;
    private TextView tv_attr;
    private ImageView iv_code;


    public ShareView(Context context) {
        this(context, null);
    }

    public ShareView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShareView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        IMAGE_WIDTH = DensityUtil.dip2px(mContext, 375);
        IMAGE_HEIGHT = DensityUtil.dip2px(mContext, 812);
        View layout = View.inflate(getContext(), R.layout.layout_share_img, this);
        iv_avatar = layout.findViewById(R.id.iv_avatar);
        tv_name = layout.findViewById(R.id.tv_name);
        tv_star = layout.findViewById(R.id.tv_star);
        spiderview = layout.findViewById(R.id.spiderview);
        tv_attr = layout.findViewById(R.id.tv_attr);
        iv_code = layout.findViewById(R.id.iv_code);
    }

    /**
     * 设置相关信息
     * 头像地址和二维码地址
     */
    public void setInfo(SoundResultBean bean) {
        String attribute = bean.getAttribute();
        String highlySimilar = bean.getHighlySimilar();
        ArrayList<SimilarityBean> list = bean.getSimilarity();
        double[] doubleArrayOf = new double[5];
        String[] stringArrayOf = new String[5];
        for (int i = 0; i < list.size(); i++) {
            SimilarityBean itemBean = list.get(i);
            double sims = itemBean.getSims();
            String userName = itemBean.getUserName();

            doubleArrayOf[i] = sims;
            stringArrayOf[i] = userName;
        }
        UserInfoBean userInfo = SpHelper.INSTANCE.getUserInfo();
        tv_name.setText(userInfo.getUserName());
        tv_star.setText(highlySimilar);
        tv_attr.setText(attribute);
        CoilUtils.INSTANCE.loadRoundBorder(iv_avatar, userInfo.getAvatar(), 32f,
                1f, ContextCompat.getColor(mContext, R.color.white));
        KLog.e("xujm", "codeUrl:" + bean.getDownloadQr());
//        CoilUtils.INSTANCE.load(iv_code,bean.getDownloadQr());
        Glide.with(mContext).load(bean.getDownloadQr()).into(iv_code);
        spiderview.setRankData(doubleArrayOf, stringArrayOf);
    }

    /**
     * 生成图片
     *
     * @return
     */
    public Bitmap createImage() {
        //由于直接new出来的view是不会走测量、布局、绘制的方法的，所以需要我们手动去调这些方法，不然生成的图片就是黑色的。
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(IMAGE_WIDTH, MeasureSpec.EXACTLY);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(IMAGE_HEIGHT, MeasureSpec.EXACTLY);

        measure(widthMeasureSpec, heightMeasureSpec);
        layout(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
        Bitmap bitmap = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        draw(canvas);

        return bitmap;
    }
}
