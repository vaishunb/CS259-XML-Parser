package cscie259.project1;

import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * A program for converting elements' attributes to child elements.
 *
 * You MAY modify this file.
 *
 * @author  Computer Science E-259
 * @version 8.0
 *
 * @author  Aliaksei Yeusiukou
 **/
public class AttributeConverter extends DefaultHandler
{
    /**
     * An instance of XMLSerializer for writing
     * to standard out
     */
	private XMLSerializer xmlSerializer;
	
	public AttributeConverter(XMLSerializer serializer){
		xmlSerializer = serializer;
		try {
			xmlSerializer.asDocumentHandler();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    /**
     * Main entry point to program.
     *
     * @param argv [0] - filename
     */
    public static void main(String[] argv)
    {
        // grab filename from command line
        if (argv.length != 1)
        {
            System.err.println(
                "usage: java " + "cscie259.project1.AttributeConverter "
                + "filename");
            System.exit(1);
        }
        String filename = argv[0];

        // create a serializer with which to pretty print our output
        XMLSerializer serializer = new XMLSerializer(
                System.out,
                new OutputFormat("XML", "UTF-8", true));

        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        
        SAXParser parser = null;
        try {
			parser = parserFactory.newSAXParser();
			parser.parse(filename, new AttributeConverter(serializer));
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
               
        
    }

	
	/**
     * Add converting to the overrided method
     *
     * @param uri - the namespace uri
     * @param localName - the local name
     * @param qName - the qualified name
     * @param attributes - the attributes attached to the element
     */
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		xmlSerializer.startElement(uri, localName, qName, null);
		
		for(int i=0; i<attributes.getLength(); i++){
			String attributeName = attributes.getQName(i);
			String attributeValue = attributes.getValue(i);
			
			xmlSerializer.startElement(attributeName, null);
			xmlSerializer.characters(attributeValue.toCharArray(), 0, attributeValue.length());
			xmlSerializer.endElement(attributeName);
		}
	}
	
	// Just redirect methods calls to our xmlserializer
	@Override
	public void endElement(String arg0, String arg1, String arg2) throws SAXException {
		xmlSerializer.endElement(arg0, arg1, arg2);
	}
		

	@Override
	public void characters(char[] arg0, int arg1, int arg2) throws SAXException {
		xmlSerializer.characters(arg0, arg1, arg2);
	}
    
    
}
