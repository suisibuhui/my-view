# 我的自定义view	

---
---

## **1.CircleView**
---  

### 使用
  
       <attr name="backgroundColor" format="color"/>
       <attr name="progressColor" format="color"/>
       <attr name="textColor" format="color"/>
       <attr name="textSize" format="dimension"/>
  
可在xml中设置运动圆环的背景颜色，进度条颜色，文字颜色，和文字大小，默认宽高100dp。  

![效果图](https://github.com/suisibuhui/my-view/blob/master/gifs/circleview.gif)

## **2.FlipView**
---
继承view，使用camera类配合canvas类做3D旋转。

![效果图](https://github.com/suisibuhui/my-view/blob/master/gifs/filpview.gif)

## **3.MyMaterialEditText**
---
继承EditText，仿写github扔物线的开源项目，实现部分功能。

![效果图](https://github.com/suisibuhui/my-view/blob/master/gifs/materialedittext.gif)

## **4.ScallableImageView**
---
继承view，主要使用GestureDetector类、OverScroller类、实现图片的放大缩小，滑动和惯性滑动，以及双指捏撑。

![效果图](https://github.com/suisibuhui/my-view/blob/master/gifs/scaleimageview.gif)

## **5.TaglayoutView**
---
继承ViewGroup，完成子view的布局，子view宽高可以是不规则的，为了好看我设置的规则子view。

![效果图](https://upload-images.jianshu.io/upload_images/13400445-b2b8dbc2067d3293.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/320 )


## **5.简单的例子，感受多点触控**
---

- MultiTouchGrapView 抢占式

![效果图](https://github.com/suisibuhui/my-view/blob/master/gifs/MultiTouchGrapView.gif)

- MultiTouchCooperateView 协作式

![效果图](https://github.com/suisibuhui/my-view/blob/master/gifs/MultiTouchCooperateView.gif) 

- MultiTouchAloneView 互不干扰式

![效果图](https://github.com/suisibuhui/my-view/blob/master/gifs/MultiTouchAloneView.gif)

### **6.仿点赞**
---
