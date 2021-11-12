package com.alan.mvvm.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.alan.mvvm.base.coil.CoilUtils;
import com.alan.mvvm.base.http.responsebean.PicBean;
import com.alan.mvvm.base.ktx.DensityKtxKt;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：alan
 * 时间：2021/11/10
 * 备注：显示1~N张图片的View
 */
public class MultiImageView extends LinearLayout {
    public static int MAX_WIDTH = 0;

    // 照片的Url列表
    private List<PicBean> imagesList;
    //teng
    private List<ImageView> imageViewList = new ArrayList<>();

    /**
     * 长度 单位为Pixel
     **/
    private int pxOneMaxWH;  // 单张图最大允许宽高
    private int pxMoreWH = 0;// 多张图的宽高
    private int pxImagePadding = 0;// 图片间的间距

    private int MAX_PER_ROW_COUNT = 3;// 每行显示最大数

    private LayoutParams onePicLp;
    private LayoutParams moreLp, moreLpColumnFirst;
    private LayoutParams rowLp;

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public MultiImageView(Context context) {
        super(context);
    }

    public MultiImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pxImagePadding = DensityKtxKt.dp2px(getContext(), 5);
    }

    public List<ImageView> getImageViewList() {
        return imageViewList;
    }

    public void setList(List<PicBean> lists) throws IllegalArgumentException {
        if (lists == null) {
            throw new IllegalArgumentException("imageList is null...");
        }
        imagesList = lists;

        if (MAX_WIDTH > 0) {
            pxMoreWH = (MAX_WIDTH - pxImagePadding * 2) / 3; //解决右侧图片和内容对不齐问题
            pxOneMaxWH = MAX_WIDTH * 2 / 3;
            initImageLayoutParams();
        }

        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MAX_WIDTH == 0) {
            int width = measureWidth(widthMeasureSpec);
            if (width > 0) {
                MAX_WIDTH = width;
                if (imagesList != null && imagesList.size() > 0) {
                    setList(imagesList);
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * Determines the width of this view
     *
     * @param measureSpec A measureSpec packed into an int
     * @return The width of the view, honoring constraints from measureSpec
     */
    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            // We were told how big to be
            result = specSize;
        } else {
            // Measure the text
            // result = (int) mTextPaint.measureText(mText) + getPaddingLeft()
            // + getPaddingRight();
            if (specMode == MeasureSpec.AT_MOST) {
                // Respect AT_MOST value if that was what is called for by
                // measureSpec
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    private void initImageLayoutParams() {
        int wrap = LayoutParams.WRAP_CONTENT;
        int match = LayoutParams.MATCH_PARENT;

        onePicLp = new LayoutParams(wrap, wrap);

        moreLpColumnFirst = new LayoutParams(pxMoreWH, pxMoreWH);
        moreLp = new LayoutParams(pxMoreWH, pxMoreWH);
        moreLp.setMargins(pxImagePadding, 0, 0, 0);

        rowLp = new LayoutParams(match, wrap);
    }

    // 根据imageView的数量初始化不同的View布局,还要为每一个View作点击效果
    private void initView() {
        this.setOrientation(VERTICAL);
        this.removeAllViews();
        if (MAX_WIDTH == 0) {
            //为了触发onMeasure()来测量MultiImageView的最大宽度，MultiImageView的宽设置为match_parent
            addView(new View(getContext()));
            return;
        }

        if (imagesList == null || imagesList.size() == 0) {
            return;
        }

        imageViewList.clear();
        int allCount = imagesList.size();
        if (allCount == 1) {
            ImageView imageView = createImageView(0, false);
            addView(imageView);
            imageViewList.add(imageView);
        } else {
            if (allCount == 4) {
                MAX_PER_ROW_COUNT = 2;
            } else {
                MAX_PER_ROW_COUNT = 3;
            }
            int rowCount = allCount / MAX_PER_ROW_COUNT + (allCount % MAX_PER_ROW_COUNT > 0 ? 1 : 0);// 行数
            for (int rowCursor = 0; rowCursor < rowCount; rowCursor++) {
                LinearLayout rowLayout = new LinearLayout(getContext());
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);

                rowLayout.setLayoutParams(rowLp);
                if (rowCursor != 0) {
                    rowLayout.setPadding(0, pxImagePadding, 0, 0);
                }

                int columnCount = allCount % MAX_PER_ROW_COUNT == 0 ? MAX_PER_ROW_COUNT
                        : allCount % MAX_PER_ROW_COUNT;//每行的列数
                if (rowCursor != rowCount - 1) {
                    columnCount = MAX_PER_ROW_COUNT;
                }
                addView(rowLayout);

                int rowOffset = rowCursor * MAX_PER_ROW_COUNT;// 行偏移
                for (int columnCursor = 0; columnCursor < columnCount; columnCursor++) {
                    int position = columnCursor + rowOffset;
                    ImageView imageView = createImageView(position, true);
                    rowLayout.addView(imageView);
                    imageViewList.add(imageView);
                }
            }
        }
    }

    /**
     * 创建ImageView
     *
     * @param position
     * @param isMultiImage
     * @return
     */
    private ImageView createImageView(int position, final boolean isMultiImage) {
        PicBean photoInfo = imagesList.get(position);
        ImageView imageView = new ImageView(getContext());
        if (isMultiImage) {
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setLayoutParams(position % MAX_PER_ROW_COUNT == 0 ? moreLpColumnFirst : moreLp);
        } else {
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            //imageView.setMaxHeight(pxOneMaxWH);

            int expectW = photoInfo.getW();
            int expectH = photoInfo.getH();

            if (expectW == 0 || expectH == 0) {
                imageView.setLayoutParams(onePicLp);
            } else {
                int actualW = 0;
                int actualH = 0;
                float scale = ((float) expectH) / ((float) expectW);
                if (expectW > pxOneMaxWH) {
                    actualW = pxOneMaxWH;
                    actualH = (int) (actualW * scale);
                } else if (expectW < pxMoreWH) {
                    actualW = pxMoreWH;
                    actualH = (int) (actualW * scale);
                } else {
                    actualW = expectW;
                    actualH = expectH;
                }
                imageView.setLayoutParams(new LayoutParams(actualW, actualH));
            }
        }

        imageView.setId(photoInfo.getUrl().hashCode());
        imageView.setOnClickListener(new ImageOnClickListener(position));
        String imgUrl = photoInfo.getUrl() + "?x-oss-process=image/resize,l_300,m_lfit";
        KLog.e("图片地址", imgUrl);
        CoilUtils.INSTANCE.load(imageView, imgUrl);

        return imageView;
    }

    private class ImageOnClickListener implements OnClickListener {

        private int position;

        public ImageOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, position);
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}