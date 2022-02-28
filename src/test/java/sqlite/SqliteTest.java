package sqlite;

import com.craftmend.storm.Storm;
import com.craftmend.storm.connection.sqlite.SqliteDriver;
import lombok.SneakyThrows;
import org.junit.Test;

import java.io.File;

public class SqliteTest {

    @Test
    @SneakyThrows
    public void testSqlite() {
        File dataFile = new File("test-data/database.db");
        dataFile.mkdirs();
        if (dataFile.exists()) dataFile.delete();

        Storm storm = new Storm(new SqliteDriver(dataFile));
    }

}
