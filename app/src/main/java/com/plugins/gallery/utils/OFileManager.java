package com.plugins.gallery.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.RequiresPermission;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.webdroid.R;
import com.webdroid.core.utils.BitmapUtils;
import com.webdroid.core.utils.ORes;
import com.webdroid.core.wrapper.helper.utils.WDPluginArgs;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class OFileManager implements DialogInterface.OnClickListener {
    public static final String TAG = OFileManager.class.getSimpleName();
    public static final int REQUEST_CAMERA = 111;
    public static final int REQUEST_IMAGE = 112;
    public static final int REQUEST_AUDIO = 113;
    public static final int REQUEST_FILE = 114;
    public static final int REQUEST_ALL_FILE = 115;
    private static final int SINGLE_ATTACHMENT_STREAM = 115;
    private static final long IMAGE_MAX_SIZE = 1000000; // 1 MB
    private Context mContext = null;
    private String[] mOptions = null;
    private RequestType requestType = null;
    private Uri newImageUri = null;
    private boolean cropImageAfterSelect = false;
    private ChoiceOptionDialogCancelListener mChoiceOptionDialogCancelListener;


    public enum RequestType {
        CAPTURE_IMAGE, IMAGE, IMAGE_OR_CAPTURE_IMAGE, AUDIO, FILE, ALL_FILE_TYPE
    }

    public OFileManager(Context context) {
        mContext = context;
    }

    private String createFile(String name, byte[] fileAsBytes, String file_type) {

        InputStream is = new ByteArrayInputStream(fileAsBytes);
        String filename = name.replaceAll("[-+^:=, ]", "_");
        String file_path = OStorageUtils.getDirectoryPath(file_type) + "/" + filename;
        try {
            FileOutputStream fos = new FileOutputStream(file_path);
            byte data[] = new byte[1024];
            int count = 0;
            while ((count = is.read(data)) != -1) {
                fos.write(data, 0, count);
            }
            is.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file_path;
    }

    private void requestIntent(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        FileNameMap mime = URLConnection.getFileNameMap();
        String mimeType = mime.getContentTypeFor(uri.getPath());
        intent.setDataAndType(uri, mimeType);
        try {
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, ORes.get(mContext)._s(R.string.toast_no_activity_found_to_handle_file),
                    Toast.LENGTH_LONG).show();
        }
    }


    private boolean fileExists(Uri uri) {
        return new File(uri.getPath()).exists();
    }

    public Bitmap getBitmapFromURI(Uri uri) {
        Bitmap bitmap;
        if (!fileExists(uri) && atLeastKitKat()) {
            String path = getDocPath(uri);
            bitmap = BitmapUtils.getBitmapImage(mContext,
                    BitmapUtils.uriToBase64(Uri.fromFile(new File(path)), mContext.getContentResolver()));
        } else {
            bitmap = BitmapUtils.getBitmapImage(mContext,
                    BitmapUtils.uriToBase64(uri, mContext.getContentResolver()));
        }
        return bitmap;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getDocPath(Uri uri) {
        String wholeID = DocumentsContract.getDocumentId(uri);
        String id = wholeID.split(":")[1];
        String[] column = {MediaStore.Images.Media.DATA};
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = mContext.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel,
                new String[]{id}, null);
        String filePath = null;
        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    public boolean atLeastKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    @RequiresPermission(allOf = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    })
    public void requestForFile(RequestType type) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        switch (type) {
            case AUDIO:
                intent.setType("audio/*");
                requestIntent(intent, REQUEST_AUDIO);
                break;
            case IMAGE:
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                requestIntent(intent, REQUEST_IMAGE);
                break;
            case CAPTURE_IMAGE:
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Odoo Mobile Attachment");
                values.put(MediaStore.Images.Media.DESCRIPTION,
                        "Captured from Odoo Mobile App");
                newImageUri = mContext.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, newImageUri);
                intent.putExtra("crop", "true");
                requestIntent(intent, REQUEST_CAMERA);
                break;
            case IMAGE_OR_CAPTURE_IMAGE:
                requestDialog(type);
                break;
            case FILE:
                intent.setType("application/*");
                requestIntent(intent, REQUEST_FILE);
                break;
            case ALL_FILE_TYPE:
                intent.setType("*/*");
                requestIntent(intent, REQUEST_ALL_FILE);
                break;
        }
    }

    public void enableCroping(boolean cropImage) {
        cropImageAfterSelect = cropImage;
    }

    public WDPluginArgs getURIDetails(Uri uri) {
        WDPluginArgs values = new WDPluginArgs();
        ContentResolver mCR = mContext.getContentResolver();
        if (uri.getScheme().equals("content")) {
            Cursor cr = mCR.query(uri, null, null, null, null);
            int nameIndex = cr.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int fileSize = cr.getColumnIndex(OpenableColumns.SIZE);
            if (cr.moveToFirst()) {
                values.put("name", cr.getString(nameIndex));
                values.put("datas_fname", values.get("name"));
                values.put("file_size", Long.toString(cr.getLong(fileSize)));
                values.put("datas", BitmapUtils.uriToBase64(uri, mCR));
                String path = getPath(uri);
                if (path != null) {
                    values.put("file_size", new File(path).length() + "");
                }
            }
        }
        if (uri.getScheme().equals("file")) {
            File file = new File(uri.toString());
            values.put("name", file.getName());
            values.put("datas_fname", values.get("name"));
            values.put("file_size", Long.toString(file.length()));
            Uri fileUri = Uri.fromFile(file);
            values.put("datas", BitmapUtils.uriToBase64(fileUri, mCR));
        }
        values.put("file_uri", uri.toString());
        values.put("scheme", uri.getScheme());
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String type = mime.getMimeTypeFromExtension(mime
                .getExtensionFromMimeType(mCR.getType(uri)));
        values.put("file_type", (type == null) ? uri.getScheme() : type);
        values.put("type", type);
        return values;
    }

    public String getPath(Uri uri) {
        ContentResolver mCR = mContext.getContentResolver();
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = mCR.query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public WDPluginArgs handleResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA:
                    WDPluginArgs values = getURIDetails(newImageUri);
                    values.put("datas", BitmapUtils.uriToBase64(newImageUri,
                            mContext.getContentResolver(), true));
                    return values;
                case REQUEST_IMAGE:
                    values = getURIDetails(data.getData());
                    values.put("datas", BitmapUtils.uriToBase64(data.getData(),
                            mContext.getContentResolver(), true));
                    return values;
                case REQUEST_ALL_FILE:
                default:
                    return getURIDetails(data.getData());
            }
        }
        return null;
    }

    private void requestIntent(Intent intent, int requestCode) {
        try {
            ((Activity) mContext).startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            makeText(mContext, "No Activity Found to handle request",
                    LENGTH_SHORT).show();
        }
    }

    private void requestDialog(RequestType type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        switch (type) {
            case IMAGE_OR_CAPTURE_IMAGE:
                requestType = type;
                mOptions = new String[]{"Select Image", "Capture Image"};
                break;
        }
        builder.setSingleChoiceItems(mOptions, -1, this);
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mChoiceOptionDialogCancelListener != null) {
                    mChoiceOptionDialogCancelListener.onDialogCancel();
                }
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (requestType) {
            case IMAGE_OR_CAPTURE_IMAGE:
                requestForFile((which == 0) ? RequestType.IMAGE : RequestType.CAPTURE_IMAGE);
                break;
        }
        dialog.dismiss();
    }

    public void setChoiceOptionDialogCancelListener(ChoiceOptionDialogCancelListener listener) {
        mChoiceOptionDialogCancelListener = listener;
    }

    public interface ChoiceOptionDialogCancelListener {
        void onDialogCancel();
    }
}