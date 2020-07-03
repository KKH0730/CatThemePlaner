package com.planer.catthemeplaner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.planer.catthemeplaner.adapter.ShoppingAdapter;
import com.planer.catthemeplaner.adapter.StoreViewPager;
import com.planer.catthemeplaner.controller.CirclePagerIndicatorDecoration;
import com.planer.catthemeplaner.model.ShoppingList;
import com.planer.catthemeplaner.model.ThemeModel;
import com.planer.catthemeplaner.ui.calendar.MyPageFragment;
import com.planer.catthemeplaner.ui.calendar.ShoppingFragment;
import com.planer.catthemeplaner.ui.calendar.StoreFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StoreShowExample extends AppCompatActivity {
    @BindView(R.id.ExampleRecyclerView)
    RecyclerView ExampleRecyclerView;
    @BindView(R.id.preViewImageView)
    ImageView preViewImageView;
    @BindView(R.id.preViewTitle)
    TextView preViewTitle;
    @BindView(R.id.preViewDescription)
    TextView preViewDescription;
    @BindView(R.id.purchaseBtn)
    Button purchaseBtn;
    @BindView(R.id.backBtn)
    Button backBtn;
    @BindView(R.id.showPointTextView)
    TextView showPointTextView;

    private static final String TAG = "StoreShowExample";
    private ShoppingAdapter adapter;
    private ThemeModel themeModel;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_show_example);

        ButterKnife.bind(this);

        init();
        buttonEvent();

        Intent intent = getIntent();

        preViewImageView.setImageResource(intent.getIntExtra("preViewImage", -1));
        preViewTitle.setText(intent.getStringExtra("title"));
        preViewDescription.setText(intent.getStringExtra("textDescription"));

        //테마 예시보여주는 부분
        if(intent.getStringExtra("title").equals("첫번째")){
            adapter.addItem(new ShoppingList(R.drawable.exam1_1));
            adapter.addItem(new ShoppingList(R.drawable.exam1_2));
            adapter.addItem(new ShoppingList(R.drawable.exam1_3));
        } else if(intent.getStringExtra("title").equals("두번째")){
            adapter.addItem(new ShoppingList(R.drawable.exam2_1));
            adapter.addItem(new ShoppingList(R.drawable.exam2_2));
            adapter.addItem(new ShoppingList(R.drawable.exam2_3));
        } else if(intent.getStringExtra("title").equals("세번째")){
            adapter.addItem(new ShoppingList(R.drawable.exam3_1));
            adapter.addItem(new ShoppingList(R.drawable.exam3_2));
            adapter.addItem(new ShoppingList(R.drawable.exam3_3));
        } else if(intent.getStringExtra("title").equals("네번째")){
            adapter.addItem(new ShoppingList(R.drawable.exam4_1));
            adapter.addItem(new ShoppingList(R.drawable.exam4_2));
            adapter.addItem(new ShoppingList(R.drawable.exam4_3));
        }


        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(ExampleRecyclerView);
        ExampleRecyclerView.addItemDecoration(new CirclePagerIndicatorDecoration());
        ExampleRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ExampleRecyclerView.setAdapter(adapter);



