package asiainfo.mview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.TypedValue;

/**
 * 自定义view 常用工具类
 */
public class ViewUtils {

    public static  int sp2px(int value){
        float scaledDensity = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int)scaledDensity*value;
    }

    public static  float dp2px(int value){
        float scaledDensity = Resources.getSystem().getDisplayMetrics().density;
        return (int)scaledDensity*value;
    }


    /**
     * 获取缩放后的bitmap
     * @param resources
     * @param layoutId
     * @param width
     * @return
     */
    public static Bitmap getAvatar(Resources resources,int layoutId,int width){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources,layoutId,options);
        int outWidth = options.outWidth;
        int inSampleSize = 1;
        while(outWidth > width){
            inSampleSize *=2;
            outWidth /=2;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeResource(resources, layoutId, options);
    }

    /**
     * 获取Z轴的放大倍数 一般-8为宜
     * @param magnifyNum
     * @return
     */
    public static float getZForCamera(int magnifyNum){
        return  magnifyNum * Resources.getSystem().getDisplayMetrics().density;
    }
}
