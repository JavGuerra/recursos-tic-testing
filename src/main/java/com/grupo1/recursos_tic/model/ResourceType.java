package com.grupo1.recursos_tic.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ResourceType {

    BOOK("Libro"),
    ARTICLE("Artículo"),
    REPORT("Informe"),
    DOCUMENTATION("Documentación"),
    DOCUMENT("Documento"),
    TUTORIAL("Tutorial"),
    FAQ("FAQ"),
    SOFTWARE("Software"),
    IMAGE("Imagen"),
    GALLERY("Galería"),
    AUDIO("Audio"),
    PODCAST("Podcast"),
    VIDEO("Video"),
    VIDEOCHANNEL("Canal de Video"),
    TVSHOW("Series de TV"),
    MOVIE("Película"),
    WEB("Página web"),
    PORTAL("Portal"),
    COMMUNITY("Comunidad"),
    BLOG("Blog"),
    POST("Entrada de blog"),
    FORUM("Foro"),
    COMMENT("Comentario"),
    NEWS("Noticias"),
    DATABASE("Base de datos"),
    REPOSITORY("Repositorio"),
    OTHER("Otro");

    private final String resourceType;

    public boolean isValidType(int ordinal) {
        ResourceType[] values = ResourceType.values();
        return ordinal >= 0 && ordinal < values.length;
    }

    @Override
    public String toString() { return resourceType; }
}
