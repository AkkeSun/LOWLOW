package church.lowlow.rest_api.accounting.resource;

import church.lowlow.rest_api.accounting.controller.AccountingController;
import church.lowlow.rest_api.accounting.db.Accounting;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

public class AccountingResource extends Resource<Accounting> {

    public AccountingResource(Accounting notice, Link... links) {
        super(notice, links);
        add(linkTo(AccountingController.class).slash(notice.getId()).withSelfRel()); // _self
    }
}
