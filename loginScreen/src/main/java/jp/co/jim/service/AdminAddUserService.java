package jp.co.jim.service;

import jp.co.jim.controller.AdminAddUser;
import jp.co.jim.entity.UserEntity;
import jp.co.jim.exceptions.DuplicatedUserException;
import jp.co.jim.repository.AdminAddUserRepository;
import jp.co.jim.repository.AdminLoginRepository;
import org.apache.catalina.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AdminAddUserService {

    private static final Logger logger = LogManager.getLogger(AdminAddUserService.class);
    private static final String LOG_HEADER = "[" + AdminAddUserService.class.getSimpleName() + "] :: ";

    @Autowired
    private AdminAddUserRepository adminAddUserRepository;

    public int checkinsertingExisting(String username) {
        return adminAddUserRepository.checkinsertingExisting(username);
    }

    public void insertUser(String username, String password) {
        adminAddUserRepository.insertUser(username, password);
    }


    @Transactional
    public void uploadAndSaveUsers(MultipartFile file) throws Exception {
        List<UserEntity> users = parseExcelFile(file);
        logger.info(LOG_HEADER + "Count of rows in Excel: " + users.size());

        //Check duplicate in ExcelFile
        Boolean isDuplicatedInExcelFile = checkUserIdDuplicateInExcel(users);

        if (isDuplicatedInExcelFile) {
            logger.warn(LOG_HEADER + "Duplicated users in uploaded excel file");
            throw new DuplicatedUserException("Duplicated users in uploaded excel file");

        } else {
            //Check duplicate
            Boolean isDuplicatedInDatabase = false;
            for (UserEntity user : users) {
                int existingCount = adminAddUserRepository.checkinsertingExisting(user.getUser_id());
                if (existingCount != 0) {
                    logger.warn(LOG_HEADER + "Duplicated users in DataBase. User_id: " + user.getUser_id());
                    isDuplicatedInDatabase = true;
                }
            }
            //Insert into DB
            if (!isDuplicatedInDatabase) {
                for (UserEntity user : users) {
                    adminAddUserRepository.insertUser(user.getUser_id(), user.getUser_password());
                }

            } else {
                logger.warn(LOG_HEADER + "Duplicated users in DataBase");
                throw new DuplicatedUserException("Duplicated users in DataBase");
            }


        }


        for (UserEntity user : users) {
            adminAddUserRepository.insertUser(user.getUser_id(), user.getUser_password());
        }
    }

    private List<UserEntity> parseExcelFile(MultipartFile file) throws IOException {
        List<UserEntity> users = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header row
            }

            Cell userIdCell = row.getCell(0);
            if (userIdCell == null || userIdCell.getCellType() == CellType.BLANK) {
                break; // Stop reading if the A column is empty
            }

            UserEntity userEntity = new UserEntity();
            userEntity.setUser_id(row.getCell(0).getStringCellValue());
            userEntity.setUser_password(row.getCell(1).getStringCellValue());

            users.add(userEntity);
        }
        workbook.close();
        return users;
    }

    private boolean checkUserIdDuplicateInExcel(List<UserEntity> users) {
        boolean isUserIdDuplicatedInExcel = false;
        Set<String> userIdSet = new HashSet<>();
        for (UserEntity user : users) {
            if (!userIdSet.add(user.getUser_id())) {
                logger.warn(LOG_HEADER + "Duplicated users in Excel. User_id: " + user.getUser_id());
                isUserIdDuplicatedInExcel = true; // 如果无法添加到集合中，说明已经存在，即存在重复
            }
        }
        return isUserIdDuplicatedInExcel;
    }


}
