<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<!-- Live chat demo Javascript 
<script src="https://code.jquery.com/jquery-1.10.2.js"></script>
<script src="https://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>-->
<!-- <script src="${pageContext.request.contextPath}/resources/js/external-source.js"></script>-->
<script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
<script src="http://code.jquery.com/ui/1.11.1/jquery-ui.min.js"></script>

<script type="text/javascript">

</script>

<script>




</script>
<style>
/* The Modal (background) */
.modal {
    display: none; /* Hidden by default */
    position: fixed; /* Stay in place */
    z-index: 1; /* Sit on top */
    padding-top: 100px; /* Location of the box */
    left: 0;
    top: 0;
    width: 100%; /* Full width */
    height: 100%; /* Full height */
    overflow: auto; /* Enable scroll if needed */
    background-color: rgb(0,0,0); /* Fallback color */
    background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
}

/* Modal Content */
.modal-content {
    position: relative;
    background-color: #fefefe;
    margin: auto;
    padding: 0;
    border: 1px solid #888;
    width: 80%;
    box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2),0 6px 20px 0 rgba(0,0,0,0.19);
    -webkit-animation-name: animatetop;
    -webkit-animation-duration: 0.4s;
    animation-name: animatetop;
    animation-duration: 0.4s
}

/* Add Animation */
@-webkit-keyframes animatetop {
    from {top:-300px; opacity:0} 
    to {top:0; opacity:1}
}

@keyframes animatetop {
    from {top:-300px; opacity:0}
    to {top:0; opacity:1}
}

/* The Close Button */
.close {
    color: white;
    float: right;
    font-size: 28px;
    font-weight: bold;
}

.close:hover,
.close:focus {
    color: #000;
    text-decoration: none;
    cursor: pointer;
}

.modal-header {
    padding: 2px 16px;
    background-color: #e9e7e9;
    color: white;
}

.modal-body {padding: 2px 16px;}

.modal-footer {
    padding: 2px 16px;
    background-color: #e9e7e9;
    color: white;
}
</style>

