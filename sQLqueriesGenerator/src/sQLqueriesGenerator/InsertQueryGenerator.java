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


    public static void main(String[] args) throws IOException {

        try {

            if (args.length != 2) {
                System.err.println(
                        "ERROR: excelFilePath and outputSQLDirRoot is required.");
                System.exit(-1);
            }


            excelFilePath = args[0];

            Path pathExcelFilePath = Paths.get(excelFilePath);

            Files.deleteIfExists(pathExcelFilePath.resolve("finalOutputSql.sql"));


            outputSQLDirRoot = args[1];


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


        imp.createInsertQuery(excelFilePath, tempoutputFilePath);

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
