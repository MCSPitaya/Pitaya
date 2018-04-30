package ch.pitaya.pitaya.authorization;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MethodAuthorizationInterceptor implements MethodInterceptor {

	@Autowired
	private Authorization auth;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Authorize annotation = invocation.getMethod().getAnnotation(Authorize.class);
		AuthCode[] codes = annotation.value();
		auth.require(codes);
		return invocation.proceed();
	}

}
