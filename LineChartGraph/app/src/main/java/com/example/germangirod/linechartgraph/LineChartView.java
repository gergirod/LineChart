package com.example.germangirod.linechartgraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by germangirod on 5/29/15.
 */
public class LineChartView extends View {

    private Context context;
    private float[] datapoints;
    private Paint paint_line = new Paint();
    public Paint paint_back = new Paint();
    private static final float GRAPH_SMOOTHNES = 0.15f;
    private int mark_value;
    private float x_point_mark, y_point_mark;

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public void setChartData(float[] line) {

        this.datapoints = line;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //canvas.drawColor(0);
        float maxValue = getMax(datapoints);

        drawBackground(canvas, maxValue);

        drawLineChart(canvas, maxValue);
    }

    private void drawBackground(Canvas canvas, float maxValue) {

        double density = getContext().getResources().getDisplayMetrics().density;
        if (density == 3.0) {
            paint_back.setTextSize(45);
        }
        if (density == 2.0) {
            paint_back.setTextSize(33);
        }

        if (density == 1.5) {
            paint_back.setTextSize(20);
        }
        //range=1;
        paint_back.setStyle(Paint.Style.FILL);

        paint_back.setColor(Color.BLUE);
        paint_back.setTextAlign(Paint.Align.RIGHT);
        paint_back.setStrokeWidth(1);

        int range = (getHeight()) / 3;

        for (int y = 0; y <= 2; y++) {

            if (y == 0) {
                double max = Math.ceil(getMax(datapoints));
                canvas.drawText("$" + String.valueOf(max), getWidth() - getPaddingRight(), 50 - 15, paint_back);

                if (density == 1.5) {
                    canvas.drawLine(0, 45, getWidth(), 45, paint_back);
                } else {
                    canvas.drawLine(0, 50, getWidth(), 50, paint_back);
                }
            }
            if (y == 2) {
                double min = Math.floor(getMin(datapoints));
                canvas.drawText("$" + String.valueOf(min), getWidth() - getPaddingRight(), getHeight() - 15, paint_back);

                if (density == 1.5) {
                    canvas.drawLine(0, (getHeight() - 5), getWidth(), (getHeight() - 5), paint_back);
                } else {
                    canvas.drawLine(0, (getHeight()), getWidth(), (getHeight()), paint_back);
                }
            }
            if (y == 1) {
                double max = Math.ceil(getMax(datapoints));
                double min = Math.floor(getMin(datapoints));

                float values = (float) (max - min);
                float final_values = (float) (min + (values / 2));
                canvas.drawText("$" + String.valueOf(final_values), getWidth() - getPaddingRight(), ((getHeight() + 50) / 2) - 15, paint_back);

                if (density == 1.5) {
                    canvas.drawLine(0, (getHeight() + (50 - 5)) / 2, getWidth(), (getHeight() + (50 - 5)) / 2, paint_back);
                } else {
                    canvas.drawLine(0, (getHeight() + 50) / 2, getWidth(), (getHeight() + 50) / 2, paint_back);
                }
            }
            paint_back.setAntiAlias(false);
            // canvas.drawLine(0, y, getWidth(), y, paint);
            paint_back.setAntiAlias(true);
        }
    }

    private void drawLineChart(Canvas canvas, float maxValue) {

        float min = getMin(datapoints);
        float height = 0;

        height = (float) ((float) (getHeight()));
        min = (min / maxValue) * height;
        min = height - min;

        double density = getContext().getResources().getDisplayMetrics().density;

        Path path = createSmoothPath(maxValue);

        paint_line.setStyle(Paint.Style.STROKE);

        //paint_line.setColor(Color.MAGENTA);
        paint_line.setAntiAlias(true);
        // paint.setShadowLayer(6, 2, 2, 0x81000000);
        canvas.drawPath(path, paint_line);
        //paint.setShadowLayer(0, 0, 0, 0);

        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(20);
        p.setColor(Color.WHITE);
        canvas.drawCircle(x_point_mark, y_point_mark, 10, p);
    }

    private Path createSmoothPath(float maxValue) {

        Path path = new Path();
        path.moveTo(getXPos(0), getYPos(datapoints[0], maxValue));
        for (int i = 0; i < datapoints.length; i++) {

            if (i == mark_value) {
                x_point_mark = getXPos(i);
                y_point_mark = getYPos(datapoints[i], maxValue);
            }

            float thisPointX = getXPos(i);
            float thisPointY = getYPos(datapoints[i], maxValue);
            float nextPointX = getXPos(i + 1);
            float nextPointY = getYPos(datapoints[ifHas(i + 1)], maxValue);

            float startdiffX = (nextPointX - getXPos(ifHas(i - 1)));
            float startdiffY = (nextPointY - getYPos(datapoints[ifHas(i - 1)], maxValue));
            float endDiffX = (getXPos(ifHas(i + 2)) - thisPointX);
            float endDiffY = (getYPos(datapoints[ifHas(i + 2)], maxValue) - thisPointY);

            float firstControlX = thisPointX + (GRAPH_SMOOTHNES * startdiffX);
            float firstControlY = thisPointY + (GRAPH_SMOOTHNES * startdiffY);
            float secondControlX = nextPointX - (GRAPH_SMOOTHNES * endDiffX);
            float secondControlY = nextPointY - (GRAPH_SMOOTHNES * endDiffY);

            path.cubicTo(firstControlX, firstControlY, secondControlX, secondControlY, nextPointX, nextPointY);
        }
        return path;
    }


    private int ifHas(int i) {
        if (i > datapoints.length - 1) {
            return datapoints.length - 1;
        } else if (i < 0) {
            return 0;
        }
        return i;
    }

    private float getMax(float[] array) {
        float max = 0;
        if (array.length != 0) {
            max = array[0];
            for (int i = 1; i < array.length; i++) {
                if (array[i] > max) {
                    max = array[i];
                }
            }
            return max;
        } else {
            return max;
        }
    }

    private float getMin(float[] array) {
        float max = 0;
        if (array.length != 0) {
            max = array[0];
            for (int i = 1; i < array.length; i++) {
                if (array[i] < max) {
                    max = array[i];
                }
            }
            return max;
        } else {
            return max;
        }
    }

    private float getYPos(float value, float maxValue) {

        float min = getMin(datapoints);
        float range = maxValue - min;

        value = (maxValue - value) * ((getHeight() / range));

        float middle = getHeight() / 2;

        if (value > middle) {
            value -= 15;
        } else {

            value += 50;
        }

        return value;
    }

    private float getXPos(float value) {
        double density = getContext().getResources().getDisplayMetrics().density;
        float width = 0;
        if (density == 1.5) {
            width = getWidth() - getPaddingLeft() - getPaddingRight() - 70;
        } else {
            width = getWidth() - getPaddingLeft() - getPaddingRight() - 150;
        }

        float maxValue = datapoints.length - 1;

        // scale it to the view size
        value = (value / maxValue) * width;

        // offset it to adjust for padding
        value += getPaddingRight();

        return value;
    }

}
