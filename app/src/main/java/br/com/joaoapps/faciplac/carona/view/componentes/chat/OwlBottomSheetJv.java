package br.com.joaoapps.faciplac.carona.view.componentes.chat;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
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

import br.vince.owlbottomsheet.AnimationBuilder;
import br.vince.owlbottomsheet.OnClickInterceptor;

public class OwlBottomSheetJv extends OwlBottomSheetBaseJv {
    public OwlBottomSheetJv(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OwlBottomSheetJv(@NonNull Context context) {
        super(context);
    }

    public OwlBottomSheetJv(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setIcon(@DrawableRes int drawable) {
        this.icon.setImageResource(drawable);
    }

    public void setBottomSheetColor(@ColorInt int color) {
        this.bottomSheet.setCardBackgroundColor(color);
    }

    public void setActivityView(AppCompatActivity activity) {
        super.setActivityView(activity);
    }

    public void setOnClickInterceptor(OnClickInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public int getDuration() {
        return this.ANIM_DURATION;
    }

    public void setDuration(int duration) {
        this.ANIM_DURATION = duration;
    }

    public OnClickInterceptor getOnClickInterceptor() {
        return this.interceptor;
    }

    public void collapse() {
        if (isExpanded()) {
            super.collapse();
        } else {
            super.expand();
        }
    }

    public void expand() {
        super.expand();
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public View getContentView() {
        return this.contentView;
    }

    public void attachContentView(View view) {
        this.contentView.addView(view);
    }

    public void attachContentView(@LayoutRes int view) {
        View.inflate(this.getContext(), view, this.contentView);
    }

    public void hide() {
        bottomSheet.setVisibility(INVISIBLE);
    }

    public void show() {
        bottomSheet.setVisibility(VISIBLE);
    }
}
