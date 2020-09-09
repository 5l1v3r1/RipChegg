package me.agramon.ripchegg;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.util.Collections;
import java.util.Date;
import java.util.StringTokenizer;

import static java.lang.Thread.sleep;

public class Rip extends Command {

    public Rip() {
        super.name = "chegg";
        super.help = "Gets a screenshot of the chegg website";
    }

    @Override
    protected void execute(CommandEvent e) {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));


        System.setProperty("webdriver.chrome.driver", Config.get("CHROMEDRIVER"));
        WebDriver driver = new ChromeDriver(options);
        //readCookie(driver);

        if (e.getArgs() != null) {
            try {
                loginChegg(driver,Config.get("EMAIL"), Config.get("PASSWORD"));
                sleep(2000);
                //logCookies(driver);
                driver.get(e.getArgs());
                File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                e.getChannel().sendFile(scrFile).queue();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        } else {
            e.getChannel().sendMessage("Please enter the link you wish to rip");
        }
    }

    public void logCookies(WebDriver driver) {
        File file = new File("Cookie.data");
        try {
            file.delete();
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferwrite = new BufferedWriter(fileWriter);
            for(Cookie cook : driver.manage().getCookies()){
                String writeup = cook.getName()+";"+cook.getValue()+";"+cook.getDomain()+";"+cook.getPath()+""
                        + ";"+cook.getExpiry()+";"+cook.isSecure();
                bufferwrite.write(writeup);
                System.out.println(writeup);
                bufferwrite.newLine();
            }
            bufferwrite.flush();bufferwrite.close();fileWriter.close();
        }catch(Exception exp){
            exp.printStackTrace();
        }
    }

    public static void readCookie(WebDriver driver) {
            try{
                File file = new File("Cookie.data");
                FileReader fileReader = new FileReader(file);
                BufferedReader Buffreader = new BufferedReader(fileReader);
                String strline;
                while((strline=Buffreader.readLine())!=null){
                    StringTokenizer token = new StringTokenizer(strline,";");
                    while(token.hasMoreTokens()){
                        String name = token.nextToken();
                        String value = token.nextToken();
                        String domain = token.nextToken();
                        String path = token.nextToken();
                        Date expiry = null;

                        String val;
                        if(!(val=token.nextToken()).equals("null")){
                            expiry = new Date(val);
                        }
                        Boolean isSecure = new Boolean(token.nextToken()).booleanValue();
                        Cookie ck = new Cookie(name,value,domain,path,expiry,isSecure);
                        driver.manage().addCookie(ck); // This will add the stored cookie to our current session
                    }
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            driver.get("https://chegg.com");
        }

    public static void loginChegg(WebDriver driver, String email, String password) throws InterruptedException {
        driver.get("https://www.chegg.com/auth?action=login");
        sleep(2000);
        driver.findElement(By.id("emailForSignIn")).sendKeys(email);
        sleep(2000);
        driver.findElement(By.id("passwordForSignIn")).sendKeys(password, Keys.ENTER);
    }

    public static void waitForLoad(WebDriver driver) {
        new WebDriverWait(driver, 30).until((ExpectedCondition<Boolean>) wd ->
                ((JavascriptExecutor) wd).executeScript("return document.readyState").equals("complete"));
    }
}