package readers;

import com.craftmend.storm.parser.ModelParser;
import com.craftmend.storm.parser.objects.ModelField;
import lombok.SneakyThrows;
import models.SimpleUserModel;
import org.junit.Assert;
import org.junit.Test;

public class TestModelParser {

    @Test
    public void testParser() {
        int fieldCount = 4;
        SimpleUserModel simpleUser = new SimpleUserModel();

        ModelParser parser = simpleUser.parsed();
        // assert that all 3 fields have been parsed properly
        Assert.assertEquals(fieldCount, parser.getParsedFields().length);
        // all 3 classes should have a parser
        for (ModelField parsedField : parser.getParsedFields()) {
            // they all should have an adapter
            Assert.assertNotNull(parsedField.getAdapter());
        }

        // table create statements should include all fields
        String tableCreateStatement = simpleUser.statements().buildSqlTableCreateStatement();
        Assert.assertEquals(fieldCount, tableCreateStatement.split(",").length);
    }

}
