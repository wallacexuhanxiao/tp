package seedu.address.model.person;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code FormClass} matches the FormClass given.
 */
public class FormClassMatchesPredicate implements Predicate<Person> {
    private final FormClass formClass;

    public FormClassMatchesPredicate(FormClass formClass) {
        this.formClass = formClass;
    }

    @Override
    public boolean test(Person person) {
        return person.getFormClass().equals(formClass);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FormClassMatchesPredicate)) {
            return false;
        }

        FormClassMatchesPredicate otherFormClass = (FormClassMatchesPredicate) other;
        return formClass.equals(otherFormClass.formClass);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("formClass", formClass).toString();
    }
}
