import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationContext {

    private static Properties appProperties;

    private ApplicationContext() {
        //To prevent creating object.
    }

    public static void init() throws IOException {


        String confFolderPath = AppUtil.getSystemProperty(CommonConstants.CONF_FOLDER_SYSTEM_PROPERTY_NAME);
        InputStream queryParamFileIS = null;

        try (InputStream appPropertiesIS = new FileInputStream(confFolderPath + File.separator + CommonConstants.APP_PROPS_FILE_NAME);
        ) {

            appProperties = new Properties();
            appProperties.load(appPropertiesIS);

        } catch (Exception ex) {
            throw ex;
        } finally {
            if (queryParamFileIS != null) {
                try {
                    queryParamFileIS.close();
                } catch (IOException e) {
                    //No action.
                }
            }
        }
    }

    public static String getChromeDriverPath() {
        return appProperties.getProperty(CommonConstants.CHROME_DRIVE_PATH);
    }

    public static String getStartPage() {
        return appProperties.getProperty(CommonConstants.START_PAGE);
    }
    public static String getStartQuestionNo(){
        return appProperties.getProperty(CommonConstants.START_QUESTION_NO);
    }

}
