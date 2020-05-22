package tinyurl.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class LocalDateTimeConverterTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void convert() {
        LocalDateTimeConverter converter=new LocalDateTimeConverter();
        LocalDateTime time=LocalDateTime.of(2020,5,22,10,30,22);
        String timeString=converter.convert(time);
        assertEquals("LocalDateTime convert","2020-05-22T10:30:22",timeString);
    }

    @Test
    public void unconvert() {
        LocalDateTimeConverter converter=new LocalDateTimeConverter();
        LocalDateTime expected=LocalDateTime.of(2020,5,22,10,30,22);
        LocalDateTime actual=converter.unconvert("2020-05-22T10:30:22");
        assertEquals("LocalDateTime unconvert",expected,actual);
    }
}