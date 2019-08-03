package asiainfo.mview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;


public class MaterialEditText extends AppCompatEditText {

    //文字大小
    private static final float TEXT_SIZE = ViewUtils.dp2px(12);
    //文字与EditText的间隔
    private static final float TEXT_MARGIN = ViewUtils.dp2px(2);
    //文字左间距
    private static final float TEXT_LEFT = ViewUtils.dp2px(3);
    //文字y
    private static final float TEXT_TOP = ViewUtils.dp2px(18);
    private static final float TEXT_ANIMATOR_TOP = ViewUtils.dp2px(12);

    //是否设置弹出提示的
    private boolean isShowTipFlag = true;

    private Paint paint;
    //输入框内容是否为空
    private boolean isEmptyFlag = true;
    //动画进度
    private float fraction;
    private ObjectAnimator animator;


    public void setShowTipFlag(boolean showTipFlag) {

        //判断是否发生变化
        if(isShowTipFlag == showTipFlag){
            //不做任何操作。
        }else{
            isShowTipFlag = showTipFlag;

            if(isShowTipFlag){
                //显示

                setPadding(getPaddingLeft(), (int) (getPaddingTop()+TEXT_SIZE),getPaddingRight(),getPaddingBottom());
            }else{
                //不显示
                setPadding(getPaddingLeft(), (int) (getPaddingTop()+TEXT_SIZE),getPaddingRight(),getPaddingBottom());
            }
        }

    }

    public float getFraction() {
        return fraction;
    }

    public void setFraction(float fraction) {
        this.fraction = fraction;
        invalidate();
    }

    public MaterialEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }
    {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(TEXT_SIZE);
        paint.setColor(Color.parseColor("#FF3399"));

        setPadding(getPaddingLeft(), (int) (getPaddingTop()+TEXT_SIZE),getPaddingRight(),getPaddingBottom());
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //显示提示的话 就做下面的事。
                if(isShowTipFlag){
                    //内容变化后
                    if(isEmptyFlag && !TextUtils.isEmpty(s.toString())){
                        // 本来为空，变化为不为空
                        isEmptyFlag = false;
                        //产生动画
                        getAnimator().start();
                    }else if(!isEmptyFlag && TextUtils.isEmpty(s.toString())){
                        //本来不为空，为空
                        isEmptyFlag = true;
                        //产生动画
                        getAnimator().reverse();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAlpha((int) (0xff*(fraction)));

        canvas.drawText(getHint() == null?"默认提示":getHint().toString(),TEXT_LEFT,TEXT_TOP+TEXT_ANIMATOR_TOP*(1- fraction),paint);

    }


    private ObjectAnimator getAnimator(){
        if(animator == null){
            animator = ObjectAnimator.ofFloat(this, "fraction", 0, 1);
        }
      return animator;
    }
}
