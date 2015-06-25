package com.damiancyk.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class FunctionalityGenerator {

	public static String IMPORT_FILE_PATH = "import/mapping.txt";
	public static String SAVE_FOLDER = "d:/";

	public static void main(String[] args) throws IOException {
		HashMap<String, Long> functionalitiesWithId = new HashMap<String, Long>();
		HashSet<String> functionalities = getFunctionalities();
		HashMap<String, HashSet<String>> functionalityGroups = getFunctionalityGroups(functionalities);
		StringBuilder query = new StringBuilder();

		// funkcjonalnosci, grupy
		query.append(generateQueryClearTables());
		query.append(generateQueryGroups(functionalityGroups));
		query.append(generateQueryFunctionalities(functionalityGroups,
				functionalitiesWithId));

		// uprawnienia
		query.append(generateAdmin(functionalityGroups));
		query.append(funcPartnerAdmin(functionalityGroups,
				functionalitiesWithId));
		query.append(funcPartnerAccountant(functionalityGroups,
				functionalitiesWithId));
		query.append(funcPartnerCustomsAgency(functionalityGroups,
				functionalitiesWithId));
		query.append(funcPartnerWarehouseman(functionalityGroups,
				functionalitiesWithId));
		query.append(funcEmployee(functionalityGroups, functionalitiesWithId));

		saveImportFile(query, "import-functionalities.sql");

	}

	public static HashSet<String> getFunctionalities() throws IOException {
		HashSet<String> functionalities = new HashSet<String>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new BufferedInputStream(new FileInputStream(new File(
						IMPORT_FILE_PATH)))));

		String line = null;
		StringBuilder sb = new StringBuilder();
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}

		Pattern p = Pattern.compile("\\[\\/(.*?)(\\-\\{|\\.html\\])");
		Matcher m = p.matcher(sb.toString());

		while (m.find()) {
			String functionalityName = m.group(1);
			functionalities.add(functionalityName);
		}

		return functionalities;
	}

	public static HashMap<String, HashSet<String>> getFunctionalityGroups(
			HashSet<String> functionalities) throws IOException {
		HashMap<String, HashSet<String>> functionalityGroups = new HashMap<String, HashSet<String>>();
		if (functionalities == null) {
			return functionalityGroups;
		}

		for (String functionality : functionalities) {
			String functionalityGroup = getGroupName(functionality);
			HashSet<String> functionalitiesInGroup = functionalityGroups
					.get(functionalityGroup);
			if (functionalitiesInGroup == null) {
				functionalitiesInGroup = new HashSet<String>();
			}
			functionalitiesInGroup.add(functionality);
			functionalityGroups.put(functionalityGroup, functionalitiesInGroup);
		}

		return functionalityGroups;
	}

	private static StringBuilder generateQueryGroups(
			HashMap<String, HashSet<String>> functionalityGroups)
			throws IOException {
		StringBuilder query = new StringBuilder();
		query.append("\n--Functionality groups");

		for (Entry<String, HashSet<String>> functionalityGroup : functionalityGroups
				.entrySet()) {
			String groupName = functionalityGroup.getKey();
			query.append(generateSingleQueryGroup(groupName));
		}

		return query;
	}

	private static String generateSingleQueryGroup(String groupName) {
		StringBuilder query = new StringBuilder();

		query.append("\n");
		query.append("Insert into FUNCTIONALITY_GROUP (GROUP_NAME,DESCRIPTION,ID_PARENT,NAME) values ('");
		query.append(groupName);
		query.append("',null,null,'");
		query.append(groupName);
		query.append("');");

		return query.toString();
	}

	private static StringBuilder generateQueryClearTables() {
		StringBuilder query = new StringBuilder();

		query.append("\nDELETE FROM ACCOUNT_TYPE_FUNCTIONALITY;");
		query.append("\nDELETE FROM FUNCTIONALITY;");
		query.append("\nDELETE FROM FUNCTIONALITY_GROUP;");

		return query;
	}

	private static StringBuilder generateQueryFunctionalities(
			HashMap<String, HashSet<String>> functionalityGroups,
			HashMap<String, Long> functionalitiesWithId) throws IOException {
		StringBuilder query = new StringBuilder();
		query.append("\n--Functionalities");
		query.append("\nSET IDENTITY_INSERT FUNCTIONALITY ON;");

		int i = 1;
		for (Entry<String, HashSet<String>> functionalityGroup : functionalityGroups
				.entrySet()) {
			String groupName = functionalityGroup.getKey();
			HashSet<String> functionalities = functionalityGroup.getValue();
			if (functionalities != null) {
				for (String functionalityName : functionalities) {
					query.append(generateSingleQueryFunctionality(groupName,
							functionalityName, i));
					functionalitiesWithId.put(functionalityName, (long) i);
					i++;
				}
			}
		}

		query.append("\nSET IDENTITY_INSERT FUNCTIONALITY OFF;");

		return query;
	}

	private static String generateSingleQueryFunctionality(String groupName,
			String functionalityName, int i) {
		StringBuilder query = new StringBuilder();
		query.append("\n");
		query.append("Insert into FUNCTIONALITY (ID_FUNCTIONALITY,DESCRIPTION,NAME,STATUS,GROUP_NAME) values (");
		query.append(i);
		query.append(",null,'");
		query.append(functionalityName);
		query.append("','ACTIVE','");
		query.append(groupName);
		query.append("');");

		return query.toString();
	}

	private static void saveImportFile(StringBuilder query, String fileName)
			throws IOException {
		String filePath = SAVE_FOLDER + fileName;
		FileUtils.writeStringToFile(new File(filePath), query.toString());
		System.out.println(filePath);
	}

	private static String generateSingleQueryAccountTypeFunctionality(
			String roleName, Long idFunctionality) {
		StringBuilder query = new StringBuilder();

		query.append("\n");
		query.append("Insert into ACCOUNT_TYPE_FUNCTIONALITY (STATUS,ACCOUNT_TYPE,ID_FUNCTIONALITY) values (");
		query.append("'ACTIVE','");
		query.append(roleName);
		query.append("',");
		query.append(idFunctionality);
		query.append(");");

		return query.toString();
	}

	private static StringBuilder generateFunctionality(
			HashMap<String, HashSet<String>> functionalityGroups,
			HashMap<String, Long> functionalitiesWithId, String accountType,
			HashSet<String> f) {
		StringBuilder query = new StringBuilder();
		query.append("\n--Functionalities ");
		query.append(accountType);
		for (Entry<String, HashSet<String>> functionalityGroup : functionalityGroups
				.entrySet()) {
			HashSet<String> functionalitiesInGroup = functionalityGroup
					.getValue();
			if (functionalitiesInGroup != null) {
				for (String functionalityInGroup : functionalitiesInGroup) {
					if (f.contains(functionalityInGroup)) {
						Long idFunctionality = functionalitiesWithId
								.get(functionalityInGroup);
						query.append(generateSingleQueryAccountTypeFunctionality(
								accountType, idFunctionality));
					}
				}
			}
		}

		return query;
	}

	private static StringBuilder generateAdmin(
			HashMap<String, HashSet<String>> functionalityGroups)
			throws IOException {
		int i = 1;
		StringBuilder query = new StringBuilder();
		query.append("\n--Functionalities ADMIN_ROLE");
		// query.append("\nSET IDENTITY_INSERT ACCOUNT_TYPE_FUNCTIONALITY ON;");
		for (Entry<String, HashSet<String>> functionalityGroup : functionalityGroups
				.entrySet()) {
			String groupName = functionalityGroup.getKey();
			HashSet<String> functionalities = functionalityGroup.getValue();
			if (functionalities != null) {
				for (String functionality : functionalities) {
					query.append("\n");
					query.append("Insert into ACCOUNT_TYPE_FUNCTIONALITY (STATUS,ACCOUNT_TYPE,ID_FUNCTIONALITY) values (");
					// query.append(i);
					query.append("'ACTIVE','ADMIN_ROLE',");
					query.append(i);
					query.append(");");
					i++;
				}
			}
		}
		// query.append("\nSET IDENTITY_INSERT ACCOUNT_TYPE_FUNCTIONALITY OFF;");

		return query;
	}

	private static StringBuilder funcPartnerAdmin(
			HashMap<String, HashSet<String>> functionalityGroups,
			HashMap<String, Long> functionalitiesWithId) {
		StringBuilder query = new StringBuilder();
		HashSet<String> f = new HashSet<String>();

		// ACCOUNT_TYPE
		// f.add("accountTypeAdd");
		// f.add("accountTypeDelete");
		// f.add("accountTypeEdit");
		// f.add("accountTypeInfo");
		// f.add("accountTypeView");

		// ACCOUNTS
		f.add("accountsAdd");
		f.add("accountsDelete");
		f.add("accountsEdit");
		f.add("accountsInfo");
		f.add("accountsLoginsAjax");
		f.add("accountsView");
		f.add("accountsViewAjax");

		// ACCOUNTTYPE_FUNCTIONALITY
		// f.add("accountTypeFunctionalityAdd");
		// f.add("accountTypeFunctionalityAddByIds");
		// f.add("accountTypeFunctionalityAddByIdsAjax");
		// f.add("accountTypeFunctionalityDelete");
		// f.add("accountTypeFunctionalityEdit");
		// f.add("accountTypeFunctionalityInfo");
		// f.add("accountTypeFunctionalitySaveOrUpdateAjax");
		// f.add("accountTypeFunctionalityView");

		// ADDRESSES_EVIDENCE
		// f.add("addressesEvidenceAdd");
		// f.add("addressesEvidenceDelete");
		// f.add("addressesEvidenceEdit");
		f.add("addressesEvidenceInfo");
		f.add("addressesEvidenceView");
		f.add("addressEvidenceDefaultAjax");
		f.add("addressEvidenceListAjax");

		// AJAX_ME24
		// f.add("calculateShipmentMe24Ajax");
		f.add("cityMe24Ajax");
		f.add("countryMe24Ajax");
		f.add("districtMe24Ajax");
		// f.add("mest24RemoteTest");
		f.add("regionMe24Ajax");
		f.add("streetMe24Ajax");

		// AJAX_OTHER
		f.add("countryAjax");
		f.add("currencyViewAjax");
		f.add("vatViewAjax");
		f.add("checkBankAccount");

		// API
		f.add("apiInfo");

		// BARCODE_POOL
		// f.add("barCodePoolAdd");
		// f.add("barCodePoolDelete");
		// f.add("barCodePoolEdit");
		// f.add("barCodePoolInfo");
		// f.add("barCodePoolView");
		// f.add("barCodeTest");

		// BASIC
		f.add("400");
		f.add("404");
		f.add("405");
		f.add("500");
		f.add("accessDenied");
		f.add("activationAccount");
		f.add("checkEmailAvailabilityAjax");
		// f.add("devExpressView");
		f.add("index");
		f.add("login");
		f.add("logout");
		f.add("remindPassword");

		// CASH_ON
		// f.add("cashOnDeliveryImport");
		// f.add("cashOnDeliveryImportCsv");
		f.add("cashOnDeliveryView");
		f.add("codParcelAdd");
		f.add("cashOnDeliveryViewAjax");

		// CHART
		// f.add("chartView");
		// f.add("invoiceValuesByPartnerAjax");
		// f.add("itemsByDateAjax");
		// f.add("itemsByPartnerAjax");
		// f.add("parcelCountByDateAjax");
		// f.add("parcelPositionChartAjax");
		// f.add("parcelStatusAjax");
		// f.add("returnsByPartnerAjax");

		// COMPANY
		// f.add("companyAdd");
		// f.add("companyDel");
		// f.add("companyEdit");
		// f.add("companyInfo");
		// f.add("companyView");
		// f.add("companyViewAjax");

		// CONTRACT
		// f.add("contractAdd");
		// f.add("contractEdit");
		// f.add("contractInfo");
		// f.add("contractView");

		// CUSTOMS_CODES
		// f.add("customsCodesAdd");
		f.add("customsCodesAjax");
		// f.add("customsCodesDelete");
		// f.add("customsCodesEdit");
		f.add("customsCodesInfo");
		f.add("customsCodesView");

		// DISPATCH_REGISTER
		f.add("dispatchRegisterAdd");
		f.add("dispatchRegisterCreate");
		f.add("dispatchRegisterEdit");
		f.add("dispatchRegisterInfo");
		f.add("dispatchRegisterView");
		f.add("generateDispatchRegisterPdf");

		// DIVISIONS
		f.add("divisionMe24Ajax");

		// DOCUMENT
		f.add("documentGeneratePdf");
		f.add("documentInfo");
		f.add("documentPrintHistoryView");
		f.add("documentView");

		// DOCUMENT_TEMPLATE
		// f.add("documentTemplateAdd");
		// f.add("documentTemplateCopy");
		// f.add("documentTemplateEdit");
		// f.add("documentTemplateFieldAjaxView");
		// f.add("documentTemplateFieldView");
		// f.add("documentTemplateInfo");
		// f.add("documentTemplateView");

		// EMPLOYEE
		// f.add("employeeAdd");
		// f.add("employeeEdit");
		// f.add("employeeInfo");
		// f.add("employeesView");

		// FILE
		f.add("addMultipleFile");
		f.add("deleteAttach");
		f.add("deleteAttachFineUploader");
		f.add("fileNotFound");
		f.add("removeFile");
		f.add("uploadFile");

		// IMPORT/EXPORT
		// f.add("importFilesView");
		// f.add("getImportXlsFilesStatusAjax");
		// f.add("parcelImport");
		// f.add("parcelExportXls");

		// // FUNCTIONALITY
		// f.add("functionalityAdd");
		// f.add("functionalityDelete");
		// f.add("functionalityEdit");
		// f.add("functionalityInfo");
		// f.add("functionalityUpdate");
		// f.add("functionalityView");
		// f.add("functionalityViewAjax");
		//
		// // FUNCTIONALITY_GROUP
		// f.add("functionalityGroupAdd");
		// f.add("functionalityGroupDelete");
		// f.add("functionalityGroupEdit");
		// f.add("functionalityGroupView");

		// INVOICE
		// f.add("envelopeGeneratePdfByIdInvoices");
		// f.add("invoiceAdd");
		f.add("invoiceAddByParcels");
		// f.add("invoiceCopy");
		// f.add("invoiceCorrect");
		// f.add("invoiceDel");
		// f.add("invoiceEdit");
		f.add("invoiceGenerateEnvelopePdf");
		f.add("invoiceGeneratePdf");
		f.add("invoiceInfo");
		// f.add("invoiceParcelAdd");
		// f.add("invoicePayment");
		// f.add("invoiceRemind");
		f.add("invoiceSend");
		f.add("invoiceView");
		f.add("invoiceViewAjax");
		// f.add("invoiceViewTableAjax");

		// MESSAGES
		f.add("countMessagesReceivedAjax");
		f.add("countMessagesTemplateAjax");
		f.add("countMessagesTrashAjax");
		f.add("editTemplate");
		f.add("editTemplateAjax");
		f.add("getMessageHeaderBean");
		f.add("markMessageAsReadAjax");
		f.add("markMultipleMessagesAsReadAjax");
		f.add("messageAttachmentsView");
		f.add("messagesContinue");
		f.add("messagesDel");
		f.add("messagesDelAll");
		f.add("messagesFind");
		f.add("messagesForward");
		f.add("messagesInfo");
		f.add("messagesNew");
		f.add("messagesReply");
		f.add("messagesRet");
		f.add("messagesRmv");
		f.add("messagesView");
		f.add("saveTemplate");
		f.add("saveTemplateAjax");

		// PARCEL
		f.add("parcelAdd");
		f.add("parcelDelete");
		f.add("parcelAddStep2");
		f.add("parcelDocumentGeneratePdf");
		f.add("parcelInfo");
		f.add("parcelListAjax");
		// f.add("parcelRefresh");
		// f.add("parcelUpdateInformation");
		f.add("parcelView");
		f.add("parcelViewTableAjax");
		f.add("parcelDelete");
		f.add("parcelViewAngularAjax");

		// PARCEL_TRACKING
		// f.add("parcelTrackingHistoryRosanRefresh");
		// f.add("parcelTrackingHistoryRosanView");
		// f.add("parcelTrackingHistoryRosanAjax");

		// PARTNER
		f.add("loggedPartnerMainInformation");
		// f.add("partnerAdd");
		// f.add("partnerAjaxInfo");
		// f.add("partnerDebtAjax");
		// f.add("partnerDelete");
		// f.add("partnerEdit");
		// f.add("partnerFindByIdsAjax");
		// f.add("partnerGetNameAjax");
		f.add("partnerInfo");
		// f.add("partnerListWihoutGroupAjax");
		// f.add("partnerSendInvitation");
		// f.add("partnerView");
		// f.add("partnerViewAjax");

		// PRICING
		// f.add("pricingAdd");
		// f.add("pricingDelete");
		// f.add("pricingEdit");
		// f.add("pricingInfo");
		// f.add("pricingView");

		// RETURNS
		// f.add("returnsAgentImport");
		// f.add("returnsAgentRefresh");
		f.add("returnsInfo");
		f.add("returnsView");
		f.add("returnsViewAjax");

		// RMA
		// f.add("addRmasToAccount");
		// f.add("addRmasToAccountByIds");
		f.add("changeStatusRmaByIds");
		f.add("rmaAccept");
		f.add("rmaAdd");
		f.add("rmaDelete");
		f.add("rmaEdit");
		f.add("rmaInfo");
		f.add("rmaReject");
		f.add("rmaStatusTypeSet");
		f.add("rmaTypeDelete");
		f.add("rmaView");
		f.add("rmaAttachmentAdd");
		f.add("rmaAttachmentDelete");
		f.add("rmaAttachmentEdit");
		f.add("rmaAttachmentInfo");
		f.add("rmaAttachmentView");

		// SETTINGS
		// f.add("settingsEdit");
		// f.add("settingsInfo");
		// f.add("settingsView");

		// SHIPMENT
		f.add("shipmentAdd");
		f.add("shipmentFormatMe24Ajax");
		f.add("shipmentOptionConditionMe24Ajax");
		f.add("shipmentView");

		query.append(generateFunctionality(functionalityGroups,
				functionalitiesWithId, "PARTNER_ADMIN_ROLE", f));

		return query;
	}

	/*
	 * ksiegowy
	 * 
	 * + to co admin
	 * 
	 * - bez kont
	 * 
	 * + przychody,wydatki
	 */
	private static StringBuilder funcPartnerAccountant(
			HashMap<String, HashSet<String>> functionalityGroups,
			HashMap<String, Long> functionalitiesWithId) {
		StringBuilder query = new StringBuilder();
		HashSet<String> f = new HashSet<String>();

		// ACCOUNT_TYPE
		// f.add("accountTypeAdd");
		// f.add("accountTypeDelete");
		// f.add("accountTypeEdit");
		// f.add("accountTypeInfo");
		// f.add("accountTypeView");

		// ACCOUNTS
		// f.add("accountsAdd");
		// f.add("accountsDelete");
		f.add("accountsEdit");
		// f.add("accountsInfo");
		// f.add("accountsLoginsAjax");
		// f.add("accountsView");
		// f.add("accountsViewAjax");

		// ACCOUNTTYPE_FUNCTIONALITY
		// f.add("accountTypeFunctionalityAdd");
		// f.add("accountTypeFunctionalityAddByIds");
		// f.add("accountTypeFunctionalityAddByIdsAjax");
		// f.add("accountTypeFunctionalityDelete");
		// f.add("accountTypeFunctionalityEdit");
		// f.add("accountTypeFunctionalityInfo");
		// f.add("accountTypeFunctionalitySaveOrUpdateAjax");
		// f.add("accountTypeFunctionalityView");

		// ADDRESSES_EVIDENCE
		// f.add("addressesEvidenceAdd");
		// f.add("addressesEvidenceDelete");
		// f.add("addressesEvidenceEdit");
		f.add("addressesEvidenceInfo");
		f.add("addressesEvidenceView");
		f.add("addressEvidenceDefaultAjax");
		f.add("addressEvidenceListAjax");

		// AJAX_ME24
		// f.add("calculateShipmentMe24Ajax");
		f.add("cityMe24Ajax");
		f.add("countryMe24Ajax");
		f.add("districtMe24Ajax");
		// f.add("mest24RemoteTest");
		f.add("regionMe24Ajax");
		f.add("streetMe24Ajax");

		// AJAX_OTHER
		f.add("countryAjax");
		f.add("currencyViewAjax");
		f.add("vatViewAjax");
		f.add("checkBankAccount");

		// API
		f.add("apiInfo");

		// BARCODE_POOL
		// f.add("barCodePoolAdd");
		// f.add("barCodePoolDelete");
		// f.add("barCodePoolEdit");
		// f.add("barCodePoolInfo");
		// f.add("barCodePoolView");
		// f.add("barCodeTest");

		// BASIC
		f.add("400");
		f.add("404");
		f.add("405");
		f.add("500");
		f.add("accessDenied");
		f.add("activationAccount");
		f.add("checkEmailAvailabilityAjax");
		// f.add("devExpressView");
		f.add("index");
		f.add("login");
		f.add("logout");
		f.add("remindPassword");

		// CASH_ON
		// f.add("cashOnDeliveryImport");
		// f.add("cashOnDeliveryImportCsv");
		f.add("cashOnDeliveryView");
		f.add("codParcelAdd");
		f.add("cashOnDeliveryViewAjax");

		// CHART
		// f.add("chartView");
		// f.add("invoiceValuesByPartnerAjax");
		// f.add("itemsByDateAjax");
		// f.add("itemsByPartnerAjax");
		// f.add("parcelCountByDateAjax");
		// f.add("parcelPositionChartAjax");
		// f.add("parcelStatusAjax");
		// f.add("returnsByPartnerAjax");

		// COMPANY
		// f.add("companyAdd");
		// f.add("companyDel");
		// f.add("companyEdit");
		// f.add("companyInfo");
		// f.add("companyView");
		// f.add("companyViewAjax");

		// CONTRACT
		// f.add("contractAdd");
		// f.add("contractEdit");
		// f.add("contractInfo");
		// f.add("contractView");

		// CUSTOMS_CODES
		// f.add("customsCodesAdd");
		f.add("customsCodesAjax");
		// f.add("customsCodesDelete");
		// f.add("customsCodesEdit");
		f.add("customsCodesInfo");
		f.add("customsCodesView");

		// DISPATCH_REGISTER
		f.add("dispatchRegisterAdd");
		f.add("dispatchRegisterCreate");
		f.add("dispatchRegisterEdit");
		f.add("dispatchRegisterInfo");
		f.add("dispatchRegisterView");
		f.add("generateDispatchRegisterPdf");

		// DOCUMENT
		f.add("documentGeneratePdf");
		f.add("documentInfo");
		f.add("documentPrintHistoryView");
		f.add("documentView");

		// DOCUMENT_TEMPLATE
		// f.add("documentTemplateAdd");
		// f.add("documentTemplateCopy");
		// f.add("documentTemplateEdit");
		// f.add("documentTemplateFieldAjaxView");
		// f.add("documentTemplateFieldView");
		// f.add("documentTemplateInfo");
		// f.add("documentTemplateView");

		// EMPLOYEE
		// f.add("employeeAdd");
		// f.add("employeeEdit");
		// f.add("employeeInfo");
		// f.add("employeesView");

		// FILE
		f.add("addMultipleFile");
		f.add("deleteAttach");
		f.add("deleteAttachFineUploader");
		f.add("fileNotFound");
		f.add("removeFile");
		f.add("uploadFile");

		// DIVISIONS
		f.add("divisionMe24Ajax");

		// IMPORT/EXPORT
		// f.add("importFilesView");
		// f.add("getImportXlsFilesStatusAjax");
		// f.add("parcelImport");
		// f.add("parcelExportXls");

		// // FUNCTIONALITY
		// f.add("functionalityAdd");
		// f.add("functionalityDelete");
		// f.add("functionalityEdit");
		// f.add("functionalityInfo");
		// f.add("functionalityUpdate");
		// f.add("functionalityView");
		// f.add("functionalityViewAjax");
		//
		// // FUNCTIONALITY_GROUP
		// f.add("functionalityGroupAdd");
		// f.add("functionalityGroupDelete");
		// f.add("functionalityGroupEdit");
		// f.add("functionalityGroupView");

		// INVOICE
		// f.add("envelopeGeneratePdfByIdInvoices");
		// f.add("invoiceAdd");
		f.add("invoiceAddByParcels");
		// f.add("invoiceCopy");
		// f.add("invoiceCorrect");
		// f.add("invoiceDel");
		// f.add("invoiceEdit");
		f.add("invoiceGenerateEnvelopePdf");
		f.add("invoiceGeneratePdf");
		f.add("invoiceInfo");
		// f.add("invoiceParcelAdd");
		// f.add("invoicePayment");
		// f.add("invoiceRemind");
		f.add("invoiceSend");
		f.add("invoiceView");
		f.add("invoiceViewAjax");
		// f.add("invoiceViewTableAjax");

		// MESSAGES
		// f.add("countMessagesReceivedAjax");
		// f.add("countMessagesTemplateAjax");
		// f.add("countMessagesTrashAjax");
		// f.add("editTemplate");
		// f.add("editTemplateAjax");
		// f.add("getMessageHeaderBean");
		// f.add("markMessageAsReadAjax");
		// f.add("markMultipleMessagesAsReadAjax");
		// f.add("messageAttachmentsView");
		// f.add("messagesContinue");
		// f.add("messagesDel");
		// f.add("messagesDelAll");
		// f.add("messagesFind");
		// f.add("messagesForward");
		// f.add("messagesInfo");
		// f.add("messagesNew");
		// f.add("messagesReply");
		// f.add("messagesRet");
		// f.add("messagesRmv");
		// f.add("messagesView");
		// f.add("saveTemplate");
		// f.add("saveTemplateAjax");

		// PARCEL
		f.add("parcelAdd");
		f.add("parcelDelete");
		f.add("parcelAddStep2");
		f.add("parcelDocumentGeneratePdf");
		f.add("parcelInfo");
		f.add("parcelListAjax");
		// f.add("parcelRefresh");
		// f.add("parcelUpdateInformation");
		f.add("parcelView");
		f.add("parcelViewTableAjax");
		f.add("parcelViewAngularAjax");

		// PARCEL_TRACKING
		// f.add("parcelTrackingHistoryRosanRefresh");
		// f.add("parcelTrackingHistoryRosanView");
		// f.add("parcelTrackingHistoryRosanAjax");

		// PARTNER
		f.add("loggedPartnerMainInformation");
		// f.add("partnerAdd");
		// f.add("partnerAjaxInfo");
		// f.add("partnerDebtAjax");
		// f.add("partnerDelete");
		// f.add("partnerEdit");
		// f.add("partnerFindByIdsAjax");
		// f.add("partnerGetNameAjax");
		f.add("partnerInfo");
		// f.add("partnerListWihoutGroupAjax");
		// f.add("partnerSendInvitation");
		// f.add("partnerView");
		// f.add("partnerViewAjax");

		// PRICING
		// f.add("pricingAdd");
		// f.add("pricingDelete");
		// f.add("pricingEdit");
		// f.add("pricingInfo");
		// f.add("pricingView");

		// RETURNS
		// f.add("returnsAgentImport");
		// f.add("returnsAgentRefresh");
		f.add("returnsInfo");
		f.add("returnsView");
		f.add("returnsViewAjax");

		// RMA
		// f.add("addRmasToAccount");
		// f.add("addRmasToAccountByIds");
		f.add("changeStatusRmaByIds");
		f.add("rmaAccept");
		f.add("rmaAdd");
		f.add("rmaDelete");
		f.add("rmaEdit");
		f.add("rmaInfo");
		f.add("rmaReject");
		f.add("rmaStatusTypeSet");
		f.add("rmaTypeDelete");
		f.add("rmaView");
		f.add("rmaAttachmentAdd");
		f.add("rmaAttachmentDelete");
		f.add("rmaAttachmentEdit");
		f.add("rmaAttachmentInfo");
		f.add("rmaAttachmentView");

		// SETTINGS
		// f.add("settingsEdit");
		// f.add("settingsInfo");
		// f.add("settingsView");

		// SHIPMENT
		f.add("shipmentAdd");
		f.add("shipmentFormatMe24Ajax");
		f.add("shipmentOptionConditionMe24Ajax");
		f.add("shipmentView");

		query.append(generateFunctionality(functionalityGroups,
				functionalitiesWithId, "PARTNER_ACCOUNTANT_ROLE", f));

		return query;
	}

	/*
	 * agencja celna
	 * 
	 * + to co ksiegowy
	 * 
	 * - bez faktur
	 * 
	 * + generowanie proformy do dispatchRegister
	 */
	private static StringBuilder funcPartnerCustomsAgency(
			HashMap<String, HashSet<String>> functionalityGroups,
			HashMap<String, Long> functionalitiesWithId) {
		StringBuilder query = new StringBuilder();
		HashSet<String> f = new HashSet<String>();

		// ACCOUNT_TYPE
		// f.add("accountTypeAdd");
		// f.add("accountTypeDelete");
		// f.add("accountTypeEdit");
		// f.add("accountTypeInfo");
		// f.add("accountTypeView");

		// ACCOUNTS
		// f.add("accountsAdd");
		// f.add("accountsDelete");
		f.add("accountsEdit");
		// f.add("accountsInfo");
		// f.add("accountsLoginsAjax");
		// f.add("accountsView");
		// f.add("accountsViewAjax");

		// ACCOUNTTYPE_FUNCTIONALITY
		// f.add("accountTypeFunctionalityAdd");
		// f.add("accountTypeFunctionalityAddByIds");
		// f.add("accountTypeFunctionalityAddByIdsAjax");
		// f.add("accountTypeFunctionalityDelete");
		// f.add("accountTypeFunctionalityEdit");
		// f.add("accountTypeFunctionalityInfo");
		// f.add("accountTypeFunctionalitySaveOrUpdateAjax");
		// f.add("accountTypeFunctionalityView");

		// ADDRESSES_EVIDENCE
		// f.add("addressesEvidenceAdd");
		// f.add("addressesEvidenceDelete");
		// f.add("addressesEvidenceEdit");
		f.add("addressesEvidenceInfo");
		f.add("addressesEvidenceView");
		f.add("addressEvidenceDefaultAjax");
		f.add("addressEvidenceListAjax");

		// AJAX_ME24
		// f.add("calculateShipmentMe24Ajax");
		f.add("cityMe24Ajax");
		f.add("countryMe24Ajax");
		f.add("districtMe24Ajax");
		// f.add("mest24RemoteTest");
		f.add("regionMe24Ajax");
		f.add("streetMe24Ajax");

		// AJAX_OTHER
		f.add("countryAjax");
		f.add("currencyViewAjax");
		f.add("vatViewAjax");
		f.add("checkBankAccount");

		// API
		f.add("apiInfo");

		// BARCODE_POOL
		// f.add("barCodePoolAdd");
		// f.add("barCodePoolDelete");
		// f.add("barCodePoolEdit");
		// f.add("barCodePoolInfo");
		// f.add("barCodePoolView");
		// f.add("barCodeTest");

		// BASIC
		f.add("400");
		f.add("404");
		f.add("405");
		f.add("500");
		f.add("accessDenied");
		f.add("activationAccount");
		f.add("checkEmailAvailabilityAjax");
		// f.add("devExpressView");
		f.add("index");
		f.add("login");
		f.add("logout");
		f.add("remindPassword");

		// CASH_ON
		// f.add("cashOnDeliveryImport");
		// f.add("cashOnDeliveryImportCsv");
		f.add("cashOnDeliveryView");
		f.add("codParcelAdd");
		f.add("cashOnDeliveryViewAjax");

		// CHART
		// f.add("chartView");
		// f.add("invoiceValuesByPartnerAjax");
		// f.add("itemsByDateAjax");
		// f.add("itemsByPartnerAjax");
		// f.add("parcelCountByDateAjax");
		// f.add("parcelPositionChartAjax");
		// f.add("parcelStatusAjax");
		// f.add("returnsByPartnerAjax");

		// COMPANY
		// f.add("companyAdd");
		// f.add("companyDel");
		// f.add("companyEdit");
		// f.add("companyInfo");
		// f.add("companyView");
		// f.add("companyViewAjax");

		// CONTRACT
		// f.add("contractAdd");
		// f.add("contractEdit");
		// f.add("contractInfo");
		// f.add("contractView");

		// CUSTOMS_CODES
		// f.add("customsCodesAdd");
		f.add("customsCodesAjax");
		// f.add("customsCodesDelete");
		// f.add("customsCodesEdit");
		f.add("customsCodesInfo");
		f.add("customsCodesView");

		// DISPATCH_REGISTER
		f.add("dispatchRegisterAdd");
		f.add("dispatchRegisterCreate");
		f.add("dispatchRegisterEdit");
		f.add("dispatchRegisterInfo");
		f.add("dispatchRegisterView");
		f.add("generateDispatchRegisterPdf");

		// DOCUMENT
		f.add("documentGeneratePdf");
		f.add("documentInfo");
		f.add("documentPrintHistoryView");
		f.add("documentView");

		// DOCUMENT_TEMPLATE
		// f.add("documentTemplateAdd");
		// f.add("documentTemplateCopy");
		// f.add("documentTemplateEdit");
		// f.add("documentTemplateFieldAjaxView");
		// f.add("documentTemplateFieldView");
		// f.add("documentTemplateInfo");
		// f.add("documentTemplateView");

		// EMPLOYEE
		// f.add("employeeAdd");
		// f.add("employeeEdit");
		// f.add("employeeInfo");
		// f.add("employeesView");

		// FILE
		f.add("addMultipleFile");
		f.add("deleteAttach");
		f.add("deleteAttachFineUploader");
		f.add("fileNotFound");
		f.add("removeFile");
		f.add("uploadFile");

		// IMPORT/EXPORT
		// f.add("importFilesView");
		// f.add("getImportXlsFilesStatusAjax");
		// f.add("parcelImport");
		// f.add("parcelExportXls");

		// // FUNCTIONALITY
		// f.add("functionalityAdd");
		// f.add("functionalityDelete");
		// f.add("functionalityEdit");
		// f.add("functionalityInfo");
		// f.add("functionalityUpdate");
		// f.add("functionalityView");
		// f.add("functionalityViewAjax");
		//
		// // FUNCTIONALITY_GROUP
		// f.add("functionalityGroupAdd");
		// f.add("functionalityGroupDelete");
		// f.add("functionalityGroupEdit");
		// f.add("functionalityGroupView");

		// INVOICE
		// f.add("envelopeGeneratePdfByIdInvoices");
		// f.add("invoiceAdd");
		f.add("invoiceAddByParcels");
		// f.add("invoiceCopy");
		// f.add("invoiceCorrect");
		// f.add("invoiceDel");
		// f.add("invoiceEdit");
		f.add("invoiceGenerateEnvelopePdf");
		f.add("invoiceGeneratePdf");
		f.add("invoiceInfo");
		// f.add("invoiceParcelAdd");
		// f.add("invoicePayment");
		// f.add("invoiceRemind");
		f.add("invoiceSend");
		f.add("invoiceView");
		f.add("invoiceViewAjax");
		// f.add("invoiceViewTableAjax");

		// MESSAGES
		// f.add("countMessagesReceivedAjax");
		// f.add("countMessagesTemplateAjax");
		// f.add("countMessagesTrashAjax");
		// f.add("editTemplate");
		// f.add("editTemplateAjax");
		// f.add("getMessageHeaderBean");
		// f.add("markMessageAsReadAjax");
		// f.add("markMultipleMessagesAsReadAjax");
		// f.add("messageAttachmentsView");
		// f.add("messagesContinue");
		// f.add("messagesDel");
		// f.add("messagesDelAll");
		// f.add("messagesFind");
		// f.add("messagesForward");
		// f.add("messagesInfo");
		// f.add("messagesNew");
		// f.add("messagesReply");
		// f.add("messagesRet");
		// f.add("messagesRmv");
		// f.add("messagesView");
		// f.add("saveTemplate");
		// f.add("saveTemplateAjax");

		// PARCEL
		f.add("parcelAdd");
		f.add("parcelDelete");
		f.add("parcelAddStep2");
		f.add("parcelDocumentGeneratePdf");
		f.add("parcelInfo");
		f.add("parcelListAjax");
		// f.add("parcelRefresh");
		// f.add("parcelUpdateInformation");
		f.add("parcelView");
		f.add("parcelViewTableAjax");
		f.add("parcelViewAngularAjax");

		// PARCEL_TRACKING
		// f.add("parcelTrackingHistoryRosanRefresh");
		// f.add("parcelTrackingHistoryRosanView");
		// f.add("parcelTrackingHistoryRosanAjax");

		// PARTNER
		f.add("loggedPartnerMainInformation");
		// f.add("partnerAdd");
		// f.add("partnerAjaxInfo");
		// f.add("partnerDebtAjax");
		// f.add("partnerDelete");
		// f.add("partnerEdit");
		// f.add("partnerFindByIdsAjax");
		// f.add("partnerGetNameAjax");
		f.add("partnerInfo");
		// f.add("partnerListWihoutGroupAjax");
		// f.add("partnerSendInvitation");
		// f.add("partnerView");
		// f.add("partnerViewAjax");

		// PRICING
		// f.add("pricingAdd");
		// f.add("pricingDelete");
		// f.add("pricingEdit");
		// f.add("pricingInfo");
		// f.add("pricingView");

		// RETURNS
		// f.add("returnsAgentImport");
		// f.add("returnsAgentRefresh");
		f.add("returnsInfo");
		f.add("returnsView");
		f.add("returnsViewAjax");

		// RMA
		// f.add("addRmasToAccount");
		// f.add("addRmasToAccountByIds");
		f.add("changeStatusRmaByIds");
		f.add("rmaAccept");
		f.add("rmaAdd");
		f.add("rmaDelete");
		f.add("rmaEdit");
		f.add("rmaInfo");
		f.add("rmaReject");
		f.add("rmaStatusTypeSet");
		f.add("rmaTypeDelete");
		f.add("rmaView");
		f.add("rmaAttachmentAdd");
		f.add("rmaAttachmentDelete");
		f.add("rmaAttachmentEdit");
		f.add("rmaAttachmentInfo");
		f.add("rmaAttachmentView");

		// SETTINGS
		// f.add("settingsEdit");
		// f.add("settingsInfo");
		// f.add("settingsView");

		// SHIPMENT
		f.add("shipmentAdd");
		f.add("shipmentFormatMe24Ajax");
		f.add("shipmentOptionConditionMe24Ajax");
		f.add("shipmentView");

		query.append(generateFunctionality(functionalityGroups,
				functionalitiesWithId, "PARTNER_CUSTOMS_AGENCY_ROLE", f));

		return query;
	}

	/*
	 * magazyn
	 * 
	 * - bez kont, ksiegowosci, COD
	 * 
	 * + paczki, rejestry, zwroty, jego info
	 */
	private static StringBuilder funcPartnerWarehouseman(
			HashMap<String, HashSet<String>> functionalityGroups,
			HashMap<String, Long> functionalitiesWithId) {
		StringBuilder query = new StringBuilder();
		HashSet<String> f = new HashSet<String>();

		// ACCOUNT_TYPE
		// f.add("accountTypeAdd");
		// f.add("accountTypeDelete");
		f.add("accountTypeEdit");
		// f.add("accountTypeInfo");
		// f.add("accountTypeView");

		// ACCOUNTS
		// f.add("accountsAdd");
		// f.add("accountsDelete");
		// f.add("accountsEdit");
		// f.add("accountsInfo");
		// f.add("accountsLoginsAjax");
		// f.add("accountsView");
		// f.add("accountsViewAjax");

		// ACCOUNTTYPE_FUNCTIONALITY
		// f.add("accountTypeFunctionalityAdd");
		// f.add("accountTypeFunctionalityAddByIds");
		// f.add("accountTypeFunctionalityAddByIdsAjax");
		// f.add("accountTypeFunctionalityDelete");
		// f.add("accountTypeFunctionalityEdit");
		// f.add("accountTypeFunctionalityInfo");
		// f.add("accountTypeFunctionalitySaveOrUpdateAjax");
		// f.add("accountTypeFunctionalityView");

		// ADDRESSES_EVIDENCE
		// f.add("addressesEvidenceAdd");
		// f.add("addressesEvidenceDelete");
		// f.add("addressesEvidenceEdit");
		f.add("addressesEvidenceInfo");
		f.add("addressesEvidenceView");
		f.add("addressEvidenceDefaultAjax");
		f.add("addressEvidenceListAjax");

		// AJAX_ME24
		// f.add("calculateShipmentMe24Ajax");
		f.add("cityMe24Ajax");
		f.add("countryMe24Ajax");
		f.add("districtMe24Ajax");
		// f.add("mest24RemoteTest");
		f.add("regionMe24Ajax");
		f.add("streetMe24Ajax");

		// AJAX_OTHER
		f.add("countryAjax");
		f.add("currencyViewAjax");
		f.add("vatViewAjax");
		f.add("checkBankAccount");

		// API
		f.add("apiInfo");

		// BARCODE_POOL
		// f.add("barCodePoolAdd");
		// f.add("barCodePoolDelete");
		// f.add("barCodePoolEdit");
		// f.add("barCodePoolInfo");
		// f.add("barCodePoolView");
		// f.add("barCodeTest");

		// BASIC
		f.add("400");
		f.add("404");
		f.add("405");
		f.add("500");
		f.add("accessDenied");
		f.add("activationAccount");
		f.add("checkEmailAvailabilityAjax");
		// f.add("devExpressView");
		f.add("index");
		f.add("login");
		f.add("logout");
		f.add("remindPassword");

		// CASH_ON
		// f.add("cashOnDeliveryImport");
		// f.add("cashOnDeliveryImportCsv");
		// f.add("cashOnDeliveryView");
		// f.add("codParcelAdd");
		// f.add("cashOnDeliveryViewAjax");

		// CHART
		// f.add("chartView");
		// f.add("invoiceValuesByPartnerAjax");
		// f.add("itemsByDateAjax");
		// f.add("itemsByPartnerAjax");
		// f.add("parcelCountByDateAjax");
		// f.add("parcelPositionChartAjax");
		// f.add("parcelStatusAjax");
		// f.add("returnsByPartnerAjax");

		// COMPANY
		// f.add("companyAdd");
		// f.add("companyDel");
		// f.add("companyEdit");
		// f.add("companyInfo");
		// f.add("companyView");
		// f.add("companyViewAjax");

		// CONTRACT
		// f.add("contractAdd");
		// f.add("contractEdit");
		// f.add("contractInfo");
		// f.add("contractView");

		// CUSTOMS_CODES
		// f.add("customsCodesAdd");
		f.add("customsCodesAjax");
		// f.add("customsCodesDelete");
		// f.add("customsCodesEdit");
		f.add("customsCodesInfo");
		f.add("customsCodesView");

		// DISPATCH_REGISTER
		f.add("dispatchRegisterAdd");
		f.add("dispatchRegisterCreate");
		f.add("dispatchRegisterEdit");
		f.add("dispatchRegisterInfo");
		f.add("dispatchRegisterView");
		f.add("generateDispatchRegisterPdf");

		// DOCUMENT
		f.add("documentGeneratePdf");
		f.add("documentInfo");
		f.add("documentPrintHistoryView");
		f.add("documentView");

		// DOCUMENT_TEMPLATE
		// f.add("documentTemplateAdd");
		// f.add("documentTemplateCopy");
		// f.add("documentTemplateEdit");
		// f.add("documentTemplateFieldAjaxView");
		// f.add("documentTemplateFieldView");
		// f.add("documentTemplateInfo");
		// f.add("documentTemplateView");

		// EMPLOYEE
		// f.add("employeeAdd");
		// f.add("employeeEdit");
		// f.add("employeeInfo");
		// f.add("employeesView");

		// FILE
		f.add("addMultipleFile");
		f.add("deleteAttach");
		f.add("deleteAttachFineUploader");
		f.add("fileNotFound");
		f.add("removeFile");
		f.add("uploadFile");

		// IMPORT/EXPORT
		// f.add("importFilesView");
		// f.add("getImportXlsFilesStatusAjax");
		// f.add("parcelImport");
		// f.add("parcelExportXls");

		// // FUNCTIONALITY
		// f.add("functionalityAdd");
		// f.add("functionalityDelete");
		// f.add("functionalityEdit");
		// f.add("functionalityInfo");
		// f.add("functionalityUpdate");
		// f.add("functionalityView");
		// f.add("functionalityViewAjax");
		//
		// // FUNCTIONALITY_GROUP
		// f.add("functionalityGroupAdd");
		// f.add("functionalityGroupDelete");
		// f.add("functionalityGroupEdit");
		// f.add("functionalityGroupView");

		// INVOICE
		// f.add("envelopeGeneratePdfByIdInvoices");
		// f.add("invoiceAdd");
		// f.add("invoiceAddByParcels");
		// f.add("invoiceCopy");
		// f.add("invoiceCorrect");
		// f.add("invoiceDel");
		// f.add("invoiceEdit");
		// f.add("invoiceGenerateEnvelopePdf");
		// f.add("invoiceGeneratePdf");
		// f.add("invoiceInfo");
		// f.add("invoiceParcelAdd");
		// f.add("invoicePayment");
		// f.add("invoiceRemind");
		// f.add("invoiceSend");
		// f.add("invoiceView");
		// f.add("invoiceViewAjax");
		// f.add("invoiceViewTableAjax");

		// MESSAGES
		// f.add("countMessagesReceivedAjax");
		// f.add("countMessagesTemplateAjax");
		// f.add("countMessagesTrashAjax");
		// f.add("editTemplate");
		// f.add("editTemplateAjax");
		// f.add("getMessageHeaderBean");
		// f.add("markMessageAsReadAjax");
		// f.add("markMultipleMessagesAsReadAjax");
		// f.add("messageAttachmentsView");
		// f.add("messagesContinue");
		// f.add("messagesDel");
		// f.add("messagesDelAll");
		// f.add("messagesFind");
		// f.add("messagesForward");
		// f.add("messagesInfo");
		// f.add("messagesNew");
		// f.add("messagesReply");
		// f.add("messagesRet");
		// f.add("messagesRmv");
		// f.add("messagesView");
		// f.add("saveTemplate");
		// f.add("saveTemplateAjax");

		// PARCEL
		f.add("parcelAdd");
		f.add("parcelAddStep2");
		f.add("parcelDocumentGeneratePdf");
		f.add("parcelInfo");
		f.add("parcelListAjax");
		// f.add("parcelRefresh");
		// f.add("parcelUpdateInformation");
		f.add("parcelView");
		f.add("parcelViewTableAjax");
		f.add("parcelViewAngularAjax");

		// PARCEL_TRACKING
		// f.add("parcelTrackingHistoryRosanRefresh");
		// f.add("parcelTrackingHistoryRosanView");
		// f.add("parcelTrackingHistoryRosanAjax");

		// PARTNER
		f.add("loggedPartnerMainInformation");
		// f.add("partnerAdd");
		// f.add("partnerAjaxInfo");
		// f.add("partnerDebtAjax");
		// f.add("partnerDelete");
		// f.add("partnerEdit");
		// f.add("partnerFindByIdsAjax");
		// f.add("partnerGetNameAjax");
		f.add("partnerInfo");
		// f.add("partnerListWihoutGroupAjax");
		// f.add("partnerSendInvitation");
		// f.add("partnerView");
		// f.add("partnerViewAjax");

		// PRICING
		// f.add("pricingAdd");
		// f.add("pricingDelete");
		// f.add("pricingEdit");
		// f.add("pricingInfo");
		// f.add("pricingView");

		// RETURNS
		// f.add("returnsAgentImport");
		// f.add("returnsAgentRefresh");
		f.add("returnsInfo");
		f.add("returnsView");
		f.add("returnsViewAjax");

		// RMA
		// f.add("addRmasToAccount");
		// f.add("addRmasToAccountByIds");
		f.add("changeStatusRmaByIds");
		f.add("rmaAccept");
		f.add("rmaAdd");
		f.add("rmaDelete");
		f.add("rmaEdit");
		f.add("rmaInfo");
		f.add("rmaReject");
		f.add("rmaStatusTypeSet");
		f.add("rmaTypeDelete");
		f.add("rmaView");
		f.add("rmaAttachmentAdd");
		f.add("rmaAttachmentDelete");
		f.add("rmaAttachmentEdit");
		f.add("rmaAttachmentInfo");
		f.add("rmaAttachmentView");

		// SETTINGS
		// f.add("settingsEdit");
		// f.add("settingsInfo");
		// f.add("settingsView");

		// SHIPMENT
		f.add("shipmentAdd");
		f.add("shipmentFormatMe24Ajax");
		f.add("shipmentOptionConditionMe24Ajax");
		f.add("shipmentView");

		query.append(generateFunctionality(functionalityGroups,
				functionalitiesWithId, "PARTNER_WAREHOUSEMAN_ROLE", f));

		return query;
	}

	private static StringBuilder funcEmployee(
			HashMap<String, HashSet<String>> functionalityGroups,
			HashMap<String, Long> functionalitiesWithId) {
		StringBuilder query = new StringBuilder();
		HashSet<String> f = new HashSet<String>();

		// ACCOUNT_TYPE
		// f.add("accountTypeAdd");
		// f.add("accountTypeDelete");
		// f.add("accountTypeEdit");
		// f.add("accountTypeInfo");
		// f.add("accountTypeView");

		// ACCOUNTS
		// f.add("accountsAdd");
		// f.add("accountsDelete");
		f.add("accountsEdit");
		// f.add("accountsInfo");
		// f.add("accountsLoginsAjax");
		// f.add("accountsView");
		// f.add("accountsViewAjax");

		// ACCOUNTTYPE_FUNCTIONALITY
		// f.add("accountTypeFunctionalityAdd");
		// f.add("accountTypeFunctionalityAddByIds");
		// f.add("accountTypeFunctionalityAddByIdsAjax");
		// f.add("accountTypeFunctionalityDelete");
		// f.add("accountTypeFunctionalityEdit");
		// f.add("accountTypeFunctionalityInfo");
		// f.add("accountTypeFunctionalitySaveOrUpdateAjax");
		// f.add("accountTypeFunctionalityView");

		// ADDRESSES_EVIDENCE
		// f.add("addressesEvidenceAdd");
		// f.add("addressesEvidenceDelete");
		// f.add("addressesEvidenceEdit");
		// f.add("addressesEvidenceInfo");
		// f.add("addressesEvidenceView");
		// f.add("addressEvidenceDefaultAjax");
		// f.add("addressEvidenceListAjax");

		// AJAX_ME24
		// f.add("calculateShipmentMe24Ajax");
		f.add("cityMe24Ajax");
		f.add("countryMe24Ajax");
		f.add("districtMe24Ajax");
		// f.add("mest24RemoteTest");
		f.add("regionMe24Ajax");
		f.add("streetMe24Ajax");

		// AJAX_OTHER
		f.add("countryAjax");
		f.add("currencyViewAjax");
		f.add("vatViewAjax");
		f.add("checkBankAccount");

		// API
		// f.add("apiInfo");

		// BARCODE_POOL
		// f.add("barCodePoolAdd");
		// f.add("barCodePoolDelete");
		// f.add("barCodePoolEdit");
		// f.add("barCodePoolInfo");
		// f.add("barCodePoolView");
		// f.add("barCodeTest");

		// BASIC
		f.add("400");
		f.add("404");
		f.add("405");
		f.add("500");
		f.add("accessDenied");
		f.add("activationAccount");
		f.add("checkEmailAvailabilityAjax");
		f.add("devExpressView");
		f.add("index");
		f.add("login");
		f.add("logout");
		f.add("remindPassword");

		// CASH_ON
		// f.add("cashOnDeliveryImport");
		// f.add("cashOnDeliveryImportCsv");
		// f.add("cashOnDeliveryView");
		// f.add("codParcelAdd");
		// f.add("cashOnDeliveryViewAjax");

		// CHART
		// f.add("chartView");
		// f.add("invoiceValuesByPartnerAjax");
		// f.add("itemsByDateAjax");
		// f.add("itemsByPartnerAjax");
		// f.add("parcelCountByDateAjax");
		// f.add("parcelPositionChartAjax");
		// f.add("parcelStatusAjax");
		// f.add("returnsByPartnerAjax");

		// COMPANY
		// f.add("companyAdd");
		// f.add("companyDel");
		// f.add("companyEdit");
		// f.add("companyInfo");
		// f.add("companyView");
		// f.add("companyViewAjax");

		// CONTRACT
		// f.add("contractAdd");
		// f.add("contractEdit");
		// f.add("contractInfo");
		// f.add("contractView");

		// CUSTOMS_CODES
		// f.add("customsCodesAdd");
		// f.add("customsCodesAjax");
		// f.add("customsCodesDelete");
		// f.add("customsCodesEdit");
		// f.add("customsCodesInfo");
		// f.add("customsCodesView");

		// DISPATCH_REGISTER
		// f.add("dispatchRegisterAdd");
		// f.add("dispatchRegisterCreate");
		// f.add("dispatchRegisterEdit");
		// f.add("dispatchRegisterInfo");
		// f.add("dispatchRegisterView");
		// f.add("generateDispatchRegisterPdf");

		// DIVISIONS
		// f.add("divisionMe24Ajax");

		// DOCUMENT
		// f.add("documentGeneratePdf");
		// f.add("documentInfo");
		// f.add("documentPrintHistoryView");
		// f.add("documentView");

		// DOCUMENT_TEMPLATE
		// f.add("documentTemplateAdd");
		// f.add("documentTemplateCopy");
		// f.add("documentTemplateEdit");
		// f.add("documentTemplateFieldAjaxView");
		// f.add("documentTemplateFieldView");
		// f.add("documentTemplateInfo");
		// f.add("documentTemplateView");

		// EMPLOYEE
		// f.add("employeeAdd");
		// f.add("employeeEdit");
		f.add("employeeInfo");
		// f.add("employeesView");

		// FILE
		f.add("addMultipleFile");
		f.add("deleteAttach");
		f.add("deleteAttachFineUploader");
		f.add("fileNotFound");
		f.add("removeFile");
		f.add("uploadFile");

		// IMPORT/EXPORT
		// f.add("importFilesView");
		// f.add("getImportXlsFilesStatusAjax");
		// f.add("parcelImport");
		// f.add("parcelExportXls");

		// // FUNCTIONALITY
		// f.add("functionalityAdd");
		// f.add("functionalityDelete");
		// f.add("functionalityEdit");
		// f.add("functionalityInfo");
		// f.add("functionalityUpdate");
		// f.add("functionalityView");
		// f.add("functionalityViewAjax");
		//
		// // FUNCTIONALITY_GROUP
		// f.add("functionalityGroupAdd");
		// f.add("functionalityGroupDelete");
		// f.add("functionalityGroupEdit");
		// f.add("functionalityGroupView");

		// INVOICE
		// f.add("envelopeGeneratePdfByIdInvoices");
		// f.add("invoiceAdd");
		// f.add("invoiceAddByParcels");
		// f.add("invoiceCopy");
		// f.add("invoiceCorrect");
		// f.add("invoiceDel");
		// f.add("invoiceEdit");
		// f.add("invoiceGenerateEnvelopePdf");
		// f.add("invoiceGeneratePdf");
		// f.add("invoiceInfo");
		// f.add("invoiceParcelAdd");
		// f.add("invoicePayment");
		// f.add("invoiceRemind");
		// f.add("invoiceSend");
		// f.add("invoiceView");
		// f.add("invoiceViewAjax");
		// f.add("invoiceViewTableAjax");

		// MESSAGES
		f.add("countMessagesReceivedAjax");
		f.add("countMessagesTemplateAjax");
		f.add("countMessagesTrashAjax");
		f.add("editTemplate");
		f.add("editTemplateAjax");
		f.add("getMessageHeaderBean");
		f.add("markMessageAsReadAjax");
		f.add("markMultipleMessagesAsReadAjax");
		f.add("messageAttachmentsView");
		f.add("messagesContinue");
		f.add("messagesDel");
		f.add("messagesDelAll");
		f.add("messagesFind");
		f.add("messagesForward");
		f.add("messagesInfo");
		f.add("messagesNew");
		f.add("messagesReply");
		f.add("messagesRet");
		f.add("messagesRmv");
		f.add("messagesView");
		f.add("saveTemplate");
		f.add("saveTemplateAjax");

		// PARCEL
		// f.add("parcelAdd");
		// f.add("parcelDelete");
		// f.add("parcelAddStep2");
		// f.add("parcelDocumentGeneratePdf");
		// f.add("parcelInfo");
		// f.add("parcelListAjax");
		// f.add("parcelRefresh");
		// f.add("parcelUpdateInformation");
		// f.add("parcelView");
		// f.add("parcelViewTableAjax");
		// f.add("parcelDelete");
		// f.add("parcelViewAngularAjax");

		// PARCEL_TRACKING
		// f.add("parcelTrackingHistoryRosanRefresh");
		// f.add("parcelTrackingHistoryRosanView");
		// f.add("parcelTrackingHistoryRosanAjax");

		// PARTNER
		// f.add("loggedPartnerMainInformation");
		// f.add("partnerAdd");
		// f.add("partnerAjaxInfo");
		// f.add("partnerDebtAjax");
		// f.add("partnerDelete");
		// f.add("partnerEdit");
		// f.add("partnerFindByIdsAjax");
		// f.add("partnerGetNameAjax");
		// f.add("partnerInfo");
		// f.add("partnerListWihoutGroupAjax");
		// f.add("partnerSendInvitation");
		// f.add("partnerView");
		// f.add("partnerViewAjax");

		// PRICING
		// f.add("pricingAdd");
		// f.add("pricingDelete");
		// f.add("pricingEdit");
		// f.add("pricingInfo");
		// f.add("pricingView");

		// RETURNS
		// f.add("returnsAgentImport");
		// f.add("returnsAgentRefresh");
		// f.add("returnsInfo");
		// f.add("returnsView");
		// f.add("returnsViewAjax");

		// RMA
		// f.add("addRmasToAccount");
		// f.add("addRmasToAccountByIds");
		// f.add("changeStatusRmaByIds");
		// f.add("rmaAccept");
		// f.add("rmaAdd");
		// f.add("rmaDelete");
		// f.add("rmaEdit");
		// f.add("rmaInfo");
		// f.add("rmaReject");
		// f.add("rmaStatusTypeSet");
		// f.add("rmaTypeDelete");
		// f.add("rmaView");
		// f.add("rmaAttachmentAdd");
		// f.add("rmaAttachmentDelete");
		// f.add("rmaAttachmentEdit");
		// f.add("rmaAttachmentInfo");
		// f.add("rmaAttachmentView");

		// SETTINGS
		// f.add("settingsEdit");
		// f.add("settingsInfo");
		// f.add("settingsView");

		// SHIPMENT
		// f.add("shipmentAdd");
		// f.add("shipmentFormatMe24Ajax");
		// f.add("shipmentOptionConditionMe24Ajax");
		// f.add("shipmentView");

		query.append(generateFunctionality(functionalityGroups,
				functionalitiesWithId, "EMPLOYEE_ROLE", f));

		return query;
	}

	public static String getGroupName(String functionalityName) {

		class FunctionalityGroups {
			private HashMap<String, HashSet<String>> groups;

			public FunctionalityGroups() {
				groups = new HashMap<String, HashSet<String>>();
			}

			public void add(String group, String functionality) {
				HashSet<String> functionalitiesInGroup = groups.get(group);
				if (functionalitiesInGroup == null) {
					functionalitiesInGroup = new HashSet<String>();
				}
				functionalitiesInGroup.add(functionality);
				groups.put(group, functionalitiesInGroup);
			}

			public HashMap<String, HashSet<String>> getGroups() {
				return groups;
			}

		}

		String groupName = functionalityName;

		FunctionalityGroups fg = new FunctionalityGroups();

		fg.add("ACCOUNTS", "accountsLoginsAjax");
		fg.add("ADDRESSES_EVIDENCE", "addressEvidenceDefaultAjax");
		fg.add("AJAX_OTHER", "countryAjax");
		fg.add("AJAX_OTHER", "currencyViewAjax");
		fg.add("AJAX_OTHER", "vatViewAjax");
		fg.add("AJAX_OTHER", "checkBankAccount");
		fg.add("AJAX_ME24", "calculateShipmentMe24Ajax");
		fg.add("AJAX_ME24", "cityMe24Ajax");
		fg.add("AJAX_ME24", "countryMe24Ajax");
		fg.add("AJAX_ME24", "districtMe24Ajax");
		fg.add("AJAX_ME24", "mest24RemoteTest");
		fg.add("AJAX_ME24", "regionMe24Ajax");
		fg.add("AJAX_ME24", "streetMe24Ajax");
		fg.add("AJAX_ME24", "divisionMe24Ajax");
		fg.add("API", "api/city");
		fg.add("API", "api/division");
		fg.add("ADDRESSES_EVIDENCE", "addressEvidenceListAjax");
		fg.add("BARCODE_POOL", "barCodeTest");
		fg.add("BASIC", "login");
		fg.add("BASIC", "logout");
		fg.add("BASIC", "index");
		fg.add("BASIC", "accessDenied");
		fg.add("BASIC", "activationAccount");
		fg.add("BASIC", "index");
		fg.add("BASIC", "remindPassword");
		fg.add("BASIC", "checkEmailAvailabilityAjax");
		fg.add("BASIC", "devExpressView");
		fg.add("BASIC", "400");
		fg.add("BASIC", "404");
		fg.add("BASIC", "405");
		fg.add("BASIC", "500");
		fg.add("CHART", "itemsByPartnerAjax");
		fg.add("CHART", "itemsByDateAjax");
		fg.add("CHART", "returnsByPartnerAjax");
		fg.add("CHART", "parcelStatusAjax");
		fg.add("CHART", "parcelPositionChartAjax");
		fg.add("CHART", "parcelCountByPartnerAjax");
		fg.add("CHART", "parcelCountByDateAjax");
		fg.add("CHART", "invoiceValuesByPartnerAjax");
		fg.add("CUSTOMS_CODES", "customsCodesAjax");
		fg.add("CASH_ON", "codParcelAdd");
		fg.add("DISPATCH_REGISTER", "dispatchRegisterCreate");
		fg.add("DOCUMENT", "documentPrintHistoryView");
		fg.add("DOCUMENT", "documentGeneratePdf");
		fg.add("DOCUMENT_TEMPLATE", "documentTemplateFieldView");
		fg.add("DOCUMENT_TEMPLATE", "documentTemplateFieldAjaxView");
		fg.add("DOCUMENT_TEMPLATE", "documentTemplateCopy");
		fg.add("FILE", "addMultipleFile");
		fg.add("FILE", "deleteAttach");
		fg.add("FILE", "deleteAttachFineUploader");
		fg.add("FILE", "fileNotFound");
		fg.add("FILE", "removeFile");
		fg.add("FILE", "uploadFile");
		fg.add("SHIPMENT", "shipmentOptionConditionMe24Ajax");
		fg.add("SHIPMENT", "shipmentFormatMe24Ajax");
		fg.add("EMPLOYEE", "employeesView");
		fg.add("IMPORT_EXPORT", "importFilesView");
		fg.add("IMPORT_EXPORT", "parcelExportXls");
		fg.add("IMPORT_EXPORT", "parcelImport");
		fg.add("IMPORT_EXPORT", "getImportXlsFilesStatusAjax");
		fg.add("INVOICE", "invoiceRemind");
		fg.add("INVOICE", "invoiceCopy");
		fg.add("INVOICE", "invoiceParcelAdd");
		fg.add("INVOICE", "invoiceCorrect");
		fg.add("INVOICE", "invoiceGeneratePdf");
		fg.add("INVOICE", "invoiceGenerateEnvelopePdf");
		fg.add("INVOICE", "invoicePayment");
		fg.add("INVOICE", "envelopeGeneratePdfByIdInvoices");
		fg.add("MESSAGES", "messagesContinue");
		fg.add("MESSAGES", "messagesFind");
		fg.add("MESSAGES", "messagesForward");
		fg.add("MESSAGES", "messagesNew");
		fg.add("MESSAGES", "messagesReply");
		fg.add("MESSAGES", "messagesRet");
		fg.add("MESSAGES", "messagesRmv");
		fg.add("MESSAGES", "markMessageAsReadAjax");
		fg.add("MESSAGES", "markMultipleMessagesAsReadAjax");
		fg.add("MESSAGES", "getMessageHeaderBean");
		fg.add("MESSAGES", "countMessagesReceivedAjax");
		fg.add("MESSAGES", "countMessagesTemplateAjax");
		fg.add("MESSAGES", "countMessagesTrashAjax");
		fg.add("MESSAGES", "saveTemplate");
		fg.add("MESSAGES", "saveTemplateAjax");
		fg.add("MESSAGES", "editTemplate");
		fg.add("MESSAGES", "editTemplateAjax");
		fg.add("MESSAGES", "messageAttachmentsView");
		fg.add("PARCEL", "parcelDocumentGeneratePdf");
		fg.add("PARCEL", "parcelPositionChartAja");
		fg.add("PARCEL", "parcelRefresh");
		fg.add("PARCEL_TRACKING", "parcelTrackingHistoryRosanRefresh");
		fg.add("PARCEL_TRACKING", "parcelTrackingHistoryRosanAjax");
		fg.add("PARCEL_TRACKING", "parcelTrackingHistoryRosanView");
		fg.add("PARTNER", "partnerDebtAjax");
		fg.add("PARTNER", "partnerAjaxInfo");
		fg.add("PARTNER", "loggedPartnerMainInformation");
		fg.add("PARTNER", "partnerDebtAjax");
		fg.add("PARTNER", "partnerFindByIdsAjax");
		fg.add("PARTNER", "partnerGetNameAjax");
		fg.add("RETURNS", "returnsAgentImport");
		fg.add("RETURNS", "returnsAgentRefresh");
		fg.add("RMA", "addRmasToAccount");
		fg.add("RMA", "addRmasToAccountByIds");
		fg.add("RMA", "rmaTypeDelete");
		fg.add("RMA", "rmaAccept");
		fg.add("RMA", "rmaReject");
		fg.add("RMA", "rmaStatusTypeSet");
		fg.add("RMA", "changeStatusRmaByIds");

		for (Entry<String, HashSet<String>> group : fg.getGroups().entrySet()) {
			String gName = group.getKey();
			HashSet<String> functionalities = group.getValue();
			if (functionalities != null) {
				for (String f : functionalities) {
					if (functionalityName.equals(f)) {
						return gName;
					}
				}
			}
		}

		if (groupName.equals(functionalityName)) {
			groupName = "OTHERS";
		}

		return groupName;
	}

}
