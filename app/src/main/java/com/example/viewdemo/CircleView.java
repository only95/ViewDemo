package com.example.viewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CircleView extends View {
    private String text;//文本
    private int textSize;//字体大小
    private int textColor;//字体颜色
    private Paint mPaint;//画笔
    private Rect mBound;//绘制矩形

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                text = randomText();
                invalidate();
//                postInvalidate();
            }
        });
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray;
        typedArray = (TypedArray) context.getTheme().obtainStyledAttributes(attrs, R.styleable.circleView, defStyleAttr, 0);
        if (typedArray != null) {
            text = typedArray.getString(R.styleable.circleView_text);
            textSize = typedArray.getDimensionPixelSize(R.styleable.circleView_textSize, 0);
            textColor = typedArray.getColor(R.styleable.circleView_textColor, 0);
            typedArray.recycle();//将TypedArray这个对象进行资源的释放。
        }
        /**
         * 获得绘制文本的宽和高
         */
        mPaint = new Paint();
        mPaint.setTextSize(textSize);
        mBound = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), mBound);
    }

    //测量
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width, height;
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(textSize);
            mPaint.getTextBounds(text, 0, text.length(), mBound);
            int textWidth = mBound.width();
            int desired = getPaddingLeft() + textWidth + getPaddingRight();
            width = desired;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(textSize);
            mPaint.getTextBounds(text, 0, text.length(), mBound);
            int textHeight = mBound.height();
            int desired = getPaddingTop() + textHeight + getPaddingBottom();
            height = desired;
        }
        setMeasuredDimension(width, height);
    }

    //定位
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.YELLOW);//背景颜色
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(textColor);//字体颜色
        canvas.drawText(text, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
    }


    //随机4位数
    private String randomText(){
        Random random=new Random();
        Set<Integer> integers=new HashSet<>();
        while (integers.size()<4){
            int randomInt=random.nextInt(10);
            integers.add(randomInt);
        }
        StringBuffer stringBuffer=new StringBuffer();
        for (Integer i:integers){
            stringBuffer.append(i);
        }
        return stringBuffer.toString();
    }

}
