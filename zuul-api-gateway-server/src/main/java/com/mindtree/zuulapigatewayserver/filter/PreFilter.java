package com.mindtree.zuulapigatewayserver.filter;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpStatus;
import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.mindtree.zuulapigatewayserver.config.MessageResourceConfig;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class PreFilter extends ZuulFilter {
	
	private static final Logger log = LoggerFactory.getLogger(PreFilter.class);

	@Autowired
	MessageResourceConfig resourceConfig;

	private static final String TOKEN_PREFIX = "Bearer";

	private static final String AUTHORIZATION = "Authorization";

	private static final String X_ACCESS_TOKEN = "X-ACCESS-TOKEN";

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {

		RequestContext currentContext = RequestContext.getCurrentContext();
		HttpServletRequest request = currentContext.getRequest();

		// Step 1: Check for authorization header
		String header = request.getHeader(AUTHORIZATION);
		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			constructZuulException(resourceConfig.authenticationRequired);
		}

		String authToken = header.replace(TOKEN_PREFIX, "");

		// Step 2: verify authentication token , decode and return claims
		Map<String, Object> claims = verifyIdToken(authToken);

		// Step 3: Encode claims with Base64
		String json = new JSONObject(claims).toString();

		// Step 4: Encode claims
		byte[] bytesEncoded = Base64.encodeBase64(json.getBytes());

		// Step 5: Add authorization header
		currentContext.addZuulRequestHeader(X_ACCESS_TOKEN, new String(bytesEncoded));
		return null;

	}

	private void constructZuulException(String message) {
		ZuulException zuulException = new ZuulException("Authentication credentials", HttpStatus.SC_UNAUTHORIZED,
				message);	
		throw new ZuulRuntimeException(zuulException);
	}

	private Map<String, Object> verifyIdToken(String idToken) throws ZuulException {
		Map<String, Object> claims = null;
		try {
			FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken.trim());
			claims = decodedToken.getClaims();
		} catch (FirebaseAuthException ex) {
			log.error(ex.getMessage());
			constructZuulException(resourceConfig.tokenExpired);
		}
		return claims;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
