package com.zhangjie.graduation.videopalyer.jni;

public class VideoPlayerDecode {
	public static native int Getcpu();
	public static native int VideoPlayer(String fileaName);
	public static native int VideoPlayerPauseOrPlay(boolean opt);//ture -- play false -- pause
	public static native int VideoPlayerStop();
	public static native int VideoPlayerChangeVideo(String fileName);//	public static native int GlCreate();
	public static native int GlCreate();
	public static native int GlInit(int width, int height);
	public static native void GlRender();
	
	static{
//		System.loadLibrary("SDL");
		System.loadLibrary("ffmpeg-neon");
		System.loadLibrary("ffmpeg-test-neon");
	}
}
