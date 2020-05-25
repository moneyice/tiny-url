package tinyurl.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.text.StringEscapeUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class Base62Test {
    //  base62(315151157690118144)= NHOWrSHp6e
    //  base62(3406166854651)= xxyyzz1
    //  base62(713665082020215393)= qianyitian
    @Test
    public void base62Encode() {
        String resuslt = Base62.base62Encode(315151157690118144L);
        assertEquals("base62 encode", resuslt, "NHOWrSHp6e");

        resuslt = Base62.base62Encode(3406166854651L);
        assertEquals("base62 encode", resuslt, "xxyyzz1");

        resuslt = Base62.base62Encode(713665082020215393L);
        assertEquals("base62 encode", resuslt, "qianyitian");
    }

    @Test
    public void base62Decode() {

        long id = Base62.base62Decode("NHOWrSHp6e");
        assertEquals("base62 decode", id, 315151157690118144L);

        id = Base62.base62Decode("xxyyzz1");
        assertEquals("base62 decode", id, 3406166854651L);

        id = Base62.base62Decode("qianyitian");
        assertEquals("base62 decode", id, 713665082020215393L);

        id = Base62.base62Decode("NM5gIJcjpo");
        assertEquals("base62 decode", id, 316176483350228992L);

    }


    @Test
    public void base62DecodeError() {

        long id = Base62.base62Decode("#@");
        System.out.println(id);

    }

    @Test
    public void jsonContainsJson() throws JsonProcessingException {
        Map<String, String> map = new HashMap<>();
        String jsonString = "{\"url\":\"http://www.baidu.com\"}";
        map.put("body", jsonString);

        ObjectMapper jsonMapper = new ObjectMapper();
        String s = jsonMapper.writeValueAsString(map);
        System.out.println(s);

    }

    @Test
    public void genID() throws JsonProcessingException {
        SnowflakeSequence sequence = new SnowflakeSequence();
        for (int i = 0; i < 10; i++) {
            System.out.println(sequence.nextValue());
        }
    }
    @Test
    public void escapeHTML() throws JsonProcessingException {
        String s=StringEscapeUtils.escapeHtml4("<div>xdsf</div>");
        System.out.println(s);
        s=StringEscapeUtils.unescapeHtml4(s);
        System.out.println(s);

    }




}