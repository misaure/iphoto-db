package com.msaure.iphotodb.parser.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.namespace.QName;

/**
 * This is a helper class that can be used to record the structure of an XML 
 * document that is parsed using an event driven API like SAX or StAX.
 * 
 * <p>It allows to track the current path in the XML tree by pushing the current
 * node onto the stack at the beginning of an <i>startElement</i> handler and
 * popping the name element at the end of the <i>endElement</i> handler. For
 * a SAX handler, it could look like this:</p>
 * 
 * <pre>
 * public class MyHandler extends DefaultHandler {
 *   private QNameStack path;
 * 
 *   public void startElement(String uri, String localName, String qName, Attributes attrs) {
 *     path.push( new QName(uri, localName) );
 *     // ... handle elements and access current tree path
 *   }
 * 
 *   public void endElement(String uri, String localName, String qName) {
 *     // ... handle elements and access current tree path
 *     path.pop();
 *   }
 * }
 * </pre>
 */
public class QNameStack 
{
    private final List<QName> stack;
    
    public QNameStack() 
    {
        this.stack = new ArrayList<>();
    }
    
    public void push(QName name)
    {
        if (null == name)
        {
            throw new IllegalArgumentException("'name' must not be null");
        }
        this.stack.add(name);
    }
    
    public void pop()
    {
        if (size() > 0)
        {
            this.stack.remove(stack.size() - 1 );
        }
        else
        {
            throw new IllegalStateException("pop called for empty stack");
        }
    }
    
    public QName top()
    {
        return this.stack.get(this.stack.size() - 1 );
    }
    
    public boolean isEmpty()
    {
        return 0 == this.stack.size();
    }
    
    public int size()
    {
        return this.stack.size();
    }
    
    public boolean matches(QName name)
    {
        return top().equals(name);
    }
    
    public boolean matches(QName[] names)
    {
        int i = size() - 1;
        for (QName name: names)
        {
            if (! this.stack.get(i).equals(name))
            {
                return false;
            }
            
            --i;
        }
        
        return true;
    }
    
    public boolean matches(Collection<? extends QName> names)
    {
        int i = size() - 1;
        for (QName name: names)
        {
            if (! this.stack.get(i).equals(name))
            {
                return false;
            }
            
            --i;
        }
        
        return true;
    }
    
    @Override
    public String toString() 
    {
        final StringBuilder sb = new StringBuilder();
        
        for (QName name: this.stack)
        {
            sb.append(name.toString());
            sb.append(" ");
        }
        
        return sb.toString();
    }
}
