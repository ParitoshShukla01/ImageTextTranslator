package com.Kalaathon.imagetexttranslator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import android.os.Bundle;

public class interactive extends AppCompatActivity {

    private static final int permission_request_code = 200;
    private static final int imggallery_code = 1000;
    private static final int imgcamera_code = 1001;
    ImageView mImageView;
    EditText mEditText;
    EditText detectedet;
    Button mButton;
    TextView mTextView;
    Spinner mSpinner;
    Uri imguri;
    ImageView cpy1, cpy2;
    ProgressBar mProgressBar;

    final String langcode[] = {"af", "am", "ar", "ar-Latn", " az", "be", "bg", "bg-Latn", "bn", "bs", "ca", "ceb", "co", "cs", "cy", "da", "de", "el", "el-Latn", "en", "eo",
            "es", "et", "eu", "fa", "fi", "fil", "fr", "fy", "ga", "gd", "gl", "gu", "ha", "haw", "hi", "hi-Latn", "hmn", "hr", "ht", "hu", "hy", "id", "ig", "is", "it",
            "iw", "ja", "ja-Latn", "jv", "ka", "kk", "km", "kn", "ko", "ku", "ky", "la", "lb", "lo", "lt", "lv", "mg", "mi", "mk", "ml", "mn", "mr", "ms", "mt", "my", "ne",
            "nl", "no", "ny", "pa", "pl", "ps", "pt", "ro", "ru", "ru-Latn", "sd", "si", "sk", "sl", "sm", "sn", "so", "sq", "sr", "st", "su", "sv", "sw", "ta", "te", "tg",
            "th", "tr", "uk", "ur", "uz", "vi", "xh", "yi", "yo", "zh", "zh-Latn", "zu"};

    final String langname[] = {"Afrikaans", "Amharic", "Arabic", "Arabic", "Azerbaijani", "Belarusian", "Bulgarian", "Bulgarian", "Bengali", "Bosnian", "Catalan",
            "Cebuano", "Corsican", "Czech", "Welsh", "Danish", "German", "Greek", "Greek", "English", "Esperanto", "Spanish", "Estonian", "Basque", "Persian",
            "Finnish", "Filipino", "French", "Western Frisian", "Irish", "Scots Gaelic", "Galician", "Gujarati", "Hausa", "Hawaiian", "Hindi", "Hindi", "Hmong",
            "Croatian", "Haitian", "Hungarian", "Armenian", "Indonesian", "Igbo", "Icelandic", "Italian", "Hebrew", "Japanese", "Japanese", "Javanese",
            "Georgian", "Kazakh", "Khmer", "Kannada", "Korean", "Kurdish", "Kyrgyz", "Latin", "Luxembourgish", "Lao", "Lithuanian", "Latvian", "Malagasy",
            "Maori", "Macedonian", "Malayalam", "Mongolian", "Marathi", "Malay", "Maltese", "Burmese", "Nepali", "Dutch", "Norwegian", "Nyanja", "Punjabi",
            "Polish", "Pashto", "Portuguese", "Romanian", "Russian", "Russian", "Sindhi", "Sinhala", "Slovak", "Slovenian", "Samoan", "Shona", "Somali",
            "Albanian", "Serbian", "Sesotho", "Sundanese", "Swedish", "Swahili", "Tamil", "Telugu", "Tajik", "Thai", "Turkish", "Ukrainian", "Urdu", "Uzbek",
            "Vietnamese", "Xhosa", "Yiddish", "Yoruba", "Chinese", "Chinese", "Zulu"};

    final String fblcode[] = {"af", "ar", "be", "bg", "bn", "ca", "cs", "cy", "da", "de", "el", "en", "eo", "es", "et", "fa", "fi", "fr", "ga", "gl", "gu",
            "hi", "hr", "ht", "hu", "id", "is", "it", "ja", "ka", "kn", "ko", "lt", "lv", "mk", "mr", "ms", "mt", "nl", "no", "pl", "pt", "ro", "ru",
            "sk", "sl", "sq", "sv", "sw", "ta", "te", "th", "tr", "uk", "ur", "vi", "zh"};

