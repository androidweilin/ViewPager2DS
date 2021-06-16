package com.example.viewpager2ds;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.lang.invoke.CallSite;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Integer> data;
    private List<View> views;
    private int[] imgs = new int[]{
            R.mipmap.img_1,
            R.mipmap.img_2,
            R.mipmap.img_3,
            R.mipmap.img_4,
            R.mipmap.img_5,
            R.mipmap.img_6,
            R.mipmap.img_7,
            R.mipmap.img_8,
            R.mipmap.img_9,
    };
    private List<Bean> paramsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);
        views = new ArrayList<>();
        views.add(findViewById(R.id.one));
        views.add(findViewById(R.id.two));
        views.add(findViewById(R.id.three));
        views.add(findViewById(R.id.four));
        views.add(findViewById(R.id.five));
        views.add(findViewById(R.id.six));
        views.add(findViewById(R.id.seven));
        views.add(findViewById(R.id.eight));
        views.add(findViewById(R.id.nine));
        paramsList = new ArrayList<>();
        for (View item : views) {
            Bean bean = new Bean();
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) item.getLayoutParams();
            bean.setCircleAngle(layoutParams.circleAngle);
            bean.setCircleRadius(layoutParams.circleRadius);
            bean.setRotation(item.getRotation());
            paramsList.add(bean);
        }
        data = new ArrayList<>();
        for (int i = 0; i < imgs.length; i++) {
            data.add(imgs[i]);
        }
        setData();
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            view.setTag("img_" + i);
            if (i == 0 || i == 8) {
                view.setTranslationZ(1);
            } else if (i == 1 || i == 7) {
                view.setTranslationZ(2);
            } else if (i == 2 || i == 6) {
                view.setTranslationZ(3);
            } else if (i == 3 || i == 5) {
                view.setTranslationZ(4);
            } else {
                view.setTranslationZ(5);
            }
        }
        for (View view : views) {
            view.setOnTouchListener(5 == view.getTranslationZ() ? new MyOnTouchLister() : null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("tag", "tag:" + v.getTag());
                    ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) v.getLayoutParams();
                    Log.e("tag", "circleRadius:" + layoutParams.circleRadius);
                    Log.e("tag", "circleAngle:" + layoutParams.circleAngle);
                    Log.e("tag", "rotation:" + v.getRotation());
                }
            });
        }
    }

    private void downData() {
        setData(false);
    }

    private void setData(boolean up) {
        for (int i = 0; i < views.size(); i++) {
//            views.get(i).setBackgroundResource(data.get(i));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                switchMenu(views.get(i), up, i);
            }
        }
    }

    private void setData() {
        for (int i = 0; i < views.size(); i++) {
            views.get(i).setBackgroundResource(data.get(i));
            views.get(i).setTag(data.get(i));
        }
    }

    private void upDaTa() {
        setData(true);
    }


    private void switchMenu(View view, boolean up, int index) {
        Log.e("tag", "tag" + view.getTag());
        int i = views.indexOf(view);
        Log.e("tag", "i" + i);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        int startRadius = layoutParams.circleRadius;
        float startC = layoutParams.circleAngle;
        float rotation = view.getRotation();
        Bean end;
        if (up) {
            if (i - 1 < 0) {
                end = paramsList.get(paramsList.size() - 1);

            } else {
                end = paramsList.get(i - 1);
            }
        } else {
            if (paramsList.size() == i + 1) {
                end = paramsList.get(0);
            } else {
                end = paramsList.get(i + 1);
            }
        }
        int endRadius = end.circleRadius;
        float endC = end.circleAngle;
        float roEnd = end.rotation;

        ValueAnimator animRadius = ValueAnimator.ofInt(startRadius, endRadius);
        ValueAnimator animC = ValueAnimator.ofFloat(startC, endC);
        ValueAnimator animR = ValueAnimator.ofFloat(rotation, roEnd);
        animRadius.setDuration(300);
        animC.setDuration(300);
        animR.setDuration(300);
        animRadius.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                layoutParams.circleRadius = animatedValue;
                view.setLayoutParams(layoutParams);
            }
        });
        animC.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                layoutParams.circleAngle = animatedValue;
                view.setLayoutParams(layoutParams);
            }
        });
        animR.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                view.setRotation(animatedValue);
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.play(animRadius).with(animC).with(animR);
        set.setDuration(300);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                view.setRotation(view.getRotation() + (up ? 10 : -10));
//                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) view.getLayoutParams();
//                lp.circleAngle = lp.circleAngle + ((up ? 10 : -10));
//                lp.circleRadius = lp.circleRadius + ((up ? ro : -ro));
//                view.setLayoutParams(lp);
                if (roEnd == 0) {
                    view.setTranslationZ(5f);
                } else if (roEnd == 10 || roEnd == -10) {
                    view.setTranslationZ(4f);
                } else if (roEnd == 20 || roEnd == -20) {
                    view.setTranslationZ(3f);
                } else if (roEnd == 30 || roEnd == -30) {
                    view.setTranslationZ(2f);
                } else if (roEnd == 40 || roEnd == -40) {
                    view.setTranslationZ(1f);
                }
                if (index == views.size() - 1) {
                    upSet(up);
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    private void upSet(boolean up) {
        if (up) {
            View view = views.get(0);
            views.remove(view);
            views.add(view);
        } else {
            View view = views.get(views.size() - 1);
            views.remove(view);
            views.add(0, view);
        }
        for (View view : views) {
            float translationZ = view.getTranslationZ();
            view.setOnTouchListener(translationZ == 5 ? new MyOnTouchLister() : null);
            Log.e("tag", "translationZ : " + translationZ);
        }
    }

    private class MyOnTouchLister implements View.OnTouchListener {
        int dX;
        int dY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    dY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float y = event.getY();
                    if (y - dY > 0) {//下
                        downData();
                    } else {//上
                        upDaTa();
                    }
                    break;
            }
            return true;
        }
    }
}