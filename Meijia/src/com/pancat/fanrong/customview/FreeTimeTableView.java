package com.pancat.fanrong.customview;

import java.util.ArrayList;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.util.LocalDateUtils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class FreeTimeTableView extends LinearLayout {
	public static final String TAG = "FreeTimeTableView";
	
	public static final int TOTALDAYS = 4;
	public static final int STARTTIME = 10;
	public static final int ENDTIME = 21;
	public static final int BUSY = 1;
	public static final int IDLE = 0;
	public static final int BORDERFORBUSY = (ENDTIME - STARTTIME) / 2;
	public static final String ONSALE = "可预约";
	public static final String NOTSALE = "被预约";
	public static final int COLNUM = 4;
	public static final int BORDERWIDTH = 2; //边框宽度
	public static final String TIMEUNIT = "点";
	public static final String SBUSY = "(忙)";
	public static final String SIDLE = "(闲)";
	
	private TableLayout timeTableLayout;
	private boolean[][] freeTime= new boolean[TOTALDAYS][ENDTIME-STARTTIME+1];
	private boolean[] busyOrIdle = new boolean[TOTALDAYS];
	
	//如果TotalDays > 4天，这里需要改动
	private String[] label = new String[]{"今天","明天","后天","大后天"};
	private ArrayList<LinearLayout> labels = new ArrayList<LinearLayout>();
	private ArrayList<LinearLayout> timeButtons = new ArrayList<LinearLayout>();
	private OnButtonClick buttonClick = null;
	
	private int curIndex = 0;
	
	//标签颜色
	private int labelFontLeft ;
	private int labelFontRightIdle ;
	private int labelFontRightBusy;
	private int labelBKGSel ;
	private int labelBKGUnSel;
	
	//按钮颜色全
	private int buttonUpFontBusy;
	private int buttonDownFontBusy;
	private int buttonUpFontIdle;
	private int buttonDownFontIdle;
	private int buttonBKGBusy;
	private int buttonBKGIdle;
	
	private int borderColor; //边框颜色
	
	private Context context;
	private LayoutInflater inflater; 
	private LinearLayout layout;
	
	public FreeTimeTableView(Context context) {
		super(context);
		this.context = context;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//layout = (LinearLayout)inflater.inflate(R.layout.free_time_table, this);
		InitAllColorAndScore();
	}

	public FreeTimeTableView setData(ArrayList<Map<Integer,Integer>>map){
		InitData(map, this.freeTime);
		BusyForToday(this.freeTime);
		InitBusyOrIdle(freeTime, busyOrIdle);
		
		InitAll();
		
		addAllView();
		return this;
	}
	
	private void addAllView()
	{
		timeTableLayout = new TableLayout(context);
		TableLayout.LayoutParams tlp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,TableLayout.LayoutParams.WRAP_CONTENT); 
	    timeTableLayout.setLayoutParams(tlp);
	    
		//TableRow.LayoutParams vlp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		//add Label
		TableRow row = new TableRow(context);
		for(int i=0;i<TOTALDAYS; i++){
			if(i != 0){
				row.addView(getVerticalView());
			}
			row.addView(labels.get(i));
		}
		//row.setLayoutParams(vlp);
		timeTableLayout.addView(row);
		timeTableLayout.addView(getUnderlineView());
		
		//add timebutton
		row = null;
		for(int i=0; i<=ENDTIME-STARTTIME; i++){
			if(i%COLNUM == 0){
				if(row != null){
					timeTableLayout.addView(row);
					timeTableLayout.addView(getUnderlineView());
				}
				row = new TableRow(context);
				//row.setLayoutParams(vlp);
			}else{
				row.addView(getVerticalView());
			}
			row.addView(timeButtons.get(i));
		}
		if(row!=null) timeTableLayout.addView(row);
		
		addView(timeTableLayout);
	}
	
	public void setButtonClickListener(OnButtonClick click){
		buttonClick = click;
		if(buttonClick != null){
			for(int i=STARTTIME; i<=ENDTIME; i++){
				final int index = i;
				timeButtons.get(i-STARTTIME).setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						buttonClick.setOnButtonClick(buttonClick(index-STARTTIME));
					}
				});
			}
		}
	}
	
	private void InitAllColorAndScore()
	{
		Resources res = context.getResources();
		
		//标签颜色
		labelFontLeft = res.getColor(R.color.free_time_label_font);
		labelFontRightIdle = res.getColor(R.color.free_time_label_idlefont);
		labelFontRightBusy = res.getColor(R.color.free_time_label_busyfont);
		labelBKGSel = res.getColor(R.color.free_time_label_sel);
		labelBKGUnSel = res.getColor(R.color.free_time_label_unsel);

		//按钮颜色全
		buttonUpFontBusy = res.getColor(R.color.free_time_dotfont);
		buttonDownFontBusy = res.getColor(R.color.free_time_busyfont);
		buttonUpFontIdle = labelBKGSel;
		buttonDownFontIdle = labelBKGSel; 
		buttonBKGBusy = res.getColor(R.color.free_time_table_busy);
		buttonBKGIdle = res.getColor(R.color.free_time_table_idle);

		borderColor = res.getColor(R.color.free_time_border);
		
	}
	
	private void UpdateTime(ArrayList<Map<Integer,Integer>>map){
		InitData(map, this.freeTime);
		BusyForToday(this.freeTime);
		InitBusyOrIdle(freeTime, busyOrIdle);
		
		changeButtonData(curIndex);
	}
	
	//标签点击选中
	private void labelClick(int index){
		if(index < 0 && index >= TOTALDAYS) return ;
		if(index == curIndex) return ;
		
		changeLabelColor(labels.get(curIndex),false,busyOrIdle[curIndex]);
		changeLabelColor(labels.get(index),true,busyOrIdle[index]);
		
		changeButtonData(index);
		curIndex = index;
	}
	//按钮点击选中
	private String buttonClick(int index){
		if(freeTime[curIndex][index]){
			String time = "";
			time += LocalDateUtils.getDate(curIndex)+" "+(index + STARTTIME)+TIMEUNIT;
			return time;
		}else{
			//Toast.makeText(context, "你不能点击别人已经预约过的时间,请重试!", Toast.LENGTH_LONG);
			return "";
		}
	}
	
	private void changeLabelColor(LinearLayout label,boolean sel,boolean busy){
		ViewHolder vh = (ViewHolder)label.getTag();
		if(sel){
			label.setBackgroundColor(labelBKGSel);
		}else{
			label.setBackgroundColor(labelBKGUnSel);
		}
		vh.left.setTextColor(labelFontLeft);
		vh.right.setTextColor(busy?labelFontRightBusy:labelFontRightIdle);
	}
	
	private void changeButtonColor(LinearLayout button,boolean busy){
		ViewHolder vh = (ViewHolder)button.getTag();
		if(busy){
			button.setBackgroundColor(buttonBKGBusy);
			vh.left.setTextColor(buttonUpFontBusy);
			vh.right.setTextColor(buttonDownFontBusy);
		}else{
			button.setBackgroundColor(buttonBKGIdle);
			vh.left.setTextColor(buttonUpFontIdle);
			vh.right.setTextColor(buttonDownFontIdle);
		}
	}
	
	private void changeButtonData(int index){
		for(int i=STARTTIME; i<=ENDTIME; i++){
			LinearLayout button = timeButtons.get(i-STARTTIME);
			ViewHolder vh = (ViewHolder)button.getTag();
			vh.right.setText((freeTime[index][i-STARTTIME])?ONSALE:NOTSALE);
			changeButtonColor(button, !freeTime[index][i-STARTTIME]);
		}
	}
	
	//获取水平边框视图
	private View getUnderlineView()
	{
		View underlineView = new View(context);
		TableLayout.LayoutParams ulp = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,BORDERWIDTH);
		underlineView.setLayoutParams(ulp);
		underlineView.setBackgroundColor(borderColor);
		return underlineView;
	}
	
	private View getVerticalView(){
		View verticalView = new View(context);
		TableRow.LayoutParams vlp = new TableRow.LayoutParams(BORDERWIDTH,TableRow.LayoutParams.MATCH_PARENT);
		verticalView.setLayoutParams(vlp);
		verticalView.setBackgroundColor(borderColor);
		return verticalView;
	}
	
	private void InitAll(){
		TableRow.LayoutParams vlp = new TableRow.LayoutParams(0,TableRow.LayoutParams.WRAP_CONTENT);
		vlp.weight = 1;
		
		//初始化标签数组
		for(int i=0; i<TOTALDAYS; i++){
			LinearLayout layoutLabel = (LinearLayout)inflater.inflate(R.layout.free_time_label_item, null);
			TextView a = (TextView)layoutLabel.findViewById(R.id.free_time_label_left);
			TextView b = (TextView)layoutLabel.findViewById(R.id.free_time_label_right);
			ViewHolder vh = new ViewHolder(a, b);
			layoutLabel.setTag(vh);
			a.setText(label[i]);
			b.setText((busyOrIdle[i])?SBUSY:SIDLE);
			changeLabelColor(layoutLabel, curIndex == i,busyOrIdle[i]);
			
			layoutLabel.setPadding(12, 12, 12, 12);
			layoutLabel.setLayoutParams(vlp);
			
			//标签监听事件
			final int index = i;
			layoutLabel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					labelClick(index);
				}
			});
			
			labels.add(layoutLabel);
		}
		
		//初始化时间按钮
		for(int i=STARTTIME; i<=ENDTIME; i++){
			LinearLayout layout_button = (LinearLayout)inflater.inflate(R.layout.free_time_item, null);
			TextView up = (TextView)layout_button.findViewById(R.id.free_time_item1);
			TextView down = (TextView)layout_button.findViewById(R.id.free_time_item2);
			ViewHolder bvh = new ViewHolder(up, down);
			layout_button.setTag(bvh);
			
			up.setText(i+TIMEUNIT);
			down.setText((freeTime[curIndex][i-STARTTIME])?ONSALE:NOTSALE);
			
			changeButtonColor(layout_button, !freeTime[curIndex][i-STARTTIME]);
			layout_button.setLayoutParams(vlp);
			
			timeButtons.add(layout_button);
		}
	}
	
	//初始化当天是否忙闲,根据BORDERFORBUSY来确定
	private static void InitBusyOrIdle(boolean[][]time,boolean[]busy){
		int falseNum = 0;
		for(int i=0; i<TOTALDAYS; i++){
			falseNum = 0;
			for(int j=STARTTIME; j<=ENDTIME; j++)
				if(!time[i][j-STARTTIME]) falseNum++;
			
			if(falseNum>=BORDERFORBUSY) busy[i] = true;
		}
	}
	
	//对今天当前小时之前的小时初始化为忙
	private static void BusyForToday(boolean[][] time){
		int hour = LocalDateUtils.getHour();
		Log.d(TAG, "curHour:  "+ hour);
		for(int i=STARTTIME; i<=hour+1 && i<=ENDTIME; i++){
			time[0][i-STARTTIME] = false;
		}
	}
	
	//初始化时间表，是否可预约,false为不可预约(忙)
	private static void InitData(ArrayList<Map<Integer,Integer>>map,boolean[][] time){
		for(int i=0;i<TOTALDAYS;i++){
			if((map==null) || map.size()<i+1){ //未提供数据，则显示为忙
				for(int j=STARTTIME;j<=ENDTIME;j++){
					time[i][j - STARTTIME] = false;
				}
			}else{
				for(int j=STARTTIME;j<=ENDTIME; j++){
					if(map.get(i).containsKey(j)){
						int t = map.get(i).get(j);
						 time[i][j-STARTTIME] = t == IDLE;
					}else 
						time[i][j-STARTTIME] = false;
				}
			}
		}
	}
	
	
	private static class ViewHolder{
		TextView left;
		TextView right;
		public ViewHolder(TextView l,TextView r) {
			left = l;
			right = r;
		}
	}
	
	public interface OnButtonClick{
		public void setOnButtonClick(String time);
	}
}
