package company.photoshooto_task.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;

import company.photoshooto_task.Helper.FontType;
import company.photoshooto_task.Interface.OfferItemClickListener;
import company.photoshooto_task.Login;
import company.photoshooto_task.Model.DailyPhoto;
import company.photoshooto_task.R;
import company.photoshooto_task.ViewHolder.DailyPhotoViewHolder;
import company.photoshooto_task.ZoomActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;
    ImageView profile;

    Drawable drawable,drawable2;
    FirebaseAuth firebaseAuth;

    //
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<DailyPhoto, DailyPhotoViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CameraShoppingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();
        databaseReference=database.getReference("DayPhoto");


        ViewGroup root = (ViewGroup)view.findViewById(R.id.root);
        FontType fontType=new FontType(getContext(),root);
        // gifImageView=(GifImageView)findViewById(R.id.gifSplash);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        recycler_menu=(RecyclerView) view.findViewById(R.id.recycler_home);
        recycler_menu.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false);
        recycler_menu.setLayoutManager(layoutManager);

        loadPicOfDay();

        profile=(ImageView) view.findViewById(R.id.profile);

        drawable =getResources().getDrawable(R.drawable.buttonshape);
        drawable2 =getResources().getDrawable(R.color.white);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Logout")
                        .setMessage("Are you sure you want to Logout..?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.signOut();
                                Intent i = new Intent(getActivity(), Login.class);
                                startActivity(i);
                                ((Activity) getActivity()).overridePendingTransition(0, 0);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("no", null).setCancelable(false);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return view;
    }

    private void loadPicOfDay() {
        adapter =new FirebaseRecyclerAdapter<DailyPhoto,DailyPhotoViewHolder>(DailyPhoto.class,
                R.layout.daily_photo_item,
                DailyPhotoViewHolder.class,
                databaseReference) {
            @Override
            protected void populateViewHolder(DailyPhotoViewHolder viewHolder, DailyPhoto model, int position) {
                Picasso.get().load(model.getImage()).into(viewHolder.offerImage);
                /*Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.menuImage);*/
                viewHolder.imgShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), viewHolder.imgShare.getId());
//
//                        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) +"/share.png";
//
//                        FileOutputStream out = null;
//
//                        File file = new File(path);
//
//                        try{
//                            out = new FileOutputStream(file);
//                            bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
//                            out.flush();
//                            out.close();
//
//                        }catch(Exception e){
//                            e.printStackTrace();
//                        }
//
//                        path = file.getPath();
//
//                        Uri imageUri = Uri.parse("file://" + path);
//                        Intent shareIntent = new Intent();
//                        shareIntent.setAction(Intent.ACTION_SEND);
//                        shareIntent.setPackage("com.whatsapp");
//                        shareIntent.putExtra(Intent.EXTRA_TEXT, "picture_text");
//                        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//                        shareIntent.setType("image/jpeg");
//                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//                        try {
//                            startActivity(shareIntent);
//                        } catch (android.content.ActivityNotFoundException ex) {
//                            //ToastHelper.MakeShortText("Whatsapp have not been installed.");
//                        }
                    }
                });

                viewHolder.imgZoom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent i = new Intent(getActivity(), ZoomActivity.class);
                        i.putExtra("Image",model.getImage());
                        startActivity(i);
                        ((Activity) getActivity()).overridePendingTransition(0, 0);
                    }
                });

                final DailyPhoto clickItem=model;
                viewHolder.setOfferItemClickListener(new OfferItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get creationid and send to new activity
                        //Intent intent=new Intent(Home.this,ServiceList.class);
                        //because creation id is key,so we just get key of this item
                        //intent.putExtra("CategoryId",adapter.getRef(position).getKey());
                        //startActivity(intent);
                    }
                });

            }
        };
        recycler_menu.setAdapter(adapter);
    }

}