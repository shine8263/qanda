package com.binwang.qanda.ui;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.binwang.qanda.R;
import com.binwang.qanda.sqlite.DBManager;

import android.R.color;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ExamErrorActivity extends Activity implements OnClickListener,OnItemClickListener{
	
	private TextView selectOne;
	private TextView selectTwo;
	private TextView selectThree;
	private TextView selectFour;
	private TextView title;//题目栏
	private TextView answer;//答题栏
	private TextView type;
	private ImageView imageOne;
	private ImageView imageTwo;
	private ImageView imageThree;
	private ImageView imageFour;
	private RelativeLayout relOne;
	private RelativeLayout relTwo;
	private RelativeLayout relThree;
	private RelativeLayout relFour;
	private TextView subjectTop;
	private TextView submit;
	private TextView rightAnswer;
	private int timuMax;//题目的总数量
	private int timuCurrent=0;//显示当前题目是第几题，0表示第一题
	private String[] selectRight;//所有表中收藏的题目每一题正确答案的集合
	private String[] selected;//每一题用户所选的选项记录的集合
	private Dialog dialog;//选题框相关
	private GridView gridView1;//选题框相关
	private View select_subject_layout;//选题框相关
	private int[] gridViewItemColor;//选题框相关
	private String[] gridViewItemText;//选题框相关
	private List<Map<String, Object>> gridViewItemList;//选题框相关
	private SimpleAdapter sim_adapter;//选题框相关
	private String timu = null;//当前题目
	private String daan = null;//当前答案
	protected int activityCloseEnterAnimation;
	protected int activityCloseExitAnimation;
	private String TAG;//覆写finish()有用，不知道具体用处...

	/*public static void intentToExamActivity(Context context, String name) {
		Intent intent = new Intent(context, ExamActivity.class);
		intent.putExtra(NAME, name);
		context.startActivity(intent);
	}*/
	
	//submit.setText("调试用");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam);
		resetTitlebar();
		//设定进入该activity的动画效果
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
		//以下是各种组件的实例
		title = (TextView) findViewById(R.id.title);
		selectOne = (TextView) findViewById(R.id.selectOne);
		selectTwo = (TextView) findViewById(R.id.selectTwo);
		selectThree = (TextView) findViewById(R.id.selectThree);
		selectFour = (TextView) findViewById(R.id.selectFour);
		rightAnswer = (TextView) findViewById(R.id.rightAnswer);
		answer = (TextView) findViewById(R.id.answer);
		type = (TextView) findViewById(R.id.type);
		RelativeLayout back = (RelativeLayout) findViewById(R.id.back);
		RelativeLayout subject = (RelativeLayout) findViewById(R.id.subject);
		RelativeLayout collect = (RelativeLayout) findViewById(R.id.collect);
		RelativeLayout forward = (RelativeLayout) findViewById(R.id.forward);
		relOne = (RelativeLayout) findViewById(R.id.relOne);
		relTwo = (RelativeLayout) findViewById(R.id.relTwo);
		relThree = (RelativeLayout) findViewById(R.id.relThree);
		relFour = (RelativeLayout) findViewById(R.id.relFour);
		selectOne = (TextView) findViewById(R.id.selectOne);
		selectTwo = (TextView) findViewById(R.id.selectTwo);
		selectThree = (TextView) findViewById(R.id.selectThree);
		selectFour = (TextView) findViewById(R.id.selectFour);
		imageOne = (ImageView) findViewById(R.id.imageOne);
		imageTwo = (ImageView) findViewById(R.id.imageTwo);
		imageThree = (ImageView) findViewById(R.id.imageThree);
		imageFour = (ImageView) findViewById(R.id.imageFour);
		subjectTop = (TextView) findViewById(R.id.tv_subjectTop);
		submit = (TextView) findViewById(R.id.submit);
		//原例注释：先读取数据库中的缓存, 数据量较多比较耗时，应使用AsyncTask
		//原例注释：new QueryTask().execute();
		//以下是给各个组件加监听器
		submit.setOnClickListener(this);
		back.setOnClickListener(this);
		subject.setOnClickListener(this);
		collect.setOnClickListener(this);
		forward.setOnClickListener(this);
		//以下是各种初始化
		initialTimuMaxandRightselected();
		initialGridViewItem(timuMax);
		setSelectTfandgridColorText();
		//资料准备完全，显示题目
		showTimu();
	}
	
	//重设标题栏
	private void resetTitlebar() {
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.view_comm_titlebar);
		TextView title = (TextView) findViewById(R.id.titlebar_title);
		LinearLayout back = (LinearLayout) findViewById(R.id.titlebar_left_layout);
		title.setText("查看所有错题");
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override//覆写finish()，目的是保证返回有动画效果
	public void finish() {
		Log.i(TAG,  "finish");
		super.finish();
		overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
	}
	
	//初始化选题框item颜色、选题框item数字
	private void initialGridViewItem(int this_timuMax){
		gridViewItemColor=new int[this_timuMax];
		gridViewItemText=new String[this_timuMax];
	}
	
	//用来确认收藏表单里面是否已经有这一题了，有相同的返回true值
	public boolean hasTheSame(){
		Cursor cursor=null;
		try{
			cursor =DBManager.db.query("collectTable",null,null,null,null,null,null);
			cursor.moveToFirst();
			if(cursor.getString(0).equals(timu)){
				return true;
			}else{
			}
			while(cursor.moveToNext()){
				if(cursor.getString(0).equals(timu)){
					return true;
				}else{
				}
			}
		}catch(Exception e){
			    e.printStackTrace();
		}finally{
			    if(cursor != null){
			        cursor.close();
			    }
		}
		return false;
	}
	
	//得到题目总数timuMax和每题的正确答案集合selectRight[]以及读取selected
	public void initialTimuMaxandRightselected(){
		Cursor cursor=null;
		try{
			cursor =DBManager.db.query("examerrorTable",null,null,null,null,null,null);
			//设定timuMax数值
			timuMax=cursor.getCount();
			//设定sxelectRight数组大小
			selectRight=new String[timuMax];
			selected=new String[timuMax];
			//遍历cursor，提取所有正确答案
			for(int i=0;i<cursor.getCount();i++){
				cursor.moveToPosition(i);
				selectRight[i]=cursor.getString(1).split("\\|")[4];
				selected[i]=cursor.getString(2);
			}
			}catch(Exception e){
			    e.printStackTrace();
			}finally{
			    if(cursor != null){
			        cursor.close();
			    }
			}
		
	}
	
	//显示题目
	public void showTimu(){
		setSelectBgColor();
		Cursor cursor = null;
		try{
		cursor =DBManager.db.query("examerrorTable",null,null,null,null,null,null);
		if(cursor.moveToFirst()) {   
	    	  cursor.move(timuCurrent);
	    	timu=cursor.getString(0);   
	    	daan=cursor.getString(1); 	
		}
		String[] daans=daan.split("\\|");
		title.setText((timuCurrent+1)+"."+timu);
		selectOne.setText(daans[0]);
		selectTwo.setText(daans[1]);
		selectThree.setText(daans[2]);
		selectFour.setText(daans[3]);
		subjectTop.setText(timuCurrent+1+"/"+timuMax);
		}catch(Exception e){
		    e.printStackTrace();
		}finally{
		    if(cursor != null){
		        cursor.close();
		    }
		}
	}
	
	@Override//答题监听器
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.forward:
			 if(timuCurrent<timuMax-1){
				 timuCurrent++;
				 showTimu();
			 }
			break;
		case R.id.back:
			 if(timuCurrent>0){
				 timuCurrent--;
				 showTimu();
			 }
			break;
		//选题框
		case R.id.subject:
			dialog = new AlertDialog.Builder(this).create();
			//显示dialog
			dialog.show();
			//实例选题框布局
			select_subject_layout = getLayoutInflater().inflate(R.layout.select_subject, null);
			//把选题框布局装进dialog
			dialog.getWindow().setContentView(select_subject_layout);
			//得到gridview网格组件里面item的组件里的属性list，背景色、下面的数字题号
			getGridViewList();
			//准备好item里面两个组件的id
			String[] from ={"gridViewItemImage2","gridViewItemText2"};
			//准备好item里面两个组件的实际物理地址int值
	        int[] to = {R.id.gridViewItemImage2,R.id.gridViewItemText2};
	        //把所有准备好的资料装进适配器
	        sim_adapter=new SimpleAdapter(this, gridViewItemList, R.layout.item2_gridview_selectsubject, from, to);
			//把适配器装进gridView里面
	        gridView1 = (GridView)select_subject_layout.findViewById(R.id.gridView1);
	        gridView1.setAdapter(sim_adapter);
	        gridView1.smoothScrollToPosition(getBoundPosition());
	        //给gridView装监听器
	        gridView1.setOnItemClickListener(this);
	        break;
	    //收藏题目
		case R.id.collect:
			//有相同的，就不收藏了
			if(hasTheSame()){
				Toast.makeText(this, "这一题已经收藏了", Toast.LENGTH_SHORT).show();
			}else{
			//没有收藏过，就可以收藏
				ContentValues cv=new ContentValues();
				cv.put("timu",timu);
				cv.put("daan",daan);
				DBManager.db.insert("collectTable", null, cv);
				Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
				break;
	}
	}
	
	//设定一个int值，使打开选题框时始终把当前的题目为中心
	public int getBoundPosition(){
		if(timuCurrent+5>timuMax-1){
			return timuMax-1;
		}else{
		}
		return timuCurrent+5;
	}
	
	@Override//gridView中的item的点击监听器
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		timuCurrent=position;
		showTimu();
		dialog.dismiss();
	}
	
	//设置答题对错结果集合以及gridView的item里面要输入的list
	public void setSelectTfandgridColorText(){
		for(int i=0;i<timuMax;i++){
			if(selected[i].equals("wu")){
				gridViewItemColor[i]=R.color.select_agodefault;
			}else if(selected[i].equals(selectRight[i])){
				gridViewItemColor[i]=R.color.select_right;
			}else if(selected[i]!=selectRight[i]){
				gridViewItemColor[i]=R.color.select_error;
			}
			gridViewItemText[i]=String.valueOf(i+1);
		}
	}
	
	//返回选择题目中item的数据list
	public List<Map<String, Object>> getGridViewList(){        
		gridViewItemList = new ArrayList<Map<String, Object>>();
		setSelectTfandgridColorText();
        for(int i=0;i<timuMax;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("gridViewItemImage2", gridViewItemColor[i]);
            map.put("gridViewItemText2", gridViewItemText[i]);
            gridViewItemList.add(map);
        }
        return gridViewItemList;
    }
	
	//设定题目所有选项正确与否的背景色，onCreate()运行一次，每次showTimu()都要运行一次
	public void setSelectBgColor(){
			setDefaultColor();
			if(selected[timuCurrent].equals("A")){
				setErrorColor(selectOne,relOne,imageOne);
			}else if(selected[timuCurrent].equals("B")){
				setErrorColor(selectTwo,relTwo,imageTwo);
			}
			else if(selected[timuCurrent].equals("C")){
				setErrorColor(selectThree,relThree,imageThree);
			}
			else if(selected[timuCurrent].equals("D")){
				setErrorColor(selectFour,relFour,imageFour);
			}
			
			if(selectRight[timuCurrent].equals("A")){
				setRightColor(selectOne,relOne,imageOne);
			}else if(selectRight[timuCurrent].equals("B")){
				setRightColor(selectTwo,relTwo,imageTwo);
			}
			else if(selectRight[timuCurrent].equals("C")){
				setRightColor(selectThree,relThree,imageThree);
			}
			else{
				setRightColor(selectFour,relFour,imageFour);
			}
	}
	
	
	/*if(selected[timuCurrent]=="A"){
		setErrorColor(selectOne,relOne,imageOne);
	}else if(selected[timuCurrent]=="B"){
		setErrorColor(selectTwo,relTwo,imageTwo);
	}
	else if(selected[timuCurrent]=="C"){
		setErrorColor(selectThree,relThree,imageThree);
	}
	else{
		setErrorColor(selectFour,relFour,imageFour);
	}*/
	
	private void setDefaultColor(){
		selectOne.setBackgroundResource(R.color.select_default);
		selectTwo.setBackgroundResource(R.color.select_default);
		selectThree.setBackgroundResource(R.color.select_default);
		selectFour.setBackgroundResource(R.color.select_default);
		relOne.setBackgroundResource(R.color.select_default);
		relTwo.setBackgroundResource(R.color.select_default);
		relThree.setBackgroundResource(R.color.select_default);
		relFour.setBackgroundResource(R.color.select_default);
		imageOne.setImageResource(R.drawable.defaults);
		imageTwo.setImageResource(R.drawable.defaults);
		imageThree.setImageResource(R.drawable.defaults);
		imageFour.setImageResource(R.drawable.defaults);
	}
	
	private void setErrorColor(TextView a,RelativeLayout b,ImageView c){
		a.setBackgroundResource(R.color.select_error);
		b.setBackgroundResource(R.color.select_error);
		c.setImageResource(R.drawable.wrong);
	}

	private void setRightColor(TextView a,RelativeLayout b,ImageView c){
		a.setBackgroundResource(R.color.select_right);
		b.setBackgroundResource(R.color.select_right);
		c.setImageResource(R.drawable.right);
	}

	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_gridview_selectsubject, null);
			}
			TextView tv = com.binwang.qanda.util.ViewHolder.get(convertView, R.id.gridViewItemText);
			if(selected[position]==selectRight[position]){
				tv.setBackgroundColor(Color.parseColor("#00CC00"));
			}else{
				tv.setBackgroundColor(Color.parseColor("#CC0000"));
			}
			tv.setText("" + timuCurrent);
			return convertView;
		}
		
	}

}
//疑点1：为什么String之间的相等有的时候==好用，有的时候必须用equals