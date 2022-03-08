package church.lowlow.user_api.batch.summerNote.listener;

import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;
import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

import java.util.Date;

public class SummerNoteParallel2Listener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        stepExecution.getExecutionContext().put("parallel2_RequestDate", new Date());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }



}
