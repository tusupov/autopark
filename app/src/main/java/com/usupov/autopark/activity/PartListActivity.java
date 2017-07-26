package com.usupov.autopark.activity;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.usupov.autopark.R;
import com.usupov.autopark.adapter.UserPartListAdapter;
import com.usupov.autopark.json.Car;
import com.usupov.autopark.json.Part;
import com.usupov.autopark.model.CarModel;
import com.usupov.autopark.model.UserPartModel;

import java.util.List;

public class PartListActivity extends BasicActivity {

    private RecyclerView rvUserPartList;
    private List<UserPartModel> userPartList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_part_list, null, false);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.addView(contentView, 0);
//        setContentView(R.layout.activity_part_list);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initToolbar();
        initFabUserPart();

        ifCarListEmpty();

        initEmptyView();
        initRecyclerView();
        initUserPartList();


    }

    private void initEmptyView() {

    }
    private void initUserPartList() {
        userPartList = Part.getUserPartList(this);
        UserPartListAdapter adapter = new UserPartListAdapter(this, userPartList);
        rvUserPartList.setAdapter(adapter);
    }
    private void initRecyclerView() {
        rvUserPartList  = (RecyclerView) findViewById(R.id.list_user_part);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        rvUserPartList.setLayoutManager(mLayoutManager);
        rvUserPartList.setItemAnimator(new DefaultItemAnimator());
        rvUserPartList.setNestedScrollingEnabled(false);
    }
    private void initFabUserPart() {
        FloatingActionButton fabUserPart = (FloatingActionButton) findViewById(R.id.fab_new_user_part);
        fabUserPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<CarModel> carList = Car.getCarList(getApplicationContext());
                if (carList==null || carList.size() != 1) {
                    startActivity(new Intent(PartListActivity.this, CarListActivity.class));
                    finish();
                }
                else {
                    Intent intent = new Intent(PartListActivity.this, PartActivity.class);
                    CarModel car = carList.get(0);
                    intent.putExtra("carName", car.getFullName());
                    intent.putExtra("carId", car.getId());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private  void ifCarListEmpty() {
        List<CarModel> carList = Car.getCarList(getApplicationContext());
        Part.getUserPartList(this);
        if (carList == null || carList.isEmpty()) {
//            startActivity(new Intent(PartListActivity.this, CarListActivity.class));
//            finish();
        }
    }

    private void initToolbar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_part_list);
//        toolbar.setTitle("Лента");
    }
}
