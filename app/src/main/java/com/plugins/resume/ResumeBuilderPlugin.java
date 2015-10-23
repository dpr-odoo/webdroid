package com.plugins.resume;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.plugins.resume.utils.ResumePDFGenerator;
import com.webdroid.core.utils.OPreferenceManager;
import com.webdroid.core.wrapper.helper.WDPluginHelper;
import com.webdroid.core.wrapper.helper.utils.WDPluginArgs;

public class ResumeBuilderPlugin extends WDPluginHelper {
    public static final String KEY_FIRST_LAUNCH = "key_first_launch";
    private OPreferenceManager pref;
    private ResumePDFGenerator pdfGenerator;

    public ResumeBuilderPlugin(Context context) {
        super(context);
        pref = new OPreferenceManager(context);
    }

    public boolean isFirstLaunch(WDPluginArgs args) {
        return pref.getBoolean(KEY_FIRST_LAUNCH, true);
    }

    public void download(WDPluginArgs args) {
        pdfGenerator = new ResumePDFGenerator(getContext(), args.getInt("user_id"));
        String path = pdfGenerator.generate();
        Uri file = Uri.parse(path);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(file.toString()));
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(file, mimeType);
            getContext().startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