<div id="left_bar">
	<div id="primary_nav" class="g_blue">
		<ul>
			<li><a href="#" title="Dashboard"><span
					class="icon_block m_dashboard">Dashboard</span></a></li>
			<li><a href="#" title="Projects"><span
					class="icon_block m_projects">Projects</span></a></li>
			<li><a href="#" title="Events"><span
					class="icon_block m_events">Events</span></a></li>
			<li><a href="#" title="Address Book"><span
					class="icon_block p_book">Address Book</span></a></li>
			<li><a href="#" title="Media"><span
					class="icon_block m_media">Media</span></a></li>
			<li><a href="#" title="Settings"><span
					class="icon_block m_settings">Settings</span></a></li>
		</ul>
	</div>
	<div id="start_menu">
		<ul>
			<li class="jtop_menu">
				<div class="icon_block black_gel">
					<span class="start_icon">Quick Menu</span>
				</div>
				<ul class="black_gel">
					<li><a
						href="${pageContext.request.contextPath}/app/projectreport"><span
							class="list-icon graph_b">&nbsp;</span>Analytics<span
							class="mnu_tline">Tagline</span></a></li>
					<li><a href="#"><span class="list-icon cog_4_b">&nbsp;</span>Settings<span
							class="mnu_tline">Tagline</span></a></li>
					<li><a href="#"><span class="list-icon vault_b">&nbsp;</span>The
							Archive<span class="mnu_tline">Tagline</span></a></li>
					<li><a href="#"><span class="list-icon list_images_b">&nbsp;</span>Task
							List<span class="mnu_tline">Tagline</span></a></li>
					<li><a href="#"><span class="list-icon documents_b">&nbsp;</span>Content
							List<span class="mnu_tline">Tagline</span></a></li>
					<li><a href="#"><span class="list-icon folder_b">&nbsp;</span>Media<span
							class="mnu_tline">Tagline</span></a></li>
					<li><a href="#"><span class="list-icon phone_3_b">&nbsp;</span>Contact<span
							class="mnu_tline">Tagline</span></a></li>
					<li><a href="#"><span class="list-icon users_b">&nbsp;</span>User<span
							class="mnu_tline">Tagline</span></a>
						<ul>
							<li><a href="#"><span class="list-icon user_2_b">&nbsp;</span>Add
									New User<span class="mnu_tline">Tagline</span></a></li>
							<li><a href="#"><span class="list-icon money_b">&nbsp;</span>Paid
									Users<span class="mnu_tline">Tagline</span></a></li>
							<li><a href="#"><span class="list-icon users_2_b">&nbsp;</span>All
									Users<span class="mnu_tline">Tagline</span></a></li>
						</ul></li>
				</ul>
			</li>
		</ul>
	</div>
	<div id="sidebar">
		<div id="secondary_nav" id="nav">
			<ul id="sidenav" class="accordion_mnu collapsible">
				<li><a href="#" title="Dashboard"><span
						class="nav_icon computer_imac"></span> Dashboards<span
						class="up_down_arrow">&nbsp;</span></a>

					<ul class="acitem">
						<li><a
							href="${pageContext.request.contextPath}/app/groupdashboard"><span
								class="list-icon">&nbsp;</span>Group Dashboard</a></li>
						<li><a
							href="${pageContext.request.contextPath}/app/groupdashboard/business"><span
								class="list-icon">&nbsp;</span>Vertical Group Dashboard</a></li>
						<li><a
							href="${pageContext.request.contextPath}/app/projectdashboard/22"><span
								class="list-icon">&nbsp;</span>Model Dashboard</a></li>
					</ul></li>

				<li><a href="#"><span class="nav_icon frames"></span>
						Vertical Group Manage<span class="up_down_arrow">&nbsp;</span></a>
					<ul class="acitem">
						<li><a href="${pageContext.request.contextPath}/app/programs"><span
								class="list-icon">&nbsp;</span>View Group</a></li>
						<li><a
							href="${pageContext.request.contextPath}/app/new-program"><span
								class="list-icon">&nbsp;</span>Add New Group</a></li>
					</ul></li>
				<li><a href="#" title="Manage Projects"><span
						class="nav_icon frames"></span> Model Management<span
						class="up_down_arrow">&nbsp;</span></a>
					<ul class="acitem">
						<li><a href="${pageContext.request.contextPath}/app/projects"><span
								class="list-icon">&nbsp;</span>View Models</a></li>
						<li><a href="${pageContext.request.contextPath}/app/risks"><span
								class="list-icon">&nbsp;</span>Risk Management</a></li>
						<li><a href="${pageContext.request.contextPath}/app/tasks"><span
								class="list-icon">&nbsp;</span>Task Management</a></li>
						<li><a href="${pageContext.request.contextPath}/app/meetings"><span
								class="list-icon">&nbsp;</span>Meetings Management</a></li>
						<li><a
							href="${pageContext.request.contextPath}/app/evmproject"><span
								class="list-icon">&nbsp;</span>Financial Management</a></li>
						<li><a href="${pageContext.request.contextPath}/app/upload"><span
								class="list-icon">&nbsp;</span>Scheduling Management</a></li>
						<li><a
							href="${pageContext.request.contextPath}/app/new-project"><span
								class="list-icon">&nbsp;</span>Add New Model</a></li>
						
					</ul>
				</li>
				
					<!--<a href="#"><span class="nav_icon frames"></span>Application
						Management<span class="up_down_arrow">&nbsp;</span></a>  
					<ul class="acitem">
						<li><a
							href="${pageContext.request.contextPath}/app/portfolio"><span
								class="list-icon">&nbsp;</span>Portfolio</a></li>
					</ul></li>--> <sec:authorize
						access="hasRole('ROLE_PROJECT_MANAGER') or hasRole('ROLE_PROGRAM_MANAGER') or hasRole('AdminOfSecurityService') or hasRole('ROLE_USER') ">
						<li><a
							href="${pageContext.request.contextPath}/app/artifacts"><span
								class="nav_icon blocks_images"></span> Artifacts Manager</a></li>
					</sec:authorize> <!--
						
						<li><a href="#"><span class="nav_icon list_images"></span>Systems Configuration
							<span class="up_down_arrow">&nbsp;</span></a>
						<ul class="acitem">
							<li><a
								href="${pageContext.request.contextPath}/app/new-requirement"><span
									class="list-icon">&nbsp;</span>Add New System</a></li>
							<li><a
								href="${pageContext.request.contextPath}/app/requirements"
								><span class="list-icon">&nbsp;</span>View
									Systems</a></li>
						</ul></li>
				<li><a href="#" title="Manage Meetings"><span
						class="nav_icon coverflow"></span>Meetings Manager<span
						class="up_down_arrow">&nbsp;</span></a>
					<ul class="acitem">
						<li><a href="${pageContext.request.contextPath}/app/meetings"><span
								class="list-icon">&nbsp;</span>View Meetings</a></li>					
					</ul></li> -->
				<li><a href="#"><span class="nav_icon documents"></span>Reports<span
						class="up_down_arrow">&nbsp;</span></a>
					<ul class="acitem">
						<li><a
							href="${pageContext.request.contextPath}/app/groupreport"><span
								class="list-icon">&nbsp;</span>Group Report</a></li>
						<li><a
							href="${pageContext.request.contextPath}/app/programreport"><span
								class="list-icon">&nbsp;</span>Program Report</a></li>
						<li><a
							href="${pageContext.request.contextPath}/app/projectreport"><span
								class="list-icon">&nbsp;</span>Project Report</a></li>

						<!-- <li><a
								href="${pageContext.request.contextPath}/app/statusreport"><span
									class="list-icon">&nbsp;</span>Status Report</a></li>-->
					</ul></li>

				<li><a href="#"><span class="nav_icon frames"></span>Stakeholder
						Administration<span class="up_down_arrow">&nbsp;</span></a>
					<ul class="acitem">
						<li><a
							href="${pageContext.request.contextPath}/app/principals"><span
								class="list-icon">&nbsp;</span>Users</a></li>
						<li><a href="${pageContext.request.contextPath}/app/roles"><span
								class="list-icon">&nbsp;</span>Roles</a></li>
						<li><a
							href="${pageContext.request.contextPath}/app/privileges"><span
								class="list-icon">&nbsp;</span>Privileges</a></li>
					</ul></li>	
					
			</ul>

           
            
			<!-- <div> -->
			<div id="left_bar">
				<div class="grid_2">
					<div class="widget_wrap" style="width: 200px;">
						<div class="widget_top">
							<span class="h_icon list"></span>
							<h6>Alerts</h6>
						</div>
						<div class="widget_content">

							<h3></h3>
							<p></p>

						</div>

					</div>
				</div>

				<div></div>
				<div id="left_bar">
					<!--<div class="social_activities">
							<div class="activities_s"
								style="height: 68px; width: 150px; padding: 10px; border-width: 5px; top: -5px; margin: -5px;">
								<div class="block_label">
									User Activities<span>54854</span>
								</div>
								<div class="activities_chart">
									<span class="activities_chart">100,150,130,100,250,280,350,250,400,450,280,350,250,400,</span>
								</div>
							</div>
						</div>  -->

					<!--<div class="activities_s"
							style="height: 68px; width: 150px; padding: 10px; border-width: 5px; top: 5px; margin: 5px;">
							<div class="block_label">
								Visits Rate<span>80%</span>
							</div>
							<div class="visit_chart"
								style="height: 68px; width: 150px; padding: 10px; border-width: 5px; top: 5px; margin: 5px;">
								<span class="activities_chart">500,450,100,500,550,
									400,300,550,480,500,320,400,450</span>
							</div>
						</div>  -->


					<!--<div class="comments_s"
							style="height: 68px; width: 150px; padding: 10px; border-width: 5px; top: 5px; margin: 5px;">
							<div class="block_label">
								Comments<span>17000</span>
							</div>
							<span class="badge_icon comment_sl"></span>
						</div>  -->
					<!--<div class="views_s"
							style="height: 68px; width: 150px; padding: 10px; border-width: 5px; top: 5px; margin: 5px;">
							<div class="block_label">
								Paid Members<span>1500</span>
							</div>
							<span class="badge_icon bank_sl"></span>
						</div>  -->
					<!-- <div class="user_s">
        <div class="block_label">
            New User's<span>12000</span>
        </div>
        <span class="badge_icon customers_sl"></span>
    </div> -->

					<!--<div class="user_s"
							style="height: 68px; width: 150px; padding: 10px; border-width: 5px; top: 5px; margin: 5px;">
							<div class="block_label">
								New User's<span>12000</span>
							</div>
							<span class="badge_icon customers_sl"></span>
						</div>  -->
				</div>
			</div>

		</div>
	</div>
</div>



</div>
