package excelMerger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelMerger {

    private static Set<String> mergedSheetNames = new HashSet<>();

    private static Map<String, List<String>> sheetHeaderMap = new HashMap<>();

    public static void main(String[] args) {


        process(Arrays.asList("D:\\test_files\\excel_combined\\work\\SDR1.xlsx", "D:\\test_files\\excel_combined\\work\\SDR2.xlsx", "D:\\test_files\\excel_combined\\work\\SDR3.xlsx"), "D:\\test_files\\excel_combined\\output\\merged.xlsx");


    }


    public static void process(List<String> inputFiles, String outputFile) {
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {

            Workbook mergedWorkbook = new XSSFWorkbook();

            //Retrive all sheets names
            for (String inputFile : inputFiles) {
                try (FileInputStream fis = new FileInputStream(inputFile)) {
                    Workbook workbook = new XSSFWorkbook(fis);
                    getAllSheetsNames(workbook);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            Iterator itr = mergedSheetNames.iterator();

            System.out.println("Retrived sheet names all : ");

            while (itr.hasNext()) {
                System.out.println(itr.next());
            }


            //Create all sheets in target excel file
            for (String sheetName : mergedSheetNames) {
                mergedWorkbook.createSheet(sheetName);
            }

            // 遍历 mergedWorkbook 中的每个 Sheet
            for (int i = 0; i < mergedWorkbook.getNumberOfSheets(); i++) {
                Sheet sheet = mergedWorkbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();

                // 检查 sheetName 是否在 sheetHeaderMap 中
                if (sheetHeaderMap.containsKey(sheetName)) {
                    List<String> headers = sheetHeaderMap.get(sheetName);

                    // 在第一个位置（第一行）写入 Headers
                    Row headerRow = sheet.createRow(0);  // Excel 中的第一行，索引为0

                    for (int j = 0; j < headers.size(); j++) {
                        Cell cell = headerRow.createCell(j);
                        cell.setCellValue(headers.get(j));
                    }
                }
            }


            for (String inputFile : inputFiles) {
                try (FileInputStream fis = new FileInputStream(inputFile)) {
                    Workbook workbook = new XSSFWorkbook(fis);
                    mergeSheets(workbook, mergedWorkbook);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            mergedWorkbook.write(fos);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getAllSheetsNames(Workbook sourceWorkbook) {

        for (int i = 0; i < sourceWorkbook.getNumberOfSheets(); i++) {
            Sheet sourceSheet = sourceWorkbook.getSheetAt(i);
            String sheetName = sourceSheet.getSheetName();
            mergedSheetNames.add(sheetName);

            List<String> headers = new ArrayList<>();

            //Retrive headers for each sheet
            Row sourceRow = sourceSheet.getRow(0);

            if (sourceRow != null) {
                for (int j = 0; j <= sourceRow.getLastCellNum(); j++) {
                    Cell sourceCell = sourceRow.getCell(j);
                    if (sourceCell != null) {
                        headers.add(sourceCell.getStringCellValue());
                    }
                }

                sheetHeaderMap.put(sheetName, headers);
            }


        }

    }

    private static void mergeSheets(Workbook sourceWorkbook, Workbook targetWorkbook) {
        for (int i = 0; i < sourceWorkbook.getNumberOfSheets(); i++) {
            Sheet sourceSheet = sourceWorkbook.getSheetAt(i);
            String sourceSheetName = sourceSheet.getSheetName();

            for (int j = 0; j < targetWorkbook.getNumberOfSheets(); j++) {

                Sheet targetSheet = targetWorkbook.getSheetAt(j);
                String targetSheetName = targetSheet.getSheetName();

                if (sourceSheetName.equals(targetSheetName)) {
                    copySheetWithoutHeader(sourceSheet, targetSheet);
                }


            }

        }
    }

    private static void copySheet(Sheet sourceSheet, Sheet targetSheet) {
        for (int i = 0; i <= getActualLastRowNumUsingIterator(sourceSheet); i++) {
//            for (int i = 0; i <= 1; i++) {

            System.out.println("sheetName : " + sourceSheet.getSheetName());
            System.out.println("sheet lastRowNum : " + getActualLastRowNumUsingIterator(sourceSheet));

            Row sourceRow = sourceSheet.getRow(i);
            Row targetRow = targetSheet.createRow(getActualLastRowNumUsingIterator(targetSheet) + 1);

            System.out.println("sourceRow : " + sourceRow.getRowNum());
            System.out.println("targetRow + " + targetRow.getRowNum());


            if (sourceRow != null) {
                for (int j = 0; j <= sourceRow.getLastCellNum(); j++) {
                    Cell sourceCell = sourceRow.getCell(j);
                    if (sourceCell != null) {
                        Cell targetCell = targetRow.createCell(j, sourceCell.getCellTypeEnum());
                        copyCellValue(sourceCell, targetCell);
                    }
                }
            }
        }
    }

    private static void copySheetWithoutHeader(Sheet sourceSheet, Sheet targetSheet) {
        for (int i = 1; i <= getActualLastRowNumUsingIterator(sourceSheet); i++) {
            Row sourceRow = sourceSheet.getRow(i);
            Row targetRow = targetSheet.createRow(getActualLastRowNumUsingIterator(targetSheet) + 1);
            if (sourceRow != null) {
                for (int j = 0; j <= sourceRow.getLastCellNum(); j++) {
                    Cell sourceCell = sourceRow.getCell(j);
                    if (sourceCell != null) {
                        Cell targetCell = targetRow.createCell(j, sourceCell.getCellTypeEnum());
                        copyCellValue(sourceCell, targetCell);
                    }
                }
            }
        }
    }

    private static void copyCellValue(Cell sourceCell, Cell targetCell) {
        switch (sourceCell.getCellTypeEnum()) {
            case STRING:
                targetCell.setCellValue(sourceCell.getStringCellValue());
                break;
            case NUMERIC:
                targetCell.setCellValue(sourceCell.getNumericCellValue());
                break;
            case BOOLEAN:
                targetCell.setCellValue(sourceCell.getBooleanCellValue());
                break;
            case FORMULA:
                targetCell.setCellFormula(sourceCell.getCellFormula());
                break;
            case BLANK:
//                targetCell.setCellValue("");
                break;
            default:
                break;
        }
    }

    private static int getActualLastRowNumUsingIterator(Sheet sheet) {
        int lastRowNum = 0;
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isRowNotEmpty(row)) {
                lastRowNum = row.getRowNum();
            }
        }
        return lastRowNum;
    }

    private static boolean isRowNotEmpty(Row row) {
        for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
                return true;
            }
        }
        return false;
    }


}