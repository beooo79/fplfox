package de.beooo79.fplfox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import de.beooo79.fplfox.model.bo.flightplan.Fix;
import de.beooo79.fplfox.model.bo.flightplan.Plan;
import de.beooo79.fplfox.model.bo.flightplan.Segment;

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
        assertEquals("", plan.getAdep());
        assertEquals("", plan.getAdes());
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

}
