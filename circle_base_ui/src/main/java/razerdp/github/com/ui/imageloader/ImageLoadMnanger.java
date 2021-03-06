package razerdp.github.com.ui.imageloader;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.Glide;

import razerdp.github.com.baseuilib.R;
import razerdp.github.com.lib.api.AppContext;


/**
 * Created by 大灯泡 on 2016/11/1.
 * <p>
 * 图片加载
 */

public enum ImageLoadMnanger {
    INSTANCE;

    static final String DRAWABLE_PREFIX = "drawable://";

    public void clearMemory(Context context) {
        Glide.get(context).clearMemory();
    }

    public void loadImage(ImageView imageView, String imgUrl) {
        loadImageByNormalConfig(imageView, imgUrl).placeholder(R.drawable.image_nophoto).into(imageView);
    }

    public void loadImage(ImageView imageView, int placeHolderColor, String imgUrl) {
        loadImageByNormalConfig(imageView, imgUrl).placeholder(new ColorDrawable(placeHolderColor)).into(imageView);
    }

    public void loadImageDontAnimate(ImageView imageView, String imgUrl) {
        loadImageByNormalConfig(imageView, imgUrl).dontAnimate()
                .into(imageView);
    }

    public void loadImage(ImageView imageView, String imgUrl, int width, int height) {
        loadImageByNormalConfig(imageView, imgUrl).placeholder(R.drawable.image_nophoto)
                .override(width, height)
                .into(imageView);
    }


    private BitmapRequestBuilder loadImageByNormalConfig(ImageView imageView, String url) {
        int resid = 0;
        try {
            resid = Integer.valueOf(url);
        } catch (NumberFormatException e) {
            Log.w("ImageLoadMnanger", "loadImageByNormalConfig: not a resource id");
        }
        return Glide.with(getImageContext(imageView)).load(resid != 0 ? resid : url).asBitmap();
    }

    private Context getImageContext(@Nullable ImageView imageView) {
        if (imageView == null) return AppContext.getAppContext();
        return imageView.getContext();
    }
}
