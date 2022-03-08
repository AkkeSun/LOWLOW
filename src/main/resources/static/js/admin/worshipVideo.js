

// ================= worshipVideoList Data Load  ====================
function worshipVideoListLoad(nowPage){

    // param setting
    var type        = "get";
    var data        = "searchId="+$("#searchId").val()+"&searchData="+$("#searchData").val()+"&nowPage="+nowPage;
    var url         = "/api/worshipVideos";
    var csrfHeader  = $("#_csrf_header").attr('content');
    var csrfToken   = $("#_csrf").attr('content');
    var async       = false;
    var callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( (data) => {

        totalPages = 1;

        var appendData = "";
        if( data._embedded ) {
            totalPages = data.page.totalPages;

            var list = data._embedded.worshipVideoList;
            list.forEach(function (data, index) {

                var modifiedDate = modifiedDateConverter(data.modifiedDate);
                appendData += `
                               <tr id="appendItem">
                                    <td style="text-align: left;">
                                        ${index+1}
                                    </td>
                                    <td style="text-align: left;">
                                        <a href='/admin/worshipVideos/${data.id}'>
                                            ${data.title}
                                        </a>
                                    </td>
                                    <td style="text-align: left;">
                                        ${modifiedDate}
                                    </td>
                                </tr>
                                `
            });
        }

        $("tr").remove("#appendItem");       // 기존 데이터 삭제
        $("#appendPath").append(appendData); // data append

    });
};





// ================= WorshipVideo Update View Setting ====================
function worshipVideoUpdateViewSetting() {
    $("#title").removeAttr("disabled");
    $("#videoUpdateView").show();
    $("#videoPreView").hide();
};



//================= youtube link converter ==========================
function youtubeLinkConverter(link) {
    var embedLink = link.replace('watch?v=', 'embed/');
    return embedLink;
};

//================= modifiedDate converter ==========================
function modifiedDateConverter(dateString){
    return dateString.substr(0,10);
}