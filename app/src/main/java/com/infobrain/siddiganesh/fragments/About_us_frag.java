import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infobrain.siddiganesh.R;

/**
 * Created by bikas on 12/3/2017.
 */

public class About_us_frag extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View some = inflater.inflate(R.layout.about_us_frag, container, false);
        getActivity().setTitle("About Us");
        return some;
    }
}
