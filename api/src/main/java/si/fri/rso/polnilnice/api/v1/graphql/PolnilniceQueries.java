package si.fri.rso.polnilnice.api.v1.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import si.fri.rso.polnilnice.lib.Polnilnice;
import si.fri.rso.polnilnice.services.beans.PolnilniceBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class PolnilniceQueries {

    @Inject
    private PolnilniceBean polnilniceBean;

    /*@GraphQLQuery
    public PaginationWrapper<Polnilnice> allImageMetadata(@GraphQLArgument(name = "pagination") Pagination pagination,
                                                       @GraphQLArgument(name = "sort") Sort sort,
                                                       @GraphQLArgument(name = "filter") Filter filter) {

        return GraphQLUtils.process(polnilniceBean.getPolnilnice(), pagination, sort, filter);
    }*/

    @GraphQLQuery
    public Polnilnice getPolnilnice(@GraphQLArgument(name = "id") Integer id) {
        return polnilniceBean.getPolnilnice(id);
    }
}
