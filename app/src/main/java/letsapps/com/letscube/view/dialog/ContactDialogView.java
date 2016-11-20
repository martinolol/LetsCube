package letsapps.com.letscube.view.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import letsapps.com.letscube.R;

public class ContactDialogView extends LinearLayout {

    EditText editText;

    public ContactDialogView(Context context) {
        super(context);

        final LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rootView = inflater.inflate(R.layout.dialog_contact, this);

        editText = (EditText)rootView.findViewById(R.id.contact_content);

    }

    public String getText(){
        return editText.getText().toString();
    }
}