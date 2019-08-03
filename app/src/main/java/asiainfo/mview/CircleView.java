package asiainfo.mview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class CircleView extends View {

    private int backgroudColor;//背景颜色
    private int progressColor;//进度条颜色
    private int textColor;
    private float textSize;//文字大小
    private int circleWidth;
    private  static final float DEFAULTWIDTH = ViewUtils.dp2px(100);//默认测量宽高
    private  static final float DEFAULTHEIGHT = ViewUtils.dp2px(100);
    private  static final float DEFAULTPADDING= ViewUtils.dp2px(20);//默认padding
    private Paint paint;
    private RectF rectF;
    private Paint.FontMetrics fontMetrics;
    private float mid;
    private float fraction;
    private ObjectAnimator animator;
    private float progress;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        backgroudColor = typedArray.getColor(R.styleable.CircleView_backgroundColor, 0xFF66CC00);
        progressColor = typedArray.getColor(R.styleable.CircleView_progressColor, 0xFF0066FF);
        textColor = typedArray.getColor(R.styleable.CircleView_textColor, 0xFF0066FF);
        textSize = typedArray.getDimension(R.styleable.CircleView_textSize, ViewUtils.sp2px(40));
        typedArray.recycle();
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //初始化一些数据
    private void init(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.getFontMetrics(fontMetrics);
        rectF = new RectF();
        fontMetrics = new Paint.FontMetrics();
    }

    //默认宽高设置一下。
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //可用原生方法 resolveSize() resolveSizeAndState
        //区别是state 会给width Measure_state_to_small
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if(widthMode != MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY){
            setMeasuredDimension((int)DEFAULTWIDTH,(int)DEFAULTHEIGHT);
        }else if(heightMode == MeasureSpec.EXACTLY){
            setMeasuredDimension(heightSize,heightSize);
        }else{
            setMeasuredDimension(widthSize,widthSize);
        }
        circleWidth = getMeasuredWidth()/15;
        //设置圆环的宽度
        paint.setStrokeWidth(circleWidth);
        rectF.left =DEFAULTPADDING ;
        rectF.top = DEFAULTPADDING;
        rectF.right = getMeasuredWidth()-DEFAULTPADDING;
        rectF.bottom =getMeasuredHeight()-DEFAULTPADDING ;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制背景圆环
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(backgroudColor);
        canvas.drawArc(rectF,0,360,false,paint);

        //绘制圆环
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(progressColor);
        canvas.drawArc(rectF,-90, Math.round(progress*fraction)*3.6f,false,paint);

        //绘制文字
        paint.setColor(textColor);
        paint.setStyle(Paint.Style.FILL);
        mid = (fontMetrics.ascent + fontMetrics.descent) / 2;
        canvas.drawText(Math.round(progress*fraction)+"%",getWidth()/2,getHeight()/2-mid+DEFAULTPADDING+circleWidth,paint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(animator!=null && animator.isRunning()){
            animator.end();
            animator = null;
        }
    }

    private void startAnimation(){
        if(animator == null){
            animator = ObjectAnimator.ofFloat(this, "fraction", 0, 1);
            animator.setDuration(1000);
            animator.setInterpolator(new LinearInterpolator());
        }
        animator.start();
    }

    public void setProgress(int progress){
        if(progress <0){
            return;
        }
        this.progress = progress;
        startAnimation();
    }
    private void setFraction(float fraction) {
        this.fraction = fraction;
        invalidate();
    }
    private double getFraction() {
        return fraction;
    }


}

