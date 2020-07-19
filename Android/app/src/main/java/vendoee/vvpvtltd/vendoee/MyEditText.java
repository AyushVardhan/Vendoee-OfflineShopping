package vendoee.vvpvtltd.vendoee;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by shagilsid on 20-11-2016.
 */

public class MyEditText extends EditText {
    public MyEditText(Context context) {
        super(context);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        Typeface tf= Typeface.createFromAsset(getContext().getAssets(),"fonts/Roboto-Medium.ttf");
        setTypeface(tf);
        setTextColor(Color.parseColor("#757575"));
    }

}
