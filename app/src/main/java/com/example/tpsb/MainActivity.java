package com.example.tpsb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tpsb.myprimg.LicencePlate;
import com.example.tpsb.utils.BottomDialog;
import com.example.tpsb.utils.CheckPermissionsActivity;
import com.example.tpsb.utils.DateUtil;
import com.example.tpsb.utils.FileUtilser;
import com.example.tpsb.utils.LogUtil;
import com.example.tpsb.utils.PictureUtil;
import com.example.tpsb.myprimg.PlateRecognizer;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends CheckPermissionsActivity implements View.OnClickListener {

    private ImageView mImgAdd;
    /**
     * 地方所
     */
    private TextView mTvResult;
    /**
     * 其他信息为
     */
    private TextView mTvOtherInfo;
    private BottomDialog bottomDialog;

    private int maxSelectNum = 19;
    private int code =  PictureConfig.CHOOSE_REQUEST;
    private int chooseMode = PictureMimeType.ofImage();
    private List<LocalMedia> selectList = new ArrayList<>();
    private String pathe;
    private String plate;
    private String underpainting;
    private String number;
    private LicencePlate licencePlate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        intData();

    }

    private void initView() {
        mImgAdd = (ImageView) findViewById(R.id.img_add);
        mImgAdd.setOnClickListener(this);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mTvOtherInfo = (TextView) findViewById(R.id.tv_other_info);
    }


    private void intData() {
        mTvResult.setText(PlateRecognizer.stringFromJNI());
        plate = DateUtil.initCity();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                licencePlate = DateUtil.parseJson(plate, LicencePlate.class);
            }
        }, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.img_add:
                showBottomDialog("选择图片类型", "从手机相册选择", "拍照");
                //myImg();
                break;
        }
    }

    //测试用例
    private void myImg() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        PlateRecognizer mPlateRecognizer = new PlateRecognizer(this);
        String plate = mPlateRecognizer.getCarNumRes(bmp);
        mTvResult.setText(plate);
        Toast.makeText(this, plate, Toast.LENGTH_LONG).show();
    }

    private void showBottomDialog(String teil, String textone, String texttwo) {
        bottomDialog = new BottomDialog(this);
        bottomDialog.setTitleText(teil);
        bottomDialog.setOneText(textone);
        bottomDialog.setTwoText(texttwo);
        bottomDialog.setClicklistener(new BottomDialog.ClickListenerInterface() {
            @Override
            public void onTitleClick() {

            }

            @Override
            public void onOneClick() {
                PictureUtil.Album(MainActivity.this, chooseMode, selectList, code, maxSelectNum, false);
                bottomDialog.dismissDialog();
            }

            @Override
            public void onTwoClick() {
                PictureUtil.Camera(MainActivity.this, chooseMode, selectList, code, maxSelectNum, false);
                bottomDialog.dismissDialog();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的

                    for (LocalMedia media : selectList) {
                        LogUtil.e("图片-----》", media.getCompressPath());
                        pathe = media.getCompressPath();
                    }

                    //进行压缩设置图片在600K，宽度为1080，高度为260
                    //pathe = FileUtilser.compressImageUpload(compress, 600, 1080, 260);

                    File file = new File(pathe);
                    String pathSiz = FileUtilser.getFormetFileSize(file);
                    LogUtil.e("文件大小为：",pathSiz);

                    mImgAdd.setAdjustViewBounds(true);
                    mImgAdd.setScaleType(ImageView.ScaleType.FIT_XY);
                    Glide.with(MainActivity.this).load(pathe).into(mImgAdd);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            uploadFile();
                        }
                    }, 1000);

                    break;
            }
        }
    }

    private void uploadFile() {
        Bitmap bitmap = FileUtilser.getPathImage(pathe);
        PlateRecognizer mPlateRecognizer = new PlateRecognizer(MainActivity.this);
        String plate = mPlateRecognizer.getCarNumRes(bitmap);

        mTvResult.setText(plate);

        if(plate.length() == 10){
            underpainting = plate.substring(0,2);
            String district = plate.substring(3,5);
            number = plate.substring(plate.length() - 5);
            getAreaCodeByNum(district);

        }else{
            mTvOtherInfo.setText("识别失败，数据不完整");
        }

    }


    private void getAreaCodeByNum(String district) {
        if(licencePlate != null && licencePlate.getCodeInfo() != null){
            List<LicencePlate.CodeInfoBean> cityes = licencePlate.getCodeInfo();
            String nub = "";
            String district_two = "";
            for (LicencePlate.CodeInfoBean city : cityes){
                if (district.equals(city.getHp())){
                    district_two = city.getHp();
                    nub = "省份：" + city.getProvince() + "\n"
                            + "拼音简称：" + city.getPcode() + "\n"
                            + "城市：" + city.getCity() + "\n"
                            + "地区代码：" + city.getAreaCode() + "\n"
                            + "牌照地区简称：" + district + "\n"
                            + "牌照颜色：" + underpainting + "\n"
                            + "牌照编号：" + number + "\n";
                }
            }
            if (district.equals(district_two)){
                mTvOtherInfo.setText(nub);
            }else {
                mTvOtherInfo.setText("暂无此车牌信息！");
            }

        }else {
            LogUtil.e("licencePlate",  "数据为空");
            mTvOtherInfo.setText("暂无此车牌信息！");
        }

    }
}
