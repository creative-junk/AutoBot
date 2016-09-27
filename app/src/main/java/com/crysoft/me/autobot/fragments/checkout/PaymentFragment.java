package com.crysoft.me.autobot.fragments.checkout;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.crysoft.me.autobot.R;
import com.crysoft.me.autobot.database.DBAdapter;
import com.crysoft.me.autobot.helpers.Utils;
import com.crysoft.me.autobot.models.ProductsModel;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PaymentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentFragment extends Fragment {
    private String selectedShippingMethod;
    private DBAdapter databaseAdapter;

    private String cartAmount;
    private int shippingAmount;
    private int totalAmount;

    private TextView tvCartTotal;
    private TextView tvShippingAmount;
    private TextView tvTotalPayable;

    private RadioButton rBtnMpesa;
    private RadioButton rBtnCash;

    private LinearLayout lLMpesa;
    private LinearLayout lLCash;

    private  EditText etMpesaNumber;
    private  EditText etMpesaCode;

    private ProgressDialog progressDialog;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public PaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1   Parameter 1.
     * @param //param2 Parameter 2.
     * @return A new instance of fragment PaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PaymentFragment newInstance(String param1) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        // args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //selectedShippingMethod = getArguments().getString(ARG_PARAM1);
            // mParam2 = getArguments().getString(ARG_PARAM2);
        }

        databaseAdapter = DBAdapter.getInstance(getActivity());
        selectedShippingMethod = ParseUser.getCurrentUser().getString("preferred_shipping");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_payment, container, false);


        if (selectedShippingMethod == "Regular"){
            shippingAmount =250;
        }else if (selectedShippingMethod == "Pickup"){
            shippingAmount =0;
        }else{
            shippingAmount = 250;
        }

        //Log.i("Shipping Method",selectedShippingMethod);
        //Log.i("Shipping Cost", String.valueOf(shippingAmount));

        final ViewPager pager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        tvCartTotal = (TextView) v.findViewById(R.id.tvCartTotal);
        tvShippingAmount = (TextView) v.findViewById(R.id.tvShippingAmount);
        tvTotalPayable = (TextView) v.findViewById(R.id.tvTotalPayable);
        lLMpesa = (LinearLayout) v.findViewById(R.id.lLMpesa);
        lLCash = (LinearLayout) v.findViewById(R.id.lLCash);
        etMpesaNumber = (EditText) v.findViewById(R.id.etMpesaNumber);
        etMpesaCode = (EditText) v.findViewById(R.id.etMpesaCode);

        rBtnMpesa = (RadioButton) v.findViewById(R.id.rBtnMpesa);
        rBtnCash = (RadioButton) v.findViewById(R.id.rBtnCash);

        totalAmount = databaseAdapter.getUnformattedCartAmount() + shippingAmount;
        String payableAmount = Utils.formatPrice(totalAmount);

        tvCartTotal.setText("Cart Amount: " + databaseAdapter.getCartAmount());
        tvShippingAmount.setText("Shipping: " + Utils.formatPrice(shippingAmount));
        tvTotalPayable.setText("Total Payable: " + payableAmount);

        v.findViewById(R.id.btnPaymentNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isOnline(getActivity())) {

                    if (rBtnMpesa.isChecked()){

                        if (etMpesaNumber.getText().toString().length() == 0) {
                            etMpesaNumber.setError("Mpesa Number is Required!");
                            Toast.makeText(getActivity(), "Mpesa Number is required", Toast.LENGTH_LONG).show();
                        } else if (etMpesaCode.getText().toString().length() == 0) {
                            etMpesaCode.setError("Mpesa Code is required!");
                            Toast.makeText(getActivity(), "Mpesa Code is required", Toast.LENGTH_LONG).show();
                        }else {
                            progressDialog = ProgressDialog.show(getActivity(), "Processing Order...", "Please Wait");
                            processOrder("mpesa");
                        }

                    }else if (rBtnCash.isChecked()){
                        progressDialog = ProgressDialog.show(getActivity(), "Processing Order...", "Please Wait");
                        processOrder("cash");
                    }

                }else{
                    Toast.makeText(getActivity(), "Check your internet Connection and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
        v.findViewById(R.id.btnPaymentPrevious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(1);
            }
        });

        v.findViewById(R.id.rBtnMpesa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    lLCash.setVisibility(View.GONE);
                    lLMpesa.setVisibility(View.VISIBLE);



            }
        });
        v.findViewById(R.id.rBtnCash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lLCash.setVisibility(View.VISIBLE);
                lLMpesa.setVisibility(View.GONE);
            }
        });

//        Log.i("shipping method", selectedShippingMethod);
        return v;
    }

    private void processOrder(String paymentType){
        if (progressDialog !=null && progressDialog.isShowing()){
            progressDialog.dismiss();

            ParseUser currentUser = ParseUser.getCurrentUser();
            List<ProductsModel> cart = databaseAdapter.getCartItems();

            ArrayList<ParseObject> orderItems = new ArrayList<ParseObject>();
            for (int i=0;i<cart.size();i++){
                ProductsModel cartItem = cart.get(i);
                ParseObject orderItem = new ParseObject("OrderItems");
                orderItem.put("product_name",cartItem.getProductName());
                orderItem.put("product_price",cartItem.getProductPrice());
                orderItem.put("product_image",cartItem.getImageFile());
                orderItem.put("product_id",cartItem.getObjectId());
                orderItem.put("quantity",cartItem.getQuantity());
                //orderItem.put("order_id",order);
                orderItems.add(orderItem);
            }

            ParseObject order = new ParseObject("Order");
            order.put("sub_total",String.valueOf(databaseAdapter.getUnformattedCartAmount()));
            order.put("shipping",String.valueOf(shippingAmount));
            order.put("total",String.valueOf(totalAmount));
            order.put("order_owner",currentUser);
            order.put("payment_type",paymentType);
            order.put("order_items",orderItems);

            //if (paymentType == "mpesa")
            order.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e==null){
                        Toast.makeText(getActivity(), "Order Processed", Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(getActivity(), "Order Processing Failed "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
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

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!getUserVisibleHint())
        {
            return;
        }

        //INSERT CUSTOM CODE HERE
        selectedShippingMethod = ParseUser.getCurrentUser().getString("preferred_shipping");
        if (selectedShippingMethod == "Regular"){
            shippingAmount =250;
        }else if (selectedShippingMethod == "Pickup"){
            shippingAmount =0;
        }else{
            shippingAmount = 250;
        }
        totalAmount = databaseAdapter.getUnformattedCartAmount() + shippingAmount;
        String payableAmount = Utils.formatPrice(totalAmount);


        tvCartTotal.setText("Cart Amount: " + databaseAdapter.getCartAmount());
        tvShippingAmount.setText("Shipping: " + Utils.formatPrice(shippingAmount));
        tvTotalPayable.setText("Total Payable: " + payableAmount);
    }
}
