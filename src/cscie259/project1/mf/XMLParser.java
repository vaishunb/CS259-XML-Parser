package cscie259.project1.mf;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


/**
 * A simplified XML parser.  In essence, this class supports a subset
 * of the functionality collectively offered by javax.xml.parsers.SAXParser
 * and javax.xml.parsers.DocumentBuilder.
 *
 * You MAY modify this file.
 *
 * @author  Computer Science E-259
 * @version 8.0
 *
 * @author  Aliaksei Yeusiukou
 **/
public class XMLParser
{
    /**
     * Storage for input file's contents.
     */
    private String data_;


    /**
     * A reference to the currently registered DefaultHandler.
     */
    private DefaultHandler handler_;


    /**
     * Index of our current location in input file's contents.
     */
    private int index_ = 0;


    /**
     * Returns true if the next characters in the stream are the beginning
     * of an element's end tag.
     *
     * @return true iff next characters in the stream are the beginning
     * of an element's end tag
     */
    protected boolean isEndTag()
    {
        return (data_.charAt(index_) == '<')
        && (data_.charAt(index_ + 1) == '/');
    }


    /**
     * Returns true if the next character in the stream is the beginning
     * of an element's start tag.
     *
     * @return true iff next character in the stream is the beginning
     * of an element's start tag
     */
    protected boolean isStartTag()
    {
        return data_.charAt(index_) == '<';
    }


    /**
     * Parses the specified file, if possible, passing SAX events
     * to given handler.
     *
     * @param filename name of file whose contents are to be parsed
     * @param handler  DefaultHandler for SAX events
     */
    public void parse(String filename, DefaultHandler handler)
    {
        // initialize to clean up from any previous parse
        data_ = "";
        index_ = 0;
        handler_ = handler;

        // attempt to open file and read contents into local storage
        try
        {
            File f = new File(filename);
            int filesize = (int) f.length();
            byte[] filebytes = new byte[filesize];
            DataInputStream in = new DataInputStream(new FileInputStream(f));
            in.readFully(filebytes);
            in.close();
            data_ = new String(filebytes);
        }
        catch (IOException E)
        {
            handler_.fatalError(new Exception("Error: could not read file"));
            return;
        }

        // parse the document; hopefully there's a root element!
        handler_.startDocument();
        readElement();
        handler_.endDocument();
    }


    /**
     * Parses an element and its content.
     */
    protected void readElement()
    {
    	//Ignoring whitespaces before start tags.
    	while(!isStartTag()){
    		if(java.lang.Character.isWhitespace(data_.charAt(index_)))
        		index_++;
    		else handler_.fatalError(new RuntimeException("Error: expecting " + "start of element"));
    	}

        // parse start tag
        String name = readStartTag();
        
        // pass element
        handler_.startElement(name, readAttributes());

        // keep reading in more elements and text until an end tag
        // is encountered
        while (!isEndTag())
        {
            if (isStartTag())
                readElement();
            else
                readText();
        }

        // parse end tag, ensuring it matches most current start tag
        readEndTag(name);
    }


    /**
     * Parses an end tag, ensuring its name matches currently opened
     * element's name.
     *
     * @param checkName currently opened element's name with which
     * end tag should be compared
     */
    protected void readEndTag(String checkName)
    {
        // start name from scratch
        String name = "";

        // read starting <
        index_++;

        // read /
        index_++;

        // read name
        while (data_.charAt(index_) != '>' && data_.charAt(index_) !=' ')
        {
            name += data_.charAt(index_);
            index_++;
        }

        // read ending >
        index_++;

        // ensure content is well-formed
        if (!checkName.equals(name))
        {
            handler_.fatalError(new RuntimeException("Error: expecting " +
                                                     "closing tag for " + 
                                                     checkName));
            return;
        }

        // pass this SAX event to handler
        handler_.endElement(name);
    }


    /**
     * Parses a start tag, returning opened element's name.
     *
     * @return name of element
     */
    protected String readStartTag()
    {
        // start name from scratch
        String name = "";

        // Read starting <
        index_++;

        // Read name
        while (data_.charAt(index_) != '>' && data_.charAt(index_) != ' ')
        {
            name += data_.charAt(index_);
            index_++;
        }

        // return this element's name, for later comparision
        // with an end tag
        return name;
    }
    
    protected Attributes readAttributes(){
        String attributeName;
        String attributeValue;
        Attributes attributes = null;
        
        while (data_.charAt(index_) != '>'){
        	
        	attributeName = "";
            attributeValue = "";
        	if (java.lang.Character.isWhitespace(data_.charAt(index_))){
        		index_++;
        		continue;
        	}
        	
        	// Read attribute name
        	while (data_.charAt(index_)!= '='){

            	if( data_.charAt(index_) >= 'A' && data_.charAt(index_) <= 'Z'
            			|| data_.charAt(index_) >= 'a' && data_.charAt(index_) <= 'z'
            			|| data_.charAt(index_) >= 0 && data_.charAt(index_) <= 9
            			|| data_.charAt(index_) == '-'
            			|| data_.charAt(index_) == '.'
            			|| data_.charAt(index_) == '_' )
            		attributeName+=data_.charAt(index_);
            	else handler_.fatalError(new Exception("Inappropriate symbol \""+data_.charAt(index_)+"\" in the attribute name"));
            	index_++;
        	}
        	
        	if( data_.charAt(++index_) != '"' )
        		handler_.fatalError(new Exception("Error in the \""+attributeName+"\" attribute."));
        	
        	// Read attribute value
        	while (data_.charAt(++index_)!='"'){
        		if(data_.charAt(index_) == '<' || data_.charAt(index_) == '>'){
        			handler_.fatalError(new Exception("Inappropriate symbol \""+data_.charAt(index_)+"\""));
        			continue;
        		}
        		attributeValue+=data_.charAt(index_);
        	}
        	
        	if(data_.charAt(++index_)!=' ' && data_.charAt(index_)!='>')
        		handler_.fatalError(new Exception("Inappropriate symbol \""+data_.charAt(index_)+"\" after the attribute "+attributeName));

        	// Add the attribute
        	if(attributes == null)
        		attributes = new Attributes();
        	attributes.addAttribute(attributeName, attributeValue);
        }
        
        return attributes;
    }


    /**
     * Parses character data.
     */
    protected void readText()
    {
        // start character data from scratch
        String content = "";

        // accumulate characters until next tag
        while (data_.charAt(index_) != '<')
        {
            content += data_.charAt(index_);
            index_++;
        }

        // pass this SAX event to handler
        handler_.characters(content);
    }
}
