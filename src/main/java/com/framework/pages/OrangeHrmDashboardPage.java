package com.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrangeHrmDashboardPage extends BasePage {

    private static final By DASHBOARD_HEADING = By.cssSelector(".oxd-topbar-header-breadcrumb h6");
    private static final By USER_DROPDOWN = By.cssSelector(".oxd-userdropdown-tab");

    public OrangeHrmDashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(DASHBOARD_HEADING) && isDisplayed(USER_DROPDOWN);
    }

    public String getHeading() {
        return getText(DASHBOARD_HEADING);
    }
}