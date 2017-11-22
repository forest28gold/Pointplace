package com.phoobs.pointplace2.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

public class ResizableImageView extends ImageView {

    private Bitmap mBitmap;

    // Constructor

    public ResizableImageView(Context context) {
        super(context);
    }

    // Overriden methods
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mBitmap != null) {
			int width = MeasureSpec.getSize(widthMeasureSpec);
			int height = width * mBitmap.getHeight() / mBitmap.getWidth();
			setMeasuredDimension(width, height);

		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	public void setImageBitmap(Bitmap bitmap) {
		mBitmap = bitmap;
		super.setImageBitmap(bitmap);
	}

}