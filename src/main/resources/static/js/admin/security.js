
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

