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
        //ʹ��FrameLayout���ֹ�����������������Լ�����λ��
        FrameLayout frame = new FrameLayout(this);
        setContentView(R.layout.main);
        
        frame.setBackgroundResource(R.drawable.black);  //����ʹ�ñ���
        bomb = MediaPlayer.create(this, R.raw.bomb); //������Ч
        myView = new MyView(this);
        myView.setBackgroundResource(R.anim.blast);  //����myView������ʾblast����
        myView.setVisibility(View.INVISIBLE); //����myViewĬ��Ϊ����
        
        anim = (AnimationDrawable) myView.getBackground(); //��ȡ��������
        frame.addView(myView);
        frame.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View source, MotionEvent event) {
				// TODO Auto-generated method stub
				//ֻ�������¼� ������ÿ�β�����������Ч����
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					anim.stop(); //��ֹͣ��������
					float x = event.getX();
					float y = event.getY();
					//����myView ����ʾλ��
					myView.setLocation((int)y - 40, (int)x - 20);
					myView.setVisibility(View.VISIBLE);
					anim.start();  //��������
					bomb.start();  //������Ч
				}
				return false;
			}
		});
        
    }
    
    //ָ����View�����Զ���View���ڲ��š���ը��Ч��
    class MyView extends ImageView
    {
		public MyView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
    	
		//����һ���������÷������ڿ���MyView����ʾλ��
		public void setLocation(int top, int left)
		{
			this.setFrame(left, top, left + 40, top + 40);
		}
		
		//��д�÷��������� ����������ŵ����һ֡ʱ�������ظ�View
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			try {
				Field field = AnimationDrawable.class.getDeclaredField("mCurFrame");
				field.setAccessible(true);
				int currentFrame = field.getInt(anim); //��ȡanim�����ĵ�ǰ֡
				//����������һ֡
				if (currentFrame == anim.getNumberOfFrames() - 1)
				{
					setVisibility(View.INVISIBLE);  //�ø�View����
				}
			} 
			catch (Exception e) {
				// TODO: handle exception
			}
			
			super.onDraw(canvas);
		}
    }
}
