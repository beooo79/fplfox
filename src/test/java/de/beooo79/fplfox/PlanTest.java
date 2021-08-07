package de.beooo79.fplfox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import de.beooo79.fplfox.model.bo.flightplan.Airway;
import de.beooo79.fplfox.model.bo.flightplan.Fix;
import de.beooo79.fplfox.model.bo.flightplan.Plan;
import de.beooo79.fplfox.model.bo.flightplan.RadioBeacon;
import de.beooo79.fplfox.model.bo.flightplan.Segment;

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
        assertEquals("", plan.getAerodromeOfDeparture());
        assertEquals("", plan.getAerodromeOfDestination());
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
        Fix a = new Fix("GIVMI");
        Fix b = new Fix("BAYWA");

        assertEquals(0, plan.size());
        plan.add(a);
        plan.add(b);
        assertEquals(2, plan.size());
        plan.remove(a);
        plan.remove(b);
        plan.remove(b);
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
    void addSegmentsAfter() {
        Fix a = new Fix("GIVMI");
        Fix b = new Fix("BAYWA");
        Segment c = new RadioBeacon("BAYWA");

        plan.add(a);
        plan.addAfter(a, b);
        assertTrue(plan.hasNext(a));
        assertEquals(b, plan.next(a));

        plan.addAfter(a, c);
        assertEquals(c, plan.next(a));
    }

    @Test
    void removeSegments() {
        Fix a = new Fix("GIVMI");
        Fix b = new Fix("BAYWA");
        Segment c = new RadioBeacon("BAYWA");

        plan.add(a);
        plan.add(b);
        plan.add(c);
        plan.remove(c);
        assertEquals(2, plan.size());
        assertTrue(plan.contains(a));
        assertTrue(plan.contains(b));
        assertFalse(plan.contains(c));

    }

    @Test
    void segmentsSuccessor() {
        Fix a = new Fix("GIVMI");
        Fix b = new Fix("BAYWA");
        Segment c = new RadioBeacon("BAYWA");
        plan.add(a);
        plan.add(b);
        plan.add(c);
        assertTrue(plan.hasNext(a));
        assertEquals(b, plan.next(a));
        assertTrue(plan.hasNext(b));
        assertEquals(c, plan.next(b));
        assertFalse(plan.hasNext(c));
        assertEquals(null, plan.next(c));
    }

    @Test
    void segmentsPredecessor() {
        Fix a = new Fix("GIVMI");
        Fix b = new Fix("BAYWA");
        Segment c = new RadioBeacon("BAYWA");
        plan.add(a);
        plan.add(b);
        plan.add(c);
        assertFalse(plan.hasPrevious(a));
        assertEquals(null, plan.previous(a));
        assertTrue(plan.hasPrevious(b));
        assertEquals(a, plan.previous(b));
        assertTrue(plan.hasPrevious(c));
        assertEquals(b, plan.previous(c));
    }

    @Test
    void aerodromesAndRouteFinder() {
        plan.setAerodromeOfDeparture("EDDF");
        plan.setAerodromeOfDestination("SBGR");
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

    @Test
    void testArraySum() {
        List<Long> ar = List.of(1000000L, 20000000000L, 3000000000L, 40000000L, 50000L, 6000L, 700L);
        assertFalse(ar.isEmpty());
        assertEquals(28, ar.stream().mapToLong(Long::longValue).sum());
    }

    @Test
    void compareTriplets() {
        List<Integer> a = List.of(17, 28, 30);
        List<Integer> b = List.of(99, 16, 8);

        int sumA = 0;
        int sumB = 0;

        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) > b.get(i))
                sumA++;
            if (b.get(i) > a.get(i))
                sumB++;
        }

        List<Integer> result = Arrays.asList(sumA, sumB);
        assertFalse(result.isEmpty());
        assertEquals(Arrays.asList(2, 1), result);
    }

    @Test
    void parallelStream() {
        List<Integer> a = IntStream.range(1, 10001).boxed().toList();
        a.parallelStream().forEachOrdered(System.out::println);
        assertNotNull(a);
    }
}
