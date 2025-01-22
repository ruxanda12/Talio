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

public class FelloListTest {

    @Test
    public void checkEmptyConstructor() {
        var b = new FelloList();
        assertNull(b.title);
        assertNull(b.tasks);
        assertNull(b.parentBoard);
        assertNull(b.background);
        assertNull(b.font);
        assertNull(b.border);
        assertFalse(b.edited);
    }

    @Test
    public void checkConstructor() {
        var list = new ArrayList<Task>();
        var parent = new Board();
        var b = new FelloList("TO DO", list, parent, "blue", "black", "red", false);
        assertEquals("TO DO", b.title);
        assertEquals(list, b.tasks);
        assertEquals(parent, b.parentBoard);
        assertEquals("blue", b.background);
        assertEquals("black", b.font);
        assertEquals("red", b.border);
        assertEquals(false, b.edited);
    }

    @Test
    public void equalsHashCode() {
        var list = new ArrayList<Task>();
        var parent = new Board();
        var a = new FelloList("TO DO", list, parent, "blue", "black", "red", false);
        var b = new FelloList("TO DO", list, parent, "blue", "black", "red", false);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var list = new ArrayList<Task>();
        var parent = new Board();
        var a = new FelloList("TO DO TODAY", list, parent, "blue", "black", "red", false);
        var b = new FelloList("TO DO", list, parent, "blue", "black", "red", false);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var list = new ArrayList<Task>();
        var parent = new Board();
        var actual = new FelloList("TO DO", list, parent, "blue", "black", "red", false).toString();
        assertTrue(actual.contains(FelloList.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("title"));
    }
}