package church.lowlow.user_api.batch.summerNote.chunk;

import church.lowlow.user_api.batch.summerNote.domain.SummerNoteVo;
import church.lowlow.user_api.batch.summerNote.singleton.SummerNoteSingleton;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NoticeWriter implements ItemWriter<SummerNoteVo> {

    @Override
    public void write(List<? extends SummerNoteVo> list) throws Exception {


        List<String> returnList = list.stream()
                                  .map(data -> data.getUploadName())
                                  .collect(Collectors.toList());

        // singleton save
        SummerNoteSingleton instance = SummerNoteSingleton.getInstance();
        instance.setNoticeContentList(returnList);

    }
}
