package com.alan.mvvm.common.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.alan.mvvm.base.ktx.DensityKtxKt;
import com.alan.mvvm.common.R;

/**
 * 作者：alan
 * 时间：2021/11/11
 * 备注：引导覆层
 */
public class GuideView extends FrameLayout implements ViewTreeObserver.OnGlobalLayoutListener {
    private final String TAG = getClass().getSimpleName();
    public static final int DEFAULT_BACKGROUND_COLOR = 0xb2000000;

    private Context mContent;
    /**
     * targetView前缀。SHOW_GUIDE_PREFIX + targetView.getId()作为保存在SP文件的key。
     */
    private static final String SHOW_GUIDE_PREFIX = "show_guide_on_view_";
    /**
     * GuideView 偏移量
     */
    private int offsetX, offsetY;
    /**
     * targetView 的外切圆半径
     */
    private int radius;

    /**
     * targetView 的外切圆半径
     */
    private int roundRadius;

    /**
     * 需要显示提示信息的View
     */
    private View targetView;
    /**
     * 自定义View
     */
    private int layoutResId;
    /**
     * 透明圆形画笔
     */
    private Paint mCirclePaint;
    private Paint paint;

    /**
     * targetView是否已测量
     */
    private boolean isMeasured;

    /**
     * targetView左上角坐标
     */
    private int[] targetLocation;
    /**
     * targetView圆心
     */
    private int[] centerLocation;

    /**
     * 背景色和透明度，格式 #aarrggbb
     */
    private int backgroundColor;

    /**
     * 背景图片
     */
    private int bgImage;

    /**
     * 相对于targetView的位置.在target的那个方向
     */
    private Direction direction;

    /**
     * 形状
     */
    private MyShape myShape;

    private boolean onClickExit;
    private OnClickCallback onclickListener;


    public GuideView(Context context) {
        super(context);
        this.mContent = context;
        init();
    }

