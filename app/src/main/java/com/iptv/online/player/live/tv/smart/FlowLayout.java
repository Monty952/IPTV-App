package com.iptv.online.player.live.tv.smart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes5.dex */
public class FlowLayout extends ViewGroup {
    private int horizontalSpacing;
    private int verticalSpacing;

    public FlowLayout(Context context) {
        super(context);
        this.horizontalSpacing = 0;
        this.verticalSpacing = 0;
    }

    public FlowLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.horizontalSpacing = 0;
        this.verticalSpacing = 0;
    }

    public FlowLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.horizontalSpacing = 0;
        this.verticalSpacing = 0;
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        int size = (View.MeasureSpec.getSize(i) - getPaddingLeft()) - getPaddingRight();
        int mode = View.MeasureSpec.getMode(i);
        View.MeasureSpec.getSize(i2);
        getPaddingTop();
        getPaddingBottom();
        int mode2 = View.MeasureSpec.getMode(i2);
        int childCount = getChildCount();
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        while (i4 < childCount) {
            View childAt = getChildAt(i4);
            if (childAt.getVisibility() == View.GONE) {
                i3 = childCount;
            } else {
                measureChild(childAt, i, i2);
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                int i9 = marginLayoutParams != null ? marginLayoutParams.leftMargin : 0;
                i3 = childCount;
                int i10 = marginLayoutParams != null ? marginLayoutParams.rightMargin : 0;
                int i11 = measuredWidth + i9 + i10;
                int i12 = measuredHeight + (marginLayoutParams != null ? marginLayoutParams.topMargin : 0) + (marginLayoutParams != null ? marginLayoutParams.bottomMargin : 0);
                if (i6 + i11 > size && i6 > 0) {
                    i5 = Math.max(i5, i6);
                    i7 += i8 + this.verticalSpacing;
                    i6 = i11;
                    i8 = i12;
                } else {
                    i6 += i11 + this.horizontalSpacing;
                    i8 = Math.max(i8, i12);
                }
            }
            i4++;
            childCount = i3;
        }
        int max = Math.max(i5, i6) + getPaddingLeft() + getPaddingRight();
        int paddingTop = i7 + i8 + getPaddingTop() + getPaddingBottom();
        if (mode == 1073741824) {
            max = View.MeasureSpec.getSize(i);
        }
        if (mode2 == 1073741824) {
            paddingTop = View.MeasureSpec.getSize(i2);
        }
        setMeasuredDimension(max, paddingTop);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int paddingRight = (i3 - i) - getPaddingRight();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int childCount = getChildCount();
        int i5 = 0;
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != View.GONE) {
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) childAt.getLayoutParams();
                int i7 = marginLayoutParams != null ? marginLayoutParams.leftMargin : 0;
                int i8 = marginLayoutParams != null ? marginLayoutParams.rightMargin : 0;
                int i9 = marginLayoutParams != null ? marginLayoutParams.topMargin : 0;
                int i10 = marginLayoutParams != null ? marginLayoutParams.bottomMargin : 0;
                int i11 = measuredWidth + i7 + i8;
                if (paddingLeft + i11 > paddingRight && paddingLeft > getPaddingLeft()) {
                    paddingLeft = getPaddingLeft();
                    paddingTop += i5 + this.verticalSpacing;
                    i5 = 0;
                }
                int i12 = i7 + paddingLeft;
                int i13 = paddingTop + i9;
                childAt.layout(i12, i13, measuredWidth + i12, i13 + measuredHeight);
                paddingLeft += i11 + this.horizontalSpacing;
                i5 = Math.max(i5, measuredHeight + i9 + i10);
            }
        }
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ViewGroup.MarginLayoutParams(-2, -2);
    }

    @Override // android.view.ViewGroup
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new ViewGroup.MarginLayoutParams(getContext(), attributeSet);
    }

    @Override // android.view.ViewGroup
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return new ViewGroup.MarginLayoutParams(layoutParams);
    }
}
