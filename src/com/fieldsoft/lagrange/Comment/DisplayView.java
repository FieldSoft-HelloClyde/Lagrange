package com.fieldsoft.lagrange.Comment;

import java.util.ArrayList;

import com.fieldsoft.lagrange.Fxinterface;
import com.fieldsoft.lagrange.MyPoint;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class DisplayView extends View {
	Fxinterface mCalFun;
	ArrayList<MyPoint> mData;
	MyPoint CalPoint;
	int X_pos;
	int Y_pos;
	int Mouse_Old_X;
	int Mouse_Old_Y;
	double PixRate = 1.0;
	boolean MoveState = false;
	Paint xyPaint = new Paint();
	Paint LnPaint = new Paint();
	Paint DataPaint = new Paint();
	Paint CalPaint = new Paint();

	// 触屏所用
	double StartLength;
	double EndLength;
	//2只手指中点到原点距离
	double divX;
	double divY;
	double OldPixRate;
	int TouchMode;

	public DisplayView(Context context) {
		super(context);
		init();
	}

	public DisplayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DisplayView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	void init() {
		this.X_pos = this.getWidth() / 2;
		this.Y_pos = this.getHeight() / 2;
		this.mCalFun = null;
		this.mData = null;
		this.xyPaint.setColor(Color.BLACK);
		this.LnPaint.setColor(Color.BLUE);
		this.DataPaint.setColor(Color.RED);
		this.CalPaint.setColor(Color.MAGENTA);
	}

	public void SetFx(Fxinterface Fx) {
		this.mCalFun = Fx;
		this.invalidate();
	}

	public void SetData(ArrayList<MyPoint> mData) {
		this.mData = new ArrayList<MyPoint>(mData);
		this.invalidate();
	}
	
	private int GetTextWidth(double num){
		String TempStr = String.valueOf(num);
		Rect TempRect = new Rect();  
		this.xyPaint.getTextBounds(TempStr, 0, TempStr.length(), TempRect);
		return TempRect.width();
	}
	
	private double ScreenToX(double src){
		return (double)(src - X_pos) / PixRate;
	}
	
	private double ScreenToY(double src){
		return (double)(Y_pos - src) / PixRate;
	}
	
	private double XToScreen(double src){
		return (int) (X_pos + src * PixRate);
	}
	
	private double YToScreen(double src){
		return (int) (Y_pos - src * PixRate);
	}
	
	private int GetTextHeight(double num){
		String TempStr = String.valueOf(num);
		Rect TempRect = new Rect();  
		this.xyPaint.getTextBounds(TempStr, 0, TempStr.length(), TempRect);
		return TempRect.height();
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//canvas.drawColor(Color.WHITE);
		// 画X轴
		canvas.drawLine(0, Y_pos, this.getWidth(), Y_pos, this.xyPaint);
		// 画Y轴
		canvas.drawLine(X_pos, 0, X_pos, this.getHeight(), this.xyPaint);
		
		//设定比例
		double EvP = this.getWidth() / (12*1.5);
		int EvN = (int) (EvP / this.PixRate);
		if (EvN == 0){
			EvN = 1;
		}
		//绘制X轴数字
		double LeftN = this.ScreenToX(0);
		double RightN = this.ScreenToX(this.getWidth() - 1);
		int StartXN;
		for (StartXN = (int) LeftN;StartXN <= RightN;StartXN ++){
			if (StartXN % EvN == 0){
				break;
			}
		}
		for (int i = StartXN;i <= RightN;i += EvN){
			int ScreenX = (int) this.XToScreen(i);
			canvas.drawLine(ScreenX, Y_pos, ScreenX, Y_pos - 1, this.xyPaint);
			canvas.drawText(String.valueOf(i), ScreenX, Y_pos + 10, xyPaint);
		}
		//绘制Y轴数字
		double UpN = this.ScreenToY(0);
		double DownN = this.ScreenToY(this.getHeight() - 1);
		int StartYN;
		for (StartYN = (int) DownN;StartYN <= UpN;StartYN ++){
			if (StartYN % EvN == 0){
				break;
			}
		}
		for (int i = StartYN;i <= UpN;i += EvN){
			if (i != 0){
				int ScreenY = (int) this.YToScreen(i);
				canvas.drawLine(X_pos, ScreenY, X_pos - 1, ScreenY, this.xyPaint);
				canvas.drawText(String.valueOf(i), X_pos, ScreenY, xyPaint);
			}
		}
		
		// 画L(n)
		if (this.mCalFun != null) {
			//Toast.makeText(this.getContext(),this.mCalFun.toString(), Toast.LENGTH_LONG).show();
			MyPoint oldPoint = null;
			for (int xx = 0; xx < this.getWidth(); xx++) {
				double x1, y1;
				x1 = ((double) (xx - X_pos)) / PixRate;
				y1 = this.mCalFun.Fx(x1);
				int yy;
				yy = (int) (Y_pos - y1 * PixRate);
				if (oldPoint != null){
					canvas.drawLine((float)oldPoint.x, (float)oldPoint.y, (float)xx, (float)yy, this.LnPaint);
				}
				oldPoint = new MyPoint(xx,yy);
			}
		}
		// 画实际数据
		if (this.mData != null) {
			for (int i = 0; i < this.mData.size(); i++) {
				int xx;
				xx = (int) (X_pos + this.mData.get(i).x * PixRate);
				int yy;
				yy = (int) (Y_pos - this.mData.get(i).y * PixRate);
				canvas.drawCircle(xx, yy, 5, this.DataPaint);
			}
		}
		//画计算点
		if (this.CalPoint != null){
			canvas.drawCircle((float)this.XToScreen(this.CalPoint.x), (float)this.YToScreen(this.CalPoint.y), 5, this.CalPaint);
			//在计算点附近绘制计算点信息
			canvas.drawText("x:" + this.CalPoint.x, (float)this.XToScreen(this.CalPoint.x) + 10, (float)this.YToScreen(this.CalPoint.y) - 10, this.CalPaint);
			canvas.drawText("y:" + this.CalPoint.y, (float)this.XToScreen(this.CalPoint.x) + 10, (float)this.YToScreen(this.CalPoint.y), this.CalPaint);
		}
		
		//画参数
		canvas.drawText("X_pos:" + String.valueOf(this.X_pos), 10, 10, this.xyPaint);
		canvas.drawText("Y_pos:" + String.valueOf(this.Y_pos), 10, 20, this.xyPaint);
		canvas.drawText("PixRate:" + String.valueOf(this.PixRate), 10, 30, this.xyPaint);
		canvas.drawText("EvN:" + String.valueOf(EvN), 10, 40, this.xyPaint);
		
		
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event){
		int PointerCount = event.getPointerCount();
		
		if (PointerCount == 2){//2根手指
			if (event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){
				this.TouchMode = 2;
				double sx = event.getX(0) - event.getX(1);
				double sy = event.getY(0) - event.getY(1);
				this.StartLength = Math.sqrt(sx * sx + sy * sy);
				if (this.StartLength == 0){
					this.StartLength = 1;
				}
				this.OldPixRate = this.PixRate;
				double mx = (event.getX(0) + event.getX(1)) / 2;
				double my = (event.getY(0) + event.getY(1)) / 2;
				this.divX = mx - this.X_pos;
				this.divY = my - this.Y_pos;
				//Toast.makeText(this.getContext(),"my:" + my + "\n" + "divY:" + this.divY, Toast.LENGTH_LONG).show();
			}
			else if (event.getActionMasked() == MotionEvent.ACTION_MOVE){
				double sx = event.getX(0) - event.getX(1);
				double sy = event.getY(0) - event.getY(1);
				this.EndLength = Math.sqrt(sx * sx + sy * sy);
				this.PixRate = this.OldPixRate * this.EndLength / this.StartLength;
				//重新计算X_pos和Y_pos
				double mx = (event.getX(0) + event.getX(1)) / 2;
				double my = (event.getY(0) + event.getY(1)) / 2;
				this.X_pos = (int) (mx - this.divX * this.EndLength / this.StartLength);
				this.Y_pos = (int) (my - this.divY * this.EndLength / this.StartLength);
			}
			else if (event.getActionMasked() == MotionEvent.ACTION_POINTER_UP){
				this.TouchMode = 0;
			}
		}
		else if (PointerCount == 1){//单根手指
			if (event.getAction() == MotionEvent.ACTION_DOWN){
				this.TouchMode = 1;
				MoveState = true;
				Mouse_Old_X = (int) event.getX();
				Mouse_Old_Y = (int) event.getY();
			}
			else if (event.getAction() == MotionEvent.ACTION_UP){
				MoveState = false;
				this.TouchMode = 0;
			}
			else if (event.getAction() == MotionEvent.ACTION_MOVE){
				if (MoveState && this.TouchMode == 1){
					int dx,dy;
					dx = (int) (event.getX() - Mouse_Old_X);
					dy = (int) (event.getY() - Mouse_Old_Y);
					X_pos += dx;
					Y_pos += dy;
					Mouse_Old_X = (int) event.getX();
					Mouse_Old_Y = (int) event.getY();
				}
			}
		}
		this.invalidate();
		return true;
	}

	@Override
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		System.out.println("onSizeChanged");
		if (oldw == 0) {
			this.X_pos = w / 2;
		} else {
			double Xdis = this.X_pos / oldw;
			this.X_pos = (int) (w * Xdis);
		}
		if (oldh == 0) {
			this.Y_pos = h / 2;
		} else {
			double Ydis = this.Y_pos / oldh;
			this.Y_pos = (int) (h * Ydis);
		}
	}

	public ArrayList<MyPoint> getData() {
		return mData;
	}

	public Fxinterface getmCalFun() {
		return mCalFun;
	}

	public MyPoint getCalPoint() {
		return CalPoint;
	}

	public void setCalPoint(MyPoint calPoint) {
		CalPoint = calPoint;
	}

	
	
}
