package com.zhangjie.graduation.videopalyer.function;

import com.zhangjie.graduation.videopalyer.jni.VideoPlayerDecode;

public class JieVideoPlayerSurfaceFunction
{
	private static JieVideoPlayerSurfaceFunction appAppCameraSurfaceFunctionInstance = null;
	public JieVideoPlayerSurfaceFunction(){
		
	}
	public static JieVideoPlayerSurfaceFunction getAppCameraSurfaceFunctionInstance(){
		if (appAppCameraSurfaceFunctionInstance == null)
		{
			appAppCameraSurfaceFunctionInstance = new JieVideoPlayerSurfaceFunction();
		}
		return appAppCameraSurfaceFunctionInstance;
	}
	/*
	 * 初始化Opengl es 2.0配置
	 */
	public void VideoPlayerShow_GLCreate(){
		VideoPlayerDecode.GlCreate();
	}
	/*
	 * 初始化Opengl es 2.0 SurfaceChanged
	 */
	public void VideoPlayerShow_GLInit(int width, int height){
		VideoPlayerDecode.GlInit(width, height);
	}
	/*
	 * 将解码后的数据渲染到Opengl es 上面
	 */
	public void VideoPlayerShow_Render(){
		VideoPlayerDecode.GlRender();
	}
}
