package com.app.zxingtest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.ImageUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class MainActivity extends Activity implements View.OnClickListener{
    private Button button1 = null;
    private Button button2 = null;
    private Button button3 = null;
    private Button button4 = null;
    private Button button5 = null;
    private Bitmap bitmap = null;
    private TextView mTvResult;
    private EditText mInput;
    private ImageView mResult;
    private CheckBox mLogo;
    private Intent intent;
    private Bitmap bitmap2 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        button1 = (Button) findViewById(R.id.Button1);
        button2 = (Button) findViewById(R.id.Button2);
        button3 = (Button) findViewById(R.id.Button3);
        button4 = (Button) findViewById(R.id.Button4);
        button5 = (Button) findViewById(R.id.Button5);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mInput = (EditText) findViewById(R.id.et_text);
        mResult = (ImageView) findViewById(R.id.LogImage);
        mLogo = (CheckBox) findViewById(R.id.cb_logo);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.Button1:
                Log.d("abc", "Button1");
                startActivityForResult(new Intent(MainActivity.this, CaptureActivity.class),111);
                break;
            case R.id.Button2:
                String input = mInput.getText().toString();
                if(input.equals("")){
                    Toast.makeText(MainActivity.this,"输入不能为空",Toast.LENGTH_LONG).show();
                }else {
                    bitmap = CodeUtils.createImage(input,400,400,
                            mLogo.isChecked()?
                                   // BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher):null);    //单目表达式 判断复选框是否被选中
                    bitmap2:null);
                    mResult.setImageBitmap(bitmap);
                }
                break;
            case R.id.Button3:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 112);
                break;
            case R.id.Button4:
                try {
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
                    String date=sdf.format(new java.util.Date());
                    saveBitmap(bitmap,date);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.Button5:
                intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            if (requestCode == 111) {
                Bundle bundle = data.getExtras();
                String resule = bundle.getString(CodeUtils.RESULT_STRING);
                mTvResult.setText(resule);
                Toast.makeText(MainActivity.this, resule, Toast.LENGTH_LONG).show();
                Log.d("abc", resule);
            } else if (requestCode == 112){
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this, uri), new CodeUtils.AnalyzeCallback()  {   //获取绝对路径
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            mTvResult.setText(result);
                            Toast.makeText(MainActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(MainActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == 1){
                try
                {
                    // 获得图片的uri
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    bitmap2 = BitmapFactory.decodeFile(picturePath);
                    mResult.setImageBitmap(bitmap2);
                }catch(Exception e)
                {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * 保存图片
     * @param mbitmap   图片
     * @param mbitName  名字
     * @throws IOException
     */
    public void saveBitmap(Bitmap mbitmap,String mbitName) throws IOException
    {
        File file = new File("/sdcard/DCIM/Camera/"+mbitName+".PNG");
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out;
        try{
            out = new FileOutputStream(file);
            if(mbitmap.compress(Bitmap.CompressFormat.PNG, 90, out))
            {
                out.flush();
                out.close();
                Toast.makeText(MainActivity.this,"保存成功：/sdcard/DCIM/Camera/"+mbitName+".PNG",Toast.LENGTH_LONG).show();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}