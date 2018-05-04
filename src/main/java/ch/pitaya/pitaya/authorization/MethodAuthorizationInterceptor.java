package ch.pitaya.pitaya.authorization;

import java.lang.annotation.Annotation;
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

	private <T> T findParam(MethodInvocation invocation, Class<T> clazz, String param) {
		if (StringUtils.isEmpty(param))
			return findUnnamed(invocation, clazz);
		else
			return findNamed(invocation, clazz, param);
	}

	@SuppressWarnings("unchecked")
	private <T> T findNamed(MethodInvocation invocation, Class<T> clazz, String param) {
		Parameter[] parameters = invocation.getMethod().getParameters();
		for (int i = 0; i < parameters.length; i++) {
			Parameter parameter = parameters[i];
			if (parameter.getName().equals(param)) {
				if (parameter.getType().isAssignableFrom(clazz))
					return (T) invocation.getArguments()[i];
				else
					throw new AppException("parameter with name '" + param + "' is not of type " + clazz.getName());
			}
		}
		throw new AppException("could not find parameter '" + param + "' of type " + clazz.getName());
	}

	@SuppressWarnings("unchecked")
	private <T> T findUnnamed(MethodInvocation invocation, Class<T> clazz) {
		int index = -1;
		for (int i = 0; i < invocation.getMethod().getParameterTypes().length; i++) {
			Class<?> clazz1 = invocation.getMethod().getParameterTypes()[i];
			if (clazz1.isAssignableFrom(clazz1)) {
				if (index == -1)
					index = i;
				else
					throw new AppException("ambiguous: multiple parameters exist for type " + clazz.getName());
			}
		}
		if (index == -1)
			throw new AppException("could not find parameter of type " + clazz);
		return (T) invocation.getArguments()[index];
	}

}
