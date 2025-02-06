package testSuite.in_progress.stack_overflow.httpEntity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class Test {
    public static void test(HttpResponse r) throws UnsupportedEncodingException {
        HttpResponse response = r;
        InputStream in = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        response.getEntity().getContent(), "UTF-8")); // Second call to getEntity()

    //Cannot call getContent twice if the entity is not repeatable. There is a method isRepeatable() to check
    }
}
