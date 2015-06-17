package com.msaure.iphotodb.parser.stax;

import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

public class PropertyListParserTest {

    InputStream sampleFile;

    @Before
    public void loadSampleFile()
    {
        sampleFile = getClass().getResourceAsStream("/com/msaure/iphotodb/parser/stax/plist2.xml");
        assertNotNull(sampleFile);
    }

    @After
    public void closeSampleFile()
    {
        IOUtils.closeQuietly(sampleFile);
    }

    @Test
    public void testIt() throws Exception
    {
        PropertyListParser parser = PropertyListParser.forInputStream(sampleFile);
        parser.parse(new DebugPropertyListParserHandler());  // TODO replace with mock
    }
}
