package com.gianvittorio.estore.userservice.query;

import com.gianvittorio.estore.core.data.PaymentDetails;
import com.gianvittorio.estore.core.data.User;
import com.gianvittorio.estore.core.query.FetchUserPaymentDetailsQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UsersEventHandler {

    @QueryHandler
    public User handle(FetchUserPaymentDetailsQuery query) {
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .cardNumber("123Card")
                .cvv("123")
                .name("Gianvittorio Castellano")
                .validUntilYear(2030)
                .validUntilMonth(12)
                .build();

        User user = User.builder()
                .firstName("Gianvittorio")
                .lastName("Castellano")
                .userId(query.getUserId())
                .paymentDetails(paymentDetails)
                .build();

        return user;
    }
}
