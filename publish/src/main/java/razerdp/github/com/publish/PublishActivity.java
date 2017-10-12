package razerdp.github.com.publish;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

import java.util.List;

import razerdp.github.com.baselibrary.imageloader.ImageLoadMnanger;
import razerdp.github.com.baselibrary.interfaces.adapter.TextWatcherAdapter;
import razerdp.github.com.baselibrary.utils.ui.SwitchActivityTransitionUtil;
import razerdp.github.com.baselibrary.utils.ui.UIHelper;
import razerdp.github.com.baselibrary.utils.ui.ViewUtil;
import razerdp.github.com.baseuilib.base.BaseTitleBarActivity;
import razerdp.github.com.baseuilib.widget.common.TitleBar;
import razerdp.github.com.baseuilib.widget.imageview.PreviewImageView;
import razerdp.github.com.models.localphotomanager.ImageInfo;
import razerdp.github.com.models.photo.PhotoBrowserInfo;
import razerdp.github.com.router.RouterList;


/**
 * Created by 大灯泡 on 2017/3/1.
 * <p>
 * 发布朋友圈页面
 */

@Route(path = "/publish/edit")
public class PublishActivity extends BaseTitleBarActivity {
    @Autowired(name = "mode")
    int mode = -1;

    private boolean canTitleRightClick = false;
    private List<ImageInfo> selectedPhotos;

    private EditText mInputContent;
    private PreviewImageView<ImageInfo> mPreviewImageView;


    @Override
    public void onHandleIntent(Intent intent) {
        mode = intent.getIntExtra(RouterList.PublishActivity.key_mode, -1);
        selectedPhotos = intent.getParcelableArrayListExtra(RouterList.PublishActivity.key_photoList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        if (mode == -1) {
            finish();
            return;
        } else if (mode == RouterList.PublishActivity.MODE_MULTI && selectedPhotos == null) {
            finish();
            return;
        }
        initView();
    }

    private void initView() {
        initTitle();
        mInputContent = findView(R.id.publish_input);
        if (mode == RouterList.PublishActivity.MODE_TEXT) {
            UIHelper.showInputMethod(mInputContent);
        }
        mPreviewImageView = findView(R.id.preview_image_content);
        ViewUtil.setViewsVisible(mode == RouterList.PublishActivity.MODE_TEXT ? View.GONE : View.VISIBLE, mPreviewImageView);
        mInputContent.setHint(mode == RouterList.PublishActivity.MODE_MULTI ? "这一刻的想法..." : null);
        mInputContent.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (mode == RouterList.PublishActivity.MODE_TEXT) {
                    setTitleRightTextColor(mInputContent.getText().toString().length() > 0);
                }
            }
        });
        loadImage();

    }

    private void loadImage() {
        mPreviewImageView.setDatas(selectedPhotos, new PreviewImageView.OnLoadPhotoListener<ImageInfo>() {
            @Override
            public void onPhotoLoading(int pos, ImageInfo data, @NonNull ImageView imageView) {
                ImageLoadMnanger.INSTANCE.loadImage(imageView, data.getImagePath());
            }
        });
        mPreviewImageView.setOnPhotoClickListener(new PreviewImageView.OnPhotoClickListener<ImageInfo>() {
            @Override
            public void onPhotoClickListener(int pos, ImageInfo data, @NonNull ImageView imageView) {
                PhotoBrowserInfo info = PhotoBrowserInfo.create(pos, null, selectedPhotos);
                ARouter.getInstance()
                       .build(RouterList.PhotoMultiBrowserActivity.path)
                       .withParcelable(RouterList.PhotoMultiBrowserActivity.key_browserinfo, info)
                       .withInt(RouterList.PhotoMultiBrowserActivity.key_maxSelectCount, selectedPhotos.size())
                       .navigation(PublishActivity.this, RouterList.PhotoMultiBrowserActivity.requestCode);
            }
        });
    }

    //title init
    private void initTitle() {
        setTitle(mode == RouterList.PublishActivity.MODE_TEXT ? "发表文字" : null);
        setTitleRightTextColor(mode != RouterList.PublishActivity.MODE_TEXT);
        setTitleMode(TitleBar.MODE_BOTH);
        setTitleLeftText("取消");
        setTitleLeftIcon(0);
        setTitleRightText("发送");
        setTitleRightIcon(0);
    }


    private void setTitleRightTextColor(boolean canClick) {
        setRightTextColor(canClick ? UIHelper.getResourceColor(R.color.wechat_green_bg) : UIHelper.getResourceColor(R.color.wechat_green_transparent));
        canTitleRightClick = canClick;
    }

    @Override
    public void onTitleRightClick() {
        if (!canTitleRightClick) return;
    }

    @Override
    public void finish() {
        super.finish();
        SwitchActivityTransitionUtil.transitionVerticalOnFinish(this);
    }
}
