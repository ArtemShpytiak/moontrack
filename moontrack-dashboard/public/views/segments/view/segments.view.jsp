<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<div class="col-md-6 col-md-offset-3 ">
		<div class="box box-primary">
			<div class="box-header with-border">
				<h1 class="box-title">${segment.name}</h1>
			</div>
			<div class="box-body pad table-responsive">
				<p>
					Total users:
					<span data-toggle="tooltip" title="" class="badge bg-red">
						${totalusers}
					</span>
				</p>
				<c:choose>
					<c:when test="${countries.isEmpty()}"> </c:when>
					<c:otherwise>
						<p
							>Countries:
							<c:forEach var="country" items="${countries}">
								<span
									data-toggle="tooltip"
									title=""
									class="badge bg-blue"
								>
									${country.getName()}
								</span>
							</c:forEach>
						</p>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${oses.isEmpty()}"> </c:when>
					<c:otherwise>
						<p
							>OSes:
							<c:forEach var="os" items="${oses}">
								<span
									data-toggle="tooltip"
									title=""
									class="badge bg-green"
									>${os.getName()}</span
								>
							</c:forEach>
						</p>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${platforms.isEmpty()}"> </c:when>
					<c:otherwise>
						<p
							>Platforms:
							<c:forEach var="p" items="${p}">
								<span
									data-toggle="tooltip"
									title=""
									class="badge bg-yellow"
									>${p.getName()}</span
								>
							</c:forEach>
						</p>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${paying == null}"> </c:when>
					<c:otherwise>
						<p
							>Paying:
							<span
								data-toggle="tooltip"
								title=""
								class="badge bg-blue"
							>
								${paying}
							</span>
						</p>
						<c:choose>
							<c:when test="${moneyfrom == null}"> </c:when>
							<c:otherwise>
								<p
									>Spent from:
									<span
										data-toggle="tooltip"
										title=""
										class="badge bg-blue"
									>
										${moneyfrom}
									</span>
								</p>
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${moneyto == null}"> </c:when>
							<c:otherwise>
								<p
									>Spent to:
									<span
										data-toggle="tooltip"
										title=""
										class="badge bg-blue"
									>
										${moneyto}
									</span>
								</p>
							</c:otherwise>
						</c:choose>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${regfrom == null}"> </c:when>
					<c:otherwise>
						<p
							>Registration from:
							<span
								data-toggle="tooltip"
								title=""
								class="badge bg-yellow"
								>${regfrom}</span
							>
						</p>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${regtill == null}"> </c:when>
					<c:otherwise>
						<p
							>Registration till:
							<span
								data-toggle="tooltip"
								title=""
								class="badge bg-yellow"
								>${regtill}</span
							>
						</p>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</div>

<c:forEach var="element" items="${elementids}">
	<jsp:include page="components/metric-box.jsp">
		<jsp:param name="metric" value="${element[0]}" />
		<jsp:param name="chart" value="${element[1]}" />
	</jsp:include>
</c:forEach>

<jsp:include page="/public/views/segments/view/script.jsp" />
