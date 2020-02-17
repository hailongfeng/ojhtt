package cn.ouju.htt.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ouju.htt.R;
import cn.ouju.htt.http.CommonService;
import cn.ouju.htt.json.JsonUtils;
import cn.ouju.htt.utils.AlertDialogUtils;
import cn.ouju.htt.utils.UserUtils;

public class MeWriteEmailActivity extends BaseActivity {
    @BindView(R.id.write_email_cancel)
    TextView writeEmailCancel;
    @BindView(R.id.write_email_send)
    TextView writeEmailSend;
    @BindView(R.id.write_email_title)
    EditText writeEmailTitle;
    @BindView(R.id.write_email_content)
    EditText writeEmailContent;
    @BindView(R.id.write_email_hint)
    TextView writeEmailHint;

    @Override
    protected void initView() {
        setChildContentView(R.layout.activity_me_write_email);
        ButterKnife.bind(this);
    }

    @Override
    protected void initData() {
        writeEmailContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 500) {
                    if (s.length() != 0) {
                        writeEmailHint.setText(getString(R.string.email_current_input) + s.length() + getString(R.string.character_may_input) + (500 - s.length()) + getString(R.string.character));
                    } else {
                        writeEmailHint.setText(getString(R.string.email_content_hint));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    @OnClick({R.id.write_email_cancel, R.id.write_email_send})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_email_cancel:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.hint)).setMessage(getString(R.string.email_fail)).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
                break;
            case R.id.write_email_send:
                String title = writeEmailTitle.getText().toString().trim();
                String content = writeEmailContent.getText().toString().trim();
                if (title.length() >= 2) {
                    if (!TextUtils.isEmpty(content)) {
                        Map<String, Object> params = new TreeMap<>();
                        params.put("user_no", UserUtils.getString("user_no", null));
                        params.put("title", title);
                        params.put("content", content);
                        CommonService.query("Home/mailCreate", params, null, this, handler);
                    } else
                        showShortToast(getString(R.string.content_exist));
                } else {
                    showShortToast(getString(R.string.email_title_must));
                }
                break;
        }
    }

    @Override
    public void onNetWorkedSuccesses(Object... t) {
        if (t != null && t.length > 0) {
            if (t[1] instanceof JsonUtils) {
                JsonUtils jsonUtils = (JsonUtils) t[1];
                if (jsonUtils.getCode().equals("200")) {
                    if (t[0].equals("Home/mailCreate")) {
                        showShortToast(getString(R.string.send_success));
                        finish();
                    }
                } else
                    AlertDialogUtils.showMsg(this, jsonUtils.getMsg());
            }
        }
    }

    @Override
    public void onDatabaseSuccess(Object... db) {

    }

    @Override
    public void onDatabaseFail() {

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.hint)).setMessage(getString(R.string.email_fail)).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }
}
