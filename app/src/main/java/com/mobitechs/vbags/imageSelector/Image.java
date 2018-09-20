package com.mobitechs.vbags.imageSelector;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mobitechs.vbags.Add_Product;
import com.mobitechs.vbags.Notification_Add;
import com.mobitechs.vbags.R;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.zelory.compressor.Compressor;

public class Image {
    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;
    private static final int PIC_CAMERA_CROP = 3;
    private static final int PIC_GALLERY_CROP = 4;
    private static final int CAMERA_PERMISSION_REQUEST = 5;
    private static final int READ_STORAGE_PERMISSION_REQUEST = 6;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 7;

    public android.support.v7.app.AlertDialog alertDialog;
    public ArrayAdapter<String> dialogAdapter;

    String currentImagePath;

    Bitmap imageToShow;
    String timeStamp;
    File storageDir;
    File originalFile;
    File cropFile;
    String imageBase64String = "";

    private Activity activity;
    String activityName = "";


    private final OnRecyclerSetImageListener onRecyclerSetImageListener;


    public Image(Add_Product gallery_event_images_add, String gallery, OnRecyclerSetImageListener onRecyclerSetImageListener) {
        this.activity = gallery_event_images_add;
        this.activityName = gallery;
        this.onRecyclerSetImageListener = onRecyclerSetImageListener;
    }

    public Image(Notification_Add notification_add, String Notification, Notification_Add onRecyclerSetImageListener) {
        this.activity = notification_add;
        this.activityName = Notification;
        this.onRecyclerSetImageListener = onRecyclerSetImageListener;
    }

    public interface OnRecyclerSetImageListener {

        void onRecyclerImageSet(Bitmap imageToShow, String firstImagePath,File cropFile);
    }

    public void getImage() {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestWriteStoragePermission();
        } else {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadStoragePermission();
            } else {
                createImageFormSelectImageDialogChooser();
            }
        }
    }

    private void createImageFormSelectImageDialogChooser() {
        dialogAdapter = new ArrayAdapter<String>(activity, android.R.layout.select_dialog_item) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setTextColor(Color.BLACK);
                text.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        activity.getResources().getDimension(R.dimen.alertDialogListNames));
                return view;
            }
        };

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            StrictMode.VmPolicy.Builder builder2 = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder2.build());
            builder2.detectFileUriExposure();
        }

        dialogAdapter.add("Take from Camera");
        dialogAdapter.add("Select from Gallery");
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(new android.support.v7.view.ContextThemeWrapper(activity, R.style.AlertDialogCustom));
        builder.setTitle("Select Image");
        builder.setAdapter(dialogAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    alertDialog.dismiss();
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestCameraPermission();
                    } else {
                        new SelectCameraImage().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                    }
                } else if (which == 1) {
                    alertDialog.dismiss();
                    new SelectGalleryImage().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
                }
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    public void getActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == CAMERA_REQUEST) {
                    originalFile = new File(currentImagePath);
                    imageCompress(originalFile);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    this.imageToShow = BitmapFactory.decodeFile(originalFile.getAbsolutePath(), bmOptions);

                    //setBitmapToImage(imageToShow);
                    doCropping(originalFile, PIC_CAMERA_CROP);
                }
                else if (requestCode == GALLERY_REQUEST) {
                    Uri uri = intent.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};
                    Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String imagePath = cursor.getString(columnIndex);
                    cursor.close();
                    File tempFile = new File(imagePath);
                    originalFile = new File(currentImagePath);
                    copyFile(tempFile, originalFile);
                    imageCompress(originalFile);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    this.imageToShow = BitmapFactory.decodeFile(originalFile.getAbsolutePath(), bmOptions);

                    //setBitmapToImage(imageToShow);
                    doCropping(originalFile, PIC_GALLERY_CROP);
                }
