package com.example.horizontalflowchart;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.horizontalflowchart.HorizontalFlowChartView.OnTouchCircleListener;

public class MainActivity extends Activity {
	
	private HorizontalFlowChartView horizontalflowchart,horizontalflowchart1,horizontalflowchart2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		horizontalflowchart =(HorizontalFlowChartView) findViewById(R.id.horizontalflowchart);
		horizontalflowchart1 = (HorizontalFlowChartView) findViewById(R.id.horizontalflowchart1);
		horizontalflowchart.setText(new String[]{"��ʼ","����","�չ�","�ص�����"});
		horizontalflowchart1.setText(new String[]{"����","�չ�","�ص�����"});
		horizontalflowchart2 = (HorizontalFlowChartView) findViewById(R.id.horizontalflowchart2);
		horizontalflowchart2.setText(new String[]{"��ʼ","����","�չ�","�ص�����"});
	}

}
