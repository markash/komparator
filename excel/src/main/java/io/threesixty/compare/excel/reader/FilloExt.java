package io.threesixty.compare.excel.reader;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.utils.FilenameUtils;
import io.threesixty.compare.util.FileSupplier;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FilloExt {
    private static final String XLS = "XLS";
    private static final String XLSX = "XLSX";
    private static final String XLSM = "XLSM";


    public static Connection getConnection(String fileLocation) throws FilloException {
        Objects.requireNonNull(fileLocation, "The file location is required");
        return getConnection(new File(fileLocation));
    }

    public static Connection getConnectionForResource(final String resourceLocation) throws FilloException {
        try {
            return getConnection(FileSupplier.forResource(resourceLocation));
        } catch (URISyntaxException e) {
            throw new FilloException(e.getMessage());
        }
    }

    public static Connection getConnection(final File file) throws FilloException {
        Objects.requireNonNull(file, "The file is required");
        try {
            Workbook workbook = getWorkbook(file);
            return new Connection(workbook, new FileInputStream(file), file.getAbsolutePath(), true);
        } catch (FileNotFoundException e) {
            throw new FilloException("Workbook is not found - " + file.getAbsolutePath());
        }
    }

    public static List<String> getSheetNames(final String fileLocation) throws FilloException {
        return getSheetNames(new File(fileLocation));
    }

    public static List<String> getSheetNames(final File file) throws FilloException {
        Workbook workbook = getWorkbook(file);
        return IntStream
                .rangeClosed(1, workbook.getNumberOfSheets())
                .mapToObj(workbook::getSheetName)
                .collect(Collectors.toList());
    }

    public static String getSqlFor(final String sheetName) {
        Objects.requireNonNull(sheetName, "The sheet name is required");
        return "SELECT * from " + sheetName;
    }

    private static Workbook getWorkbook(final File file) throws FilloException {
        Objects.requireNonNull(file);

        try {
            FileInputStream fis = new FileInputStream(file);
            switch (FilenameUtils.getExtension(file.getAbsolutePath()).toUpperCase()) {
                case XLS:
                    return new HSSFWorkbook(fis);
                case XLSX:
                    return new XSSFWorkbook(fis);
                case XLSM:
                    return new XSSFWorkbook(OPCPackage.open(fis));
                default:
                    throw new FilloException("Invalid file format - " + file.getAbsolutePath());
            }
        } catch (FileNotFoundException e) {
            throw new FilloException("Workbook is not found - " + file.getAbsolutePath());
        } catch (IOException | InvalidFormatException e) {
            throw new FilloException("Unable to connect workbook - " + file.getAbsolutePath());
        }
    }
}
