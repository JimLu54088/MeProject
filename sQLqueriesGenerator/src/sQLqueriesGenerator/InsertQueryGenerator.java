package sQLqueriesGenerator;


import java.io.File;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


//This program is for Maria DB

public class InsertQueryGenerator {

    private static String excelFilePath = ""; // 修改為你的Excel文件路徑
    private static String outputSQLDirRoot = "";

    private static String lastRow = "";


    public static void main(String[] args) throws IOException {

        int intLastRow;

        try {

            if (args.length != 3) {
                System.err.println(
                        "ERROR: excelFilePath, outputSQLDirRoot, lastRow is required.");
                System.exit(-1);
            }


            excelFilePath = args[0];

            Path pathExcelFilePath = Paths.get(excelFilePath);

            Files.deleteIfExists(pathExcelFilePath.resolve("finalOutputSql.sql"));


            outputSQLDirRoot = args[1];

            lastRow = args[2];

            intLastRow = Integer.parseInt(lastRow);


            Path diroutputSQLDirRoot = Paths.get(outputSQLDirRoot);
            if (!Files.exists(diroutputSQLDirRoot)) {
                Files.createDirectory(diroutputSQLDirRoot);
            }


        } catch (Exception e) {
            e.printStackTrace();

            throw e;
        }

        InsertQueriesGeneratorImp imp = new InsertQueriesGeneratorImp();

        String tempoutputFilePath = outputSQLDirRoot + "\\output_wrk.sql";


        imp.createInsertQuery(excelFilePath, tempoutputFilePath, intLastRow);

        // Replace 'NULL' to NULL
        String outputFilePath = imp.replaceSQNullSQToNull(outputSQLDirRoot, tempoutputFilePath);

        System.out.println("SQL statements have been written to " + outputFilePath);

        // Delete work temp outputFile
        File tempoutputFile = new File(tempoutputFilePath);
        boolean istempoutputFileDeleted = tempoutputFile.delete();

        if (istempoutputFileDeleted) {
            System.out.println("temp work output sql file:" + tempoutputFilePath + " is deleted.");
        }

    }


}
