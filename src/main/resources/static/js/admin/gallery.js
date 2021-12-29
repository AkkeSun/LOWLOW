
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
                                        ${data.title}
                                    </td>
                                    <td style="text-align: left;">
                                        <a href='/admin/members/${data.id}'>
                                            ${data.writer.writer}
                                        </a>
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




// ================= gallery paging process ====================
function galleryPagingProcess(){

    let nowPage = 1;

    $("#basicPagination").twbsPagination('destroy');
    $("#basicPagination").twbsPagination({
        startPage:  nowPage,
        totalPages: totalPages,
        visiblePages: 5,
        prev:"Prev",
        next:"Next",
        first:'<span sria-hidden="true">«</span>',
        last:'<span sria-hidden="true">»</span>',
        initiateStartPageClick:false,
        onPageClick:function(event, page){
            nowPage = page;
            memberListLoad(nowPage-1);
        }
    });
}



// ================ 검색기능 ================
function gallerySearch(){
    galleryListLoad(0);
    galleryPagingProcess();
}


// ================ 검색기능 초기화================
function gallerySearchInitialize() {
    $("#searchId").val("title");
    $("#searchData").val("");;
    galleryListLoad(0);
    galleryPagingProcess();
}




// ================= Gallery Create & Update process  ====================
function galleryCreateAndUpdateProcess(type){

    // param setting
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let url         = "/api/galleries";
    let redirectUrl = "/admin/galleries";
    let async       = true;
    let isSecurity  = false;
    let data        = objToJson($('#galleryFrm').serializeArray());

    // update url 변경
    if(type == 'put')
        url = "/api/galleries/"+$("#id").val();

    // ajax process
    let callback = ajaxComm(type, JSON.stringify(data), url, async, csrfHeader, csrfToken);

    callback.done( data => ajaxCallbackProcess(isSecurity, data, type, redirectUrl) );

    callback.fail ( (xhr, status, error) => {

        let errorResource = JSON.parse(xhr.responseText).content[0];
        console.log("[ERROR STATUS] : " + xhr.status);
        console.log(errorResource);
        alert(errorResource.defaultMessage);
    });

};


