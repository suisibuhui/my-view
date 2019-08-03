package asiainfo.mview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class FlipView extends View {

    //默认宽高
    private static final float DEFAULTWIDTH = ViewUtils.dp2px(150);
    private Bitmap bitmap;
    private Paint paint;
    private Camera camera;
    private float bottomFilp;
    private float canvasRotate;;

    public FlipView(Context context) {
        super(context);
        init();
    }

    public FlipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){
       bitmap = ViewUtils.getAvatar(getResources(),R.drawable.line, (int) DEFAULTWIDTH);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        camera = new Camera();
        camera.setLocation(0,0,ViewUtils.getZForCamera(-8));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //上半部分 不动
        canvas.save();
        canvas.translate(getWidth()/2,getHeight()/2);
        canvas.rotate(-canvasRotate);
        camera.save();
        camera.rotateX(0);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.clipRect(-bitmap.getWidth()/2-400,-bitmap.getHeight()/2-400,bitmap.getWidth()/2+400,0);
        canvas.rotate(canvasRotate);
        canvas.translate(-getWidth()/2,-getHeight()/2);
        canvas.drawBitmap(bitmap,getWidth()/2-bitmap.getWidth()/2,getHeight()/2-bitmap.getHeight()/2,paint);
        canvas.restore();

        //下半部分翻转45度 流程 绘制 平移到中间 切割 camere apply 平移回来  倒着写
        canvas.save();
        canvas.translate(getWidth()/2,getHeight()/2);
        canvas.rotate(-canvasRotate);
        camera.save();
        camera.rotateX(bottomFilp);
        camera.applyToCanvas(canvas);
        camera.restore();
        canvas.clipRect(-bitmap.getWidth()/2-400,0,bitmap.getWidth()/2+400,bitmap.getHeight()/2+400);
        canvas.rotate(canvasRotate);
        canvas.translate(-getWidth()/2,-getHeight()/2);
        canvas.drawBitmap(bitmap,getWidth()/2-bitmap.getWidth()/2,getHeight()/2-bitmap.getHeight()/2,paint);
        canvas.restore();

    }

    public void startAnimator(){
        ObjectAnimator bottomFilp = ObjectAnimator.ofFloat(this, "bottomFilp", 0, 45);
        bottomFilp.setDuration(3000);

        ObjectAnimator rotateAnimaor = ObjectAnimator.ofFloat(this, "canvasRotate", 0,360);
        rotateAnimaor.setDuration(6000);

        ObjectAnimator bottomFilpRecover = ObjectAnimator.ofFloat(this, "bottomFilp", 45,0);
        bottomFilpRecover.setDuration(3000);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(bottomFilp,rotateAnimaor,bottomFilpRecover);
        animatorSet.start();
    }

    public float getCanvasRotate() {
        return canvasRotate;
    }

    public void setCanvasRotate(float canvasRotate) {
        this.canvasRotate = canvasRotate;
        invalidate();
    }

    public float getBottomFilp() {
        return bottomFilp;
    }

    public void setBottomFilp(float bottomFilp) {
        this.bottomFilp = bottomFilp;
        invalidate();
    }
}
