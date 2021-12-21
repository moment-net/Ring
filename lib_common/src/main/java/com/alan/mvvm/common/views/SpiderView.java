package com.alan.mvvm.common.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.alan.mvvm.base.ktx.DensityKtxKt;

public class SpiderView extends View {

    //正N边形边线
    Paint edgesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //中心发出的射线
    Paint radialLinesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //等级线
    Paint rankPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //等级顶点圆
    Paint rankCircleSmallPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint rankCircleBigPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    //等级文字
    Paint rankTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //自定义view的宽高
    float width, height;
    //战力值数据
    private double[] rankData = {0.2, 0.1, 0.4, 0.6, 0.3};

    //战力种类
    private String[] rankText = {"击杀", "助攻", "金钱", "物理", "防御"};

    //N边形的半径
    int ROUND_BIG = 70;
    int ROUND_MIDDLE = 45;
    int ROUND_SMALL = 20;
    int ROUND_VERTEX_SMALL = 8;
    int ROUND_VERTEX_BIG = 10;

    //N边形的边数
    int edges = 5;
    //根据边数求得每个顶点对应的度数
    double degrees = 360 / edges;
    //根据度数，转化为弧度
    double hudu = (Math.PI / 180) * degrees;


    public SpiderView(Context context) {
        this(context, null);
    }

