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


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
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

import com.archsystemsinc.ipms.persistence.service.IService;
import com.archsystemsinc.ipms.poi.service.DownloadService;
import com.archsystemsinc.ipms.sec.model.Issue;
import com.archsystemsinc.ipms.sec.model.Principal;
import com.archsystemsinc.ipms.sec.model.Program;
import com.archsystemsinc.ipms.sec.model.Project;
import com.archsystemsinc.ipms.sec.model.RevisionHistory;
import com.archsystemsinc.ipms.sec.model.Task;
import com.archsystemsinc.ipms.sec.model.TaskStatus;
import com.archsystemsinc.ipms.sec.persistence.service.IIssueService;
import com.archsystemsinc.ipms.sec.persistence.service.IMeetingService;
import com.archsystemsinc.ipms.sec.persistence.service.IPrincipalService;
import com.archsystemsinc.ipms.sec.persistence.service.IProgramService;
import com.archsystemsinc.ipms.sec.persistence.service.IProjectService;
import com.archsystemsinc.ipms.sec.persistence.service.IRevisionHistoryService;
import com.archsystemsinc.ipms.sec.persistence.service.ITaskService;
import com.archsystemsinc.ipms.sec.util.GenericConstants;
import com.archsystemsinc.ipms.web.common.AbstractController;


@Controller
public class TaskController extends AbstractController<Task> {

	public TaskController() {
		super(Task.class);
	}

	private final Log log = LogFactory.getLog(TaskController.class);

	@Autowired
	private ITaskService service;

	@Autowired
	private IProjectService projectService;

	@Autowired
	private IProgramService programService;

	@Autowired
	private IPrincipalService principalService;
	
	@Autowired
	private IMeetingService meetingService;
	
	@Autowired
	private DownloadService downloadService;
	
	@Autowired
	private IRevisionHistoryService revisionHistoryService;

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
	
	@RequestMapping(value = "/tasks/xls", method = RequestMethod.GET)
	public void getXLS(final HttpServletResponse response, final Model model,
			final java.security.Principal principal)
					throws ClassNotFoundException {
		logger.debug("Received request to download tasks report as an XLS");
		final String sheetName = GenericConstants.TASKS;
		final String[] coloumnNames = { "1", "2", "3", "4", "5", "6"};
		// Delegate to downloadService. Make sure to pass an instance of
		// HttpServletResponse
		final Principal currentUser = principalService.findByName(principal
				.getName());
		final List tasks = service.findCurrentUserTasks(currentUser);
		downloadService
				.downloadXLS(response, sheetName, tasks, coloumnNames);
	}

	@RequestMapping(value = "/tasks")
	public String task(final Model model) {
		final List<Task> tasks = service.findByMsProjectParentTaskIdIsNull();
		List<Task> tasksFinalList = new ArrayList<Task>();
		for(Task task: tasks) {
			if(!task.getStatus().equalsIgnoreCase("Closed") && !task.getStatus().equalsIgnoreCase("Resolved")) {
				tasksFinalList.add(task);
			}
		}
		model.addAttribute("tasks", tasksFinalList);
		return "tasks";
	}
	
	@RequestMapping(value = "/contactus")
	public String contactus(final Model model) {
		
		return "contactus";
	}


	@RequestMapping(value = "/task/{id}", method = RequestMethod.GET)
	public String task(@PathVariable("id") final Long id,
			final Model model) {
		final Task task = service.findOne(id);
		if(null != task.getAssignedTo())
			task.setAssignedToId(task.getAssignedTo().getId());
		if(null!=task.getCreatedBy())
			task.setCreatedById(task.getCreatedBy().getId());
		final List<RevisionHistory> revisionHist = revisionHistoryService.findByTask(id);
		task.setRevisions(new HashSet<RevisionHistory>(revisionHist));
		model.addAttribute("task", task);
		if(null!=task.getMsProjectTaskId()) {
			final List<Task> subTasks = service.findByMsProjectParentTaskId(task.getMsProjectTaskId());
			model.addAttribute("subTasks", subTasks);
		}
		model.addAttribute("referenceData", referenceData());
		return "task";
	}

