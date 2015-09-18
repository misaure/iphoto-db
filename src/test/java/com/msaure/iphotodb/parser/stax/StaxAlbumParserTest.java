package com.msaure.iphotodb.parser.stax;

import org.junit.Before;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;

public class StaxAlbumParserTest {
    StaxAlbumParser parser;

    @Before
    public void setUp() throws IOException, XMLStreamException {
        parser = new StaxAlbumParser();
        try (InputStream album1Stream = getClass().getResourceAsStream("/com/msaure/iphotodb/parser/stax/AlbumData_001.xml")) {
            assertNotNull("unable to load test resource 'AlbumData_001.xml'", album1Stream);
            parser.parseAlbumlXml(album1Stream);
        }
    }

    @Test
    public void test1() {

    }
}
