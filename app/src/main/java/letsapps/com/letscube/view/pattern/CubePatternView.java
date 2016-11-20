package letsapps.com.letscube.view.pattern;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import letsapps.com.letscube.puzzle.Cube;
import letsapps.com.letscube.puzzle.CubeHelper;
import letsapps.com.letscube.puzzle.Puzzle;

public class CubePatternView extends View {

    final float OUTER_BORDER_WIDTH = 4f;

    private Paint outerBorderPaint;
    private Paint innerBorderPaint;
    private Paint stickerPaint;

    private Cube cube;

    public CubePatternView(Context context) {
        super(context);
        init();
    }

    public CubePatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CubePatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        outerBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        outerBorderPaint.setStrokeWidth(OUTER_BORDER_WIDTH);
        outerBorderPaint.setColor(Color.BLACK);
        outerBorderPaint.setStyle(Paint.Style.STROKE);

        innerBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerBorderPaint.setStrokeWidth(2f);
        innerBorderPaint.setColor(Color.BLACK);
        innerBorderPaint.setStyle(Paint.Style.STROKE);

        stickerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        cube = new Cube(3);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        final float width = canvas.getWidth();
        final float height = (3 * width) / 4 ;

        Log.d("CubePattern", "width : " + width);
        Log.d("CubePattern", "height : " + height);

        /* draw stickers */
        drawFaceStickers(canvas, cube.getFace(CubeHelper.FACE_U), width / 4, 0f);
        drawFaceStickers(canvas, cube.getFace(CubeHelper.FACE_F), width / 4, height / 3);
        drawFaceStickers(canvas, cube.getFace(CubeHelper.FACE_D), width / 4, (2 * height) / 3);
        drawFaceStickers(canvas, cube.getFace(CubeHelper.FACE_L), 0, height / 3);
        drawFaceStickers(canvas, cube.getFace(CubeHelper.FACE_R), width / 2, height / 3);
        drawFaceStickers(canvas, cube.getFace(CubeHelper.FACE_B), (3 * width) / 4, height / 3);

        /* draw INNER BORDERS */
        // vertical borders
        final int nbVerticalLines = cube.getSize() * 4;
        for(int i = 1; i < nbVerticalLines; i++){
            if(i > cube.getSize() && i < (cube.getSize() * 2)){
                canvas.drawLine(
                        i * (width / nbVerticalLines),
                        0,
                        i * (width / nbVerticalLines),
                        height,
                        innerBorderPaint);
            }else {
                canvas.drawLine(
                        i * (width / nbVerticalLines),
                        height / 3,
                        i * (width / nbVerticalLines),
                        (2 * height) / 3,
                        innerBorderPaint);
            }
        }

        // vertical borders
        final int nbHorizontalLines = cube.getSize() * 3;
        for(int i = 1; i < nbHorizontalLines; i++){
            if(i > cube.getSize() && i < (cube.getSize() * 2)){
                canvas.drawLine(
                        0,
                        i * (height / nbHorizontalLines),
                        width,
                        i * (height / nbHorizontalLines),
                        innerBorderPaint);
            }else {
                canvas.drawLine(
                        width / 4,
                        i * (height / nbHorizontalLines),
                        width / 2,
                        i * (height / nbHorizontalLines),
                        innerBorderPaint);
            }
        }

        /* draw OUTER BORDERS */
        // width rect
        canvas.drawRect(
                OUTER_BORDER_WIDTH / 2,
                height / 3,
                width - (OUTER_BORDER_WIDTH / 2),
                (2 * height) / 3,
                outerBorderPaint);

        // height rect
        canvas.drawRect(
                width / 4,
                OUTER_BORDER_WIDTH / 2,
                width / 2,
                height - (OUTER_BORDER_WIDTH / 2),
                outerBorderPaint);

        // border between R & B
        canvas.drawLine(
                (3 * width) / 4,
                height / 3,
                (3 * width) / 4,
                (2 * height) / 3,
                outerBorderPaint);

    }

    private void drawFaceStickers(Canvas canvas, byte[][] faceStickers, float xOffset, float yOffset){
        final float width = canvas.getWidth();
        final float height = ((3 * width) / 4);
        for(int lin = 0; lin < cube.getSize(); lin++){
            for(int col = 0; col < cube.getSize(); col++) {
                canvas.drawRect(
                        xOffset + col * (width / (cube.getSize() * 4)),
                        yOffset + lin * (height / (cube.getSize() * 3)),
                        xOffset + (col + 1) * (width / (cube.getSize() * 4)),
                        yOffset + (lin + 1) * (height / (cube.getSize() * 3)),
                        getColoredPaint((faceStickers[lin][col])));
            }
        }
    }

    private Paint getColoredPaint(byte stickerColor){
        switch(stickerColor){
            case CubeHelper.GREEN : stickerPaint.setColor(PuzzlePatternView.COLOR_GREEN); return stickerPaint;
            case CubeHelper.WHITE : stickerPaint.setColor(PuzzlePatternView.COLOR_WHITE); return stickerPaint;
            case CubeHelper.YELLOW : stickerPaint.setColor(PuzzlePatternView.COLOR_YELLOW); return stickerPaint;
            case CubeHelper.RED : stickerPaint.setColor(PuzzlePatternView.COLOR_RED); return stickerPaint;
            case CubeHelper.ORANGE : stickerPaint.setColor(PuzzlePatternView.COLOR_ORANGE); return stickerPaint;
            case CubeHelper.BLUE : stickerPaint.setColor(PuzzlePatternView.COLOR_BLUE); return stickerPaint;
            default :  stickerPaint.setColor(Color.BLACK); return stickerPaint;
        }
    }

    public void setCube(Cube cube){
        this.cube = cube;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int availableHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int calculatedHeight = (3 * width) / 4;

        if(calculatedHeight > availableHeight){
            final int calculatedWidth = (4 * availableHeight) / 3;
            super.setMeasuredDimension(calculatedWidth, availableHeight);
        }else{
            super.setMeasuredDimension(width, calculatedHeight);
        }
    }
}