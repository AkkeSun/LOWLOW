// ================= security ====================
// Tab 컨트롤
function securityTabControl(nowTab){

    if( nowTab ) {
        if(nowTab == 'tab2'){
            $("#tab1").removeClass('active');
            $("#user").removeClass('active').addClass('fade');
            $("#tab2").removeClass('fade').addClass('active');
            $("#role").removeClass('fade').addClass('active');
        }
        if(nowTab == 'tab3'){
            $("#tab1").removeClass('active');
            $("#user").removeClass('active').addClass('fade');
            $("#tab3").removeClass('fade').addClass('active');
            $("#resource").removeClass('fade').addClass('active');
        }
    }
}

// security Paging 처리 
function securityPaging( accountPagingData, rolePagingData, resourcePagingData ){

    $("#accountPagination").twbsPagination('destroy');
    $("#accountPagination").twbsPagination({
        startPage:  accountPagingData.nowPage,
        totalPages: accountPagingData.totalPages,
        visiblePages: 5,
        prev:"Prev",
        next:"Next",
        first:'<span sria-hidden="true">«</span>',
        last:'<span sria-hidden="true">»</span>',
        initiateStartPageClick:false,
        onPageClick:function(event, page){
            location.href="/admin/security/pagingReload?dbName=account&num="+page;
        }
    });

    $("#rolePagination").twbsPagination('destroy');
    $("#rolePagination").twbsPagination({
        startPage:  rolePagingData.nowPage,
        totalPages: rolePagingData.totalPages,
        visiblePages: 5,
        prev:"Prev",
        next:"Next",
        first:'<span sria-hidden="true">«</span>',
        last:'<span sria-hidden="true">»</span>',
        initiateStartPageClick:false,
        onPageClick:function(event, page){
            location.href="/admin/security/pagingReload?dbName=role&num="+page;
        }
    });

    $("#resourcePagination").twbsPagination('destroy');
    $("#resourcePagination").twbsPagination({
        startPage:  resourcePagingData.nowPage,
        totalPages: resourcePagingData.totalPages,
        visiblePages: 5,
        prev:"Prev",
        next:"Next",
        first:'<span sria-hidden="true">«</span>',
        last:'<span sria-hidden="true">»</span>',
        initiateStartPageClick:false,
        onPageClick:function(event, page){
            location.href="/admin/security/pagingReload?dbName=resource&num="+page;
        }
    });
}



// Ajax 처리 함수
function ajaxComm(type, data, url, csrfHeader, csrfToken) {
    let callback =
        $.ajax({
            type       : type,
            url        : url,
            enctype    : 'multipart/form-data',
            data       : data,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Content-type", "application/json");
                xhr.setRequestHeader(csrfHeader, csrfToken)
            }
        });
    return callback;
}

// Ajax 콜백 처리 함수
function ajaxCallbackProcess(data, type, redirectUrl){

    let returnMsg = "";
    switch(type){
        case "post"    : returnMsg="성공적으로 등록되었습니다"; break;
        case "put"     : returnMsg="성공적으로 수정되었습니다"; break;
        case "delete"  : returnMsg="성공적으로 삭제되었습니다";
    }

    if(data != 'success')
        alert(data);
    else{
        alert(returnMsg);
        location.href = redirectUrl;
    }
}


// serializeArray() -> Json Object
function objToJson(formData){
    let data = formData;
    let obj = {};
    $.each(data, function(idx, ele){
        obj[ele.name] = ele.value;
    });
    return obj;
}

function securityFormDataSetting(database){
    switch (database){
        case "account" :
            return JSON.stringify(objToJson($("#accountFrm").serializeArray()));
        case "role"     :
            return JSON.stringify(objToJson($('#roleFrm').serializeArray()));
        case "resource" :
            return JSON.stringify(objToJson($('#resourceFrm').serializeArray()));
    }
}

function securityRedirectUrlSetting(database){
    switch (database){
        case "account" :
            return "/admin/security"
        case "role"     :
            return  "/admin/security?tab=2"
        case "resource" :
            return "/admin/security?tab=3";
    }
}

