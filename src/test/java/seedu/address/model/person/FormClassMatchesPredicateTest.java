package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PersonBuilder;

public class FormClassMatchesPredicateTest {

    @Test
    void test_formClassMatchesPredicate_returnsTrue() {
        FormClassMatchesPredicate predicate = new FormClassMatchesPredicate(new FormClass("SomeClass"));
        assertTrue(predicate.test(new PersonBuilder().withClass("SomeClass").build()));
    }

    @Test
    void test_formClassMatchesPredicate_returnsFalse() {
        FormClassMatchesPredicate predicate = new FormClassMatchesPredicate(new FormClass("SomeClass"));
        assertFalse(predicate.test(new PersonBuilder().withClass("NotSomeClass").build()));
    }

    @Test
    void equals() {
        FormClassMatchesPredicate firstPredicateClass = new FormClassMatchesPredicate(new FormClass("C1"));
        FormClassMatchesPredicate secondPredicateClass = new FormClassMatchesPredicate(new FormClass("C2"));
        FormClassMatchesPredicate thirdPredicateClass = new FormClassMatchesPredicate(new FormClass("C1"));

        assertEquals(firstPredicateClass, firstPredicateClass);

        assertEquals(firstPredicateClass, thirdPredicateClass);

        assertFalse(firstPredicateClass.equals(secondPredicateClass));

        assertFalse(firstPredicateClass.equals(null));
    }

    @Test
    void toStringMethod() {
        String keywords = "SomeClass";
        FormClassMatchesPredicate predicate = new FormClassMatchesPredicate(new FormClass(keywords));
        String expected = FormClassMatchesPredicate.class.getCanonicalName() + "{formClass=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
