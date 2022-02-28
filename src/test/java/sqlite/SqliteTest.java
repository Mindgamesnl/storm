package sqlite;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.enums.Where;
import com.craftmend.storm.connection.sqlite.SqliteDriver;
import lombok.SneakyThrows;
import models.SimpleUserModel;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.Collection;
import java.util.UUID;

public class SqliteTest {

    @Test
    @SneakyThrows
    public void testSqlite() {
        File dataFile = new File("test-data/database.db");
        dataFile.mkdirs();
        if (dataFile.exists()) dataFile.delete();

        Storm storm = new Storm(new SqliteDriver(dataFile));
        storm.migrate(new SimpleUserModel());

        // create a new user
        SimpleUserModel mindgamesnl = new SimpleUserModel();
        mindgamesnl.setUserName("Mindgamesnl");
        mindgamesnl.setEmailAddress("mats@toetmats.nl");
        mindgamesnl.setScore(9009);
        storm.save(mindgamesnl);

        SimpleUserModel niceFriend = new SimpleUserModel();
        niceFriend.setUserName("Some Friend");
        niceFriend.setEmailAddress("friend@pornhub.com");
        niceFriend.setScore(50);
        storm.save(niceFriend);

        SimpleUserModel randomBloke = new SimpleUserModel();
        randomBloke.setUserName("Random Bloke");
        randomBloke.setEmailAddress("whatever@sheeesh.com");
        randomBloke.setScore(394);
        storm.save(randomBloke);

        // try to find all users
        Collection<SimpleUserModel> allUsers = storm.findAll(SimpleUserModel.class).join();
        Assert.assertEquals(3, allUsers.size());

        // check if all UUID's are loaded propery
        for (SimpleUserModel allUser : allUsers) {
            Assert.assertNotNull(allUser.getMinecraftUserId());
            Assert.assertEquals(UUID.class, allUser.getMinecraftUserId().getClass());
        }

        // test queries
        Collection<SimpleUserModel> justMindgamesnl =
                storm.buildQuery(SimpleUserModel.class)
                        .where("user_name", Where.EQUAL, "Mindgamesnl")
                        .limit(1)
                        .execute()
                        .join();

        Assert.assertEquals("Mindgamesnl", justMindgamesnl.stream().findFirst().get().getUserName());

    }

}
