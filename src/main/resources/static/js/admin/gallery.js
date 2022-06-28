
// ================= Gallery List Data Load  ====================
function galleryListLoad(nowPage){

    // param setting
    var type        = "get";
    var data        = "searchId="+$("#searchId").val()+"&searchData="+$("#searchData").val()+"&nowPage="+nowPage;
    var url         = "/api/galleries";
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var async       = false;
    var callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( function (data) {

        totalPages = 1;

        var appendData = "";
        if( data._embedded ) {

            totalPages = data.page.totalPages;
            var list = data._embedded.galleryList;

            list.forEach(function (data, index) {

                var createdDate = (data.createdDate).substring(0, 10);
                appendData += `
                               <tr id="appendItem">
                                    <td style="text-align: left;">
                                        ${data.id}
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

// ================= gallery Detail Data ====================
function getGalleryDetail(id) {

    var type        = "get";
    var url         = "/api/galleries/"+id;
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var async       = true;
    var callback    = ajaxComm(type, null, url, async, csrfHeader, csrfToken);

    callback.done((data) => {
        $("#preViewTitle").append(data.title);
        $("#preViewContents").append(data.contents);
        $("#id").val(data.id);
        $("#title").val(data.title);
        $('#contents').summernote('code', data.contents);
    });
}



// ================= gallery Update View Setting  ====================
function galleryUpdateViewSetting() {
    $("#preView").addClass('hide')
    $("#deleteBtn").addClass('hide');
    $("#editView").removeClass('hide');
    $("#updateProcessBtn").removeClass('hide');
}

