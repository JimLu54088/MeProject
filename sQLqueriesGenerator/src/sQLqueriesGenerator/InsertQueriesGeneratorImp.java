package sQLqueriesGenerator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Scanner;

public class InsertQueriesGeneratorImp {

	public void createInsertQuery(String excelFilePath, String tempoutputFilePath) throws IOException {

		try (FileInputStream fis = new FileInputStream(excelFilePath);
				Workbook workbook = new XSSFWorkbook(fis);
				FileWriter writer = new FileWriter(tempoutputFilePath)) {

			// 读取第一个工作表
			Sheet sheet = workbook.getSheetAt(0);

			// 获取表头
			Row headerRow = sheet.getRow(0);
//            int columnCount = headerRow.getPhysicalNumberOfCells();

			// 询问用户输入表名称
			System.out.print("Enter the table name: ");

			Scanner scanner = new Scanner(System.in);
			String tableName = scanner.next();

			System.out.print("Enter the maximum column count of the excel file (A is 1, B is 2.....) :");

			int columnCount = scanner.nextInt();

			// 遍历每一行（从第二行开始，跳过表头）
			for (int i = 1; i <= sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);

				// Actually don't need this, because we won't have blank row.
//                if (row == null)
//                    continue; // 跳过空行

				StringBuilder sql = new StringBuilder("INSERT INTO `").append(tableName).append("` (");

				// 添加列名称
				for (int j = 0; j < columnCount; j++) {
					Cell headerCell = headerRow.getCell(j);
					sql.append("`").append(headerCell.getStringCellValue()).append("`");
					if (j < columnCount - 1) {
						sql.append(", ");
					}
				}

				sql.append(") VALUES (");

				// 添加行数据
				for (int j = 0; j < columnCount; j++) {
					Cell cell = row.getCell(j);

					if (cell == null || cell.getCellTypeEnum() == CellType.BLANK) {
						sql.append("''");
					} else {
						switch (cell.getCellTypeEnum()) {
						case STRING:
							String value = cell.getStringCellValue().replace("'", "''");
							sql.append("'").append(value).append("'");
							break;

						// No need, because always use string format in excel;
//						case NUMERIC:
//							sql.append(cell.getNumericCellValue());
//							break;
						default:
							sql.append(cell.getNumericCellValue());
						}
					}

					if (j < columnCount - 1) {
						sql.append(", ");
					}
				}

				sql.append(");");

				// 写入到文件
				writer.write(sql.toString());
				writer.write(System.lineSeparator());
			}

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}

	public String replaceSQNullSQToNull(String outputSQLDirRoot, String tempoutputFilePath) throws IOException {

		String inputFilePath = tempoutputFilePath; // 输入文件路径
		String outputFilePath = outputSQLDirRoot + "\\finalOutputSql.sql"; // 输出文件路径

		try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
				BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

			String line;
			while ((line = reader.readLine()) != null) {
				// Replace 'NULL' to NULL
				line = line.replace("'NULL'", "NULL");
				// 写入到输出文件
				writer.write(line);
				writer.newLine();
			}

			System.out.println("Replacement 'NULL' to NULL complete. Check the output file.");

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		return outputFilePath;

	}

}
