package asiainfo.mview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MultiTouchGrapView extends View {

    private static final float WIDTH = ViewUtils.dp2px(100);
    private Bitmap bitmap;
    private Paint paint;
    private int pointerId;

    public MultiTouchGrapView(Context context) {
        super(context);
    }

    public MultiTouchGrapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmap = ViewUtils.getAvatar(getResources(), R.drawable.touiang, (int) WIDTH);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    public MultiTouchGrapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    float downX ;
    float downY ;
    float originalOffSetX;
    float originalOffSetY;
    float offSetX ;
    float offSetY ;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        switch(actionMasked){

            case MotionEvent.ACTION_DOWN:

                pointerId = event.getPointerId(event.getActionIndex());
                downX = event.getX(event.findPointerIndex(pointerId));
                downY = event.getY(event.findPointerIndex(pointerId));
                originalOffSetX = offSetX;
                originalOffSetY = offSetY;
                break;

            case MotionEvent.ACTION_MOVE:
                offSetX = originalOffSetX + event.getX(event.findPointerIndex(pointerId))-downX;
                offSetY = originalOffSetY + event.getY(event.findPointerIndex(pointerId))-downY;

                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                int actionIndex = event.getActionIndex();
                pointerId = event.getPointerId(actionIndex);
                //重新记录 downX 和位移。
                downX = event.getX(event.findPointerIndex(pointerId));
                downY = event.getY(event.findPointerIndex(pointerId));
                originalOffSetX = offSetX;
                originalOffSetY = offSetY;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                //抬起的id
                int actionIndexUp = event.getActionIndex();
                int pointerIdPointerUp = event.getPointerId(actionIndexUp);
                if(pointerId == pointerIdPointerUp){
                    //判断抬起的是不是最后一个手指
                    if(actionIndexUp == event.getPointerCount()-1){
                        //是的话，就给pointer赋值为他的前一个手指
                        pointerId = event.getPointerId(event.getPointerCount()-2);
                    }else{
                        //不是的话 赋值给最后一个手指
                        pointerId = event.getPointerId(event.getPointerCount()-1);
                    }


                }else{
                    //抬起的不是当前滑动手指 那你随便咯
                }
                downX = event.getX(event.findPointerIndex(pointerId));
                downY = event.getY(event.findPointerIndex(pointerId));
                originalOffSetX = offSetX;
                originalOffSetY = offSetY;
                break;
            case MotionEvent.ACTION_UP:
                break;

        }

        return true;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap,offSetX,offSetY,paint);
    }
}
