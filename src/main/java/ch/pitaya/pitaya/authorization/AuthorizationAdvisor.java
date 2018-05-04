package ch.pitaya.pitaya.authorization;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationAdvisor implements PointcutAdvisor {

	private final StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
		@Override
		public boolean matches(Method method, Class<?> targetClass) {
			return method.isAnnotationPresent(Authorize.class) //
					|| method.isAnnotationPresent(AuthorizeCase.class)
					|| method.isAnnotationPresent(AuthorizeFile.class);
		}
	};

	@Autowired
	private MethodAuthorizationInterceptor interceptor;

	@Override
	public Pointcut getPointcut() {
		return this.pointcut;
	}

	@Override
	public Advice getAdvice() {
		return this.interceptor;
	}

	@Override
	public boolean isPerInstance() {
		return false;
	}

}
