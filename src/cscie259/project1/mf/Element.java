package cscie259.project1.mf;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * A simplified, non-interface version of org.w3c.dom.Element.
 *
 * You MAY modify this file, provided you do not change the
 * declarations or definitions of the constructor and getNodeType.
 *
 * @author  Computer Science E-259
 * @version 8.0
 *
 * @author  Aliaksei Yeusiukou
 **/
public class Element extends Node
{
	private ArrayList<Attr> attributesList = new ArrayList<Attr>();
	
    /**
     * Sets node's name.
     *
     * @param   name    name for new element
     */
    public Element(String name)
    {
        setNodeName(name);
    }


    /**
     * Returns code (Node.ELEMENT_NODE) signifying this node's type.
     *
     * @return Node.ELEMENT_NODE
     */
    public int getNodeType()
    {
        return Node.ELEMENT_NODE;
    }
    
    /**
     *  Adds an attribute to the list.
     */
    public void addAtribute(Attr attribute){
    	attributesList.add(attribute);
    }
    
    /**
     * @return Iterator of all the attributes.
     */
    public Iterator<Attr> getAttrIterator(){
    	return attributesList.iterator();
    }
    
    /**
     * Returns all attributes of the element.
     * @return all attributes of the element
     */
    public Attributes getAttributes(){
    	Attributes attributes = new Attributes();
    	for(Attr attr:attributesList)
    		attributes.addAttribute(attr.getNodeName(), attr.getNodeValue());
    	return attributes;
    }
}
