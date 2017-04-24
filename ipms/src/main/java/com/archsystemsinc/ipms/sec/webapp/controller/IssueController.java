/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.archsystemsinc.ipms.sec.webapp.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.archsystemsinc.ipms.persistence.service.IService;
import com.archsystemsinc.ipms.poi.service.DownloadService;
import com.archsystemsinc.ipms.sec.model.ActionItemPriority;
import com.archsystemsinc.ipms.sec.model.Issue;
import com.archsystemsinc.ipms.sec.model.IssueStatus;
import com.archsystemsinc.ipms.sec.model.Principal;
import com.archsystemsinc.ipms.sec.model.Project;
import com.archsystemsinc.ipms.sec.model.RevisionHistory;
import com.archsystemsinc.ipms.sec.persistence.service.IIssueService;
import com.archsystemsinc.ipms.sec.persistence.service.IPrincipalService;
import com.archsystemsinc.ipms.sec.persistence.service.IProjectService;
import com.archsystemsinc.ipms.sec.persistence.service.IRevisionHistoryService;
import com.archsystemsinc.ipms.sec.util.GenericConstants;
import com.archsystemsinc.ipms.web.common.AbstractController;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Controller
public class IssueController extends AbstractController<Issue> {

	public IssueController() {
		super(Issue.class);
	}

	private final Log log = LogFactory.getLog(IssueController.class);

	@Autowired
	private IIssueService service;

	@Autowired
	private IProjectService projectService;
	
	@Autowired
	private IRevisionHistoryService revisionHistoryService;

	@Autowired
	private IPrincipalService principalService;

	@Autowired
	private DownloadService downloadService;

	@Override
	@InitBinder
	public void initBinder(final WebDataBinder binder) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(
				GenericConstants.DEFAULT_DATE_FORMAT);
		dateFormat.setLenient(false);
		// true passed to CustomDateEditor constructor means convert empty
		// String to null
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

	
	/**
	 * Downloads the report as an Excel format.
	 * <p>
	 * Make sure this method doesn't return any model. Otherwise, you'll get an
	 * "IllegalStateException: getOutputStream() has already been called for this response"
	 * final HttpServletResponse response, final String sheetName, final
	 * List<Object> exportList, final String[] coloumnNames
	 */
	@RequestMapping(value = "/issues/xls", method = RequestMethod.GET)
	public void getXLS(final HttpServletResponse response, final Model model,
			final java.security.Principal principal)
			throws ClassNotFoundException {
		logger.debug("Received request to download issues report as an XLS");
		final String sheetName = GenericConstants.ISSUES;
		final String[] coloumnNames = { "1", "2", "3", "4", "5", "6" };
		// Delegate to downloadService. Make sure to pass an instance of
		// HttpServletResponse
		final Principal currentUser = principalService.findByName(principal
				.getName());
		final List issues = service.findCurrentUserIssues(currentUser);
		downloadService.downloadXLS(response, sheetName, issues, coloumnNames);
		
	}

	@RequestMapping(value = "/issues", method = RequestMethod.GET)
	public String issue(final Model model, final java.security.Principal principal) {
		final Principal currentUser = principalService.findByName(principal
				.getName());
		final List<Issue> issues = service.findCurrentUserIssues(currentUser);
		model.addAttribute("issues", issues);
		return "issues";
	}

	@RequestMapping(value = "/issue/{id}", method = RequestMethod.GET)
	public String issue(@PathVariable("id") final Long id, final Model model) {
		final Issue issue = service.findOne(id);
		issue.setAssignedId(issue.getAssigned().getId());
		issue.setAssigneeId(issue.getAssignee().getId());
		final List<RevisionHistory> revisionHist = revisionHistoryService
				.findByIssue(id);
		issue.setRevisions(new HashSet<RevisionHistory>(revisionHist));
		model.addAttribute("issue", issue);
		model.addAttribute("referenceData", referenceData());
		return "issue";
	}

	@RequestMapping(value = "/new-issue", method = RequestMethod.GET)
	public String newIssue(final Model model) {
		final Issue issue = new Issue();
		model.addAttribute("issue", issue);
		model.addAttribute("referenceData", referenceData());
		return "issuesadd";
	}

