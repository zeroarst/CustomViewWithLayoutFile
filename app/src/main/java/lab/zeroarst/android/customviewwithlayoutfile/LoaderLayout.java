package lab.zeroarst.android.customviewwithlayoutfile;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lab.zeroarst.android.customviewwithlayout.AppController;
import lab.zeroarst.android.customviewwithlayout.R;

import static android.animation.Animator.AnimatorListener;

/**
 * An handy layout that displays loading animation and messages.
 * Can be predefined in xml layout file or dynamically used in code.
 * Note only a sub child view can be contained, otherwise an exception raises.
 */
public class LoaderLayout extends RelativeLayout {

    private LinearLayout mLoaderViewsContainerLo;

    private LoadingImageView mLoadingIv;

    private TextView mMsgTv;

    private View mContentView;

    private Button mBtn;

    public LoaderLayout(FragmentActivity activity) {
        super(activity);
        init();
    }

    public LoaderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Retrieves the loading image view. Note this could be null if called before it is initialized.
     *
     * @return
     */
    @Nullable
    public LoadingImageView getLoadingImageView() {
        return mLoadingIv;
    }

    /**
     * Sets message text color.
     *
     * @param color
     */
    public void setMessageTextColor(int color) {
        if (mMsgTv != null)
            mMsgTv.setTextColor(color);
    }

    /**
     * Set message text size.
     *
     * @param size Sp as unit.
     */
    public void setMessageTextSize(int size) {
        if (mMsgTv != null)
            mMsgTv.setTextSize(size);
    }

    /**
     * Retrieves the message view. Note this could be null if called before it is initialized.
     *
     * @return
     */
    @Nullable
    public TextView getMessageTextView() {
        return mMsgTv;
    }

    private String mBtnTxt;

    /**
     * Sets button text res id.
     *
     * @param resId
     */
    public void setButtonText(int resId) {
        if (mBtn != null)
            mBtn.setText(AppController.getContext().getString(resId));
    }

    /**
     * Sets button text.
     *
     * @param text
     */
    public void setButtonText(String text) {
        mBtnTxt = text;
        if (mBtn != null)
            mBtn.setText(text);
    }

    // Default message color.
    private int mBtnTxtColor = Color.BLACK;

    /**
     * Sets button text color.
     *
     * @param color
     */
    public void setButtonTextColor(int color) {
        mBtnTxtColor = color;
        if (mBtn != null)
            mBtn.setTextColor(color);
    }

    @Nullable
    public Button getButton() {
        return mBtn;
    }

    public void setButtonClickListener(OnClickListener listener) {
        mBtn.setOnClickListener(listener);
    }

    public void showButton(@Nullable OnClickListener listener) {
        if (mBtn == null)
            return;
        mBtn.setVisibility(View.VISIBLE);

        if (listener == null)
            return;
        setButtonClickListener(listener);
    }

    public void hideButton() {
        if (mBtn == null)
            return;
        mBtn.setVisibility(View.GONE);
        mBtn.setOnClickListener(null);
    }

