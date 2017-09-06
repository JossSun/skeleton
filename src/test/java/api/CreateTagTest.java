package api;


import io.dropwizard.jersey.validation.Validators;
import org.junit.Test;

import javax.validation.Validator;
import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class CreateTagTest {

    private final Validator validator = Validators.newValidator();

    @Test
    public void testValid() {
        CreateTagRequest tag = new CreateTagRequest();
        tag.tagName = "OK";


        assertThat(validator.validate(tag), empty());// validate(receipt) tries to determine whether the reipt is valid; empty means it passed
    }

    @Test
    public void testMissingTagname() {
        CreateTagRequest tag = new CreateTagRequest();
        // can pass without amount
        //receipt.amount = new BigDecimal(33.44);
        assertThat(validator.validate(tag), hasSize(1));
    }

}