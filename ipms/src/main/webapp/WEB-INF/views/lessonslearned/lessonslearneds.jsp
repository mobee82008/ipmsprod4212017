<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="content">
	<div class="grid_container">
		<div class="grid_12">
			<div class="widget_wrap">
				<div class="widget_top">
					<span class="h_icon documents"></span>
					<h6>Lessons Learned</h6>
					<div class="c_actions">
						<ul>
							<li><a
								href="${pageContext.request.contextPath}/app/lessonslearneds/xls"
								title="Export to XLS"> <img
									src="${pageContext.request.contextPath}/resources/images/table-tools/xls_hover.png"
									alt="Export to XLS">
							</a></li>
							<!--<li><a href="#" title="Export to PDF"> <img
									src="${pageContext.request.contextPath}/resources/images/table-tools/pdf_hover.png"
									alt="Export to PDF">
							</a></li>  -->
						</ul>
					</div>
				</div>
				<div class="widget_content">
					<c:if test="${not empty success}">
						<div class="successblock">
							<spring:message code="${success}"></spring:message>
						</div>
					</c:if>
					<table class="display data_tbl">
						<thead>
							<tr>
								<th>Summary</th>
								<th>ID</th>
								<th>Impact</th>
								<th>Recommendation</th>
								<th>Administer></th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="lessonsLearned" items="${lessonsLearneds}">
								<tr>
									<td><a href="lessonslearned/${lessonsLearned.id}"><c:out
												value="${lessonsLearned.summary}" /></a></td>
									<td><c:out value="${lessonsLearned.id}" /></td>
									<td><c:out value="${lessonsLearned.impact}" /></td>
									<td><c:out value="${lessonsLearned.recommendation}" /></td>
									<td style="white-space: nowrap;">
                                                <span><a class="action-icons c-edit" href="${pageContext.request.contextPath}/app/edit-lessonslearned/${lessonsLearned.id}" title="Edit">Edit</a></span><span><a class="action-icons c-approve" href="${pageContext.request.contextPath}/app/new-lessonslearned" title="Create">Create</a></span>
                                            </td>
                                        </tr>
								</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
		</div>

	</div>
</div>