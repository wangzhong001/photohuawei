package com.qihe.zzj.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.blankj.utilcode.util.ConvertUtils;
import com.qihe.zzj.R;


/**
 * Created by Arif on 2020/6/17.
 * Email 512542209@qq.com
 */
class VIPCardBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    private int maxOffsetHeight = 300;//最大下拉高度   px

    private int offset = 0;//当前下拉高度

    private boolean canObserve;//是否监听滑动事件

    private boolean observeFiling = true;//是否监听惯性滑动

    public VIPCardBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        maxOffsetHeight = ConvertUtils.dp2px(30);
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull V child, int layoutDirection) {
//        return super.onLayoutChild(parent, child, layoutDirection);
        //此处是为了防止重新布局时，页面位置错乱
        parent.onLayoutChild(child,layoutDirection);
        View VIPShade = parent.findViewById(R.id.view3);
        if (VIPShade == null) return true;
//        ViewCompat.offsetTopAndBottom(VIPShade,offset);//移动View
        return true;
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        Log.e("onStart",child.getId()+"");
        //只监听垂直滑动
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL && canObserve;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

        //被监听的view滑动事件
        //判断是否捕捉滑动
        //1 判断下拉时 当前view(即设置本VIPCardBehavior的view NestedScrollView) 是否可下拉 可下拉则不拦截
        if (dy<0 && child.canScrollVertically(-1)) return;

        //2 判断下拉时 VIP 卡遮罩下拉高度达到最大值（offset == maxOffsetHeight）不拦截
        if (dy<0 && offset == maxOffsetHeight) return;

        //3 判断上滑时 VIP 卡遮罩的下拉高度为0（offset 为 0） ，不拦截
        if (dy>0 && offset == 0) return;

        if (type == ViewCompat.TYPE_NON_TOUCH)
            Log.e("PreScroll",dy+"   observeFiling:"+observeFiling);

        //找到遮罩View
        View VIPShade = coordinatorLayout.findViewById(R.id.view3);
        if (VIPShade == null) return;

        //4 如果是惯性向下滑动 并且 遮罩未下拉，则不拦截（此处是为了当整体页面可下滑时，快速滑动后，惯性滑动使VIP遮罩下滑）
        if (type == ViewCompat.TYPE_NON_TOUCH && offset ==0 && dy < 0){
            return;
        }

        //处理拦截事件
        if (dy<0){ //下滑
            //如果当前滑动距离加上遮罩已下拉高度大于最大下拉高度，则消费部分滑动
            if (offset-dy>maxOffsetHeight){
                int useY = offset - maxOffsetHeight;//消费距离
                ViewCompat.offsetTopAndBottom(VIPShade,-useY);//移动View
                consumed[1] = useY; //告知已消费多少
                offset -= useY;//设置当前下拉高度
            }else{
                ViewCompat.offsetTopAndBottom(VIPShade,-dy);
                consumed[1] = dy;
                offset -= dy;
            }
        }else {//上滑
            //如果遮罩已下拉高度减去当前滑动距离小于0，则消费部分滑动
            if (offset-dy<0){
                int useY = offset;
                ViewCompat.offsetTopAndBottom(VIPShade,-useY);
                consumed[1] = useY; //告知已消费多少
                offset -= useY;//设置当前下拉高度
            }else{
                ViewCompat.offsetTopAndBottom(VIPShade,-dy);
                consumed[1] = dy;
                offset -= dy;
            }
        }
    }


    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull V child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        if (type == ViewCompat.TYPE_NON_TOUCH){
            observeFiling = true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull CoordinatorLayout parent, @NonNull V child, @NonNull MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            canObserve = judgeCanObserve(parent,ev);
        }else if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            canObserve = false;
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }


    /**
     * 判断手指按下位置是否在遮罩view 下方
     */
    private boolean judgeCanObserve(@NonNull CoordinatorLayout parent,MotionEvent event){
        View view = parent.findViewById(R.id.view3);
        final int[] location = new int[2];
        view.getLocationOnScreen (location);
        if (location[1] <=event.getRawY())
            return true;
        return false;
    }
}