//                else if (requestCode == PIC_CAMERA_CROP) {
//                    Bundle extras = intent.getExtras();
//                    this.imageToShow = extras.getParcelable("data");
//                    String filename = currentImagePath.substring(currentImagePath.lastIndexOf("/") + 1);
//                    this.imageToShow = saveCropBitmap(imageToShow);
//                    setBitmapToImage(imageToShow);
//                }
//                else if (requestCode == PIC_GALLERY_CROP) {
//                    Bundle extras = intent.getExtras();
//                    this.imageToShow = extras.getParcelable("data");
//                    this.imageToShow = saveCropBitmap(imageToShow);
//                    setBitmapToImage(imageToShow);
//                }
                else if(requestCode == UCrop.REQUEST_CROP) {
                    final Uri resultUri = UCrop.getOutput(intent);
                    File tempFile = new File(resultUri.getPath());

                    this.imageToShow = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), resultUri);
                    this.imageToShow = saveCropBitmap(imageToShow);
                    setBitmapToImage(imageToShow);
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(activity, "Did not taken any image!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(e.getMessage(), "Error");
            Toast.makeText(activity, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private String createBase64StringFromImageFile(File originalFile) {
        Bitmap bitmap = BitmapFactory.decodeFile(originalFile.getAbsolutePath());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encodedImage;
    }

    private void setBitmapToImage(Bitmap imageToShow) {
        if (imageBase64String == "") {
            //firstImage.setImageBitmap(imageToShow);
            //firstImagePath = "data:image/png;base64,"+imageBase64String;
            imageBase64String = createBase64StringFromImageFile(new File(currentImagePath));
            onRecyclerSetImageListener.onRecyclerImageSet(imageToShow, imageBase64String, cropFile);
        }

    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }

    }

    public void imageCompress(File sourceFile) {
        Bitmap tempBitmap = null;
        try {
            tempBitmap = new Compressor(activity)
                    .setQuality(75)
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                    .compressToBitmap(sourceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(originalFile.exists()) {
            originalFile.delete();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            tempBitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos);
            byte[] bitmapdata = bos.toByteArray();

            try {
                FileOutputStream fos = new FileOutputStream(originalFile);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void doCropping(File image, int request_code) {

//        Intent cropIntent = new Intent(activity, CropImage.class);
//
//        cropIntent.putExtra("image-path", currentImagePath);
//        cropIntent.putExtra("crop", true);
//        cropIntent.putExtra("scale", true);
//        cropIntent.putExtra("aspectX", 1);
//        cropIntent.putExtra("aspectY", 1);
//        cropIntent.putExtra("outputX", 256);
//        cropIntent.putExtra("outputY", 256);
//        cropIntent.putExtra("return-data", true);

        try {
            //  activity.startActivityForResult(cropIntent, request_code);

            Uri imageUri = Uri.fromFile(image);

            UCrop.Options options = new UCrop.Options();
            options.setCompressionFormat(Bitmap.CompressFormat.PNG);
            options.setFreeStyleCropEnabled(true);
            options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
            options.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
            options.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
            options.setActiveWidgetColor(ContextCompat.getColor(activity, R.color.colorAccent));

            UCrop.of(imageUri, imageUri)
                    .withOptions(options)
                    .start(activity);
        } catch (Exception e) {
            Toast.makeText(activity, "Crop Error", Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap saveCropBitmap(Bitmap imageToShow) {
        FileOutputStream outStream = null;

        cropFile = new File(currentImagePath);
        if (cropFile.exists()) {
            cropFile.delete();
            cropFile = new File(storageDir + "/" + timeStamp + ".png");
        }
        try {
            outStream = new FileOutputStream(cropFile);
            imageToShow.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageToShow;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);

        File image = new File(storageDir + "/" + timeStamp + ".png");
        try {
            currentImagePath = image.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    private void requestReadStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            createImageFormSelectImageDialogChooser();
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST);
        }
    }

    private void requestWriteStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            createImageFormSelectImageDialogChooser();
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        }
    }

    public class SelectCameraImage extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    activity.startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                }
            }
            return null;
        }
    }

    public class SelectGalleryImage extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // Create intent to Open Image applications like Gallery_Title_List, Google Photos
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activity.startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
            return null;
        }
    }

    public void getRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {

        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new SelectCameraImage().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
            } else {
                Toast.makeText(activity, "CAMERA permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == WRITE_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestReadStoragePermission();
            } else {
                Toast.makeText(activity, "Write storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == READ_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createImageFormSelectImageDialogChooser();
            } else {
                Toast.makeText(activity, "Read storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}