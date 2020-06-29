package com.myapp.emra.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.myapp.emra.R;
import com.myapp.emra.networking.response.RespCustomerDetails;
import com.myapp.emra.networking.response.RespImage;
import com.myapp.emra.networking.response.RespSubmitForm;
import com.myapp.emra.networking.retrofit.ApiConfig;
import com.myapp.emra.networking.retrofit.RetrofitClient;
import com.myapp.emra.networking.retrofit.RetrofitRespondListener;
import com.myapp.emra.utils.LoadingDialog;
import com.myapp.emra.utils.Storage;
import com.myapp.emra.utils.Utilities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, RetrofitRespondListener {

    private String TAG = "===MainActivity";
    private Context context = this;
    private EditText et_customerID, et_kwhManual, et_baseURL;
    private TextView tv_meterNumber, tv_customerName, tv_unitUP, tv_tarif, tv_power, tv_kwhAutoDetect;
    private AppCompatImageView iv_sendCustomerID, iv_autoImg, iv_captureMeter, iv_saveBaseURL;
    private ProgressBar pb_sendCustomerID;
    private Button btn_submitForm;
    private LinearLayout ll_detailsLayout, ll_submitLayout;
    private RelativeLayout rl_baseUrlLayout, rl_meterTypeLayout;
    private LoadingDialog loadingDialog;
    private Spinner sp_mode, sp_meterType;
    private Storage storage;
    boolean isBaseUrlShowing = false;
    private String str_mode, str_imgPath, str_resultImgPath, str_regionProbability;
    private boolean closeApp = true;
    private final int CAMERA_REQUEST = 0;
    private final int GALLERY_PICTURE = 1;
    private String selectedMode = "0", selectedMeterType = null;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupModes();
        setupMeterType();
        runtimePermissions();
    }


    private void initViews() {
        loadingDialog = new LoadingDialog(context);
        storage = new Storage(context);
        et_customerID = findViewById(R.id.et_customerID);
        et_kwhManual = findViewById(R.id.et_kwhManual);
//        et_baseURL = findViewById(R.id.et_baseURL);
        tv_meterNumber = findViewById(R.id.tv_meterNumber);
        tv_customerName = findViewById(R.id.tv_customerName);
        tv_unitUP = findViewById(R.id.tv_unitUP);
        tv_tarif = findViewById(R.id.tv_tarif);
        tv_power = findViewById(R.id.tv_power);
        tv_kwhAutoDetect = findViewById(R.id.tv_kwhAutoDetect);
        iv_sendCustomerID = findViewById(R.id.iv_sendCustomerID);
        iv_captureMeter = findViewById(R.id.iv_captureMeter);
        iv_autoImg = findViewById(R.id.iv_autoImg);
//        iv_saveBaseURL = findViewById(R.id.iv_saveBaseURL);
        pb_sendCustomerID = findViewById(R.id.pb_sendCustomerID);
        btn_submitForm = findViewById(R.id.btn_submitForm);
        ll_detailsLayout = findViewById(R.id.ll_detailsLayout);
        ll_submitLayout = findViewById(R.id.ll_submitLayout);
//        rl_baseUrlLayout = findViewById(R.id.rl_baseUrlLayout);
        rl_meterTypeLayout = findViewById(R.id.rl_meterTypeLayout);
        sp_mode = findViewById(R.id.sp_mode);
        sp_meterType = findViewById(R.id.sp_meterType);

        //adding listeners
        iv_sendCustomerID.setOnClickListener(this);
//        iv_saveBaseURL.setOnClickListener(this);
        iv_captureMeter.setOnClickListener(this);
        btn_submitForm.setOnClickListener(this);

        sp_mode.setOnItemSelectedListener(this);
        sp_meterType.setOnItemSelectedListener(this);

    }

    private void setupModes() {
        List<String> list = new ArrayList<String>();
//        list.add("none");
        list.add("0");
        list.add("1");
        list.add("2");

        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_mode.setAdapter(adapter);
    }


    private void setupMeterType() {
        List<String> list = new ArrayList<String>();
        list.add("reading");
        list.add("digital_reading");

        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_meterType.setAdapter(adapter);
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private void submitMeterImg(MultipartBody.Part body) {
        closeApp = false;
        loadingDialog.show();

        RequestBody meterType = null;
        if (selectedMeterType != null) {
            meterType = RequestBody.create(MediaType.parse("text/plain"), selectedMeterType);
        }
        Call<RespImage> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class).API_submitMeterImg(body, Integer.parseInt(selectedMode), meterType);
        RetrofitClient.callRetrofit(apiCall, "METER_IMG_SUBMIT", this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_history) {
            startActivity(new Intent(context, HistoryActivity.class));
            return true;
        } else if (itemId == R.id.action_settings) {
            startActivity(new Intent(context, SettingsActivity.class));


//            if (isBaseUrlShowing) {
//                rl_baseUrlLayout.setVisibility(View.GONE);
//                isBaseUrlShowing = false;
//            } else {
//                rl_baseUrlLayout.setVisibility(View.VISIBLE);
//                isBaseUrlShowing = true;
//                et_baseURL.setText(storage.getBaseURL());
//            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_sendCustomerID: {
                String str_customerID = et_customerID.getText().toString().trim();
                if (str_customerID.isEmpty()) {
                    Toast.makeText(this, "Enter customer ID", Toast.LENGTH_SHORT).show();
                } else fetchCustomerDetails(str_customerID);
            }
            break;

            case R.id.iv_captureMeter:
                if (storage.getImgSelection()) {
                    askForImgSelection();
                } else onlyCamCapture();

                break;

            case R.id.btn_submitForm: {
                String str_customerID = et_customerID.getText().toString().trim();
                String str_kwhPredicted = tv_kwhAutoDetect.getText().toString().trim();
                String str_kwhManual = et_kwhManual.getText().toString().trim();
                if (str_kwhManual.isEmpty()) {
                    Toast.makeText(this, "Fields must not empty", Toast.LENGTH_SHORT).show();
                } else
                    submitForm(str_customerID, str_regionProbability, str_kwhPredicted, str_kwhManual);
            }
            break;

//            case R.id.iv_saveBaseURL: {
//                Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show();
//                storage.saveBaseURL(et_baseURL.getText().toString().trim());
//            }
//            break;
        }
    }


    private void fetchCustomerDetails(String str_customerID) {
        iv_sendCustomerID.setVisibility(View.GONE);//hide send btn
        pb_sendCustomerID.setVisibility(View.VISIBLE); //show loader
        //reqst submit
        Call<RespCustomerDetails> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_customerDetails(Utilities.API_CLIENT + str_customerID);
        RetrofitClient.callRetrofit(apiCall, "CUSTOMER_DETAILS", this);

    }

    private void submitForm(String str_customerID, String predictionID, String str_kwhPredicted, String str_kwhManual) {
        long sysTimestamp = System.currentTimeMillis();
        String date = Utilities.formatDateTimeFromTS(sysTimestamp, "yyyy-MM-dd");
        String time = Utilities.formatDateTimeFromTS(sysTimestamp, "hh:mm a");

        loadingDialog.show();
        Call<RespSubmitForm> apiCall = RetrofitClient.getRetrofitInstance(context).create(ApiConfig.class)
                .API_submitForm(str_mode, str_customerID, str_imgPath, date, date, time, str_resultImgPath, str_resultImgPath,
                        str_kwhPredicted, str_kwhManual, str_regionProbability);
        RetrofitClient.callRetrofit(apiCall, "FORM_SUBMITTED", this);
    }

    @Override
    public void onRetrofitSuccess(Response<?> response, String requestName) {
        switch (requestName) {
            case "CUSTOMER_DETAILS": {
                responseCustomerDetails(response);
                iv_sendCustomerID.setVisibility(View.VISIBLE);//show send btn
                pb_sendCustomerID.setVisibility(View.GONE); //hide loader
            }
            break;

            case "METER_IMG_SUBMIT":
                responseMeterImg(response);
                break;

            case "FORM_SUBMITTED":
                responseFormSubmitted(response);
                break;
        }

        loadingDialog.dismiss();
    }


    @Override
    public void onRetrofitFailure(String responseError, String requestName) {
        if (responseError.equalsIgnoreCase("timeout")) {
            Toast.makeText(context, "Timeout", Toast.LENGTH_SHORT).show();
        } else if (responseError.contains("Failed to connect")) {
            Toast.makeText(context, "Failed to connect", Toast.LENGTH_SHORT).show();
        }

        iv_sendCustomerID.setVisibility(View.VISIBLE);//show send btn
        pb_sendCustomerID.setVisibility(View.GONE); //hide loader

        loadingDialog.dismiss();

    }


    private void responseCustomerDetails(Response<?> response) {
        RespCustomerDetails respCustomerDetails = (RespCustomerDetails) response.body(); //main obj
        if (response.code() == 200) {
            tv_meterNumber.setText(respCustomerDetails.getCustomerMeterNo());
            tv_customerName.setText(respCustomerDetails.getCustomerName());
            tv_unitUP.setText(respCustomerDetails.getCustomerUnitUP());
            tv_tarif.setText(respCustomerDetails.getCustomerTarif());
            tv_power.setText(respCustomerDetails.getCustomerPower());
            ll_detailsLayout.setVisibility(View.VISIBLE); //show details
        } else if (response.code() == 404) {
            Toast.makeText(context, "Not found! Re-enter correct ClientID", Toast.LENGTH_LONG).show();
        }
    }

    private void responseMeterImg(Response<?> response) {
        RespImage respImage = (RespImage) response.body(); //main obj
        if (response.code() == 200) { //created
            str_mode = respImage.getMode();
            str_imgPath = respImage.getImgSrcFile();
            str_resultImgPath = respImage.getResultImgPath();
            tv_kwhAutoDetect.setText(respImage.getReadingValue());
            str_regionProbability = respImage.getRegionProbability();

            String readingImg = respImage.getResultImgPath();
            if (readingImg != null) {
                Utilities.loadImage(context, str_resultImgPath, iv_autoImg);
                iv_autoImg.setVisibility(View.VISIBLE); //if gone
                ll_submitLayout.setVisibility(View.VISIBLE);
            } else {
                if (str_mode.equals("2")) {
                    iv_autoImg.setVisibility(View.GONE);
                    ll_submitLayout.setVisibility(View.VISIBLE);

                } else
                    Toast.makeText(context, "Prediction Failed! Please take the picture straight, not angled", Toast.LENGTH_LONG).show();

            }
        } else if (response.code() == 400)
            Toast.makeText(context, "Prediction Failed!", Toast.LENGTH_LONG).show();

    }


    private void responseFormSubmitted(Response<?> response) {
        RespSubmitForm respSubmitForm = (RespSubmitForm) response.body(); //main obj
        if (response.code() == 201) { //created
            Toast.makeText(context, "Submitted successfully!", Toast.LENGTH_SHORT).show();
            ll_detailsLayout.setVisibility(View.GONE);
            ll_submitLayout.setVisibility(View.GONE);
            resetForm();
        }
    }

    private void resetForm() {
        et_customerID.setText("");
        iv_captureMeter.setImageResource(R.drawable.ic_camera);
        iv_captureMeter.setPadding(100, 100, 100, 100);
    }

    private void askForImgSelection() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Intent pictureActionIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pictureActionIntent, GALLERY_PICTURE);
            }
        });

        myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                });
        myAlertDialog.show();
    }


    private void onlyCamCapture() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GALLERY_PICTURE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    iv_captureMeter.setPadding(0, 0, 0, 0);
                    iv_captureMeter.setImageURI(selectedImage);

                    File file = new File(getRealPathFromURI(selectedImage));
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("image_source_file", file.getName(), requestFile);

                    submitMeterImg(body);
                }
                break;

            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    iv_captureMeter.setPadding(0, 0, 0, 0);
                    iv_captureMeter.setImageBitmap(photo);

                    Uri tempUri = getImageUri(context, photo);

                    File file = new File(getRealPathFromURI(tempUri));
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    long currentTS = System.currentTimeMillis();
                    String fileName = Utilities.formatDateTimeFromTS(currentTS, "yyyyMM") + "_" +
                            et_customerID.getText().toString().trim() + "_" +
                            Utilities.formatDateTimeFromTS(currentTS, "yyyyMMdd") +
                            "_" + Utilities.formatDateTimeFromTS(currentTS, "HHMMSS") + "_"
                            + tv_unitUP.getText().toString() + "_" + tv_tarif.getText().toString() + "." +
                            getFileExtension(tempUri);

                    Log.d(TAG, "onActivityResult: " + fileName);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("image", fileName, requestFile);
                    submitMeterImg(body);
                }
                break;
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void runtimePermissions() {
        if (Build.VERSION.SDK_INT >= 23
                && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {

                askDialogIP();

            } else runtimePermissions();
        }
    }

    private void askDialogIP() {
        final EditText editText = new EditText(this);
//        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setHint("x.x.x.x:xxxx");
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("IP Address (Optional)")
                .setCancelable(false)
                .setView(editText)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Saved!", Toast.LENGTH_SHORT).show();
                        storage.saveBaseURL(editText.getText().toString().trim());
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (closeApp) {
            super.onBackPressed();
        } else {
            ll_submitLayout.setVisibility(View.GONE);
            iv_captureMeter.setImageResource(R.drawable.ic_camera);
            iv_captureMeter.setPadding(100, 100, 100, 100);

            closeApp = true;
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()) {
            case R.id.sp_mode: {
                selectedMode = sp_mode.getSelectedItem().toString();
                if (selectedMode.equals("2")) {
                    rl_meterTypeLayout.setVisibility(View.VISIBLE);
                    selectedMeterType = "reading"; //default
                } else {
                    rl_meterTypeLayout.setVisibility(View.GONE);
                    selectedMeterType = null;
                }
            }
            break;

            case R.id.sp_meterType:
                selectedMeterType = sp_meterType.getSelectedItem().toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}