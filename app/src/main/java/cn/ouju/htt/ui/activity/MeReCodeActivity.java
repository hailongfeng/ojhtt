package cn.ouju.htt.ui.activity;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.utils.QRCodeUtils;
import cn.ouju.htt.utils.UserUtils;

public class MeReCodeActivity extends BaseActivity {
    @BindView(R.id.toolbar_white_back)
    ImageView toolbarWhiteBack;
    @BindView(R.id.recode_iv_code)
    ImageView recodeIvCode;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_main_recode);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        //检测是否有写的权限
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
        final Bitmap bm = QRCodeUtils.createBitmap(UserUtils.getString("qrcode", null), 180, 180);
        recodeIvCode.setImageBitmap(bm);
    }

    private void saveBitmap(Bitmap bitmap, String bitName) {
        String fileName;
        File file;
        if (Build.BRAND.equals("Xiaomi")) { // 小米手机
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + bitName;
        } else {  // Meizu 、Oppo
            fileName = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + bitName;
        }
        file = new File(fileName);

        if (file.exists()) {
            file.delete();
        }
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                out.flush();
                out.close();
                MediaStore.Images.Media.insertImage(this.getContentResolver(), file.getAbsolutePath(), bitName, null);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();

        }
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + fileName)));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                showShortToast(getString(R.string.need_authorization_upload_img));
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean check() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 5);
            return false;
        } else {
            return true;
        }
    }

    @Override
    @OnClick({R.id.recode_tv_share, R.id.toolbar_white_back, R.id.recode_tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.recode_tv_share:
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(UserUtils.getString("qrcode", null));
                showShortToast(getString(R.string.copy));
                break;
            case R.id.toolbar_white_back:
                finish();
                break;
            case R.id.recode_tv_save:
                if (check()) {
                    View v = getWindow().getDecorView();
                    v.setDrawingCacheEnabled(true);
                    v.buildDrawingCache();
                    Bitmap temBitmap = v.getDrawingCache();
                    showLongToast(getString(R.string.save_photo));
                    saveBitmap(temBitmap, System.currentTimeMillis() + ".JPEG");
                }
                break;
        }
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {

    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }

}
