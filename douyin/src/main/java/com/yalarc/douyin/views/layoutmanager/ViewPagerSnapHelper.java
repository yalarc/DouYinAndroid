package com.yalarc.douyin.views.layoutmanager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

public abstract class ViewPagerSnapHelper extends RecyclerView.OnFlingListener {
    static final float MILLISECONDS_PER_INCH = 100.0F;
    RecyclerView mRecyclerView;
    private Scroller mGravityScroller;
    private final RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        boolean mScrolled = false;

        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == 0 && this.mScrolled) {
                this.mScrolled = false;
                ViewPagerSnapHelper.this.snapToTargetExistingView();
            }

        }

        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (dx != 0 || dy != 0) {
                this.mScrolled = true;
            }
        }
    };

    public ViewPagerSnapHelper() {
    }

    public boolean onFling(int velocityX, int velocityY) {
        RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
        if (layoutManager == null) {
            return false;
        } else {
            RecyclerView.Adapter adapter = this.mRecyclerView.getAdapter();
            if (adapter == null) {
                return false;
            } else {
                int minFlingVelocity = this.mRecyclerView.getMinFlingVelocity();
                return (Math.abs(velocityY) > minFlingVelocity || Math.abs(velocityX) > minFlingVelocity) && this.snapFromFling(layoutManager, velocityX, velocityY);
            }
        }
    }

    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        if (this.mRecyclerView != recyclerView) {
            if (this.mRecyclerView != null) {
                this.destroyCallbacks();
            }

            this.mRecyclerView = recyclerView;
            if (this.mRecyclerView != null) {
                this.setupCallbacks();
                this.mGravityScroller = new Scroller(this.mRecyclerView.getContext(), new DecelerateInterpolator());
                this.snapToTargetExistingView();
            }

        }
    }

    private void setupCallbacks() throws IllegalStateException {
        if (this.mRecyclerView.getOnFlingListener() != null) {
            throw new IllegalStateException("An instance of OnFlingListener already set.");
        } else {
            this.mRecyclerView.addOnScrollListener(this.mScrollListener);
            this.mRecyclerView.setOnFlingListener(this);
        }
    }

    private void destroyCallbacks() {
        this.mRecyclerView.removeOnScrollListener(this.mScrollListener);
        this.mRecyclerView.setOnFlingListener((RecyclerView.OnFlingListener) null);
    }

    public int[] calculateScrollDistance(int velocityX, int velocityY) {
        int[] outDist = new int[2];
        this.mGravityScroller.fling(0, 0, velocityX, velocityY, -2147483648, 2147483647, -2147483648, 2147483647);
        outDist[0] = this.mGravityScroller.getFinalX();
        outDist[1] = this.mGravityScroller.getFinalY();
        return outDist;
    }

    private boolean snapFromFling(@NonNull RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return false;
        } else {
            RecyclerView.SmoothScroller smoothScroller = this.createScroller(layoutManager);
            if (smoothScroller == null) {
                return false;
            } else {
                int targetPosition = this.findTargetSnapPosition(layoutManager, velocityX, velocityY);
                if (targetPosition == -1) {
                    return false;
                } else {
                    smoothScroller.setTargetPosition(targetPosition);
                    layoutManager.startSmoothScroll(smoothScroller);
                    return true;
                }
            }
        }
    }

    void snapToTargetExistingView() {
        if (this.mRecyclerView != null) {
            RecyclerView.LayoutManager layoutManager = this.mRecyclerView.getLayoutManager();
            if (layoutManager != null) {
                View snapView = this.findSnapView(layoutManager);
                if (snapView != null) {
                    int[] snapDistance = this.calculateDistanceToFinalSnap(layoutManager, snapView);
                    if (snapDistance[0] != 0 || snapDistance[1] != 0) {
                        this.mRecyclerView.smoothScrollBy(snapDistance[0], snapDistance[1]);
                    }

                }
            }
        }
    }

    @Nullable
    protected RecyclerView.SmoothScroller createScroller(RecyclerView.LayoutManager layoutManager) {
        return this.createSnapScroller(layoutManager);
    }

    /**
     * @deprecated
     */
    @Deprecated
    @Nullable
    protected LinearSmoothScroller createSnapScroller(RecyclerView.LayoutManager layoutManager) {
        return !(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider) ? null : new LinearSmoothScroller(this.mRecyclerView.getContext()) {
            protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                if (ViewPagerSnapHelper.this.mRecyclerView != null) {
                    int[] snapDistances = ViewPagerSnapHelper.this.calculateDistanceToFinalSnap(ViewPagerSnapHelper.this.mRecyclerView.getLayoutManager(), targetView);
                    int dx = snapDistances[0];
                    int dy = snapDistances[1];
                    int time = this.calculateTimeForDeceleration(Math.max(Math.abs(dx), Math.abs(dy)));
                    if (time > 0) {
                        action.update(dx, dy, time, this.mDecelerateInterpolator);
                    }

                }
            }

            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return 100.0F / (float) displayMetrics.densityDpi;

            }
        };
    }

    @Nullable
    public abstract int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager var1, @NonNull View var2);

    @Nullable
    public abstract View findSnapView(RecyclerView.LayoutManager var1);

    public abstract int findTargetSnapPosition(RecyclerView.LayoutManager var1, int var2, int var3);
}
