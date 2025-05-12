package com.cakeshop.api_main.service.fiserv;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FiservWebhookPayload {
//    private int retryNumber;
    private String storeId;
    private String checkoutId;
    private String orderId;
    private String transactionType;
    private String transactionStatus;
//    private PaymentMethodUsed paymentMethodUsed;
    private Money approvedAmount;
//    private IpgTransactionDetails ipgTransactionDetails;

//    @Getter
//    @Setter
//    public static class PaymentMethodUsed {
//        private Cards cards;
//        private String paymentMethod;
//
//        @Getter
//        @Setter
//        public static class Cards {
//            private ExpiryDate expiryDate;
//            private String brand;
//
//            @Getter
//            @Setter
//            public static class ExpiryDate {
//                private String month;
//                private String year;
//            }
//        }
//    }
//
//    @Getter
//    @Setter
//    public static class ApprovedAmount {
//        private BigDecimal total;
//        private String currency;
//    }
//
//    @Getter
//    @Setter
//    public static class IpgTransactionDetails {
//        private String ipgTransactionId;
//        private String transactionStatus;
//        private String approvalCode;
//    }
}
