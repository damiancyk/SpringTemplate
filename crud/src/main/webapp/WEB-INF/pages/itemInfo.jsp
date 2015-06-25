<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page isELIgnored="false"%>

<div class="box">
	<div class="box-header">
		<div class="box-title">
			<a href="pricingView.html"><fmt:message key="menu.pricings" /> </a>
			<span class="active">/ <fmt:message
					key="pricing.info.infoPrice" /> </span>
		</div>

	</div>
	<div class="info-site partner-info">
		<div class="container-fluid">
			<div class="row package-info-title">
				<div class="col-md-6">
					<h2>
						<span>${pricing.name}</span>
					</h2>
				</div>
				<div class="col-md-6">
					<h2 class="right">
						<span><fmt:message key="form.partner" />:</span> <a
							href="partnerInfo-${pricing.idPartner }.html">${pricing.partnerName}</a>
					</h2>
				</div>
			</div>

			<hr>

			<div class="row ">
				<div class="col-md-12">
					<h2><fmt:message key="form.basicInformations" /></h2>
				</div>
				
				<div class="col-md-6 package-info">
					
					<ul>
						<li><strong><fmt:message
									key="pricingInfo.pricing.kind" />: </strong> <fmt:message
								key="${pricing.pricingType.text}" /></li>
						<li><strong><fmt:message key="form.contract" />: </strong> <a
							href="contractInfo-${pricing.idContract}.html">${pricing.contractNumber}</a>
						</li>
						<li><strong><fmt:message key="pricingInfo.valid" />:
						</strong> <fmt:message key="pricingInfo.from" /> <fmt:formatDate
								value="${pricing.dateFrom}" /> do <fmt:formatDate
								value="${pricing.dateTo}" /></li>

						<li><strong> <fmt:message key="pricing.customsPrice" />:
						</strong> <fmt:formatNumber value="${pricing.customsPrice}"
								minFractionDigits="2" maxFractionDigits="2" />
						</li>


						<li><strong> <fmt:message key="pricing.sadBisPrice" />:
						</strong> <fmt:formatNumber value="${pricing.sadBisPrice}"
								minFractionDigits="2" maxFractionDigits="2" />
						</li>

						
					</ul>
				</div>
				
				<div class="col-md-6 package-info">
					<ul>
						<li><strong> <fmt:message
									key="pricing.moneyBackPrice" />: </strong> <fmt:formatNumber
								value="${pricing.moneyBackPrice}" minFractionDigits="1"
								maxFractionDigits="2" />
						</li>
					
						<li><strong> <fmt:message
									key="pricing.addressChangePrice" />: </strong> <fmt:formatNumber
								value="${pricing.addressChangePrice}" minFractionDigits="1"
								maxFractionDigits="2" />
						</li>

						
						<c:if test="${pricing.returnType=='EURO'}">
							<li><strong><fmt:message
										key="pricinAddParcel.service.for.return.missed.parcel2" />: </strong>
								<fmt:formatNumber value="${pricing.returnPriceEuro}"
									minFractionDigits="2" maxFractionDigits="2" /> EUR</li>
						</c:if>
						<c:if test="${pricing.returnType=='PERCENT'}">
							<li><strong><fmt:message
										key="pricinAddParcel.service.for.return.missed.parcel2" />: </strong>
								<fmt:formatNumber value="${pricing.returnPricePercent}"
									minFractionDigits="1" maxFractionDigits="2" /> % wartości
								paczki</li>
						</c:if>
					</ul>
				</div>
				
			</div>		

			<br><br>	
					
			<div class="row">

				<div class="col-md-12 ">
					<c:if test="${pricing.returnType=='RANGES'}">
							<c:if test="${fn:length(pricing.returnPricingWeightRanges)>0}">
								<h2><fmt:message
											key="pricinAddParcel.service.for.return.missed.parcel2" /> (<fmt:message
											key="pricingAdd.weight" />) </h2>
									<table class="table-main-style">
										<tr>
											<th><fmt:message key="pricingAdd.weight.from2" />
											</th>
											<th><fmt:message key="pricingAdd.weight.to2" />
											</th>
											<th><fmt:message key="pricingInfo.price" /> [EUR]</th>
											<th><fmt:message key="pricingInfo.price" /> [PLN]</th>
											<th><fmt:message key="pricingInfo.price" /> [UAH]</th>
										</tr>
										<c:forEach items="${pricing.returnPricingWeightRanges}"
											var="returnPricingWeightRange">
											<tr>
												<td><fmt:formatNumber
														value="${returnPricingWeightRange.weightFrom}"
														minFractionDigits="1" maxFractionDigits="2" />
												</td>
												<td><fmt:formatNumber
														value="${returnPricingWeightRange.weightTo}"
														minFractionDigits="1" maxFractionDigits="2" />
												</td>
												<td><fmt:formatNumber
														value="${returnPricingWeightRange.euroNetto}"
														minFractionDigits="2" maxFractionDigits="2" />
												</td>
												<td><fmt:formatNumber
														value="${returnPricingWeightRange.plnNetto}"
														minFractionDigits="2" maxFractionDigits="2" />
												</td>
												<td><fmt:formatNumber
														value="${returnPricingWeightRange.uahNetto}"
														minFractionDigits="2" maxFractionDigits="2" />
												</td>
											</tr>

										</c:forEach>
									</table>
								
							</c:if>
						</c:if>
					
					
					<br><br>
					<h2>
						<fmt:message key="pricing.info.COD.transfer" />
					</h2>
					<div>
						<h5><strong><fmt:message
									key="pricingAdd.minAmountComissionCOD2" />: </strong> <fmt:formatNumber
								value="${pricing.codMinPrice}" minFractionDigits="2"
								maxFractionDigits="2" /> EUR</li>
						</h5>
					</div>
					<table class="table-main-style">
						<tr>
							<th><fmt:message key="pricingInfo.value.from" /> (UAH)</th>
							<th><fmt:message key="pricingInfo.value.to" /> (UAH)</th>
							<th><fmt:message key="pricingAdd.comission.percent" /></th>
						</tr>
						<c:if test="${fn:length(pricing.pricingCodRanges)==0}">
							<tr>
								<td colspan="3">-</td>
							</tr>
						</c:if>
						<c:forEach items="${pricing.pricingCodRanges}"
							var="pricingCodRange">
							<tr>
								<td><fmt:formatNumber
										value="${pricingCodRange.uahValueFrom}" minFractionDigits="2"
										maxFractionDigits="2" /></td>
								<td><fmt:formatNumber value="${pricingCodRange.uahValueTo}"
										minFractionDigits="2" maxFractionDigits="2" /></td>
								<td><fmt:formatNumber value="${pricingCodRange.percentage}"
										minFractionDigits="1" maxFractionDigits="2" /></td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
				
			<br><br>	
			<div class="row">
				<div class="col-md-12">

					<c:if test="${pricing.pricingType=='WITH_WEIGHT'}">
						<h2>
							<fmt:message key="pricing.type.with.weight" />
						</h2>
						<table class="table-main-style">
							<tr>
								<th><fmt:message key="pricingAdd.weight.from" /></th>
								<th><fmt:message key="pricingAdd.weight.to" /></th>
								<th><fmt:message key="pricingInfo.price.consignment" /></th>
							</tr>
							<c:if test="${fn:length(pricing.pricingWeightRanges)==0}">
								<tr>
									<td colspan="3">-</td>
								</tr>
							</c:if>
							<c:forEach items="${pricing.pricingWeightRanges}"
								var="pricingWeightRange">
								<tr>
									<td><fmt:formatNumber
											value="${pricingWeightRange.weightFrom}"
											minFractionDigits="2" maxFractionDigits="2" /></td>
									<td><fmt:formatNumber
											value="${pricingWeightRange.weightTo}" minFractionDigits="2"
											maxFractionDigits="2" /></td>
									<td><fmt:formatNumber
											value="${pricingWeightRange.euroNetto}" minFractionDigits="2"
											maxFractionDigits="2" /> EUR</td>
								</tr>
							</c:forEach>
						</table>
					</c:if>

					<c:if test="${pricing.pricingType=='PARCELS_PER_MONTH'}">
						<div class="row">
							<div class="col-md-12">
								<h2>
									<fmt:message key="pricinAddParcel.ranges.parcels" />
								</h2>

								<c:forEach items="${pricing.pricingParcelRanges}"
									var="pricingParcelRange">
									<h5><strong>Nazwa cennika:</strong> ${pricingParcelRange.name}</h5>
									<table class="table-main-style">
										<tr>
											<th><fmt:message key="pricinAddParcel.percent.of.return" />
											</th>
											<th><fmt:message key="pricingAdd.weight" /></th>
										</tr>
										<c:forEach
											items="${pricingParcelRange.pricingParcelRangePositions}"
											var="pricingParcelRangePosition">
											<tr>
												<td><strong><fmt:formatNumber
															value="${pricingParcelRangePosition.percentFrom}"
															minFractionDigits="2" maxFractionDigits="2" />% </strong> <c:choose>
														<c:when
															test="${pricingParcelRangePosition.percentTo!=null}">
																do <strong><fmt:formatNumber
																	value="${pricingParcelRangePosition.percentTo}"
																	minFractionDigits="2" maxFractionDigits="2" />% </strong>
														</c:when>
														<c:otherwise>
															<fmt:message key="pricingInfo.and.more" />
														</c:otherwise>
													</c:choose></td>
												<td><table class="tableWidthForm" style="width: 100%;">
														
														<c:forEach
															items="${pricingParcelRangePosition.pricingWeightRanges}"
															var="pricingWeightRange">
														
														<tr>
															<th><fmt:message key="pricingAdd.weight.from" /></th>
															<th><fmt:message key="pricingAdd.weight.to" /></th>
															<th><fmt:message key="pricingInfo.price.consignment" />
															</th>
														</tr>
															<tr>
																<td><fmt:formatNumber
																		value="${pricingWeightRange.weightFrom}"
																		minFractionDigits="2" maxFractionDigits="2" /></td>
																<td><fmt:formatNumber
																		value="${pricingWeightRange.weightTo}"
																		minFractionDigits="2" maxFractionDigits="2" /></td>
																<td><c:if
																		test="${pricingWeightRange.euroNetto!=null}">
																		<fmt:formatNumber
																			value="${pricingWeightRange.euroNetto}"
																			minFractionDigits="2" maxFractionDigits="2" /> EUR
																			</c:if> <c:if test="${pricingWeightRange.euroNetto==null}">
																		N/D
																		</c:if></td>

															</tr>
															<c:if
																test="${fn:length(pricingWeightRange.pricingWeightRangePositions)>0}">
																<tr>
																	<td colspan="100%">
																		<table class="table table-hover">
																			<tr>
																				<th>Rodzaj dostawy</th>
																				<th>USD</th>
																				<th>PLN</th>
																				<th>EUR</th>
																				<th>UAH</th>
																			</tr>
																			<c:forEach
																				items="${pricingWeightRange.pricingWeightRangePositions}"
																				var="pos">
																				<tr>
																					<td><fmt:message
																							key="${pos.parcelDeliveryType.text}" /></td>
																					<td><fmt:formatNumber value="${pos.usdNetto}"
																							minFractionDigits="2" maxFractionDigits="2" /></td>
																					<td><fmt:formatNumber value="${pos.plnNetto}"
																							minFractionDigits="2" maxFractionDigits="2" /></td>
																					<td><fmt:formatNumber value="${pos.euroNetto}"
																							minFractionDigits="2" maxFractionDigits="2" /></td>
																					<td><fmt:formatNumber value="${pos.uahNetto}"
																							minFractionDigits="2" maxFractionDigits="2" /></td>
																				</tr>
																			</c:forEach>
																		</table></td>
																</tr>
															</c:if>
														</c:forEach>
													</table></td>
											</tr>
										</c:forEach>

									</table>
									<br>
								</c:forEach>

							</div>
						</div>
					</c:if>


				</div>

			</div>





		</div>
	</div>

</div>