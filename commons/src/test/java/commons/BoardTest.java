/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    public void checkEmptyConstructor() {
        var b = new Board();
        assertNull(b.title);
        assertFalse(b.prot);
        assertNull(b.password);
        assertNull(b.felloLists);
        assertNull(b.background);
        assertNull(b.font);
        assertNull(b.border);
        assertNull(b.listBackground);
        assertNull(b.listFont);
        assertNull(b.listBorder);
    }

    @Test
    public void checkConstructor() {
        var list = new ArrayList<FelloList>();
        var tags = new ArrayList<Tags>();
        var palettes = new ArrayList<Palette>();
        var b = new Board("School Assignments", true, "penguin", list, tags, palettes,
                "blue", "black", "red", "blue", "black", "red");
        assertEquals("School Assignments", b.title);
        assertEquals(true, b.prot);
        assertEquals("penguin", b.password);
        assertEquals(list, b.felloLists);
        assertEquals("blue", b.background);
        assertEquals("black", b.font);
        assertEquals("red", b.border);
        assertEquals("blue", b.listBackground);
        assertEquals("black", b.listFont);
        assertEquals("red", b.listBorder);
    }

    @Test
    public void equalsHashCode() {
        var list = new ArrayList<FelloList>();
        var tags = new ArrayList<Tags>();
        var palettes = new ArrayList<Palette>();
        var a = new Board("School Assignments", true, "penguin", list, tags, palettes,
                "blue", "black", "red", "blue", "black", "red");
        var b = new Board("School Assignments", true, "penguin", list, tags, palettes,
                "blue", "black", "red", "blue", "black", "red");
        b.key = a.key;
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var list = new ArrayList<FelloList>();
        var tags = new ArrayList<Tags>();
        var palettes = new ArrayList<Palette>();
        var a = new Board("School Assignments", true, "penguin", list, tags, palettes,
                "blue", "black", "red", "blue", "black", "red");
        var b = new Board("School Assignments", true, "kitty", list, tags, palettes,
                "blue", "black", "red", "blue", "black", "red");
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var list = new ArrayList<FelloList>();
        var tags = new ArrayList<Tags>();
        var palettes = new ArrayList<Palette>();
        var actual = new Board("School Assignments", true, "kitty", list, tags, palettes,
                "blue", "black", "red", "blue", "black", "red").toString();
        assertTrue(actual.contains(Board.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("password"));
    }

    @Test
    public void hasGetTitle() {
        var list = new ArrayList<FelloList>();
        var tags = new ArrayList<Tags>();
        var palettes = new ArrayList<Palette>();
        var board = new Board("School Assignments", true, "kitty", list, tags, palettes,
                "blue", "black", "red", "blue", "black", "red");
        assertEquals(board.title, board.getTitle());
    }
}