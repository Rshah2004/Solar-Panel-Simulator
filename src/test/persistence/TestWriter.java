package persistence;


import model.Polycrystalline;
import model.SolarPanels;
import org.junit.jupiter.api.Test;


import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class TestWriter {

    @Test
    void testWrongFile() {
        JsonWriter json = new JsonWriter("./data/\0wrongfilename.json");
        try {
            json.open();
            fail("should not have opened the file");
        } catch (IOException e){
        }
    }

    @Test
    void testFullSolarPanels() {
        JsonWriter json = new JsonWriter("./data/workingFile.json");
        try {
            json.open();
            SolarPanels sr = new SolarPanels();
            sr.addSolarPanels(new Polycrystalline());
            sr.getSolarpanels().get(0).setEfficiency(0.24);
            json.write(sr);
            json.close();

            JsonReader json1 = new JsonReader("./data/workingFile.json");
            try {
                assertEquals(1, json1.read().getSolarpanels().size());
                assertEquals(0, json1.read().calculateTotalEnergy());
                assertEquals(0.24, json1.read().getSolarpanels().get(0).getEfficiency());

            } catch(IOException e) {
                fail("No exception supposed to be thrown");
            }
        } catch (IOException e) {
            fail("not supposed to catch any exception");
        }
    }

    @Test
    void testEmptySolarPanels() {
        JsonWriter json1 = new JsonWriter("./data/EmptyFile.json");
        try {
            json1.open();
            SolarPanels sr = new SolarPanels();
            json1.write(sr);
            json1.close();

            JsonReader json = new JsonReader("./data/EmptyFile.json");
            try {
                assertEquals(0, json.read().getSolarpanels().size());
                assertEquals(0, json.read().calculateTotalEnergy());
            } catch(IOException e) {
                fail("Excpetion not supposed to be thrown");
            }
        } catch(IOException e) {
            fail("not supposed to catch any exception");
        }
    }
}
