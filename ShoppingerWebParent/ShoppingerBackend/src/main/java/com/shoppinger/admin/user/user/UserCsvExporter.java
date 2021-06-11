package com.shoppinger.admin.user.user;

import com.shoppinger.admin.user.AbstractExporter;
import com.shoppinger.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {

    public void export(List<User> userList, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"text/csv",".csv");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String [] csvHeader = {"User ID", "E-mail", "Firstname", "Lastname","Roles","Enabled"};
        String [] fieldMapping = {"id","email","firstName", "lastName","roles", "enabled"};

        csvWriter.writeHeader(csvHeader);

        for (User user : userList) {
            csvWriter.write(user,fieldMapping);
        }

        csvWriter.close();
    }
}
