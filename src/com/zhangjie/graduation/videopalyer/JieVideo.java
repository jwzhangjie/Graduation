package com.zhangjie.graduation.videopalyer;

import java.util.List;

import com.zhangjie.graduation.videopalyer.component.JieVideoListViewAdapter;
import com.zhangjie.graduation.videopalyer.component.LoadedImage;
import com.zhangjie.graduation.videopalyer.videofile.AbstructProvider;
import com.zhangjie.graduation.videopalyer.videofile.Video;
import com.zhangjie.graduation.videopalyer.videofile.VideoProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Video.Thumbnails;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class JieVideo extends Activity{

	public JieVideo instance = null;
	ListView mJieVideoListView;
	JieVideoListViewAdapter mJieVideoListViewAdapter;
	List<Video> listVideos;
	int videoSize;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jie_video);
		instance = this;
		AbstructProvider provider = new VideoProvider(instance);
        listVideos = provider.getList();
        videoSize = listVideos.size();
		mJieVideoListViewAdapter = new JieVideoListViewAdapter(this, listVideos);
		mJieVideoListView = (ListView)findViewById(R.id.jievideolistfile);
		mJieVideoListView.setAdapter(mJieVideoListViewAdapter);
		mJieVideoListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent();
				intent.setClass(JieVideo.this, JieVideoPlayer.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("video", listVideos.get(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		loadImages();
	}
	/**
	 * Load images.
	 */
	private void loadImages() {
		final Object data = getLastNonConfigurationInstance();
		if (data == null) {
			new LoadImagesFromSDCard().execute();
		} else {
			final LoadedImage[] photos = (LoadedImage[]) data;
			if (photos.length == 0) {
				new LoadImagesFromSDCard().execute();
			}
			for (LoadedImage photo : photos) {
				addImage(photo);
			}
		}
	}
	private void addImage(LoadedImage... value) {
		for (LoadedImage image : value) {
			mJieVideoListViewAdapter.addPhoto(image);
			mJieVideoListViewAdapter.notifyDataSetChanged();
		}
	}
	@Override
	public Object onRetainNonConfigurationInstance() {
		final ListView grid = mJieVideoListView;
		final int count = grid.getChildCount();
		final LoadedImage[] list = new LoadedImage[count];

		for (int i = 0; i < count; i++) {
			final ImageView v = (ImageView) grid.getChildAt(i);
			list[i] = new LoadedImage(
					((BitmapDrawable) v.getDrawable()).getBitmap());
		}

		return list;
	}
	/** 
	    * 获取视频缩略图 
	    * @param videoPath 
	    * @param width 
	    * @param height 
	    * @param kind 
	    * @return 
	    */  
	   private Bitmap getVideoThumbnail(String videoPath, int width , int height, int kind){  
	    Bitmap bitmap = null;  
	    bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);  
	    bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);  
	    return bitmap;  
	   }  

	class LoadImagesFromSDCard extends AsyncTask<Object, LoadedImage, Object> {
		@Override
		protected Object doInBackground(Object... params) {
			Bitmap bitmap = null;
			for (int i = 0; i < videoSize; i++) {
				bitmap = getVideoThumbnail(listVideos.get(i).getPath(), 120, 120, Thumbnails.MINI_KIND);
				if (bitmap != null) {
					publishProgress(new LoadedImage(bitmap));
				}
			}
			return null;
		}
		
		@Override
		public void onProgressUpdate(LoadedImage... value) {
			addImage(value);
		}
	}

	@Override
	protected void onDestroy() {
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath())));
		super.onDestroy();
	}
	
}
