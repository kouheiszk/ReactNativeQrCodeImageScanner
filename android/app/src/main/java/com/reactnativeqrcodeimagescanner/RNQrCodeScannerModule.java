package com.reactnativeqrcodeimagescanner;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class RNQrCodeScannerModule extends ReactContextBaseJavaModule {
  public RNQrCodeScannerModule(ReactApplicationContext reactApplicationContext) {
    super(reactApplicationContext);
  }

  @Override
  public String getName() {
    return "RNQrCodeScanner";
  }

  @ReactMethod
  public void scanQrCodeUrl(String filePath, Promise promise) {
    InputImage image;
    try {
      image = InputImage.fromFilePath(getReactApplicationContext(), getUri(filePath));
    } catch (IOException e) {
      promise.reject("file_not_found", "The local file specified does not exist on the device.", e);
      return;
    }

    BarcodeScannerOptions options = new BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE).build();
    BarcodeScanner scanner = BarcodeScanning.getClient(options);

    scanner.process(image)
      .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
        @Override
        public void onSuccess(List<Barcode> barcodes) {
          Log.d("ReactNativeQrCodeImageScanner", barcodes.toString());

          if (!barcodes.isEmpty()) {
            for (Barcode barcode : barcodes) {
              int valueType = barcode.getValueType();
              if (valueType == Barcode.TYPE_URL) {
                String url = barcode.getUrl().getUrl();
                promise.resolve(url);
                return;
              }
            }
          }

          promise.reject("invalid_qr_code_object_error", "URL is not included in the scanned QR code.");
        }
      })
      .addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          promise.reject("barcode_scanner_process_error", "URL is not included in the scanned QR code.");
        }
      });
  }

  private static Uri getUri(String uri) {
    Uri parsed = Uri.parse(uri);

    if (parsed.getScheme() == null || parsed.getScheme().isEmpty()) {
      return Uri.fromFile(new File(uri));
    }

    return parsed;
  }
}
