package com.jwcnetworks.bsyoo.jwc.user.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jwcnetworks.bsyoo.jwc.R;
import com.jwcnetworks.bsyoo.jwc.hppt.HttpSignUp;
import com.jwcnetworks.bsyoo.jwc.model.ModelUser;
import com.jwcnetworks.bsyoo.jwc.network.Network;
import com.jwcnetworks.bsyoo.jwc.network.NetworkCheck;
import com.jwcnetworks.bsyoo.jwc.user.terms.TermsActivity;

import java.util.Date;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private TextView tv_addr1, tv_addr2, tv_terms1, tv_terms2, tv_pwcheck;
    private EditText et_id, et_pw, et_pw2, et_name, et_email, et_email2, et_addr3, et_phone1, et_phone2, et_phone3, et_hphone1, et_hphone2, et_hphone3, et_Mutual, et_Representation, et_Buisness1, et_Buisness2, et_Buisness3, et_Sectors;
    private CheckBox ch_email, ch_phone, ch_terms1, ch_terms2;
    private RadioButton radioButton;
    public Integer REQUEST_CODE = 8575;
    private LinearLayout buisness_layout;
    private ModelUser user = new ModelUser();

    // ID, Email 중복확인
    private int idcheck = 0;
    private int emailcheck = 0;

    private Boolean netcheck = true;  // 네트워크 연결확인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // 액션바에 백그라운드 이미지 넣기
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable d = getResources().getDrawable(R.drawable.actionbar);
        getSupportActionBar().setBackgroundDrawable(d);
        getSupportActionBar().setTitle("회원가입");

        // 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setbyid();

        // 밑줄치기
        SpannableString content = new SpannableString("회원가입 이용약관에 동의 합니다.");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tv_terms1.setText(content);
        SpannableString content2 = new SpannableString("개인정보취급방침에 동의합니다.");
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        tv_terms2.setText(content2);

        // 비밀번호 일치한지 확인
        et_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_pw2.getText().toString().equals(et_pw.getText().toString())) {
                    tv_pwcheck.setText("비밀번호가 일치합니다.");
                    tv_pwcheck.setTextColor(Color.parseColor("#FF22FF00"));
                } else {
                    tv_pwcheck.setText("비밀번호가 일치하지않습니다.");
                    tv_pwcheck.setTextColor(Color.parseColor("#FF0000"));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        et_pw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_pw.getText().toString().equals(et_pw2.getText().toString())) {
                    tv_pwcheck.setText("비밀번호가 일치합니다.");
                    tv_pwcheck.setTextColor(Color.parseColor("#FF22FF00"));
                } else {
                    tv_pwcheck.setText("비밀번호가 일치하지않습니다.");
                    tv_pwcheck.setTextColor(Color.parseColor("#FF0000"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                tv_addr1.setText(data.getStringExtra("우편"));
                tv_addr2.setText(data.getStringExtra("주소"));
            }
            // 리턴값이 없을떄
            else {
            }
        }
        // 네트워크 불량에서 오는 Result
        if (requestCode == 7777 ) {
            if (resultCode == RESULT_OK) {

            }
            //리턴값이 없을때
            else {
            }
        }
    }

    public void signClick(View view) {
        switch (view.getId()) {
            case R.id.btn_id_check:
                user.setID(et_id.getText().toString());
                if (user.getID().toString().length() <= 5) {
                    Toast.makeText(this, "아이디를 6자리 이상 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    netcheck = networkcheck();
                    if(netcheck == true) {
                        new SignUpActivity.HttpIDCheck().execute(user);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), NetworkCheck.class);
                        startActivityForResult(intent, 7777);
                    }
                }
                break;
            case R.id.btn_email_check:
                user.setEmail(et_email.getText().toString()+"@"+et_email2.getText().toString());
                if (user.getEmail().toString().length() <= 4) {
                    Toast.makeText(this, "이메일을 정확히 입력해 주세요", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    netcheck = networkcheck();
                    if(netcheck == true) {
                        new SignUpActivity.HttpEmailCheck().execute(user);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), NetworkCheck.class);
                        startActivityForResult(intent, 7777);
                    }
                }
                break;
            case R.id.btn_addr:
                Intent intent = new Intent(SignUpActivity.this, AddressActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.tv_terms1:
                Intent intent2 = new Intent(SignUpActivity.this, TermsActivity.class);
                intent2.putExtra("순서", 0);
                startActivity(intent2);
                break;
            case R.id.tv_terms2:
                Intent intent3 = new Intent(SignUpActivity.this, TermsActivity.class);
                intent3.putExtra("순서", 1);
                startActivity(intent3);
                break;
            case R.id.radioButton:
                buisness_layout.setVisibility(View.GONE);
                break;
            case R.id.radioButton2:
                buisness_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_signupfinish:
                user.setID(et_id.getText().toString());
                user.setPW(et_pw2.getText().toString());
                user.setName(et_name.getText().toString());
                user.setEmail(et_email.getText().toString()+"@"+et_email2.getText().toString());
                user.setAddr(tv_addr1.getText().toString() + " " + tv_addr2.getText().toString() + " / " + et_addr3.getText().toString());
                user.setPhone(et_phone1.getText().toString() + et_phone2.getText().toString() + et_phone3.getText().toString());
                user.setPhone_home(et_hphone1.getText().toString() +"-"+ et_hphone2.getText().toString() +"."+ et_hphone3.getText().toString());

                Date mDate = new Date();
                user.setUserTime(mDate);
                if (ch_email.isChecked() == true) {
                    user.setEmail_sms(1);
                } else {
                    user.setEmail_sms(2);
                }
                if (ch_phone.isChecked() == true) {
                    user.setPhone_sms(1);
                } else {
                    user.setPhone_sms(2);
                }
                if (radioButton.isChecked() == true) {
                    user.setOK(1);
                } else {
                    user.setOK(2);
                    user.setMutual(et_Mutual.getText().toString());
                    user.setRepresentation(et_Representation.getText().toString());
                    user.setBuisness_number(et_Buisness1.getText().toString() + et_Buisness2.getText().toString() + et_Buisness3.getText().toString());
                    user.setSectors(et_Sectors.getText().toString());
                }

                // 회원가입정보를 입력하지 않은 경우
                if (user.getPW().length() <= 7) {
                    Toast.makeText(this, "패스워드를 8자리 이상 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    break;
                } else if (!et_pw.getText().toString().equals(et_pw2.getText().toString())) {
                    Toast.makeText(this, "비밀번호가 일치하지 않습니다..", Toast.LENGTH_SHORT).show();
                    break;
                } else if (user.getName().length() <= 1) {
                    Toast.makeText(this, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    break;
                } else if (tv_addr2.getText().toString().equals("")) {
                    Toast.makeText(this, "주소를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    break;
                } else if (user.getPhone().length() <= 10) {
                    Toast.makeText(this, "핸드폰 번호를 전부 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    break;
                } else if (idcheck == 0) {
                    Toast.makeText(this, "ID 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                } else if (emailcheck == 0) {
                    Toast.makeText(this, "Email 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (radioButton.isChecked() == false) {
                    if (user.getMutual().length() <= 3) {
                        Toast.makeText(this, "상호명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (user.getRepresentation().length() <= 1) {
                        Toast.makeText(this, "대표자명을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (user.getBuisness_number().length() <= 9) {
                        Toast.makeText(this, "사업자번호를 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    } else if (user.getSectors().length() <= 3) {
                        Toast.makeText(this, "업태 / 업종을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (ch_terms1.isChecked() == false || ch_terms2.isChecked() == false) {
                    Toast.makeText(this, "모든 이용약관에 동의해 주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                netcheck = networkcheck();
                if(netcheck == true) {
                    new SignUp().execute(user);
                } else {
                    Intent intent4 = new Intent(getApplicationContext(), NetworkCheck.class);
                    startActivityForResult(intent4, 7777);
                }
                break;
        }
    }

    // EditText 영문&숫자만 적용
    protected InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

    private void setbyid() {
        et_id = (EditText) findViewById(R.id.et_id);
        et_id.setFilters(new InputFilter[]{filter});
        et_pw = (EditText) findViewById(R.id.et_pw);
        et_pw.setFilters(new InputFilter[]{filter});
        et_pw2 = (EditText) findViewById(R.id.et_pw2);
        et_pw2.setFilters(new InputFilter[]{filter});
        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_email.setFilters(new InputFilter[]{filter});
        et_email2 = (EditText) findViewById(R.id.et_email2);
        et_email2.setFilters(new InputFilter[]{filter});
        et_addr3 = (EditText) findViewById(R.id.et_addr3);
        et_phone1 = (EditText) findViewById(R.id.et_phone1);
        et_phone2 = (EditText) findViewById(R.id.et_phone2);
        et_phone3 = (EditText) findViewById(R.id.et_phone3);
        et_hphone1 = (EditText) findViewById(R.id.et_hphone1);
        et_hphone2 = (EditText) findViewById(R.id.et_hphone2);
        et_hphone3 = (EditText) findViewById(R.id.et_hphone3);
        et_Mutual = (EditText) findViewById(R.id.et_Mutual);
        et_Representation = (EditText) findViewById(R.id.et_Representation);
        et_Buisness1 = (EditText) findViewById(R.id.et_Buisness1);
        et_Buisness2 = (EditText) findViewById(R.id.et_Buisness2);
        et_Buisness3 = (EditText) findViewById(R.id.et_Buisness3);
        et_Sectors = (EditText) findViewById(R.id.et_Sectors);

        ch_email = (CheckBox) findViewById(R.id.ch_email);
        ch_phone = (CheckBox) findViewById(R.id.ch_phone);
        ch_terms1 = (CheckBox) findViewById(R.id.ch_terms1);
        ch_terms2 = (CheckBox) findViewById(R.id.ch_terms2);

        radioButton = (RadioButton) findViewById(R.id.radioButton);

        tv_addr1 = (TextView) findViewById(R.id.tv_addr1);
        tv_addr2 = (TextView) findViewById(R.id.tv_addr2);
        tv_terms1 = (TextView) findViewById(R.id.tv_terms1);
        tv_terms2 = (TextView) findViewById(R.id.tv_terms2);
        tv_pwcheck = (TextView) findViewById(R.id.tv_pwcheck);

        buisness_layout = (LinearLayout) findViewById(R.id.buisness_layout);
    }


    // 회원가입
    public class SignUp extends AsyncTask<ModelUser, Integer, Integer> {

        private ProgressDialog waitDlg = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // ProgressDialog 보이기
            // 서버 요청 완료후 Mating dialog를 보여주도록 한다.
            waitDlg = new ProgressDialog(SignUpActivity.this);
            waitDlg.setMessage("회원가입중 입니다.");
            waitDlg.show();
        }

        @Override
        protected Integer doInBackground(ModelUser... params) {

            Integer count = new HttpSignUp().signupinsert(user);

            return count;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);

            // Progressbar 감추기 : 서버 요청 완료수 Maiting dialog를 제거한다.
            if (waitDlg != null) {
                waitDlg.dismiss();
                waitDlg = null;
            }
            if (s == 1) {
                if(user.getOK()==1){
                    Toast.makeText(SignUpActivity.this, "회원가입을 환영합니다. 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(SignUpActivity.this, "사업자 로그인 승인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    // ID중복체크
    public class HttpIDCheck extends AsyncTask<ModelUser, Integer, Integer> {

        private ProgressDialog waitDlg = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // ProgressDialog 보이기
            // 서버 요청 완료후 Mating dialog를 보여주도록 한다.
            waitDlg = new ProgressDialog(SignUpActivity.this);
            waitDlg.setMessage("ID 확인중 입니다.");
            waitDlg.show();
        }

        @Override
        protected Integer doInBackground(ModelUser... params) {

            Integer count = new HttpSignUp().IDCheck(user);

            return count;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);

            // Progressbar 감추기 : 서버 요청 완료수 Maiting dialog를 제거한다.
            if (waitDlg != null) {
                waitDlg.dismiss();
                waitDlg = null;
            }
            if (s == 1) {
                Toast.makeText(SignUpActivity.this, "중복된 ID 입니다.", Toast.LENGTH_SHORT).show();
                idcheck = 0;
            } else {
                Toast.makeText(SignUpActivity.this, "사용가능한 ID 입니다.", Toast.LENGTH_SHORT).show();
                idcheck = 1;
            }
        }
    }

    // Email중복체크
    public class HttpEmailCheck extends AsyncTask<ModelUser, Integer, Integer> {

        private ProgressDialog waitDlg = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // ProgressDialog 보이기
            // 서버 요청 완료후 Mating dialog를 보여주도록 한다.
            waitDlg = new ProgressDialog(SignUpActivity.this);
            waitDlg.setMessage("Email 확인중 입니다.");
            waitDlg.show();
        }

        @Override
        protected Integer doInBackground(ModelUser... params) {

            Integer count = new HttpSignUp().EmailCheck(user);

            return count;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);

            // Progressbar 감추기 : 서버 요청 완료수 Maiting dialog를 제거한다.
            if (waitDlg != null) {
                waitDlg.dismiss();
                waitDlg = null;
            }
            if (s == 1) {
                Toast.makeText(SignUpActivity.this, "중복된 Email 입니다.", Toast.LENGTH_SHORT).show();
                emailcheck = 0;
            } else {
                Toast.makeText(SignUpActivity.this, "사용가능한 Email 입니다.", Toast.LENGTH_SHORT).show();
                emailcheck = 1;
            }
        }
    }


    // 네트워크 체크
    private boolean networkcheck(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Boolean wifi = new Network().isNetWork(networkInfo);
        if(wifi == true){
            return true;
        } else {
            return false;
        }
    }
}
