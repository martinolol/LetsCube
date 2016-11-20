package letsapps.com.letscube.view.pattern;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import letsapps.com.letscube.R;
import letsapps.com.letscube.puzzle.Cube;
import letsapps.com.letscube.puzzle.Puzzle;
import letsapps.com.letscube.puzzle.Pyraminx;

public class PuzzlePatternView extends RelativeLayout {

    public static final int COLOR_BLUE = Color.parseColor("#1188FF");
    public static final int COLOR_ORANGE = Color.parseColor("#FFAA00");
    public static final int COLOR_RED = Color.RED;
    public static final int COLOR_GREEN = Color.GREEN;
    public static final int COLOR_WHITE = Color.parseColor("#FEFEFE");
    public static final int COLOR_YELLOW = Color.YELLOW;

    CubePatternView cubePattern;
    PyraminxPatternView pyraminxPattern;

    public PuzzlePatternView(Context context) {
        super(context);
        init();
    }

    public PuzzlePatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PuzzlePatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.view_puzzle_pattern, this);

        cubePattern = (CubePatternView)rootView.findViewById(R.id.pattern_cube);
        pyraminxPattern = (PyraminxPatternView)rootView.findViewById(R.id.pattern_pyraminx);
    }

    public void setPuzzle(Puzzle puzzle){
        if(puzzle instanceof Cube){
            cubePattern.setVisibility(VISIBLE);
            pyraminxPattern.setVisibility(GONE);
            cubePattern.setCube((Cube)puzzle);
        }else if(puzzle instanceof Pyraminx){
            cubePattern.setVisibility(GONE);
            pyraminxPattern.setVisibility(VISIBLE);
            pyraminxPattern.setPyraminx((Pyraminx) puzzle);
        }
    }

}
