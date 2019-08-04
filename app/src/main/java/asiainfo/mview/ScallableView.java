package asiainfo.mview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

/**
 * 功能：放大 缩小 放大拖拽 惯性拖拽。
 * 1.放大缩小 双击 那么把TouchEvent 交给GestureDetector
 * 下面计算 放大的尺寸。一种是按照宽度 一种是按照长度
 * 一种是小的放大，按照最小宽度放大，一种大的放大 按照最小宽度相反的边放大。
 * <p>
 * 2.实现拖拽功能
 * 坑：缩放和平移的顺序 先缩放后平移，平移要考虑缩放的倍数，它会自动帮你算，所以你的实际
 * 平移要转换。比如：先放大1倍，再平移100px，实际效果是平移了200px。
 * 所以，我先平移 再缩放 就没这个问题了。
 * <p>
 * <p>
 * 3.实现惯性拖拽功能
 * 交给overScller
 * <p>
 * <p>
 * 4.实现手指缩放功能
 * 交给ScaleGestureDetector
 */
public class ScallableView extends View {

    private static final float BITMAP_RANGE = ViewUtils.dp2px(200);
    private Bitmap bitmap;
    private Paint paint;
    private int width;
    private int height;
    private float smallScale;
    private float bigScale;

    private float smallScaleFrac;
    private float bigScaleFrac;
    private boolean bigFlag = true;
    GestureDetector gd;
    private ObjectAnimator animator;
    private float amplification_coefficient = 2.0f;
    //动画系数 初始值 为0
    private float fraction = 0;
    private float offSetX = 0;
    private float offSetY = 0;
    private float widthRange;
    private float heightRange;
    private OverScroller overScroller;
    private MyRunnable myRunnable;
    private ScaleGestureDetector scaleGestureDetector;

    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
        if(scaleFactor == smallScale && bigFlag){
            offSetX = 0;
            offSetY = 0;
        }
        invalidate();
    }

    public ScallableView(Context context) {
        super(context);
    }

    private ObjectAnimator getAnimator(float start, float end) {
        //懒加载
        if (animator == null) {
            animator = ObjectAnimator.ofFloat(this, "scaleFactor", 0);
        }
        //设置初始值 和默认值。
        animator.setFloatValues(start,end);
        return animator;
    }

    private float scaleFactor ;
    private boolean canScale = true;
    private boolean isScaleState = false;
    public ScallableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        overScroller = new OverScroller(context);
        myRunnable = new MyRunnable();
        bitmap = ViewUtils.getAvatar(getResources(), R.drawable.sc, (int) BITMAP_RANGE);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gd = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            //消费系列事件
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onDown(MotionEvent e) {
                isFlingFlag = false;
                return true;
            }
            //双击事件的处理 放大和缩小
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                scaleFlag = true;
                if (!isScaleState&&bigFlag) {
                    bigFlag = !bigFlag;
                    // 双击哪里 放大哪里 就是平移到哪里。
                    //双击放大缩小后 计算范围。
                    widthRange = (bitmap.getWidth() / 2f * bigScale - width / 2f);
                    heightRange = (bitmap.getHeight() / 2f * bigScale - height / 2f);
                    offSetX = (e.getX()-getWidth()/2f)-(e.getX()-getWidth()/2f)*bigScale/smallScale;
                    offSetY = (e.getY()-getHeight()/2f)-(e.getY()-getHeight()/2f)*bigScale/smallScale;

                    Log.i("dongdongdongdong",e.getX()+":e.getX");
                    Log.i("dongdongdongdong",e.getY()+":e.getY");
                    //放大
                    getAnimator(scaleFactor,bigScale).start();
                } else if(!isScaleState&&!bigFlag) {
                    bigFlag = !bigFlag;
                    //缩小
                    getAnimator(scaleFactor,smallScale).start();
                }else{
                    bigFlag = true;
                    isScaleState = !isScaleState;
                    getAnimator(scaleFactor,smallScale).start();
                }


                return super.onDoubleTap(e);
            }

            //拖拽功能实现
            //参数分析 按下的MotionEvent 当前的MotionEvent 偏移X 偏移Y
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!bigFlag && canScale) {
                    //只有放大时 有拖拽功能
                    offSetX -= distanceX;
                    offSetY -= distanceY;
                    //出现了超出范围的情况。要有一个最大最小值。
                    offSetX = Math.min(offSetX, widthRange);
                    offSetX = Math.max(offSetX, -widthRange);
                    offSetY = Math.min(offSetY, heightRange);
                    offSetY = Math.max(offSetY, -heightRange);

                    invalidate();
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            //惯性滑动
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //参数说明 当前位置 sumX sumY，速度 sumX sumY，x值的最大最小值，y值的最大最小值。
                overScroller.fling((int) offSetX, (int) offSetY, (int) velocityX, (int) velocityY, (int) -widthRange, (int) widthRange, (int) -heightRange, (int) heightRange);
                isFlingFlag = true;
                postOnAnimation(myRunnable);
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });


        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.OnScaleGestureListener() {
            private float beginScale;

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFlag = false;
                //放大倍数。
                float scale = detector.getScaleFactor();
               //在smallScale
                scaleFactor = beginScale * scale;
                scaleFactor = Math.max(scaleFactor,1);
                scaleFactor = Math.min(scaleFactor,bigScale);
                //animator.setFloatValues(1,2); 得到实际放大倍数后 设置FloatValues
                //刷新
                invalidate();
                return false;
            }

            //开始放大
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {

                //对X 和 Y进行重置
                //计算出X 和Y 的值 需要判断是否有offSet存在。
                float X = (offSetX/(1f-bigScale/smallScale)+getWidth()/2f);
                float Y = (offSetY/(1f-bigScale/smallScale)+getHeight()/2f);
                float widthPart= getWidth() / scaleFactor;
                float HeightPart= getHeight() / scaleFactor;
                float widthRatio = detector.getFocusX() / getWidth();
                float heightRatio = detector.getFocusY() / getHeight();
                X += (widthRatio-X/getWidth())*widthPart;
                Y += (heightRatio-Y/getHeight())*HeightPart;
                Log.i("dongdongdongdong","sumX.getX="+(offSetX/(1f-bigScale/smallScale)+getWidth()/2f)
                +"sumX.getY="+(offSetY/(1f-bigScale/smallScale)+getHeight()/2f));
                if(offSetX == 0 && offSetY == 0){
                    offSetX = (detector.getFocusX()-getWidth()/2f)-(detector.getFocusX()-getWidth()/2f)*bigScale/smallScale;
                    offSetY = (detector.getFocusY()-getHeight()/2f)-(detector.getFocusY()-getHeight()/2f)*bigScale/smallScale;
                }else{
                    offSetX = (X-getWidth()/2f)-(X-getWidth()/2f)*bigScale/smallScale;
                    offSetY = (Y-getHeight()/2f)-(Y-getHeight()/2f)*bigScale/smallScale;
                }

                Log.i("dongdongdong","width="+getWidth()+",height="+getHeight()+"offSetX = "+offSetX+",offSetY="+offSetY+","+",X="+X+","+",Y="+Y
                        +",focusX = "+detector.getFocusX()+",focusY="+detector.getFocusY());

                canScale = false;//开始时 禁用滑动
                if (beginScale == 0) {
                    if(bigFlag){
                        beginScale = smallScale;
                    }else{
                        beginScale = bigScale;
                    }

                } else {
                    beginScale = scaleFactor;
                }
                //返回true
                return true;
            }

            //结束放大。
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                //缩放结束 计算范围
                if(scaleFactor != smallScale){
                    //处于缩放状态
                    isScaleState= true;
                }
                if(scaleFactor >smallScale){
                    canScale = true;
                    bigFlag = false;
                    //双指之后，实际放大范围大于正常大小 计算可滑动范围。
                    widthRange = (bitmap.getWidth() / 2f*scaleFactor - width / 2f);
                    heightRange = (bitmap.getHeight() / 2f *scaleFactor - height / 2f);
                    widthRange = Math.max(widthRange,0);
                    heightRange = Math.max(heightRange,0);
                }else{
                    bigFlag = true;
                }
            }
        });
    }

    //测量结束后 尺寸发生变化。
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获得view 实际宽高
        width = w;
        height = h;
        //计算small 和big
        if ((float) bitmap.getWidth() / bitmap.getHeight() >= (float) w / h) {
            //说明宽为最小宽度
            smallScale = (float) w / bitmap.getWidth();
            bigScale = ((float) h / bitmap.getHeight()) * amplification_coefficient;
        } else {
            bigScale = (float) w / bitmap.getWidth() * amplification_coefficient;
            smallScale = ((float) h / bitmap.getHeight());
        }
        //计算可拖动的最大范围。
        //bitmap宽高的放大倍数*实际宽高减去view的宽高除2


        scaleFactor = smallScale;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // scaleFactor 根据scaleFactor来放大
        //如果是双击事件。

           if(scaleFactor<=smallScale){
               //手指滑动事件 缩小过程
               fraction = 0;
           }else{
               //手指滑动事件 放大过程
               fraction = (scaleFactor-smallScale)/(bigScale-smallScale);
           }


        //比例的计算
        canvas.translate(offSetX * fraction, offSetY * fraction);
        Log.i("fraction","fraction="+fraction+"offSet = "+offSetX+"offsetY = "+offSetY);
        canvas.scale(scaleFactor, scaleFactor, width / 2f, height / 2f);
        canvas.drawBitmap(bitmap, (width - bitmap.getWidth()) / 2f, (height - bitmap.getHeight()) / 2f, paint);