	@RequestMapping(value = "/new-issue/{id}", method = RequestMethod.GET)
	public String newIssue1(@PathVariable("id") final Long id, final Model model) {
		final Issue issue = new Issue();
		Project project = null;
		try {
			project = projectService.findOne(id);
			issue.setProject(project);
			issue.setProjectId(project.getId());
		} catch (final Exception e) {
		}

		model.addAttribute("issue", issue);
		model.addAttribute("referenceData", referenceData());
		return "issuesadd";
	}
	
	public static HttpSession getSession() {
		final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
		return attr.getRequest().getSession(true);
	}

	@RequestMapping(value = "/edit-issue/{id}", method = RequestMethod.GET)
	public String editIssue(@PathVariable("id") final Long id, final Model model) {
		final Issue issue = service.findOne(id);
		model.addAttribute("issue", issue);
		model.addAttribute("referenceData", referenceData());
		
		final HttpSession session = getSession();
		session.setAttribute("issue", issue);
		return "issuesedit";
	}
	
	private List<RevisionHistory> buildRevisionHistory(Issue oldIssue,
			Issue issue, Principal principal) {
		List<RevisionHistory> returnList = new ArrayList<RevisionHistory>();
		RevisionHistory revisionHistory = null;
		if (!oldIssue.getPriority().equalsIgnoreCase(
				issue.getPriority())){
			revisionHistory = new RevisionHistory();
			revisionHistory.setPrincipal(principal);
			revisionHistory.setPrincipalId(principal.getId());
			revisionHistory.setIssueId(issue.getId());
			
			revisionHistory.setText("Priority String : " + oldIssue.getPriority() + " - "
					+ oldIssue.getPriority());
			revisionHistory.setIssue(issue);
			returnList.add(revisionHistory);
		}
		
		if(!oldIssue.getStatus().equalsIgnoreCase(
				issue.getStatus())){
			revisionHistory = new RevisionHistory();
			revisionHistory.setPrincipal(principal);
			revisionHistory.setPrincipalId(principal.getId());
			revisionHistory.setIssueId(issue.getId());
			revisionHistory.setText("Risk Level : " + oldIssue.getStatus() + " - "
					+ issue.getStatus());
			revisionHistory.setIssue(issue);
			returnList.add(revisionHistory);
		}
		return returnList;
	}

	@RequestMapping(value = "/new-issue", method = RequestMethod.POST)
	public String addIssue(@Valid @ModelAttribute("issue") final Issue issue,
			final BindingResult result, final Model model) {
		String returnView = "";
		final Project project = projectService.findOne(issue.getProjectId());
		final Principal assignee = principalService.findOne(issue
				.getAssigneeId());
		final Principal assigned = principalService.findOne(issue
				.getAssignedId());
		issue.setProject(project);
		issue.setAssigned(assigned);
		issue.setAssignee(assignee);
		if (result.hasErrors()) {
			returnView = "issuesadd";
			model.addAttribute("issue", issue);
			model.addAttribute("referenceData", referenceData());
		} else {
			service.create(issue);
			model.addAttribute("success", "success.issue.created");
			returnView = "redirect:project/" + project.getId()
					+ "?page=issues&success=2";
		}
		return returnView;
	}

	@RequestMapping(value = "/edit-issue", method = RequestMethod.POST)
	public String updateIssue(
			@Valid @ModelAttribute("issue") final Issue issue,
			final BindingResult result, final Model model) {
		String returnView = "";
		// using name as long --bad idea
		final Project project = projectService.findOne(issue.getProjectId());
		final Principal assignee = principalService.findOne(issue
				.getAssigneeId());
		final Principal assigned = principalService.findOne(issue
				.getAssignedId());
		issue.setProject(project);
		issue.setAssigned(assigned);
		issue.setAssignee(assignee);
		if (result.hasErrors()) {
			returnView = "issuesedit";
			model.addAttribute("issue", issue);
			model.addAttribute("referenceData", referenceData());
		} else {
			final String currentUser = SecurityContextHolder.getContext()
					.getAuthentication().getName();
			Principal principal = principalService.findByName(currentUser);
			Issue oldIssue = service.findOne(issue.getId());
			List<RevisionHistory> histList = buildRevisionHistory(oldIssue,
														issue, principal);
			service.update(issue);
			if(null != histList && histList.size() > 0){
				revisionHistoryService.bulkCreate(histList);
			}
			model.addAttribute("success", "success.issue.updated");
			returnView = "redirect:issues";
		}
		model.addAttribute("issue", issue);
		model.addAttribute("referenceData", referenceData());
		return returnView;
	}
	
