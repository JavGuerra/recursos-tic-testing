package com.grupo1.recursos_tic.functional.template;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class FormUserFunctionalPage {

    private WebDriver driver;

    @FindBy(tagName = "h1")
    public WebElement h1;

}
