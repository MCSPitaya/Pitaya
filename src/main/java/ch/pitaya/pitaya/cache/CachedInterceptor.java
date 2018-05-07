package ch.pitaya.pitaya.cache;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;

@Component
public class CachedInterceptor implements MethodInterceptor {

	private com.google.common.cache.Cache<CacheEntry, Object> cache = CacheBuilder.newBuilder().maximumSize(1000).recordStats()
			.build();

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {

		CacheEntry entry = new CacheEntry(invocation);

		return cache.get(entry, () -> {
			try {
				return invocation.proceed();
			} catch (Throwable t) {
				throw new ExecutionException(t);
			}
		});

	}
	
	public CacheStats getCacheStats() {
		return cache.stats();
	}

	private static class CacheEntry {

		public Method method;
		public Object target;
		public Object[] params;

		public CacheEntry(MethodInvocation invocation) {
			method = invocation.getMethod();
			target = invocation.getThis();
			params = invocation.getArguments();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CacheEntry other = (CacheEntry) obj;
			if (method == null) {
				if (other.method != null)
					return false;
			} else if (!method.equals(other.method))
				return false;
			if (!Arrays.equals(params, other.params))
				return false;
			if (target == null) {
				if (other.target != null)
					return false;
			} else if (!target.equals(other.target))
				return false;
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((method == null) ? 0 : method.hashCode());
			result = prime * result + Arrays.hashCode(params);
			result = prime * result + ((target == null) ? 0 : target.hashCode());
			return result;
		}

	}

}
