package com.tmall.wireless.flare.example;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.tmall.wireless.flare.FlareUtil;
import com.tmall.wireless.flare.CfgDataParser;
import com.tmall.wireless.flare.IFlareConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private List<AnimatorSet> mAnimatorSetList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        intView();
    }

    private void intView() {
        final TextView textView = (TextView) findViewById(R.id.tv_anim);
        TextView textView2 = (TextView) findViewById(R.id.tv_anim_2);
//        AnimUtil.bindTest(textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("ddddddd");
            }
        });
        String json = new String(getAssertsFile(this, "data.json"));
        FlareUtil.init(json);

        if (false) {
            mAnimatorSetList = FlareUtil.bind(IFlareConstant.PAGE_COUDAN, getWindow().getDecorView().findViewById(android.R.id.content));
            mAnimatorSetList.get(0).addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    Log.d("MainActivity", "onAnimationStart");
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    Log.d("MainActivity", "onAnimationEnd");
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    Log.d("MainActivity", "onAnimationCancel");
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    Log.d("MainActivity", "onAnimationRepeat");
                }
            });
        }

        if (true) {
            FlareUtil.bindStatic(IFlareConstant.PAGE_COUDAN, textView);
        }

        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.resume).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
        findViewById(R.id.end).setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static byte[] getAssertsFile(Context context, String fileName) {
        InputStream inputStream = null;
        AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open(fileName);
            if (inputStream == null) {
                return null;
            }

            BufferedInputStream bis = null;
            int length;
            try {
                bis = new BufferedInputStream(inputStream);
                length = bis.available();
                byte[] data = new byte[length];
                bis.read(data);

                return data;
            } catch (IOException e) {

            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (Exception e) {

                    }
                }
            }

            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pause) {
            mAnimatorSetList.get(0).pause();
        } else if (v.getId() == R.id.resume) {
            mAnimatorSetList.get(0).resume();
        } else if (v.getId() == R.id.cancel) {
            mAnimatorSetList.get(0).cancel();
        } else if (v.getId() == R.id.end) {
            mAnimatorSetList.get(0).end();
        }
    }
}
