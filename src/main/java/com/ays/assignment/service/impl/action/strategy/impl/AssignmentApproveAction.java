package com.ays.assignment.service.impl.action.strategy.impl;


import com.ays.assignment.model.entity.AssignmentEntity;
import com.ays.assignment.model.enums.AssignmentStatus;
import com.ays.assignment.service.impl.action.strategy.AssignmentAction;
import com.ays.user.model.enums.UserSupportStatus;

public class AssignmentApproveAction implements AssignmentAction {

    @Override
    public AssignmentEntity execute(AssignmentEntity assignmentEntity) {
        assignmentEntity.updateAssignmentStatus(AssignmentStatus.ASSIGNED);
        assignmentEntity.getUser().setSupportStatus(UserSupportStatus.BUSY);
        return assignmentEntity;
    }

}
