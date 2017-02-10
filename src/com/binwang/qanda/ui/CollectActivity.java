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
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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

public class CollectActivity extends Activity implements OnClickListener,OnItemClickListener{
	
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
	private TextView help;
	private int timuMax;//题目的总数量
	private int timuCurrent=0;//显示当前题目是第几题，0表示第一题
	private String[] selectRight;//所有表中收藏的题目每一题正确答案的集合
	private String[] selected;//每一题用户所选的选项记录的集合
	private MyAdapter myAdapter;
	private String timu = null;//当前题目
	private String daan = null;//当前答案
	private Dialog dialog;//选题框相关
	private GridView gridView1;//选题框相关
	private View select_subject_layout;//选题框相关
	private int[] gridViewItemColor;//选题框相关
	private String[] gridViewItemText;//选题框相关
	private List<Map<String, Object>> gridViewItemList;//选题框相关
	private SimpleAdapter sim_adapter;//选题框相关
	private String TAG;//覆写finish()有用，不知道具体用处...
	
	//注意：collectTable已经预先在其中插入了一个全为空的题目
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collect);
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
		RelativeLayout collect = (RelativeLayout) findViewById(R.id.remove);
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
		help = (TextView) findViewById(R.id.help);
		RelativeLayout rel_help = (RelativeLayout) findViewById(R.id.rel_help);
		//原例注释：先读取数据库中的缓存, 数据量较多比较耗时，应使用AsyncTask
		//原例注释：new QueryTask().execute();
		//以下是给各个组件加监听器
		submit.setOnClickListener(this);
		selectOne.setOnClickListener(this);
		selectTwo.setOnClickListener(this);
		selectThree.setOnClickListener(this);
		selectFour.setOnClickListener(this);
		back.setOnClickListener(this);
		subject.setOnClickListener(this);
		collect.setOnClickListener(this);
		forward.setOnClickListener(this);
		rel_help.setOnClickListener(this);
		//以下是各种初始化
		//提醒没有收藏题目
		/*原来是initialTimuMaxandRight，
		但是由于在去掉收藏时要重设最大题数，所以改为setTimuMaxandRight*/
		setTimuMaxandRight();
		initialSelectedandTf(timuMax);
		initialGridViewItem(timuMax);
		setSelectTfandgridColorText();
		//资料准备完全，显示题目
		showTimu();
	}
	
	//重设标题栏
	private void resetTitlebar() {
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.view_comm_titlebar);
		TextView title = (TextView) findViewById(R.id.titlebar_title);
		TextView right = (TextView) findViewById(R.id.titlebar_right_text);
		LinearLayout back = (LinearLayout) findViewById(R.id.titlebar_left_layout);
		title.setText("我的收藏");
		right.setText("清空");
		right.setOnClickListener(this);
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
	
	//初始化答题情况、答题结果
		public void initialSelectedandTf(int this_timuMax){
			selected=new String[this_timuMax];
			for(int i=0;i<selected.length;i++){
				selected[i]="wu";
			}
		}
		
		//初始化选题框item颜色、选题框item数字
		private void initialGridViewItem(int this_timuMax){
			gridViewItemColor=new int[this_timuMax];
			gridViewItemText=new String[this_timuMax];
		}
	
	
	//得到题目总数timuMax和每题的正确答案集合selectRight[]以及每题已答的答案selected
	//下面这个getTimuMax是专为collect类remove题目以后用来重设最大题数用的
	public void getTimuMax(){
		Cursor cursor=null;
		try{
			cursor =DBManager.db.query("collectTable",null,null,null,null,null,null);
			timuMax=cursor.getCount();
		}catch(Exception e){
		    e.printStackTrace();
		}finally{
		    if(cursor != null){
		        cursor.close();
		    }
		}
	}
	public void setSelected(){
		for(int i=0;i<timuMax;i++){
			
		}
	}
	
	//用来判断收藏表单中是否存在某个相同的String的题目，有则返回true
			public boolean hasTheSame(String str){
				//submit.setText("进入了getTimuMaxandRight()");
				Cursor cursor=null;
				try{
					cursor =DBManager.db.query("collectTable",null,null,null,null,null,null);
					cursor.moveToFirst();
					if(cursor.getString(0).equals(str)){
						return true;
					}else{
					}
					while(cursor.moveToNext()){
						if(cursor.getString(0).equals(str)){
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
	
	//得到题目总数timuMax和每题的正确答案集合selectRight[]以及每题已答的答案selected
	public void setTimuMaxandRight(){
		//submit.setText("进入了getTimuMaxandRight()");
		Cursor cursor=null;
		try{
			cursor =DBManager.db.query("collectTable",null,null,null,null,null,null);
			//设定timuMax数值
			timuMax=cursor.getCount();
			//设定sxelectRight数组大小
			selectRight=new String[timuMax];
			//遍历cursor，提取所有正确答案
			for(int i=0;i<cursor.getCount();i++){
				cursor.moveToPosition(i);
				selectRight[i]=cursor.getString(1).split("\\|")[4];
			}
			}catch(Exception e){
			    e.printStackTrace();
			}finally{
			    if(cursor != null){
			        cursor.close();
			    }
			}
		
	}
	/*private class QueryTask extends AsyncTask<Params, Progress, Result>*/
	
	//显示题目
	public void showTimu(){
		//submit.setText("进入了showTimu()");
		setSelectBgColor();
		Cursor cursor = null;
		try{
		cursor =DBManager.db.query("collectTable",null,null,null,null,null,null);
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
		//submit.setText("selectRight为"+selectRight[timuCurrent]+"  selected为"+selected[timuCurrent]);
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
			case R.id.selectOne:
				if(selected[timuCurrent]=="wu"){
					selected[timuCurrent]="A";
					setSelectBgColor();
				}
			break;
			case R.id.selectTwo:
				if(selected[timuCurrent]=="wu"){
					selected[timuCurrent]="B";
					setSelectBgColor();
				}
			break;
			case R.id.selectThree:
				if(selected[timuCurrent]=="wu"){
					selected[timuCurrent]="C";
					setSelectBgColor();
				}
			break;
			case R.id.selectFour:
				if(selected[timuCurrent]=="wu"){
					selected[timuCurrent]="D";
					setSelectBgColor();
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
	        
	    //取消收藏
		case R.id.remove:
			//题目总数大于1，则删除此题
			if(timuMax>1){
				String[] whereArgs = {timu};  
				DBManager.db.delete("collectTable", "timu=?", whereArgs);
				Toast.makeText(this,"取消成功", Toast.LENGTH_SHORT).show();
				selected=getStringArray(selected, timuCurrent);
				setTimuMaxandRight();
				if(timuCurrent>timuMax-1){
					timuCurrent--;
				}else{
				}
			//题目总数只剩一题，则把它变成空题
			}else if(timuMax==1){
				String[] whereArgs = {timu};  
				DBManager.db.delete("collectTable", "timu=?", whereArgs);
				finish();
				Toast.makeText(this,"已清空全部收藏记录", Toast.LENGTH_SHORT).show();
			}else{
				
			}
			showTimu();
			break;
		//清空收藏
		case R.id.titlebar_right_text:
			AlertDialog dialog = new AlertDialog.Builder(this).setMessage("确定要清空吗？")
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					DBManager.db.delete("collectTable", null, null);
					finish();
					Toast.makeText(CollectActivity.this, "已清空全部收藏的题目", Toast.LENGTH_SHORT).show();
				}
			}).setNegativeButton("取消", null).create();
	dialog.show();
				break;
		default:
				break;
	}
	}
	
	//这个方法是我自己写的用来去掉字符串数组中指定位置的元素用的
	public String[] getStringArray(String[] strs,int index){
		List<String> strlist=new ArrayList<String>();
		int m=0;
		for(String str:strs){
			if(m!=index){
			strlist.add(str);
			}else{}
			m++;
		}
		strs=new String[strlist.size()];
		strs=strlist.toArray(strs);
	return strs;
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
			if(selected[i]=="wu"){
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
		if(selected[timuCurrent]=="wu"){
			setDefaultColor();
		}
		else{
			setDefaultColor();
			if(selected[timuCurrent]=="A"){
				setErrorColor(selectOne,relOne,imageOne);
			}else if(selected[timuCurrent]=="B"){
				setErrorColor(selectTwo,relTwo,imageTwo);
			}
			else if(selected[timuCurrent]=="C"){
				setErrorColor(selectThree,relThree,imageThree);
			}
			else{
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
	}
	
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