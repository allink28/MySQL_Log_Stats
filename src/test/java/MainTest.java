import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class MainTest {

    @Test
    public void parseSelectQueryTest() {
        String line = "taxii_collection this_ where this_.name='testfeed3' limit 1";
        String result;
        System.out.println(result = Main.parseSelectQuery(line));
        assertFalse(result.contains("="));
        line = "table this where (this.column1='one' and this.column2='2' and this.column3='e@mail.com')";
        System.out.println(result = Main.parseSelectQuery(line));
        assertFalse(result.contains("="));
        line = "flare_user_X_flare_role this_ where (this_.user_id=9 and this_.role_id=5)";
        System.out.println(result = Main.parseSelectQuery(line));
        assertFalse(result.contains("="));
    }

    @Test
    public void parseInsertQueryTest() {
        String line =
                "taxii_collection_X_taxii_content " +
                        "(flare_package_id, collection_id, available, timestamp) " +
                        "values (62, 10, true, '2016-06-24 09:36:03')";
        String result;
        System.out.println(result = Main.parseInsertQuery(line));
        assertFalse(result.contains("62"));
        assertFalse(result.contains("true"));
        assertFalse(result.contains("2016-06-24 09:36:03"));
    }

}
