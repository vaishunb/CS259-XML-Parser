package cscie259.project1.mf;

import java.io.BufferedWriter;
import java.io.IOException;


/**
 * A ContentHandler for SAX events that serializes (to an output stream)
 * the events back into XML.  Essentially, a simplified version of
 * org.apache.xml.serialize.XMLSerializer.
 *
 * You MAY modify this file to provide support for the
 * serialization of attributes.
 *
 * @author  Computer Science E-259
 * @version 8.0
 *
 * @author  YOUR NAME GOES HERE
 **/
public class XMLSerializer extends DefaultHandler
{
    /**
     * The output stream to which we are serializing.
     */
    private BufferedWriter out_;


    /**
     * A flag signifying whether output should be indented (i.e.,
     * pretty-printed).
     */
    private boolean prettyPrint_;


    /**
     * The current level of indentation, if applicable.
     */
    private int indentLevel_;


    /**
     * Configures XMLSerializer with given BufferedWriter and for
     * pretty-printedness, if applicable.
     *
     * @param writer      BufferedWriter for serialization
     * @param prettyPrint flag indicating whether to pretty-print
     */
    public XMLSerializer(BufferedWriter writer, boolean prettyPrint)
    {
        out_ = writer;
        prettyPrint_ = prettyPrint;
        indentLevel_ = 0;
    }


    /**
     * Prints out character data, pretty-printed if applicable.
     *
     * @param content  character data
     */
    public void characters(String content)
    {
        try
        {
            // pretty-print if applicable
            if (prettyPrint_)
                indent();

            // write character data
            out_.write(content, 0, content.length());

            // pretty-print if applicable
            if (prettyPrint_)
                out_.newLine();
        }
        catch (IOException E)
        {
            throw new RuntimeException("Error: I/O error " + E.getMessage());
        }
    }


    /**
     * Closes the output stream.
     */
    public void endDocument()
    {
        try
        {
            out_.close();
        }
        catch (IOException E)
        {
            throw new RuntimeException("Error: I/O error " + E.getMessage());
        }
    }


    /**
     * Prints out the end element tag, pretty-printed if applicable, and
     * updates the current level of indentation.
     *
     * @param name name of element
     */
    public void endElement(String name)
    {
        try
        {
            // pretty-print if applicable
            if (prettyPrint_)
            {
                indentLevel_--;
                indent();
            }

            // write </
            out_.write('<');
            out_.write('/');

            // write element's name
            out_.write(name, 0, name.length());

            // write >
            out_.write('>');

            // pretty-print if applicable
            if (prettyPrint_)
                out_.newLine();
        }
        catch (IOException E)
        {
            throw new RuntimeException("Error: I/O error " + E.getMessage());
        }
    }


    /**
     * Prints the number of indents currently appropriate.
     *
     * @throws IOException run-time failure of writing operation
     */
    private void indent() throws IOException
    {
        for (int i = 0; i < indentLevel_; i++)
        {
            out_.write("    ");
        }
    }


    /**
     * Prints out the start element tag, pretty-printed if applicable, and
     * updates the current level of indentation.
     *
     * @param name name of element
     * @param atts element's collection of attributes
     */
    public void startElement(String name, Attributes atts)
    {
        try
        {
            // pretty-print if applicable
            if (prettyPrint_)
                indent();

            // write <
            out_.write('<');

            // write element's name
            out_.write(name, 0, name.length());

            // write element's attributes, if any
            // TODO

            // write >
            out_.write('>');

            // pretty-print if applicable
            if (prettyPrint_)
            {
                out_.newLine();
                indentLevel_++;
            }
        }
        catch (IOException E)
        {
            throw new RuntimeException("Error: I/O error " + E.getMessage());
        }
    }
}
