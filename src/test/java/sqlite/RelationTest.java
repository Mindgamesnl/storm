package sqlite;

import com.craftmend.storm.Storm;
import com.craftmend.storm.api.enums.Where;
import com.craftmend.storm.connection.sqlite.SqliteFileDriver;
import lombok.SneakyThrows;
import models.SocialPost;
import models.User;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class RelationTest {

    @Test
    @SneakyThrows
    public void testSqlite() {
        File dataFile = new File("test-data/relation-test.db");
        dataFile.mkdirs();
        if (dataFile.exists()) dataFile.delete();

        Storm storm = new Storm(new SqliteFileDriver(dataFile));
        storm.registerModel(new User());
        storm.registerModel(new SocialPost());
        storm.runMigrations();

        // create a new user
        User mindgamesnl = new User();
        mindgamesnl.setUserName("Mindgamesnl");
        mindgamesnl.setEmailAddress("mats@toetmats.nl");
        mindgamesnl.setScore(9009);
        storm.save(mindgamesnl);

        // create a social post for random bloke
        SocialPost socialPost = new SocialPost();
        socialPost.setPoster(1);
        socialPost.setContent("What a wonderful day");

        storm.save(socialPost);

        // now load user 1, and get their posts from the database
        User user = storm.buildQuery(User.class)
                .where("id", Where.EQUAL, 1)
                .limit(1)
                .execute()
                .join()
                .stream().findFirst().get();

        for (SocialPost post : user.getPosts()) {
            System.out.println(user.getUserName() + " posted: " + post.getContent());
        }

        // count posts
        Assert.assertEquals("1", storm.count(SocialPost.class).join().toString());

    }

}
