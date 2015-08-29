package com.damiancyk.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Operator {

	String ip;
	String name;
	String email;
	String jsession;

	Long idAccount;
	Long idCompany;

	Long idEmployee;
	//
	Long idPartners;
	Long idSupplier;
	Long idPartnerSupplier;
	//

	String accountType;

	boolean firstLogin;
	String iconPublicPath;
	String iconRealPath;

	public Operator() {
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getJsession() {
		return jsession;
	}

	public void setJsession(String jsession) {
		this.jsession = jsession;
	}

	public Long getIdAccount() {
		return idAccount;
	}

	public void setIdAccount(Long idAccount) {
		this.idAccount = idAccount;
	}

	public Long getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(Long idCompany) {
		this.idCompany = idCompany;
	}

	public Long getIdPartner() {
		return idPartners;
	}

	public void setIdPartners(Long idPartners) {
		this.idPartners = idPartners;
	}

	public Long getIdSupplier() {
		return idSupplier;
	}

	public void setIdSupplier(Long isSupplier) {
		this.idSupplier = isSupplier;
	}

	public Long getIdPartnerSupplier() {
		return idPartnerSupplier;
	}

	public void setIdPartnerSupplier(Long idPartnerSupplier) {
		this.idPartnerSupplier = idPartnerSupplier;
	}

	public Long getIdEmployee() {
		return idEmployee;
	}

	public void setIdEmployee(Long idEmployee) {
		this.idEmployee = idEmployee;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public boolean getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(boolean firstLogin) {
		this.firstLogin = firstLogin;
	}

	public String getIconPublicPath() {
		return iconPublicPath;
	}

	public void setIconPublicPath(String iconPublicPath) {
		this.iconPublicPath = iconPublicPath;
	}

	public String getIconRealPath() {
		return iconRealPath;
	}

	public void setIconRealPath(String iconRealPath) {
		this.iconRealPath = iconRealPath;
	}

}
