import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class AutomaticImp {

    public static void automaticImplement() {
        // 设置 ChromeDriver 路径
        System.setProperty("webdriver.chrome.driver", ApplicationContext.getChromeDriverPath());

        WebDriver driver = new ChromeDriver();

        try {
            // 1. 打开起始页面
            driver.get(ApplicationContext.getStartPage());
            Thread.sleep(1300); // 等待页面加载

            WebElement link = driver.findElement(By.xpath("//a[@href='q" + ApplicationContext.getStartQuestionNo() + ".html']"));
            link.click();

            int intStartQuestionNo = Integer.parseInt(ApplicationContext.getStartQuestionNo());

            for (int i = (intStartQuestionNo - 1); i < 100; i++) {
                //获取正确答案
                WebElement showAnswerButton = driver.findElement(By.id("showAnswerBtn"));
                showAnswerButton.click(); // 点击显示答案按钮
                Thread.sleep(500); // 等待答案显示

                WebElement correctAnswerElement = driver.findElement(By.id("answerChar"));
                String correctAnswer = correctAnswerElement.getText();
                System.out.println("The answer of question " + (i + 1) + " is: " + correctAnswer);

                //点击正确答案选项
                String answerButtonXPath = String.format("//button[text()='%s']", correctAnswer);
                WebElement correctAnswerButton = driver.findElement(By.xpath(answerButtonXPath));
                correctAnswerButton.click();
                Thread.sleep(800); // 等待选项点击完成

                //点击下一题按钮（如果有）
                List<WebElement> nextButton = driver.findElements(By.id("tonext"));
                if (!nextButton.isEmpty()) {
                    nextButton.get(0).click();
                    Thread.sleep(2000); // 等待加载下一题
                } else {
                    System.out.println("已经到达最后一题！");
                    break;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 7. 关闭浏览器
            driver.quit();
        }
    }


}
