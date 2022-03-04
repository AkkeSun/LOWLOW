package church.lowlow.user_api.admin.notice.service;

import church.lowlow.rest_api.notice.db.Notice;

public interface AdminNoticeService {

    Notice getNotice(Long id);

}
