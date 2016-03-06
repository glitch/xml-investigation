package dev.glitch;

import com.fasterxml.aalto.stax.InputFactoryImpl;
import org.codehaus.stax2.XMLInputFactory2;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple xml investigation
 */
public class App {
    public static void main(String[] args) throws XMLStreamException, IOException {
        String foo = "<foo>";
        foo += "<element attr1=\"attrValue1\" attr2=\"attrValue2\" attr3=\"attrValue3\"/>";
        foo += "<element attr1=\"overwriteValue1\" anotherAttr=\"anotherValue\"/>";
        foo += "</foo>";

        App app = new App();
        app.getXmlAttributesAsMap(new ByteArrayInputStream(foo.getBytes()));

    }

    /**
     * Really bare bones method for pulling attributes from an xmlInputStream.  This is a specialized method and has the following shortcomings:
     *   1. Reads the entire xml stream
     *   2. Puts attributes in the map for ALL ELEMENTS and overwrites them with the last value parsed if there is a name collision
     * @param xmlInputStream xml snippet wrapped in an input stream
     * @return Map of attribute names to value.  Attribute names are assumed unique in the input, otherwise they are overwritten with last value.
     * @throws XMLStreamException
     */
    public Map<String, String> getXmlAttributesAsMap(InputStream xmlInputStream) throws XMLStreamException, IOException {
//        XMLInputFactory2 xmlInputFactory = (XMLInputFactory2) InputFactoryImpl.newInstance();
//        XMLStreamReader2 xmlStreamReader = (XMLStreamReader2) xmlInputFactory.createXMLStreamReader(xmlInputStream);
        XMLStreamReader2 xmlStreamReader = (XMLStreamReader2) xmlInputFactory2ThreadLocal.get().createXMLStreamReader(xmlInputStream);

        Map<String, String> attrMap = new HashMap<String, String>();
        while (xmlStreamReader.hasNext()) {
            int eventType = xmlStreamReader.next();
            switch (eventType) {
                case XMLEvent.START_ELEMENT:
                    int attrCount = xmlStreamReader.getAttributeCount();
                    for (int i = 0; i < attrCount; i++) {
                        System.out.println("Found -> " + xmlStreamReader.getAttributeName(i) + " : " + xmlStreamReader.getAttributeValue(i));
                        attrMap.put(xmlStreamReader.getAttributeName(i).getLocalPart(), xmlStreamReader.getAttributeValue(i));
                    }
                    break;
                default:
                    //do nothing
            }
        }
        xmlInputStream.close();
        return attrMap;
    }

    final ThreadLocal<XMLInputFactory2> xmlInputFactory2ThreadLocal = new ThreadLocal<XMLInputFactory2>() {
        public XMLInputFactory2 initialValue() {
//            return (XMLInputFactory2) XMLInputFactory.newFactory("com.fasterxml.aalto.stax.InputFactoryImpl", ClassLoader.getSystemClassLoader());
            return (XMLInputFactory2) InputFactoryImpl.newInstance();
        }
    };

}
