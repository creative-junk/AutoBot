package com.crysoft.me.autobot;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.crysoft.me.autobot.database.DBAdapter;
import com.crysoft.me.autobot.fragments.checkout.AddressFragment;
import com.crysoft.me.autobot.fragments.checkout.ConfirmFragment;
import com.crysoft.me.autobot.fragments.checkout.PaymentFragment;
import com.crysoft.me.autobot.fragments.checkout.ShippingFragment;

import java.util.ArrayList;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity implements AddressFragment.OnFragmentInteractionListener,ConfirmFragment.OnFragmentInteractionListener,PaymentFragment.OnFragmentInteractionListener,ShippingFragment.OnFragmentInteractionListener {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    public String selectedShippingMethod;

    private int[] tabIcons = {
            R.drawable.location,
            R.drawable.truck,
            R.drawable.payment,

    };
    private DBAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseAdapter = DBAdapter.getInstance(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        setupTabs();

    }

    private void setupTabs() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AddressFragment(), "Address");
        adapter.addFragment(new ShippingFragment(), "Shipping");
        adapter.addFragment(new PaymentFragment(), "Payment");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }


        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        switch (item.getItemId()) {

            case R.id.action_cart:
               // showCart();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

}
