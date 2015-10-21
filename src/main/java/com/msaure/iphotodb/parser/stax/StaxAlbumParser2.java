package com.msaure.iphotodb.parser.stax;

import java.io.InputStream;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class StaxAlbumParser2 {
    
    private InputStream inputStream;
    private int currentEvent;
    
    public StaxAlbumParser2(InputStream inputStream) {
        this.inputStream = inputStream;
    }
    
    public void parseAlbumlXml() throws XMLStreamException {
        XMLInputFactory f = XMLInputFactory.newInstance();
        XMLStreamReader r = f.createXMLStreamReader(inputStream);
        
        while(r.hasNext()) {
            r.next();
        }
    }
}
