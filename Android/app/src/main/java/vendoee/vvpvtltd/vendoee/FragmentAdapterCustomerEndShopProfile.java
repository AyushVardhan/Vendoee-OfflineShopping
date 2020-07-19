package vendoee.vvpvtltd.vendoee;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Ayush Vardhan on 4/7/2017.
 */

public class FragmentAdapterCustomerEndShopProfile  extends FragmentPagerAdapter {

    int TabCount;

    public FragmentAdapterCustomerEndShopProfile(FragmentManager fragmentManager, int CountTabs) {

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
                return new shopInfoCustomer();

            case 1:
                return new OffersShopProfileCustomer();

            case 2:
                return new ProductShopProfileCustomer();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TabCount;
    }
}
