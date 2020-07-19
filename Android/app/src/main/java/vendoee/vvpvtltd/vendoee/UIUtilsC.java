package vendoee.vvpvtltd.vendoee;

import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;

/**
 * Created by Ayush Vardhan on 5/6/2017.
 */

public class UIUtilsC {
    public static ImageButton getNavButtonView(Toolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++)
            if (toolbar.getChildAt(i) instanceof ImageButton)
                return (ImageButton) toolbar.getChildAt(i);
        return null;
    }
}