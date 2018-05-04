package ch.pitaya.pitaya.authorization;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ch.pitaya.pitaya.exception.AppException;
import ch.pitaya.pitaya.model.Case;
import ch.pitaya.pitaya.model.File;

@Component
public class MethodAuthorizationInterceptor implements MethodInterceptor {

	@Autowired
	private Authorization auth;

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		for (Annotation annotation2 : invocation.getMethod().getAnnotations()) {
			if (annotation2.annotationType().isAssignableFrom(Authorize.class))
				// @Authorize
				auth.require(((Authorize) annotation2).value());
			else if (annotation2.annotationType().isAssignableFrom(AuthorizeCase.class)) {
				// @AuthorizeCase
				AuthorizeCase annot = (AuthorizeCase) annotation2;
				doCaseAuth(invocation, annot.value(), annot.param());
			} else if (annotation2.annotationType().isAssignableFrom(AuthorizeFile.class)) {
				// @AuthorizeFile
				AuthorizeFile annot = (AuthorizeFile) annotation2;
				doFileAuth(invocation, annot.value(), annot.param());
			}
		}

		return invocation.proceed();
	}

	private void doCaseAuth(MethodInvocation invocation, AuthCode[] codes, String param) {
		Case caze = findParam(invocation, Case.class, param);
		auth.require(caze, codes);
	}

	private void doFileAuth(MethodInvocation invocation, AuthCode[] codes, String param) {
		File file = findParam(invocation, File.class, param);
		auth.require(file, codes);
	}

	@SuppressWarnings("unchecked")
	private <T> T findParam(MethodInvocation invocation, Class<T> clazz, String param) {
		int index = findIndex(invocation.getMethod(), clazz, param);
		switch (index) {
		case -1:
			throw new AppException("could not find parameter '" + param + "' of type " + clazz.getName());
		case -2:
			throw new AppException("ambiguous: found multiple parameters of type " + clazz.getName());
		case -3:
			throw new AppException("parameter '" + param + "' is not of type " + clazz.getName());
		default:
			return (T) invocation.getArguments()[index];
		}
	}

	// TODO: caching
	private int findIndex(Method method, Class<?> clazz, String param) {
		if (StringUtils.isEmpty(param))
			return findUnnamed(method, clazz);
		return findNamed(method, clazz, param);
	}

	private int findNamed(Method method, Class<?> clazz, String param) {
		Parameter[] parameters = method.getParameters();
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			if (parameter.getName().equals(param)) {
				if (parameter.getType().isAssignableFrom(clazz))
					return i;
				else
					return -3; // TYPE ERROR
			}
		}
		return -1; // NOT FOUND
	}

	private int findUnnamed(Method method, Class<?> clazz) {
		int index = -1;
		for (int i = 0; i < method.getParameterTypes().length; i++) {
			Class<?> clazz1 = method.getParameterTypes()[i];
			if (clazz1.isAssignableFrom(clazz1)) {
				if (index == -1)
					index = i;
				else
					return -2; // MULTIPLE MATCHES
			}
		}
		return index;
	}

}
