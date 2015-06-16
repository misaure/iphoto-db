package com.msaure.iphotodb.parser.util;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QNameStackTest 
{
    private QNameStack stack;
    
    @Before
    public void setUp()
    {
        this.stack = new QNameStack();
    }
    
    @Test
    public void testThatStackInitiallyIsEmpty()
    {
        assertTrue(this.stack.isEmpty());
        assertEquals(0, this.stack.size());
    }
    
    @Test
    public void testAddingOneElement()
    {
        final QName name = new QName("root");
        
        this.stack.push(name);
        assertFalse(this.stack.isEmpty());
        assertEquals(1, this.stack.size());
        assertEquals(name, this.stack.top());
        assertTrue(this.stack.matches(name));
    }
    
    @Test
    public void testAddingAndRemovingOneElement()
    {
        final QName name = new QName("root");
        
        this.stack.push(name);
        this.stack.pop();
        assertTrue(this.stack.isEmpty());
        assertEquals(0, this.stack.size());
    }
    
    @Test
    public void testAddingTwoAndRemovingOneElement()
    {
        final QName parentName = new QName("root");
        final QName childName = new QName("child");
        
        this.stack.push(parentName);
        this.stack.push(childName);
        
        assertEquals(childName, this.stack.top());
        assertFalse(this.stack.isEmpty());
        assertEquals(2, this.stack.size());
        assertTrue(this.stack.matches(new QName("child")));
        assertTrue(this.stack.matches(new QName[] { new QName("child"), new QName("root") }));
    }

    @Test
    public void testMatchingLists()
    {
        List<QName> expectedPath = new ArrayList<>();
        expectedPath.add(new QName("child"));
        expectedPath.add(new QName("root"));
        
        this.stack.push(new QName("root"));
        this.stack.push(new QName("child"));
        
        assertTrue(this.stack.matches(expectedPath));
    }
}

