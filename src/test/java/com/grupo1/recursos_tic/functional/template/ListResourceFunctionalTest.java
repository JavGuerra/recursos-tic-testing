package com.grupo1.recursos_tic.functional.template;

import com.grupo1.recursos_tic.service.ResourceService;
import com.grupo1.recursos_tic.service.UserService;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ListResourceFunctionalTest {

    private final String url = "http://localhost:8082/resources";

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ListResourceFunctionalPage page;

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        //resourceService.deleteAll();

        // Activar para Chrome, desactivar Firefox
        // Info: En linux no funciona si no es con la versión 113
        // ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless"); // para que no se abra el navegador
        // options.addArguments("--disable-gpu"); // Deshabilita la aceleración de hardware
        // options.addArguments("--window-size=1920,1080"); // Tamaño de la ventana
        //options.addArguments("--no-sandbox"); // Bypass OS security model, requerido en entornos sin GUI
        //options.addArguments("--disable-dev-shm-usage"); // Deshabilita el uso de /dev/shm manejo de memoria compartida
        //driver = new ChromeDriver(options);
        // Fin Chrome

        // Activar para Firefox, desactivar Chrome
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--headless"); // para que no se abra el navegador
        options.addArguments("--disable-gpu"); // Deshabilita la aceleración de hardware
        options.addArguments("--window-size=1920,1080"); // Tamaño de la ventana
        options.addArguments("--no-sandbox"); // Bypass OS security model, requerido en entornos sin GUI
        options.addArguments("--disable-dev-shm-usage"); // Deshabilita el uso de /dev/shm manejo de memoria compartida
        options.addPreference("dom.disable_beforeunload", true);
        driver = new FirefoxDriver(options);
        //driver = new FirefoxDriver(); // Sin opciones
        // Fin Firefox

        driver.get(url);
        driver.manage().window().maximize();
        if (page == null) {
            page = new ListResourceFunctionalPage(driver, userService, passwordEncoder);
        }
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /*
     * Tests Selenium
     */

    @Test
    @DisplayName("Comprobar etiqueta <title>")
    void title() {
        assertEquals("Recursos", page.getTitle());
    }

    @Test
    @DisplayName("Comprobar la etiqueta <h1>")
    void h1() {
        assertEquals("Recursos", page.h1.getText());
    }

    @Test
    @DisplayName("Comprobar que NO existe el botón «Crear nuevo recurso» " +
            "hasta que se haya registrado un usuario")
    void buttonCreateResource() {
        page.saveUser("admin", "Admin1234", "admin@admin.es","admin");
        try {
            assertFalse(page.btnCreateResource.isDisplayed());
        } catch (NoSuchElementException e) {
            assertTrue(true); // El elemento no existe
        }
        page.loginAsUser("admin", "Admin1234");
        driver.get(url);
        assertTrue(page.btnCreateResource.isDisplayed());
    }

    // Otros tests

}
