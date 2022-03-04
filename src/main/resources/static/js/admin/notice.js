
// ================= Notice List Data Load  ====================
function noticeListLoad(nowPage){

    // param setting
    var type        = "get";
    var data        = "searchId="+$("#searchId").val()+"&searchData="+$("#searchData").val()+"&nowPage="+nowPage;
    var url         = "/api/notices";
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var async       = false;
    var callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( function (data) {

        totalPages = 1;

        var appendData = "";
        if( data._embedded ) {

            totalPages = data.page.totalPages;
            var list = data._embedded.noticeList;

            list.forEach(function (data, index) {

                var createdDate = (data.createdDate).substring(0, 10);
                appendData += `
                               <tr id="appendItem">
                                    <td style="text-align: left;">
                                        ${index+1}
                                    </td>
                                    <td style="text-align: left;">
                                        <a href='/admin/notices/${data.id}'>
                                            ${data.title}
                                        </a>
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.writer.writer}
                                    </td>
                                    <td style="text-align: left;">
                                        ${createdDate}
                                    </td>
                                </tr>
                                `
            });
        }

        $("tr").remove("#appendItem");       // 기존 데이터 삭제
        $("#appendPath").append(appendData); // data append

    });
};



// ================= Notice Update View Setting  ====================
function noticeUpdateViewSetting() {
    $("#preView").hide();
    $("#editView").show();
}

