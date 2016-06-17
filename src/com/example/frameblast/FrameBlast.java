package com.example.frameblast;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class FrameBlast extends Activity {
	
	private MyView myView;
	private AnimationDrawable anim;
	private MediaPlayer bomb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //使用FrameLayout布局管理器，它允许组件自己控制位置
        FrameLayout frame = new FrameLayout(this);
        setContentView(R.layout.main);
        
        frame.setBackgroundResource(R.drawable.black);  //设置使用背景
        bomb = MediaPlayer.create(this, R.raw.bomb); //加载音效
        myView = new MyView(this);
        myView.setBackgroundResource(R.anim.blast);  //设置myView用于显示blast动画
        myView.setVisibility(View.INVISIBLE); //设置myView默认为隐藏
        
        anim = (AnimationDrawable) myView.getBackground(); //获取动画对象
        frame.addView(myView);
        frame.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View source, MotionEvent event) {
				// TODO Auto-generated method stub
				//只处理按下事件 （避免每次产生两个动画效果）
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					anim.stop(); //先停止动画播放
					float x = event.getX();
					float y = event.getY();
					//控制myView 的显示位置
					myView.setLocation((int)y - 40, (int)x - 20);
					myView.setVisibility(View.VISIBLE);
					anim.start();  //启动动画
					bomb.start();  //播放音效
				}
				return false;
			}
		});
        
    }
    
    //指定义View，该自定义View用于播放“爆炸”效果
    class MyView extends ImageView
    {
		public MyView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
    	
		//定义一个方法，该方法用于控制MyView的显示位置
		public void setLocation(int top, int left)
		{
			this.setFrame(left, top, left + 40, top + 40);
		}
		
		//重写该方法，控制 如果动画播放到最后一帧时，就隐藏该View
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			try {
				Field field = AnimationDrawable.class.getDeclaredField("mCurFrame");
				field.setAccessible(true);
				int currentFrame = field.getInt(anim); //获取anim动画的当前帧
				//如果到了最后还一帧
				if (currentFrame == anim.getNumberOfFrames() - 1)
				{
					setVisibility(View.INVISIBLE);  //让该View隐藏
				}
			} 
			catch (Exception e) {
				// TODO: handle exception
			}
			
			super.onDraw(canvas);
		}
    }
}
