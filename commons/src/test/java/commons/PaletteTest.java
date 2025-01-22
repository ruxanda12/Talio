package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaletteTest {

    @Test
    public void checkEmptyConstructor() {
        var b = new Palette();
        assertNull(b.parentBoard);
        assertNull(b.title);
        assertFalse(b.isDefault);
        assertNull(b.background);
        assertNull(b.font);
        assertNull(b.border);
    }

    @Test
    public void checkConstructor() {
        var x = new Board();
        var b = new Palette(x, "Dark Theme", true, "blue", "black", "gray");
        assertEquals(x, b.parentBoard);
        assertEquals("Dark Theme", b.title);
        assertEquals(true, b.isDefault);
        assertEquals("blue", b.background);
        assertEquals("black", b.font);
        assertEquals("gray", b.border);
    }

    @Test
    public void equalsHashCode() {
        var x = new Board();
        var a = new Palette(x, "Dark Theme", true, "blue", "black", "gray");
        var b = new Palette(x, "Dark Theme", true, "blue", "black", "gray");
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        var x = new Board();
        var a = new Palette(x, "Light Theme", true, "blue", "black", "gray");
        var b = new Palette(x, "Dark Theme", true, "blue", "black", "gray");
        assertNotEquals(a, b);
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void hasToString() {
        var x = new Board();
        var actual = new Palette(x, "Dark Theme", true, "blue",
                "black", "gray").toString();
        assertTrue(actual.contains(Palette.class.getSimpleName()));
        assertTrue(actual.contains("\n"));
        assertTrue(actual.contains("blue"));
    }
}
