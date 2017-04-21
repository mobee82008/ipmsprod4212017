<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<script>
$('#tab2').bind('tabsselect', function(event, ui) {
	
	alert(ui.tab);

    // Objects available in the function context:
    //ui.tab     // anchor element of the selected (clicked) tab
    //ui.panel   // element, that contains the selected/clicked tab contents
    //ui.index   // zero-based index of the selected (clicked) tab
    alert("Good Luck");

});
</script>

<div id="breadcrumb">
    <ul>
        <li>
            <a href="${pageContext.request.contextPath}/app/groupdashboard">Home</a> <span> >> </span>
        </li>
        <li>
            <a href="#" style="text-decoration: none;">Artifacts</a>
        </li>
    </ul>
</div>
<div class="content">
    <div class="grid_container">
        <div class="grid_12">
            <div class="widget_wrap">
                <div class="widget_content">
                    <div class="widget_top">
                        <span class="h_icon list_image"></span>
                        <h6>Artifacts Manager</h6>
                    </div>

                    <div id="tabs" class="widget_wrap tabby">
                        <div class="widget_top">
                            <div id="widget_tab" style="float:left;">
                                <ul>
                                    <li><a href="#tab1">Issues</a></li>
                                    <li><a href="#tab2">Action Items</a></li>
                                    <li><a href="#tab3">Lessons Learned</a></li>
                                    <li><a href="#tab4">Risks</a></li>
                                    <li><a href="#tab5">Tasks</a></li>
                                </ul>
                            </div>
                        </div>
                        <div class="widget_content">
                        <c:if test="${not empty success}">
                        <div class="successblock"><spring:message code="${success}"></spring:message>
                        </div>
                  		</c:if>
                  		
                            <sec:authorize access="hasRole('ROLE_PROGRAM_MANAGER')">
                            <div id="tab1">
                            	<table class="display data_tbl">
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Summary</th>
                                        <th>Project</th>
                                        <th>Reported By</th>
                                        <th>Assignee</th>
                                        <th>Status</th>
                                        <th>Priority</th>
                                        <th>Resolution</th>
                                        <th>Created</th>
                                        <th>Updated</th>
                                        <th>Due Date</th>
                                        <th>Administer></th>
                                    </tr>
                                    </thead>
									<tbody>
                                    <c:forEach var="issue" items="${issues}">
                                        <tr>
                                            <td>${issue.id }</td>
                                            <td><a href="${pageContext.request.contextPath}/app/issue/${issue.id }">${issue.summary }</a></td>
                                            <td>${issue.project.name}</td>
                                            <td>${issue.assigned.name }</td>
                                            <td>${issue.assignee.name }</td>
                                            <td><span class="badge_style b_${fn:toLowerCase(issue.status)}">${issue.status }</span></td>
                                            <td><span class="badge_style b_${fn:toLowerCase(issue.priority)}">${issue.priority }</span></td>
                                            <td>${issue.summary }</td>
                                            <td><fmt:formatDate type="date" value='${issue.dateReported }' /></td>
                                            <td><fmt:formatDate type="date" value='${issue.dueDate }' /></td>
                                            <td><fmt:formatDate type="date" value='${issue.dueDate }' /></td>
                                            <td style="white-space: nowrap;">
                                                <span><a class="action-icons c-edit" href="${pageContext.request.contextPath}/app/edit-issue/${issue.id}" title="Edit">Edit</a></span><!-- <span><a class="action-icons c-delete" href="#" title="delete">Delete</a></span> --><span><a class="action-icons c-approve" href="${pageContext.request.contextPath}/app/new-issue" title="Create">Create</a></span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                              </div>
                              </sec:authorize>
                            
                            
                            <div id="tab2">
                                <table class="display data_tbl">
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Summary</th>
                                        <th>Assigned To</th>
                                        <th>Date Due</th>
                                        <th>Status</th>
                                        <th>Administer></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <c:forEach var="actionitem" items="${actionItems}">
                                        <tr>
                                            <td>${actionitem.id}</td>
                                            <td><a href="${pageContext.request.contextPath}/app/actionitem/${actionitem.id}"><c:out value="${actionitem.summary}" /></a></td>
                                            <td>${actionitem.assignedTo.name}</td>
                                            <td><fmt:formatDate type="date" value="${actionitem.dueDate}" /></td>
                                            <td><span class="badge_style b_${fn:toLowerCase(actionitem.status)}">${actionitem.status}</span></td>
                                            <td style="white-space: nowrap;">
                                                <span><a class="action-icons c-edit" href="${pageContext.request.contextPath}/app/edit-actionitem/${actionitem.id}" title="Edit">Edit</a></span><!-- <span><a class="action-icons c-delete" href="#" title="delete">Delete</a></span> --><span><a class="action-icons c-approve" href="${pageContext.request.contextPath}/app/new-actionitem" title="Create">Create</a></span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                    <tfoot>
                                    <tr>
                                        <td colspan="6"><a class="activeobjectlink" href="${pageContext.request.contextPath}/app/new-actionitem"><button
                                            class="btn_small btn_blue">Add Action Item</button></a></td>
                                    </tr>
                                    </tfoot>
                                </table>
                            </div>

                            <div id="tab3">
                                <table class="display data_tbl">
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Summary</th>
                                        <th>Impact</th>
                                        <th>Recommendation</th>
                                        <th>Administer></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <c:forEach var="lessonsLearned" items="${lessonsLearned}">
                                        <tr>
                                            <td>${lessonsLearned.id}</td>
                                            <td><a href="lessonslearned/${lessonsLearned.id}">${lessonsLearned.summary}</a></td>
                                            <td>${lessonsLearned.impact}</td>
                                            <td>${lessonsLearned.recommendation}</td>
                                            <td style="white-space: nowrap;">
                                                <span><a class="action-icons c-edit" href="${pageContext.request.contextPath}/app/edit-lessonslearned/${lessonsLearned.id}" title="Edit">Edit</a></span><span><a class="action-icons c-approve" href="${pageContext.request.contextPath}/app/new-lessonslearned" title="Create">Create</a></span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                    <tfoot>
                                    <tr>
                                        <td colspan="5"><a class="activeobjectlink" href="${pageContext.request.contextPath}/app/new-lessonslearned"><button
                                                class="btn_small btn_blue">Add Lessons Learned</button></a></td>
                                    </tr>
                                    </tfoot>
                                </table>
                            </div>
                            <div id="tab4">
                                <table class="display data_tbl">
                                    <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Summary</th>
                                        <th>Mitigation Plan</th>
                                        <th>Risk/Cost</th>
                                        <th>Risk - Accept/transfer/Avoid</th>
                                        <th>Administer></th>
                                    </tr>
                                    </thead>

                                    <tbody>
                                    <c:forEach var="risk" items="${risks}">
                                        <tr>
                                            <td>${risk.id }</td>
                                            <td><a href="risk">${risk.riskSummary}</a></td>
                                            <td>${risk.mitigatingFactors}</td>
                                            <td>${risk.likelihood}</td>
                                            <td>${risk.impact}</td>
                                            <td style="white-space: nowrap;">
                                                <span><a class="action-icons c-edit" href="${pageContext.request.contextPath}/app/edit-risk/${risk.id}" title="Edit">Edit</a></span><!-- <span><a class="action-icons c-delete" href="#" title="delete">Delete</a></span> --><span><a class="action-icons c-approve" href="${pageContext.request.contextPath}/app/new-risk" title="Create">Create</a></span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                    <tfoot>
                                    <tr>
                                        <td colspan="6"><a class="activeobjectlink" href="${pageContext.request.contextPath}/app/new-risk"><button
                                                class="btn_small btn_blue">Add Risk</button></a></td>
                                    </tr>
                                    </tfoot>
                                </table>
                            </div>
                             <div id="tab5">
                             <table class="display data_tbl">
                        <thead>
                        <tr>
                            <th>Description</th>
                            <th>ID</th>
                            <th>Created By</th>
                            <th>Assigned To</th>
                            <th>Date Due</th>
                            <th>Status</th>
                            <th>Administer></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="task" items="${tasks}">
                            <tr>
                                <td><a href="task/${task.id}">${task.description}</a></td>
                                <td>${task.id}</td>
                                <td>${task.createdBy.name}</td>
                                <td>${task.assignedTo.name}</td>
                                <td><fmt:formatDate type="date" value="${task.dueDate}" /></td>
                                <td><span class="badge_style b_${fn:toLowerCase(task.status)}">${task.status }</span></td>
                                <td>
                                    <span><a class="action-icons c-edit" href="${pageContext.request.contextPath}/app/edit-task/${task.id}" title="Edit">Edit</a></span><span><a class="action-icons c-approve" href="${pageContext.request.contextPath}/app/new-task" title="Create">Create</a></span>
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
    </div>

</div>
</div>
