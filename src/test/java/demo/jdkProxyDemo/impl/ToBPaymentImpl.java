package demo.jdkProxyDemo.impl;

import demo.jdkProxyDemo.ToBPayment;

public class ToBPaymentImpl implements ToBPayment {
    @Override
    public void pay() {
        System.out.println("以公司的名义支付了X元钱");
    }
}
