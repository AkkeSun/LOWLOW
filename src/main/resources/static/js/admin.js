/**********************************
                COMMON
 *********************************/

// ================= Ajax 처리 함수 ====================
function ajaxComm(type, data, url, async, csrfHeader, csrfToken) {
    let callback =
        $.ajax({
            type       : type,
            url        : url,
            enctype    : 'multipart/form-data',
            data       : data,
            async      : async,
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Content-type", "application/json");
                xhr.setRequestHeader(csrfHeader, csrfToken)
            }
        });
    return callback;
}




// ================= Ajax 콜백 처리 함수 ====================
function ajaxCallbackProcess(isSecurity, data, type, redirectUrl){

    let returnMsg = "";

    switch(type)
    {
        case "post"    : returnMsg="성공적으로 등록되었습니다"; break;
        case "put"     : returnMsg="성공적으로 수정되었습니다"; break;
        case "delete"  : returnMsg="성공적으로 삭제되었습니다";
    }

    if(isSecurity)
    {
        if(data != 'success')
            alert(data);
        else{
            alert(returnMsg);
            location.href = redirectUrl;
        }
    }
    else
    {
        alert(returnMsg);
        location.href = redirectUrl;
    }
}




// ================= serializeArray() -> Json Object ====================
function objToJson(formData){
    let data = formData;
    let obj = {};
    $.each(data, function(idx, ele){
        obj[ele.name] = ele.value;
    });
    return obj;
}



// ================= 파일 업로드 처리함수 ====================
function ajaxFileUpload (csrfHeader, csrfToken){

    let callback =
        $.ajax({
            type       : "post",
            url        : "/file/upload",
            enctype    : 'multipart/form-data',
            async      : false,
            processData: false,
            contentType: false,
            data       : new FormData($("#withFileUploadFrm")[0]),
            beforeSend : xhr  => { xhr.setRequestHeader(csrfHeader, csrfToken) }
        });
    return callback;
};


// ================= 파일 삭제 처리함수 ====================
function ajaxFileDelete (csrfHeader, csrfToken, uploadFileName){

    let callback =
        $.ajax({
            type       : "post",
            url        : "/file/delete",
            async      : false,
            data       : {"uploadFileName":uploadFileName},
            beforeSend : xhr  => { xhr.setRequestHeader(csrfHeader, csrfToken) }
        });
    return callback;
};



/**********************************
            SECURITY
 *********************************/
// ================= Tab 컨트롤 ====================
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



// ================= security Paging 처리  ====================
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



// ================= security formData 셋팅  ====================
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



// ================= security redirect url 셋팅  ====================
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



// ================= security create  ====================
function securityCreateFunc(database){

    let type        = "post";
    let url         = "/admin/security/"+database;
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let redirectUrl = securityRedirectUrlSetting(database);
    let data        = securityFormDataSetting(database);
    let async       = true;
    let isSecurity  = true;

    let callback = ajaxComm(type, data, url, async, csrfHeader, csrfToken);
    callback.done( data => ajaxCallbackProcess(isSecurity, data, type, redirectUrl) );
}



// ================= security update  ====================
function securityUpdateFunc(database){

    let type        = "put";
    let url         = "/admin/security/"+ database +"/" + $("#id").val();
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let redirectUrl = securityRedirectUrlSetting(database);
    let data        = securityFormDataSetting(database);
    let async       = true;
    let isSecurity  = true;

    let callback = ajaxComm(type, data, url, async, csrfHeader, csrfToken);
    callback.done( data => ajaxCallbackProcess(isSecurity, data, type, redirectUrl) );
}



// ================= security delete  ====================
function securityDeleteFunc(database){

    let check = confirm('정말 삭제하시겠습니까');
    if(check) {
        let type        = "delete";
        let url         = "/admin/security/"+ database +"/" + $("#id").val();
        let csrfHeader  = $("#_csrf_header").attr('content');
        let csrfToken   = $("#_csrf").attr('content');
        let redirectUrl = securityRedirectUrlSetting(database);
        let async       = true;
        let isSecurity  = true;

        let callback = ajaxComm(type, "", url, async, csrfHeader, csrfToken);
        callback.done( data => ajaxCallbackProcess(isSecurity, data, type, redirectUrl) );
    }
}






