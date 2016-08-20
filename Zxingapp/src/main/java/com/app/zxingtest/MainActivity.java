package com.app.zxingtest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

public class MainActivity extends Activity implements View.OnClickListener{
    private Button button1 = null;
    private Button button2 = null;
    private TextView mTvResult;
    private EditText mInput;
    private ImageView mResult;
    private CheckBox mLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initView();
    }

    private void initView() {
        button1 = (Button) findViewById(R.id.Button1);
        button2 = (Button) findViewById(R.id.Button2);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mInput = (EditText) findViewById(R.id.et_text);
        mResult = (ImageView) findViewById(R.id.LogImage);
        mLogo = (CheckBox) findViewById(R.id.cb_logo);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Button1:
                Log.d("abc", "Button1");
                startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class),0);
                break;
            case R.id.Button2:
                String input = mInput.getText().toString();
                if(input.equals("")){
                    Toast.makeText(MainActivity.this,"输入不能为空",Toast.LENGTH_LONG).show();
                }else {
                    Bitmap bitmap = CodeUtils.createImage(input,400,400,
                            mLogo.isChecked()?
                            BitmapFactory.decodeResource(getResources(),R.drawable.logo):null);
                    mResult.setImageBitmap(bitmap);
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            Bundle bundle = data.getExtras();
            String resule = bundle.getString(CodeUtils.RESULT_STRING);
            mTvResult.setText(resule);
            Toast.makeText(MainActivity.this,resule+"----",Toast.LENGTH_LONG).show();
            Log.d("abc",resule+"dsfdsf");
        }
    }
}
