package com.binwang.qanda.ui;

import com.binwang.qanda.R;
import com.binwang.qanda.R.id;
import com.binwang.qanda.R.layout;
import com.binwang.qanda.R.menu;
import com.binwang.qanda.sqlite.DBManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	private String TAG;//覆写finish()有用，不知道具体用处...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resetTitlebar();
        //设定进入该activity的动画效果
        overridePendingTransition(R.anim.alpha_scale_rotate, R.anim.push_left_out);
        TextView order=(TextView)findViewById(R.id.order);
        TextView simulate = (TextView) findViewById(R.id.simulate);
		TextView replacement = (TextView) findViewById(R.id.replacement);
		LinearLayout favorite = (LinearLayout) findViewById(R.id.favorite);
		LinearLayout wrong = (LinearLayout) findViewById(R.id.wrong);
		LinearLayout history = (LinearLayout) findViewById(R.id.history);
        order.setOnClickListener(this);
        simulate.setOnClickListener(this);
        favorite.setOnClickListener(this);
        wrong.setOnClickListener(this);
        history.setOnClickListener(this);
        DBManager.createDB();
        //DBManager.simpleCreateDB();
    }
    
  //重设标题栏
  	private void resetTitlebar() {
  		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.titlebar_mainactivity);
  		TextView title = (TextView) findViewById(R.id.titlebar_title);
  		TextView titlebar_left_text=(TextView)findViewById(R.id.titlebar_left_text);
  		title.setText("医学知识学习助手 by王彬瑞");
  	}
  	
  	@Override//覆写finish()，目的是保证返回有动画效果
	public void finish() {
		Log.i(TAG,  "finish");
		super.finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.zoom_exit);
	}

	@Override//监听器
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.order:
				 Intent intent=new Intent();
				    intent.setClass(MainActivity.this,OrderActivity.class);
				       startActivity(intent);
				       //finish();
				//右边这种更简介：startActivity(new Intent(MainActivity.this, OrderActivity.class));
				break;
			case R.id.simulate:
				View layout = getLayoutInflater().inflate(R.layout.enter_simulate, null);
				final Dialog dialog = new Dialog(this);
				dialog.setTitle("温馨提示");
				dialog.show();
				dialog.getWindow().setContentView(layout);
				final EditText et_name = (EditText) layout.findViewById(R.id.et_name);
				TextView confirm = (TextView) layout.findViewById(R.id.confirm);
				TextView cancel = (TextView) layout.findViewById(R.id.cancel);
				confirm.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (TextUtils.isEmpty(et_name.getText().toString().trim())) {
							Toast.makeText(MainActivity.this, "请先输入考试姓名", Toast.LENGTH_SHORT).show();
						} else {
							DBManager.insertExamTable();
							ResultActivity.NAME=et_name.getText().toString().trim();
							//ExamActivity.intentToExamActivity(MainActivity.this, et_name.getText().toString().trim());
							startActivity(new Intent(MainActivity.this, ExamActivity.class));
							Toast.makeText(MainActivity.this, "考试开始", Toast.LENGTH_SHORT).show();
						}
						dialog.dismiss();
					}
				});

				cancel.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				break;
			case R.id.favorite:
				Cursor cursor2=null;
				try{
					cursor2=DBManager.db.query("collectTable", null, null, null, null, null, null);
					if(cursor2.getCount()!=0){
						startActivity(new Intent(this, CollectActivity.class));
					}else{
						Toast.makeText(MainActivity.this, "您还没有收藏记录", Toast.LENGTH_SHORT).show();
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(cursor2!=null){
						cursor2.close();
					}
				}
				break;
			case R.id.wrong:
				Cursor cursor=null;
				try{
					cursor=DBManager.db.query("errorTable", null, null, null, null, null, null);
					if(cursor.getCount()!=0){
						startActivity(new Intent(this, ErrorActivity.class));
					}else{
						Toast.makeText(MainActivity.this, "你还没有错题记录", Toast.LENGTH_SHORT).show();
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(cursor!=null){
						cursor.close();
					}
				}
				break;
			case R.id.history:
				Cursor cursor1=null;
				try{
					cursor1=DBManager.db.query("historyTable", null, null, null, null, null, null);
					if(cursor1.getCount()!=0){
						startActivity(new Intent(this, HisResultActivity.class));
					}else{
						Toast.makeText(MainActivity.this, "你还没有模拟考试记录", Toast.LENGTH_SHORT).show();
					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(cursor1!=null){
						cursor1.close();
					}
				}
				break;
			default:
					break;
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
	
}
