package com.example.daidaijie.syllabusapplication.login.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.daidaijie.syllabusapplication.R;
import com.example.daidaijie.syllabusapplication.main.MainActivity;
import com.example.daidaijie.syllabusapplication.base.BaseActivity;
import com.example.daidaijie.syllabusapplication.bean.UserLogin;
import com.example.daidaijie.syllabusapplication.user.UserComponent;
import com.example.daidaijie.syllabusapplication.util.SnackbarUtil;
import com.example.daidaijie.syllabusapplication.widget.LoadingDialogBuiler;

import javax.inject.Inject;

import butterknife.BindView;

public class LoginActivity extends BaseActivity implements LoginContract.view, View.OnClickListener {

    @BindView(R.id.titleLayout)
    LinearLayout mTitleLayout;
    @BindView(R.id.inputLayout)
    LinearLayout mInputLayout;
    @BindView(R.id.loginButton)
    Button mLoginButton;
    @BindView(R.id.tipTextView)
    TextView mTipTextView;
    @BindView(R.id.usernameEditText)
    EditText mUsernameEditText;
    @BindView(R.id.passwordEditText)
    EditText mPasswordEditText;

    AlertDialog mAlertDialog;

    @Inject
    LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerLoginComponent.builder()
                .appComponent(mAppComponent)
                .loginModule(new LoginModule(this))
                .build().inject(this);

        mTipTextView.setText(Html.fromHtml("<u>注意事项</u>"));

        mAlertDialog = LoadingDialogBuiler.getLoadingDialog(this,
                getResources().getColor(R.color.colorPrimary));

        mLoginButton.setOnClickListener(this);
        mLoginPresenter.start();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }


    @Override
    public void onClick(View v) {

        if (mUsernameEditText.getText().toString().trim().isEmpty()) {
            mUsernameEditText.setError("账号名不能为空");
            return;
        }
        if (mPasswordEditText.getText().toString().trim().isEmpty()) {
            mPasswordEditText.setError("密码不能为空");
            return;
        }

        mLoginPresenter.login(mUsernameEditText.getText().toString(),
                mPasswordEditText.getText().toString());
    }

    @Override
    public void showLoading(boolean isShow) {
        if (isShow) {
            mAlertDialog.show();
        } else {
            mAlertDialog.dismiss();
        }
    }

    @Override
    public void setLogin(UserLogin userLogin) {
        mUsernameEditText.setText(userLogin.getUsername());
        mPasswordEditText.setText(userLogin.getPassword());
    }

    @Override
    public void toMainView() {
        UserComponent.buildInstance(mAppComponent);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void showFailMessage(String msg) {
        SnackbarUtil.ShortSnackbar(mPasswordEditText, msg, SnackbarUtil.Alert).show();
    }
}
