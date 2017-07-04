package iosTest;

import DataProvider.DataProvid;
import io.appium.java_client.ios.IOSDriver;
import method.*;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import util.AutoLogger;
import util.InitSetup;

import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by mff on 2017/3/28.
 */
public class FinshPushClassTest {
    private static AutoLogger logger = AutoLogger.getLogger(FinshPushClassTest.class);
    IOSDriver driverios;
    InitSetup is;
    PushClass pushClass;
    Login login;
    SelectClass selectClass;

    @BeforeMethod
    public void setUp() throws Exception {
        //initialize = new InitializeDriver();
        is = new InitSetup();
        driverios = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), is.InitSetUpCFG(new DesiredCapabilities()));
        driverios.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test(description = "登陆成功", dataProvider = "LoginSucess", dataProviderClass = DataProvid.class)
    public void pushClassTest(String username, String pwd, String platv) throws Exception {
        logger.log("This is finishPushClassTest log");
        driverios.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Thread.sleep(3000);
        int width = driverios.manage().window().getSize().width;
        int height = driverios.manage().window().getSize().height;
        System.out.println("屏幕宽度：" + width);
        System.out.println("屏幕长度：" + height);
        if (platv.startsWith("9")) {
            ScrollPagesFor9 scrollPages = new ScrollPagesFor9();
            for (int i = 0; i < 3; i++) {
                scrollPages.scrollRightToLeft(driverios, (i + 1), width, height);
            }
        } else if (platv.startsWith("10")) {
            ScrollPages scrollPages = new ScrollPages();
            for (int i = 0; i < 3; i++) {
                scrollPages.scrollRightToLeft(driverios, (i + 1), width, height);
            }
        } else {
            System.out.println("版本信息错误~~~");
        }
        driverios.findElement(By.className("UIAButton")).click();
        login = new Login();
        login.loginSucess(driverios, username, pwd);
        selectClass = new SelectClass();
        selectClass.selectClass(driverios);
        pushClass = new PushClass();
        pushClass.pushClassOne(driverios);
        ScrollPages scrollPages = new ScrollPages();
        Random r1 = new Random();
        int count1 = r1.nextInt(5)+1;
        for (int i =0; i < count1; i++){
            scrollPages.scrollUpToDown(driverios,(i+1),width,height);
        }
        Random r2 = new Random();
        int count2 = r2.nextInt(5)+1;
        for (int i =0; i < count2; i++){
            scrollPages.scrollDownToUp(driverios,(i+1),width,height);
        }
        Thread.sleep(1000);
        pushClass.finishPushClassOne(driverios,width,height);
        Assert.assertEquals("推送",driverios.findElement(By.id("推送")).getText());
    }

    @AfterMethod
    public void tearDown() {
        driverios.quit();
    }
}