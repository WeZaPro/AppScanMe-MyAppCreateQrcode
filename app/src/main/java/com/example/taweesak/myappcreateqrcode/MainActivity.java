package com.example.taweesak.myappcreateqrcode;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.EnumMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button btnGenText;

    EditText et_nameQrcode, et_surnameQrcode, et_phoneNumber, et_lineId, et_email, et_nameSurname;
    EditText et_carType,et_namePlate,et_country,et_dateTime,et_carBrand,et_carฺBodyNumber;
    EditText et_carTypeGen,et_namePlateGen,et_countryGen,et_dateTimeGen,et_carBrandGen,et_carฺBodyNumberGen;

    ImageView mv_qrcode, mv_image,qrcode_create;
    String name, surname, phone, lineid, email;
    private Bitmap qrImage;

    private MainActivity self;


    TextView tvQrName,tvQrSurName,tvQrPhone,tvQrLine,tvQrEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        self = this;

        et_carType = findViewById(R.id.et_carType);
        et_namePlate = findViewById(R.id.et_namePlate);
        et_country = findViewById(R.id.et_country);
        et_dateTime = findViewById(R.id.et_dateTime);
        et_carBrand = findViewById(R.id.et_carBrand);
        et_carฺBodyNumber = findViewById(R.id.et_carฺBodyNumber);

        et_carTypeGen = findViewById(R.id.et_carTypeGen);
        et_namePlateGen = findViewById(R.id.et_namePlateGen);
        et_countryGen = findViewById(R.id.et_countryGen);
        et_dateTimeGen = findViewById(R.id.et_dateTimeGen);
        et_carBrandGen = findViewById(R.id.et_carBrandGen);
        et_carฺBodyNumberGen = findViewById(R.id.et_carฺBodyNumberGen);

        mv_qrcode = findViewById(R.id.mv_qrcode);
        qrcode_create = findViewById(R.id.qrcode_create);


        btnGenText = findViewById(R.id.btnGenText);
        btnGenText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_carTypeGen.setText(et_carType.getText().toString().trim());
                et_namePlateGen.setText(et_namePlate.getText().toString().trim());
                et_countryGen.setText(et_country.getText().toString().trim());
                et_dateTimeGen.setText(et_dateTime.getText().toString().trim());
                et_carBrandGen.setText(et_carBrand.getText().toString().trim());
                et_carฺBodyNumberGen.setText(et_carฺBodyNumber.getText().toString().trim());

            }
        });


        qrcode_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                self.generateImage();
            }
        });
    }

    // gen barcode

    private void generateImage(){

        /*final String text = "{\"name\":\""+ tvQrName.getText().toString()+"\""
        + ",\""
        + "surname\":\""+ tvQrSurName.getText().toString()+"\""
        + ",\""
        + "phone\":\""+ tvQrPhone.getText().toString()+"\""
        + ",\""
        + "email\":\""+ tvQrEmail.getText().toString()+"\""
        + ",\""
        + "lineid\":\""+ tvQrLine.getText().toString()+"\"}";*/

        final String textGen = "{\"carType\":\""+ et_carTypeGen.getText().toString()+"\""
                + ",\""
                + "namePlate\":\""+ et_namePlateGen.getText().toString()+ ",\""
                + ",\""
                + "country\":\""+ et_countryGen.getText().toString()+ ",\""
                + ",\""
                + "dateTime\":\""+ et_dateTimeGen.getText().toString()+ ",\""
                + ",\""
                + "carBrand\":\""+ et_carBrandGen.getText().toString()+ ",\""
                + ",\""
                + "carฺBodyNumber\":\""+ et_carฺBodyNumberGen.getText().toString()+"\"}";


        if(textGen.trim().isEmpty()){
            alert("No Data QR Code");
            return;
        }


        //endEditing();
        showLoadingVisible(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int size = mv_qrcode.getMeasuredWidth();
                if (size > 1) {
                    //Log.e(tag, "size is set manually");
                    size = 260;
                }

                Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
                hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
                hintMap.put(EncodeHintType.MARGIN, 1);
                QRCodeWriter qrCodeWriter = new QRCodeWriter();
                try {
                    BitMatrix byteMatrix = qrCodeWriter.encode(textGen, BarcodeFormat.QR_CODE, size,
                            size, hintMap);
                    int height = byteMatrix.getHeight();
                    int width = byteMatrix.getWidth();
                    self.qrImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            qrImage.setPixel(x, y, byteMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }

                    self.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            self.showImage(self.qrImage);
                            self.showLoadingVisible(false);
                            //self.snackbar("QRCode telah dibuat");
                        }
                    });
                } catch (WriterException e) {
                    e.printStackTrace();
                    alert(e.getMessage());
                }
            }
        }).start();
    }

    private void showLoadingVisible(boolean visible) {
        if (visible) {
            showImage(null);
        }

    }

    private void showImage(Bitmap bitmap) {
        if (bitmap == null) {
            mv_qrcode.setImageResource(android.R.color.transparent);
            qrImage = null;
            //txtSaveHint.setVisibility(View.GONE);
        } else {
            mv_qrcode.setImageBitmap(bitmap);
            //txtSaveHint.setVisibility(View.VISIBLE);
        }
    }

    private void alert(String message) {
        AlertDialog dlg = new AlertDialog.Builder(self)
                .setTitle("QRCode Generator")
                .setMessage(message)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        dlg.show();
    }
}
