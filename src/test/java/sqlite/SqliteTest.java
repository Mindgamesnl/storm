package sqlite;

import com.craftmend.storm.Storm;
import com.craftmend.storm.connection.sqlite.SqliteDriver;
import lombok.SneakyThrows;
import models.SimpleUserModel;
import org.junit.Test;

import java.io.File;

public class SqliteTest {

    @Test
    @SneakyThrows
    public void testSqlite() {
        File dataFile = new File("test-data/database.db");
        dataFile.mkdirs();
        // if (dataFile.exists()) dataFile.delete();

        Storm storm = new Storm(new SqliteDriver(dataFile));
        storm.migrate(new SimpleUserModel());

        // create a new user
        SimpleUserModel user = new SimpleUserModel();
        user.setUserName("Mindgamesnl");
        user.setEmailAddress("mats@toetmats.nl");
        user.setId(1);
        user.setScore(9009);

        storm.save(user);
    }

}