    private void init() {

        // Intercept click event to avoid being passed to background.
        setClickable(true);

        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setContentView(View v) {
        if (v != null) {
            mContentView = v;
            addView(v, 0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new RuntimeException("LoaderLayout can only have one and child view.");
        }
        if (getChildCount() == 1)
            mContentView = getChildAt(0);

        inflate(getContext(), R.layout.loader_layout_views, this);

        mLoaderViewsContainerLo = (LinearLayout) findViewById(R.id.lo_loading_msg_container);

        mMsgTv = (TextView) findViewById(R.id.tv_loader_message);

        mLoadingIv = (LoadingImageView) findViewById(R.id.iv_loader_img);

        mBtn = (Button) findViewById(R.id.loader_btn);
    }

    /**
     * Show loading animation.
     *
     * @param hideContent Indicate to show/hide content view. The content view is the view behind the loader views.
     */
    public synchronized void showLoader(boolean hideContent) {
        showLoader(hideContent, false);
    }

    /**
     * Show loading animation.
     *
     * @param hideContent Indicates to show/hide content view. The content view is the view behind the loader views.
     * @param fadeIn      Indicates whether to user fade in animation.
     */
    public synchronized void showLoader(boolean hideContent, boolean fadeIn) {
        if (mLoaderViewsContainerLo != null) {
            mLoaderViewsContainerLo.setVisibility(View.VISIBLE);
        }
        if (mMsgTv != null) {
            mMsgTv.setVisibility(View.GONE);
        }
        if (mBtn != null) {
            mBtn.setVisibility(View.GONE);
        }
        if (mLoadingIv != null) {
            mLoadingIv.start(fadeIn);
        }
        if (hideContent)
            hideContentView();
    }

    /**
     * Hide loading animation.
     *
     * @param showContent Indicates whether to display content view.
     */
    public synchronized void hideLoader(boolean showContent) {
        hideLoader(showContent, false);
    }

    /**
     * Hide loading animation.
     *
     * @param showContent Indicates whether to display content view.
     * @param fadeOut     Indicates whether to user fade out animation.
     */
    public synchronized void hideLoader(boolean showContent, boolean fadeOut) {
        if (mLoaderViewsContainerLo != null) {
            mLoaderViewsContainerLo.setVisibility(View.GONE);
        }
        if (mLoadingIv != null) {
            mLoadingIv.stop(fadeOut);
        }
        if (showContent)
            showContentView();
    }

    /**
     * Show message.
     *
     * @param resId String resource id.
     */
    public synchronized void showMessage(int resId) {
        showMessage(getContext().getString(resId));
    }

    /**
     * Show message.
     *
     * @param msg Message content.
     */
    public synchronized void showMessage(String msg) {
        if (mLoaderViewsContainerLo != null) {
            mLoaderViewsContainerLo.setVisibility(View.VISIBLE);
        }

        boolean isLoadingImageAnimating = false;
        // Call to stop loading animation in case it is animating.
        if (mLoadingIv != null) {
            if (mLoadingIv.isShowing())
                isLoadingImageAnimating = true;
            mLoadingIv.stop();
        }

        if (mMsgTv != null) {
            mMsgTv.setText(msg);

            // Solve the issue that hiding spinning image and showing msg text causes a flash of moving up text.
            // We give the showing text view a small delay to prevent this issue.
            if (isLoadingImageAnimating)
                mMsgTv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMsgTv.setVisibility(View.VISIBLE);
                    }
                }, 100);
            else
                mMsgTv.setVisibility(View.VISIBLE);
        }

