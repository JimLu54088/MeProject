public class AppUtil {

    private AppUtil() {
        //To prevent creating object.
    }

    public static String getSystemProperty(String propertyKey) {
        String systemPropertyValue = "";

        try {
            systemPropertyValue = System.getProperty(propertyKey);
        } catch (Exception ex) {

            throw ex;
        }

        return systemPropertyValue;
    }


}
