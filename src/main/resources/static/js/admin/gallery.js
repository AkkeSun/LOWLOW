
// ================= Gallery List Data Load  ====================
function galleryListLoad(nowPage){

    // param setting
    let type        = "get";
    let data        = "searchId="+$("#searchId").val()+"&searchData="+$("#searchData").val()+"&nowPage="+nowPage;
    let url         = "/api/galleries";
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let async       = false;
    let callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( (data) => {

        totalPages = 1;

        let appendData = "";
        if( data._embedded ) {

            totalPages = data.page.totalPages;
            let list = data._embedded.galleryList;

            list.forEach(function (data, index) {

                let createdDate = (data.createdDate).substring(0, 10);
                appendData += `
                               <tr id="appendItem">
                                    <td style="text-align: left;">
                                        ${index+1}
                                    </td>
                                    <td style="text-align: left;">
                                        <a href='/admin/galleries/${data.id}'>
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



// ================= gallery Update View Setting  ====================
function galleryUpdateViewSetting() {
    $("#preView").hide();
    $("#editView").show();
}