	@RequestMapping(value = "/edit-task/{id}", method = RequestMethod.GET)
	public String editTask(@PathVariable("id") final Long id,
			final Model model) {
		final Task task = service.findOne(id);
		model.addAttribute("task", task);
		model.addAttribute("referenceData", referenceData());
		return "tasksedit";
	}
	
	private List<RevisionHistory> buildRevisionHistory(Task oldTask,
			Task task, Principal principal) {
		List<RevisionHistory> returnList = new ArrayList<RevisionHistory>();
		RevisionHistory revisionHistory = null;
		if (!oldTask.getDescription().equalsIgnoreCase(
				task.getDescription())){
			revisionHistory = new RevisionHistory();
			revisionHistory.setPrincipal(principal);
			revisionHistory.setPrincipalId(principal.getId());
			revisionHistory.setTaskId(task.getId());
			
			revisionHistory.setText("Priority String : " + oldTask.getDescription() + " - "
					+ oldTask.getDateCreated());
			revisionHistory.setTask(task);
			long time = (new Date()).getTime();
			
			revisionHistory.setUpdatedAt(new Timestamp(time));
			returnList.add(revisionHistory);
		}
		
		if(!oldTask.getStatus().equalsIgnoreCase(
				task.getStatus())){
			revisionHistory = new RevisionHistory();
			revisionHistory.setPrincipal(principal);
			revisionHistory.setPrincipalId(principal.getId());
			revisionHistory.setTaskId(task.getId());
			
			revisionHistory.setText("Risk Level : " + oldTask.getStatus() + " - "
					+ task.getStatus());
			revisionHistory.setTask(task);
			long time = (new Date()).getTime();
			
			revisionHistory.setUpdatedAt(new Timestamp(time));
			returnList.add(revisionHistory);
		}
		return returnList;
	}
	
	@RequestMapping(value = "/portfolio", method = RequestMethod.POST)
	public String portfolio(final Model model) {
		return "portfolio";
	}

	@RequestMapping(value = "/new-task", method = RequestMethod.POST)
	public String addTask(@Valid @ModelAttribute("task") final Task task,
			final BindingResult result, final Model model) {
		String returnView = "";
		final Principal assignedTo = principalService.findOne(task
				.getAssignedToId());

		final Principal createdBy = principalService.findOne(task
				.getCreatedById());

		if (task.getProgramId() != null) {
			final Program program = programService.findOne(task.getProgramId());
			returnView = "redirect:program/"+program.getId()+"?page=tasks&success=1";
			task.setProgram(program);
			
		}
		if (task.getProjectId() != null) {
			final Project project = projectService.findOne(task.getProjectId());
			task.setProject(project);
			returnView = "redirect:project/"+project.getId()+"?page=tasks&success=1";
		}
		task.setAssignedTo(assignedTo);
		task.setCreatedBy(createdBy);

		if (result.hasErrors()) {
			returnView = "tasksadd";
			model.addAttribute("task", task);
			model.addAttribute("referenceData", referenceData());
		} else {
			task.setDateCreated(new Date());
			service.create(task);
			model.addAttribute("success","success.task.created");
			returnView = "forward:tasks";
		}		
		return returnView;
	}

	@RequestMapping(value = "/new-task", method = RequestMethod.GET)
	public String newTask(final Model model,
			final java.security.Principal principal) {
		final Task task = new Task();
		final com.archsystemsinc.ipms.sec.model.Principal currentUser = principalService
				.findByName(principal.getName());
		task.setCreatedBy(currentUser);
		task.setCreatedById(currentUser.getId());
		model.addAttribute("success","success.task.created");
		model.addAttribute("task", task);
		model.addAttribute("referenceData", referenceData());
		return "tasksadd";
	}

	
	@RequestMapping(value = "/new-programtask/{id}", method = RequestMethod.GET)
	public String newProgramTask(@PathVariable("id") final Long id,final Model model,
			final java.security.Principal principal) {
		final Task task = new Task();
		Program program = programService.findOne(id);
		final com.archsystemsinc.ipms.sec.model.Principal currentUser = principalService
				.findByName(principal.getName());
		task.setCreatedBy(currentUser);
		task.setCreatedById(currentUser.getId());
	    task.setProgram(program);
	    task.setProgramId(program.getId());
		model.addAttribute("success","success.task.created");
		model.addAttribute("task", task);
		model.addAttribute("referenceData", referenceData());
		return "tasksadd";
	}

