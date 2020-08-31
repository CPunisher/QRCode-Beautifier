package com.cpunisher.qrcodebeautifier;

import android.graphics.Color;
import com.cpunisher.qrcodebeautifier.util.ColorHelper;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void toColorHex_isCorrect() {
        assertEquals("#123FFF", ColorHelper.toColorHex(Color.parseColor("#123FFF")));
    }
}