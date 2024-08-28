package excelMerger;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class ExcelMergetTest {

    private static final String LOCAL_TEST_FOLDER = "D:\\myjava\\ttt";

    private static Path localTestFileDirPath;

    @BeforeClass
    public static void createTestDirectory() throws IOException {
        localTestFileDirPath = Files.createDirectories(FileSystems.getDefault().getPath(LOCAL_TEST_FOLDER));

        createDirIfNotExist(String.valueOf(localTestFileDirPath.resolve("work")));

        createDirIfNotExist(String.valueOf(localTestFileDirPath.resolve("output")));

    }



    @Before
    public void setUp() throws Exception{
        // Assuming the field is in a class named Controller
        Class<?> clazz = ExcelMerger.class;

        Field sheetHeaderMapField = clazz.getDeclaredField("sheetHeaderMap");
        Field mergedSheetNamesField = clazz.getDeclaredField("mergedSheetNames");

        sheetHeaderMapField.setAccessible(true);
        mergedSheetNamesField.setAccessible(true);

        // Get the actual Map and Set objects from the fields in ExcelMerger class
        Map<String, List<String>> sheetHeaderMap = (Map<String, List<String>>) sheetHeaderMapField.get(null);
        Set<String> mergedSheetNames = (Set<String>) mergedSheetNamesField.get(null);

        // Clear the collections directly in ExcelMerger
        if (sheetHeaderMap != null) {
            sheetHeaderMap.clear();  // This clears the actual static Map in ExcelMerger class
        }
        if (mergedSheetNames != null) {
            mergedSheetNames.clear();  // This clears the actual static Set in ExcelMerger class
        }
    }

    @After
    public void deleteTempFileInEachTest() throws Exception {
        this.deleteFilesInFolder(LOCAL_TEST_FOLDER);
    }

    @AfterClass
    public static void removeTestDirectory() throws IOException {

        FileUtils.deleteDirectory(localTestFileDirPath.toFile());

    }


    @Test
    public void combine2filescommon1sheet() throws Exception {

        System.out.println("==================test combine2filescommon1sheet start====================");

        createTestExcel1();

        createTestExcel2();

        Files.deleteIfExists(localTestFileDirPath.resolve("output\\merged.xlsx"));

        // 呼叫被測試的方法
        String[] args = {LOCAL_TEST_FOLDER + "\\work", LOCAL_TEST_FOLDER + "\\output\\merged.xlsx"};

        ExcelMerger.main(args);

        //assert Result


        String filePath = LOCAL_TEST_FOLDER + "\\output\\merged.xlsx";
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            assertEquals(1, workbook.getNumberOfSheets(), 0);

            Sheet sheet = workbook.getSheet("test1"); // Verify the sheet name

            // Assert sheet is not null
            assertNotNull("Sheet 'test1' should exist", sheet);

            // Check the header row
            Row headerRow = sheet.getRow(0);
            assertEquals("ECT", headerRow.getCell(0).getStringCellValue());
            assertEquals("AAA", headerRow.getCell(1).getStringCellValue());
            assertEquals("RTY", headerRow.getCell(2).getStringCellValue());

            // Check the second row
            Row secondRow = sheet.getRow(1);
            assertEquals(5, secondRow.getCell(0).getNumericCellValue(), 0);
            assertNull(secondRow.getCell(1));
            assertEquals(7, secondRow.getCell(2).getNumericCellValue(), 0);

            // Check the third row
            Row thirdRow = sheet.getRow(2);
            assertEquals("3B", thirdRow.getCell(0).getStringCellValue());
            assertEquals("kk", thirdRow.getCell(1).getStringCellValue());
            assertEquals("う", thirdRow.getCell(2).getStringCellValue());

            // Check the 4th row
            Row fourthRow = sheet.getRow(3);
            assertEquals(6, fourthRow.getCell(0).getNumericCellValue(), 0);
            assertNull(fourthRow.getCell(1));
            assertEquals(7, fourthRow.getCell(2).getNumericCellValue(), 0);

            // Check the 5th row
            Row fifthRow = sheet.getRow(4);
            assertEquals("4B", fifthRow.getCell(0).getStringCellValue());
            assertEquals("kk", fifthRow.getCell(1).getStringCellValue());
            assertEquals("え", fifthRow.getCell(2).getStringCellValue());


        }
        System.out.println("==================test combine2filescommon1sheet end====================");

    }

    @Test
    public void combine2filescommon1sheetotherDifferent() throws Exception {

        System.out.println("==================test combine2filescommon1sheetotherDifferent start====================");

        createTestExcel3();

        createTestExcel4();

        Files.deleteIfExists(localTestFileDirPath.resolve("output\\merged.xlsx"));

        // 呼叫被測試的方法
        String[] args = {LOCAL_TEST_FOLDER + "\\work", LOCAL_TEST_FOLDER + "\\output\\merged.xlsx"};

        ExcelMerger.main(args);

        //assert Result


        String filePath = LOCAL_TEST_FOLDER + "\\output\\merged.xlsx";
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            assertEquals(3, workbook.getNumberOfSheets(), 0);


            Sheet sheet1 = workbook.getSheet("test1"); // Verify the sheet name
            Sheet sheet2 = workbook.getSheet("test2");
            Sheet sheet3 = workbook.getSheet("test3");

            // Assert sheet is not null
            assertNotNull("Sheet 'test1' should exist", sheet1);
            assertNotNull("Sheet 'test2' should exist", sheet2);
            assertNotNull("Sheet 'test3' should exist", sheet3);

            //Assert sheet 1
            // Check the header row
            Row headerRow = sheet1.getRow(0);
            assertEquals("ECT", headerRow.getCell(0).getStringCellValue());
            assertEquals("AAA", headerRow.getCell(1).getStringCellValue());
            assertEquals("RTY", headerRow.getCell(2).getStringCellValue());

            // Check the second row
            Row assertedRow = sheet1.getRow(1);
            assertEquals(6, assertedRow.getCell(0).getNumericCellValue(), 0);
            assertNull(assertedRow.getCell(1));
            assertEquals(7, assertedRow.getCell(2).getNumericCellValue(), 0);

            // Check the third row
            assertedRow = sheet1.getRow(2);
            assertEquals("4B", assertedRow.getCell(0).getStringCellValue());
            assertEquals("kk", assertedRow.getCell(1).getStringCellValue());
            assertEquals("え", assertedRow.getCell(2).getStringCellValue());


            //Assert sheet 2
            // Check the header row
            assertedRow = sheet2.getRow(0);
            assertEquals("AAA", assertedRow.getCell(0).getStringCellValue());
            assertEquals("BBB", assertedRow.getCell(1).getStringCellValue());
            assertEquals("CCC", assertedRow.getCell(2).getStringCellValue());

            // Check the second row
            assertedRow = sheet2.getRow(1);
            assertEquals(6, assertedRow.getCell(0).getNumericCellValue(), 0);
            assertEquals("H5", assertedRow.getCell(1).getStringCellValue());
            assertEquals(7, assertedRow.getCell(2).getNumericCellValue(), 0);

            // Check the third row
            assertedRow = sheet2.getRow(2);
            assertEquals("TYU&", assertedRow.getCell(0).getStringCellValue());
            assertEquals("CC", assertedRow.getCell(1).getStringCellValue());
            assertEquals("は", assertedRow.getCell(2).getStringCellValue());

            // Check the fourth row
            assertedRow = sheet2.getRow(3);
            assertEquals("FF", assertedRow.getCell(0).getStringCellValue());
            assertEquals("OK", assertedRow.getCell(1).getStringCellValue());
            assertEquals("UII87", assertedRow.getCell(2).getStringCellValue());

            //Assert sheet 3
            // Check the header row
            assertedRow = sheet3.getRow(0);
            assertEquals("TRY", assertedRow.getCell(0).getStringCellValue());
            assertEquals("RRRR", assertedRow.getCell(1).getStringCellValue());
            assertEquals("TY&UI", assertedRow.getCell(2).getStringCellValue());

            // Check the second row
            assertedRow = sheet3.getRow(1);
            assertEquals(6, assertedRow.getCell(0).getNumericCellValue(), 0);
            assertEquals("H5", assertedRow.getCell(1).getStringCellValue());
            assertEquals(7, assertedRow.getCell(2).getNumericCellValue(), 0);

            // Check the third row
            assertedRow = sheet3.getRow(2);
            assertEquals("TYU&", assertedRow.getCell(0).getStringCellValue());
            assertEquals("CC", assertedRow.getCell(1).getStringCellValue());
            assertEquals("は", assertedRow.getCell(2).getStringCellValue());


        }

        System.out.println("==================test combine2filescommon1sheetotherDifferent end====================");
    }


    public void createTestExcel1() throws Exception {


        Files.deleteIfExists(localTestFileDirPath.resolve("work\\SDR01.xlsx"));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("test1");

        // Create header row
        Row headerRow = sheet.createRow(0); // Row 0 (A1)
        headerRow.createCell(0).setCellValue("ECT"); // Cell A1
        headerRow.createCell(1).setCellValue("AAA"); // Cell B1
        headerRow.createCell(2).setCellValue("RTY"); // Cell C1

        // Create second row
        Row secondRow = sheet.createRow(1); // Row 1 (A2)
        secondRow.createCell(0).setCellValue(5);   // Cell A2
        secondRow.createCell(2).setCellValue(7);   // Cell C2

        // Create third row
        Row thirdRow = sheet.createRow(2); // Row 2 (A3)
        thirdRow.createCell(0).setCellValue("3B"); // Cell A3
        thirdRow.createCell(1).setCellValue("kk"); // Cell B3
        thirdRow.createCell(2).setCellValue("う");  // Cell C3

        try (FileOutputStream fileOut = new FileOutputStream(String.valueOf(localTestFileDirPath.resolve("work\\SDR01.xlsx")))) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Closing the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Excel file created successfully.");
    }

    public void createTestExcel2() throws Exception {

        createDirIfNotExist(String.valueOf(localTestFileDirPath.resolve("work")));

        Files.deleteIfExists(localTestFileDirPath.resolve("work\\SDR02.xlsx"));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("test1");

        // Create header row
        Row headerRow = sheet.createRow(0); // Row 0 (A1)
        headerRow.createCell(0).setCellValue("ECT"); // Cell A1
        headerRow.createCell(1).setCellValue("AAA"); // Cell B1
        headerRow.createCell(2).setCellValue("RTY"); // Cell C1

        // Create second row
        Row secondRow = sheet.createRow(1); // Row 1 (A2)
        secondRow.createCell(0).setCellValue(6);   // Cell A2
        secondRow.createCell(2).setCellValue(7);   // Cell C2

        // Create third row
        Row thirdRow = sheet.createRow(2); // Row 2 (A3)
        thirdRow.createCell(0).setCellValue("4B"); // Cell A3
        thirdRow.createCell(1).setCellValue("kk"); // Cell B3
        thirdRow.createCell(2).setCellValue("え");  // Cell C3

        try (FileOutputStream fileOut = new FileOutputStream(String.valueOf(localTestFileDirPath.resolve("work\\SDR02.xlsx")))) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Closing the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Excel file created successfully.");
    }

    public void createTestExcel3() throws Exception {

        createDirIfNotExist(String.valueOf(localTestFileDirPath.resolve("work")));

        Files.deleteIfExists(localTestFileDirPath.resolve("work\\SDR03.xlsx"));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("test1");
        Sheet sheet2 = workbook.createSheet("test2");


        // Create header row
        Row headerRow = sheet1.createRow(0); // Row 0 (A1)
        headerRow.createCell(0).setCellValue("ECT"); // Cell A1
        headerRow.createCell(1).setCellValue("AAA"); // Cell B1
        headerRow.createCell(2).setCellValue("RTY"); // Cell C1

        // Create second row
        Row secondRow = sheet1.createRow(1); // Row 1 (A2)
        secondRow.createCell(0).setCellValue(6);   // Cell A2
        secondRow.createCell(2).setCellValue(7);   // Cell C2

        // Create third row
        Row thirdRow = sheet1.createRow(2); // Row 2 (A3)
        thirdRow.createCell(0).setCellValue("4B"); // Cell A3
        thirdRow.createCell(1).setCellValue("kk"); // Cell B3
        thirdRow.createCell(2).setCellValue("え");  // Cell C3
        //==============================================================
        // Create header row
        Row headerRow2 = sheet2.createRow(0); // Row 0 (A1)
        headerRow2.createCell(0).setCellValue("AAA"); // Cell A1
        headerRow2.createCell(1).setCellValue("BBB"); // Cell B1
        headerRow2.createCell(2).setCellValue("CCC"); // Cell C1

        // Create second row
        Row secondRow2 = sheet2.createRow(1); // Row 1 (A2)
        secondRow2.createCell(0).setCellValue(6);   // Cell A2
        secondRow2.createCell(1).setCellValue("H5");
        secondRow2.createCell(2).setCellValue(7);   // Cell C2

        // Create third row
        Row thirdRow2 = sheet2.createRow(2); // Row 2 (A3)
        thirdRow2.createCell(0).setCellValue("TYU&"); // Cell A3
        thirdRow2.createCell(1).setCellValue("CC"); // Cell B3
        thirdRow2.createCell(2).setCellValue("は");  // Cell C3


        try (FileOutputStream fileOut = new FileOutputStream(String.valueOf(localTestFileDirPath.resolve("work\\SDR03.xlsx")))) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Closing the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Excel file created successfully.");
    }

    public void createTestExcel4() throws Exception {

        createDirIfNotExist(String.valueOf(localTestFileDirPath.resolve("work")));

        Files.deleteIfExists(localTestFileDirPath.resolve("work\\SDR04.xlsx"));

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet1 = workbook.createSheet("test2");
        Sheet sheet2 = workbook.createSheet("test3");

        // Create header row
        Row headerRow = sheet1.createRow(0); // Row 0 (A1)
        headerRow.createCell(0).setCellValue("AAA"); // Cell A1
        headerRow.createCell(1).setCellValue("BBB"); // Cell B1
        headerRow.createCell(2).setCellValue("CCC"); // Cell C1

        // Create second row
        Row secondRow = sheet1.createRow(1); // Row 1 (A2)
        secondRow.createCell(0).setCellValue("FF");   // Cell A2
        secondRow.createCell(1).setCellValue("OK");   // Cell C2
        secondRow.createCell(2).setCellValue("UII87");
        //==============================================================
        // Create header row
        Row headerRow2 = sheet2.createRow(0); // Row 0 (A1)
        headerRow2.createCell(0).setCellValue("TRY"); // Cell A1
        headerRow2.createCell(1).setCellValue("RRRR"); // Cell B1
        headerRow2.createCell(2).setCellValue("TY&UI"); // Cell C1

        // Create second row
        Row secondRow2 = sheet2.createRow(1); // Row 1 (A2)
        secondRow2.createCell(0).setCellValue(6);   // Cell A2
        secondRow2.createCell(1).setCellValue("H5");
        secondRow2.createCell(2).setCellValue(7);   // Cell C2

        // Create third row
        Row thirdRow2 = sheet2.createRow(2); // Row 2 (A3)
        thirdRow2.createCell(0).setCellValue("TYU&"); // Cell A3
        thirdRow2.createCell(1).setCellValue("CC"); // Cell B3
        thirdRow2.createCell(2).setCellValue("は");  // Cell C3

        try (FileOutputStream fileOut = new FileOutputStream(String.valueOf(localTestFileDirPath.resolve("work\\SDR04.xlsx")))) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Closing the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Excel file created successfully.");
    }

    public static void createDirIfNotExist(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            Boolean result = directory.mkdirs();
        }
    }


    public void deleteFilesInFolder(String folderPath) {

        try {

            AtomicInteger deletedFileCount = new AtomicInteger(0);

            Files.walkFileTree(Paths.get(folderPath), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    deletedFileCount.incrementAndGet();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // Handle the exception
                    System.out.println("Error occured.");
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    // Do nothing for directories
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
