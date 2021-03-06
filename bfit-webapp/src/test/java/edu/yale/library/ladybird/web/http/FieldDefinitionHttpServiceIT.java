package edu.yale.library.ladybird.web.http;


import edu.yale.library.ladybird.web.AbstractWarTest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.tika.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.MalformedURLException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class FieldDefinitionHttpServiceIT extends AbstractWarTest {

    private final String HTTP_SERVICE = "fielddefinitions";

    @BeforeClass
    public static void setup() throws MalformedURLException {
        try {
            AbstractWarTest.setupContainer();
        } catch (RuntimeException e) {
            fail("Error starting container");
        }
    }

    @AfterClass
    public static void tearDown() {
        //TODO
    }

    @Test
    public void testGet() throws Exception {
        HttpServiceTestUtil httpServiceTestUtil = new HttpServiceTestUtil();
        final HttpGet getMethod0 = httpServiceTestUtil.doGET(HTTP_SERVICE);
        final HttpResponse response0 = httpServiceTestUtil.httpClient.execute(getMethod0);
        assertNotNull(response0);
        assertEquals(IOUtils.toString(response0.getEntity().getContent()), 200,
                response0.getStatusLine().getStatusCode());
    }

}