//       canvas.translate(offSetX * fraction, offSetY * fraction);
//       //scaleFactor 这个是双指缩放的放大倍数。
//        canvas.scale(scaleFactor, scaleFactor, width / 2f, height / 2f);
//        canvas.drawBitmap(bitmap, (width - bitmap.getWidth()) / 2f, (height - bitmap.getHeight()) / 2f, paint);

    }

    private boolean isFlingFlag = false;

    class MyRunnable implements Runnable {

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            //判断位置
            if (overScroller.computeScrollOffset() && isFlingFlag) {
                int currX = overScroller.getCurrX();
                int currY = overScroller.getCurrY();
                offSetX = currX;
                offSetY = currY;
                postInvalidate();
                postOnAnimation(this);
            }
        }
    }

    private boolean scaleFlag = false;
    private boolean eventFlag = false;//判断手势 防止一个手指抬起时，引起的冲突。
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
            eventFlag = true;//初始化
        }
        //首先交给scaleGestureDetector 如果不是 再交给gestureDetector
        boolean result = scaleGestureDetector.onTouchEvent(event);
        if (!scaleGestureDetector.isInProgress()) {
            if(event.getActionMasked() == MotionEvent.ACTION_POINTER_UP){
                //其中一个手指抬起
                eventFlag = false;
            }
            if(eventFlag){
                result = gd.onTouchEvent(event);
            }

        }
        return result;
    }
}
