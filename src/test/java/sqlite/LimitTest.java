package sqlite;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.enums.Order;
import com.craftmend.storm.connection.sqlite.SqliteFileDriver;
import com.craftmend.storm.connection.sqlite.SqliteMemoryDriver;
import lombok.SneakyThrows;
import models.SocialPost;
import models.User;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

public class LimitTest {

    @Test
    @SneakyThrows
    public void testLimits() {
        File dataFile = new File("test-data/mass-test.db");
        dataFile.mkdirs();
        if (dataFile.exists()) dataFile.delete();

        Storm storm = new Storm(new SqliteFileDriver(dataFile));
        storm.registerModel(new User());
        storm.registerModel(new SocialPost());
        storm.runMigrations();
        int accounts = 200;

        for (int i = 0; i < accounts; i++) {
            User u = new User();
            u.setUserName("Matt-" + i);
            u.setEmailAddress(i + "@craftmend.com");
            u.setScore(500 - i);
            storm.save(u);
        }

        Collection<User> limited = storm
                .buildQuery(User.class)
                .orderBy("id", Order.DESC)
                .limit(10)
                .execute()
                .join();

        Assert.assertEquals(limited.size(), 10);
    }

}
