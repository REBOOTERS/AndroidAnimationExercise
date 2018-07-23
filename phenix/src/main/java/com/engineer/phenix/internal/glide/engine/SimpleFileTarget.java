package com.engineer.phenix.internal.glide.engine;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;

import java.io.File;

public class SimpleFileTarget implements Target<File> {

	private static final String TAG = "SimpleFileTarget";

	@Override public void onLoadStarted(Drawable placeholder) {
		Log.d(TAG, "onLoadStarted");
	}

	@Override public void onLoadFailed(Exception e, Drawable errorDrawable) {
		if (e != null) {
			Log.d(TAG, "onLoadFailed e--->" + e.toString());
		} else {
			Log.d(TAG, "onLoadFailed");
		}
	}

	@Override public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
		Log.d(TAG, "onResourceReady " + resource.getAbsolutePath());
	}

	@Override public void onLoadCleared(Drawable placeholder) {

	}

	@Override public void getSize(SizeReadyCallback cb) {
		cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
	}

	@Override public void setRequest(Request request) {

	}

	@Override public Request getRequest() {
		return null;
	}

	@Override public void onStart() {

	}

	@Override public void onStop() {

	}

	@Override public void onDestroy() {

	}
}