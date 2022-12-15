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


@RequestScoped
public class PolnilniceBean {

    private Logger log = Logger.getLogger(PolnilniceBean.class.getName());

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
