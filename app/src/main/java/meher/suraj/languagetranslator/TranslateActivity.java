package meher.suraj.languagetranslator;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.util.ArrayList;
import java.util.Locale;

import meher.suraj.smtranslator.Language;
import meher.suraj.smtranslator.SMTranslate;


public class TranslateActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_1;
    TextView txt, txt_lan_1, txt_lan_2;
    ClipboardManager clipboard;
    ClipData clip;
    MDToast mdToast;
    Boolean flag = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        et_1 = findViewById(R.id.et_1);
        txt = findViewById(R.id.txt);
        txt_lan_1 = findViewById(R.id.txt_lan_1);
        txt_lan_2 = findViewById(R.id.txt_lan_2);
        findViewById(R.id.swap).setOnClickListener(this);
        findViewById(R.id.mic).setOnClickListener(this);
        findViewById(R.id.cp_1).setOnClickListener(this);
        findViewById(R.id.cp_2).setOnClickListener(this);

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        et_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // TODO Auto-generated method stub
                if (flag)
                    translate(et_1.getText().toString());
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mic:
                voice();
                break;

            /*case R.id.swap:
                swap();
                break;*/

            case R.id.cp_1:
                try {
                    copy(et_1.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.cp_2:
                try {
                    copy(txt.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    void toast(String message, int type) {
        mdToast = MDToast.makeText(getApplicationContext(), message, MDToast.LENGTH_SHORT, type);
        mdToast.show();
    }

    void voice() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        if (flag)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        else intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "or");
        try {
            startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException a) {
            toast("Intent Problem", 3);
        }
    }

    void swap() {
        String a = txt_lan_1.getText().toString();
        String b = txt_lan_2.getText().toString();
        a = a + b;
        b = a.substring(0, a.length() - b.length());
        a = a.substring(b.length());
        txt_lan_1.setText(a);
        txt_lan_2.setText(b);
        if (flag)
            flag = false;
        else flag = true;
        et_1.setText(null);
        txt.setText(null);
        toast("Language Changed", 1);
    }

    void copy(String text) {
        if (!text.equals("")) {
            clip = ClipData.newPlainText("text", text);
            clipboard.setPrimaryClip(clip);
            toast("Text Copied", 1);
        } else {
            toast("Text Copied", 2);
//            mdToast = MDToast.makeText(getApplicationContext(), "There is no text", MDToast.LENGTH_SHORT, MDToast.TYPE_WARNING);
        }
        mdToast.show();
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                assert result != null;
                et_1.setText(result.get(0));

                if (flag)
                    translate(et_1.getText().toString().trim());

            }
        }
    }

    public void translate(String text1) {
        SMTranslate translateAPI = new SMTranslate(
                Language.AUTO_DETECT,
                Language.ODIA,
                text1);

        translateAPI.setTranslateListener(new SMTranslate.TranslateListener() {
            @Override
            public void onSuccess(String translatedText) {
                Log.d(TAG, "onSuccess: " + translatedText);
                txt.setText(translatedText.toString());
            }

            @Override
            public void onFailure(String ErrorText) {
                Log.d(TAG, "onFailure: " + ErrorText);
            }
        });
    }

}
    /*private static final String TAG = "MainActivity";
    EditText editText;
    TextView textView;
    Button translateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        translateButton = findViewById(R.id.button);

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SMTranslate translateAPI = new SMTranslate(
                        Language.AUTO_DETECT,
                        Language.ODIA,
                        editText.getText().toString().trim());

                translateAPI.setTranslateListener(new SMTranslate.TranslateListener() {
                    @Override
                    public void onSuccess(String translatedText) {
                        Log.d(TAG, "onSuccess: "+translatedText);
                        textView.setText(translatedText);
                    }

                    @Override
                    public void onFailure(String ErrorText) {
                        Log.d(TAG, "onFailure: "+ErrorText);
                    }
                });

            }
        });

    }
}*/