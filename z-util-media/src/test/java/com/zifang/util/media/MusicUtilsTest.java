package com.zifang.util.media;

import org.junit.Test;
import static org.junit.Assert.*;

public class MusicUtilsTest {

    @Test
    public void testMusicUtilsExists() {
        MusicUtils utils = new MusicUtils();
        assertNotNull(utils);
    }
}
