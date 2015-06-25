package com.damiancyk.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Service;

@Service
public class RealTimeVoter implements AccessDecisionVoter {

	private static Set<String> revokedUsers = new HashSet<String>();

	@PersistenceContext
	EntityManager em;

	@Override
	public boolean supports(ConfigAttribute arg0) {
		return true;
	}

	public void suspend(String user) {
		revokedUsers.add(user);
	}

	public boolean isSuspended(String user) {
		return revokedUsers.contains(user);
	}

	public void grant(String user) {
		revokedUsers.remove(user);
	}

	@Override
	public int vote(Authentication authentication, Object object,
			Collection config) {
		return ACCESS_GRANTED;
	}

	private String prepareUrl(FilterInvocation f) {
		String url = f.getRequestUrl().replace("/", "");
		if (url.contains("-")) {
			url = url.substring(0, url.indexOf("-"));
		}
		if (url.contains(".")) {
			url = url.substring(0, url.indexOf("."));
		}
		// System.out.println(url);
		return url;
	}

	@Override
	public boolean supports(Class arg0) {
		return true;
	}

}
