package com.zhangsong.com.signup_system.uiRefresh.NarutoNinja;

/**
 * 刷新回调接口
 */
public interface PullToRefreshListener {
    /**
     * 刷新中。。。
     * @param refreshLayout
     */
    void onRefresh(RefreshLayout refreshLayout);
}
