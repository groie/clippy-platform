package clippy;

import org.dalesbred.integration.spring.DalesbredConfigurationSupport;
import org.hsqldb.jdbc.JDBCDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by ilkkaharmanen on 11.06.2015.
 */
@Configuration
@EnableTransactionManagement
public class ClippyDatabaseConfiguration extends DalesbredConfigurationSupport {

    @Bean
    public DataSource dataSource() throws Exception {
        Properties p = new Properties();
        p.setProperty("url", "jdbc:hsqldb:mem:notedb;hsqldb.tx=mvcc");
        p.setProperty("user", "sa");
        p.setProperty("password", "");
        return JDBCDataSourceFactory.createDataSource(p);
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws Exception {
        return new DataSourceTransactionManager(dataSource());
    }
}
