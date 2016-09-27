package com.crysoft.me.autobot.fragments.checkout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crysoft.me.autobot.R;
import com.crysoft.me.autobot.database.DBAdapter;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private DBAdapter databaseAdapter;
    private TextView tvCartTotal;
    private String totalAmount;
    private Button btnAddressNext;
    ParseUser currentUser;

    private EditText etShippingFirstName;
    private EditText etShippingLastName;
    private EditText etShippingDelivery;
    private EditText etShippingLandmark;
    private EditText etShippingCity;
    private EditText etShippingTelephone;
    private RelativeLayout rlLoadingPanel;
    private RelativeLayout rLAddress;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressFragment newInstance(String param1, String param2) {
        AddressFragment fragment = new AddressFragment();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (currentUser != null) {
            etShippingFirstName.setText(currentUser.getString("shipping_first_name"));
            etShippingLastName.setText(currentUser.getString("shipping_last_name"));
            etShippingDelivery.setText(currentUser.getString("shipping_address"));
            etShippingLandmark.setText(currentUser.getString("shipping_landmark"));
            etShippingCity.setText(currentUser.getString("shipping_city"));
            etShippingTelephone.setText(currentUser.getString("shipping_telephone"));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_address, container, false);

        etShippingFirstName = (EditText) v.findViewById(R.id.etShippingFirstName);
        etShippingLastName = (EditText) v.findViewById(R.id.etShippingLastName);
        etShippingDelivery = (EditText) v.findViewById(R.id.etShippingDelivery);
        etShippingLandmark = (EditText) v.findViewById(R.id.etShippingLandmark);
        etShippingCity = (EditText) v.findViewById(R.id.etShippingCity);
        etShippingTelephone = (EditText) v.findViewById(R.id.etShippingTelephone);
        rlLoadingPanel = (RelativeLayout) v.findViewById(R.id.rlLoadingPanel);
        rLAddress = (RelativeLayout) v.findViewById(R.id.rLAddressHolder);

        currentUser = ParseUser.getCurrentUser();

        final ViewPager pager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        v.findViewById(R.id.btnAddressNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etShippingFirstName.getText().toString().length() == 0) {
                    etShippingFirstName.setError("Last name is required!");
                } else if (etShippingLastName.getText().toString().length() == 0) {
                    etShippingLastName.setError("Last name is required!");
                } else if (etShippingDelivery.getText().toString().length() == 0) {
                    etShippingDelivery.setError("Delivery Address is required!");
                } else if (etShippingLandmark.getText().toString().length() == 0) {
                    etShippingLandmark.setError("Landmark is required!");
                } else if (etShippingCity.getText().toString().length() == 0) {
                    etShippingCity.setError("City is required!");
                } else if (etShippingTelephone.getText().toString().length() == 0) {
                    etShippingTelephone.setError("Telephone is required!");
                } else {
                    updateShipping();
                    pager.setCurrentItem(1);
                }
            }
        });

        return v;
    }
    private void updateShipping(){
        rLAddress.setVisibility(View.GONE);
        rlLoadingPanel.setVisibility(View.VISIBLE);

        currentUser.put("shipping_first_name",etShippingFirstName.getText().toString());
        currentUser.put("shipping_last_name",etShippingLastName.getText().toString());
        currentUser.put("shipping_delivery",etShippingDelivery.getText().toString());
        currentUser.put("shipping_address",etShippingDelivery.getText().toString());
        currentUser.put("shipping_landmark",etShippingLandmark.getText().toString());
        currentUser.put("shipping_city",etShippingCity.getText().toString());
        currentUser.put("shipping_telephone",etShippingTelephone.getText().toString());
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                rLAddress.setVisibility(View.VISIBLE);
                rlLoadingPanel.setVisibility(View.GONE);
            }
        });
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
}
