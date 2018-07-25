package com.vismaad.naad.navigation.home.adapter;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.vismaad.naad.R;
import com.vismaad.naad.custom_views.BlurTransformation;
import com.vismaad.naad.custom_views.ItemClickSupport;
import com.vismaad.naad.navigation.home.raagi_detail.RaagiDetailActivity;
import com.vismaad.naad.player.service.App;
import com.vismaad.naad.rest.model.raagi.RaagiInfo;
import com.vismaad.naad.utils.BlurTransform;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ivesingh on 2/2/18.
 */

public class RaagiInfoAdapter extends RecyclerView.Adapter<RaagiInfoAdapter.RaagiInfoViewHolder> implements Filterable {

    private Context context;
    private Activity activity;
    private List<RaagiInfo> raagiInfoList;

    //Define these variables at the beginning of your Activity or Fragment:
//    private long then;
//    private int longClickDuration = 300;
//    private boolean isScaled = false;

    private int animationTime = 150;
    private GestureDetector mGestureDetector;
    private CustomGestureDetector customGestureDetector;
    private List<RaagiInfo> contactListFiltered;

    public RaagiInfoAdapter(Activity context, List<RaagiInfo> raagiInfoList) {
        this.context = context;
        this.activity = context;
        this.raagiInfoList = raagiInfoList;
        this.contactListFiltered = raagiInfoList;
        // Create an object of our Custom Gesture Detector Class
        customGestureDetector = new CustomGestureDetector();
        // Create a GestureDetector
        mGestureDetector = new GestureDetector(context, customGestureDetector);

    }

    @Override
    public RaagiInfoAdapter.RaagiInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_raagi, parent, false);
        return new RaagiInfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RaagiInfoAdapter.RaagiInfoViewHolder holder, final int position) {
//        ObjectAnimator scaleDownX3 = ObjectAnimator.ofFloat(
//                holder.raagi_card_layout, "scaleX", 1f);
//        ObjectAnimator scaleDownY3 = ObjectAnimator.ofFloat(
//                holder.raagi_card_layout, "scaleY", 1f);
//        scaleDownX3.setDuration(10);
//        scaleDownY3.setDuration(10);
//
//        AnimatorSet scaleDown3 = new AnimatorSet();
//        scaleDown3.play(scaleDownX3).with(scaleDownY3);
//
//        scaleDown3.start();

        final RaagiInfo raagiInfo = contactListFiltered.get(position);

        // added this code to override the image size and use the original image size
        // and using apply method below, added this optiom
        RequestOptions option = new RequestOptions().fitCenter()
                .override(Target.SIZE_ORIGINAL);

        Glide.with(context)
                .load(raagiInfo.getRaagiImageURL())
                .apply(option)
                .into(holder.raagi_thumbnail_IV);

        holder.raagi_name_TV.setText(raagiInfo.getRaagiName());
        holder.shabads_count_TV.setText(raagiInfo.getShabadsCount() + " shabads");

        holder.raagi_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_intent(holder, raagiInfo);
            }
        });
//        setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, final MotionEvent event) {
//                customGestureDetector.setCustomGestureDetector(holder, raagiInfo);
//                mGestureDetector.onTouchEvent(event);
//                return true;
//            }
//        });

        //for long click to trigger after 5 seconds ...
      /*  holder.raagi_thumbnail_IV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                customGestureDetector.setCustomGestureDetector(holder, raagiInfo);
                mGestureDetector.onTouchEvent(event);
                return true;
//                return animateTheCard(event, holder, raagiInfo);
            }
        });*/

/*
      holder.raagi_thumbnail_IV.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, final MotionEvent event) {
              customGestureDetector.setCustomGestureDetector(holder, raagiInfo);
              mGestureDetector.onTouchEvent(event);
              return true;
//                return animateTheCard(event, holder, raagiInfo);
          }
      });

        holder.raagi_name_TV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                customGestureDetector.setCustomGestureDetector(holder, raagiInfo);
                mGestureDetector.onTouchEvent(event);
                return true;
//                return animateTheCard(event, holder, raagiInfo);
            }
        });

        holder.shabads_count_TV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, final MotionEvent event) {
                customGestureDetector.setCustomGestureDetector(holder, raagiInfo);
                mGestureDetector.onTouchEvent(event);
                return true;
//                return animateTheCard(event, holder, raagiInfo);
            }
        });
*/