	@RequestMapping(value = "/new-projecttask/{id}", method = RequestMethod.GET)
	public String newTask1(@PathVariable("id") final Long id,
			final Model model, final java.security.Principal principal) {
		final Task task = new Task();
		final Project project = projectService.findOne(id);
		final com.archsystemsinc.ipms.sec.model.Principal currentUser = principalService
				.findByName(principal.getName());
		task.setProject(project);
		task.setProjectId(id);
		task.setCreatedBy(currentUser);
		task.setCreatedById(currentUser.getId());
		model.addAttribute("task", task);
		model.addAttribute("referenceData", referenceData());
		return "tasksadd";
	}

	@RequestMapping(value = "/edit-task", method = RequestMethod.POST)
	public String updateTask(@Valid @ModelAttribute("task") final Task task,
			final BindingResult result, final Model model) {
		String returnView = "";
		// using name as long --bad idea
		final Principal assignedTo = principalService.findOne(task
				.getAssignedToId());
		final Principal createdBy = principalService.findOne(task
				.getCreatedById());
		if (task.getProgramId() != null) {

			final Program program = programService.findOne(task.getProgramId());
			final Project project = projectService.findOne(task.getProgramId());

			task.setProgram(program);
			task.setProject(project);
		}
		task.setAssignedTo(assignedTo);
		task.setCreatedBy(createdBy);

		if (result.hasErrors()) {
			returnView = "tasksedit";
			model.addAttribute("task", task);
			model.addAttribute("referenceData", referenceData());
		} else {
			final String currentUser = SecurityContextHolder.getContext()
					.getAuthentication().getName();
			Principal principal = principalService.findByName(currentUser);
			Task oldTask = service.findOne(task.getId());
			List<RevisionHistory> histList = buildRevisionHistory(oldTask,
														task, principal);
			service.update(task);
			if(null != histList && histList.size() > 0){
				revisionHistoryService.bulkCreate(histList);
			}
			model.addAttribute("success","success.task.updated");
			returnView = "redirect:tasks";
		}		
		model.addAttribute("task", task);
		model.addAttribute("referenceData", referenceData());
		return returnView;
	}

	protected Map referenceData() {
		final Map referenceData = new HashMap();

		final List<Principal> list = principalService.findAll();
		final Map<Integer, String> aList = new LinkedHashMap<Integer, String>();
		for (int i = 0; i < list.size(); i++) {
			aList.put(list.get(i).getId().intValue(), list.get(i).getName());
		}
		referenceData.put("assignedToList", aList);
		referenceData.put("createdByList", aList);

		final List<Project> listOfProjects = projectService.findAll();
		final Map<Integer, String> prList = new LinkedHashMap<Integer, String>();

		for (int i = 0; i < listOfProjects.size(); i++) {
			prList.put(listOfProjects.get(i).getId().intValue(), listOfProjects
					.get(i).getName());
		}
		referenceData.put("projectList", prList);

		final List<Program> listOfPrograms = programService.findAll();
		final Map<Integer, String> prgList = new LinkedHashMap<Integer, String>();
		for (int i = 0; i < listOfPrograms.size(); i++) {
			prgList.put(listOfPrograms.get(i).getId().intValue(),
					listOfPrograms.get(i).getName());
		}

		referenceData.put("programList", prgList);
		final Map<String, String> sList = new LinkedHashMap<String, String>();
		sList.put(TaskStatus.Closed.toString(), TaskStatus.Closed.toString());

		sList.put(TaskStatus.Open.toString(), TaskStatus.Open.toString());
		sList.put(TaskStatus.Pending.toString(), TaskStatus.Pending.toString());
		sList.put(TaskStatus.In_Progress.toString(),
				TaskStatus.In_Progress.toString());
		sList.put(TaskStatus.Resolved.toString(),
				TaskStatus.Resolved.toString());
		sList.put(TaskStatus.Reopened.toString(),
				TaskStatus.Reopened.toString());
		referenceData.put("statusList", sList);
		return referenceData;
	}
	
	@Override
	protected IService<Task> getService() {
		return service;
	}

}
