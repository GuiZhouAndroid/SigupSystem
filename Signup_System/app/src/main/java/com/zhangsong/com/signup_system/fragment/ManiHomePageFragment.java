package com.zhangsong.com.signup_system.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.activity.InSchoolActivity;
import com.zhangsong.com.signup_system.activity.RegisterActivity;
import com.zhangsong.com.signup_system.activity.SeeMoreActivity;
import com.zhangsong.com.signup_system.activity.SignEncyActivity.Title1Activity;
import com.zhangsong.com.signup_system.activity.SignEncyActivity.Title2Activity;
import com.zhangsong.com.signup_system.activity.SignEncyActivity.Title3Acitvity;
import com.zhangsong.com.signup_system.activity.SignEncyActivity.Title4Acitvity;
import com.zhangsong.com.signup_system.activity.SignQueryActivity;
import com.zhangsong.com.signup_system.adapter.FragmentPagetAdapter.CollectRecycleAdapter;
import com.zhangsong.com.signup_system.adapter.FragmentPagetAdapter.NoticeEntity;
import com.zhangsong.com.signup_system.utils.Open_Map.AddressInfo;
import com.zhangsong.com.signup_system.utils.Open_Map.BottomSheetPop;
import com.zhangsong.com.signup_system.utils.ui.FlowLayout.AutoFlowLayout;
import com.zhangsong.com.signup_system.utils.ui.FlowLayout.FlowAdapter;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGALocalImageSize;

public class ManiHomePageFragment extends Fragment implements View.OnClickListener {
    //图片轮播
    private BGABanner banner_guide_background;
    //定义view用来设置fragment的layout
    private View view;
    //定义RecyclerView
    public RecyclerView recyclerView;
    //定义以goodsentity实体类为对象的数据集合
    private ArrayList<NoticeEntity> entities = new ArrayList<>();
    //自定义recyclerveiw的适配器
    private CollectRecycleAdapter mCollectRecyclerAdapter;
    //流式布局
    private AutoFlowLayout auto_flow_layout;
    private LayoutInflater LayoutInflater_flow_layout;
    private static final String[] Flow_Tv_Data = new String[]{"office2010","ASP.NET","PhotoShopCS5", "AutoCAD2010(建筑)",
            "AutoCAD2005(建筑)", "AutoCAD2010(机械)"};
    //定制网格布局
    private AutoFlowLayout auto_grid_layout;
    private LayoutInflater LayoutInflater_grid_layout;
    private static final String[] Grid_Tv_Data = new String[]{"通知公告","进入官网", "报名查询", "位置服务"};
    private static final int[] Grid_Iv_Data =new int[]{R.mipmap.notice,R.mipmap.schoolnetwork,R.mipmap.signquery,R.mipmap.mani_map};