        if (mBtn != null) {
            mBtn.setVisibility(View.GONE);
        }
        hideContentView();
    }

    /**
     * Show message.
     *
     * @param msgResId            Message resource id.
     * @param btnTxtResId         Button text resource id.
     * @param buttonClickListener Optional. If given retry button will be shown.
     */
    public synchronized void showMessageAndButton(int msgResId, int btnTxtResId, OnClickListener buttonClickListener) {
        showMessageAndButton(getContext().getString(msgResId), getContext().getString(btnTxtResId), buttonClickListener);
    }

    /**
     * Show message.
     *
     * @param msg                 Message.
     * @param btnTxtResId         Button text resource id.
     * @param buttonClickListener Optional. If given retry button will be shown.
     */
    public synchronized void showMessageAndButton(String msg, int btnTxtResId, OnClickListener buttonClickListener) {
        showMessageAndButton(msg, getContext().getString(btnTxtResId), buttonClickListener);
    }

    /**
     * Show message.
     *
     * @param msg                 Message content.
     * @param buttonClickListener Optional. If given retry button will be shown.
     */
    public synchronized void showMessageAndButton(String msg, @Nullable String buttonText, OnClickListener buttonClickListener) {
        if (mLoaderViewsContainerLo != null) {
            mLoaderViewsContainerLo.setVisibility(View.VISIBLE);
        }

        // Call to stop loading animation in case it is animating.
        if (mLoadingIv != null) {
            mLoadingIv.stop();
        }

        if (mMsgTv != null) {
            mMsgTv.setText(msg);
            mMsgTv.setVisibility(View.VISIBLE);
        }

        if (mBtn != null) {
            if (!TextUtils.isEmpty(buttonText) && buttonClickListener != null) {
                setButtonText(buttonText);
                showButton(buttonClickListener);
            } else
                mBtn.setVisibility(View.GONE);
        }

        hideContentView();
    }

    /**
     * Hide message.
     */
    public synchronized void hideMessage() {
        if (mLoaderViewsContainerLo != null) {
            mLoaderViewsContainerLo.setVisibility(View.GONE);
        }
        showContentView();
    }

    /**
     * Show both loading animation and message. Loading image will be above message.
     *
     * @param msg Message content.
     */
    public synchronized void showLoaderAndMessage(String msg) {
        if (mLoaderViewsContainerLo != null) {
            mLoaderViewsContainerLo.setVisibility(View.VISIBLE);
        }
        if (mLoadingIv != null) {
            mLoadingIv.start();
        }
        if (mMsgTv != null) {
            mMsgTv.setText(msg);
            mMsgTv.setVisibility(View.VISIBLE);
        }

        hideContentView();
    }

    /**
     * Hide loading animation and message.
     */
    public synchronized void hideLoaderAndMessage() {
        if (mLoaderViewsContainerLo != null) {
            mLoaderViewsContainerLo.setVisibility(View.GONE);
        }
        // Call to stop loading animation in case it is animating.
        if (mLoadingIv != null) {
            mLoadingIv.stop();
        }
        showContentView();
    }

    /**
     * Starts a fade in animation. Sets the visibility to visible after animation.
     */
    private synchronized void showContentView() {
        showContentView(null);
    }

    /**
     * Starts a fade in animation. Sets the visibility to visible after animation.
     */
    public synchronized void showContentView(@Nullable ObjectAnimator oa) {

        if (mContentView == null)
            return;

        AnimatorListener listener = new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mContentView.setVisibility(View.VISIBLE);
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
        };

        ViewCompat.animate(mContentView).cancel();
        if (oa != null) {
            if (oa.getTarget() != mContentView)
                oa.setTarget(oa);
            oa.addListener(listener);
            oa.start();
        } else
            mContentView.animate().setDuration(500).alpha(1.0f).setListener(listener);
    }

    /**
     * Starts a fade out animation. Sets the visibility to invisible after animation.
     */
    public synchronized void hideContentView() {
        hideContentView(null);
    }

    /**
     * Starts a fade out animation. Sets the visibility to invisible after animation.
     */
    public synchronized void hideContentView(@Nullable ObjectAnimator oa) {
        // Set as INVISIBLE
        if (mContentView == null || mContentView.getVisibility() == INVISIBLE)
            return;

        AnimatorListener listener = new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mContentView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };

        ViewCompat.animate(mContentView).cancel();
        if (oa != null) {
            if (oa.getTarget() != mContentView)
                oa.setTarget(oa);
            oa.addListener(listener);
            oa.start();
        } else
            mContentView.animate().setDuration(0).alpha(0.0f).setListener(listener);
    }


    /**
     * If {@linkplain Exception#getMessage()} is not empty, displays it, otherwise display {@linkplain R
     * .string#msg_something_is_wrong}. Also displays retry button if {@code listener} is given.
     *
     * @param e
     * @param listener
     */
    public synchronized void showExceptionMessage(Exception e, OnClickListener listener) {
        showMessageAndButton(TextUtils.isEmpty(e.getMessage()) ? getContext().getString(R.string.msg_err_occur) : e
                        .getMessage(),
                getContext().getString(R.string.btn_retry), listener);
    }

    /**
     * If {@linkplain Exception#getMessage()} is not empty, displays it, otherwise display {@linkplain R
     * <p/>
     * .string#msg_something_is_wrong}.
     *
     * @param e
     */
    public synchronized void showExceptionMessage(Exception e) {
        showExceptionMessage(e, null);
    }

    public boolean isContentShowing() {
        return mContentView != null && (mContentView.getVisibility() == View.VISIBLE);
    }

    public boolean isMessageShowing() {
        return mMsgTv != null && (mMsgTv.getVisibility() == View.VISIBLE) && mLoaderViewsContainerLo != null && mLoaderViewsContainerLo
                .getVisibility() == View.VISIBLE;
    }
}
