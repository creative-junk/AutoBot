package com.crysoft.me.autobot.fragments.checkout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.crysoft.me.autobot.R;
import com.crysoft.me.autobot.helpers.Constants;
import com.crysoft.me.autobot.models.ShippingModel;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShippingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShippingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShippingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private LinearLayout lLRegularContent;
    private LinearLayout lLPickupContent;
    private ArrayList<ShippingModel> shippingMethods;
    private TextView tvRegularTiming;
    private TextView tvRegularFee;
    private TextView tvPickupTiming;
    private TextView tvPickupFee;
    private String preferredShipping;
    private RadioButton rBtnRegularShipping;
    private RadioButton rBtnPickup;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ShippingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShippingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShippingFragment newInstance(String param1, String param2) {
        ShippingFragment fragment = new ShippingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_shipping, container, false);

        lLRegularContent = (LinearLayout) v.findViewById(R.id.lLRegularContent);
        lLPickupContent = (LinearLayout) v.findViewById(R.id.lLPickupContent);

        tvRegularTiming = (TextView) v.findViewById(R.id.tvRegularTiming);
        tvRegularFee = (TextView) v.findViewById(R.id.tvRegularFee);
        tvPickupTiming = (TextView) v.findViewById(R.id.tvPickupTiming);
        tvPickupFee = (TextView) v.findViewById(R.id.tvPickupFee);

        rBtnRegularShipping = (RadioButton) v.findViewById(R.id.rBtnRegularShipping);
        rBtnPickup = (RadioButton) v.findViewById(R.id.rBtnPickupShipping);
        final ViewPager pager = (ViewPager) getActivity().findViewById(R.id.viewpager);



        v.findViewById(R.id.btnShippingNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rBtnRegularShipping.isChecked()){
                  preferredShipping = "Regular";

                }else if (rBtnPickup.isChecked()){
                 preferredShipping = "Pickup";
                }
                setPreferredShipping(preferredShipping,pager);
            }
        });
        v.findViewById(R.id.btnShippingPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(0);
            }
        });

        shippingMethods = getShippingMethods();
        for (int i = 0; i < shippingMethods.size(); i++) {
            ShippingModel shippingMethod = shippingMethods.get(i);
            if (shippingMethod.getName() == "Regular") {
                tvRegularTiming.setText("Shipping Time " + shippingMethod.getTiming());
                tvRegularFee.setText("Fee " + Constants.CURRENCY + " " + shippingMethod.getFee());
            } else if (shippingMethod.getName() == "Pickup") {
                tvPickupTiming.setText("Shipping Time " + shippingMethod.getTiming());
                tvPickupFee.setText("Fee " + Constants.CURRENCY + " " + shippingMethod.getFee());
            }

        }

        v.findViewById(R.id.rBtnRegularShipping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lLPickupContent.setVisibility(View.GONE);
                lLRegularContent.setVisibility(View.VISIBLE);
                preferredShipping = "Regular";
                setPreferredShippingMethod(preferredShipping,pager);

            }
        });
        v.findViewById(R.id.rBtnPickupShipping).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lLPickupContent.setVisibility(View.VISIBLE);
                lLRegularContent.setVisibility(View.GONE);
                preferredShipping = "Pickup";
                setPreferredShippingMethod(preferredShipping,pager);
            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setPreferredShipping(String preferredShipping,ViewPager viewPager) {
        final ViewPager pager= viewPager;
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("preferred_shipping",preferredShipping);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    pager.setCurrentItem(2);
                }else{
                    Toast.makeText(getActivity(), "Something went wrong when selecting, Please try again", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void setPreferredShippingMethod(String preferredShipping,ViewPager viewPager) {
        final ViewPager pager= viewPager;
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("preferred_shipping",preferredShipping);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e==null){
                    //
                }else{
                    Toast.makeText(getActivity(), "Something went wrong when selecting, Please try again", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private ArrayList<ShippingModel> getShippingMethods() {
        final ArrayList<ShippingModel> shippingMethods = new ArrayList<ShippingModel>();

        ParseQuery query = new ParseQuery("ShippingMethods");
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List objects, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        ShippingModel shippingMethod = new ShippingModel();
                        ParseObject model = (ParseObject) objects.get(i);
                        shippingMethod.setName(model.getString("name"));
                        shippingMethod.setTiming(model.getString("timing"));
                        shippingMethod.setFee(model.getString("fee"));
                        shippingMethods.add(shippingMethod);
                    }
                }
            }

            @Override
            public void done(Object o, Throwable throwable) {

            }
        });
        return shippingMethods;
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        final ViewPager pager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rBtnRegularShipping:
                if (checked)
                    preferredShipping = "Regular";
                setPreferredShipping(preferredShipping,pager);
                    break;
            case R.id.rBtnPickupShipping:
                if (checked)
                    preferredShipping = "Pickup";
                setPreferredShipping(preferredShipping,pager);
                    break;
        }
    }

}
