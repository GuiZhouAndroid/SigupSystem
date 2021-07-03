package com.zhangsong.com.signup_system;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.zhouwei.library.CustomPopWindow;
import com.zhangsong.com.signup_system.Bmob_date.User;
import com.zhangsong.com.signup_system.activity.AboutAuthorActivity;
import com.zhangsong.com.signup_system.activity.BaseActivity;
import com.zhangsong.com.signup_system.activity.CalendarActivity;
import com.zhangsong.com.signup_system.activity.InSchoolActivity;
import com.zhangsong.com.signup_system.activity.LoginActivity;
import com.zhangsong.com.signup_system.activity.MineActivity;
import com.zhangsong.com.signup_system.activity.SettingActivity;
import com.zhangsong.com.signup_system.fragment.MainSignUpFragment;
import com.zhangsong.com.signup_system.fragment.ManiClassificationFragment;
import com.zhangsong.com.signup_system.fragment.ManiHomePageFragment;
import com.zhangsong.com.signup_system.fragment.ManiMyInfoFragment;
import com.zhangsong.com.signup_system.navigation.EasyNavigitionBar;
import com.zhangsong.com.signup_system.navigation.View.KickBackAnimator;
import com.zhangsong.com.signup_system.utils.Bmob.AppUtils;
import com.zhangsong.com.signup_system.utils.Bmob.Constant;
import com.zhangsong.com.signup_system.utils.Navigation.Anim;
import com.zhangsong.com.signup_system.utils.Navigation.NavigitionUtil;
import com.zhangsong.com.signup_system.utils.Permission.LogUtils;
import com.zhangsong.com.signup_system.utils.ui.Dialogs.DialogPrompt;
import com.zhangsong.com.signup_system.utils.ui.Dialogs.DialogPromptPermission;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.zhangsong.com.signup_system.utils.Bmob.AppUtils.setIconFilePath;