function securityCreateFunc(database){

    let type        = "post";
    let url         = "/admin/security/"+database;
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let redirectUrl = securityRedirectUrlSetting(database);
    let data        = securityFormDataSetting(database);

    let callback = ajaxComm("post", data, url, csrfHeader, csrfToken);
    callback.done( data => ajaxCallbackProcess(data, type, redirectUrl) );
}

function securityUpdateFunc(database){

    let type        = "put";
    let url         = "/admin/security/"+ database +"/" + $("#id").val();
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let redirectUrl = securityRedirectUrlSetting(database);
    let data        = securityFormDataSetting(database);

    let callback = ajaxComm(type, data, url, csrfHeader, csrfToken);
    callback.done( data => ajaxCallbackProcess(data, type, redirectUrl) );
}

function securityDeleteFunc(database){

    let check = confirm('정말 삭제하시겠습니까');
    if(check) {
        let type        = "delete";
        let url         = "/admin/security/"+ database +"/" + $("#id").val();
        let csrfHeader  = $("#_csrf_header").attr('content');
        let csrfToken   = $("#_csrf").attr('content');
        let redirectUrl = securityRedirectUrlSetting(database);

        let callback = ajaxComm(type, "", url, csrfHeader, csrfToken);
        callback.done( data => ajaxCallbackProcess(data, type, redirectUrl) );
    }
}










// 전역변수
let totalPages = "";

// 기본 페이징처리 함수
function pagingProcess(totalPages){

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





/**********************************
                MEMBER
 *********************************/
function churchOfficerChangeToKor(churchOfficer){

    switch( churchOfficer ){
        case "LAYMAN"              : return '평신도';
        case "DEACONESS", "DEACON" : return '집사';
        case "ORDAINED_DEACON"     : return '안수집사';
        case "SENIOR_DEACONESS"    : return '권사';
        case "ELDER"               : return '장로';
        case "JUNIOR_PASTOR"       : return '전도사';
        case "ASSISTANT_PASTOR"    : return '부목사';
        case "SENIOR_PASTOR"       : return '담임목사';
        case "WIFE"                : return '사모';
    };
}


function memberListLoad(nowPage){

    let type        = "get";
    let data         = "";
    let url         = "/api/members";
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let callback    = ajaxComm(type, data, url, csrfHeader, csrfToken);

    callback.done( (data) => {

        totalPages = data.page.totalPages;
        let list = data._embedded.memberList;
        let appendData = "";

        list.forEach(function (data, index) {

            let churchOfficer = churchOfficerChangeToKor(data.churchOfficer);

            appendData += `
                           <tr class="hover" id="appendItem">
                                <td style="text-align: left;">
                                    ${data.belong}
                                </td>
                                <td style="text-align: left;">
                                    <a href='/admin/security/account/ + ${data.id}'>
                                        ${data.name}
                                    </a>
                                </td>
                                <td style="text-align: left;">
                                    ${data.phoneNumber}
                                </td>
                                <td style="text-align: left;">
                                    ${data.birthYear}
                                </td>
                                <td style="text-align: left;">
                                    ${churchOfficer}
                                </td>
                                <td style="text-align: left;">
                                    ${data.regiDate}
                                </td>
                            </tr>
                         `
        });

        $("#appendItem").remove(); // 기존 데이터 삭제
        $("#appendPath").append(appendData);

    });
};



function memberCreate(){

    let fileNameList = fileUpload();
    alert(JSON.stringify(fileNameList));
};


function fileUpload(){

    if( $("#image").val() ) {

        let csrfHeader  = $("#_csrf_header").attr('content');
        let csrfToken   = $("#_csrf").attr('content');

        let callback =
            $.ajax({
                type       : "post",
                url        : "/fileUpload",
                enctype    : 'multipart/form-data',
                processData: false,
                contentType: false,
                data       :  new FormData($("#withFileUploadFrm")[0]),
                beforeSend : xhr  => { xhr.setRequestHeader(csrfHeader, csrfToken) },
                done       : data => { return data; }
            });
    }
};

