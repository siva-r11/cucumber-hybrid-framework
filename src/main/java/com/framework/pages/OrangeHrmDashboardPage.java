package com.framework.pages;

import com.framework.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class OrangeHrmDashboardPage extends BasePage {

    // Primary locators
    private static final By DASHBOARD_HEADING = By.cssSelector(".oxd-topbar-header-breadcrumb h6");
    private static final By USER_DROPDOWN = By.cssSelector(".oxd-userdropdown-tab");
    // Alternative locators for robustness
    private static final By MAIN_CONTENT = By.cssSelector(".orangehrm-container, .orangehrm-dashboard-grid, [data-v-app]");
    private static final By USER_PROFILE_BUTTON = By.xpath("//img[contains(@src, 'profilepicture')] | //span[contains(@class, 'user-info')] | button[contains(@class, 'user')]");
    private static final By PAGE_TITLE = By.xpath("//h1 | //h2 | //h6[contains(text(), 'Dashboard')] | //*[contains(text(), 'Dashboard')]");
    
    private static final By SIDE_PANEL = By.cssSelector(".oxd-sidepanel");
    private static final By DASHBOARD_GRID = By.cssSelector(".orangehrm-dashboard-grid");
    private static final By ADMIN_MENU = By.xpath("//span[contains(@class,'oxd-main-menu-item--name') and normalize-space()='Admin']");

    public OrangeHrmDashboardPage(WebDriver driver) {
        super(driver);
        // Ensure page has time to load after navigation
        waitForPageLoad();
    }

    private void waitForPageLoad() {
        try {
            // Wait for main content to be visible
            WaitUtils.visible(driver, MAIN_CONTENT);
            log.info("OrangeHRM Dashboard page loaded successfully");
        } catch (Exception e) {
            log.warn("Main content not found with primary selector, waiting for page elements");
        }
    }

    public boolean isLoaded() {
        // Try primary selectors first
        if (isDisplayed(DASHBOARD_HEADING) && isDisplayed(USER_DROPDOWN)) {
            return true;
        }
        // Fallback to alternative selectors
        boolean hasMainContent = isDisplayed(MAIN_CONTENT);
        boolean hasPageTitle = isDisplayed(PAGE_TITLE);
        boolean hasUserElement = isDisplayed(USER_PROFILE_BUTTON) || isDisplayed(USER_DROPDOWN);
        
        return hasMainContent && (hasPageTitle || hasUserElement);
    }

    public boolean hasSidePanel() {
        return isDisplayed(SIDE_PANEL);
    }

    public boolean hasDashboardGrid() {
        return isDisplayed(DASHBOARD_GRID);
    }

    public OrangeHrmAdminPage openAdmin() {
        isLoaded();
        click(ADMIN_MENU);
        return new OrangeHrmAdminPage(driver);
    }

    public String getHeading() {
        return getText(DASHBOARD_HEADING);
    }
}