package letsapps.com.letscube.view.pattern;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import letsapps.com.letscube.puzzle.Cube;
import letsapps.com.letscube.puzzle.CubeHelper;
import letsapps.com.letscube.puzzle.Pyraminx;
import letsapps.com.letscube.puzzle.PyraminxHelper;

public class PyraminxPatternView extends View {

    final float OUTER_BORDER_WIDTH = 6f;

    private Paint outerBorderPaint;
    private Paint innerBorderPaint;
    private Paint blankPaint;
    private Paint stickerPaint;

    private Pyraminx pyraminx;

    public PyraminxPatternView(Context context) {
        super(context);
        init();
    }

    public PyraminxPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PyraminxPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
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

        blankPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        blankPaint.setStrokeWidth(6f);
        blankPaint.setColor(Color.WHITE);

        stickerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        innerBorderPaint.setStyle(Paint.Style.FILL);

        pyraminx = new Pyraminx();
    }

    @Override
    protected void onDraw(Canvas canvas){
        final float width = canvas.getWidth();
        final float height = getHeightFromWidth(width);
        final float pieceSide = width / 6;
        final float halfPieceSide = pieceSide / 2;
        final float pieceHeight = height / 6;

        /* DRAW FACES */
        // draw F face
        for(int lin = 0; lin < PyraminxHelper.NB_LAYER; lin++){
            for(int col = 0; col < pyraminx.getNbColFromLine(lin); col++){
                if(isPieceFacingUp(PyraminxHelper.FACE_F, lin, col)) {
                    drawTriangle(
                            canvas,
                            (width / 2) - ((lin + 1) * halfPieceSide) +
                                    (col / 2) * pieceSide,
                            (lin + 1) * pieceHeight,
                            (width / 2) - ((lin + 1) * halfPieceSide) +
                                    ((col / 2) + 1) * pieceSide,
                            (lin + 1) * pieceHeight,
                            (width / 2) - ((lin + 1) * halfPieceSide) +
                                    ((col / 2) * pieceSide) + halfPieceSide,
                            lin * pieceHeight,
                            getColoredPaint(pyraminx.getSticker(PyraminxHelper.FACE_F, lin, col))
                    );
                }else{
                    drawTriangle(
                            canvas,
                            (width / 2) - (lin * halfPieceSide) +
                                    (col / 2) * pieceSide,
                            lin * pieceHeight,
                            (width / 2) - (lin * halfPieceSide) +
                                    ((col / 2) + 1) * pieceSide,
                            lin * pieceHeight,
                            (width / 2) - (lin * halfPieceSide) +
                                    ((col / 2) * pieceSide) + halfPieceSide,
                            (lin + 1) * pieceHeight,
                            getColoredPaint(pyraminx.getSticker(PyraminxHelper.FACE_F, lin, col))
                    );
                }
            }
        }

        // draw D face
        for(int lin = 0; lin < PyraminxHelper.NB_LAYER; lin++){
            for(int col = 0; col < pyraminx.getNbColFromLine(lin); col++){
                if(!isPieceFacingUp(PyraminxHelper.FACE_D, lin, col)) {
                    //Log.d("PPV", "D(" + lin + ";" + col +  ") : pieceFacingUp");
                    drawTriangle(
                            canvas,
                            width - ((width / 2) - ((lin + 1) * halfPieceSide) +
                                    (col / 2) * pieceSide),
                            height - ((lin + 1) * pieceHeight),
                            width - ((width / 2) - ((lin + 1) * halfPieceSide) +
                                    ((col / 2) + 1) * pieceSide),
                            height - ((lin + 1) * pieceHeight),
                            width - ((width / 2) - ((lin + 1) * halfPieceSide) +
                                    ((col / 2) * pieceSide) + halfPieceSide),
                            height - (lin * pieceHeight),
                            getColoredPaint(pyraminx.getSticker(PyraminxHelper.FACE_D, lin, col))
                    );
                }else{
                    //Log.d("PPV", "D(" + lin + ";" + col +  ") : pieceFacingDown");
                    drawTriangle(
                            canvas,
                            width - ((width / 2) - (lin * halfPieceSide) +
                                    (col / 2) * pieceSide),
                            height - (lin * pieceHeight),
                            width - ((width / 2) - (lin * halfPieceSide) +
                                    ((col / 2) + 1) * pieceSide),
                            height - (lin * pieceHeight),
                            width - ((width / 2) - (lin * halfPieceSide) +
                                    ((col / 2) * pieceSide) + halfPieceSide),
                            height - ((lin + 1) * pieceHeight),
                            getColoredPaint(pyraminx.getSticker(PyraminxHelper.FACE_D, lin, col))
                    );
                }
            }
        }

        // draw R face
        for(int lin = 0; lin < PyraminxHelper.NB_LAYER; lin++){
            for(int col = 0; col < pyraminx.getNbColFromLine(lin); col++){
                if(isPieceFacingUp(PyraminxHelper.FACE_R, lin, col)) {
                    drawTriangle(
                            canvas,
                            (width / 2)  + (((col + lin) + ((col + lin) % 2)) / 2) * halfPieceSide,
                            ((((((((col + lin) + ((col + lin) % 2)) / 2) % 2) + 2) % 3) / 2) + 1) * pieceHeight,
                            ((width / 2)  + (((col + lin) + ((col + lin) % 2)) / 2) * halfPieceSide) + pieceSide,
                            ((((((((col + lin) + ((col + lin) % 2)) / 2) % 2) + 2) % 3) / 2) + 1) * pieceHeight,
                            ((width / 2)  + (((col + lin) + ((col + lin) % 2)) / 2) * halfPieceSide) + halfPieceSide,
                            (((((((col + lin) + ((col + lin) % 2)) / 2) % 2) + 2) % 3) / 2) * pieceHeight,
                            getColoredPaint(pyraminx.getSticker(PyraminxHelper.FACE_R, lin, col))
                    );
                }else{
                    switch(lin + col){
                        case 6 : // (2; 4)
                        case 3 : // (1; 2)
                        case 0 : // (0; 0)
                            drawTriangle(canvas,
                                    (width / 2) + (1 + col) * halfPieceSide, pieceHeight,
                                    (width / 2) + (2 + col) * halfPieceSide, 0,
                                    (width / 2) + (col) * halfPieceSide, 0,
                                    getColoredPaint(pyraminx.getSticker(PyraminxHelper.FACE_R, lin, col))

                            );
                            break;
                        case 4 : // (2; 2)
                        case 1 : // (1; 0)
                            drawTriangle(canvas,
                                    (width / 2) + (2 + col) * halfPieceSide, 2 * pieceHeight,
                                    (width / 2) + (3 + col) * halfPieceSide, pieceHeight,
                                    (width / 2) + (1 + col) * halfPieceSide, pieceHeight,
                                    getColoredPaint(pyraminx.getSticker(PyraminxHelper.FACE_R, lin, col))

                            );
                            break;

                        case 2 : // (2; 0)
                            drawTriangle(canvas,
                                    (width / 2) + (3 + col) * halfPieceSide, 3 * pieceHeight,
                                    (width / 2) + (4 + col) * halfPieceSide, 2 * pieceHeight,
                                    (width / 2) + (2 + col) * halfPieceSide, 2 * pieceHeight,
                                    getColoredPaint(pyraminx.getSticker(PyraminxHelper.FACE_R, lin, col))

                            );
                            break;
                    }
                }
            }
        }

        // draw L face
        for(int lin = 0; lin < PyraminxHelper.NB_LAYER; lin++){
            for(int col = 0; col < pyraminx.getNbColFromLine(lin); col++){
                if(isPieceFacingUp(PyraminxHelper.FACE_L, lin, col)) {
                    switch(lin + col){
                        case 2 : // (1; 1)
                            drawTriangle(canvas,
                                    3 * halfPieceSide, pieceHeight,
                                    5 * halfPieceSide, pieceHeight,
                                    2 * pieceSide, 0,
                                    getColoredPaint(pyraminx.getSticker(PyraminxHelper.FACE_L, lin, col))

                            );
                            break;
                        case 3 : // (2; 1)
                            drawTriangle(canvas,
                                    halfPieceSide, pieceHeight,
                                    3 * halfPieceSide, pieceHeight,
                                    pieceSide, 0,
                                    getColoredPaint(pyraminx.getSticker(PyraminxHelper.FACE_L, lin, col))

                            );
                            break;
                        case 5 : // (2; 3)
                            drawTriangle(canvas,
                                    pieceSide, 2 * pieceHeight,
                                    2 * pieceSide, 2 * pieceHeight,
                                    3 * halfPieceSide, pieceHeight,
                                    getColoredPaint(pyraminx.getSticker(PyraminxHelper.FACE_L, lin, col))

                            );
                            break;
                    }
                }else{
                    switch(lin + col){
                        case 2 : // (2; 0)
                        case 1 : // (1; 0)
                        case 0 : // (0; 0)
                            drawTriangle(canvas,
                                    (width / 2) - (((2 * lin) + 1) * halfPieceSide), pieceHeight,
                                    (width / 2) - (((2 * lin)) * halfPieceSide), 0,
                                    (width / 2) - (((2 * lin) + 2) * halfPieceSide), 0,
                                    getColoredPaint(pyraminx.getSticker(PyraminxHelper.FACE_L, lin, col))

                            );
                            break;
                        case 4 : // (2; 2)
                        case 3 : // (1; 2)
                            drawTriangle(canvas,
                                    (width / 2) - (lin * pieceSide), 2 * pieceHeight,
                                    (width / 2) - (lin * pieceSide) + halfPieceSide, pieceHeight,
                                    (width / 2) - (lin * pieceSide) - halfPieceSide, pieceHeight,
                                    getColoredPaint(pyraminx.getSticker(PyraminxHelper.FACE_L, lin, col))

                            );
                            break;

                        case 6 : // (2; 4)
                            drawTriangle(canvas,
                                    width / 4, 3 * pieceHeight,
                                    (width / 4) + halfPieceSide, 2 * pieceHeight,
                                    (width / 4) - halfPieceSide, 2 * pieceHeight,
                                    getColoredPaint(pyraminx.getSticker(PyraminxHelper.FACE_L, lin, col))

                            );
                            break;
                    }
                }
            }
        }

        /* DRAW INNER BORDER LAYER */
        final int NB_LINES = 5;
        // draw HORIZONTAL lines
        for(int i = 1; i < NB_LINES + 1; i++){ // offset : 1
            canvas.drawLine(
                    i * (width / 12),
                    i * (height / 6),
                    width - i * (width / 12),
                    i * (height / 6),
                    innerBorderPaint
            );
        }

        // draw INCREASING lines
        for(int i = 1; i < NB_LINES + 1; i++){ // offset : 1
            canvas.drawLine(
                    (i * width) / 12,
                    (i * height) / 6,
                    (i * width) / 6,
                    0,
                    innerBorderPaint
            );
        }

        // draw DECREASING lines
        for(int i = 1; i < NB_LINES + 1; i++){ // offset : 1
            canvas.drawLine(
                    (i * width) / 6,
                    0,
                    ((6 + i) * width) / 12,
                    height - ((i * height) / 6),
                    innerBorderPaint
            );
        }

        /* DRAW OUTER BORDER TRIANGLES */
        // draw the OUTER triangle
        drawTriangle(canvas,
                OUTER_BORDER_WIDTH / 2,
                OUTER_BORDER_WIDTH / 2,
                width - (OUTER_BORDER_WIDTH / 2),
                OUTER_BORDER_WIDTH / 2,
                width / 2,
                height - (OUTER_BORDER_WIDTH / 2),
                outerBorderPaint);

        // draw the INNER triangle
        drawTriangle(canvas,
                width / 4,
                height / 2,
                (3 * width) / 4,
                height / 2,
                width / 2,
                OUTER_BORDER_WIDTH / 2,
                outerBorderPaint);

        canvas.drawLine(-6f, 0f, width / 2, height + 6f, blankPaint);
        canvas.drawLine(width + 6f, 0f, width / 2, height + 6f, blankPaint);
    }

    private void drawTriangle(Canvas canvas,
                              float x1, float y1, float x2, float y2, float x3, float y3,
                              Paint paint){

        final Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x1, y1);
        path.close();

        canvas.drawPath(path, paint);

    }

    private Paint getColoredPaint(byte stickerColor){

        switch(stickerColor){
            case PyraminxHelper.GREEN : stickerPaint.setColor(PuzzlePatternView.COLOR_GREEN); return stickerPaint;
            case PyraminxHelper.ORANGE : stickerPaint.setColor(PuzzlePatternView.COLOR_ORANGE); return stickerPaint;
            case PyraminxHelper.YELLOW : stickerPaint.setColor(PuzzlePatternView.COLOR_YELLOW); return stickerPaint;
            case PyraminxHelper.BLUE : stickerPaint.setColor(PuzzlePatternView.COLOR_BLUE); return stickerPaint;
            default :  stickerPaint.setColor(Color.BLACK); return stickerPaint;
        }
    }

    private boolean isPieceFacingUp(int faceId, int lin, int col){
        boolean isPieceFacingUp;

        if(col % 2 == 1){
            isPieceFacingUp = false;
        }else{
            isPieceFacingUp =  true;
        }

        if(faceId == PyraminxHelper.FACE_F){
            return isPieceFacingUp;
        }else{
            return !isPieceFacingUp;
        }
    }

    // return the height of an equilateral triangle according to the base size (width).
    private float getHeightFromWidth(float width){
        return (float)Math.sqrt(.75d) * width;
    }

    private float getWidthFromHeight(float height){
        return height / (float)Math.sqrt(.75d);
    }

    public void setPyraminx(Pyraminx pyraminx){
        this.pyraminx = pyraminx;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        final int availableWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int availableHeight = MeasureSpec.getSize(heightMeasureSpec);
        final int calculatedHeight = (int)getHeightFromWidth(availableWidth);

        if(calculatedHeight > availableHeight){
            final int calculatedWidth = (int)getWidthFromHeight(availableHeight);
            super.setMeasuredDimension(calculatedWidth, availableHeight);
        }else{
            super.setMeasuredDimension(availableWidth, calculatedHeight);
        }
    }
}
