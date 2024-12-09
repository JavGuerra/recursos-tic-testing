package com.grupo1.recursos_tic.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EnumTag {

    HARDWARE("Hardware"),
    SOFTWARE("Software"),
    OS("Sistema Operativo"),
    ARCHITECTURE("Arquitectura"),
    PROGRAMMING("Programación"),
    WEBDEV("Diseño web"),
    DESIGN("Diseño gráfico"),
    UIUX("UI/UX"),
    ACCESSIBILITY("Accesibilidad"),
    TELECOM("Telecomunicaciones"),
    NETWORKING("Redes"),
    IOT("IoT"),
    CLOUD("Cloud"),
    AI("Inteligencia Artificial"),
    VIRTUALIZATION("Virtualización"),
    METODOLOGY("Metodología"),
    STANDARDS("Estándares"),
    SECURITY("Seguridad"),
    DATABASES("Bases de datos"),
    DATASCIENCE("Ciencia de datos"),
    BIGDATA("Big Data"),
    BLOCKCHAIN("Blockchain"),
    CRYPTO("Criptomonedas"),
    PERFORMANCE("Rendimiento"),
    SEO("SEO"),
    DEVELOPMENT("Desarrollo"),
    TESTING("Pruebas"),
    LEARNING("Aprendizaje"),
    MOBILE("Mobile"),
    RESEARCH("Investigación"),
    CONSULTING("Consultoría"),
    BUSINESS("Negocios"),
    ECOMMERCE("Comercio electrónico"),
    ENTERTAINMENT("Entretenimiento"),
    HISTORY("Historia"),
    PROJECTS("Proyectos"),
    SUPPORT("Soporte"),
    IMPORTANT("Importante"),
    NEWS("Noticias"),
    TIPS("Tips"),
    OTHER("Otro");

    private final String tag;

    @Override
    public String toString() { return tag; }
}
