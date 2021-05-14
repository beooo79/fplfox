package de.beooo79.fplfox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import de.beooo79.fplfox.model.bo.flightplan.Airway;
import de.beooo79.fplfox.model.bo.flightplan.Fix;
import de.beooo79.fplfox.model.bo.flightplan.Plan;

@SpringBootTest
public class PlanTest {

    private Plan plan;

    @BeforeEach
    void setup() {
        this.plan = new Plan();
    }

    @Test
    void newPlanHasZeroWaypointsAndDefaultFields() {
        assertEquals(0, plan.size());
        assertEquals("IFPL", plan.getTitle());
        assertEquals("", plan.getADEP());
        assertEquals("", plan.getADES());
    }

    @Test
    void newPlanRawRoutesAreEmpty() {
        assertNotNull(plan.getAdexp());
        assertNotNull(plan.getIcao());
        assertEquals(0, plan.getAdexp().size());
        assertEquals(0, plan.getIcao().size());
    }

    @Test
    void addAndRemoveSegments() {
        assertEquals(0, plan.size());
        plan.add(new Fix("GIVMI"));
        plan.add(new Fix("BAYWA"));
        assertEquals(2, plan.size());
        plan.remove(new Fix("BLAWA"));
        assertEquals(1, plan.size());
    }

    @Test
    void aerodromesAndRoute() {
        plan.setADEP("EDDF");
        plan.setADEP("SBGR");
        plan.setRoute(
                "ANEKI Y163 HERBI Y164 OLBEN UN869 AGN UL866 PPN UN10 VASUM UN857 MIA UW50 SEBTA W8 RDE W45 PETRI");
        assertTrue(plan.size() > 0);
        Fix a = new Fix("HERBI");
        Airway b = new Airway("Y164");
        assertTrue(plan.contains(a));
        assertTrue(plan.contains(b));
        assertTrue(plan.hasSuccessor(a));
        assertEquals(b, plan.successor(a));
    }
}
