package ch.epfl.sdp.game;

import org.junit.Test;

import static ch.epfl.sdp.game.TimerHelper.millisToTimeString;
import static org.junit.Assert.*;

public class TimerHelperTest {

    @Test
    public void millisToTimeStringTest() {
        assertEquals("0:02", millisToTimeString(2000));
        assertEquals("0:00", millisToTimeString(0));
        assertEquals("2:00", millisToTimeString(120000));
        assertEquals("1:35", millisToTimeString(95000));
    }
}