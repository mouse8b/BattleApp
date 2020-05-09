package com.caderea.battleapp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import com.caderea.battleapp.R;
import com.caderea.battleapp.queue.BattleQueue;
import com.caderea.battleapp.queue.QueueDrawer;

import java.util.List;

public class BattleViewGroup extends ViewGroup {

    private QueueDrawer p1queue;
    private QueueDrawer p2queue;
    private ProgressBar tickProgress;
    private BattleDrawArea battleDrawArea;
    private BattleInputArea battleInputArea;

    public BattleViewGroup(Context context) {
        this(context, null);
    }

    public BattleViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        p1queue = findViewById(R.id.player1QDrawer);
        p2queue = findViewById(R.id.player2QDrawer);
        tickProgress = findViewById(R.id.tickProgressBar);
        battleDrawArea = findViewById(R.id.battleDrawArea);
        battleInputArea = findViewById(R.id.battle_buttons);

    }

    public void updateTickProgress(int progress) {
        tickProgress.setProgress(progress);
    }

    public void setQueues(BattleQueue q1, BattleQueue q2) {
        p1queue.setQueue(q1);
        p2queue.setQueue(q2);
    }

    public void refreshQueues() {
        p1queue.update();
        p2queue.update();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int totalHeight = MeasureSpec.getSize(heightMeasureSpec);

        int oneFifthWidth = totalWidth / 5;
        int topBarHeight = 150;
        int inputAreaHeight = 300;
        int queueHeight  = totalHeight - topBarHeight - inputAreaHeight;
        int battleDrawWidth = totalWidth - (2 * oneFifthWidth);

        p1queue.measure(MeasureSpec.makeMeasureSpec(oneFifthWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(queueHeight, MeasureSpec.EXACTLY));
        p2queue.measure(MeasureSpec.makeMeasureSpec(oneFifthWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(queueHeight, MeasureSpec.EXACTLY));
        tickProgress.measure(MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(topBarHeight, MeasureSpec.EXACTLY));
        battleDrawArea.measure(MeasureSpec.makeMeasureSpec(battleDrawWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(queueHeight, MeasureSpec.EXACTLY));
        battleInputArea.measure(MeasureSpec.makeMeasureSpec(totalWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(inputAreaHeight, MeasureSpec.EXACTLY));

        setMeasuredDimension(totalWidth, totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int width = right - left;
        int height = bottom - top;

        int tickLeft = 0;
        int tickTop = 0;
        int tickRight = width;
        int tickBottom = 100;
        tickProgress.layout(tickLeft, tickTop, tickRight, tickBottom);

        // queues 20% of each side
        int q1Left = 0;
        int q1Top = 100;
        int q1Right = q1Left + width / 5;
        int q1Bottom = height - 100;
        p1queue.layout(q1Left, q1Top, q1Right, q1Bottom);

        int q2Left = right - width / 5;
        int q2Top = 100;
        int q2Right = right;
        int q2Bottom = height - 100;
        p2queue.layout(q2Left, q2Top, q2Right, q2Bottom);

        int drawAreaLeft = q1Right + 1;
        int drawAreaTop = 100;
        int drawAreaRight = q2Left - 1;
        int drawAreaBottom = height - 100;
        battleDrawArea.layout(drawAreaLeft, drawAreaTop, drawAreaRight, drawAreaBottom);

        int inputAreaLeft = left;
        int inputAreaTop = height - 99;
        int inputAreaRight = right;
        int inputAreaBottom = height;
        battleInputArea.layout(inputAreaLeft, inputAreaTop, inputAreaRight, inputAreaBottom);

    }
}