public class MainActivity extends BaseActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainActivity";
    private CoordinatorLayout coordinator;
    //Activity之间传的数据Code
    private final int REQUEST_CODE_LOGIN = 102;
    private final int REQUEST_CODE_UPDATE = 104;
    private final int REQUEST_CODE_PERMISSIONS = 1005;
    private DrawerLayout drawer_layout;//侧滑
    private Toolbar toolbar_constant;//自定义Toolbar
    private NavigationView nav_view;  //隐藏抽屉
    private long lastClickTime;//计算时刻，判断3s内再次点击是否退出
    public static ImageView nav_header_icon;//头像
    public static TextView tv_username,tv_signature,mani_title;//侧滑—用户名+个性签名+
    private MyLocationListener myListener = new MyLocationListener();  //定位监听器
    public LocationClient mLocationClient = null;
    LocationClientOption option = new LocationClientOption();
    private TextView mani_title_location;//Title定位显示
    private Timer timer = new Timer();//获取定时器对象
    private Handler handler=new Handler();
    //底部导航+动画弹窗
    private EasyNavigitionBar navigitionbar;
    private String[] tabText = {"首页", "分类", "助手", "报名", "我的"};
    //未选中icon
    private int[] normalIcon = {R.mipmap.index, R.mipmap.find, R.mipmap.add_image, R.mipmap.message, R.mipmap.me};
    //选中时icon
    private int[] selectIcon = {R.mipmap.index1, R.mipmap.find1, R.mipmap.add_image, R.mipmap.message1, R.mipmap.me1};
    //仿微博图片和文字集合
    private int[] menuIconItems = {R.mipmap.questionbank, R.mipmap.achievementquery, R.mipmap.mydata, R.mipmap.mysign};
    private String[] menuTextItems = {"题库宝典", "成绩查询", "我的资料", "数据管理"};
    private List<Fragment> fragments = new ArrayList<>(); //碎片集合
    private LinearLayout menuLayout;
    private View cancelImageView;
    private Handler ShowMenuHandler = new Handler();
    private ImageView menuImage1,menuImage2, menuImage3,menuImage4;
    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;
    //Toolbar弹窗
    private CustomPopWindow mCustomPopWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient=new LocationClient(getApplicationContext());
        //用实例通过registerLocationListener()方法注册 定位监听器，获取当前位置的时候，回调这个定位监听器
        mLocationClient.registerLocationListener(myListener);
        setContentView(R.layout.activity_main);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        // 如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);
        //mLocationClient为第二步初始化过的LocationClient对象
        // 需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(option);

        createTimer();//用来每100s实时监听单点登录及账户信息变化，若变化就强制下线，不能报名
        initView();
        initBottomNavigation();

    }

    /**
     * 判断是否登录
     */
    private void initUserState() {
        //判断User表中是否有数据
        if(Constant.user==null){//无数据情况，点头像跳转登录页面
            // 跳转到登录界面—LoginActivity
            startActivityForResult(new Intent(this,LoginActivity.class),REQUEST_CODE_LOGIN);
        }else {//无数据情况，进入主页并打开侧滑
            Snackbar snackbar = Snackbar.make(drawer_layout, "已为您自动登录", Snackbar.LENGTH_LONG);
            setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
            snackbar.show();
        }
    }

    /**
     * 创建定时器 100秒
     */
    private void createTimer() {
        timer.schedule(new Task(),1,100000);
    }
    /**
     * 判断检查文件是否存在，不存在就指定创建文件夹
     */
    private void initData(){
        File fileBase = new File(Constant.imagePath);
        if (!fileBase.exists()) {//不存在情况
            fileBase.mkdirs();//创建子目录
        }
        setUserInfo();
    }
    /**
     *设置用户信息
     */
    @SuppressLint("SetTextI18n")
    private void setUserInfo() {
        Constant.user = BmobUser.getCurrentUser(User.class); //将本地的用户信息提取出来 取用缓存的数据
        if(Constant.user!=null){//若有缓存数据+已登录
            tv_username.setText(Constant.user.getUsername());//设置侧滑用户名
            //tv_myinfo_username.setText(Constant.user.getUsername());//设置我的用户名
            getDate(mani_title);//设置友好时间标题
            String path = AppUtils.getIconFilePath();
            File file = new File(path);
            if (file.exists()){//文件已存在
                setIcon(path);//Glide圆形图片
            }else {//头像文件不存在，图片在logout()方法中调用 AppUtils.setIconFilePath("");文件被清空删除了或者没有设置头像
                BmobFile iconFile = Constant.user.getIcon();//获取Bmob云端图片
                if(iconFile!= null){//如果是有头像的就下载下来,用于缓存头像
                    iconFile.download(new File(Constant.imagePath + File.separator + iconFile.getFilename()),new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {//下载成功
                            if(e==null){
                                setIcon(s);//把图片设置到头像
                                setIconFilePath(s);//图片本地保存
                                if (Looper.myLooper() == Looper.getMainLooper()) {
                                    Log.i(TAG, "主线程");
                                } else {
                                    Log.i(TAG, "子线程");
                                }
                            }
                        }
                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                }else {//如果没有头像，就使用默认头像
                    setDefaultIcon();
                }
            }
            //判断个性签名，已登录就设置Bmob中存储的签名数据
            if (Constant.user.getSignature() != null && !Constant.user.getSignature().equals("")) {
                tv_signature.setText(Constant.user.getSignature());
            }
        }else {//未登录
            setDefaultNotlogin();
        }
    }

    /**
     * 未登录状态默认设置
     */
    private void setDefaultNotlogin(){
        setDefaultIcon();//使用默认头像
        tv_username.setText(R.string.click_user_image);//使用默认用户名
        tv_signature.setText(R.string.signature);//使用默认个性签名
        mani_title.setText(getString(R.string.app_name));//标题
    }
    /**
     * 调用Glide 设置加载圆形图片
     * Glide加载图片缓存库出现——You cannot start a load for a destroyed activity
     * 不要再非主线程里面使用Glide加载图片，如果真的使用了，请把context参数换成getApplicationContext。
     * @param path
     */
    @SuppressLint("CheckResult")
    private void setIcon(String path) {
        Log.i("MainActivity","path="+path);
        Log.i("MainActivity","getApplicationContext()="+getApplicationContext());
        Log.i("MainActivity","nav_header_icon()="+nav_header_icon.getId());
        RequestOptions options = new RequestOptions();
        options.circleCrop();
        Glide.with(getApplicationContext())
                .load(path)
                .apply(options)
                .into(nav_header_icon);
    }

    /**
     * 设置默认头像图片
     * Glide加载图片缓存库出现——You cannot start a load for a destroyed activity
     * 不要再非主线程里面使用Glide加载图片，如果真的使用了，请把context参数换成getApplicationContext。
     */
    @SuppressLint("CheckResult")
    private void setDefaultIcon() {
        RequestOptions options = new RequestOptions();
        options.circleCrop();
        Glide.with(getApplicationContext())
                .load(R.mipmap.wheat)
                .apply(options)
                .into(nav_header_icon);
    }

    /**
     * 权限申请
     * 百度定位检查权限/获取文件位置+写入外部存储器+读取手机状态
     */
    private void initPermission() {
        List<String> permissionList = new ArrayList<>();  //创建空List集合
        //运行时权限申请
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
            initData();
        }
    }
    /**
     * 初始化View
     */
    private void initView() {

        coordinator=findViewById(R.id.coordinator);
        //下拉刷新
        swipeRefresh=findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        swipeRefresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefresh.setDistanceToTriggerSync(300);// 设置手指在屏幕下拉多少距离会触发下拉刷新//
        //  mSwipeLayout.setProgressBackgroundColor(R.color.colorBackground); // 设定下拉圆圈的背景//
        // mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小

        mani_title_location=findViewById(R.id.mani_title_location);
        //mani标题
        mani_title=findViewById(R.id.mani_title);
        //Toolbar+DrawerLayout
        toolbar_constant =findViewById(R.id.toolbar_constant);
        setSupportActionBar(toolbar_constant); //设置自定义Toolbar
        //推动DrawerLayout主布局+隐藏布局
        drawer_layout=findViewById(R.id.drawer_layout);
        drawer_layout.openDrawer(GravityCompat.START);//默认打开侧滑
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, toolbar_constant, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //获取mDrawerLayout中的第一个子布局，也就是布局中的RelativeLayout
                //获取抽屉的view
                View mContent = drawer_layout.getChildAt(0);
                float scale = 1 - slideOffset;
                float endScale = 0.8f + scale * 0.2f;
                float startScale = 1 - 0.3f * scale;
                //设置左边菜单滑动后的占据屏幕大小
                drawerView.setScaleX(startScale);
                drawerView.setScaleY(startScale);
                //设置菜单透明度
                drawerView.setAlpha(0.6f + 0.4f * (1 - scale));
                //设置内容界面水平和垂直方向偏转量
                //在滑动时内容界面的宽度为 屏幕宽度减去菜单界面所占宽度
                mContent.setTranslationX(drawerView.getMeasuredWidth() * (1 - scale));
                //设置内容界面操作无效（比如有button就会点击无效）
                mContent.invalidate();
                //设置右边菜单滑动后的占据屏幕大小
                mContent.setScaleX(endScale);
                mContent.setScaleY(endScale);
            }
        };
        toggle.syncState();
        drawer_layout.addDrawerListener(toggle);
        navigitionbar = findViewById(R.id.navigitionbar);//底部导航

        /**
         * 给DrawerLayout侧滑的Navigation导航菜单添加点击事件
         */
        nav_view=findViewById(R.id.nav_view);   //侧滑抽屉菜单
        //头像—获取侧滑布局中头部的View，并获取其中id
        nav_header_icon=nav_view.getHeaderView(0).findViewById(R.id.nav_header_icon);//头像
        nav_header_icon.setOnClickListener(this);//设置监听
        tv_username=nav_view.getHeaderView(0).findViewById(R.id.tv_username);//用户名
        tv_signature=nav_view.getHeaderView(0).findViewById(R.id.tv_signature);//个性签名
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.drawer_menu_school:
                        startActivity(new Intent(MainActivity.this, InSchoolActivity.class));
                        return true;
                    case R.id.drawer_menu_see_calendar:
                        startActivity(new Intent(MainActivity.this,CalendarActivity.class));
                        return true;
                    case R.id.drawer_menu_setting:
                        startActivity(new Intent(MainActivity.this,SettingActivity.class));
                        return true;
                    case R.id.drawer_menu_about_author:
                        startActivity(new Intent(MainActivity.this,AboutAuthorActivity.class));
                        return true;
                    case R.id.drawer_menu_logout:
                        logout();
                        drawer_layout.closeDrawers();//关闭侧滑
                        return true;
                }
                drawer_layout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        initPermission();//权限申请
        initUserState();//判断是否登录
    }
    /**
     * 初始化Fragment
     */
    private void initBottomNavigation() {
        fragments.add(new ManiHomePageFragment());
        fragments.add(new ManiClassificationFragment());
        fragments.add(new MainSignUpFragment());
        fragments.add(new ManiMyInfoFragment());
        navigitionbar.titleItems(tabText)//item标题
                .normalIconItems(normalIcon)//未选中Icon
                .selectIconItems(selectIcon)//选中Icon
                .fragmentList(fragments)//碎片集合
                .fragmentManager(getSupportFragmentManager())
                .onTabClickListener(new EasyNavigitionBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int position) {
                        if(position==4){
                            if(Constant.user!=null){
                                return false;
                            }else {
                                DialogPrompt dialogPrompt = new DialogPrompt(MainActivity.this, R.string.please_do_login);
                                dialogPrompt.show();
                                return true;//return true则拦截事件、不进行页面切换
                            }
                        }
                        if(position==3){
                            if(Constant.user!=null){
                                return false;
                            }else {
                                DialogPrompt dialogPrompt = new DialogPrompt(MainActivity.this, R.string.please_do_login);
                                dialogPrompt.show();
                                return true;//return true则拦截事件、不进行页面切换
                            }
                        }
                        if (position == 2) {
                            //弹出菜单
                            showMenu();
                        }
                        return false;
                    }
                })
                .mode(EasyNavigitionBar.MODE_ADD)
                .anim(Anim.ZoomIn)
                .build();
        navigitionbar.setAddViewLayout(createMainView());
    }
    //仿微博弹出菜单
    private View createMainView() {
        ViewGroup view = (ViewGroup) View.inflate(MainActivity.this, R.layout.item_add_view, null);
        menuLayout = view.findViewById(R.id.icon_group);
        cancelImageView = view.findViewById(R.id.cancel_iv);
        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeAnimation();
            }
        });
        View itemView = View.inflate(MainActivity.this, R.layout.item_ppup_view, null);
        menuImage1 = itemView.findViewById(R.id.menu_icon_iv1);
        TextView menuText1 = itemView.findViewById(R.id.menu_text_tv1);
        menuImage1.setImageResource(menuIconItems[0]);
        menuText1.setText(menuTextItems[0]);
        menuImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, menuTextItems[1], Toast.LENGTH_SHORT).show();
            }
        });

        menuImage2 = itemView.findViewById(R.id.menu_icon_iv2);
        TextView menuText2 = itemView.findViewById(R.id.menu_text_tv2);
        menuImage2.setImageResource(menuIconItems[1]);
        menuText2.setText(menuTextItems[1]);
        menuImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, menuTextItems[1], Toast.LENGTH_SHORT).show();
            }
        });
        menuImage3 = itemView.findViewById(R.id.menu_icon_iv3);
        TextView menuText3 = itemView.findViewById(R.id.menu_text_tv3);
        menuImage3.setImageResource(menuIconItems[2]);
        menuText3.setText(menuTextItems[2]);
        menuImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.user==null){//无数据情况，点头像跳转登录页面
                    DialogPrompt dialogPrompt = new DialogPrompt(MainActivity.this, getString(R.string.please_do_login));
                    dialogPrompt.show();
                }else {//无数据情况，点头像跳转更新资料页面
                    // 跳转到个人资料—MineActivity
                    startActivityForResult(new Intent(MainActivity.this,MineActivity.class),REQUEST_CODE_UPDATE);
                }
            }
        });
        menuImage4 = itemView.findViewById(R.id.menu_icon_iv4);
        TextView menuText4 = itemView.findViewById(R.id.menu_text_tv4);
        menuImage4.setImageResource(menuIconItems[3]);
        menuText4.setText(menuTextItems[3]);
        menuImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, menuTextItems[3], Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        itemView.setLayoutParams(params);
        itemView.setVisibility(View.GONE);
        menuLayout.addView(itemView);
        return view;
    }

    /**
     * 点击显示弹窗动画
     */
    private void showMenu() {
        startAnimation();
        ShowMenuHandler.post(new Runnable() {
            @Override
            public void run() {
                //＋ 旋转动画
                cancelImageView.animate().rotation(90).setDuration(500);
            }
        });
        //菜单项弹出动画
        for (int i = 0; i < menuLayout.getChildCount(); i++) {
            final View child = menuLayout.getChildAt(i);
            child.setVisibility(View.INVISIBLE);
            ShowMenuHandler.postDelayed(new Runnable() {

                @Override
                public void run() {
                    child.setVisibility(View.VISIBLE);
                    ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
                    fadeAnim.setDuration(600);
                    KickBackAnimator kickAnimator = new KickBackAnimator();
                    kickAnimator.setDuration(600);
                    fadeAnim.setEvaluator(kickAnimator);
                    fadeAnim.start();
                }
            }, i * 50 + 100);
        }
    }
    private void startAnimation() {
        ShowMenuHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //圆形扩展的动画
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        int x = NavigitionUtil.getScreenWidth(MainActivity.this) / 2;
                        int y = (int) (NavigitionUtil.getScreenHeith(MainActivity.this) - NavigitionUtil.dip2px(MainActivity.this, 25));
                        Animator animator = ViewAnimationUtils.createCircularReveal(navigitionbar.getAddViewLayout(), x,
                                y, 0, navigitionbar.getAddViewLayout().getHeight());
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                navigitionbar.getAddViewLayout().setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                //							layout.setVisibility(View.VISIBLE);
                            }
                        });
                        animator.setDuration(400);
                        animator.start();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 关闭window动画
     */
    private void closeAnimation() {
        ShowMenuHandler.post(new Runnable() {
            @Override
            public void run() {
                cancelImageView.animate().rotation(0).setDuration(500);
            }
        });
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                int x = NavigitionUtil.getScreenWidth(this) / 2;
                int y = (NavigitionUtil.getScreenHeith(this) - NavigitionUtil.dip2px(this, 25));
                Animator animator = ViewAnimationUtils.createCircularReveal(navigitionbar.getAddViewLayout(), x,
                        y, navigitionbar.getAddViewLayout().getHeight(), 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        //							layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        navigitionbar.getAddViewLayout().setVisibility(View.GONE);
                        //dismiss();
                    }
                });
                animator.setDuration(400);
                animator.start();
            }
        } catch (Exception e){

        }
    }

    public EasyNavigitionBar getNavigitionBar() {
        return navigitionbar;
    }

    /**
     * 选择监听
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.nav_header_icon:
                //判断User表中是否有数据
                if(Constant.user==null){//无数据情况，点头像跳转登录页面
                    // 跳转到登录界面—LoginActivity
                    startActivityForResult(new Intent(this,LoginActivity.class),REQUEST_CODE_LOGIN);
                }else {//无数据情况，点头像跳转更新资料页面
                    // 跳转到个人资料—MineActivity
                    startActivityForResult(new Intent(this,MineActivity.class),REQUEST_CODE_UPDATE);
                }
                break;
        }
    }
    /**
     * 重写下拉刷新
     */
    @Override
    public void onRefresh() {
        Message msg =  Refreshhandler.obtainMessage(0x123);
        Refreshhandler.sendMessageDelayed(msg,4000);
    }

    /**
     * 下拉刷新使用的Handler
     */
    private Handler Refreshhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0x123){
                swipeRefresh.setRefreshing(false);//停止刷新，无数据操作
            }
        }
    };

    /**
     * 开始定位,定位结果回调注册的监听器
     */
    private void requestLocation() {
        mLocationClient.start();
    }
    /**
     * 判断权限
     * 所有权限被授权后才调用requestLocation()方法开始定位，只要有一个没授权就执行finish退出程序
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
                case REQUEST_CODE_PERMISSIONS:
                    boolean isOK = true;
                    for (int grantResult : grantResults) {
                        LogUtils.i(TAG, new Throwable(), "grantResult=" + grantResult);
                        if (PackageManager.PERMISSION_GRANTED != grantResult) {
                            LogUtils.i(TAG, new Throwable(), "grantResult=" + (PackageManager.PERMISSION_GRANTED != grantResult));
                            isOK = false;
                            break;
                        }
                    }
                    if (isOK) {
                        initData();
                    } else {
                        // 用户授权拒绝之后，友情提示一下就可以了
                        LogUtils.e("Login", new Throwable(), "权限被拒绝");
                        // 这里应该弹出dialog详细说明一下
                        // Toast.makeText(this,
                        // "您拒绝了所需录音权限的申请，将不能进行操作，请在设置或安全中心开启该项权限后重新操作",
                        // Toast.LENGTH_LONG).show();
                        DialogPromptPermission dialogPromptPermission = new DialogPromptPermission(this);
                        dialogPromptPermission.setPromptText("您拒绝了应用所需权限的申请，继续操作将导致部分功能无法正常使用，请在设置或安全中心开启相应的权限后重新操作");
                        dialogPromptPermission.show();
                    }
                    break;
        }
    }



    /**
     * 注册定位监听器
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isAvailable()) {
                mani_title_location.setText("定位失败，请检查网络重启定位");
            }
            else {
                //在此处进行你的后续联网操作
                mani_title_location.setText(city+"-"+district+"-"+street);
            }
        }
    }
    /**
     * 引入自定义Toolbar的菜单——二维码扫一扫
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        return true;
    }

    /**
     * Toolbar 的Navigation Button 添加点击事件,用于打开或关闭侧滑抽屉
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawer_layout=findViewById(R.id.drawer_layout);
                drawer_layout.openDrawer(GravityCompat.START);
                break;
            case R.id.search:
//                new ToastUtil().ToastLocation(MainActivity.this,"搜索待实现", Toast.LENGTH_SHORT, Gravity.CENTER);
                OpenToolbarPopup();//打开标题栏弹窗
                break;
        }
        return true;
    }

    /**
     * 打开标题栏弹窗
     */
    private void OpenToolbarPopup() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.custom_toolbar_pop_menu,null);
        //处理popWindow 显示内容
        handleLogic(contentView);
        //创建并显示popWindow
        mCustomPopWindow= new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Log.e("TAG","onDismiss");
                    }
                })
                .create()
                .showAsDropDown(findViewById(R.id.search),0,20);
    }
    /**
     * 处理弹出显示内容、点击事件等逻辑
     * @param contentView
     */
    private void handleLogic(View contentView){
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCustomPopWindow!=null){
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()){
                    case R.id.menu1:
                        Toast.makeText(MainActivity.this, "点击 Item菜单1", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu2:
                        Toast.makeText(MainActivity.this, "点击 Item菜单2", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu3:
                        Toast.makeText(MainActivity.this, "点击 Item菜单3", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu4:

                        Toast.makeText(MainActivity.this, "点击 Item菜单4", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu5:
                        Toast.makeText(MainActivity.this, "点击 Item菜单5", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        contentView.findViewById(R.id.menu1).setOnClickListener(listener);
        contentView.findViewById(R.id.menu2).setOnClickListener(listener);
        contentView.findViewById(R.id.menu3).setOnClickListener(listener);
        contentView.findViewById(R.id.menu4).setOnClickListener(listener);
        contentView.findViewById(R.id.menu5).setOnClickListener(listener);
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     * 第一个参数为请求码，即调用startActivityForResult()传递过去的值
     * 第二个参数为结果码，结果码用于标识返回数据来自LoginActivity
     * 第三个参数为返回数据，存放了返回数据的Intent，使用第三个输入参数可以取出LoginActivity返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_LOGIN || requestCode == REQUEST_CODE_UPDATE) {
                setUserInfo();//登录或修改个人信息后更新数据
            }
        }
    }
    /**
     * 判断当前时间处于那个时间
     * @param tv
     */
    private void getDate(TextView tv){
        Date d = new Date();
        if(d.getHours()<11){
            tv.setText(Constant.user.getUsername()+",早上好");
        }else if(d.getHours()<13){
            tv.setText(Constant.user.getUsername()+",中午好");
        }else if(d.getHours()<18){
            tv.setText(Constant.user.getUsername()+",下午好");
        }else if(d.getHours()<24){
            tv.setText(Constant.user.getUsername()+",晚上好");
        }
    }
    /**
     * 防触碰处理
     * 判断内再按一次退出程序+侧滑开关状态
     */
    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            if (lastClickTime == 0) {
                lastClickTime = System.currentTimeMillis();
                Snackbar snackbar = Snackbar.make(coordinator, "再次点击退出应用", Snackbar.LENGTH_LONG);
                setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                snackbar.show();
               // new ToastUtil().ToastLocation(MainActivity.this,"", Toast.LENGTH_SHORT, Gravity.CENTER);
            } else {
                long now = System.currentTimeMillis();
                if (now - lastClickTime < 3000) {
                    super.onBackPressed();
                } else {
                    lastClickTime = now;
                    Snackbar snackbar = Snackbar.make(coordinator, "再次点击退出应用", Snackbar.LENGTH_LONG);
                    setSnackbarMessageTextColor(snackbar, Color.parseColor("#FFFFFF"));
                    snackbar.show();
                }
            }
        }
    }
    /**
     * 不会点击外面和按返回键消失
     */
    public void showNoProject(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setMessage(getResources().getString(R.string.token_exp))
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BmobUser.logOut();   //清除缓存用户对象
                        AppUtils.setIconFilePath("");
                        Constant.user = null;
                        startActivityForResult(new Intent(MainActivity.this,LoginActivity.class),REQUEST_CODE_LOGIN);
                        dialog.dismiss();
                        //timer.cancel();

                    }
                })
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        builder.setCancelable(false);
        builder.show();
    }
    /**
     * 退出后，使用默认设置
     */
    public void logout(){
        BmobUser.logOut();   //清除缓存用户对象
        setIconFilePath("");
        Constant.user = null;
        tv_signature.setText(R.string.signature);//个性签名
        tv_username.setText(R.string.click_user_image);//侧滑用户名
        setDefaultIcon();//侧滑默认头像
        mani_title.setText(R.string.app_name);
    }
    /**
     *
     *定时器执行的操作
     */
    private class Task extends TimerTask {
        @Override
        public void run() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String username=tv_username.getText().toString();
                    //设置需要更新的数据
                    BmobUser bmobUser = new BmobUser();
                    bmobUser.setUsername(username);
                    bmobUser.setMobilePhoneNumber(username);
                    if(Constant.user!=null){//提交数据并更新
                        bmobUser.update(Constant.user.getObjectId(), new UpdateListener() {
                             @Override
                             public void done(BmobException e) {
                                 if(e==null){

                                 }else {//更新失败
                                     // LogUtils.i(TAG, new Throwable(), e.getErrorCode() + ":" + e.getMessage());
                                    Constant.user=null;
                                    BmobUser.logOut();
                                    setDefaultIcon();//使用侧滑默认头像
                                    tv_username.setText(R.string.click_user_image);//使用默认用户名
                                    tv_signature.setText(R.string.signature);//使用默认个性签名
                                    mani_title.setText(getString(R.string.app_name));//标题
                                    AppUtils.setIconFilePath("");
                                    showNoProject();
                                }
                            }
                        });
                    }else {
                        setDefaultNotlogin();
                    }
                }
            });
        }
    }
    /**
     * 设置Snackbar上提示的字体颜色
     * @param snackbar
     * @param color
     */
    public static void setSnackbarMessageTextColor(Snackbar snackbar, int color){
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }
    /**
     * 销毁程序后，停止定时器
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 将定时器销毁掉
        timer.cancel();
    }
}
//        //滑到底部出现悬浮按钮
//        nested_view=findViewById(R.id.nested_view);
//        nested_view.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            //滑动高度:scrollY 获得NestedScrollView屏幕高度:v.getMeasuredHeight()
//            // 获得子控件的高度,即LinearLayout高度:v.getChildAt(0).getMeasuredHeight()
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY==(v.getChildAt(0).getMeasuredHeight()-v.getMeasuredHeight())){
//                    AnimatorUtil.showFab(floating_action_btn);
//                }
//            }
//        });







