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

public class SubTaskTest {

    @Test
    public void checkEmptyConstructor() {
        var b = new SubTask();
        assertNull(b.title);
        assertNull(b.parentTask);
        assertFalse(b.done);
    }

    @Test
    public void checkConstructor() {
        var parent = new Task();
        var b = new SubTask("Drink water", parent, false);
        assertEquals("Drink water", b.title);
        assertEquals(parent, b.parentTask);
        assertEquals(false, b.done);
    }

    @Test
    public void equalsHashCode() {
        var parent = new Task();
        var a = new SubTask("Drink water", parent, false);
        var b = new SubTask("Drink water", parent, false);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var parent = new Task();
        var a = new SubTask("Drink water", parent, false);
        var b = new SubTask("Drink beer", parent, false);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var parent = new Task();
        var actual = new SubTask("Drink beer", parent, false).toString();
        assertTrue(actual.contains(SubTask.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("title"));
    }
}