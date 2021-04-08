package com.sdo.abc.service;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.sdo.abc.model.AbcResult;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servicio de consulta para búsquedas en la web de ABC.
 */
@Service
public class AbcService {

    /** Log de la clase */
    private static Logger LOG = LoggerFactory.getLogger(AbcService.class);

    /** URL general de ABC. */
    private static final String URL_ABC = "https://www.abc.com.py";
    
    /** URL del buscador dentro de la web de ABC. */
    private static final String URL_ABC_SEARCH = "https://www.abc.com.py/buscar";

    /**
     * Búsqueda de artículos de noticias en la web de ABC.
     * @param criteria Filtros de búsqueda.
     * @return Listado con los resultados de la búsqueda (comisiones).
     * @throws MalformedURLException        URL malformada.
     * @throws HttpStatusException          Respuesta no correcta y bandera de ignorar errores HTTP desactivada.
     * @throws UnsupportedMimeTypeException Tipo de dato en respuesta no soportado.
     * @throws SocketTimeoutException       Tiempo de conexión agotado.
     * @throws IOException                  Error desconocido.
     */
    public List<AbcResult> getResultados(String consulta)
            throws MalformedURLException, HttpStatusException, UnsupportedMimeTypeException, SocketTimeoutException, IOException {

        List<AbcResult> resultados = new ArrayList<AbcResult>();

        // Realizamos una conexión a la web de ABC para obtener cookies
        Connection.Response response = Jsoup
                .connect(URL_ABC)
                .method(Connection.Method.GET)
                .execute();

        // Realizamos una conexión al buscador web de ABC
        Document serviceResponse = Jsoup
                .connect(URL_ABC_SEARCH+consulta)
                .userAgent("Mozilla/5.0")
                .timeout(10 * 1000)
                .cookies(response.cookies())
                .post();

        // Validación de errores generados.
        LOG.debug(serviceResponse.outerHtml());
        for(Element script : serviceResponse.getElementsByTag("script")) {
            Optional<DataNode> errorDataNode = script.dataNodes().stream().filter(node -> node.getWholeData().contains(".error")).findFirst();
            if (errorDataNode.isPresent()) {
                Pattern p = Pattern.compile("toastr.error\\('(.*?)'\\)");
                Matcher m = p.matcher(errorDataNode.get().getWholeData());
                if (m.find()) {
                    throw new HttpStatusException(m.group(1), HttpStatus.INTERNAL_SERVER_ERROR.value(), URL_ABC_SEARCH);
                }
            }
        }

        // Proceso de scrapping donde aún falta acceder de mejor forma a los valores de cada campo, así como el casteo para la fecha.
        Elements articles = serviceResponse.select("article-list");
        if (articles.size() > 1) {
            Elements rows = articles.get(1).select("item-article");

            for (int i = 1; i < rows.size(); i++) {
                Elements values = rows.get(i).select("td");
                String fecha =  values.get(0).text();
                String enlace = values.get(1).text();
                String enlaceURL = values.get(2).text();
                String titulo = values.get(3).text();
                String resumen = values.get(4).text();

                resultados.add(new AbcResult(fecha, enlace, enlaceURL, titulo, resumen));
            }
        }

        return resultados;
    }

}