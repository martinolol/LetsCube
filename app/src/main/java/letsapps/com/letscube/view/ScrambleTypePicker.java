package letsapps.com.letscube.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import letsapps.com.letscube.util.scramble.ScrambleGenerator;

public class ScrambleTypePicker extends Spinner {

    ArrayAdapter<String> adapter;
    String[] scrambleTypes;

    public ScrambleTypePicker(Context context) {
        super(context);
        init(context);
    }

    public ScrambleTypePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        scrambleTypes = ScrambleGenerator.getScrambleTypesStr(context);

        adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_dropdown_item_1line, scrambleTypes);

        setAdapter(adapter);
    }

    public void select(int scrambleTypeId){
        setSelection(adapter.getPosition(
                ScrambleGenerator.getScrambleTypeName(getContext(), scrambleTypeId)));
    }

    public int getSelectedScrambleType(){
        return ScrambleGenerator.getScrambleTypeId(getContext(),
                scrambleTypes[getSelectedItemPosition()]);
    }
}
