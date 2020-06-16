package demo.jdkProxyDemo.impl;

import demo.jdkProxyDemo.ToCPayment;

public class ToCPaymentImpl implements ToCPayment {

    @Override
    public void pay() {
        System.out.println("以用户的名义支付了X元钱");
    }
}
