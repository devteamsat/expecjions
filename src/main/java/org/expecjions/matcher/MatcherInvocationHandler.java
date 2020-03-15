package org.expecjions.matcher;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MatcherInvocationHandler<V, M extends Matcher<V>> implements InvocationHandler {
	private final V value;
	private final Class<M> matcherType;
	private Matcher.Context<V> context;

	public MatcherInvocationHandler(V value, Class<M> matcherType) {
		this.value = value;
		this.matcherType = matcherType;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.getName().equals("setContext")) {
			this.context = (Matcher.Context) args[0];
		} else if(method.getName().equals("getContext")) {
			return this.context;
		} else {
			this.context.pushMethod(method.getName(), method.getParameters(), args);
			Object ret = MethodHandles.lookup()
					.findSpecial(
							matcherType,
							method.getName(),
							MethodType.methodType(method.getReturnType(), method.getParameterTypes()),
							matcherType)
					.bindTo(proxy)
					.invokeWithArguments(args);
			return ret;
		}
		return null;
	}
}
