package com.zhangjie.graduation.videopalyer;

import java.util.Timer;
import java.util.TimerTask;

import com.zhangjie.graduation.videopalyer.jni.VideoPlayerDecode;
import com.zhangjie.graduation.videopalyer.surface.VideoSurfaceView;
import com.zhangjie.graduation.videopalyer.videofile.Video;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

public class JieVideoPlayer extends Activity {

	VideoSurfaceView videoSurfaceView;
	Button stopButton, playButton;
	Handler handler;
	public JieVideoPlayer instance = null;
	Video playVideoFile;
	boolean isExit = false;
	
	String videoFileName = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN, WindowManager.LayoutParams. FLAG_FULLSCREEN);    
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_jie_video_player);
		instance = this;
		
		Bundle bundle = getIntent().getExtras();
		playVideoFile = (Video)bundle.getSerializable("video");
		videoFileName = playVideoFile.getPath();
		videoSurfaceView = (VideoSurfaceView)findViewById(R.id.videoPlay);
		
		stopButton = (Button)findViewById(R.id.stop);
		stopButton.getBackground().setAlpha(100);
		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isExit = true;
				instance.finish();
			}
		});
		
		playButton = (Button)findViewById(R.id.playe);
		playButton.getBackground().setAlpha(100);
		playButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (playOrStop == 1) {
					playButton.setText(R.string.video_play);
					playOrStop = 2;
				}else if(playOrStop == 2){
					playButton.setText(R.string.video_pause);
					playOrStop = 1;
				}
				VideoPlayerDecode.VideoPlayerPauseOrPlay(true);
			}
		});
		
//		handler = new Handler(){
//			@Override
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				switch (msg.what) {
//				case 1111:
//					//初始化解码器
//					//VideoPlayerDecode.VideoPlayer("/mnt/sdcard/test.mp4");
//					break;
//				}
//			}
//		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!isExit && playOrStop == 1) {
			Timer aTimer = new Timer();
			aTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					//初始化解码器
					VideoPlayerDecode.VideoPlayer(videoFileName);
				}
			}, 10);
		}
	}

	
	@Override
	protected void onStop() {
		if(!isExit){ //播放状态
			if (playOrStop == 1) {
				playOrStop = 2;
				playButton.setText(R.string.video_play);
				VideoPlayerDecode.VideoPlayerPauseOrPlay(true);
			}
		}else{
			try {
				if (playOrStop == 2) {
					playOrStop = 1;
					playButton.setText(R.string.video_pause);
					VideoPlayerDecode.VideoPlayerPauseOrPlay(true);
					Thread.sleep(50);
				}
				VideoPlayerDecode.VideoPlayerStop();
				Thread.sleep(550);
				sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"
						+ Environment.getExternalStorageDirectory()
								.getAbsolutePath())));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		super.onStop();
	}

	@Override
	public void onBackPressed() {
		isExit = true;
		instance.finish();
	}

	public int playOrStop = 1;
}
