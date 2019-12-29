package in.koreatech.koin.core.util;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public class CustomBorderDrawable extends ShapeDrawable {
    private Paint fillpaint, strokepaint;
    private static final int WIDTH = 1;

    public CustomBorderDrawable(Shape s) {
        super(s);
        fillpaint = this.getPaint();
        strokepaint = new Paint(fillpaint);
        strokepaint.setStyle(Paint.Style.STROKE);
        strokepaint.setStrokeWidth(WIDTH);
        strokepaint.setARGB(100, 85, 85, 85);
    }

    @Override
    protected void onDraw(Shape shape, Canvas canvas, Paint fillpaint) {
        shape.draw(canvas, fillpaint);
        shape.draw(canvas, strokepaint);
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0, 0, canvas.getClipBounds().right,
                        canvas.getClipBounds().bottom),
                new RectF(WIDTH / 2, WIDTH / 2, canvas.getClipBounds().right - WIDTH / 2,
                        canvas.getClipBounds().bottom - WIDTH / 2),
                Matrix.ScaleToFit.FILL);
        canvas.concat(matrix);

        shape.draw(canvas, strokepaint);
    }

    public void setFillColor(int id) {
        fillpaint.setColor(id);
    }
}