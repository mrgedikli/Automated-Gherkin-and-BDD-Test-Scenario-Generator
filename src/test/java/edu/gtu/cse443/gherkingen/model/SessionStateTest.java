package edu.gtu.cse443.gherkingen.model;

import static edu.gtu.cse443.gherkingen.model.SessionState.EXPORTING;
import static edu.gtu.cse443.gherkingen.model.SessionState.FAILED;
import static edu.gtu.cse443.gherkingen.model.SessionState.FINISHED;
import static edu.gtu.cse443.gherkingen.model.SessionState.GENERATING;
import static edu.gtu.cse443.gherkingen.model.SessionState.LOADING;
import static edu.gtu.cse443.gherkingen.model.SessionState.PREVIEWING;
import static edu.gtu.cse443.gherkingen.model.SessionState.VALIDATING;
import static edu.gtu.cse443.gherkingen.model.SessionState.WAITING_FOR_CONFIRMATION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

/** Transitions of Figure 12 (state machine GenerationSession). */
class SessionStateTest {

    private static Set<SessionState> targetsOf(SessionState from) {
        Set<SessionState> targets = EnumSet.noneOf(SessionState.class);
        for (SessionState to : SessionState.values()) {
            if (from.canMoveTo(to)) {
                targets.add(to);
            }
        }
        return targets;
    }

    @Test
    void transitionsFollowFigure12() {
        assertEquals(EnumSet.of(VALIDATING, FAILED), targetsOf(LOADING));
        assertEquals(EnumSet.of(GENERATING, FAILED), targetsOf(VALIDATING));
        assertEquals(EnumSet.of(PREVIEWING), targetsOf(GENERATING));
        assertEquals(EnumSet.of(EXPORTING, FINISHED), targetsOf(PREVIEWING));
        assertEquals(EnumSet.of(WAITING_FOR_CONFIRMATION, FINISHED), targetsOf(EXPORTING));
        assertEquals(EnumSet.of(EXPORTING), targetsOf(WAITING_FOR_CONFIRMATION));
        assertEquals(EnumSet.noneOf(SessionState.class), targetsOf(FINISHED));
        assertEquals(EnumSet.noneOf(SessionState.class), targetsOf(FAILED));
    }

    @Test
    void exportWithOverwriteQuestionReachesFinished() {
        List<SessionState> path = List.of(LOADING, VALIDATING, GENERATING, PREVIEWING,
                EXPORTING, WAITING_FOR_CONFIRMATION, EXPORTING, FINISHED);
        assertEquals(LOADING, SessionState.initial());
        for (int i = 1; i < path.size(); i++) {
            assertTrue(path.get(i - 1).canMoveTo(path.get(i)), path.get(i - 1) + " -> " + path.get(i));
        }
        assertTrue(path.get(path.size() - 1).isFinal());
    }

    @Test
    void onlyFinishedAndFailedAreFinal() {
        for (SessionState state : SessionState.values()) {
            assertEquals(state == FINISHED || state == FAILED, state.isFinal(), state.name());
        }
        assertFalse(GENERATING.canMoveTo(FAILED));
    }
}
