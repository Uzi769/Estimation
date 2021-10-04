package ru.irlix.evaluation.validation;

import ru.irlix.evaluation.dto.request.TaskRequest;
import ru.irlix.evaluation.utils.constant.EntitiesIdConstants;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RepeatCountTaskValidator implements ConstraintValidator<ValidTask, TaskRequest> {

    @Override
    public boolean isValid(TaskRequest task, ConstraintValidatorContext context) {
        if (task.getRepeatCount() == null) {
            return true;
        }

        if (EntitiesIdConstants.FEATURE_ID.equals(task.getType()) && task.getRepeatCount() >= 0) {
            return true;
        }

        if (EntitiesIdConstants.TASK_ID.equals(task.getType()) && task.getRepeatCount() >= 1) {
            return true;
        }

        return false;
    }
}
