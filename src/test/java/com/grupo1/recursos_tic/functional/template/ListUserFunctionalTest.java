package com.grupo1.recursos_tic.functional.template;

import com.grupo1.recursos_tic.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ListUserFunctionalTest {

    private final String url = "http://localhost:8082/users";

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ListUserFunctionalPage page;

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        //driver = new ChromeDriver(); // Activar para Chrome, desactivar Firefox
        driver = new FirefoxDriver(); // Activar para Firefox, desactivar Chrome
        driver.get(url);
        driver.manage().window().maximize();
        if (page == null) {
            page = new ListUserFunctionalPage(driver, userService, passwordEncoder);
        }
        page.saveUser("admin", "Admin1234", "admin@admin.es","admin");
        page.saveUser("javier", "User1234", "user@user.es","author");
        page.saveUser("kevin", "User1234", "user@user.es","author");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Comprobar etiqueta <title> antes de iniciar sesión")
    void title() {
        assertEquals("Please sign in", page.getTitle());
    }

    @Test
    @DisplayName("Comprobar la etiqueta <h1>")
    void h1() {
        page.loginAsUser("admin", "Admin1234");
        driver.get(url);
        assertEquals("Usuarios", page.h1.getText());
        page.salir.click();
    }

    @Test
    @DisplayName("Comprobar que NO existe el botón «Crear nuevo usuario» " +
            "hasta que se haya registrado un usuario con el role ADMIN")
    void buttonCreateUser() {
        try {
            assertFalse(page.btnCreateUser.isDisplayed());
        } catch (NoSuchElementException e) {
            assertTrue(true); // El elemento no existe
        }
        page.loginAsUser("admin", "Admin1234");
        driver.get(url);
        assertTrue(page.btnCreateUser.isDisplayed());
        page.salir.click();
    }

    @Test
    @DisplayName("Comprobar que NO se muestra la vista con la lista de usuarios si no eres ADMIN")
    void listOfUsers() {
        try {
            assertFalse(page.btnCreateUser.isDisplayed());
            assertFalse(page.userList.isDisplayed());
        } catch (NoSuchElementException e) {
            assertTrue(true); // El elemento no existe
        }
        page.loginAsUser("admin", "Admin1234");
        driver.get(url);
        assertTrue(page.btnCreateUser.isDisplayed());
        page.salir.click();
    }

}
