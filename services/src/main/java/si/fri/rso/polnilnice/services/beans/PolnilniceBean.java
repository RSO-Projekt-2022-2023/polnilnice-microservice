package si.fri.rso.polnilnice.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.polnilnice.lib.Polnilnice;
import si.fri.rso.polnilnice.models.converters.PolnilniceConverter;
import si.fri.rso.polnilnice.models.entities.PolnilniceEntity;
import org.eclipse.microprofile.metrics.annotation.Timed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.InternalServerErrorException;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;


@RequestScoped
public class PolnilniceBean {

    private Optional<String> polnilnice_host;

    CloseableHttpClient httpClient = HttpClients.createDefault();

    private Logger log = Logger.getLogger(PolnilniceBean.class.getName());

    @Inject
    private PolnilniceBean polnilniceBeanProxy;

    @Inject
    private EntityManager em;

    public Polnilnice getPolnilnice() {

        TypedQuery<PolnilniceEntity> query = em.createNamedQuery(
                "PolnilniceEntity.getAll", PolnilniceEntity.class);

        List<PolnilniceEntity> resultList = query.getResultList();

        return (Polnilnice) resultList.stream().map(PolnilniceConverter::toDto).collect(Collectors.toList());

    }

    @Timed
    public List<Polnilnice> getPolnilniceFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, PolnilniceEntity.class, queryParameters).stream()
                .map(PolnilniceConverter::toDto).collect(Collectors.toList());
    }

    public Polnilnice getPolnilnice(Integer id) {

        PolnilniceEntity polnilniceEntity = em.find(PolnilniceEntity.class, id);

        if (polnilniceEntity == null) {
            throw new NotFoundException();
        }

        Polnilnice polnilnice = PolnilniceConverter.toDto(polnilniceEntity);
        polnilnice.setAvailable(polnilniceBeanProxy.getAvailable(id));

        return polnilnice;
    }

    public Polnilnice createPolnilnice(Polnilnice polnilnice) {

        PolnilniceEntity polnilniceEntity = PolnilniceConverter.toEntity(polnilnice);

        try {
            beginTx();
            em.persist(polnilniceEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (polnilniceEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return PolnilniceConverter.toDto(polnilniceEntity);
    }

    public Polnilnice putPolnilnice(Integer id, Polnilnice polnilnice) {

        PolnilniceEntity c = em.find(PolnilniceEntity.class, id);

        if (c == null) {
            return null;
        }

        PolnilniceEntity updatedPolnilniceEntity = PolnilniceConverter.toEntity(polnilnice);

        try {
            beginTx();
            updatedPolnilniceEntity.setId(c.getId());
            updatedPolnilniceEntity = em.merge(updatedPolnilniceEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return PolnilniceConverter.toDto(updatedPolnilniceEntity);
    }

    public boolean deletePolnilnice(Integer id) {

        PolnilniceEntity polnilnice = em.find(PolnilniceEntity.class, id);

        if (polnilnice != null) {
            try {
                beginTx();
                em.remove(polnilnice);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    @Timeout(value = 2, unit = ChronoUnit.SECONDS)
    @CircuitBreaker(requestVolumeThreshold = 3)
    @Fallback(fallbackMethod = "getAvailableFallback")
    public Integer getAvailable(Integer polnilniceId) {
        log.info("Executing FT!!!!");
        polnilnice_host = Optional.of("http://localhost:8080");
        String polnilniceString = myHttpGet(polnilnice_host.get() + "/v1/polnilnice", null);
        System.out.println(polnilniceString);

        try {
            System.out.println("Getting polnilnica available");
            JSONArray polnilniceArray = new JSONArray(polnilniceString);
            JSONObject polnilniceObject = polnilniceArray.getJSONObject(polnilniceId-1);
            // System.out.println(polnilniceObject.toString());
            Integer available = polnilniceObject.getInt("available");
            System.out.println("Available:" + available);
            return available;
        } catch (WebApplicationException | ProcessingException e) {
            System.out.println("TU2!!");
            log.severe(e.getMessage());
            throw new InternalServerErrorException(e);
        }
    }

    public Integer getAvailableFallback(Integer polnilniceId) {
        System.out.println("TU3");
        return null;
    }

    private String myHttpGet(String url, String body) {
        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(request);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            return  e.getMessage();
        }
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
