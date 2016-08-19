package com.app.zxingtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;

public class MainActivity extends Activity implements View.OnClickListener{
    private Button button1 = null;
    private TextView mTvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initView();
    }

    private void initView() {
        button1 = (Button) findViewById(R.id.Button1);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        button1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Button1:
                Log.d("abc", "Button1");
                startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class),0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            String resule = bundle.getString("result");
            mTvResult.setText(resule);
            Toast.makeText(MainActivity.this,resule+"----",Toast.LENGTH_LONG).show();
            Log.d("abc",resule+"dsfdsf");
        }
    }
}
