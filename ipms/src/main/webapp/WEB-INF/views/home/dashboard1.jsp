<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!-- // content starts here // -->

<div class="content">


	<div class="row-fluid">
		<div class="span8">
			<ul class="quickstats">
				<li>
					<div class="small-chart" data-color="6a9d29" data-stroke="345010"
						data-type="line">5,3,9,6,5,9,7,3,5,10</div>
					<div class="chart-detail">
						<span class="amount green">55.68%</span> <span class="description">Issue
							Resolution Rate</span>
					</div>
				</li>
				<li>
					<div class="small-chart" data-color="2c5b96" data-stroke="102c50"
						data-type="bar">2,5,4,6,5,4,7,8</div>
					<div class="chart-detail">
						<span class="amount">128.32</span> <span class="description">Issues
							/ Month Ratio </span>
					</div>
				</li>


				<li>
					<div class="small-chart" data-color="2c5b96" data-stroke="102c50"
						data-type="line">521,500,481,429,550,521</div>
					<div class="chart-detail">
						<span class="amount">521.3 / month</span> <span
							class="description">Unique visitors rate</span>
					</div>
				</li>

			</ul>

			<sec:authorize access="hasAnyRole('ROLE_PROJECT_MANAGER','ROLE_PROGRAM_MANAGER')">
				<div class="box">
					<div class="box-head">
						<h3>Projects Snapshot</h3>
						<div style="float:right">
						
						<!-- <select class="cho" >
						   <option>Select another program</option>
						   <c:forEach var="program" items="${programs}">
						      <option><c:out value='${program.name}' /></option>
						   </c:forEach>
						</select>
						-->
						</div>
					</div>
					<div class="box-content box-nomargin">
						<table class='table table-striped dataTable table-bordered'>
							<thead>
								<tr>
								    <th>Project</th>
								    <th>Program</th>
									<th>Issues</th>
									<th>Action Items</th>
									<th>Risks</th>

								</tr>
							</thead>
							<tbody>
								<c:forEach var="project" items="${projects}">
								         <c:if test="${not project.active }">
									       <c:set var="linkcolor" value="activeobjectlink" />
									     </c:if>
									<tr>
									    <td>
									      <a class="${linkcolor }"  href="${pageContext.request.contextPath}/app/project/${project.id}">
									        <c:out value="${project.name }" />
									       </a>
									    </td>
										<td><a class="${linkcolor }"><c:out value="${project.program.name}" /></a></td>
										<td><a class="${linkcolor }"><c:out value="${fn:length(project.issues) }" /></a></td>
										<td><a class="${linkcolor }"><c:out value="${fn:length(project.actionItems) }" /></a></td>
										<td><a class="${linkcolor }"><c:out value="${fn:length(project.risks) }" /></a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</sec:authorize>
            
            <div class="box">
				<div class="box-head">
					<h3>My Tasks</h3>
				</div>
				<div class="box-content box-nomargin">
					<table
						class='table table-striped dataTable table-bordered dataTable-noheader'>
						<thead>
							<tr>
								<th>Task</th>
								<th>Created By</th>
								<th>Date Created</th>
								<th>Date Due</th>
							</tr>
						</thead>
						<tbody>
						    <c:forEach var="task" items="${tasks}">
							<tr>
								<td><a href="${pageContext.request.contextPath}/task/${task.id}"><c:out value="${task.name }" /></a></td>
								<td><c:out value="${task.createdBy.name }" /> </td>
								<td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${task.dateCreated }" /> </td>
								<td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${task.dueDate }" /> </td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<div class="box">
                <div class="box-head">
					<h3>My Action Items</h3>
				</div>
				<div class="box-content box-nomargin">
				<table
						class='table table-striped dataTable table-bordered dataTable-noheader'>
						<thead>
							<tr>
								<th>Action Item</th>
								<th>Created From</th>
								<th>Date Due</th>
							</tr>
						</thead>
						<tbody>
						    <c:forEach var="actionItem" items="${actionItems}">
							<tr>
								<td><a href="actionItem/${actionItem.id}"><c:out value="${fn:substring(actionItem.summary, 1, 10) }" /></a></td>
								<td>
								   <c:if test="${not empty actionItem.issue }">
								      Issue: <c:out value="${actionItem.issue.name}" />
								   </c:if>
								   <c:if test="${not empty actionItem.meeting }">
								      Meeting: <c:out value="${actionItem.meeting.title}" />
								   </c:if>
								 </td>
								<td><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${actionItem.dueDate }" /> </td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
            </div>
            
		</div>
		<div class="span4">
			<div class="box">
				<div class="box-content box-nomargin">

					<div class="box-head">
						<h3>My Meetings</h3>
						<div class="actions">
							<ul>
								<li class="dropdown"><a href="#"
									class='tip btn btn-mini btn-square dropdown-toggle'
									title="Click for more actions" data-toggle="dropdown"> <img
										src="${pageContext.request.contextPath}/resources/img/icons/fugue/gear.png"
										alt="">
								</a>
									<ul class="dropdown-menu pull-right custom">
										<li><a href="#" class='fugue'><img
												src="${pageContext.request.contextPath}/resources/img/icons/fugue/printer.png"
												alt=""> Schedule a Meeting</a></li>
										<li class="divider"></li>
										<li><a href="#" class='fugue'><img
												src="${pageContext.request.contextPath}/resources/img/icons/fugue/gear.png"
												alt=""> Export Calendar</a></li>
										<li><a href="#" class='fugue'><img
												src="${pageContext.request.contextPath}/resources/img/icons/fugue/bin-metal.png"
												alt=""> Print Calendar</a></li>
									</ul></li>
								
								
							</ul>
						</div>
					</div>
					<div class="box-content box-nomargin">
						<div id='calendar'></div>
					</div>
				</div>
			</div>
			
            
            
		</div>
	</div>
</div>
