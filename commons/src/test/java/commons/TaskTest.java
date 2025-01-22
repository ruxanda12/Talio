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

public class TaskTest {

    @Test
    public void checkEmptyConstructor() {
        var b = new Task();
        assertNull(b.title);
        assertNull(b.description);
        assertNull(b.subTasks);
        assertNull(b.parentFelloList);
        assertNull(b.background);
        assertNull(b.font);
        assertNull(b.border);
    }

    @Test
    public void checkConstructor() {
        var list = new ArrayList<SubTask>();
        var parent = new FelloList();
        var b = new Task("Maths Homework", "Exercises 3,4 and 7",
                list, parent, "blue", "black", "red", false);
        assertEquals("Maths Homework", b.title);
        assertEquals("Exercises 3,4 and 7", b.description);
        assertEquals(list, b.subTasks);
        assertEquals(parent, b.parentFelloList);
        assertEquals("blue", b.background);
        assertEquals("black", b.font);
        assertEquals("red", b.border);
    }

    @Test
    public void equalsHashCode() {
        var list = new ArrayList<SubTask>();
        var parent = new FelloList();
        var a = new Task("Maths Homework", "Exercises 3,4 and 7",
                list, parent, "blue", "black", "red", false);
        var b = new Task("Maths Homework", "Exercises 3,4 and 7",
                list, parent, "blue", "black", "red", false);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var list = new ArrayList<SubTask>();
        var parent = new FelloList();
        var a = new Task("Maths Homework", "Exercises 3,4 and 7",
                list, parent, "blue", "black", "red", false);
        var b = new Task("Chemistry Homework", "Exercises 3,4 and 7",
                list, parent, "blue", "black", "red", false);
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var list = new ArrayList<SubTask>();
        var parent = new FelloList();
        var actual = new Task("Maths Homework", "Exercises 3,4 and 7",
                list, parent, "blue", "black", "red", false).toString();
        assertTrue(actual.contains(Task.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("title"));
    }
}