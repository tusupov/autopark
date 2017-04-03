package com.usupov.autopark.activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.usupov.autopark.R;
import com.usupov.autopark.http.Config;
import com.usupov.autopark.http.HttpHandler;
import com.usupov.autopark.service.SpeachRecogn;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class CarNewActivity extends AppCompatActivity {

    protected static final int RESULT_SPEECH = 1;
    TextView tvVinError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_new);

        initToolbar();

        tvVinError = (TextView) findViewById(R.id.tvVinError);
        tvVinError.setTextColor(Color.RED);

        initVoiceBtn();
        initVinEdittext();
    }

    /**
     * Initial toolbar
     */
    public void initVinEdittext() {
        final EditText vinEditText = (EditText)findViewById(R.id.edittext_vin_number);
        final HttpHandler handler = new HttpHandler();
        final String urlVin = Config.getUrlVin();
        vinEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (vinEditText.getText().length()==17) {
                    String vin = vinEditText.getText()+"";
                    String url = urlVin+vinEditText.getText();
                    String jSonString = handler.ReadHttpResponse(url);
//                    String jSonString = "{name : \"Mersedes\", description : \"Benz\"}";
                    if (jSonString==null) {
                        tvVinError.setText(getString(R.string.error_vin));
//                        Toast.makeText(CarNewActivity.this, getString(R.string.error_vin), Toast.LENGTH_LONG).show();
                    }
                    else {
                        tvVinError.setText("");
                        JSONObject jObject = null;
                        try {
                            jObject = new JSONObject(jSonString);
                            String name = jObject.getString("name");
                            String description = jObject.getString("description");
                            Intent intent = new Intent(CarNewActivity.this, CarFoundActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("name", name);
                            bundle.putString("description", description);
                            bundle.putString("vin", vin);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                        catch (Exception e) {
                            Toast.makeText(CarNewActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else if (vinEditText.getText().length() > 17) {
                    vinEditText.setText(vinEditText.getText().toString().substring(0, 17));
                    Toast.makeText(CarNewActivity.this, getString(R.string.max_limit), Toast.LENGTH_LONG).show();
                }
                else {
                    tvVinError.setText("");
                }
            }
        });
    }
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_car_new);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CarNewActivity.this, MainActivity.class));
                finish();
            }
        });


    }

    private void initVoiceBtn() {

        final EditText edt = (EditText) findViewById(R.id.edittext_vin_number);
        edt.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {

                    if(event.getRawX() >= (edt.getRight() - edt.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {

                        Intent intent = new Intent(CarNewActivity.this, RecognizerSampleActivity.class);
                        startActivityForResult(intent, RESULT_SPEECH);

                        return true;
                    }
                }
                return false;
            }
        });

    }
//3VWBB61C4WM050210
//45RT78WEDST12
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> text = data
                            .getStringArrayListExtra("all_results");
                    EditText edt = (EditText) findViewById(R.id.edittext_vin_number);
//                    edt.setText(text.get(0));

//                    String edt_text = "";
//                    if (edt.getText() != null)
//                        edt_text = edt.getText()+"";
                    String edt_text = SpeachRecogn.vinSpeach(text);
//                    Toast.makeText(CarNewActivity.this, text.size()+"", Toast.LENGTH_LONG).show();
//                    String edt_text = data.getExtras().getString("recognated_string");
//                    Toast.makeText(CarNewActivity.this, edt_text, Toast.LENGTH_LONG).show();
                    if (edt_text.length() > 17)
                        edt_text = edt_text.substring(0, 17);
                    edt.setText(edt_text);
                }
                break;
            }

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
