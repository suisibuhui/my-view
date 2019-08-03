package asiainfo.mview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MultiTouchView extends View {

    private static final float WIDTH = ViewUtils.dp2px(200);
    private Bitmap bitmap;
    private Paint paint;

    public MultiTouchView(Context context) {
        super(context);
    }

    public MultiTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bitmap = ViewUtils.getAvatar(getResources(), R.drawable.touiang, (int) WIDTH);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public MultiTouchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap,moveX,moveY,paint);
    }
    float moveX = 0;
    float moveY = 0;
    float currentMoveX;
    float currentMoveY;
    float offSetX ;
    float offSetY ;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();
        switch(actionMasked){

            case MotionEvent.ACTION_DOWN:
                offSetX = event.getX();
                offSetY = event.getY();

                break;

            case MotionEvent.ACTION_MOVE:
                moveX = currentMoveX + event.getX()-offSetX;
                moveY = currentMoveY + event.getY()-offSetY;
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                currentMoveX = moveX;
                currentMoveY = moveY;
                break;

        }

        return true;
    }

}
