package com.example.viewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 继承View，此时提示需要重载构造参数，我们可以看到有4种：
 * 第一种:一般在直接被代码中new一个View的时候调用
 * 第二种：一般在layout文件中使用的时候被调用，关于它的所有属性（包括自定义属性）都会包含在attrs.xml中传递过来
 * 第三种：在xml布局文件中调用，并且标签中还有自定义属性，这里调用的还是第二个构造函数
 * 第四种：是API21添加的，一般用不到。
 * 一般实现重载前3种构造函数就可以了
 *
 *
 *
 *
 *
 * Android自定义View / ViewGroup的步骤大致如下：
 * 1)  自定义属性；
 * 2)  选择和设置构造方法；
 * 3)  重写onMeasure()方法；
 * 4)  重写onDraw()方法；
 * 5)  重写onLayout()方法；
 * 6)  重写其他事件的方法（如：滑动监听等）。
 */
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
        //获得我自定义的样式属性
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
        mPaint = new Paint();//获得Paint的实例
        mPaint.setTextSize(textSize);//设置Paint大小
        mBound = new Rect();//获取Rect实例
        // 获取字体的显示范围，这个显示范围是只跟字体大小和字符长度有关的属性
        // 不管字符是否被画出，这个显示范围的属性都存在
        // 可以看到getTextBounds在drawText之前调用的，还是可以获得数据
        mPaint.getTextBounds(text, 0, text.length(), mBound);//由调用者返回在边界(分配)的最小矩形包含所有的字符,以隐含原点(0,0)
    }


    /**
     * 系统测量高度时一般宽高都是MATCH_PARNET，当我们明确设置宽高时，系统帮我们测量的结果就是我们设置的结果
     * 当我们设置为WRAP_CONTENT,或者MATCH_PARENT系统帮我们测量的结果就是MATCH_PARENT的长度。
     *
     * 注：所以当我们设置宽高为WRAP_CONTENT时，我们需要重写onMeasure方法，重写测量
     *
     * 重写之前先了解MeasureSpec的specMode,一共三种类型：
     * EXACTLY：一般是设置了明确的值或者是MATCH_PARENT
     * AT_MOST：表示子布局限制在一个最大值内，一般为WARP_CONTENT
     * UNSPECIFIED：表示子布局想要多大就多大，很少使用
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
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
        setMeasuredDimension(width, height);//设置重新测量的宽高
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
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);//这里的getMeasuredWidth和getMeasuredHeight是我们重写onMeasure重新测量出来的宽高
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
