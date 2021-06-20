package org.isegodin.web.bookmanager;

//import com.opentable.db.postgres.embedded.EmbeddedPostgres;
//import com.opentable.db.postgres.embedded.LiquibasePreparer;
//import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
//import com.opentable.db.postgres.junit.PreparedDbRule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest(/*classes = TestContext.class*/)
class BookManagerApplicationTests {

//    public void DataSource

//    @RegisterExtension
//    public PreparedDbRule db = EmbeddedPostgresRules.preparedDatabase(
//            LiquibasePreparer.forClasspathLocation("liqui/master.xml")
//    );


    @Test
    void contextLoads() throws IOException {

//        try(EmbeddedPostgres pg = EmbeddedPostgres.start()) {
//            System.out.println();
//        }
    }

}
