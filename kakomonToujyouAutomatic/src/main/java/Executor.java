import java.io.IOException;

public class Executor {
    public static void main(String[] args) throws IOException {
        System.out.println("-------------Started----------");
        System.setProperty("appConfPath", "D:\\myjava\\kakomonToujyouAutomatic\\Config_kakomonToujyouAutomatic");
        ApplicationContext.init();
        AutomaticImp.automaticImplement();
    }
}