    public SpiderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpiderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void init(Context context) {
        edgesPaint.setStyle(Paint.Style.STROKE);
        edgesPaint.setStrokeWidth(3);
        edgesPaint.setColor(Color.BLACK);

        radialLinesPaint.setStyle(Paint.Style.STROKE);
        radialLinesPaint.setStrokeWidth(2);
        radialLinesPaint.setColor(Color.BLUE);

        rankPaint.setStyle(Paint.Style.FILL);
        rankPaint.setStrokeWidth(10);
        rankPaint.setColor(Color.parseColor("#80A848FF"));


        rankCircleSmallPaint.setColor(Color.parseColor("#99A848FF"));//设置颜色
        rankCircleSmallPaint.setStyle(Paint.Style.FILL);//绘图为线条模式


        rankCircleBigPaint.setColor(Color.parseColor("#924ED0"));//设置颜色
        rankCircleBigPaint.setStyle(Paint.Style.STROKE);//绘图为线条模式
        rankCircleBigPaint.setStrokeWidth(DensityKtxKt.dp2px(context, 2f));


        rankTextPaint.setStyle(Paint.Style.FILL);
        rankTextPaint.setStrokeWidth(1);
        rankTextPaint.setColor(Color.WHITE);
        rankTextPaint.setTextSize(DensityKtxKt.sp2px(context, 14));

        ROUND_BIG = DensityKtxKt.dp2px(context, 68f);
        ROUND_MIDDLE = DensityKtxKt.dp2px(context, 43f);
        ROUND_SMALL = DensityKtxKt.dp2px(context, 22f);
        ROUND_VERTEX_SMALL = DensityKtxKt.dp2px(context, 5f);
        ROUND_VERTEX_BIG = DensityKtxKt.dp2px(context, 5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //正N边形个数
        edgesPaint.setStyle(Paint.Style.FILL);
        edgesPaint.setColor(Color.parseColor("#80ffffff"));
        drawCircle(canvas, ROUND_BIG);
        edgesPaint.setColor(Color.parseColor("#80ffffff"));
        drawCircle(canvas, ROUND_MIDDLE);
        edgesPaint.setColor(Color.parseColor("#80ffffff"));
        drawCircle(canvas, ROUND_SMALL);


        //从中心点到各个顶点的射线
//        drawLines(canvas, ROUND_BIG);

        //画战力值区域
        drawRanks(canvas, ROUND_BIG);
        //画战力值顶点
        drawDots(canvas, ROUND_BIG);

        //画战力文字
        drawRankText(canvas, ROUND_BIG);
    }

    /**
     * 画战力值区域
     *
     * @param canvas
     * @param radius
     */
    private void drawRanks(Canvas canvas, float radius) {
        Path path = new Path();
        float endx, endy;
        for (int i = 0; i < edges; i++) {
            endx = (float) (width / 2 + radius * Math.sin(hudu * i) * rankData[i]);
            endy = (float) (height / 2 - radius * Math.cos(hudu * i) * rankData[i]);
            if (i == 0) {
//                path.moveTo(width / 2, (float) (height / 2 - radius * 0.5));
                path.moveTo(endx, endy);
            } else {
                path.lineTo(endx, endy);
            }
        }
        path.close();
        canvas.drawPath(path, rankPaint);
    }

    /**
     * 画战力值顶点
     *
     * @param canvas
     * @param radius
     */
    private void drawDots(Canvas canvas, float radius) {
        float endx, endy;
        for (int i = 0; i < edges; i++) {
            endx = (float) (width / 2 + radius * Math.sin(hudu * i) * rankData[i]);
            endy = (float) (height / 2 - radius * Math.cos(hudu * i) * rankData[i]);
            //通过线条绘图模式绘制圆环
            canvas.drawCircle(endx, endy, ROUND_VERTEX_BIG, rankCircleBigPaint);
            canvas.drawCircle(endx, endy, ROUND_VERTEX_SMALL, rankCircleSmallPaint);
        }
    }


    /**
     * 画战力文字
     *
     * @param canvas
     * @param radius
     */
    private void drawRankText(Canvas canvas, float radius) {
        float endx, endy;
        Rect bounds = new Rect();
        for (int i = 0; i < edges; i++) {
            rankTextPaint.getTextBounds(rankText[i], 0, rankText[i].length(), bounds);
            endx = (float) (width / 2 + radius * 1.4 * Math.sin(hudu * i) - (bounds.right - bounds.left) / 2);
            endy = (float) (height / 2 - radius * 1.2 * Math.cos(hudu * i) + (bounds.bottom - bounds.top) / 2);
            canvas.drawText(rankText[i], endx, endy, rankTextPaint);
        }
    }

    /**
     * 从中心点到各个顶点的射线
     *
     * @param canvas
     * @param radius
     */
    private void drawLines(Canvas canvas, float radius) {
        //从中心点出发
        Path path = new Path();
        path.moveTo(width / 2, height / 2);
        float endx, endy;
        for (int i = 0; i < edges; i++) {
            endx = (float) (width / 2 + radius * Math.sin(hudu * i));
            endy = (float) (height / 2 - radius * Math.cos(hudu * i));
            path.lineTo(endx, endy);
            canvas.drawPath(path, radialLinesPaint);
            //画完一条线后，重置起点在中心点，再画下一条直线
            endx = width / 2;
            endy = height / 2;
            path.moveTo(endx, endy);
        }
    }

    /**
     * 画正N边形
     *
     * @param canvas
     * @param radius
     */
    private void drawPolygon(Canvas canvas, float radius) {
        //从上面的顶点出发
        Path path = new Path();
        path.moveTo(width / 2, height / 2 - radius);

        float endx, endy;
        for (int i = 0; i < edges; i++) {
            endx = (float) (width / 2 + radius * Math.sin(hudu * i));
            endy = (float) (height / 2 - radius * Math.cos(hudu * i));
            path.lineTo(endx, endy);
        }
        path.close();
        canvas.drawPath(path, edgesPaint);
    }

    /**
     * 画圆形
     *
     * @param canvas
     * @param radius
     */
    private void drawCircle(Canvas canvas, float radius) {
        canvas.drawCircle(width / 2, height / 2, radius, edgesPaint);
    }

    /**
     * 设置数据刷新
     *
     * @param data
     * @param content
     */
    public void setRankData(double[] data, String[] content) {
        this.rankData = data;
        this.rankText = content;
        invalidate();
    }
}

