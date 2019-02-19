package br.com.joaoapps.faciplac.carona.view.componentes.chat;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.card.MaterialCardView;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.joaov.faciplac.caronasolidaria.R;

import br.vince.owlbottomsheet.AnimationBuilder;
import br.vince.owlbottomsheet.OnClickInterceptor;

public class OwlBottomSheetBaseJv extends FrameLayout {
    MaterialCardView bottomSheet;
    AppCompatImageView icon;
    ViewGroup contentView;
    View scrimView;
    private View mActivityView;
    OnClickInterceptor interceptor;
    int ANIM_DURATION = 350;
    static boolean isExpanded = false;
    private AnimationBuilder animationBuilder;

    public OwlBottomSheetBaseJv(@NonNull Context context) {
        super(context);
        this.init();
    }

    public OwlBottomSheetBaseJv(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public OwlBottomSheetBaseJv(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService("layout_inflater");
        inflater.inflate(R.layout.owl_bottom_sheet, this, true);
        this.scrimView = this.getChildAt(0);
        this.bottomSheet = (MaterialCardView)this.getChildAt(1);
        this.icon = (AppCompatImageView)((ViewGroup)this.getChildAt(1)).getChildAt(0);
        this.contentView = (ViewGroup)((ViewGroup)this.getChildAt(1)).getChildAt(1);
        this.setupViews();
    }

    private void setupViews() {
        this.bottomSheet.setOnClickListener((v) -> {
            this.expand();
        });
        this.contentView.setVisibility(8);
        this.scrimView.setVisibility(8);
    }

    void collapse() {
        if (isExpanded) {
            this.initAnimBuilder();
            ValueAnimator widthMotion = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
            widthMotion.setDuration((long)(5 * this.ANIM_DURATION / 6));
            widthMotion.setStartDelay((long)(this.ANIM_DURATION / 3));
            widthMotion.setInterpolator(new AccelerateDecelerateInterpolator());
            ValueAnimator heightMotion = ValueAnimator.ofFloat(new float[]{0.0F, 1.0F});
            heightMotion.setDuration((long)this.ANIM_DURATION);
            heightMotion.setInterpolator(new FastOutSlowInInterpolator());
            widthMotion.addUpdateListener(this::expandXAnimation);
            heightMotion.addUpdateListener(this::expandYAnimation);
            widthMotion.start();
            heightMotion.start();
            this.contentView.setAlpha(1.0F);
            this.contentView.setVisibility(0);
            this.scrimView.setAlpha(1.0F);
            this.scrimView.setVisibility(0);
            this.icon.setAlpha(0.0F);
            this.icon.setVisibility(0);
            this.contentView.animate().alpha(0.0F).setDuration((long)(this.ANIM_DURATION / 2)).setStartDelay(0L).withEndAction(() -> {
                this.contentView.setVisibility(8);
            });
            this.scrimView.animate().alpha(0.0F).setDuration((long)(this.ANIM_DURATION / 2)).setStartDelay((long)(this.ANIM_DURATION / 2)).withEndAction(() -> {
                this.scrimView.setVisibility(8);
                if (this.interceptor != null) {
                    this.interceptor.onCollapseBottomSheet();
                }

            });
            this.icon.animate().alpha(1.0F).setStartDelay((long)(this.ANIM_DURATION / 2)).setDuration((long)(this.ANIM_DURATION / 2)).start();
            this.bottomSheet.animate().translationY(0.0F).setStartDelay(0L).setDuration((long)this.ANIM_DURATION).start();
            this.bottomSheet.animate().translationX(0.0F).setStartDelay((long)(this.ANIM_DURATION / 2)).setDuration((long)(this.ANIM_DURATION / 2)).start();
            isExpanded = false;
        }
    }

    void expand() {
        if (!isExpanded) {
            this.initAnimBuilder();
            ValueAnimator widthMotion = ValueAnimator.ofFloat(new float[]{1.0F, 0.0F});
            widthMotion.setDuration((long)(this.ANIM_DURATION / 3));
            widthMotion.setInterpolator(new DecelerateInterpolator());
            ValueAnimator heightMotion = ValueAnimator.ofFloat(new float[]{1.0F, 0.0F});
            heightMotion.setDuration((long)this.ANIM_DURATION);
            heightMotion.setInterpolator(new FastOutSlowInInterpolator());
            widthMotion.addUpdateListener(this::expandXAnimation);
            heightMotion.addUpdateListener(this::expandYAnimation);
            widthMotion.start();
            heightMotion.start();
            this.contentView.setAlpha(0.0F);
            this.contentView.setVisibility(0);
            this.scrimView.setAlpha(0.0F);
            this.scrimView.setVisibility(0);
            this.icon.setAlpha(1.0F);
            this.contentView.animate().alpha(1.0F).setStartDelay((long)(this.ANIM_DURATION / 2)).setDuration((long)(this.ANIM_DURATION / 2)).start();
            this.scrimView.animate().setStartDelay(0L).alpha(1.0F).setDuration((long)(this.ANIM_DURATION / 3)).start();
            this.icon.animate().alpha(0.0F).setDuration((long)(this.ANIM_DURATION / 3)).setStartDelay(0L).withEndAction(() -> {
                this.icon.setVisibility(8);
                if (this.interceptor != null) {
                    this.interceptor.onExpandBottomSheet();
                }

            });
            this.bottomSheet.animate().translationY((float)(-this.dp2px(48.0F))).setStartDelay(0L).setDuration((long)this.ANIM_DURATION).start();
            this.bottomSheet.animate().translationX((float)(-this.dp2px(42.0F))).setStartDelay(0L).setDuration((long)(this.ANIM_DURATION / 3)).start();
            isExpanded = true;
        }
    }

    private void expandXAnimation(ValueAnimator animation) {
        float fraction = (Float)animation.getAnimatedValue();
        this.animationBuilder.getCardView().setRadius(this.interpolate(this.animationBuilder.getFromRadius(), this.animationBuilder.getToRadius(), fraction));
        this.animationBuilder.getCardView().getLayoutParams().width = (int)((this.animationBuilder.getToWidth() - this.animationBuilder.getFromWidth()) * (1.0F - fraction) + this.animationBuilder.getFromWidth());
        this.animationBuilder.getCardView().requestLayout();
    }

    private void expandYAnimation(ValueAnimator animation) {
        float fraction = (Float)animation.getAnimatedValue();
        this.animationBuilder.getCardView().getLayoutParams().height = (int)((this.animationBuilder.getToHeight() - this.animationBuilder.getFromHeight()) * (1.0F - fraction) + this.animationBuilder.getFromHeight());
        this.animationBuilder.getCardView().requestLayout();
    }

    private float interpolate(float from, float to, float fraction) {
        return (from - to) * fraction + to;
    }

    private void initAnimBuilder() {
        if (this.animationBuilder == null) {
            this.animationBuilder = new AnimationBuilder();
            this.animationBuilder.setCardView(this.bottomSheet);
            this.animationBuilder.setFromHeight((float)this.bottomSheet.getHeight());
            this.animationBuilder.setFromRadius(this.bottomSheet.getRadius());
            this.animationBuilder.setFromWidth((float)this.bottomSheet.getWidth());
            this.animationBuilder.setFromX(this.bottomSheet.getX());
            this.animationBuilder.setFromY(this.bottomSheet.getY());
            this.animationBuilder.setToHeight((float)this.getContentView().getHeight());
            this.animationBuilder.setToWidth((float)this.getContentView().getWidth());
            this.animationBuilder.setToRadius(0.0F);
            this.animationBuilder.setToX(0.0F);
            this.animationBuilder.setToY(0.0F);
        }
    }

    void setActivityView(AppCompatActivity activity) {
        this.mActivityView = activity.getWindow().getDecorView().findViewById(16908290) != null ? activity.getWindow().getDecorView().findViewById(16908290) : this.findViewById(16908290).getRootView();
    }

    private int dp2px(float dp) {
        float scale = this.getContext().getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5F);
    }

    private View getContentView() {
        return this.mActivityView;
    }
}
