package tool;

import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvParser;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRow;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class csvProcess {

    public void csvReadOperation() throws IOException {
        File file = new File("D:\\Coding\\java_projects\\VisualCOVID\\src\\main\\" +
                "resources\\vaccine_data_global.csv");
        CsvReader csvReader = new CsvReader();
        csvReader.setContainsHeader(true);

        CsvContainer csv = csvReader.read(file, StandardCharsets.UTF_8);
        for (CsvRow row : csv.getRows()) {
            System.out.println("First column of line: " + row.getField("Country_Region"));
        }
    }


    public static void csvReadOperation2() throws IOException {
        File file = new File("F:\\Excel\\customer-info.csv");
        CsvReader csvReader = new CsvReader();

        try (CsvParser csvParser = csvReader.parse(file, StandardCharsets.UTF_8)) {
            CsvRow row;
            while ((row = csvParser.nextRow()) != null) {
                System.out.println("Read line: " + row);
                System.out.println("First column of line: " + row.getField(0));
            }
        }
    }

    public static void main(String[] args) throws IOException {
        csvProcess process = new csvProcess();
        process.csvReadOperation();
    }
}
