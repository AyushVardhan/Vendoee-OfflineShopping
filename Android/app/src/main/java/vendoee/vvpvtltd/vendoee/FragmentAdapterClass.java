package vendoee.vvpvtltd.vendoee;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapterClass extends FragmentPagerAdapter {

    int TabCount;

    public FragmentAdapterClass(FragmentManager fragmentManager, int CountTabs) {

        super(fragmentManager);
        this.TabCount = CountTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new electronics();


            case 1:
                return new Appliances();


            case 2:
                return new Men();


            case 3:
                return new Women();

            case 4:
                return new KidsAndBaby();


            case 5:
                return new HomeAndFurniture();


            case 6:
                return new BooksAndMore();


            case 7:
                return new Grocery();


            case 8:
                return new AutomobileAndParts();

            case 9:
                return new FoodAndRestaurents();


            case 10:
                return new handloom();


            case 11:
                return new OthersCategory();


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return TabCount;
    }
}