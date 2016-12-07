package razerdp.friendcircle.mvp.model;

import cn.bmob.v3.exception.BmobException;
import razerdp.friendcircle.app.net.OnResponseListener;
import razerdp.friendcircle.app.net.request.AddLikeRequest;
import razerdp.friendcircle.config.Define;
import razerdp.friendcircle.mvp.callback.OnLikeChangeCallback;

/**
 * Created by 大灯泡 on 2016/12/7.
 * <p>
 * 点赞model
 */

public class LikeImpl implements ILike {

    @Override
    public void addLike(String momentid, final OnLikeChangeCallback onLikeChangeCallback) {
        if (onLikeChangeCallback == null) return;
        AddLikeRequest request= new AddLikeRequest(momentid);
        request.setOnResponseListener(new OnResponseListener<Boolean>() {
            @Override
            public void onStart(int requestType) {

            }

            @Override
            public void onSuccess(Boolean response, int requestType) {
                onLikeChangeCallback.onLikeChange(response ? Define.LikeState.LIKE : Define.LikeState.UNLIKE);
            }

            @Override
            public void onError(BmobException e, int requestType) {

            }
        });
        request.execute();
    }

    @Override
    public void unLike() {

    }
}
