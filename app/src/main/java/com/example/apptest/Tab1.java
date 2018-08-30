package com.example.apptest;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import jp.wasabeef.picasso.transformations.CropTransformation;

import static java.lang.Math.log;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tab1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tab1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tab1 extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private FloatingActionButton mFab;
    private View rootView;

    float bglevel = 0;
    float strip1 = 0;
    float strip2 = 0;

    private static final String TRANSLATION_Y = "translationY";
    private ImageButton fab;
    private boolean expanded = false;
    private View fabAction1;
    private View fabAction2;
    private View fabAction3;
    private float offset1;
    private float offset2;

    String DTime;
    float avgconc;

    public Tab1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tab1.
     */
    public static Tab1 newInstance(String param1, String param2) {
        Tab1 fragment = new Tab1();
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
        rootView = inflater.inflate(R.layout.fragment_tab1, container, false);

        final ViewGroup fabContainer = (ViewGroup) rootView.findViewById(R.id.t1_container);

        fab = (ImageButton) rootView.findViewById(R.id.activity_main_float);

        fabAction1 = rootView.findViewById(R.id.activity_main_float2);
        fabAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "onClick: Clicked SecScreen.");
                Intent intent = new Intent(getActivity(), Camera.class);
                getActivity().startActivity(intent);
            }
        });

        //Todo: Work out why this is the only way I can get this to work
        fabAction2 = rootView.findViewById(R.id.activity_main_float3);
        fabAction2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File myFile = new File(Environment.getExternalStorageDirectory(), "v");
                try {
                    if (!myFile.exists()) {
                        myFile.createNewFile();
                        FileWriter writer = new FileWriter(myFile+"als.txt");
                        writer.close();
                    }
                }catch (IOException e) {
                    Log.e("Exception", "File create failed: " + e.toString());
                }

                try {
                    FileWriter writer = new FileWriter(myFile+"als.txt", true);
                    //writer.write("\n" + Float.toString(bglevel) + "   " + Float.toString(strip1) + "   " + Float.toString(strip2));
                    writer.write("\n" + DTime + "                    " + Float.toString(avgconc));
                    writer.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
            }
        });

        fabAction3 = (FloatingActionButton) rootView.findViewById(R.id.activity_main_float4);
        fabAction3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PicRefresh();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expanded = !expanded;
                if (expanded) {
                    expandFab();
                } else {
                    collapseFab();
                }
            }
        });
        fabContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                fabContainer.getViewTreeObserver().removeOnPreDrawListener(this);
                offset1 = fab.getY() - fabAction1.getY();
                fabAction1.setTranslationY(offset1);
                offset2 = fab.getY() - fabAction2.getY();
                fabAction2.setTranslationY(offset2);
                return true;
            }
        });

        return rootView;
    }

    private void collapseFab() {
        fab.setImageResource(R.drawable.minus_animated);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createCollapseAnimator(fabAction1, offset1),
                createCollapseAnimator(fabAction2, offset2));
        animatorSet.start();
        animateFab();
    }

    private void expandFab() {
        fab.setImageResource(R.drawable.plus_animated);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(createExpandAnimator(fabAction1, offset1),
                createExpandAnimator(fabAction2, offset2));
        animatorSet.start();
        animateFab();
    }

    private Animator createCollapseAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, 0, offset)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private Animator createExpandAnimator(View view, float offset) {
        return ObjectAnimator.ofFloat(view, TRANSLATION_Y, offset, 0)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private void animateFab() {
        Drawable drawable = fab.getDrawable();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
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
        PicRefresh();
        Toast.makeText(getContext(), "Press big button if image not reloaded", Toast.LENGTH_LONG).show();
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

    public float mean(int middle, int w, int h, ArrayList<Float> array) {
        float y;

        float total = 0;
        int count = 0;

        for (int i = middle - w/20; i < middle + w/20; i++) {
            for (int j = 0; j < h; j++) {
                y = array.get(i*h+j);
                if (y != 0) {
                    total = total + bglevel - y;
                    count++;
                }
            }
        }
        return total / count;
    }

    float atanh (float x)
    {
        return (float) ((log(1+x) - log(1-x))/2);
    }

    public void PicRefresh() {
        final int imageDimension = (int) getResources().getDimension(R.dimen.image_size);

        final ImageView imageView = (ImageView) rootView.findViewById(R.id.activity_main_image);
        float scalingFactor = 2f;
        imageView.setScaleX(scalingFactor);
        imageView.setScaleY(scalingFactor);

                //Todo: Change file save location and use Environment.getExternalStorageDirectory() or whatever
        Picasso.with(getActivity()).invalidate("file:///storage/emulated/0/pic.jpg");
        Picasso.with(getActivity())
                .load("file:///storage/emulated/0/pic.jpg")
                .transform(new CropTransformation(0, 0, imageDimension*62/160, imageDimension*12/160))
                .resize(imageDimension, imageDimension)
                .centerCrop()
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                        assert imageView != null;
                        imageView.setImageBitmap(bitmap);
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        ArrayList<Float> array = new ArrayList<Float>(0);
                                        int h = bitmap.getHeight();
                                        int w = bitmap.getWidth();
                                        float total1 = 0;
                                        int c1 = 0;

                                        for (int i=w/10; i <3*w/10; i++) {
                                            for (int j = 0; j < h; j++) {
                                                int pixel = bitmap.getPixel(i,j);
                                                float greyValue = (Color.red(pixel) + Color.blue(pixel) + Color.green(pixel)) / 3;
                                                total1 = total1 + greyValue;
                                                c1++;
                                            }
                                        }
                                        bglevel = total1 / c1;

                                        for (int i=0; i < w; i++) {
                                            for (int j = 0; j < h; j++) {
                                                int pixel = bitmap.getPixel(i,j);
                                                float greyValue = ((Color.red(pixel) + Color.blue(pixel) + Color.green(pixel)) / 3) - bglevel;
                                                if (greyValue <= -10) { //Threshold for background
                                                    array.add(greyValue + bglevel);
                                                }
                                                else {
                                                    array.add((float) -1);
                                                }
                                            }
                                        }

                                        strip1 = mean(3*w/5, w, h, array);
                                        strip2 = mean(4*w/5, w, h, array);

                                        float normalised11 = 100*strip1/bglevel;
                                        float normalised21 = 100*strip2/bglevel;

                                        float conc1 = 2 * atanh((normalised11 / 50) - 1);
                                        float conc2 = -2 * (atanh((normalised21-50)/50)-1)-2;

                                        avgconc = (float) 0.5 * (conc1 + conc2);
                                        DTime = DateFormat.getDateTimeInstance(3, 3).format(new Date());

                                        TextView tv1 = (TextView)rootView.findViewById(R.id.activity_main_title);
                                        //tv3.setText("I2: " + Float.toString(strip2) + " (" + Float.toString(normalised21) + ")");
                                        tv1.setText(" ");
                                        TextView tv2 = (TextView)rootView.findViewById(R.id.activity_main_body2);
                                        tv2.setText("Read at: " + DTime);
                                        TextView tv3 = (TextView)rootView.findViewById(R.id.activity_main_body);
                                        tv3.setText("Concentration: " + Float.toString(avgconc));

                                    }
                                });
                    }
                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                    }
                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                });
        Picasso.with(getActivity()).invalidate("file:///storage/emulated/0/pic.jpg");
    }
}
