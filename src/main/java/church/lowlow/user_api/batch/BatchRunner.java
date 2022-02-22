package church.lowlow.user_api.batch;


import church.lowlow.user_api.batch.summerNote.service.SummerNoteService;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
public class BatchRunner {

    @Autowired
    private JobOperator jobOperator;

    @Autowired
    private SummerNoteService service;

    @GetMapping("/test")
    public String job1Start() throws JobInstanceAlreadyExistsException, NoSuchJobException, JobParametersInvalidException {

        jobOperator.start("SummerNoteJob", "requestDate=" + new Date());
        return "BATCH START";
    }

    @GetMapping("/test2")
    public String test()  {
        System.out.println("in");
        service.getGalleryList();
        return "BATCH START";
    }

}