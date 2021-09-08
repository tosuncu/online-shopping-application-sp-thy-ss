package com.shoppinger.admin.category;

import com.shoppinger.admin.user.export.AbstractExporter;
import com.shoppinger.common.entity.Category;
import com.shoppinger.common.entity.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryCSVExporter extends AbstractExporter {
    public void export(List<Category> categoryList, HttpServletResponse response) throws IOException {
        super.setResponseHeader(response,"category_","text/csv",".csv");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String [] csvHeader = {"CategoryID", "Name"};
        String [] fieldMapping = {"id","name"};

        csvWriter.writeHeader(csvHeader);

        for (Category category : categoryList) {
            csvWriter.write(category,fieldMapping);
        }

        csvWriter.close();
    }
}
