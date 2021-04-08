package com.sdo.abc.controller;

import com.sdo.abc.model.AbcResult;
import com.sdo.abc.service.AbcService;

import org.jsoup.HttpStatusException;
import org.jsoup.UnsupportedMimeTypeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * Controlador para peticiones al abc-apifier.
 */
@RestController
@RequestMapping("abc")
@Validated
public class AbcController {

    /** Log de la clase */
    protected Logger LOG = LoggerFactory.getLogger(AbcController.class);

    /** Servicio para consultar el buscador web de ABC. */
    @Autowired
    private AbcService abcService;

    /**
     * Comtrolador para resultados de búsqueda de artículos de noticias.
     * @param consulta Consulta para el buscador
     * @return Artículos de noticias encontradas.
     * @throws MalformedURLException        URL malformada.
     * @throws HttpStatusException          Respuesta no correcta y bandera de ignorar errores HTTP desactivada.
     * @throws UnsupportedMimeTypeException Tipo de dato en respuesta no soportado.
     * @throws SocketTimeoutException       Tiempo de conexión agotado.
     * @throws IOException                  Error desconocido.
     */
    @GetMapping("abc")
    public List<AbcResult> getResultados(String consulta)
            throws MalformedURLException, HttpStatusException, UnsupportedMimeTypeException, SocketTimeoutException, IOException {

        LOG.info("request started -> 'www.abc.com.py/buscar' -> consulta ({})", consulta);
        final List<AbcResult> resultados = abcService.getResultados(consulta);
        LOG.info("request finished -> results ({})", resultados.size());
        return resultados;
    }

}