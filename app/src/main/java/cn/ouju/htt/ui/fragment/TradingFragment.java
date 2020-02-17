package cn.ouju.htt.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.ouju.htt.R;
import cn.ouju.htt.adapter.HomeAssetAdapter;
import cn.ouju.htt.bean.TradeBean;
import cn.ouju.htt.bean.UserInfo;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.http.HttpListener;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.popupwindow.PhotoWindow;
import cn.ouju.htt.ui.activity.AssetPayToastActivity;
import cn.ouju.htt.ui.activity.BaseActivity;
import cn.ouju.htt.ui.activity.LoginActivity;
import cn.ouju.htt.ui.activity.MainAssetToastActivity;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.SHAUtils;
import cn.ouju.htt.utils.UserUtils;

import static android.app.Activity.RESULT_OK;

public class TradingFragment extends BaseFragment implements OnRefreshLoadMoreListener {
    private static HttpListener listeners;
    private static BaseActivity.MyHandler handlers;
    @BindView(R.id.change_tv1)
    TextView changeTv1;
    @BindView(R.id.change_tv2)
    TextView changeTv2;
    @BindView(R.id.change_tv3)
    TextView changeTv3;
    @BindView(R.id.change_tv4)
    TextView changeTv4;
    @BindView(R.id.change_lv)
    ListView changeLv;
    @BindView(R.id.main_srf)
    SmartRefreshLayout mainSrf;
    @BindView(R.id.common_ll_data_null)
    LinearLayout commonLlDataNull;
    @BindView(R.id.common_ll_failure)
    LinearLayout commonLlFailure;
    private List<TradeBean> lists = new ArrayList<>();
    private HomeAssetAdapter assetAdapter;
    private int page = 1;
    private int isEnd;
    private PhotoWindow photoWindow;
    private File tempFile;
    private String order_id;
    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    private static final String PHOTO_NAME = "temp_head.jpg";

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (!TextUtils.isEmpty(UserUtils.getString("user_no", null))) {
                page = 1;
                lists.clear();
                if (flag == 0) {
                    changeOrSell(0, "Dti/orderList");
                } else if (flag == 1) {
                    changeOrSell(1, "Dti/trade");
                } else if (flag == 2) {
                    changeOrSell(0, "Dti/trade");
                } else if (flag == 3) {
                    changeOrSell(1, "Dti/orderList");
                }
            }
        }
    }


    public static TradingFragment getInstance(HttpListener listener, BaseActivity.MyHandler handler) {
        TradingFragment tradingFragment = new TradingFragment();
        listeners = listener;
        handlers = handler;
        return tradingFragment;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_change);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, 5);
    }

    private boolean isClick;

    @Override
    protected void initData() {
        photoWindow = new PhotoWindow(getActivity(), this);

        assetAdapter = new HomeAssetAdapter(lists, getActivity());
        assetAdapter.setBack(new HomeAssetAdapter.CallBack() {
            @Override
            public void setPosition(final int position, int flag) {
                if (isClick) {
                    return;
                }
                if (lists == null || lists.size() <= 0) {
                    return;
                }
                if (flag == 0) {
                    isClick = true;
                    Map<String, Object> params = new HashMap<>();
                    params.put("user_no", UserUtils.getString("user_no", null));
                    params.put("order_id", lists.get(position).getOrder_id());
                    CommonService.query("Dti/getUser", params, null, listeners, handlers);
                } else if (flag == 1) {
                    final EditText et = new EditText(getContext());
                    et.setFocusable(true);
                    et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(getString(R.string.input_safe_psw)).setView(et).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String psw = et.getText().toString().trim();
                            if (!TextUtils.isEmpty(psw)) {
                                Map<String, Object> params = new HashMap<>();
                                params.put("user_no", UserUtils.getString("user_no", null));
                                params.put("password", SHAUtils.getSHA(psw));
                                String orderId = lists.get(position).getOrder_id();
                                isClick = true;
                                if (!TextUtils.isEmpty(orderId)) {
                                    //交易列表取消
                                    params.put("order_id", orderId);
                                    CommonService.query("Dti/cancelOrder", params, null, listeners, handlers);
                                } else {
                                    //求购和出售列表取消
                                    params.put("trade_id", lists.get(position).getTrade_id());
                                    CommonService.query("Dti/cancelTrade", params, null, listeners, handlers);
                                }
                            } else
                                showShortToast(getString(R.string.input_safe_psw_hint));
                        }
                    }).show();

                } else if (flag == 2) {
                    final EditText et = new EditText(getContext());
                    et.setFocusable(true);
                    et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(getString(R.string.input_safe_psw)).setView(et).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            String psw = et.getText().toString().trim();
                            if (!TextUtils.isEmpty(psw)) {
                                isClick = true;
                                Map<String, Object> params = new HashMap<>();
                                params.put("user_no", UserUtils.getString("user_no", null));
                                params.put("order_id", lists.get(position).getOrder_id());
                                params.put("password", SHAUtils.getSHA(psw));
                                CommonService.query("Dti/receiveOrder", params, null, listeners, handlers);
                            } else
                                showShortToast(getString(R.string.input_safe_psw_hint));
                        }
                    }).show();
                    //查看凭证
                } else if (flag == 3) {
                    isClick = true;
                    Map<String, Object> params = new HashMap<>();
                    params.put("user_no", UserUtils.getString("user_no", null));
                    params.put("order_id", lists.get(position).getOrder_id());
                    CommonService.query("Dti/viewPay", params, null, listeners, handlers);

                    //上传凭证
                } else if (flag == 4) {
                    order_id = lists.get(position).getOrder_id();
                    if (photoWindow != null && photoWindow.isShowing()) {
                        photoWindow.dismiss();
                    } else
                        photoWindow.showAtLocation(getView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }
            }
        });
        changeLv.setAdapter(assetAdapter);
        mainSrf.setOnRefreshLoadMoreListener(this);
        changeOrSell(0, "Dti/orderList");
    }

    private int flag;
    private Uri contentUri;

    @Override
    @OnClick({R.id.change_tv1, R.id.change_tv2, R.id.change_tv3, R.id.change_tv4, R.id.common_ll_failure})
    public void onClick(View view) {
        if (isClick) {
            return;
        }
        switch (view.getId()) {
            case R.id.change_tv1:
                if (flag != 0) {
                    isClick = true;
                    flag = 0;
                    changeTv1.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    changeTv1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBtn));
                    changeTv2.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    changeTv2.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    changeTv3.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    changeTv3.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    changeTv4.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    changeTv4.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    lists.clear();
                    page = 1;
                    mainSrf.setNoMoreData(true);
                    changeOrSell(0, "Dti/orderList");
                }
                break;
            case R.id.change_tv2:
                if (flag != 1) {
                    isClick = true;
                    flag = 1;
                    changeTv2.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    changeTv2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBtn));
                    changeTv1.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    changeTv1.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    changeTv3.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    changeTv3.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    changeTv4.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    changeTv4.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    lists.clear();
                    page = 1;
                    mainSrf.setNoMoreData(true);
                    changeOrSell(1, "Dti/trade");
                }
                break;
            case R.id.change_tv3:
                if (flag != 2) {
                    isClick = true;
                    flag = 2;
                    changeTv3.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    changeTv3.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBtn));
                    changeTv2.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    changeTv2.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    changeTv1.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    changeTv1.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    changeTv4.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    changeTv4.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    lists.clear();
                    page = 1;
                    mainSrf.setNoMoreData(true);
                    changeOrSell(0, "Dti/trade");
                }
                break;
            case R.id.change_tv4:
                if (flag != 3) {
                    isClick = true;
                    flag = 3;
                    changeTv4.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    changeTv4.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorBtn));
                    changeTv2.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    changeTv2.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    changeTv3.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    changeTv3.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    changeTv1.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
                    changeTv1.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
                    lists.clear();
                    page = 1;
                    mainSrf.setNoMoreData(true);
                    changeOrSell(1, "Dti/orderList");
                }
                break;
            //拍照
            case R.id.photo_take_btn:
                if (!check()) {
                    return;
                }
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_NAME);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    contentUri = FileProvider.getUriForFile(getContext(), "com.mydomain.fileprovider", tempFile);
                } else {
                    contentUri = Uri.fromFile(tempFile);
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                photoWindow.dismiss();
                startActivityForResult(intent, PHOTO_REQUEST_CAREMA);
                break;
            //相册
            case R.id.photo_pick_btn:
                if (!check()) {
                    return;
                }
                photoWindow.dismiss();
                Intent intent1 = new Intent();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent1.setAction(Intent.ACTION_OPEN_DOCUMENT);
                } else {
                    intent1.setAction(Intent.ACTION_GET_CONTENT);
                }
                intent1.setType("image/*");
                startActivityForResult(intent1, PHOTO_REQUEST_GALLERY);
                break;
            case R.id.common_ll_failure:
                lists.clear();
                page = 1;
                switch (flag) {
                    case 0:
                        changeOrSell(0, "Dti/orderList");
                        break;
                    case 1:
                        changeOrSell(1, "Dti/trade");
                        break;
                    case 2:
                        changeOrSell(0, "Dti/trade");
                        break;
                    case 3:
                        changeOrSell(1, "Dti/orderList");
                        break;
                }

                break;
        }
    }


    private boolean check() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 5);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_REQUEST_CAREMA) {
                if (hasSdcard()) {
                   /* Map<String, Object> params = new HashMap<>();
                    params.put("user_no", UserUtils.getString("user_no", null));
                    params.put("order_id", trade.getOrder_id());
                    params.put("password", SHAUtils.getSHA(psw));
                    Map<String, File> maps = new HashMap<>();
                    maps.put("voucher", tempFile);
                    CommonService.upLoad("Gfc/payOrder", params, null, this, handler, maps);*/
                    quality(tempFile);
                } else
                    showShortToast(getString(R.string.sd_card_fail));

            } else if (requestCode == PHOTO_REQUEST_GALLERY) {
                if (data != null) {

                    Uri uri = data.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        handleImgeOnKitKat(data);
                    } else {
                        String[] projection = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        cursor.moveToFirst();
                        filePath = cursor.getString(column_index);
                    }
                    quality(new File(filePath));
                   /* Map<String, Object> params = new HashMap<>();
                    params.put("user_no", UserUtils.getString("user_no", null));
                    params.put("order_id", trade.getOrder_id());
                    params.put("password", SHAUtils.getSHA(psw));
                    Map<String, File> maps = new HashMap<>();
                    maps.put("voucher", new File(filePath));
                    CommonService.upLoad("Gfc/payOrder", params, null, this, handler, maps);*/
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String filePath;

    private void handleImgeOnKitKat(Intent data) {
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getContext(), uri)) {
            //如果是document类型的uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //解析出数字格式的id
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                filePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                filePath = getImagePath(contentUri, null);
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                //如果是content类型的uri，则使用普通方式处理
                filePath = getImagePath(uri, null);
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                //如果是file类型的uri，直接获取图片路径即可
                filePath = uri.getPath();
            }
        }
    }

    /**
     * 通过uri和selection来获取真实的图片路径
     */
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void quality(File file) {
        try {
            Bitmap bm = getBitmapFormUri(getActivity(), Uri.fromFile(file));
            int degree = getBitmapDegree(file.getAbsolutePath());
            /**
             * 把图片旋转为正的方向
             */
            Bitmap newbitmap = rotateBitmapByDegree(bm, degree);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newbitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            final File file1 = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + PHOTO_NAME);
            FileOutputStream fos = new FileOutputStream(file1);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            final EditText et = new EditText(getContext());
            et.setFocusable(true);
            et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            String language = UserUtils.getString("language", null);
            Log.d("----222222---", "" + language);
            builder.setTitle(getString(R.string.input_safe_psw)).setView(et).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String psw = et.getText().toString().trim();
                    if (!TextUtils.isEmpty(psw)) {
                        isClick = true;
                        Map<String, Object> params = new HashMap<>();
                        params.put("user_no", UserUtils.getString("user_no", null));
                        params.put("order_id", order_id);
                        params.put("password", SHAUtils.getSHA(psw));
                        Map<String, File> maps = new HashMap<>();
                        maps.put("voucher", file1);
                        CommonService.upLoad("Dti/payOrder", params, null, listeners, handlers, maps);
                    } else
                        showShortToast(getString(R.string.input_safe_psw_hint));
                }
            }).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 通过Uri获取文件
     *
     * @param ac
     * @param uri
     * @return
     */
    public static File getFileFromMediaUri(Context ac, Uri uri) {
        if (uri.getScheme().compareTo("content") == 0) {
            ContentResolver cr = ac.getContentResolver();
            Cursor cursor = cr.query(uri, null, null, null, null);// 根据Uri从数据库中找
            if (cursor != null) {
                cursor.moveToFirst();
                String filePath = cursor.getString(cursor.getColumnIndex("_data"));// 获取图片路径
                cursor.close();
                if (filePath != null) {
                    return new File(filePath);
                }
            }
        } else if (uri.getScheme().compareTo("file") == 0) {
            return new File(uri.toString().replace("file://", ""));
        }
        return null;
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 1280f;//这里设置高度为800f
        float ww = 720f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 质量压缩方法
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }


    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    private void changeOrSell(int type, String service) {
        Map<String, Object> params = new HashMap<>();
        params.put("user_no", UserUtils.getString("user_no", null));
        params.put("type", type);
        params.put("page", page);
        if (service.equals("Dti/trade")) {
            params.put("is_my", 1);
            params.put("user_no", UserUtils.getString("user_no", null));
        }
        CommonService.query(service, params, null, listeners, handlers);
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        isClick = false;
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals("200")) {
                    if (t[0].equals("Dti/orderList") || t[0].equals("Dti/trade")) {
                        commonLlDataNull.setVisibility(View.GONE);
                        commonLlFailure.setVisibility(View.GONE);
                        isEnd = jsonUtils.getInt("data", "is_end");
                        List<TradeBean> beans = jsonUtils.getEntityList("data", "data_list", new TradeBean());
                        if (beans != null && beans.size() > 0) {
                            lists.addAll(beans);
                            mainSrf.setNoMoreData(false);
                            for (int i = 0; i < lists.size(); i++) {
                                if (flag == 0) {
                                    if (lists.get(i).getSale_uno().equals(UserUtils.getString("user_no", null))) {
                                        if (lists.get(i).getStatus().equals("0")) {
                                            lists.get(i).setType(0);
                                        } else if (lists.get(i).getStatus().equals("1")) {
                                            lists.get(i).setType(1);
                                        }
                                    } else if (lists.get(i).getBuy_uno().equals(UserUtils.getString("user_no", null))) {
                                        if (lists.get(i).getStatus().equals("0")) {
                                            lists.get(i).setType(2);
                                        } else if (lists.get(i).getStatus().equals("1")) {
                                            lists.get(i).setType(3);
                                        }
                                    }
                                } else if (flag == 1) {
                                    lists.get(i).setType(4);
                                    lists.get(i).setBuy_sell(1);
                                } else if (flag == 2) {
                                    lists.get(i).setType(4);
                                    lists.get(i).setBuy_sell(0);
                                } else if (flag == 3) {
                                    lists.get(i).setType(5);
                                }
                            }
                        }
                        if (lists != null && lists.size() <= 0) {
                            commonLlDataNull.setVisibility(View.VISIBLE);
                            commonLlFailure.setVisibility(View.GONE);
                        }
                        assetAdapter.notifyDataSetChanged();
                        if (mainSrf.getState() == RefreshState.Refreshing) {
                            mainSrf.finishRefresh();
                        }
                        if (mainSrf.getState() == RefreshState.Loading) {
                            mainSrf.finishLoadMore();
                        }
                        if (isEnd == 1) {
                            mainSrf.finishLoadMoreWithNoMoreData();
                        }
                    } else if (t[0].equals("Dti/getUser")) {
                        UserInfo userInfo = jsonUtils.getEntity("data", new UserInfo());
                        Intent intent = new Intent();
                        intent.setClass(getContext(), MainAssetToastActivity.class);
                        intent.putExtra("info", userInfo);
                        startActivity(intent);
                    } else if (t[0].equals("Dti/cancelOrder")) {
                        page = 1;
                        lists.clear();
                        if (flag == 0) {
                            changeOrSell(0, "Dti/orderList");
                        } else if (flag == 1) {
                            changeOrSell(1, "Dti/trade");
                        } else if (flag == 2) {
                            changeOrSell(0, "Dti/trade");
                        } else if (flag == 3) {
                            changeOrSell(1, "Dti/orderList");
                        }
                        showShortToast(getString(R.string.cancel_success));

                    } else if (t[0].equals("Dti/receiveOrder")) {
                        page = 1;
                        lists.clear();
                        if (flag == 0) {
                            changeOrSell(0, "Dti/orderList");
                        } else if (flag == 1) {
                            changeOrSell(1, "Dti/trade");
                        } else if (flag == 2) {
                            changeOrSell(0, "Dti/trade");
                        } else if (flag == 3) {
                            changeOrSell(1, "Dti/orderList");
                        }
                        showShortToast(getString(R.string.collection_success));
                    } else if (t[0].equals("Dti/payOrder")) {
                        page = 1;
                        lists.clear();
                        if (flag == 0) {
                            changeOrSell(0, "Dti/orderList");
                        } else if (flag == 1) {
                            changeOrSell(1, "Dti/trade");
                        } else if (flag == 2) {
                            changeOrSell(0, "Dti/trade");
                        } else if (flag == 3) {
                            changeOrSell(1, "Dti/orderList");
                        }
                        showShortToast(getString(R.string.upload_success));
                    } else if (t[0].equals("Dti/viewPay")) {
                        String path = jsonUtils.getString("picture", "data");
                        Intent intent = new Intent();
                        intent.setClass(getContext(), AssetPayToastActivity.class);
                        intent.putExtra("path", path);
                        startActivity(intent);
                    } else if (t[0].equals("Dti/cancelTrade")) {
                        page = 1;
                        lists.clear();
                        if (flag == 0) {
                            changeOrSell(0, "Dti/orderList");
                        } else if (flag == 1) {
                            changeOrSell(1, "Dti/trade");
                        } else if (flag == 2) {
                            changeOrSell(0, "Dti/trade");
                        } else if (flag == 3) {
                            changeOrSell(1, "Dti/orderList");
                        }
                        showShortToast(getString(R.string.cancel_success));
                    }

                } else if (jsonUtils.getCode().equals("405")) {
                    if (mainSrf.getState() == RefreshState.Refreshing) {
                        mainSrf.finishRefresh();
                    }
                    if (mainSrf.getState() == RefreshState.Loading) {
                        mainSrf.finishLoadMore();
                    }
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                } else
                    AlertDialogUtils.showMsg(getContext(), jsonUtils.getMsg());
            }
        }
    }

    @Override
    public void onNetWorkedFail(Object... strings) {
        isClick = false;
        if (mainSrf.getState() == RefreshState.Refreshing) {
            mainSrf.finishRefresh();
        }
        if (mainSrf.getState() == RefreshState.Loading) {
            mainSrf.finishLoadMore();
        }
        lists.clear();
        assetAdapter.notifyDataSetChanged();
        commonLlDataNull.setVisibility(View.GONE);
        commonLlFailure.setVisibility(View.VISIBLE);
        showShortToast(getResources().getString(R.string.network_fail));
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        page = 1;
        lists.clear();
        if (flag == 0) {
            changeOrSell(0, "Dti/orderList");
        } else if (flag == 1) {
            changeOrSell(1, "Dti/trade");
        } else if (flag == 2) {
            changeOrSell(0, "Dti/trade");
        } else if (flag == 3) {
            changeOrSell(1, "Dti/orderList");
        }

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

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        if (isEnd != 1) {
            if (flag == 0) {
                changeOrSell(0, "Dti/orderList");
            } else if (flag == 1) {
                changeOrSell(1, "Dti/trade");
            } else if (flag == 2) {
                changeOrSell(0, "Dti/trade");
            } else if (flag == 3) {
                changeOrSell(1, "Dti/orderList");
            }
        }
    }

}