/*
        editor.remove("canPurchase").commit();
        editor.remove("canPurchase2").commit();
        editor.remove("canPurchase3").commit();
        editor.remove("canPurchase4").commit();

 */







        if ((preViewTitle.getText().toString()).equals("첫번째")) {
            if ((pref.getString("canPurchase", "true")).equals("false")) {
                purchaseBtn.setVisibility(View.GONE);
            }
        } else if ((preViewTitle.getText().toString()).equals("두번째")) {
            if ((pref.getString("canPurchase2", "true")).equals("false")) {
                purchaseBtn.setVisibility(View.GONE);
            }
        } else if ((preViewTitle.getText().toString()).equals("세번째")) {
            if ((pref.getString("canPurchase3", "true")).equals("false")) {
                purchaseBtn.setVisibility(View.GONE);
            }
        } else if ((preViewTitle.getText().toString()).equals("네번째")) {
            if ((pref.getString("canPurchase4", "true")).equals("false")) {
                purchaseBtn.setVisibility(View.GONE);
            }
        }


    }

    public void init() {
        adapter = new ShoppingAdapter();
        themeModel = new ThemeModel(this);


        pref = this.getSharedPreferences("TodoData", this.MODE_PRIVATE);
        editor = pref.edit();

        showPointTextView.setText(pref.getInt("totalPoint", -1) + "/100");
    }

    public void buttonEvent() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        purchaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int point = pref.getInt("totalPoint", -1);

                if ((preViewTitle.getText().toString()).equals("첫번째")) {
                    String canPurchase = pref.getString("canPurchase", "true");
                    if (canPurchase.equals("true")) {
                        if (point != -1) {
                            if (point >= 30) {
                                if (canPurchase.equals("true")) {
                                    point -= 30;
                                    editor.putInt("totalPoint", point).commit();
                                    editor.putString("canPurchase", "false").commit();
                                    showPointTextView.setText(pref.getInt("totalPoint", -1) + "/100");

                                    Toast.makeText(getApplicationContext(), "테마를 구입했습니다.", Toast.LENGTH_SHORT).show();
                                    purchaseBtn.setVisibility(View.GONE);
                                    themeModel.saveThemeInfo(preViewTitle.getText().toString());

                                    finish();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "냥포인트가 부족합니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else if ((preViewTitle.getText().toString()).equals("두번째")) {
                    String canPurchase2 = pref.getString("canPurchase2", "true");

                    if (canPurchase2.equals("true")) {
                        if (point != -1) {
                            if (point >= 30) {
                                if (canPurchase2.equals("true")) {
                                    point -= 30;
                                    editor.putInt("totalPoint", point).commit();
                                    editor.putString("canPurchase2", "false").commit();
                                    showPointTextView.setText(pref.getInt("totalPoint", -1) + "/100");

                                    Toast.makeText(getApplicationContext(), "테마를 구입했습니다.", Toast.LENGTH_SHORT).show();
                                    purchaseBtn.setVisibility(View.GONE);
                                    themeModel.saveThemeInfo(preViewTitle.getText().toString());

                                    finish();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "냥포인트가 부족합니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }


                } else if ((preViewTitle.getText().toString()).equals("세번째")) {
                    String canPurchase3 = pref.getString("canPurchase3", "true");

                    if (canPurchase3.equals("true")) {
                        if (point != -1) {
                            if (point >= 30) {
                                if (canPurchase3.equals("true")) {
                                    point -= 30;
                                    editor.putInt("totalPoint", point).commit();
                                    editor.putString("canPurchase3", "false").commit();
                                    showPointTextView.setText(pref.getInt("totalPoint", -1) + "/100");


                                    Toast.makeText(getApplicationContext(), "테마를 구입했습니다.", Toast.LENGTH_SHORT).show();
                                    purchaseBtn.setVisibility(View.GONE);
                                    themeModel.saveThemeInfo(preViewTitle.getText().toString());

                                    finish();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "냥포인트가 부족합니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                } else if ((preViewTitle.getText().toString()).equals("네번째")) {
                    String canPurchase4 = pref.getString("canPurchase4", "true");

                    if (canPurchase4.equals("true")) {
                        if (point != -1) {
                            if (point >= 30) {
                                if (canPurchase4.equals("true")) {
                                    point -= 30;
                                    editor.putInt("totalPoint", point).commit();
                                    editor.putString("canPurchase4", "false").commit();
                                    showPointTextView.setText(pref.getInt("totalPoint", -1) + "/100");

                                    Toast.makeText(getApplicationContext(), "테마를 구입했습니다.", Toast.LENGTH_SHORT).show();
                                    purchaseBtn.setVisibility(View.GONE);
                                    themeModel.saveThemeInfo(preViewTitle.getText().toString());

                                    finish();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "냥포인트가 부족합니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        });


    }

}
