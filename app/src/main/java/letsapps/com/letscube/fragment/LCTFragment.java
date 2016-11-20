package letsapps.com.letscube.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

/**
 * Created by marti on 30/08/2016.
 */
public class LCTFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void startAnimation(Animation animation){
        final View rootView = super.getView();
        if(rootView != null) {
            rootView.startAnimation(animation);
        }else{
            Log.d("LCTFragment", "can't start anim because of a null root view");
        }
    }
}
