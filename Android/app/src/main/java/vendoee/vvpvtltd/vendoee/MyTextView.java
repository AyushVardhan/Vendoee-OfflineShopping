package vendoee.vvpvtltd.vendoee;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Ayush Vardhan on 12/14/2016.
 */
public class MyTextView extends TextView {
    public MyTextView(Context context) {
        super(context);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        Typeface tf= Typeface.createFromAsset(getContext().getAssets(),"fonts/RobotoCondensed-Regular.ttf");
        setTypeface(tf);
        setTextColor(Color.parseColor("#212121"));
        setTextSize(20.0f);
        //setShadowLayer(4.0f,4.0f,5.5f,Color.LTGRAY);
    }
}