//        holder.raagi_thumbnail_IV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startAnimationAndClick(holder, raagiInfo);
////                create_intent(holder, raagiInfo);
//            }
//        });
//        holder.raagi_thumbnail_IV.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                openActivityFromBottom(raagiInfo);
//                return false;
//            }
//        });
//        holder.raagi_name_TV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startAnimationAndClick(holder, raagiInfo);
////                create_intent(holder, raagiInfo);
//            }
//        });
//        holder.raagi_name_TV.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                openActivityFromBottom(raagiInfo);
//                return false;
//            }
//        });
//        holder.shabads_count_TV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startAnimationAndClick(holder, raagiInfo);
////                create_intent(holder, raagiInfo);
//            }
//        });
//        holder.shabads_count_TV.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                openActivityFromBottom(raagiInfo);
//                return false;
//            }
//        });

        // NOTE: Uncomment the following lines when three dots are viewed.
        /*
        holder.raagi_menu_IV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.raagi_menu_IV, raagiInfo);
            }
        });

        */
    }

    private void startAnimationAndClick(final RaagiInfoViewHolder holder, final RaagiInfo raagiInfo) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(holder.raagi_card_layout,
                "scaleX", 0.9f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(holder.raagi_card_layout,
                "scaleY", 0.9f);
        scaleDownX.setDuration(animationTime);
        scaleDownY.setDuration(animationTime);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);

        scaleDown.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(
                        holder.raagi_card_layout, "scaleX", 1f);
                ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(
                        holder.raagi_card_layout, "scaleY", 1f);
                scaleDownX2.setDuration(animationTime);
                scaleDownY2.setDuration(animationTime);

                AnimatorSet scaleDown2 = new AnimatorSet();
                scaleDown2.play(scaleDownX2).with(scaleDownY2);

                scaleDown2.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        create_intent(holder, raagiInfo);
                    }
                }, animationTime);
            }
        }, animationTime);
    }

    private void create_intent(final RaagiInfoViewHolder holder, RaagiInfo raagiInfo) {
        Pair<View, String> p1 = Pair.create((View) holder.raagi_thumbnail_IV, "raagi_image");
        Pair<View, String> p2 = Pair.create((View) holder.raagi_name_TV, "raagi_name");
        Pair<View, String> p3 = Pair.create((View) holder.shabads_count_TV, "raagi_shabads_count");
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, p1, p2, p3);

        Intent intent = new Intent(context, RaagiDetailActivity.class);
        intent.putExtra("raagi_image_url", raagiInfo.getRaagiImageURL());
        intent.putExtra("raagi_name", raagiInfo.getRaagiName());
        intent.putExtra("num_of_shabads", raagiInfo.getShabadsCount());
        intent.putExtra("total_shabads_length", raagiInfo.getMinutesOfShabads());

        context.startActivity(intent, activityOptionsCompat.toBundle());
    }

    private void showPopupMenu(View view, final RaagiInfo raagiInfo) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.raagi_card, popup.getMenu());
        popup.getMenu().getItem(2).setVisible(true);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_favorite:
                        Toast.makeText(context, "Add Favorite", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.play_now:
                        Toast.makeText(context, "Play now", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.more_options:
                        // open bottom sheet for more options
                        openActivityFromBottom(raagiInfo);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private void openActivityFromBottom(final RaagiInfo raagi) {
        int heights = activity.getWindowManager().getDefaultDisplay().getHeight() / 2;
        int height = heights + (activity.getWindowManager().getDefaultDisplay().getHeight() / 4);

        View view = activity.getLayoutInflater().inflate(R.layout.activity_raagi_option, null);

        final Dialog mBottomSheetDialog = new Dialog(activity, R.style.MaterialDialogSheet);

        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCanceledOnTouchOutside(true);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, height);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

        LinearLayout layout = (LinearLayout) mBottomSheetDialog.findViewById(R.id.bottom_sheet_layout);
        LinearLayout underLayout = (LinearLayout) mBottomSheetDialog.findViewById(R.id.under_layout);

        ImageView image = (ImageView) mBottomSheetDialog.findViewById(R.id.raagi_image);
        TextView name = (TextView) mBottomSheetDialog.findViewById(R.id.raagi_name);
        int imgHeight, width;
        imgHeight = width = activity.getWindowManager().getDefaultDisplay().getWidth() / 3;

        RequestOptions option = new RequestOptions().fitCenter().override(width, imgHeight);

        Glide.with(context).load(raagi.getRaagiImageURL()).apply(option).into(image);

        name.setText(raagi.getRaagiName());

        RecyclerView list = (RecyclerView) mBottomSheetDialog.findViewById(R.id.options_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(activity));
        List<String> listOption = new ArrayList<String>();
        listOption.add("Option 1");
        listOption.add("Option 2");
        listOption.add("Option 3");
        listOption.add("Option 4");
        listOption.add("Option 5");
        listOption.add("Option 6");

        RaagiOptionsAdapter adapter = new RaagiOptionsAdapter(context, listOption);
        list.setAdapter(adapter);

        ItemClickSupport.addTo(list).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
//                Log.e("Position", "Clicked: " + position);
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                }
            }
        });

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });

        underLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mBottomSheetDialog.show();
    }

    private void startAnimationOnly(final RaagiInfoViewHolder holder) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(holder.raagi_card_layout,
                "scaleX", 0.9f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(holder.raagi_card_layout,
                "scaleY", 0.9f);
        scaleDownX.setDuration(animationTime);
        scaleDownY.setDuration(animationTime);

        AnimatorSet scaleDown = new AnimatorSet();
        scaleDown.play(scaleDownX).with(scaleDownY);

        scaleDown.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator scaleDownX2 = ObjectAnimator.ofFloat(
                        holder.raagi_card_layout, "scaleX", 1f);
                ObjectAnimator scaleDownY2 = ObjectAnimator.ofFloat(
                        holder.raagi_card_layout, "scaleY", 1f);
                scaleDownX2.setDuration(animationTime);
                scaleDownY2.setDuration(animationTime);

                AnimatorSet scaleDown2 = new AnimatorSet();
                scaleDown2.play(scaleDownX2).with(scaleDownY2);

                scaleDown2.start();

            }
        }, animationTime);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = raagiInfoList;
                } else {
                    List<RaagiInfo> filteredList = new ArrayList<>();
                    for (RaagiInfo row : raagiInfoList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getRaagiName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<RaagiInfo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class CustomGestureDetector implements GestureDetector.OnGestureListener,
            GestureDetector.OnDoubleTapListener {

        RaagiInfoViewHolder holder;
        RaagiInfo info;

        public void setCustomGestureDetector(RaagiInfoViewHolder holder, RaagiInfo info) {
            this.holder = holder;
            this.info = info;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //Log.e("GestureDetector", "onSingleTapConfirmed");
//            startAnimationAndClick(holder, info);
            create_intent(holder, info);
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            //Log.e("GestureDetector", "onDown");
            // NOTE: Uncomment the following line to enable viewing activity from bottom
            //startAnimationOnly(holder);
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
//            Log.e("GestureDetector", "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            //Log.e("GestureDetector", "onLongPress");
            // NOTE: Uncomment the following line to enable viewing activity from bottom
            //openActivityFromBottom(info);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    class RaagiInfoViewHolder extends RecyclerView.ViewHolder {

        private TextView raagi_name_TV, shabads_count_TV;
        private ImageView raagi_thumbnail_IV, raagi_menu_IV;
        private CardView raagi_card_layout;
        private View raagi_card_view;

        public RaagiInfoViewHolder(View itemView) {
            super(itemView);
            raagi_thumbnail_IV = itemView.findViewById(R.id.raagi_thumbnail_IV);
            raagi_menu_IV = itemView.findViewById(R.id.raagi_menu_IV);

            raagi_name_TV = itemView.findViewById(R.id.raagi_name_TV);
            shabads_count_TV = itemView.findViewById(R.id.shabads_count_TV);
            raagi_card_layout = itemView.findViewById(R.id.raagi_card_layout);
            raagi_card_view = itemView.findViewById(R.id.raagi_card_view);
        }
    }

}