    private BottomSheetPop mBottomSheetPop;
    private View openbottomtview;//3个地图v底部弹窗
    private Button btn_baidu,btn_gaode,btn_tencent,btn_cancel;//百度+高德+腾讯+取消
    AddressInfo mInfo = new AddressInfo();
    //报名百科数据 标题
    private static final String[] signEncyTitleData = new String[]{"计算机高新技术考试概述","开展计算机信息高新技术考试意义",
            "全国计算机信息高新技术考试性质","全国计算机高新技术考试特点"};
    //报名百科数据内容概况
    private static final String[] signEncyContentData = new String[]{"全国计算机信息高新技术...","全国计算机高新技术考试，是...","一、考试名称 劳动和社..."
            ,"全国计算机信息高新技术考试标..."};
    //报名百科数据发布时间
    private static final String[] signEncySourceData = new String[]{"来源：CITT工作网","来源：CITT工作网","来源：CITT工作网","来源：CITT工作网"};
    //报名百科数据发布时间
    private static final String[] signEncyTimeData = new String[]{"时间：2015-06-09","时间：2015-06-09","时间：2015-06-09","时间：2015-06-09"};
    private TextView tv_see_more;//查看更多
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homepage,container,false);
        initView();//初始化View
        ImagePlay();//图片轮播
        FlowLayout();//流式标签布局
        GridLayout();//网格布局
        setRecyclerView();//报名百科
        return view;
    }
    /**
     * 初始化View
     */
    private void initView() {
        banner_guide_background = view.findViewById(R.id.banner); //图片轮播
        //流式标签布局
        LayoutInflater_flow_layout = LayoutInflater.from(getActivity());//获取实例--
        auto_flow_layout= view.findViewById(R.id.auto_flow_layout);//获取XML的id
        //获取RecyclerView
        recyclerView= view.findViewById(R.id.recyclerView);
        //网格布局
        LayoutInflater_grid_layout = LayoutInflater.from(getActivity());//获取实例--
        auto_grid_layout= view.findViewById(R.id.auto_grid_layout);//获取XML的id
        //3个地图id
        openbottomtview=LayoutInflater.from(getActivity()).inflate(R.layout.open_map_bottom_navagation,null);
        btn_baidu=openbottomtview.findViewById(R.id.btn_baidu);//底部百度
        btn_gaode=openbottomtview.findViewById(R.id.btn_gaode);//底部高德
        btn_tencent=openbottomtview.findViewById(R.id.btn_tencent);//底部腾讯
        btn_cancel=openbottomtview.findViewById(R.id.btn_cancel);//取消
        btn_baidu.setOnClickListener(this);
        btn_gaode.setOnClickListener(this);
        btn_tencent.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        //设置你目的地的经纬度
        mInfo.setLat(26.3050200000);
        mInfo.setLng(107.4562900000);
        tv_see_more=view.findViewById(R.id.tv_see_more);
        tv_see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SeeMoreActivity.class));
            }
        });
    }
    /**
     * 图片轮播
     */
    private void ImagePlay() {
        BGALocalImageSize bgaLocalImageSize=new BGALocalImageSize(720,1280,320,640);
        banner_guide_background.setData(bgaLocalImageSize, ImageView.ScaleType.CENTER_CROP,
                R.drawable.uoko_guide_background_1,
                R.drawable.uoko_guide_background_2,
                R.drawable.uoko_guide_background_3);
    }
    /**
     * 流式标签布局
     */
    private void FlowLayout() {
        auto_flow_layout.setLineCenter(true);//布局居中
        auto_flow_layout.setAdapter(new FlowAdapter(Arrays.asList(Flow_Tv_Data)) {
            @Override
            public View getView(int position) {
                View item = LayoutInflater_flow_layout.inflate(R.layout.item_fragment_homepage_flow, null);//加载指定的布局
                TextView tv_flow_tag = item.findViewById(R.id.tv_flow_tag);
                tv_flow_tag.setText(Flow_Tv_Data[position]);//设置自定义样式的TextView
                return item;
            }
        });
        auto_flow_layout.setOnItemClickListener(new AutoFlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                switch (position){
                    case 0://office2010
                        Toast.makeText(getActivity(), "office2010,待实现其他逻辑", Toast.LENGTH_SHORT).show();
                        break;
                    case 1://ASP.NET
                        Toast.makeText(getActivity(), "ASP.NET,待实现其他逻辑", Toast.LENGTH_SHORT).show();
                        break;
                    case 2://PhotoShopCS5
                        Toast.makeText(getActivity(), "PhotoShopCS5,待实现其他逻辑", Toast.LENGTH_SHORT).show();
                        break;
                    case 3://AutoCAD2010(建筑)
                        Toast.makeText(getActivity(), "AutoCAD2010(建筑),待实现其他逻辑", Toast.LENGTH_SHORT).show();
                        break;
                    case 4://AutoCAD2005(建筑)
                        Toast.makeText(getActivity(), "AutoCAD2005(建筑),待实现其他逻辑", Toast.LENGTH_SHORT).show();
                        break;
                    case 5://AutoCAD2010(机械)
                        Toast.makeText(getActivity(), "AutoCAD2010(机械),待实现其他逻辑", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
    /**
     * 网格布局
     */
    private void GridLayout() {
        auto_grid_layout.setAdapter(new FlowAdapter(Grid_Tv_Data) {
            @Override
            public View getView(int position) {
                View item = LayoutInflater_grid_layout.inflate(R.layout.item_fragment_homepage_grid, null);//加载指定的布局
                TextView tv_gird_tab = item.findViewById(R.id.tv_gird_tab);
                tv_gird_tab.setText(Grid_Tv_Data[position]);
                ImageView iv_grid_show=item.findViewById(R.id.iv_grid_show);
                iv_grid_show.setImageResource(Grid_Iv_Data[position]);
                return item;
            }
        });
        /**
         * 网格布局设置Item监听
         */
        auto_grid_layout.setOnItemClickListener(new AutoFlowLayout.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                switch (position){
                    case 0://通知公告
                        Toast.makeText(getActivity(), "通知公告", Toast.LENGTH_SHORT).show();
                        break;
                    case 1://进入官网
                        startActivity(new Intent(getActivity(), InSchoolActivity.class));
                        break;
                    case 2://成绩查询
                        startActivity(new Intent(getActivity(), SignQueryActivity.class));
                        break;
                    case 3://位置服务
                        OpenbottomLocation();
                        break;
                }
            }
        });
    }

    /**
     * 打开第三方地图，并设置经纬度定位
     */
    private void OpenbottomLocation() {
        mBottomSheetPop = new BottomSheetPop(getActivity());
        mBottomSheetPop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mBottomSheetPop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mBottomSheetPop.setContentView(openbottomtview);
        mBottomSheetPop.setBackgroundDrawable(new ColorDrawable(0x00000000));
        mBottomSheetPop.setOutsideTouchable(true);
        mBottomSheetPop.setFocusable(true);
        mBottomSheetPop.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    /**
     * RecyclerView
     */
    private void setRecyclerView() {
        for (int i =0;i<signEncyTitleData.length;i++){
            //设置百科数据
            NoticeEntity noticeEntity=new NoticeEntity();
            noticeEntity.setNoticeTitle(signEncyTitleData[i]);//公告标题
            noticeEntity.setNoticeContent(signEncyContentData[i]);//公告内容概况
            noticeEntity.setNoticeSource(signEncySourceData[i]);//公告信息来源
            noticeEntity.setNoticeTime(signEncyTimeData[i]);//公告发布时间
            entities.add(noticeEntity);
        }




        //当我们确定Item的改变不会影响RecyclerView的宽高的时候可以设置setHasFixedSize(true)，
        // 并通过Adapter的增删改插方法去刷新RecyclerView，而不是通过notifyDataSetChanged()。
        // （当需要改变宽高的时候就用notifyDataSetChanged()去整体刷新一下）
        //获取LinearLayoutManager实例
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        ////限制recyclerview自身滑动特性,滑动全部靠scrollview完成
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setFocusable(false);
        //当平滑滚动条被禁用时，滚动条拇指的位置和大小是基于
        //只关注适配器中项目的数量以及内部可见项的位置
        //适配器。这提供了一个稳定的滚动条，用户可以通过一个项目列表进行导航
        //有不同的宽度/高度。
        recyclerView.setLayoutManager(layoutManager);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);
        //创建adapter
        mCollectRecyclerAdapter = new CollectRecycleAdapter(getActivity(), entities);
        //给RecyclerView设置adapter
        recyclerView.setAdapter(mCollectRecyclerAdapter);
        mCollectRecyclerAdapter.notifyDataSetChanged();
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //设置item的分割线
        //添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(),R.drawable.recyclerview_shape));
        recyclerView.addItemDecoration(divider);
        //RecyclerView中没有item的监听事件，需要自己在适配器中写一个监听事件的接口。参数根据自定义
        mCollectRecyclerAdapter.setOnItemClickListener(new CollectRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, NoticeEntity data) {
                switch (data.NoticeTitle){
                    case "计算机高新技术考试概述":
                        startActivity(new Intent(getActivity(), Title1Activity.class));
                        break;
                    case "开展计算机信息高新技术考试意义":
                        startActivity(new Intent(getActivity(), Title2Activity.class));
                        break;
                    case "全国计算机信息高新技术考试性质":
                        startActivity(new Intent(getActivity(), Title3Acitvity.class));
                        break;
                    case "全国计算机高新技术考试特点":
                        startActivity(new Intent(getActivity(), Title4Acitvity.class));
                        break;
                }
            }

        });
    }

    /**
     * 选择监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_baidu://百度
                if (isAvilible(getActivity(), "com.baidu.BaiduMap")) {//传入指定应用包名
                    try {
                        Intent intent = Intent.getIntent("intent://map/direction?" +
                                "destination=latlng:" + mInfo.getLat() + "," + mInfo.getLng() + "|name:黔南民族职业技术学院" +        //终点
                                "&mode=driving&" +          //导航路线方式
                                "&src=appname#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                        startActivity(intent); //启动调用
                    } catch (URISyntaxException e) {
                        Log.e("intent", e.getMessage());
                    }
                } else {//未安装
                    //market为路径，id为包名
                    //显示手机上所有的market商店
                    Toast.makeText(getActivity(), "您尚未安装百度地图", Toast.LENGTH_LONG).show();
                    Uri uri = Uri.parse("market://details?id=com.baidu.BaiduMap");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null){
                        startActivity(intent);
                    }
                }
                mBottomSheetPop.dismiss();
                break;
            case R.id.btn_gaode:
                if (isAvilible(getActivity(), "com.autonavi.minimap")) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);

                    //将功能Scheme以URI的方式传入data
                    Uri uri = Uri.parse("androidamap://navi?sourceApplication=appname&poiname=fangheng&lat=" + mInfo.getLat() + "&lon=" + mInfo.getLng() + "&dev=1&style=2");
                    intent.setData(uri);

                    //启动该页面即可
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "您尚未安装高德地图", Toast.LENGTH_LONG).show();
                    Uri uri = Uri.parse("market://details?id=com.autonavi.minimap");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null){
                        startActivity(intent);
                    }
                }
                mBottomSheetPop.dismiss();
                break;
            case R.id.btn_tencent:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_DEFAULT);

                //将功能Scheme以URI的方式传入data
                Uri uri = Uri.parse("qqmap://map/routeplan?type=drive&to=黔南民族职业技术学院&tocoord=" + mInfo.getLat() + "," + mInfo.getLng());
                intent.setData(uri);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    //启动该页面即可
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "您尚未安装腾讯地图", Toast.LENGTH_LONG).show();
                }
                mBottomSheetPop.dismiss();
                break;
            case R.id.btn_cancel:
                if (mBottomSheetPop != null) {
                    mBottomSheetPop.dismiss();
                }
                break;
        }
    }
    /**
     * 检查手机上是否安装了指定的软件
     *
     * @param context
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}
