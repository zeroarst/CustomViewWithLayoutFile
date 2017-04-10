package lab.zeroarst.android.customviewwithlayoutfile;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import lab.zeroarst.android.customviewwithlayout.R;

/**
 * Displays spinning loading animation.
 */
public class LoadingImageView extends AppCompatImageView {

    //RotateAnimation mRotateAnim;
    private boolean mShowing = false;

    private AnimatorSet mASStart;

    private ViewPropertyAnimatorCompat mAlphaAnimator;

    private ObjectAnimator mOAAlpha;
    private ObjectAnimator mOARotate;

    public LoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //int defaultWH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, context.getResources().getDisplayMetrics());

        init();

    }

    public LoadingImageView(Context context) {
        super(context);
        //int defaultWH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, context.getResources().getDisplayMetrics());
        init();
    }


    private void init() {
        setImageResource(R.drawable.ic_loading);
        //setScaleType(ScaleType.CENTER);
        //setBackgroundColor(Color.argb(100, 255, 255, 255));

        //mExpectWidth = expectWidth;
        //mExpectHeight = expectHeight;
        //mFooter.loadingImg.clearAnimation();

        //mRotateAnim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //mRotateAnim.setDuration(1000);
        //mRotateAnim.setInterpolator(getContext(), android.R.anim.linear_interpolator);
        //mRotateAnim.setRepeatCount(Animation.INFINITE);

        //setVisibility(View.INVISIBLE);
    }


    public void start() {
        start(false);
    }

    public void start(boolean fadeIn) {
        mShowing = true;
        //start() method might be call before stop finish its anime, so we stop it manually here.
        if (mAlphaAnimator != null) {
            mAlphaAnimator.cancel();
            mAlphaAnimator = null;
        }

        mOARotate = ObjectAnimator.ofFloat(this, "rotation", 0f, 360f);
        mOARotate.setDuration(1000);
        mOARotate.setInterpolator(new LinearInterpolator());
        mOARotate.setRepeatMode(ValueAnimator.RESTART);
        mOARotate.setRepeatCount(ValueAnimator.INFINITE);

        mASStart = new AnimatorSet();
        mASStart.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        mOAAlpha = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1f).setDuration(fadeIn ? 500 : 0);
        mASStart.playTogether(mOARotate, mOAAlpha);
        mASStart.start();
    }

    public void stop() {
        stop(false);
    }

    public void stop(boolean fadeOut) {

        mShowing = false;

        if (mASStart != null) {
            mASStart.cancel();
            mASStart = null;
        }

        if (fadeOut) {
            mAlphaAnimator = ViewCompat.animate(this).setDuration(500).alpha(0f).setListener(new ViewPropertyAnimatorListener() {
                @Override
                public void onAnimationStart(View view) {

                }

                @Override
                public void onAnimationEnd(View view) {
                    setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(View view) {

                }
            });
        } else {
            // To resolve the issue in LoaderLayout when hide this image and show other views causes
            // the views being shown earlier then the hiding of this image if using above animation.
            setVisibility(View.GONE);
            setAlpha(0f);
        }
    }

    public boolean isShowing() {
        return mShowing;
    }

}