    private void init() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mCirclePaint.setXfermode(xfermode);
        //设置画笔遮罩滤镜,可以传入BlurMaskFilter或EmbossMaskFilter，前者为模糊遮罩滤镜而后者为浮雕遮罩滤镜
        //这个方法已经被标注为过时的方法了，如果你的应用启用了硬件加速，你是看不到任何阴影效果的
        mCirclePaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.INNER));
        //关闭当前view的硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
    }


    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setShape(MyShape shape) {
        this.myShape = shape;
    }

    public void setLayoutResId(int layoutResId) {
        this.layoutResId = layoutResId;
    }

    public int getRoundRadius() {
        return roundRadius;
    }

    public void setRoundRadius(int roundRadius) {
        this.roundRadius = roundRadius;
    }

    public void setBgColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public View getTargetView() {
        return targetView;
    }

    public void setTargetView(View targetView) {
        this.targetView = targetView;
    }


    public void setBgImage(int bgImage) {
        this.bgImage = bgImage;
    }

    public void showOnce() {
        if (targetView != null) {
            mContent.getSharedPreferences(TAG, Context.MODE_PRIVATE).edit().putBoolean(generateUniqId(targetView), true).commit();
        }
    }

    private boolean hasShown() {
        if (targetView == null) {
            return true;
        }
        return mContent.getSharedPreferences(TAG, Context.MODE_PRIVATE).getBoolean(generateUniqId(targetView), false);
    }

    private String generateUniqId(View v) {
        return SHOW_GUIDE_PREFIX + v.getId();
    }


    public int[] getCenter() {
        return centerLocation;
    }

    public void setCenter(int[] centerLocation) {
        this.centerLocation = centerLocation;
    }

    public void setOnClickExit(boolean onClickExit) {
        this.onClickExit = onClickExit;
    }

    public void setOnclickListener(OnClickCallback onclickListener) {
        this.onclickListener = onclickListener;
    }

    /**
     * 获得targetView 的宽高，如果未测量，返回｛-1， -1｝
     *
     * @return
     */
    private int[] getTargetViewSize() {
        int[] size = {-1, -1};
        if (isMeasured) {
            size[0] = targetView.getWidth();
            size[1] = targetView.getHeight();
        }
        return size;
    }

    /**
     * 获得targetView 的半径
     *
     * @return
     */
    private int getTargetViewRadius() {
        if (isMeasured) {
            int[] size = getTargetViewSize();
            int x = size[0];
            int y = size[1];

            return (int) (Math.sqrt(x * x + y * y) / 2);
        }
        return -1;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.v(TAG, "onDraw");

        if (!isMeasured)
            return;

        if (targetView == null)
            return;

        // 先绘制背景
        Log.v(TAG, "drawBackground");
        canvas.drawColor(backgroundColor == 0 ? DEFAULT_BACKGROUND_COLOR : backgroundColor);
        drawHighlights(canvas);
    }

    private void drawHighlights(Canvas canvas) {
        int halfWidth = targetView.getWidth() / 2;
        int halfHeight = targetView.getHeight() / 2;
        if (myShape != null) {
            RectF oval = new RectF();
            switch (myShape) {
                case CIRCULAR://圆形
                    canvas.drawCircle(centerLocation[0], centerLocation[1], radius, mCirclePaint);//绘制圆形
                    break;
                case ELLIPSE://椭圆
                    //RectF对象
                    oval.left = centerLocation[0] - 150;                              //左边
                    oval.top = centerLocation[1] - 50;                                   //上边
                    oval.right = centerLocation[0] + 150;                             //右边
                    oval.bottom = centerLocation[1] + 50;                                //下边
                    canvas.drawOval(oval, mCirclePaint);                   //绘制椭圆
                    break;
                case RECTANGULAR://圆角矩形
                    //RectF对象
                    oval.left = centerLocation[0] - halfWidth - 10;                              //左边
                    oval.top = centerLocation[1] - halfHeight - 10;                                   //上边
                    oval.right = centerLocation[0] + halfWidth + 10;                             //右边
                    oval.bottom = centerLocation[1] + halfHeight + 10;                                //下边
                    canvas.drawRoundRect(oval, roundRadius, roundRadius, mCirclePaint);                   //绘制圆角矩形
                    break;
            }
        } else {
            canvas.drawCircle(centerLocation[0], centerLocation[1], radius, mCirclePaint);//绘制圆形
        }

        if (bgImage != 0) {
            int left = targetLocation[0];
            int top = targetLocation[1];
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), bgImage);
            canvas.drawBitmap(bitmap, left - DensityKtxKt.dp2px(mContent, 23f), top - DensityKtxKt.dp2px(mContent, 22f), paint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (myShape != null && !onClickExit) {
                    RectF oval = new RectF();
                    switch (myShape) {
                        case CIRCULAR://圆形
                            oval.left = centerLocation[0] - radius;                              //左边
                            oval.top = centerLocation[1] - radius;                                   //上边
                            oval.right = centerLocation[0] + radius;                             //右边
                            oval.bottom = centerLocation[1] + radius;
                            break;
                        case ELLIPSE://椭圆
                            //RectF对象
                            oval.left = centerLocation[0] - radius;                              //左边
                            oval.top = centerLocation[1] - radius;                                   //上边
                            oval.right = centerLocation[0] + radius;                             //右边
                            oval.bottom = centerLocation[1] + radius;                 //绘制椭圆
                            break;
                        case RECTANGULAR://圆角矩形
                            int width = targetView.getWidth() / 2;
                            int height = targetView.getHeight() / 2;
                            //RectF对象
                            oval.left = centerLocation[0] - width - 10;                              //左边
                            oval.top = centerLocation[1] - height - 10;                                   //上边
                            oval.right = centerLocation[0] + width + 10;                             //右边
                            oval.bottom = centerLocation[1] + height + 10;                                //下边
                            break;
                    }
                    if (oval.contains((int) event.getX(), (int) event.getY())) {
                        hide();
                        return false;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public void onGlobalLayout() {
        if (isMeasured) {
            return;
        }
        if (targetView.getHeight() > 0 && targetView.getWidth() > 0) {
            isMeasured = true;
        }

        // 获取targetView的中心坐标
        if (centerLocation == null) {
            // 获取右上角坐标
            targetLocation = new int[2];
            targetView.getLocationInWindow(targetLocation);
            centerLocation = new int[2];
            // 获取中心坐标
            centerLocation[0] = targetLocation[0] + targetView.getWidth() / 2;
            centerLocation[1] = targetLocation[1] + targetView.getHeight() / 2;
        }
        // 获取targetView外切圆半径
        if (radius == 0) {
            radius = getTargetViewRadius();
        }
        // 添加GuideView
        createGuideView();
    }

    /**
     * 添加提示文字，位置在targetView的下边
     * 在屏幕窗口，添加蒙层，蒙层绘制总背景和透明圆形，圆形下边绘制说明文字
     */
    private void createGuideView() {
        Log.v(TAG, "createGuideView");
        removeAllViews();
        if (layoutResId != 0) {
            View customGuideView = LayoutInflater.from(getContext()).inflate(layoutResId, this, false);
            LayoutParams guideViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            if (direction != null) {
                int width = this.getWidth();
                int height = this.getHeight();

                int left = targetLocation[0];
                int right = targetLocation[0] + targetView.getWidth();
                int top = targetLocation[1];
                int bottom = targetLocation[1] + targetView.getHeight();
                switch (direction) {
                    case TOP:
                        guideViewParams.setMargins(offsetX, top, -offsetX, height - top - offsetY);
                        break;
                    case LEFT:
                        guideViewParams.setMargins(offsetX - width + left, top + offsetY, width - left - offsetX, -top - offsetY);
                        break;
                    case BOTTOM:
                        guideViewParams.setMargins(0, bottom + offsetY, 0, 0);
                        break;
                    case RIGHT:
                        guideViewParams.setMargins(right + offsetX, top + offsetY, -right - offsetX, -top - offsetY);
                        break;
                    case LEFT_TOP:
                        guideViewParams.setMargins(offsetX - width + left, offsetY - height + top, width - left - offsetX, height - top - offsetY);
                        break;
                    case LEFT_BOTTOM:
                        guideViewParams.setMargins(0, bottom + offsetY, 0, 0);
                        break;
                    case RIGHT_TOP:
                        guideViewParams.setMargins(right + offsetX, offsetY - height + top, -right - offsetX, height - top - offsetY);
                        break;
                    case RIGHT_BOTTOM:
                        guideViewParams.setMargins(0, bottom + offsetY, 0, 0);
                        break;
                }
            } else {
                guideViewParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                guideViewParams.setMargins(offsetX, offsetY, -offsetX, -offsetY);
            }


            this.addView(customGuideView, guideViewParams);
        }
    }


    public void hide() {
        Log.v(TAG, "hide");
        targetView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        this.removeAllViews();
        ((FrameLayout) ((Activity) mContent).getWindow().getDecorView()).removeView(this);
        restoreState();
    }

    public void show() {
        Log.v(TAG, "show");
        if (hasShown())
            return;

        if (targetView != null) {
            targetView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }

        this.setBackgroundResource(R.color.transparent);

        ((FrameLayout) ((Activity) mContent).getWindow().getDecorView()).addView(this, new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }


    private void setClickInfo() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onclickListener != null) {
                    onclickListener.onClickedGuideView();
                }
                if (onClickExit) {
                    hide();
                }
            }
        });
    }

    public void restoreState() {
        Log.v(TAG, "restoreState");
        offsetX = offsetY = 0;
        radius = 0;
        mCirclePaint = null;
        isMeasured = false;
        centerLocation = null;
        backgroundColor = DEFAULT_BACKGROUND_COLOR;
    }


    /**
     * 定义GuideView相对于targetView的方位，共八种。不设置则默认在targetView下方
     */
    public enum Direction {
        LEFT, TOP, RIGHT, BOTTOM,
        LEFT_TOP, LEFT_BOTTOM,
        RIGHT_TOP, RIGHT_BOTTOM
    }

    /**
     * 定义目标控件的形状，共3种。圆形，椭圆，带圆角的矩形（可以设置圆角大小），不设置则默认是圆形
     */
    public enum MyShape {
        CIRCULAR, ELLIPSE, RECTANGULAR
    }

    /**
     * GuideView点击Callback
     */
    public interface OnClickCallback {
        void onClickedGuideView();
    }

    public static class Builder {
        static GuideView guiderView;
        static Builder instance = new Builder();
        Context mContext;

        private Builder() {
        }

        public Builder(Context ctx) {
            mContext = ctx;
        }

        public static Builder newInstance(Context ctx) {
            guiderView = new GuideView(ctx);
            return instance;
        }

        public Builder setTargetView(View target) {
            guiderView.setTargetView(target);
            return instance;
        }

        public Builder setBgColor(int color) {
            guiderView.setBgColor(color);
            return instance;
        }

        public Builder setDirction(Direction dir) {
            guiderView.setDirection(dir);
            return instance;
        }

        public Builder setShape(MyShape shape) {
            guiderView.setShape(shape);
            return instance;
        }

        public Builder setOffset(int x, int y) {
            guiderView.setOffsetX(x);
            guiderView.setOffsetY(y);
            return instance;
        }

        public Builder setRoundRadius(int roundRadius) {
            guiderView.setRoundRadius(roundRadius);
            return instance;
        }

        public Builder setLayoutResId(int layoutResId) {
            guiderView.setLayoutResId(layoutResId);
            return instance;
        }

        public Builder setBgImage(int imageResId) {
            guiderView.setBgImage(imageResId);
            return instance;
        }

        public Builder setCenter(int X, int Y) {
            guiderView.setCenter(new int[]{X, Y});
            return instance;
        }

        public Builder showOnce() {
            guiderView.showOnce();
            return instance;
        }

        public GuideView build() {
            guiderView.setClickInfo();
            return guiderView;
        }

        public Builder setOnclickExit(boolean onclickExit) {
            guiderView.setOnClickExit(onclickExit);
            return instance;
        }

        public Builder setOnclickListener(final OnClickCallback callback) {
            guiderView.setOnclickListener(callback);
            return instance;
        }
    }
}
