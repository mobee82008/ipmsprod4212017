package com.archsystemsinc.ipms.sec.persistence.service;

import com.archsystemsinc.ipms.persistence.service.IService;
import com.archsystemsinc.ipms.sec.model.Comment;

public interface ICommentService extends IService<Comment> {

	Comment findByName(final String name);

}
