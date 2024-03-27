package org.damiane.config;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class JpaConfigTest {

    @InjectMocks
    private JpaConfig jpaConfig;

    @Test
    public void Given_JpaConfigInitialized_When_GettingDataSource_Then_NonNullDataSource() {
        DataSource dataSource = jpaConfig.dataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void Given_JpaConfigInitialized_When_GettingEntityManagerFactory_Then_NonNullEntityManagerFactory() {
        DataSource dataSourceMock = mock(DataSource.class);
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = jpaConfig.entityManagerFactory();
        assertNotNull(entityManagerFactoryBean);
        assertNotNull(entityManagerFactoryBean.getDataSource());
    }

    @Test
    public void Given_EntityManagerFactoryMockInitialized_When_GettingTransactionManager_Then_NonNullTransactionManager() {
        EntityManagerFactory entityManagerFactoryMock = mock(EntityManagerFactory.class);
        assertNotNull(jpaConfig.transactionManager(entityManagerFactoryMock));
    }
}