	@RequestMapping(value = "/delete-issue/{id}/{returnPage}", method = RequestMethod.GET)
	public String deleteIssue(@PathVariable("id") final Long id,
			@PathVariable("returnPage") final String returnPage,
			final Model model) {
		String returnView = "";
		service.delete(id);
		model.addAttribute("success", "success.issue.deleted");
		if (returnPage.equalsIgnoreCase("")) {
			returnView = "forward:issues";
		} else {
			returnView = "forward:" + returnPage;
		}
		return returnView;
	}

	protected Map referenceData() {
		final Map referenceData = new HashMap();
		final List<Principal> list = principalService.findAll();
		final Map<Integer, String> aList = new LinkedHashMap<Integer, String>();
		for (int i = 0; i < list.size(); i++) {
			aList.put(list.get(i).getId().intValue(), list.get(i).getName());
		}
		referenceData.put("assignList", aList);

		final List<Project> projectlist = projectService.findActiveProjects();
		final Map<Integer, String> pList = new LinkedHashMap<Integer, String>();
		for (int i = 0; i < projectlist.size(); i++) {
			pList.put(projectlist.get(i).getId().intValue(), projectlist.get(i)
					.getName());
		}
		referenceData.put("projectList", pList);

		final Map<String, String> priorityList = new LinkedHashMap<String, String>();
		priorityList.put(ActionItemPriority.High.toString(),
				ActionItemPriority.High.toString());
		priorityList.put(ActionItemPriority.Medium.toString(),
				ActionItemPriority.Medium.toString());
		priorityList.put(ActionItemPriority.Low.toString(),
				ActionItemPriority.Low.toString());
		referenceData.put("priorityList", priorityList);

		final Map<String, String> sList = new LinkedHashMap<String, String>();
		sList.put(IssueStatus.Closed.toString(), IssueStatus.Closed.toString());
		sList.put(IssueStatus.Open.toString(), IssueStatus.Open.toString());
		sList.put(IssueStatus.Pending.toString(),
				IssueStatus.Pending.toString());
		sList.put(IssueStatus.In_Progress.toString(),
				IssueStatus.In_Progress.toString());
		sList.put(IssueStatus.Reopened.toString(),
				IssueStatus.Reopened.toString());
		sList.put(IssueStatus.Resolved.toString(),
				IssueStatus.Resolved.toString());
		referenceData.put("statusList", sList);

		return referenceData;
	}
	

	@Override
	protected IService<Issue> getService() {
		return service;
	}
	
	  @RequestMapping(value = "/issues/pdf", method = RequestMethod.GET)
	  public void getPDF1(final HttpServletResponse response, final Model model,
				final java.security.Principal principal)
						throws DocumentException,IOException,ClassNotFoundException {
		  logger.debug("Received request to download issues report as an PDF");
		  
		  try{
		  	Document document = new Document(PageSize.A4,50,50,50,50);
		  	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("C:\\Test.pdf"));
		  	document.open();
		  	final String sheetName = GenericConstants.ISSUES;
		  	final String[] coloumnNames = { "1", "2", "3", "4", "5", "6" };
		  	final Principal currentUser = principalService.findByName(principal
					.getName());
			final List issues = service.findCurrentUserIssues(currentUser);
			
		      
			
			document.add(new Paragraph(GenericConstants.ISSUES));
			PdfPTable t = new PdfPTable(3);

		      t.setSpacingBefore(25);
		      
		      t.setSpacingAfter(25);
		      for(int i=0;i< issues.size();i++){
		    	  Object isu =issues.get(i);
		    	 
		      }
		      
		
		      
		      t.addCell("1.1");
		      
		      t.addCell("1.2");
		      
		      t.addCell("1.3");
		      
		      document.add(t);
		  	  document.close();
		  
		  } catch (Exception e) {
		      e.printStackTrace();
		    }

	  }

}
