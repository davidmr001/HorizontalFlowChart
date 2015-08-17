package com.example.horizontalflowchart;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class HorizontalFlowChartView extends View {

	// Ĭ�����߶θ߶ȣ�dp
	private final int DEFAULT_BIG_LINE_HEIGHT = 12;
	// Ĭ�����߶θ߶ȣ�dp
	private final int DEFAULT_SMALL_LINE_HEIGHT = 5;

	// Ĭ��Բ������
	private final int DEFAULT_CIRCLE_SUM = 3;

	// Ĭ����Բ�����ߣ���ɫ
	private final int DEFAULT_BIG_COLOR = Color.parseColor("#c0c0c0");
	// Ĭ����Բ�����ߣ���ɫ
	private final int DEFAULT_SMALL_COLOR = Color.parseColor("#789262");

	// Ĭ��Բ�����ı�
	private final boolean DEFAULT_HAS_TEXT = true;
	// Ĭ���ı���С��sp
	private final int DEFAULT_TEXT_SIZE = 12;
	// Ĭ���ı���ɫ
	private final int DEFAULT_TEXT_COLOR = Color.parseColor("#ffffff");

	// Ĭ�ϴ���ʱ��Բ����ɫ
	private final int DEFAULT_TOUCH_CIRCLE_COLOR = Color.parseColor("#555555");
	
	//Ĭ�϶�������
	private final int DEFAULT_LOADING_RATE = 0;
	
	//Ĭ���ı�����
	private final int DEFAULT_TEXT_GRAVITY = 0;
	
	//�ı����֣��ı���Բ��
	private final int TEXT_GRAVITY_CENTER = 0;
	//�ı����֣��ı���Բ��
	private final int TEXT_GRAVITY_BOTTOM = 1;
	//�ı����֣��ı���Բ��
	private final int TEXT_GRAVITY_TOP = 2;
	
	//Ĭ���ı�ƫ��
	private final int DEFAULT_TEXT_OFFSET = 5;
	

	// ��Բ�뾶
	private int mBigCircleRadius;
	// ��Բ�뾶
	private int mSmallCircleRadius;
	// ���߶γ���
	private int mBigLineWidth;
	// ���߶γ���
	private int mSmallLineWidth;
	// ���߶θ߶�
	private int mBigLineHeight;
	// ���߶θ߶�
	private int mSmallLineHeight;

	// Բ������
	private int mCircleSum;
	
	//Բ�ĵ�y������
	private float mCircleY;

	// ��Բ�����ߣ�����ɫ
	private int mBigColor;
	// ��Բ�����ߣ�����ɫ
	private int mSmallColor;

	// ��view��ʵ�Ŀ�Ⱥ͸߶�
	private int mRealWidth, mRealHeight;

	// ����
	private Paint mPaint = new Paint();

	// ��ǰ���ȣ�Ĭ��Ϊ0
	private int mProgress = 0;
	// ��ǰ���ȵ����ֵ
	private int mMaxProgress;
	//��ǰ���ȵ�Ŀ��ֵ
	private int mToProgress = mProgress;
	
	//���õ�ǰ����
	public void setNowCircle(int  num)
	{
		mProgress = 180*num + mSmallLineWidth*(num-1);
		mToProgress = mProgress;
	}

	// Բ���Ƿ����ı�
	private boolean mHasText;
	// �ı������С
	private int mTextSize;
	// �ı�������ɫ
	private int mTextColor;
	//�ı�����
	private int mTextGravity;
	//�ı�ƫ��
	private int mTextOffset;
	//�ı���Y������
	private float mTextY;;

	// �����¼��У�Բ��˳��
	private int mTouchCircle = -1;
	// ����ʱԲ�ı�����ɫ
	private int mTouchCircleColor;

	// �ı�
	private String[] mText;

	// �����ı�
	public void setText(String[] mText) {
		this.mText = mText;
		invalidate();
	}

	// ���ԲȦ�¼�������
	private OnTouchCircleListener onTouchCircleListener;

	// ���õ��ԲȦʱ�������
	public OnTouchCircleListener setOnTouchCircleListener(
			OnTouchCircleListener onTouchCircleListener) {
		this.onTouchCircleListener = onTouchCircleListener;
		return this.onTouchCircleListener;
	}
	
	//��������
	private int mLoadingRate;
	//���ý׶��Զ�������ʱ��
	public void setLoadingRate(int mLoadingRate)
	{
		this.mLoadingRate = mLoadingRate;
	}
	

	
	public HorizontalFlowChartView(Context context) {
		this(context, null);
	}

	public HorizontalFlowChartView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HorizontalFlowChartView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		obtainStyledAttributes(attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		// ��ø�view��ʵ�Ŀ�Ⱥ͸߶�
		mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
		mRealHeight = getMeasuredHeight() - getPaddingTop()
				- getPaddingBottom();

		mPaint.setTextSize(mTextSize);
		
		
		//Բ�ĵ�Y������
		switch(mTextGravity)
		{
		case TEXT_GRAVITY_CENTER:
			// �Ը�view�Ĵ�С�Լ�Բ���������Ĭ�ϵ���Բ�뾶
			if(mBigCircleRadius == -1)
			mBigCircleRadius = Math.min(mRealWidth / (3 * mCircleSum-1),
					mRealHeight / 2);
			//���Բ��Y��
			mCircleY = getPaddingTop() + mBigCircleRadius;
			mTextY = mCircleY-(mPaint.descent() + mPaint.ascent())/2;
			break;
		case TEXT_GRAVITY_BOTTOM:
			//�Ը�view�Ĵ�С�Լ�Բ���������ı�ƫ�ƻ��Ĭ�ϵ���Բ�뾶
			if(mBigCircleRadius == -1)
				mBigCircleRadius = (int) Math.min(mRealWidth/(3*mCircleSum-1), (mRealHeight-(mPaint.descent() - mPaint.ascent())-mTextOffset)/2);
			//���Բ������
			mCircleY = getPaddingTop() + mBigCircleRadius;
			mTextY = mCircleY + mBigCircleRadius +mTextOffset +(mPaint.descent() - mPaint.ascent()) ;
			break;
		case TEXT_GRAVITY_TOP:
			//�Ը�view�Ĵ�С�Լ�Բ���������ı�ƫ�ƻ��Ĭ�ϵ���Բ�뾶
			if(mBigCircleRadius == -1)
				mBigCircleRadius = (int) Math.min(mRealWidth/(3*mCircleSum-1), (mRealHeight-(mPaint.descent() - mPaint.ascent())-mTextOffset)/2);
			//���Բ������
			mCircleY = getPaddingTop() + mTextOffset + (mPaint.descent() - mPaint.ascent())+mBigCircleRadius;
			mTextY = getPaddingTop() + (mPaint.descent()-mPaint.ascent());
			break;
		}
		
		
		//���߳���
		if(mBigLineWidth == -1)
			mBigLineWidth = (mRealWidth-mCircleSum*2*mBigCircleRadius)/(mCircleSum-1);
		
		// ����Բ�뾶��4/5��ΪĬ����Բ�뾶
				if(mSmallCircleRadius == -1)
				mSmallCircleRadius = mBigCircleRadius * 4 / 5;

				// ���Ĭ�����߳���
				mSmallLineWidth = mBigLineWidth + 2
						* (mBigCircleRadius - mSmallCircleRadius);

				// ��ǰ���ȵ����ֵ
				mMaxProgress = 180 * mCircleSum + mSmallLineWidth * (mCircleSum - 1);
		
	}
	
	//���Զ��������л������ֵ
	private void obtainStyledAttributes(AttributeSet attrs)
	{
		final TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalFlowChartView);
		
		//��ȡ������
		mCircleSum = typedArray.getInteger(R.styleable.HorizontalFlowChartView_circle_sum, DEFAULT_CIRCLE_SUM);
		
		mBigCircleRadius = (int) typedArray.getDimension(R.styleable.HorizontalFlowChartView_big_circle_radius, -1);
		mSmallCircleRadius = (int) typedArray.getDimension(R.styleable.HorizontalFlowChartView_small_circle_radius, -1);
		
		mBigLineWidth = (int) typedArray.getDimension(R.styleable.HorizontalFlowChartView_big_line_width, -1);
		
		mBigLineHeight = (int) typedArray.getDimension(R.styleable.HorizontalFlowChartView_big_line_height, dp2px(DEFAULT_BIG_LINE_HEIGHT));
		mSmallLineHeight = (int) typedArray.getDimension(R.styleable.HorizontalFlowChartView_small_line_height, dp2px(DEFAULT_SMALL_LINE_HEIGHT));
		
		mBigColor = typedArray.getColor(R.styleable.HorizontalFlowChartView_big_color, DEFAULT_BIG_COLOR);
		mSmallColor = typedArray.getColor(R.styleable.HorizontalFlowChartView_small_color, DEFAULT_SMALL_COLOR);
		
		mHasText = typedArray.getBoolean(R.styleable.HorizontalFlowChartView_has_text, DEFAULT_HAS_TEXT);
		mTextColor = typedArray.getColor(R.styleable.HorizontalFlowChartView_text_color, DEFAULT_TEXT_COLOR);
		mTextSize = (int) typedArray.getDimension(R.styleable.HorizontalFlowChartView_text_size, sp2px(DEFAULT_TEXT_SIZE));	
		
		mTouchCircleColor = typedArray.getColor(R.styleable.HorizontalFlowChartView_touch_circle_color, DEFAULT_TOUCH_CIRCLE_COLOR);
		mLoadingRate = typedArray.getInteger(R.styleable.HorizontalFlowChartView_loading_rate, DEFAULT_LOADING_RATE);
		
		mTextGravity = typedArray.getInteger(R.styleable.HorizontalFlowChartView_text_gravity, DEFAULT_TEXT_GRAVITY);
		mTextOffset = (int) typedArray.getDimension(R.styleable.HorizontalFlowChartView_text_offset, dp2px(DEFAULT_TEXT_OFFSET));
		
		typedArray.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// ���ÿ����
		mPaint.setAntiAlias(true);
		// ������Χ
		drawBig(canvas);
		// ������Χ
		drawSmall(canvas);
		// �����ı�
		if (mHasText) {
			drawText(canvas);
		}
	}

	// ������Χ����
	protected void drawBig(Canvas canvas) {
		// ���û�������
		mPaint.setColor(mBigColor);
		mPaint.setStyle(Style.FILL);
		mPaint.setStrokeWidth(mBigLineHeight);

		// ��Բ
		for (int i = 1; i <= mCircleSum; i++) {
			if (mTouchCircle >= 0 && mTouchCircle == i - 1) {
				mPaint.setColor(mTouchCircleColor);
			} else {
				mPaint.setColor(mBigColor);
			}

			canvas.drawCircle(getPaddingLeft() + i * 2 * mBigCircleRadius
					- mBigCircleRadius + (i - 1) * mBigLineWidth,
					mCircleY, mBigCircleRadius,
					mPaint);
		}
		mPaint.setColor(mBigColor);
		// ����
		for (int i = 1; i < mCircleSum; i++)
			canvas.drawLine(getPaddingLeft() + i * 2 * mBigCircleRadius
					+ (i - 1) * mBigLineWidth-1, mCircleY, getPaddingLeft() + i * 2
					* mBigCircleRadius + i * mBigLineWidth+1, mCircleY, mPaint);
	}

	// ������Χ����
	protected void drawSmall(Canvas canvas) {
		// �жϵ�ǰ���ȣ����������ֵ���������ֵ
		if (mProgress > mMaxProgress) {
			mProgress = mMaxProgress;
		}
		// �жϵ�ǰ���ȣ���С��0������0
		if (mProgress < 0) {
			mProgress = 0;
		}
		// ���û�������
		mPaint.setColor(mSmallColor);
		mPaint.setStyle(Style.FILL);
		mPaint.setStrokeWidth(mSmallLineHeight);

		// ������ɵ�Բ+�����
		int mFinish = mProgress / (180 + mSmallLineWidth);
		if (mFinish > 0) {
			for (int i = 0; i < mFinish; i++) {
				// ��Բ
				canvas.drawCircle(getPaddingLeft() + (i * 2 + 1)
						* mBigCircleRadius + i * mBigLineWidth, mCircleY, mSmallCircleRadius, mPaint);
				// ����
				canvas.drawLine(getPaddingLeft() + (i * 2 + 1)
						* mBigCircleRadius + i * mBigLineWidth
						+ mSmallCircleRadius, mCircleY, getPaddingLeft() + (i * 2 + 1)
						* mBigCircleRadius + i * mBigLineWidth
						+ mSmallCircleRadius + mSmallLineWidth, mCircleY, mPaint);
			}
		}
		// ��ȥ����ɵ�Բ+����ϣ���ʣ����
		int mUnFinish = mProgress % (180 + mSmallLineWidth);
		// �������Բ
		if (mUnFinish <= 180) {
			// ������
			canvas.drawArc(new RectF(getPaddingLeft() + mFinish
					* (2 * mBigCircleRadius + mBigLineWidth) + mBigCircleRadius
					- mSmallCircleRadius, mCircleY
					- mSmallCircleRadius, getPaddingLeft() + mFinish
					* (2 * mBigCircleRadius + mBigLineWidth) + mBigCircleRadius
					+ mSmallCircleRadius, mCircleY
					+ mSmallCircleRadius), 180 - mUnFinish, 2 * mUnFinish,
					false, mPaint);
		}
		// ���������
		else {
			// ��Բ
			canvas.drawCircle(
					getPaddingLeft() + mFinish
							* (2 * mBigCircleRadius + mBigLineWidth)
							+ mBigCircleRadius, mCircleY, mSmallCircleRadius, mPaint);
			// ����
			canvas.drawLine(getPaddingLeft() + mFinish
					* (2 * mBigCircleRadius + mBigLineWidth) + mBigCircleRadius
					+ mSmallCircleRadius, mCircleY,
					getPaddingLeft() + mFinish
							* (2 * mBigCircleRadius + mBigLineWidth)
							+ mBigCircleRadius + mSmallCircleRadius + mUnFinish
							- 180, mCircleY, mPaint);
		}
	}

	// �����ı�����
	protected void drawText(Canvas canvas) {
		// �����ı�����
		mPaint.setColor(mTextColor);
		mPaint.setTextSize(mTextSize);
		if (mText != null) {
			for (int i = 1; i <= mText.length; i++) {
				float startWidth = getPaddingLeft() + i * 2 * mBigCircleRadius
						- mBigCircleRadius + (i - 1) * mBigLineWidth
						- mPaint.measureText(mText[i - 1]) / 2;
				float startHeight = mTextY;
				canvas.drawText(mText[i - 1], startWidth, startHeight, mPaint);
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getY() > getPaddingTop()
				&& event.getY() < getMeasuredHeight() - getPaddingBottom()) {
			
			switch (event.getAction()) {
			// �����¼��Լ��ƶ��¼�
			case MotionEvent.ACTION_MOVE:
			case MotionEvent.ACTION_DOWN:

				onActionDown(event);

				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				onActionUp(event);
				invalidate();
				break;
			}
			
		}
		else
		{
			mTouchCircle = -1;
			invalidate();
		}
		return true;
	}

	// ACTION_DOWN��������
	private void onActionDown(MotionEvent event) {
		float actionX = event.getX();
		float cache = (actionX - getPaddingLeft())
				% (2 * mBigCircleRadius + mBigLineWidth);
		if (cache <= 2 * mBigCircleRadius) {
			mTouchCircle = (int) (actionX - getPaddingLeft())
					/ (2 * mBigCircleRadius + mBigLineWidth);
		} else {
			mTouchCircle = -1;
		}
	}

	// ACTION_UP��������
	private void onActionUp(MotionEvent event) {
		float actionX = event.getX();
		float cache = (actionX - getPaddingLeft())
				% (2 * mBigCircleRadius + mBigLineWidth);
		int num = (int) (actionX - getPaddingLeft())
				/ (2 * mBigCircleRadius + mBigLineWidth);
		if (cache <= 2 * mBigCircleRadius) {
			if(onTouchCircleListener != null)
			onTouchCircleListener
					.onTouchCircle(num);
			mToProgress = (num+1)*180+num*mSmallLineWidth;
			
			for(int i = 0 ; i <= mLoadingRate ; i++)
			startLoading();
		}
		mTouchCircle = -1;
	}

	// dp to px
	protected int dp2px(int dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpVal, getResources().getDisplayMetrics());
	}

	// sp to px
	protected int sp2px(int spVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				spVal, getResources().getDisplayMetrics());
	}

	public interface OnTouchCircleListener {
		public void onTouchCircle(int mCircleNum);
	}
	
	
	private Handler mHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			//�����������
			if(mProgress == mToProgress)
			{
				return;
			}
			switch(msg.what)
			{
			case 0:
			case 1:
				mProgress ++;
				invalidate();
				startLoading();
				break;
			case 2:
			case 3:
				mProgress--;
				invalidate();
				startLoading();
				break;
			}
			super.handleMessage(msg);
		}
		
	};
	
	private void startLoading()
	{
		//���Ҽ��ض���
		if(mProgress < mToProgress)
		{
			//ԲȦ��
			if(mProgress%(180+ mSmallLineWidth)<=180)
			{
				mHandler.sendEmptyMessageDelayed(0, 0);
			}
			//�߶���
			else
			{
				mHandler.sendEmptyMessageDelayed(1, 0);
			}
		}
		//�������
		else if (mProgress > mToProgress)
		{
			//ԲȦ��
			if(mProgress%(180+ mSmallLineWidth)<=180)
			{
				mHandler.sendEmptyMessageDelayed(2, 0);
			}
			//�߶���
			else
			{
				mHandler.sendEmptyMessageDelayed(3, 0);
			}
		}
	}
	

}
