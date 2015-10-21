package com.msaure.iphotodb.parser.stax;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.msaure.iphotodb.parser.util.QNameStack;

public class StaxAlbumParser {
    
    public void parseAlbumlXml(InputStream inputStream) throws XMLStreamException
    {
        final XMLInputFactory factory = XMLInputFactory.newInstance();
        final XMLEventReader xml = factory.createXMLEventReader(inputStream);

        //int depth = 0;
        final QNameStack path = new QNameStack();
        //final Stack<ValueType> 
        boolean insideKeyElement = false;
        boolean expectAlbumList = false;
        boolean expectMasterImageList = false;
        String currentKey = null;
        
        int albumCount = 0;
        int imageCount = 0;
        
        try {
            while (xml.hasNext()) 
            {
                XMLEvent event = xml.nextEvent();
                
                if (event.isStartElement()) {
                    final StartElement element = event.asStartElement();
                    
                    path.push(element.getName());
                    
                    if (1 == path.size()) {
                        if (!"plist".equals(element.getName().getLocalPart())) {
                            throw new RuntimeException("album file must start with 'plist'"); // TODO use proper exception type
                        }
                        
                    } else if (2 == path.size()) {
                        if (!"dict".equals(element.getName().getLocalPart())) {
                            throw new RuntimeException("expected dictionary as root container"); // TODO use proper exception type
                        }
                        
                    } else if (3 == path.size()) {
                        if ("key".equals(element.getName().getLocalPart())) {
                            insideKeyElement = true;
                        }
                        
                    } else if (expectAlbumList && 4 == path.size()) {
                        if ("dict".equals(element.getName().getLocalPart())) {
                            ++albumCount;
                            //System.out.println("  album from album list " + albumCount);
                        }
                        
                    } else if (expectMasterImageList && 4 == path.size()) {
                        if ("dict".equals(element.getName().getLocalPart())) {
                            ++imageCount;
                            //System.out.println("  image from master" + imageCount);
                        }
                    }
                    
                } else if (event.isCharacters()) {
                    if (insideKeyElement) {
                    	currentKey = event.asCharacters().getData();
                    	
                        if ("List of Albums".equals(currentKey)) {
                            expectAlbumList = true;
                            System.out.println("List of albums");
                            
                        } else if ("Master Image List".equals(currentKey)) {
                            expectMasterImageList = true;
                            System.out.println("Master album");
                        }
                    }
                    
                } else if (event.isEndElement()) {
                    final EndElement element = event.asEndElement();
                    
                    if (3 == path.size()) {
                        if ("key".equals(element.getName().getLocalPart())) {
                            insideKeyElement = false;
                        }
                    }
                    
                    if (expectAlbumList && 4 == path.size() && "dict".equals(element.getName().getLocalPart())) {
                    	expectAlbumList = false;
                    }
                    
                    if (!"key".equals(element.getName().getLocalPart())) {
                    	currentKey = null;
                    }
                    
                    path.pop();
                }
            }
        } finally {
            xml.close();
        }
        
        System.out.println("album count: " + albumCount);
        System.out.println("image count: " + imageCount);
    }
    
    public static void main(String[] args) {
    	final File testFile = new File("/Users/msaure/Desktop/AlbumData.xml");
        
        StaxAlbumParser parser = new StaxAlbumParser();
        try (InputStream testStream = new FileInputStream(testFile)) {
            parser.parseAlbumlXml(testStream);
        }
        catch (XMLStreamException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
