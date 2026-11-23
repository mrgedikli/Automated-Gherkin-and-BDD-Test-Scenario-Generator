package edu.gtu.cse443.gherkingen.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

class RequirementTest {

    /** REQ-3 of samples/login.txt. */
    private static EarsRequirement req3() {
        return new EarsRequirement("REQ-3",
                "While the user is on the login page, when the user enters a wrong password,"
                        + " the system shall display an error message.",
                4, "User login",
                List.of(new Clause(ClauseKind.WHILE, "the user is on the login page"),
                        new Clause(ClauseKind.WHEN, "the user enters a wrong password"),
                        new Clause(ClauseKind.SHALL, "display an error message")));
    }

    @Test
    void combinedRequirementHasBothPatterns() {
        EarsRequirement r = req3();
        assertEquals(Set.of(EarsPattern.STATE_DRIVEN, EarsPattern.EVENT_DRIVEN), r.patterns());
        assertTrue(r.hasResponse());
        assertFalse(r.isCompound());
        assertEquals(2, r.preconditionClauses().size());
        assertEquals(InputFormat.EARS, r.inputFormat());
        assertEquals(Optional.of("User login"), r.featureName());
    }

    @Test
    void responseOnlyRequirementIsUbiquitous() {
        EarsRequirement r = new EarsRequirement("REQ-10",
                "The system shall mask all but the last four digits of the card number.", 10, null,
                List.of(new Clause(ClauseKind.SHALL, "mask all but the last four digits of the card number")));
        assertEquals(Set.of(EarsPattern.UBIQUITOUS), r.patterns());
        assertTrue(r.isUbiquitous());
        assertEquals(Optional.empty(), r.featureName());
    }

    @Test
    void requirementWithoutShallHasNoResponse() {
        // REQ-4 of samples/payment.txt (FR-32)
        EarsRequirement r = new EarsRequirement("REQ-4",
                "When the user clicks Pay, the payment is processed.", 4, null,
                List.of(new Clause(ClauseKind.WHEN, "the user clicks Pay, the payment is processed")));
        assertFalse(r.hasResponse());
    }

    @Test
    void twoShallClausesMakeACompoundRequirement() {
        EarsRequirement r = new EarsRequirement("L1", "The system shall a and shall b.", 1, null,
                List.of(new Clause(ClauseKind.SHALL, "a"), new Clause(ClauseKind.SHALL, "b")));
        assertTrue(r.isCompound());
    }

    @Test
    void statusFollowsTheStateMachine() {
        EarsRequirement r = req3();
        assertEquals(RequirementStatus.LOADED, r.status());
        r.markValid();
        assertTrue(r.isValid());
        r.markTranslated();
        assertEquals(RequirementStatus.TRANSLATED, r.status());
        assertThrows(IllegalStateException.class, r::markInvalid);
    }

    @Test
    void invalidRequirementCannotBeTranslated() {
        EarsRequirement r = req3();
        r.markInvalid();
        assertThrows(IllegalStateException.class, r::markTranslated);
        assertTrue(r.status().isFinal());
    }

    @Test
    void rejectsBadArguments() {
        List<Clause> clauses = List.of(new Clause(ClauseKind.SHALL, "x"));
        assertThrows(IllegalArgumentException.class, () -> new EarsRequirement(" ", "t", 1, null, clauses));
        assertThrows(IllegalArgumentException.class, () -> new EarsRequirement("R", "t", 0, null, clauses));
        assertThrows(IllegalArgumentException.class, () -> new EarsRequirement("R", "t", 1, null, List.of()));
        assertThrows(IllegalArgumentException.class, () -> new Clause(ClauseKind.WHEN, "a\nb"));
    }

    @Test
    void userStoryKeepsItsCriteria() {
        UserStory story = new UserStory("L1",
                "As a customer, I want to add items to the cart, so that I can buy several products in one order.",
                1, null, "customer", "to add items to the cart", "I can buy several products in one order");
        story.addAcceptanceCriterion(new AcceptanceCriterion(List.of(
                "Given the product is in stock",
                "When the customer adds the product to the cart",
                "Then the cart shows the product")));
        assertTrue(story.isComplete());
        assertEquals(1, story.acceptanceCriteria().size());
        assertEquals(InputFormat.USER_STORY, story.inputFormat());
        assertThrows(UnsupportedOperationException.class, () -> story.acceptanceCriteria().clear());
    }

    @Test
    void userStoryWithoutGoalIsIncomplete() {
        UserStory story = new UserStory("L1", "As a customer.", 1, null, "customer", "", null);
        assertFalse(story.isComplete());
        assertEquals(Optional.empty(), story.benefit());
    }

    @Test
    void acceptedNormalizationMovesFreeTextToNormalized() {
        FreeTextRequirement free = new FreeTextRequirement("L1", "Make the app fast", 2, null);
        assertThrows(IllegalStateException.class, free::acceptNormalization);
        free.recordNormalization(new EarsRequirement("L1",
                "The system shall respond within 2 seconds.", 2, null,
                List.of(new Clause(ClauseKind.SHALL, "respond within 2 seconds"))));
        free.acceptNormalization();
        assertEquals(RequirementStatus.NORMALIZED, free.status());
        assertEquals(Optional.of(true), free.accepted());
        free.markValid();
        assertTrue(free.isValid());
    }

    @Test
    void rejectedNormalizationMakesFreeTextInvalid() {
        FreeTextRequirement free = new FreeTextRequirement("L1", "Make the app fast", 2, null);
        free.recordNormalization(new EarsRequirement("L1", "The system shall be fast.", 2, null,
                List.of(new Clause(ClauseKind.SHALL, "be fast"))));
        free.rejectNormalization();
        assertTrue(free.isInvalid());
        assertEquals(Optional.of(false), free.accepted());
    }

    @Test
    void normalizationMustKeepIdAndLine() {
        FreeTextRequirement free = new FreeTextRequirement("L1", "Make the app fast", 2, null);
        EarsRequirement other = new EarsRequirement("L9", "The system shall be fast.", 9, null,
                List.of(new Clause(ClauseKind.SHALL, "be fast")));
        assertThrows(IllegalArgumentException.class, () -> free.recordNormalization(other));
    }
}
