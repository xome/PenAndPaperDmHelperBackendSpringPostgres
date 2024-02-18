package de.mayer.backendspringpostgres.graph;

import de.mayer.backendspringpostgres.graph.domainservice.Cache;
import de.mayer.backendspringpostgres.graph.domainservice.ChapterLinkRepository;
import de.mayer.backendspringpostgres.graph.domainservice.ChapterRepository;
import de.mayer.backendspringpostgres.graph.domainservice.GraphService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SpringConfiguration implements BeanFactoryAware {

    private BeanFactory beanFactory;

    @Bean
    @Scope("prototype")
    public GraphService graphService() {
        return new GraphService(beanFactory.getBean(ChapterRepository.class),
                beanFactory.getBean(ChapterLinkRepository.class),
                beanFactory.getBean(Cache.class));
    }

    @Override
    public void setBeanFactory(@SuppressWarnings("NullableProblems") BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
