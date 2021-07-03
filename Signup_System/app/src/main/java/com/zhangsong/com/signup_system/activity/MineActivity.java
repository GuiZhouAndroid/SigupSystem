package com.zhangsong.com.signup_system.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yalantis.ucrop.UCrop;
import com.zhangsong.com.signup_system.Bmob_date.User;
import com.zhangsong.com.signup_system.R;
import com.zhangsong.com.signup_system.activity.OpenFileActivity.Select.SelectFileExActivity;
import com.zhangsong.com.signup_system.utils.Bmob.AppUtils;
import com.zhangsong.com.signup_system.utils.Bmob.Constant;
import com.zhangsong.com.signup_system.utils.Permission.LogUtils;
import com.zhangsong.com.signup_system.utils.ui.Dialogs.DialogPrompt;
import com.zhangsong.com.signup_system.utils.ui.UI.UiTools;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class MineActivity extends MySwipeBackActivity implements View.OnClickListener {
    private static final String TAG = "MineActivity";
    private Toolbar toolbar_constant;
    private TextView tv_toolbar_title;//标题
    private final int REQUEST_CODE_SELECT_FILE = 105;//Activity请求码
    private RelativeLayout rl_userImage;//头像根布局
    private ImageView rv_userImage;//头像
    private RelativeLayout rl_userSex;//性别根布局
    private TextView tv_userSex;//性别
    private RelativeLayout rl_userPhone;//电话号码根布局
    private TextView tv_userPhone;//电话号码
    private RelativeLayout rl_userSignature;//个性签名根布局
    private TextView tv_userSignature;//个性签名
    private RelativeLayout rl_userRegisterDate;//注册时间根布局
    private TextView tv_userRegisterDate;//注册时间
    private Button btn_update_pwd;//点击更新+密码修改
    private BmobFile bfile;//Bmob文件处理对象
    private EditText edit_old_pwd,edit_new_pwd;//旧密码+新密码
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine );
       // initWindows();//初始化窗体
        initView();//初始化View
        initToolbar();//初始化标题栏Toolbar
        initData();
    }
    /**
     * 初始化窗体
     */
    private void initWindows() {
        // 设置状态栏字体白色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    /**
     * 初始化View
     */
    private void initView() {
        //Toolbar
        toolbar_constant= (Toolbar) findViewById(R.id.toolbar_constant);
        //标题
        tv_toolbar_title = (TextView) findViewById(R.id.tv_toolbar_title);
        tv_toolbar_title.setText(R.string.My_Mine);
        //头像
        rl_userImage= (RelativeLayout) findViewById(R.id.rl_userImage);//头像根布局
        rv_userImage= (ImageView) findViewById(R.id.rv_userImage);//头像
        rl_userImage.setOnClickListener(this);//添加监听器

        //性别
        rl_userSex= (RelativeLayout) findViewById(R.id.rl_userSex);//性别根布局
        tv_userSex= (TextView) findViewById(R.id.tv_userSex);//性别
        rl_userSex.setOnClickListener(this);//添加监听器
        //电话号码
        rl_userPhone= (RelativeLayout) findViewById(R.id.rl_userPhone);//性别根布局
        tv_userPhone= (TextView) findViewById(R.id.tv_userPhone);//性别
        //个性签名
        rl_userSignature= (RelativeLayout) findViewById(R.id.rl_userSignature);//个性签名根布局
        tv_userSignature= (TextView) findViewById(R.id.tv_userSignature);//个性签名
        rl_userSignature.setOnClickListener(this);//添加监听器
        //注册时间
        rl_userRegisterDate= (RelativeLayout) findViewById(R.id.rl_userRegisterDate);//注册时间根布局
        tv_userRegisterDate= (TextView) findViewById(R.id.tv_userRegisterDate);//注册时间

        //点击修改
        btn_update_pwd= (Button) findViewById(R.id.btn_update_pwd);
        btn_update_pwd.setOnClickListener(this);//添加监听器
        //旧密码
        edit_old_pwd= (EditText) findViewById(R.id.edit_old_pwd);
        //新密码
        edit_new_pwd= (EditText) findViewById(R.id.edit_new_pwd);
    }
    private void initToolbar() {
        setSupportActionBar(toolbar_constant);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        //设置返回键可用，如果某个页面想隐藏掉返回键比如首页，可以调用Toolbar.setNavigationIcion(null);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Constant.user = BmobUser.getCurrentUser(User.class); //将本地的用户信息提取出来 取用缓存的数据
        tv_userSignature.setText(Constant.user.getSignature());//设置个性签名
        tv_userPhone.setText(Constant.user.getMobilePhoneNumber());//设置电话号码
        tv_userRegisterDate.setText(Constant.user.getCreatedAt());//设置注册时间
        File file = new File(AppUtils.getIconFilePath());//获取本地Bmob的缓存头像数据
        if (file.exists()) {//如果文件存在
            setIcon(AppUtils.getIconFilePath());//设置文件显示到头像
        } else {
            //头像文件不存在，有可能是图片被删除了，或者没有设置头像
            BmobFile avatarFile = Constant.user.getIcon();
            if (avatarFile != null) {//如果是有头像的就下载下来
                avatarFile.download(new File(Constant.imagePath + File.separator + avatarFile.getFilename()), new DownloadFileListener() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            //下载成功
                            setIcon(s);
                            AppUtils.setIconFilePath(s);
                        }
                    }

                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                });
            } else {
                setDefaultAvatar();
            }
        }
        if (Constant.user.getSignature() != null && !Constant.user.getSignature().equals("")) {
            tv_userSignature.setText(Constant.user.getSignature());
        }
        if (Constant.user.getSex() != null) {
            tv_userSex.setText(Constant.user.getSex());
        }
    }



    /**
     * 选择监听
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_userImage://头像根布局
                openAlbum();//打开相册
                break;
            case R.id.rl_userSex://性别根布局
                editSex();
                break;
            case R.id.rl_userSignature://个性签名根布局
                editSignature();
                break;
            case R.id.btn_update_pwd:
                updateUserPwd();
                break;
        }
    }

    /**
     * 密码修改
     */
    private void updateUserPwd() {

        String oldPwd=edit_old_pwd.getText().toString();//获取旧密码文本
        String newPwd=edit_new_pwd.getText().toString();//获取新密码文本
        UiTools.showSimpleLD(this, R.string.loading_update_pwd);//开始提示框
        BmobUser.updateCurrentUserPassword(oldPwd, newPwd, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                UiTools.closeSimpleLD();//关闭提示框
                if(e==null){//修改成功提示框
                    DialogPrompt dialogPrompt = new DialogPrompt(MineActivity.this, R.string.update_ResetOK, 3);
                    dialogPrompt.showAndFinish(MineActivity.this);
                    MineActivity.this.setResult(RESULT_OK);
                }else {//修改失败提示框
                    DialogPrompt dialogPrompt = new DialogPrompt(MineActivity.this, R.string.update_ResetNo);
                    dialogPrompt.show();

                }
            }
        });
    }

    /**
     * 点击更新用户信息
     */
    private void updateUserData() {
        Constant.user = BmobUser.getCurrentUser(User.class); //将本地的用户信息提取出来 取用缓存的数据
        UiTools.showSimpleLD(this, R.string.loading_update);
        //获取EditText文本内容
        String userSex = tv_userSex.getText().toString();
        String userPhone = tv_userPhone.getText().toString();
        String userSignature = tv_userSignature.getText().toString();
        //设置需要更新的数据
        User user = new User();
        user.setSex(userSex);
        user.setMobilePhoneNumber(userPhone);
        user.setSignature(userSignature);
        //文件存在时
        if (bfile != null) {
            user.setIcon(bfile);
        }
        //提交数据并更新
        user.update(Constant.user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(Constant.user!=null){
                    UiTools.closeSimpleLD();
                    if (e == null) {//更新成功
                        if (bfile != null) {
                            AppUtils.setIconFilePath("");
                        }
                        DialogPrompt dialogPrompt = new DialogPrompt(MineActivity.this, R.string.update_complete, 3);
                        dialogPrompt.showAndFinish(MineActivity.this);
                        MineActivity.this.setResult(RESULT_OK);
                    } else {//更新失败
                        // LogUtils.e(TAG, new Throwable(), e.getErrorCode() + "：" + e.getMessage());
                        if(e.getErrorCode()==211){
                            BmobUser.logOut();   //清除缓存用户对象
                            setDefaultUserInfo();
                            DialogPrompt dialogPrompt = new DialogPrompt(MineActivity.this, R.string.token_exp, 3);
                            dialogPrompt.showAndFinish(MineActivity.this);
                        }
                    }
                }else {
                    Toast.makeText(MineActivity.this, "过期", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * 选择性别
     */
    private void editSex() {
        AlertDialog.Builder adb = new AlertDialog.Builder(MineActivity.this);
        adb.setTitle("选择");
        final String[] items = {"男生", "女生"};
        adb.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int index) {
                tv_userSex.setText(items[index]);
            }
        });
        adb.show();
    }
    /**
     * 打开相册
     */
    private void openAlbum() {
        Intent intent = new Intent(MineActivity.this, SelectFileExActivity.class);
        intent.putExtra("fileType", 0);
        intent.putExtra("maxFileSize", 1);
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * 个性签名
     */
    private void editSignature() {
        AlertDialog.Builder adb = new AlertDialog.Builder(MineActivity.this);
        adb.setTitle(R.string.user_nick_name);
        final EditText et = new EditText(MineActivity.this);
        if (Constant.user.getSignature() != null) {
            et.setText(Constant.user.getSignature());
        }
        adb.setView(et);
        adb.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        arg0.cancel();
                    }
                });
        adb.setPositiveButton(R.string.comfirm,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        tv_userSignature.setText(et.getText().toString());
                    }
                });
        adb.show();
    }
    /**
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     * 第一个参数为请求码，即调用startActivityForResult()传递过去的值
     * 第二个参数为结果码，结果码用于标识返回数据来自指定的Activity
     * 第三个参数为返回数据，存放了返回数据的Intent，使用第三个输入参数可以取出新Activity返回的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_FILE) {

                /**
                 * 请注意， File fileTemp = new File(Constant.basePath + File.separator + "temp.png"  );这句中，Constant.basePath 该目录必须是存在的，否则
                 * 会导致  UCrop.of(uri, Uri.fromFile(fileTemp))后，进行剪裁后，出现E/ANDR-PERF-MPCTL: Invalid profile no. 0, total profiles 0 only
                 * 的错误，该错误仅有一行，不注意的话很难发现。
                 */

                /**
                 * 下面这个是调用系统相册后返回的uri,来选择图片
                 */
