package dev.glitch;


import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void testApp() throws XMLStreamException, IOException {
        String foo = "<foo>";
        foo += "<element attr1=\"attrValue1\" attr2=\"attrValue2\" attr3=\"attrValue3\"/>";
        foo += "<element attr1=\"overwriteValue1\" anotherAttr=\"anotherValue\"/>";
        foo += "</foo>";

        App app = new App();
        Map<String, String> attrs = app.getXmlAttributesAsMap(new ByteArrayInputStream(foo.getBytes()));

        Map<String, String> expectedMap = new HashMap<String, String>();
        expectedMap.put("attr1", "overwriteValue1");
        expectedMap.put("attr2", "attrValue2");
        expectedMap.put("attr3", "attrValue3");
        expectedMap.put("anotherAttr", "anotherValue");

        Assert.assertTrue(CollectionUtils.isEqualCollection(expectedMap.entrySet(), attrs.entrySet()));

    }
}
