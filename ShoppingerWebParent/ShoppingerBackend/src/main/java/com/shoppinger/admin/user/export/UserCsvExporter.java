package com.shoppinger.admin.user.export;

import com.shoppinger.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {

    public void export(List<User> userList, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"users_","text/csv",".csv");

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