    final String fblname[] = {"Afrikaans", "Arabic", "Belarusian", "Bulgarian", "Bengali", "Catalan", "Czech", "Welsh", "Danish", "German", "Greek", "English",
            "Esperanto", "Spanish", "Estonian", "Persian", "Finnish", "French", "Irish", "Galician", "Gujarati", "Hindi", "Croatian", "Haitian", "Hungarian",
            "Indonesian", "Icelandic", "Italian", "Japanese", "Georgian", "Kannada", "Korean", "Lithuanian", "Latvian", "Macedonian", "Ma rathi", "Malay",
            "Maltese", "Dutch", "Norwegian", "Polish", "Portuguese", "Romanian", "Russian", "Slovak", "Slovenian", "Albanian", "Swedish", "Swahili", "Tamil",
            "Telugu", "Thai", "Turkish", "Ukrainian", "Urdu", "Vietnamese", "Chinese"};

    int slc;
    int tlc;
    String spnr;
    String spnrcode;
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interactive);

        getSupportActionBar().setTitle("Image Text Translator");
        mButton = findViewById(R.id.translatebtn);
        mEditText = findViewById(R.id.result);
        mImageView = findViewById(R.id.imgview);
        mTextView = findViewById(R.id.detectedlanguage);
        mSpinner = findViewById(R.id.spinner);
        detectedet = findViewById(R.id.translatedresult);
        cpy1 = findViewById(R.id.copy);
        cpy2 = findViewById(R.id.translatedcopy);
        mProgressBar = findViewById(R.id.prgsbar);

        ArrayAdapter arrayAdapter = new ArrayAdapter(interactive.this, android.R.layout.simple_spinner_item, fblname);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spnr = adapterView.getItemAtPosition(i).toString().trim();
                for (int j = 0; j < fblname.length; j++) {
                    if (spnr.equals(fblname[j])) {
                        spnrcode = fblcode[j].trim();
                    }
                }
                tlc = getSourceLanguageCode(spnrcode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] items = {"Camera", "Gallery"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(interactive.this);
                dialog.setTitle("Select Image");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (!permission())
                            requestpermission();
                        else {
                            if (i == 0)
                                pickcamera();
                            if (i == 1)
                                pickgallery();
                        }
                    }
                });
                dialog.create().show();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Lang detect
                String strtext = mEditText.getText().toString();
                detectedet.getText().clear();
                FirebaseLanguageIdentification identification = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
                identification.identifyLanguage(strtext).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        int qwerty = 0;
                        if (s.equals("und")) {
                            mTextView.setVisibility(View.GONE);
                            Toast.makeText(interactive.this, "Unrecognizable Language!", Toast.LENGTH_SHORT).show();
                        } else {
                            mProgressBar.setVisibility(View.VISIBLE);
                            for (int i = 0; i < langcode.length; i++) {
                                if (langcode[i].trim().equals(s.trim())) {
                                    mTextView.setText("Detected Language: " + langname[i]);
                                    mTextView.setVisibility(View.VISIBLE);
                                    break;
                                }
                            }
                            for (int i = 0; i < fblcode.length; i++) {
                                if (s.equals(fblcode[i].trim())) {
                                    slc = getSourceLanguageCode(s);
                                    translatetext(slc, tlc);
                                    qwerty = 1;
                                    break;
                                }
                            }
                            if (qwerty == 0)
                                Toast.makeText(interactive.this, "Translation From This Language Currently Not Supported!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        cpy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copytext(mEditText.getText().toString().trim());
            }
        });
        cpy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copytext(detectedet.getText().toString().trim());
            }
        });
    }

    private void copytext(String trim) {
        if (!trim.isEmpty()) {
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("EditText", trim);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(this, "Text Copied To Clipboard.", Toast.LENGTH_SHORT).show();
        }
    }

    private void translatetext(int slc, int tlc) {
        Toast.makeText(interactive.this, "Please wait! Translating first time may take some time.", Toast.LENGTH_SHORT).show();
        FirebaseTranslatorOptions options =
                new FirebaseTranslatorOptions.Builder()
                        .setSourceLanguage(slc)
                        .setTargetLanguage(tlc)
                        .build();

        final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                String sourcetext = mEditText.getText().toString().trim();
                translator.translate(sourcetext).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        detectedet.setText(s);
                        detectedet.setVisibility(View.VISIBLE);
                        cpy2.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.GONE);
                    }
                });
            }
        });
        translator.downloadModelIfNeeded(conditions).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(interactive.this, "Language Script Downloading Failed!", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private int getSourceLanguageCode(String s) {
        int lc;
        switch (s) {
            case "af":
                lc = FirebaseTranslateLanguage.AF;
                return lc;

            case "ar":
                lc = FirebaseTranslateLanguage.AR;
                return lc;

            case "be":
                lc = FirebaseTranslateLanguage.BE;
                return lc;

            case "bg":
                lc = FirebaseTranslateLanguage.BG;
                return lc;

            case "bn":
                lc = FirebaseTranslateLanguage.BN;
                return lc;

            case "ca":
                lc = FirebaseTranslateLanguage.CA;
                return lc;

            case "cs":
                lc = FirebaseTranslateLanguage.CS;
                return lc;

            case "cy":
                lc = FirebaseTranslateLanguage.CY;
                return lc;

            case "da":
                lc = FirebaseTranslateLanguage.DA;
                return lc;

            case "de":
                lc = FirebaseTranslateLanguage.DE;
                return lc;

            case "el":
                lc = FirebaseTranslateLanguage.EL;
                return lc;

            case "en":
                lc = FirebaseTranslateLanguage.EN;
                return lc;

            case "eo":
                lc = FirebaseTranslateLanguage.EO;
                return lc;

            case "es":
                lc = FirebaseTranslateLanguage.ES;
                return lc;

            case "et":
                lc = FirebaseTranslateLanguage.ET;
                return lc;

            case "fa":
                lc = FirebaseTranslateLanguage.FA;
                return lc;

            case "fi":
                lc = FirebaseTranslateLanguage.FI;
                return lc;

            case "fr":
                lc = FirebaseTranslateLanguage.FR;
                return lc;

            case "ga":
                lc = FirebaseTranslateLanguage.GA;
                return lc;

            case "gl":
                lc = FirebaseTranslateLanguage.GL;
                return lc;

            case "gu":
                lc = FirebaseTranslateLanguage.GU;
                return lc;

            case "hi":
                lc = FirebaseTranslateLanguage.HI;
                return lc;

            case "hr":
                lc = FirebaseTranslateLanguage.HR;
                return lc;

            case "ht":
                lc = FirebaseTranslateLanguage.HT;
                return lc;

            case "hu":
                lc = FirebaseTranslateLanguage.HU;
                return lc;

            case "id":
                lc = FirebaseTranslateLanguage.ID;
                return lc;

            case "is":
                lc = FirebaseTranslateLanguage.IS;
                return lc;

            case "it":
                lc = FirebaseTranslateLanguage.IT;
                return lc;

            case "ja":
                lc = FirebaseTranslateLanguage.JA;
                return lc;

            case "ka":
                lc = FirebaseTranslateLanguage.KA;
                return lc;

            case "kn":
                lc = FirebaseTranslateLanguage.KN;
                return lc;

            case "ko":
                lc = FirebaseTranslateLanguage.KO;
                return lc;

            case "lt":
                lc = FirebaseTranslateLanguage.LT;
                return lc;

            case "lv":
                lc = FirebaseTranslateLanguage.LV;
                return lc;

            case "mk":
                lc = FirebaseTranslateLanguage.MK;
                return lc;

            case "mr":
                lc = FirebaseTranslateLanguage.MR;
                return lc;

            case "ms":
                lc = FirebaseTranslateLanguage.MS;
                return lc;

            case "mt":
                lc = FirebaseTranslateLanguage.MT;
                return lc;

            case "nl":
                lc = FirebaseTranslateLanguage.NL;
                return lc;

            case "no":
                lc = FirebaseTranslateLanguage.NO;
                return lc;

            case "pl":
                lc = FirebaseTranslateLanguage.PL;
                return lc;

            case "pt":
                lc = FirebaseTranslateLanguage.PT;
                return lc;

            case "ro":
                lc = FirebaseTranslateLanguage.RO;
                return lc;

            case "ru":
                lc = FirebaseTranslateLanguage.RU;
                return lc;

            case "sk":
                lc = FirebaseTranslateLanguage.SK;
                return lc;

            case "sl":
                lc = FirebaseTranslateLanguage.SL;
                return lc;

            case "sq":
                lc = FirebaseTranslateLanguage.SQ;
                return lc;

            case "sv":
                lc = FirebaseTranslateLanguage.SV;
                return lc;

            case "sw":
                lc = FirebaseTranslateLanguage.SW;
                return lc;

            case "ta":
                lc = FirebaseTranslateLanguage.TA;
                return lc;

            case "te":
                lc = FirebaseTranslateLanguage.TE;
                return lc;

            case "th":
                lc = FirebaseTranslateLanguage.TH;
                return lc;

            case "tr":
                lc = FirebaseTranslateLanguage.TR;
                return lc;

            case "uk":
                lc = FirebaseTranslateLanguage.UK;
                return lc;

            case "ur":
                lc = FirebaseTranslateLanguage.UR;
                return lc;

            case "vi":
                lc = FirebaseTranslateLanguage.VI;
                return lc;

            case "zh":
                lc = FirebaseTranslateLanguage.ZH;
                return lc;
            default:
                lc = -1;
                return lc;
        }
    }

    private void pickgallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, imggallery_code);
    }

    private void pickcamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "ImageToText");
        imguri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imguri);
        startActivityForResult(cameraIntent, imgcamera_code);
    }

    private void requestpermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA}, permission_request_code);
    }

    private boolean permission() {

        Boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        Boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == imggallery_code) {
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(this);
            }
            if (requestCode == imgcamera_code) {
                CropImage.activity(imguri).setGuidelines(CropImageView.Guidelines.ON).start(this);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resulturi = result.getUri();
                mImageView.setImageURI(resulturi);

                BitmapDrawable drawable = (BitmapDrawable) mImageView.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                //image to text
                TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                if (!textRecognizer.isOperational()) {
                    Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = textRecognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myitems = items.valueAt(i);
                        sb.append(myitems.getValue());
                        sb.append("\n");
                    }

                    mEditText.setText(sb.toString());
                    String strtext = mEditText.getText().toString();
                    FirebaseLanguageIdentification identification = FirebaseNaturalLanguage.getInstance().getLanguageIdentification();
                    identification.identifyLanguage(strtext).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            mTextView.setVisibility(View.VISIBLE);
                            for (int i = 0; i < langcode.length; i++) {
                                if (langcode[i].trim().equals(s.trim())) {
                                    mTextView.setText("Detected Language: " + langname[i]);
                                    break;
                                }
                            }
                        }
                    });
                }
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = new Exception();
                Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Tap again to exit", Toast.LENGTH_SHORT).show();
        }

        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about: {
                AlertDialog.Builder alrt = new AlertDialog.Builder(this);
                alrt.setTitle("About app:");
                alrt.setMessage("Having something right in front of your eyes but still not able to understand it? Do you feel it's the language barrier stopping you?\n" +
                        "\n" +
                        "With an idea of overcoming such language barriers and helping the people understand every language for effective communication, we have conceptualised the \"Image text translator\" that helps our users to effortlessly capture, crop and translate image text in multiple languages.\n" +
                        "\n" +
                        "Please do rate us on Play store.");
                AlertDialog alertDialog = alrt.create();
                alertDialog.show();
                break;
            }
            case R.id.rateus: {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=com.Kalaathon.imagetexttranslator"));
                startActivity(intent);
                break;
            }
            case R.id.signout: {
                FirebaseAuth.getInstance().signOut();
                //LoginManager.getInstance().logOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, gso);
                googleSignInClient.signOut();
                Toast.makeText(this, "Successfully Signed Out!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
