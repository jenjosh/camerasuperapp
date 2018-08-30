package com.example.apptest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab2 extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private View rootView;
    private TextView tv, tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9;

    private OnFragmentInteractionListener mListener;

    public Tab2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab2.
     */
    public static Tab2 newInstance(String param1, String param2) {
        Tab2 fragment = new Tab2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            fileRefresh();
        }
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
        rootView = inflater.inflate(R.layout.fragment_tab2, container, false);
        // Inflate the layout for this fragment
        return rootView;
    }

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
    public void onResume() {
        super.onResume();
        fileRefresh();
    }

    @Override
    public void onPause() {
        super.onPause();
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void fileRefresh() {

        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard,"vals.txt");
        StringBuilder text = new StringBuilder();
        ArrayList<String> vals = new ArrayList<String>(0);

        if (vals.size() < 10) {
            for (int i = vals.size(); i < 10; i++) {
                vals.add(0, " ");
            }
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                    text.delete(0, text.length());
                    text.append(line);
                    vals.add(text.toString());
            }
            br.close();

        tv = rootView.findViewById(R.id.activity_main_previous_values);
        tv.setText(getString(R.string.titlevals));

        tv1 = rootView.findViewById(R.id.activity_main_previous_values2);
        tv1.setText(vals.get(vals.size()-1));

        tv2 = rootView.findViewById(R.id.activity_main_previous_values3);
        tv2.setText(vals.get(vals.size()-2));

        tv3 = rootView.findViewById(R.id.activity_main_previous_values4);
        tv3.setText(vals.get(vals.size()-3));

        tv4 = rootView.findViewById(R.id.activity_main_previous_values5);
        tv4.setText(vals.get(vals.size()-4));

        tv5 = rootView.findViewById(R.id.activity_main_previous_values6);
        tv5.setText(vals.get(vals.size()-5));

        tv6 = rootView.findViewById(R.id.activity_main_previous_values8);
        tv6.setText(vals.get(vals.size()-6));

        tv7 = rootView.findViewById(R.id.activity_main_previous_values9);
        tv7.setText(vals.get(vals.size()-7));

        tv8 = rootView.findViewById(R.id.activity_main_previous_values10);
        tv8.setText(vals.get(vals.size()-8));

        tv9 = rootView.findViewById(R.id.activity_main_previous_values11);
        tv9.setText(vals.get(vals.size()-9));

        }
        catch (IOException e) {
            Log.e("Exception", "File read failed: " + e.toString());
        }
    }
}
