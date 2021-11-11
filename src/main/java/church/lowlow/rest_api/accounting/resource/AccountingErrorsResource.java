package church.lowlow.rest_api.accounting.resource;

import church.lowlow.rest_api.accounting.controller.AccountingController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.validation.Errors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class AccountingErrorsResource extends Resource<Errors> {
    public AccountingErrorsResource(Errors errors, Link... links) {
        super(errors, links);
        add(linkTo(AccountingController.class).withRel("index"));
    }
}