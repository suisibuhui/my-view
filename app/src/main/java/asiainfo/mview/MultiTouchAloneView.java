package asiainfo.mview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

public class MultiTouchAloneView extends View {

    private static final float WIDTH = ViewUtils.dp2px(100);
    private Paint paint;
    private SparseArray<Path> pathSparseArray;


    public MultiTouchAloneView(Context context) {
        super(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public MultiTouchAloneView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        pathSparseArray = new SparseArray<Path>();
    }

    public MultiTouchAloneView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionMasked = event.getActionMasked();

        switch(actionMasked){
            case MotionEvent.ACTION_DOWN:

            case MotionEvent.ACTION_POINTER_DOWN:
                Path path = new Path();
                int actionIndex = event.getActionIndex();
                int pointerId = event.getPointerId(actionIndex);
                path.moveTo(event.getX(actionIndex),event.getY(actionIndex));
                pathSparseArray.append(pointerId, path);
                break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < pathSparseArray.size(); i++) {
                     pointerId = event.getPointerId(i);
                    path = pathSparseArray.get(pointerId);
                    path.lineTo(event.getX(i),event.getY(i));
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                //从集合中删除
                pathSparseArray.remove(event.getPointerId(event.getActionIndex()));
                invalidate();
                break;

        }
        return true;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < pathSparseArray.size(); i++) {
            Path path = pathSparseArray.valueAt(i);
            canvas.drawPath(path,paint);

        }
    }
}
