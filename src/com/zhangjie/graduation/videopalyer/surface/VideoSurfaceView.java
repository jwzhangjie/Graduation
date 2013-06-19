package com.zhangjie.graduation.videopalyer.surface;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.zhangjie.graduation.videopalyer.function.JieVideoPlayerSurfaceFunction;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
public class VideoSurfaceView extends GLSurfaceView implements Renderer{

	static Handler handler;
	public VideoSurfaceView(Context context) {
		super(context);
		initial();
	}
	
	public VideoSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initial();
	}

	/**
	 * 初始化底层Opengl es 与上层的连接
	 */
	public void initial() {
		setEGLContextClientVersion(2);
		setRenderer(this);
		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 112:
					requestRender();
					break;

				default:
					break;
				}
			}
			
		};
		
	}
	
	/**
	 * 请求渲染视频
	 * Request render video
	 */
	public static void DrawVideoPlay(){
		handler.sendEmptyMessage(112);
	}
	
	/**
	 * 实现渲染画面的函数
	 * When you call render, will implement this function
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		JieVideoPlayerSurfaceFunction.getAppCameraSurfaceFunctionInstance().VideoPlayerShow_Render();
	}
	
	/**
	 * 当画面大小改变的时候，会调用此函数
	 * When SurfaceView change size, to implement this function
	 */
	public boolean isFirstChange = true;
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (isFirstChange) {
			JieVideoPlayerSurfaceFunction.getAppCameraSurfaceFunctionInstance().VideoPlayerShow_GLInit(width, height);
			isFirstChange = false;
		}
	}
	
	/**
	 * 当画面第一次创建的时候，会调用此函数
	 * When SurfaceView when first created , to achieve this function
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		JieVideoPlayerSurfaceFunction.getAppCameraSurfaceFunctionInstance().VideoPlayerShow_GLCreate();
	}

}
