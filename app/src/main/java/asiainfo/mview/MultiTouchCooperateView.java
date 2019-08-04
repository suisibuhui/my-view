package asiainfo.mview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MultiTouchCooperateView extends View {

    private static final float WIDTH = ViewUtils.dp2px(100);
    private Bitmap bitmap;
    private Paint paint;


    public MultiTouchCooperateView(Context context) {
        super(context);
    }

    public MultiTouchCooperateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmap = ViewUtils.getAvatar(getResources(), R.drawable.touiang, (int) WIDTH);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    public MultiTouchCooperateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    float downX ;
    float downY ;
    float originalOffSetX;
    float originalOffSetY;
    float offSetX ;
    float offSetY ;
    float focusX ;
    float focusY ;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        //初始化
        float sumX = 0;
        float sumY = 0;
        for (int i = 0; i < event.getPointerCount(); i++) {
            sumX += event.getX(i);
            sumY += event.getY(i);
        }
        focusX = sumX/event.getPointerCount();
        focusY = sumY/event.getPointerCount();
        switch(actionMasked){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                downX = focusX;
                downY = focusY;
                originalOffSetX = offSetX;
                originalOffSetY = offSetY;
                break;
            case MotionEvent.ACTION_MOVE:
                offSetX = originalOffSetX + focusX - downX;
                offSetY = originalOffSetY + focusY - downY;
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_UP:
                int actionIndexUp = event.getActionIndex();
                sumX = 0;
                sumY = 0;
                for (int i = 0; i < event.getPointerCount() ; i++) {
                    //抬起的这根手指 不计入计算。
                    if(actionIndexUp == i){
                    }else{
                        sumX += event.getX(i);
                        sumY += event.getY(i);
                    }
                }
                focusX = sumX/(event.getPointerCount() - 1);
                focusY = sumY/(event.getPointerCount() - 1);
                downX = focusX;
                downY = focusY;
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
