package com.plugins.resume.utils;

import android.content.Context;
import android.os.Environment;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.plugins.database.helper.DataRow;
import com.plugins.database.tables.Company;
import com.plugins.database.tables.PersonalDetail;
import com.plugins.database.tables.SystemUsers;
import com.webdroid.R;
import com.webdroid.core.utils.BitmapUtils;
import com.webdroid.core.utils.ORes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResumePDFGenerator {
    private Context mContext;
    private int user_id = -1;
    private PersonalDetail personalDetail;
    private SystemUsers userObj;
    private DataRow user;
    private DataRow sysUser;
    private Company company;

    private List<DataRow> companies = new ArrayList<>();

    public ResumePDFGenerator(Context context, int user_id) {
        mContext = context;
        this.user_id = user_id;
        userObj = new SystemUsers(context);
        personalDetail = new PersonalDetail(context);
        company = new Company(context);
        sysUser = userObj.browse(user_id);
        user = personalDetail.browse("user_id = ?", new String[]{user_id + ""});
        companies = company.select("user_id = ?", new String[]{user_id + ""});
    }

    public String generate() {
        String root = Environment.getExternalStorageDirectory().toString();
        String path = root + "/resumes/" + new Slug().makeSlug(user.getString("full_name")) + ".pdf";
        File myDir = new File(root + "/resumes");
        myDir.mkdir();

        Document document = new Document(PageSize.A4);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();
            addPersonalDetails(writer, document);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    private void addPersonalDetails(PdfWriter writer, Document document) throws DocumentException {
        if (user != null) {
            document.addTitle("Resume for " + user.getString("full_name"));
            document.addAuthor(ORes.get(mContext)._s(R.string.app_name));
            Paragraph header = new Paragraph();
            header.setLeading(20);
            header.setAlignment(Element.ALIGN_LEFT);
            header.setFont(new Font(Font.FontFamily.HELVETICA, 26, Font.BOLD));
            header.add(user.getString("full_name") + "\n");
            header.setFont(new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL));
            header.add(sysUser.getString("email") + "\n");
            if (!user.getString("image").equals("false")) {
                writer.setCompressionLevel(0);
                try {
                    float diff = PageSize.A4.getWidth() * 70 / 100;
                    Image image = Image.getInstance(BitmapUtils.getBitmapBytes(user.getString("image")));
                    float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                            - document.rightMargin() - diff) / image.getWidth()) * 100;
                    image.scalePercent(scaler);
                    image.setAbsolutePosition((PageSize.A4.getWidth() - (document.topMargin() + image.getScaledWidth())),
                            (PageSize.A4.getHeight() - (document.rightMargin() + image.getScaledHeight())));
                    writer.getDirectContent().addImage(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (companies.size() > 0) {
                DataRow cmp = companies.get(companies.size() - 1);
                header.add(cmp.getString(Company.COL_DESIGNATION) + " at " +
                        cmp.getString(Company.COL_NAME));
            }
            header.add(Chunk.NEWLINE);
            header.add(Chunk.NEWLINE);
            header.setFont(new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD));
            header.add("\n\nSUMMARY\n\n");
            header.setFont(new Font(Font.FontFamily.HELVETICA, 16, Font.NORMAL));
            header.add(user.getString(PersonalDetail.COL_SUMMARY));
            document.add(header);
        }
    }


    private void addHR(Document document) throws DocumentException {
        LineSeparator line = new LineSeparator();
        document.add(new Chunk(line));
    }

}
