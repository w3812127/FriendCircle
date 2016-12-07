package razerdp.friendcircle.mvp.presenter;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import razerdp.friendcircle.app.manager.LocalHostManager;
import razerdp.friendcircle.config.Define;
import razerdp.friendcircle.mvp.callback.OnLikeChangeCallback;
import razerdp.friendcircle.mvp.model.CommentImpl;
import razerdp.friendcircle.mvp.model.LikeImpl;
import razerdp.friendcircle.mvp.model.entity.UserInfo;
import razerdp.friendcircle.mvp.view.IMomentView;
import razerdp.friendcircle.utils.ToolUtil;

/**
 * Created by 大灯泡 on 2016/12/7.
 * <p>
 * 朋友圈presenter
 */

public class MomentPresenter implements IMomentPresenter {
    private IMomentView momentView;
    private CommentImpl commentModel;
    private LikeImpl likeModel;

    public MomentPresenter() {
        this(null);
    }

    public MomentPresenter(IMomentView momentView) {
        this.momentView = momentView;
        commentModel = new CommentImpl();
        likeModel = new LikeImpl();
    }

    @Override
    public IBasePresenter<IMomentView> bindView(IMomentView view) {
        this.momentView = view;
        return this;
    }

    @Override
    public IBasePresenter<IMomentView> unbindView() {
        return this;
    }

    @Override
    public void addLike(final int viewHolderPos, String momentid, final List<UserInfo> currentLikeUserList) {
        likeModel.addLike(momentid, new OnLikeChangeCallback() {
            @Override
            public void onLikeChange(@Define.LikeState int likeStae) {
                List<UserInfo> resultLikeList = new ArrayList<UserInfo>();
                if (!ToolUtil.isListEmpty(currentLikeUserList)) {
                    resultLikeList.addAll(currentLikeUserList);
                }
                boolean hasLocalLiked = false;
                int localHostPos = -1;
                for (int i = 0; i < resultLikeList.size(); i++) {
                    UserInfo userinfo = resultLikeList.get(i);
                    if (TextUtils.equals(userinfo.getUserid(), LocalHostManager.INSTANCE.getUserid())) {
                        hasLocalLiked = true;
                        localHostPos = i;
                        break;
                    }

                }
                if (likeStae == Define.LikeState.LIKE) {
                    if (!hasLocalLiked) {
                        resultLikeList.add(0, LocalHostManager.INSTANCE.getLocalHostUser());
                    }
                } else {
                    if (hasLocalLiked) {
                        resultLikeList.remove(localHostPos);
                    }
                }
                if (momentView != null) {
                    momentView.onLikeChange(viewHolderPos, resultLikeList);
                }
            }
        });
    }

    @Override
    public void unLike(int viewHolderPos, String momentid, List<UserInfo> currentLikeUserList) {

    }


    //------------------------------------------interface impl-----------------------------------------------
}
