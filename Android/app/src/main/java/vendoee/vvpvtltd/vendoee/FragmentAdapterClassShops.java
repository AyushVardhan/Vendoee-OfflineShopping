package vendoee.vvpvtltd.vendoee;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapterClassShops extends FragmentPagerAdapter {

    int TabCount;

    public FragmentAdapterClassShops(FragmentManager fragmentManager, int CountTabs) {

        super(fragmentManager);
        this.TabCount = CountTabs;
    }


    @Override
    public int getItemPosition(Object object) {
        // POSITION_NONE makes it possible to reload the PagerAdapter
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new stores();


            case 1:
                return new brands();


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TabCount;
    }
}