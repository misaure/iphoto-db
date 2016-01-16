package com.msaure.iphotodb.parser.stax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StaxAlbumParser2 {

    private static final Logger LOG = LoggerFactory.getLogger(StaxAlbumParser2.class);

    public void parseAlbumlXml(PropertyListParser pplParser) throws XMLStreamException {
        ParseContext context = new ParseContext();

        parseRootDictionary(pplParser, context);
    }

    private void parseRootDictionary(PropertyListParser pplParser, ParseContext context) {
    }

    private static class ParseContext {

    }

    public static void main(String[] args) {
        StaxAlbumParser2 parser = new StaxAlbumParser2();

        for (String inputFileName: args) {
            try (InputStream xmlInputStream = new GZIPInputStream(new FileInputStream(inputFileName))) {
                parser.parseAlbumlXml(new PropertyListParser(xmlInputStream));
            }
            catch (IOException e) {
                LOG.error("failed to open file " + inputFileName, e);
            }
            catch(XMLStreamException e) {
                LOG.error("invalid XML content in file " + inputFileName, e);
            }
        }
    }
}
