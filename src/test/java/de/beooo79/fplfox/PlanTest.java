package de.beooo79.fplfox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import de.beooo79.fplfox.model.bo.flightplan.Airway;
import de.beooo79.fplfox.model.bo.flightplan.Fix;
import de.beooo79.fplfox.model.bo.flightplan.Plan;
import de.beooo79.fplfox.model.bo.flightplan.RadioBeacon;

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
        plan.remove(new Fix("BLAWA"));
        assertEquals(0, plan.size());
    }

    @Test
    void addAndRemoveSegmentsMore() {
        Fix a = new Fix("GIVMI");
        Fix b = new Fix("BAYWA");
        plan.add(a);
        plan.add(b);
        plan.remove(a);
        assertFalse(plan.contains(a));
        assertTrue(plan.contains(b));
        plan.add(a);
        assertTrue(plan.contains(a));
    }

    @Test
    void segmentsSuccessor() {
        Fix a = new Fix("GIVMI");
        Fix b = new Fix("BAYWA");
        plan.add(a);
        plan.add(b);
        assertTrue(plan.hasNext(a));
        assertEquals(b, plan.next(a));
        assertFalse(plan.hasNext(b));
        assertEquals(null, plan.next(b));
    }

    @Test
    void segmentsPredecessor() {
        Fix a = new Fix("GIVMI");
        Fix b = new Fix("BAYWA");
        plan.add(a);
        plan.add(b);
        assertFalse(plan.hasPrevious(a));
        assertEquals(a, plan.previous(b));
        assertTrue(plan.hasPrevious(b));
        assertEquals(null, plan.previous(a));
    }

    @Test
    void aerodromesAndRouteFinder() {
        plan.setADEP("EDDF");
        plan.setADEP("SBGR");
        plan.setRoute(
                "ANEKI Y163 HERBI Y164 OLBEN UN869 AGN UL866 PPN UN10 VASUM UN857 MIA UW50 SEBTA W8 RDE W45 PETRI");
        assertTrue(plan.size() > 0);
        Fix a = new Fix("HERBI");
        Airway b = new Airway("Y164");
        RadioBeacon c = new RadioBeacon("RDE");
        assertTrue(plan.contains(a));
        assertTrue(plan.contains(b));
        assertTrue(plan.contains(c));
        assertTrue(plan.hasNext(a));
        assertEquals(b, plan.next(a));
        assertFalse(plan.hasNext(new Fix("PETRI")));
    }

}
