package helloworld.util;

import org.junit.Test;

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
    }


}