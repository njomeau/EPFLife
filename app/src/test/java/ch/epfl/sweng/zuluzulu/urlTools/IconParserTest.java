package ch.epfl.sweng.zuluzulu.urlTools;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class IconParserTest {
    IconParser parser;

    @Before
    public void init() {
        this.parser = new IconParser();
    }


    @Test
    public void parseIconWrongData() {
        List<String> result = parser.parse(new BufferedReader(new StringReader("test")));
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void parseIconWithNull() {
        assertThat(parser.parse(null), is(nullValue()));
    }

    @Test
    public void parseIcon() {
        List<String> result = parser.parse(new BufferedReader(
                new StringReader("<link href=\"my.ico\" />")));
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void parseIconWithWronInputStream() {
        StringReader input = new StringReader("input");
        input.close();
        List<String> result = parser.parse(new BufferedReader(
                input));
        assertNull(result);
    }

    @Test
    public void parseIconWithClosedBf() {
        BufferedReader bf = new BufferedReader(new StringReader("&#8211; <a href=\"http://lauzhack.com\">LauzHack</a> (Organisation d&#8217;un Hackaton)<br />"));
        try {
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> result = parser.parse(bf);

        assertThat(result, is(nullValue()));
    }
}