import org.junit.jupiter.api.Test;

public class MainTest {

    @Test
    public void parseQueryTest() {
        String line = "taxii_collection this_ where this_.name='testfeed3' limit 1";
        System.out.println(Main.parseQuery(line));
        line = "table this where (this.column1='one' and this.column2='2' and this.column3='e@mail.com')";
        System.out.println(Main.parseQuery(line));
        line = "flare_user_X_flare_role this_ where (this_.user_id=9 and this_.role_id=5)";
        System.out.println(Main.parseQuery(line));
    }

}
