package sqlite;

import com.craftmend.storm.Storm;
import com.craftmend.storm.connection.StormDriver;
import com.craftmend.storm.connection.sqlite.SqliteFileDriver;
import com.craftmend.storm.connection.sqlite.SqliteMemoryDriver;
import lombok.SneakyThrows;
import models.SocialPost;
import models.User;
import org.junit.Test;
import performance.StopWatch;

import java.io.File;
import java.util.Collection;

public class MassSqliteTest {

    @Test
    @SneakyThrows
    public void testSqlite() {
        File dataFile = new File("test-data/mass-test.db");
        dataFile.mkdirs();
        if (dataFile.exists()) dataFile.delete();

        testWithDriver(new SqliteFileDriver(dataFile), "With file");
        testWithDriver(new SqliteMemoryDriver(), "From memory");
    }

    private void testWithDriver(StormDriver stormDriver, String name) throws Exception {
        StopWatch stopWatch = new StopWatch(name);

        stopWatch.start("Initializing database");
        Storm storm = new Storm(stormDriver);
        storm.registerModel(new User());
        storm.registerModel(new SocialPost());
        storm.runMigrations();

        stopWatch.stop();
        int accounts = 10000;

        stopWatch.start("creating and inserting one single account");

        User singleUser = new User();
        singleUser.setUserName("JustOneMatt");
        singleUser.setEmailAddress("IAmUniueq@craftmend.com");
        singleUser.setScore(978);

        storm.save(singleUser);

        stopWatch.stop();

        stopWatch.start("creating and inserting " + accounts + " accounts");
        for (int i = 0; i < accounts; i++) {
            User u = new User();
            u.setUserName("Matt-" + i);
            u.setEmailAddress(i + "@craftmend.com");
            u.setScore(500 - i);
            storm.save(u);
        }
        stopWatch.stop();

        stopWatch.start("Loading all accounts from database to heap");
        Collection<User> everyone = storm.findAll(User.class).join();
        stopWatch.stop();

        stopWatch.start("Deleting all users");
        for (User user : everyone) {
            storm.delete(user);
        }
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());
    }

}
