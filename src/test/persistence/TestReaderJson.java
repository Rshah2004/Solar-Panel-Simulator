package persistence;


import model.SolarPanels;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
public class TestReaderJson {
    private JsonReader read;
    @Test
    void testJsonReaderWithNofile() {
        read = new JsonReader("./data/noSuchFileExists.json");
        try {
            SolarPanels sr = read.read();
            fail("System should lead to an exception");
        } catch (IOException e) {
            //passed
        }
    }

    @Test
    void testJsonReaderEmptyFile() {
        read = new JsonReader("./data/emptySolarPanels.json");
        try {
            SolarPanels sr = read.read();
            assertEquals(0, sr.calculateTotalEnergy());
            assertEquals(0, read.read().getSolarpanels().size());
        } catch (IOException e){
            fail("The system is not supposed to catch any exception");
        }
    }

    @Test
    void testMainfile() {
        read = new JsonReader("./data/NotemptySolarPAnels.json");
        try {
            SolarPanels sr = read.read();
            assertEquals(4, sr.getSolarpanels().size());
            assertEquals("Polycrystalline", sr.getSolarpanels().get(0).getSolarType());
            assertEquals("Monocrystalline", sr.getSolarpanels().get(1).getSolarType());
            assertEquals("Thinfilm", sr.getSolarpanels().get(2).getSolarType());
            assertEquals("Perc", sr.getSolarpanels().get(3).getSolarType());
            assertEquals(12, sr.getSolarpanels().get(0).getEfficiency());
        } catch(IOException e){
            fail("Aint supposed to catch any excpetion");
        }
    }
}