//                Uri uri= intent.getData();
//                if (uri != null) {
//                    File fileTemp = new File(Constant.basePath + File.separator + "temp.png"  );
//                    if (fileTemp.exists()) {
//                        fileTemp.delete();
//                    }
//                    UCrop.of(uri, Uri.fromFile(fileTemp))
//                            .withAspectRatio(1, 1)
//                            .withMaxResultSize(512, 512)
//                            .start(this);
//                }

                /**
                 * 下面这个是调用自定义图片选择器来选择的。
                 */
                List<File> list = (List<File>) intent.getSerializableExtra("list");
                if (list.size() > 0) {
                    File selectFile = list.get(0);
                    Uri uri = null;
                    uri = Uri.fromFile(selectFile);
                    if (uri != null) {
                        File fileTemp = new File(Constant.basePath + File.separator + "temp" + selectFile.getName());
                        if (fileTemp.exists()) {
                            fileTemp.delete();
                        }
                        UCrop.of(uri, Uri.fromFile(fileTemp))
                                .withAspectRatio(1, 1)
                                .withMaxResultSize(512, 512)
                                .start(this);
                    }
                }
            }
            if (requestCode == UCrop.REQUEST_CROP) {
                Log.i(TAG, "处理完成");
                Uri resultUri = UCrop.getOutput(intent);
                //这里的resultUri.getPath()获取到的是图片的绝对路径
                Log.e(TAG, "resultUri.getPath()=" + resultUri.getPath());
                bfile = new BmobFile(new File(resultUri.getPath()));
                setIcon(resultUri.getPath());
            } else if (resultCode == UCrop.RESULT_ERROR) {
                Throwable cropError = UCrop.getError(intent);
                LogUtils.e(TAG, new Throwable(), "剪裁错误：" + cropError.getMessage());
            }
        }
    }

    /**
     * 设置默认头像图片
     */
    private void setDefaultAvatar() {
        RequestOptions options = new RequestOptions();
        options.circleCrop();
        Glide.with(this)
                .load(R.mipmap.wheat)
                .apply(options)
                .into(rv_userImage);
    }
    /**
     * 调用Glide 设置加载圆形图片
     * @param path
     */
    private void setIcon(String path) {
        RequestOptions options = new RequestOptions();
        options.circleCrop();
        Glide.with(this)
                .load(path)
                .apply(options)
                .into(rv_userImage);
    }

    /**
     * 设置默认用户信息
     */
    private void setDefaultUserInfo() {
        //圆形头像
        RequestOptions options = new RequestOptions();
        options.circleCrop();
        Glide.with(this)
                .load(R.mipmap.wheat)
                .apply(options)
                .into(rv_userImage);
        tv_userSex.setText(getString(R.string.token_exp));//性别
        tv_userSignature.setText(getString(R.string.token_exp));
        tv_userRegisterDate.setText(getString(R.string.token_exp));//注册时间

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
     * 软键盘的关闭
     */
    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    /**
     *  从报名查询返回主页
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
    /**
     * 引入自定义Toolbar的菜单——二维码扫一扫
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_update,menu);
        return true;
    }
    /**
     * Toolbar 的Navigation Button 添加点击事件,用于返回首页
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.info_update:
                updateInfo();
                break;
        }
        return true;
    }

    /**
     * 开始更新
     */
    private void updateInfo() {
        UiTools.showSimpleLD(this, R.string.loading);
        if (bfile != null) {
            bfile.uploadblock(new UploadFileListener() {//分块上传文件
                @Override
                public void done(BmobException e) {
                    if (e == null) {//上传文件成功
                        updateUserData();
                    } else {
                        Toast.makeText(MineActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onProgress(Integer value) {
                    super.onProgress(value);
                }
            });
        } else {
            updateUserData();
        }
    }

    /**
     * 销毁当前活动时
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Bmob文件位置有缓存文件就删除
        if (bfile != null) {
            bfile.getLocalFile().delete();
        }
    }

}