/**********************************
                MEMBER
 *********************************/
// ================= 직분 한글 변경 함수  ====================
function churchOfficerChangeToKor(churchOfficer){

    switch( churchOfficer ){
        case "LAYMAN"              : return '평신도';
        case "DEACON"              : return '집사';
        case "ORDAINED_DEACON"     : return '안수집사';
        case "SENIOR_DEACONESS"    : return '권사';
        case "ELDER"               : return '장로';
        case "JUNIOR_PASTOR"       : return '전도사';
        case "ASSISTANT_PASTOR"    : return '부목사';
        case "SENIOR_PASTOR"       : return '담임목사';
        case "WIFE"                : return '사모';
    };
}


// ================= MemberList 데이터 로드  ====================
function memberListLoad(nowPage){

    let type        = "get";
    let data        = "searchId="+$("#searchId").val()+"&searchData="+$("#searchData").val()+"&nowPage="+nowPage;
    let url         = "/api/members";
    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let async       = false;
    let callback    = ajaxComm(type, data, url, async, csrfHeader, csrfToken);

    callback.done( (data) => {

        totalPages = data.page.totalPages;
        let appendData = "";

        if( data._embedded ) {

            let list = data._embedded.memberList;

            list.forEach(function (data, index) {
                let churchOfficer = churchOfficerChangeToKor(data.churchOfficer);
                appendData += `
                               <tr id="appendItem">
                                    <td style="text-align: left;">
                                        ${data.belong}
                                    </td>
                                    <td style="text-align: left;">
                                        <a href='/admin/members/${data.id}'>
                                            ${data.name}
                                        </a>
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.phoneNumber}
                                    </td>
                                    <td style="text-align: left;">
                                        ${data.birthDay}
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
            }

            $("#searchData").val("");  // 검색내용 삭제
            $("#appendItem").remove(); // 기존 데이터 삭제
            $("#appendPath").append(appendData);

    });
};

// ================ 리스트 검색 함수 ================
function memberSearch(){
    memberListLoad(0);
    pagingProcess();
}


// ================= Member 페이징 처리함수 ====================
let totalPages = "";
function pagingProcess(){

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


// ================= Member Create 함수  ====================
function memberCreate(){

    let csrfHeader  = $("#_csrf_header").attr('content');
    let csrfToken   = $("#_csrf").attr('content');
    let type        = "post";
    let url         = "/api/members";
    let redirectUrl = "/admin/members";
    let async       = true;
    let isSecurity  = false;
    let data        = objToJson($('#withFileUploadFrm').serializeArray());

    if($("#image").val()){
        let fileUploadCallback = ajaxFileUpload(csrfHeader, csrfToken);
        fileUploadCallback.done( uploadData => {
            data.uploadName   = uploadData.uploadName;
            data.originalName = uploadData.originalName;
        });
    }

    let callback = ajaxComm(type, JSON.stringify(data), url, async, csrfHeader, csrfToken);

    callback.done( data => ajaxCallbackProcess(isSecurity, data, type, redirectUrl) );

    callback.fail ( (xhr, status, error) => {

        ajaxFileDelete(csrfHeader, csrfToken, data.uploadName);

        let errorResource = JSON.parse(xhr.responseText).content[0];
        console.log("[ERROR STATUS] : " + xhr.status);
        console.log(errorResource);

        alert(errorResource.defaultMessage);
    });

};



// ================= Member Update View Setting ====================
function memberUpdateViewSetting() {
    $("#name").removeAttr("disabled");
    $("#phoneNumber").removeAttr("disabled");
    $("#gender").removeAttr("disabled");
    $("#birthDay").removeAttr("disabled");
    $("#belong").removeAttr("disabled");
    $("#name").removeAttr("disabled");
    $("#regiDate").removeAttr("disabled");
    $("#churchOfficer").removeAttr("disabled");
    $("#datePicker").removeAttr("disabled");

    $("#showImg").hide();
    $("#memberUpdateView").hide();
    $("#memberDelete").hide();
    $("#memberUpdate").show();
    $("#uploadImg").show();
};