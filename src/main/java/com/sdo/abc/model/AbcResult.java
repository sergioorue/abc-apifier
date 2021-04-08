package com.sdo.abc.model;

import java.time.LocalDateTime;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Resultados de la búsqueda en la web de ABC.
 */
public class AbcResult {

    /** Fecha. */
    final LocalDateTime fecha;

    /** Link de la noticia */
    final String enlace;

    /** Link de la fotografía */
    final String enlaceURL;

    /** Título */
    final String titulo;

    /** Grupo */
    final String resumen;


    /**
     * Constructor.
     * @param fecha
     * @param enlace
     * @param enlaceURL
     * @param titulo
     * @param resumen
     */
    public AbcResult(LocalDateTime fecha, String enlace, String enlaceURL, String titulo, String resumen) {
        this.fecha = fecha;
        this.enlace = enlace;
        this.enlaceURL = enlaceURL;
        this.titulo = titulo;
        this.resumen = resumen;
    }

    /**
     * @return Fecha.
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * @return Enlace de la noticia.
     */
    public String getEnlace() {
        return enlace;
    }

    /**
     * @return Enlace de la fotografía.
     */
    public String getEnlaceURL() {
        return enlaceURL;
    }

    /**
     * @return Título.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @return Resumen.
     */
    public String getResumen() {
        return resumen;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof AbcResult)) {
            return false;
        }

        AbcResult castOther = (AbcResult) other;
        return new EqualsBuilder()
                .append(fecha, castOther.fecha)
                .append(enlace, castOther.enlace)
                .append(enlaceURL, castOther.enlaceURL)
                .append(titulo, castOther.titulo)
                .append(resumen, castOther.resumen)
                .isEquals();
    }

}